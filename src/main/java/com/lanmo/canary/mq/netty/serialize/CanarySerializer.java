package com.lanmo.canary.mq.netty.serialize;

import io.netty.buffer.ByteBuf;

import java.io.IOException;


/**
 * @author bo5.wang@56qq.com
 * @version 1.0
 * @desc 序列化基础类
 * @date 2017/4/24
 */
public interface CanarySerializer {

   /**
    * 编码
    * @param out
    * @param message
    * @throws IOException
    */
   public void encode(final ByteBuf out, final Object message) throws IOException;

}
