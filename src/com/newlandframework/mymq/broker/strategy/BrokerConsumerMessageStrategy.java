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
package com.newlandframework.mymq.broker.strategy;

import com.newlandframework.mymq.broker.ConsumerMessageListener;
import com.newlandframework.mymq.broker.ProducerMessageListener;
import com.newlandframework.mymq.broker.SendMessageLauncher;
import com.newlandframework.mymq.core.CallBackInvoker;
import com.newlandframework.mymq.model.RequestMessage;
import com.newlandframework.mymq.model.ResponseMessage;
import io.netty.channel.ChannelHandlerContext;

/**
 * @filename:BrokerConsumerMessageStrategy.java
 * @description:BrokerConsumerMessageStrategy功能模块
 * @author beat
 * test
 * @since 2023-8-11
 */
public class BrokerConsumerMessageStrategy implements BrokerStrategy {

    public BrokerConsumerMessageStrategy() {

    }

    public void messageDispatch(RequestMessage request, ResponseMessage response) {
        String key = response.getMsgId();
        if (SendMessageLauncher.getInstance().trace(key)) {
            CallBackInvoker<Object> future = SendMessageLauncher.getInstance().detach(key);
            if (future == null) {
                return;
            } else {
                future.setMessageResult(request);
            }
        } else {
            return;
        }
    }

    public void setHookProducer(ProducerMessageListener hookProducer) {

    }

    public void setHookConsumer(ConsumerMessageListener hookConsumer) {

    }

    public void setChannelHandler(ChannelHandlerContext channelHandler) {

    }
}
