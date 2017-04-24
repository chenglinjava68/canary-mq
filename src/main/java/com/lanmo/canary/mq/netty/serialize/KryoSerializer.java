package com.lanmo.canary.mq.netty.serialize;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.google.common.io.Closer;
import io.netty.buffer.ByteBuf;

import java.io.*;

/**
 * @author bo5.wang@56qq.com
 * @version 1.0
 * @desc
 * @date 2017/4/24
 */
public class KryoSerializer implements CanarySerializer {


    private Kryo kryo;

    private Closer closer;

    public KryoSerializer(Class<?> ... classes) {
        closer = Closer.create();
        kryo = new Kryo();
        for (Class c: classes) {
            kryo.register(c);
        }
    }

    /**
     * 编码
     *
     * @param out
     * @param message
     * @throws IOException
     */
    @Override
    public void encode(ByteBuf out, Object message) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            serialize(byteArrayOutputStream, message);
            byte[] body = byteArrayOutputStream.toByteArray();
            int dataLength = body.length;
            // 数据包: 数据包头 + 数据包体
            out.writeInt(dataLength);
            out.writeBytes(body);
            closer.register(byteArrayOutputStream);
        } finally {
            closer.close();
        }
    }

    private void serialize(OutputStream output, Object object) throws IOException {
        try {
            Output out = new Output(output);
            closer.register(out);
            closer.register(output);
            kryo.writeClassAndObject(out, object);
        } finally {
            closer.close();
        }
    }
    /**
     * 解码
     *
     * @param body
     * @return
     * @throws IOException
     */
    @Override
    public Object decode(byte[] body) throws IOException {
        ByteArrayInputStream byteArrayInputStream = null;
        try {
            byteArrayInputStream = new ByteArrayInputStream(body);
            return deserialize(byteArrayInputStream);
        } finally {
            byteArrayInputStream.close();
        }
    }

    private Object deserialize(InputStream input) throws IOException {
        try {
            Input in = new Input(input);
            closer.register(in);
            closer.register(input);
            Object result = kryo.readClassAndObject(in);
            return result;
        } finally {
            closer.close();
        }
    }
}

