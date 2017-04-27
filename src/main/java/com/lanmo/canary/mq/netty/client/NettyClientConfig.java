package com.lanmo.canary.mq.netty.client;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import lombok.Data;

import java.util.Map;
import java.util.concurrent.ThreadFactory;

/**
 * @author bo5.wang@56qq.com
 * @version 1.0
 * @desc netty client的基本配置
 * @date 2017/4/27
 */
@Data
public class NettyClientConfig {
    private boolean ePool;
    private int socketThreads;
    private ThreadFactory clientThreadFactory;
    private Map<ChannelOption,Object> options;
    private ChannelHandler channelHandler;
    private String remoteAddress;
}
