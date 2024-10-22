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
package com.newlandframework.mymq.model;

/**
 * @filename:MessageSource.java
 * @description:MessageSource功能模块
 * @author beat
 * test
 * @since 2023-8-11
 */
public enum MessageSource {

    MyMQConsumer(1),
    MyMQBroker(2),
    MyMQProducer(3);

    private int source;

    private MessageSource(int source) {
        this.source = source;
    }

    public int getSource() {
        return source;
    }
}
