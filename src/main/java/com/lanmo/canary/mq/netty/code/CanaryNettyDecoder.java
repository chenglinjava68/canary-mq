package com.lanmo.canary.mq.netty.code;

import com.lanmo.canary.mq.netty.serialize.CanarySerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * @author bo5.wang@56qq.com
 * @version 1.0
 * @desc netty解码
 * @date 2017/4/27
 */
public class CanaryNettyDecoder extends ByteToMessageDecoder{

  private static  Logger logger= LoggerFactory.getLogger(CanaryNettyDecoder.class);

  private final static int MESSAGE_LENGTH=4;

  private CanarySerializer canarySerializer;

  public CanaryNettyDecoder(CanarySerializer canarySerializer){
    this.canarySerializer=canarySerializer;
  }



  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
    // 读取包头长度
    if (in.readableBytes() <MESSAGE_LENGTH) {
      return;
    }

    // 标记Buf可读位置
    in.markReaderIndex();
    int messageLength = in.readInt();

    if (messageLength < 0) {
      ctx.close();
    }

    if (in.readableBytes() < messageLength) {
      // 可读字节长度小于实际包体长度, 则发生读半包, 重置Buf的readerIndex为markedReaderIndex
      in.resetReaderIndex();
    } else {
      byte[] messageBody = new byte[messageLength];
      in.readBytes(messageBody);
      try {
        Object obj = canarySerializer.decode(messageBody);
        out.add(obj);
      } catch (IOException ex) {
        logger.error("socket bytes serialize to object exception", ex);
      }
    }
  }
}
