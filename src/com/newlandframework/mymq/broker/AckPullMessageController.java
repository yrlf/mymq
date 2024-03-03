/**
 * Copyright (C) 2016 Newland Group Holding Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.newlandframework.mymq.broker;

import com.newlandframework.mymq.msg.ProducerAckMessage;
import com.newlandframework.mymq.core.AckTaskQueue;
import com.newlandframework.mymq.core.ChannelCache;
import com.newlandframework.mymq.core.MessageSystemConfig;
import com.newlandframework.mymq.core.SemaphoreCache;
import com.newlandframework.mymq.model.MessageType;
import com.newlandframework.mymq.model.ResponseMessage;
import com.newlandframework.mymq.model.MessageSource;
import com.newlandframework.mymq.netty.NettyUtil;
import io.netty.channel.Channel;
import java.util.concurrent.Callable;

/**
 * @filename:AckPullMessageController.java
 * @description:AckPullMessageController功能模块
 * @author beat
 * test
 * @since 2023-8-11
 */
public class AckPullMessageController implements Callable<Void> {

    private volatile boolean stoped = false;

    public void stop() {
        stoped = true;
    }

    public boolean isStoped() {
        return stoped;
    }

    public Void call() {
        while (!stoped) {
            SemaphoreCache.acquire(MessageSystemConfig.AckTaskSemaphoreValue);
            ProducerAckMessage ack = AckTaskQueue.getAck();
            String requestId = ack.getAck();
            ack.setAck("");

            Channel channel = ChannelCache.findChannel(requestId);
            if (NettyUtil.validateChannel(channel)) {
                ResponseMessage response = new ResponseMessage();
                response.setMsgId(requestId);
                response.setMsgSource(MessageSource.MyMQBroker);
                response.setMsgType(MessageType.MyMQProducerAck);
                response.setMsgParams(ack);

                channel.writeAndFlush(response);
            }
        }
        return null;
    }
}
