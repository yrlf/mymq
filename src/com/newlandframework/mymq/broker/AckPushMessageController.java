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
package com.newlandframework.mymq.broker;

import com.newlandframework.mymq.core.AckMessageCache;
import com.newlandframework.mymq.core.MessageSystemConfig;
import java.util.concurrent.Callable;

/**
 * @filename:AckPushMessageController.java
 * @description:AckPushMessageController功能模块
 * @author beat
 * test
 * @since 2023-8-11
 */
public class AckPushMessageController implements Callable<Void> {

    private volatile boolean stoped = false;

    public Void call() {
        AckMessageCache ref = AckMessageCache.getAckMessageCache();
        int timeout = MessageSystemConfig.AckMessageControllerTimeOutValue;
        while (!stoped) {
            if (ref.hold(timeout)) {
                ref.commit();
            }
        }
        return null;
    }

    public void stop() {
        stoped = true;
    }

    public boolean isStoped() {
        return stoped;
    }
}
