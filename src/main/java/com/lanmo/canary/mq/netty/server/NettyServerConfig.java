package com.lanmo.canary.mq.netty.server;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import lombok.Data;

import java.util.Map;
import java.util.concurrent.ThreadFactory;

/**
 * @author bo5.wang@56qq.com
 * @version 1.0
 * @desc 服务端配置
 * @date 2017/4/24
 */
@Data
public class NettyServerConfig {
    /**
     * 主线程工厂--构建接受请求的线程
     */
    public ThreadFactory bossThreadFactory;

    /**
     * 工作线程工厂--构建工作线程
     */
    public ThreadFactory workerThreadFactory;

    /**
     * 处理链
     */
    public Map<ChannelOption, Object> options;

    public Map<ChannelOption, Object> childOptions;

    public String host;

    public int port;

    public int socketThreads;

    public ChannelHandler channelHandler;

    public ChannelHandler childChannelHandler;

    public boolean ePoll;
}
