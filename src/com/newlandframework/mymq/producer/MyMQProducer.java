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
package com.newlandframework.mymq.producer;

import com.newlandframework.mymq.core.MyMQAction;
import com.newlandframework.mymq.model.MessageSource;
import com.newlandframework.mymq.model.MessageType;
import com.newlandframework.mymq.model.RequestMessage;
import com.newlandframework.mymq.model.ResponseMessage;
import com.newlandframework.mymq.msg.Message;
import com.newlandframework.mymq.msg.ProducerAckMessage;
import com.newlandframework.mymq.netty.MessageProcessor;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @filename:MyMQProducer.java
 * @description:MyMQProducer功能模块
 * @author beat
 * test
 * @since 2023-8-11
 */
public class MyMQProducer extends MessageProcessor implements MyMQAction {

    private boolean brokerConnect = false;
    private boolean running = false;
    private String brokerServerAddress;
    private String topic;
    private String defaultClusterId = "MyMQProducerClusters";
    private String clusterId = "";
    private AtomicLong msgId = new AtomicLong(0L);

    public MyMQProducer(String brokerServerAddress, String topic) {
        super(brokerServerAddress);
        this.brokerServerAddress = brokerServerAddress;
        this.topic = topic;
    }

    private ProducerAckMessage checkMode() {
        if (!brokerConnect) {
            ProducerAckMessage ack = new ProducerAckMessage();
            ack.setStatus(ProducerAckMessage.FAIL);
            return ack;
        }

        return null;
    }

    public void start() {
        super.getMessageConnectFactory().connect();
        brokerConnect = true;
        running = true;
    }

    public void init() {
        ProducerHookMessageEvent hook = new ProducerHookMessageEvent();
        hook.setBrokerConnect(brokerConnect);
        hook.setRunning(running);
        super.getMessageConnectFactory().setMessageHandle(new MessageProducerHandler(this, hook));
    }

    public ProducerAckMessage delivery(Message message) {
        if (!running || !brokerConnect) {
            return checkMode();
        }

        message.setTopic(topic);
        message.setTimeStamp(System.currentTimeMillis());

        RequestMessage request = new RequestMessage();
        request.setMsgId(String.valueOf(msgId.incrementAndGet()));
        request.setMsgParams(message);
        request.setMsgType(MessageType.MyMQMessage);
        request.setMsgSource(MessageSource.MyMQProducer);
        message.setMsgId(request.getMsgId());

        ResponseMessage response = (ResponseMessage) sendAsynMessage(request);
        if (response == null) {
            ProducerAckMessage ack = new ProducerAckMessage();
            ack.setStatus(ProducerAckMessage.FAIL);
            return ack;
        }

        ProducerAckMessage result = (ProducerAckMessage) response.getMsgParams();
        return result;
    }

    public void shutdown() {
        if (running) {
            running = false;
            super.getMessageConnectFactory().close();
            super.closeMessageConnectFactory();
        }
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getClusterId() {
        return clusterId;
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId;
    }
}
