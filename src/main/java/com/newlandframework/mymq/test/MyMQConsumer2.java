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
package com.newlandframework.mymq.test;

import com.newlandframework.mymq.consumer.MyMQConsumer;
import com.newlandframework.mymq.consumer.ProducerMessageHook;
import com.newlandframework.mymq.msg.ConsumerAckMessage;
import com.newlandframework.mymq.msg.Message;

/**
 * @filename:MyMQConsumer2.java
 * @description:MyMQConsumer2功能模块
 * 
 * 
 * 
 */
public class MyMQConsumer2 {

    private static ProducerMessageHook hook = new ProducerMessageHook() {
        public ConsumerAckMessage hookMessage(Message message) {
            System.out.printf("MyMQConsumer2 收到消息编号:%s,消息内容:%s\n", message.getMsgId(), new String(message.getBody()));
            ConsumerAckMessage result = new ConsumerAckMessage();
            result.setStatus(ConsumerAckMessage.SUCCESS);
            return result;
        }
    };

    public static void main(String[] args) {
        MyMQConsumer consumer = new MyMQConsumer("127.0.0.1:18888", "MyMQ-Topic-2", hook);
        consumer.init();
        consumer.setClusterId("MyMQCluster2");
        consumer.receiveMode();
        consumer.start();
    }
}
