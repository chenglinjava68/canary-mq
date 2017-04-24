package com.lanmo.canary.mq.netty.server;

import com.lanmo.canary.mq.common.NettyUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import lombok.Data;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author bo5.wang@56qq.com
 * @version 1.0
 * @desc
 * @date 2017/4/24
 */
@Data
public class CanaryNettyServer {

    private NettyServerConfig config;

    private EventLoopGroup bossGroup;

    private EventLoopGroup workerGroup;

    private ChannelFuture channelFuture;

    private AtomicBoolean started=new AtomicBoolean(false);

    private InetSocketAddress socketAddress;

    public void start(){
        //主线程--接受请求
        bossGroup= NettyUtils.createEventLoopGroup(config.isEPoll(),1,config.getBossThreadFactory());
       //工作线程--处理请求
        workerGroup=NettyUtils.createEventLoopGroup(config.isEPoll(),config.getSocketThreads(),config.getWorkerThreadFactory());

        //netty
        ServerBootstrap bootstrap=new ServerBootstrap();
        bootstrap.group(bossGroup,workerGroup).channel(NettyUtils.getServerChannelClass(config.isEPoll()))
                .childHandler(config.getChildChannelHandler());

        //添加boss的处理链
        if (config.getOptions() != null) {
            for (Map.Entry<ChannelOption, Object> entry : config.getOptions().entrySet()) {
                bootstrap.option(entry.getKey(), entry.getValue());
            }
        }

        /**
         * 添加工作线程的处理链
         */
        if (config.getChildOptions() != null) {
            for (Map.Entry<ChannelOption, Object> entry : config.getChildOptions().entrySet()) {
                bootstrap.childOption(entry.getKey(), entry.getValue());
            }
        }

        channelFuture=bootstrap.bind(new InetSocketAddress(config.getHost(),config.getPort()));

        //监听时候成功开启服务
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
               if(future.channel().isActive()){
                   started.set(true);
                   //返回绑定的本地地址
                   socketAddress= (InetSocketAddress) future.channel().localAddress();
               }
            }
        });
         //future模式
        channelFuture.syncUninterruptibly();
    }

    /**
     * 是否开启服务
     * @return
     */
    public boolean isServing(){
        return started.get();
    }

    public void stop(int awaitTime, TimeUnit timeUnit){
        if (started.get()) {
            started.set(false);
        }
        if (channelFuture != null) {
            channelFuture.channel().closeFuture().awaitUninterruptibly(awaitTime, timeUnit);
            channelFuture = null;
        }
        if (bossGroup != null) {
            bossGroup.shutdownGracefully(awaitTime, awaitTime, timeUnit);
            bossGroup = null;
        }

        if (workerGroup != null) {
            workerGroup.shutdownGracefully(awaitTime, awaitTime, timeUnit);
            workerGroup = null;
        }
    }

    public void blockUntilStarted(long seconds) throws InterruptedException {
        synchronized (this) {
            long maxWaitTimeMs = TimeUnit.MILLISECONDS.convert(seconds, TimeUnit.SECONDS);
            wait(maxWaitTimeMs);
        }
    }


}
