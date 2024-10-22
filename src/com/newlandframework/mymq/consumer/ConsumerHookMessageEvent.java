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

import com.newlandframework.mymq.core.HookMessageEvent;
import com.newlandframework.mymq.model.ResponseMessage;
import com.newlandframework.mymq.msg.BaseMessage;
import com.newlandframework.mymq.msg.ConsumerAckMessage;
import com.newlandframework.mymq.msg.Message;

/**
 * @filename:ConsumerHookMessageEvent.java
 * @description:ConsumerHookMessageEvent功能模块
 * @author beat
 * test
 * @since 2023-8-11
 */
public class ConsumerHookMessageEvent extends HookMessageEvent<Object> {

    private ProducerMessageHook hook;

    public ConsumerHookMessageEvent(ProducerMessageHook hook) {
        this.hook = hook;
    }

    public Object callBackMessage(Object obj) {
        ResponseMessage response = (ResponseMessage) obj;
        if (response.getMsgParams() instanceof Message) {
            ConsumerAckMessage result = hook.hookMessage((Message) response.getMsgParams());
            result.setMsgId(((Message) response.getMsgParams()).getMsgId());
            return result;
        } else {
            return null;
        }
    }
}
