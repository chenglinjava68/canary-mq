package com.lanmo.canary.mq.cluster.broker;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bo5.wang@56qq.com
 * @version 1.0
 * @desc
 * @date 2017/4/24
 */
@Slf4j
public class BrokerServer implements Server {


    private String brokerId;


    /**
     * 开启
     *
     * @throws Exception
     */
    @Override
    public void start() throws Exception {

    }

    /**
     * 关闭
     *
     * @throws Exception
     */
    @Override
    public void shutdown() throws Exception {

    }

    /**
     * 获取服务地址
     *
     * @return
     */
    @Override
    public String getServerAddress() {
        return null;
    }
}
