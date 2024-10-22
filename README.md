
* MyMQ基于Java语言进行编写，网络通讯依赖Netty。
* 若干个消费者可以组成一个消费者集群，生产者可以向这个消费者集群投递消息。并且每个消费者只消费自己关注的生产者发送的消息。
* 支持动态新增、删除生产者、消费者。
* 消息投递支持负载均衡策略。
* 消息处理转发服务器（MyMQServerStartup）基于异步多线程消息队列进行组织架构，并且和Spring无缝集成。
* 生产者、消费者测试用例参考：com.newlandframework.mymq.test包。
* 启动MyMQ的消息处理转发服务器（MyMQServerStartup）之后，才能进行分布式消息队列系统的消息路由、传递。
* 基于Netty的主从事件线程池模型，网络传输中的消息序列化采用Kryo序列化框架，进一步提升消息序列化性能。
* Broker消息的投递，目前支持严格的消息顺序。其中Broker还支持消息的缓冲派发，即Broker会缓存一定数量的消息之后，再批量分配给对此消息感兴趣的消费者。

## MyMQ In Action
**Netty构建分布式消息队列（MyMQ）设计指南之架构篇**

**Architecture of Netty to build a distributed message queue system (MyMQ) design guide**

http://www.cnblogs.com/jietang/p/5808735.html

**Netty构建分布式消息队列实现原理浅析**

**Analysis of the principle of distributed message queue implementation by Netty**

http://www.cnblogs.com/jietang/p/5847458.html

----------
## Usage at a glance
This simple example for MyMQ Producer:
~~~~~~~~~~java
package com.newlandframework.mymq.test;

import com.newlandframework.mymq.msg.Message;
import com.newlandframework.mymq.msg.ProducerAckMessage;
import com.newlandframework.mymq.producer.MyMQProducer;
import org.apache.commons.lang3.StringUtils;

public class MyMQProducer {

    public static void main(String[] args) throws InterruptedException {
        MyMQProducer producer = new MyMQProducer("127.0.0.1:18888", "MyMQ-Topic-1");
        producer.setClusterId("MyMQCluster");
        producer.init();
        producer.start();

        System.out.println(StringUtils.center("MyMQProducer 消息发送开始", 50, "*"));

        for (int i = 0; i < 1; i++) {
            Message message = new Message();
            String str = "Hello MyMQ From Producer1[" + i + "]";
            message.setBody(str.getBytes());
            ProducerAckMessage result = producer.delivery(message);
            if (result.getStatus() == (ProducerAckMessage.SUCCESS)) {
                System.out.printf("MyMQProducer 发送消息编号:%s\n", result.getMsgId());
            }

            Thread.sleep(100);
        }

        producer.shutdown();
        System.out.println(StringUtils.center("MyMQProducer 消息发送完毕", 50, "*"));
    }
}
~~~~~~~~~~
This simple example for MyMQ Consumer:
~~~~~~~~~~java
package com.newlandframework.mymq.test;

import com.newlandframework.mymq.consumer.MyMQConsumer;
import com.newlandframework.mymq.consumer.ProducerMessageHook;
import com.newlandframework.mymq.msg.ConsumerAckMessage;
import com.newlandframework.mymq.msg.Message;

public class MyMQConsumer {

    private static ProducerMessageHook hook = new ProducerMessageHook() {
        public ConsumerAckMessage hookMessage(Message message) {
            System.out.printf("MyMQConsumer 收到消息编号:%s,消息内容:%s\n", message.getMsgId(), new String(message.getBody()));
            ConsumerAckMessage result = new ConsumerAckMessage();
            result.setStatus(ConsumerAckMessage.SUCCESS);
            return result;
        }
    };

    public static void main(String[] args) {
        MyMQConsumer consumer = new MyMQConsumer("127.0.0.1:18888", "MyMQ-Topic-1", hook);
        consumer.init();
        consumer.setClusterId("MyMQCluster");
        consumer.receiveMode();
        consumer.start();
    }
}
~~~~~~~~~~
----------
