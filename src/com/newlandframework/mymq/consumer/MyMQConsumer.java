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

import com.google.common.base.Joiner;
import com.newlandframework.mymq.core.MyMQAction;
import com.newlandframework.mymq.core.MessageIdGenerator;
import com.newlandframework.mymq.core.MessageSystemConfig;
import com.newlandframework.mymq.model.MessageType;
import com.newlandframework.mymq.model.RequestMessage;
import com.newlandframework.mymq.msg.SubscribeMessage;
import com.newlandframework.mymq.msg.UnSubscribeMessage;
import com.newlandframework.mymq.netty.MessageProcessor;

/**
 * @filename:MyMQConsumer.java
 * @description:MyMQConsumer功能模块
 * @author beat
 * test
 * @since 2023-8-11
 */
public class MyMQConsumer extends MessageProcessor implements MyMQAction {

    private ProducerMessageHook hook;
    private String brokerServerAddress;
    private String topic;
    private boolean subscribeMessage = false;
    private boolean running = false;
    private String defaultClusterId = "MyMQConsumerClusters";
    private String clusterId = "";
    private String consumerId = "";

    public MyMQConsumer(String brokerServerAddress, String topic, ProducerMessageHook hook) {
        super(brokerServerAddress);
        this.hook = hook;
        this.brokerServerAddress = brokerServerAddress;
        this.topic = topic;
    }

    private void unRegister() {
        RequestMessage request = new RequestMessage();
        request.setMsgType(MessageType.MyMQUnsubscribe);
        request.setMsgId(new MessageIdGenerator().generate());
        request.setMsgParams(new UnSubscribeMessage(consumerId));
        sendSyncMessage(request);
        super.getMessageConnectFactory().close();
        super.closeMessageConnectFactory();
        running = false;
    }

    private void register() {
        RequestMessage request = new RequestMessage();
        request.setMsgType(MessageType.MyMQSubscribe);
        request.setMsgId(new MessageIdGenerator().generate());

        SubscribeMessage subscript = new SubscribeMessage();
        subscript.setClusterId((clusterId.equals("") ? defaultClusterId : clusterId));
        subscript.setTopic(topic);
        subscript.setConsumerId(consumerId);

        request.setMsgParams(subscript);

        sendAsynMessage(request);
    }

    public void init() {
        super.getMessageConnectFactory().setMessageHandle(new MessageConsumerHandler(this, new ConsumerHookMessageEvent(hook)));
        Joiner joiner = Joiner.on(MessageSystemConfig.MessageDelimiter).skipNulls();
        consumerId = joiner.join((clusterId.equals("") ? defaultClusterId : clusterId), topic, new MessageIdGenerator().generate());
    }

    public void start() {
        if (isSubscribeMessage()) {
            super.getMessageConnectFactory().connect();
            register();
            running = true;
        }
    }

    public void receiveMode() {
        setSubscribeMessage(true);
    }

    public void shutdown() {
        if (running) {
            unRegister();
        }
    }

    public String getBrokerServerAddress() {
        return brokerServerAddress;
    }

    public void setBrokerServerAddress(String brokerServerAddress) {
        this.brokerServerAddress = brokerServerAddress;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public boolean isSubscribeMessage() {
        return subscribeMessage;
    }

    public void setSubscribeMessage(boolean subscribeMessage) {
        this.subscribeMessage = subscribeMessage;
    }

    public String getDefaultClusterId() {
        return defaultClusterId;
    }

    public void setDefaultClusterId(String defaultClusterId) {
        this.defaultClusterId = defaultClusterId;
    }

    public String getClusterId() {
        return clusterId;
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId;
    }
}
