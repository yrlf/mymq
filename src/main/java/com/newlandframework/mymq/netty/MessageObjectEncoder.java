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
package com.newlandframework.mymq.netty;

import com.newlandframework.mymq.serialize.MessageCodecUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @filename:MessageObjectEncoder.java
 * @description:MessageObjectEncoder功能模块
 * 
 * 
 * 
 */
public class MessageObjectEncoder extends MessageToByteEncoder<Object> {

    private MessageCodecUtil util = null;

    public MessageObjectEncoder(final MessageCodecUtil util) {
        this.util = util;
    }

    protected void encode(final ChannelHandlerContext ctx, final Object msg, final ByteBuf out) throws Exception {
        util.encode(out, msg);
    }
}
