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

import com.newlandframework.mymq.msg.Message;
import com.newlandframework.mymq.msg.ProducerAckMessage;
import com.newlandframework.mymq.producer.MyMQProducer;
import org.apache.commons.lang3.StringUtils;

/**
 * @filename:MyMQProducer1.java
 * @description:MyMQProducer1功能模块
 * @author beat
 * test
 * @since 2023-8-11
 */
public class MyMQProducer1 {

    public static void main(String[] args) throws InterruptedException {
        MyMQProducer producer = new MyMQProducer("127.0.0.1:18888", "MyMQ-Topic-1");
        producer.setClusterId("MyMQCluster");
        producer.init();
        producer.start();

        System.out.println(StringUtils.center("MyMQProducer1 消息发送开始", 50, "*"));

        for (int i = 0; i < 1; i++) {
            Message message = new Message();
            String str = "Hello MyMQ From Producer1[" + i + "]";
            message.setBody(str.getBytes());
            ProducerAckMessage result = producer.delivery(message);
            if (result.getStatus() == (ProducerAckMessage.SUCCESS)) {
                System.out.printf("MyMQProducer1 发送消息编号:%s\n", result.getMsgId());
            }

            Thread.sleep(100);
        }

        producer.shutdown();
        System.out.println(StringUtils.center("MyMQProducer1 消息发送完毕", 50, "*"));
    }
}
