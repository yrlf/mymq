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
package com.newlandframework.mymq.consumer;

import io.netty.channel.ChannelHandlerContext;
import com.newlandframework.mymq.msg.ConsumerAckMessage;
import com.newlandframework.mymq.model.RequestMessage;
import com.newlandframework.mymq.model.ResponseMessage;
import com.newlandframework.mymq.model.MessageSource;
import com.newlandframework.mymq.core.HookMessageEvent;
import com.newlandframework.mymq.model.MessageType;
import com.newlandframework.mymq.netty.MessageEventWrapper;
import com.newlandframework.mymq.netty.MessageProcessor;

/**
 * @filename:MessageConsumerHandler.java
 * @description:MessageConsumerHandler功能模块
 * @author beat
 * test
 * @since 2023-8-11
 */
public class MessageConsumerHandler extends MessageEventWrapper<Object> {

    private String key;

    public MessageConsumerHandler(MessageProcessor processor) {
        this(processor, null);
        super.setWrapper(this);
    }

    public MessageConsumerHandler(MessageProcessor processor, HookMessageEvent hook) {
        super(processor, hook);
        super.setWrapper(this);
    }

    public void beforeMessage(Object msg) {
        key = ((ResponseMessage) msg).getMsgId();
    }

    public void handleMessage(ChannelHandlerContext ctx, Object msg) {
        if (!factory.traceInvoker(key) && hook != null) {

            ResponseMessage message = (ResponseMessage) msg;
            ConsumerAckMessage result = (ConsumerAckMessage) hook.callBackMessage(message);
            if (result != null) {
                RequestMessage request = new RequestMessage();
                request.setMsgId(message.getMsgId());
                request.setMsgSource(MessageSource.MyMQConsumer);
                request.setMsgType(MessageType.MyMQMessage);
                request.setMsgParams(result);

                ctx.writeAndFlush(request);
            }
        }
    }
}
