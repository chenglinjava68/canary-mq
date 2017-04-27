package com.lanmo.canary.mq.netty.client;

import com.lanmo.canary.mq.common.NettyUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author bo5.wang@56qq.com
 * @version 1.0
 * @desc netty client
 * @date 2017/4/27
 */
public class CanaryNettyClient {

    //netty socket thread 管理组
    private EventLoopGroup eventLoopGroup;

    private Channel channel;
    //client的配置
    private NettyClientConfig config;
    //启动标识
    private AtomicBoolean started=new AtomicBoolean(false);


    //启动client
    public void start(){
        eventLoopGroup= NettyUtils.createEventLoopGroup(config.isEPool(),config.getSocketThreads(),config.getClientThreadFactory());

        Bootstrap bootstrap=new Bootstrap();
        bootstrap.group(eventLoopGroup).channel(NettyUtils.getClientChannelClass(config.isEPool()))
                .handler(config.getChannelHandler());

        if(config.getOptions()!=null){
            for (Map.Entry<ChannelOption, Object> entry : config.getOptions().entrySet()) {
                bootstrap.option(entry.getKey(), entry.getValue());
            }
        }

        ChannelFuture channelFuture=bootstrap.connect(NettyUtils.getInetSocketAddress(config.getRemoteAddress()));

        channelFuture.addListeners(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                //说明已经启动了
                started.set(true);
                channel=future.channel();
            }
        });
    }

    //写数据
    public ChannelFuture writeAndFlush(Object object) {
        if (!isStarted()) {
            throw new IllegalStateException("client not started !");
        }
        return channel.writeAndFlush(object);
    }

    public void addChannelHandler(String handlerName, ChannelHandler channelHandler) {
        if (channelHandler == null) {
            throw new IllegalArgumentException("channelHandler is null");
        }
        assert isStarted();
        if (channel.pipeline().get(handlerName) == null) {
            channel.pipeline().addLast(handlerName, channelHandler);
        }
    }

    public boolean isStarted() {
        return started.get();
    }

    public void stop(int await, TimeUnit unit) throws InterruptedException {
        started.set(false);
        if (channel != null) {
            channel.closeFuture().sync();
        }
        if (eventLoopGroup != null) {
            eventLoopGroup.shutdownGracefully(await, await, unit);
        }
    }
}
