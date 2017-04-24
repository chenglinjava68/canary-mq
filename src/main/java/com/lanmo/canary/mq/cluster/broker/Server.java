package com.lanmo.canary.mq.cluster.broker;

/**
 * @author bo5.wang@56qq.com
 * @version 1.0
 * @desc 服务的基础接口
 * @date 2017/4/24
 */
public interface Server {

    /**
     * 开启
     * @throws Exception
     */
    public void start() throws Exception;

    /**
     * 关闭
     * @throws Exception
     */
    public void  shutdown() throws Exception;

    /**
     * 获取服务地址
     * @return
     */
    public String getServerAddress();

}
