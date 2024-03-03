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
import com.newlandframework.mymq.model.RequestMessage;
import com.newlandframework.mymq.model.ResponseMessage;
import com.newlandframework.mymq.model.MessageSource;
import io.netty.channel.ChannelHandlerContext;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.collections.map.TypedMap;

/**
 * @filename:BrokerStrategyContext.java
 * @description:BrokerStrategyContext功能模块
 * @author beat
 * test
 * @since 2023-8-11
 */
public class BrokerStrategyContext {

    public final static int MyMQProducerMessageStrategy = 1;
    public final static int MyMQConsumerMessageStrategy = 2;
    public final static int MyMQSubscribeStrategy = 3;
    public final static int MyMQUnsubscribeStrategy = 4;

    private RequestMessage request;
    private ResponseMessage response;
    private ChannelHandlerContext channelHandler;
    private ProducerMessageListener hookProducer;
    private ConsumerMessageListener hookConsumer;
    private BrokerStrategy strategy;

    private static Map strategyMap = TypedMap.decorate(new HashMap(), Integer.class, BrokerStrategy.class);

    static {
        strategyMap.put(MyMQProducerMessageStrategy, new BrokerProducerMessageStrategy());
        strategyMap.put(MyMQConsumerMessageStrategy, new BrokerConsumerMessageStrategy());
        strategyMap.put(MyMQSubscribeStrategy, new BrokerSubscribeStrategy());
        strategyMap.put(MyMQUnsubscribeStrategy, new BrokerUnsubscribeStrategy());
    }

    public BrokerStrategyContext(RequestMessage request, ResponseMessage response, ChannelHandlerContext channelHandler) {
        this.request = request;
        this.response = response;
        this.channelHandler = channelHandler;
    }

    public void setHookProducer(ProducerMessageListener hookProducer) {
        this.hookProducer = hookProducer;
    }

    public void setHookConsumer(ConsumerMessageListener hookConsumer) {
        this.hookConsumer = hookConsumer;
    }

    public void invoke() {
        switch (request.getMsgType()) {
            case MyMQMessage:
                strategy = (BrokerStrategy) strategyMap.get(request.getMsgSource() == MessageSource.MyMQProducer ? MyMQProducerMessageStrategy : MyMQConsumerMessageStrategy);
                break;
            case MyMQSubscribe:
                strategy = (BrokerStrategy) strategyMap.get(MyMQSubscribeStrategy);
                break;
            case MyMQUnsubscribe:
                strategy = (BrokerStrategy) strategyMap.get(MyMQUnsubscribeStrategy);
                break;
            default:
                break;
        }

        strategy.setChannelHandler(channelHandler);
        strategy.setHookConsumer(hookConsumer);
        strategy.setHookProducer(hookProducer);
        strategy.messageDispatch(request, response);
    }
}
