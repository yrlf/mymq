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
package com.newlandframework.mymq.spring;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @filename:MyMQContainer.java
 * @description:MyMQContainer功能模块
 * 
 * 
 * 
 */
public class MyMQContainer implements Container {

    public static final String MyMQConfigFilePath = "mymq-broker.xml";

    private MyMQContext springContext;

    public void start() {
        AbstractApplicationContext context = new ClassPathXmlApplicationContext(MyMQConfigFilePath);
        springContext = new MyMQContext(context);
        context.start();
    }

    public void stop() {
        if (null != springContext && null != springContext.get()) {
            springContext.get().close();
            springContext = null;
        }
    }

    public MyMQContext getContext() {
        return springContext;
    }
}
