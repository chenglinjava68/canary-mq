package com.lanmo.canary.mq.netty.code;

import com.lanmo.canary.mq.netty.serialize.CanarySerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author bo5.wang@56qq.com
 * @version 1.0
 * @desc 编码
 * @date 2017/4/27
 */
public class CanaryNettyEncoder extends MessageToByteEncoder<Object>{

    private CanarySerializer canarySerializer;

    public CanaryNettyEncoder (CanarySerializer canarySerializer){
        this.canarySerializer=canarySerializer;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        canarySerializer.encode(out,msg);
    }

    @Override
    public boolean isSharable() {
        //标识不能被共享
        return false;
    }
}
