package com.lanmo.canary.mq.cluster.broker;

import com.lanmo.canary.mq.common.CanaryConfig;
import com.lanmo.canary.mq.common.Utils;
import lombok.Data;

/**
 * @author bo5.wang@56qq.com
 * @version 1.0
 * @desc 读取配置文件 获取配置信息
 * @date 2017/4/20
 */
@Data
public class BrokerConfig {


    private CanaryConfig canaryConfig;

    public BrokerConfig(CanaryConfig canaryConfig){
        this.canaryConfig=canaryConfig;
    }

    public String getBrokerHost() {
        assert canaryConfig != null;
        return canaryConfig.getString(BrokerConstant.BROKER_SERVER_HOST, Utils.getIpV4());
    }

    public int getBrokerPort() {
        assert canaryConfig != null;
        return canaryConfig.getInt(BrokerConstant.BROKER_SERVER_PORT, 0);
    }

    public int getSocketThreads() {
        assert canaryConfig != null;
        return canaryConfig.getInt(BrokerConstant.BROKER_SERVER_SOCKET_IO_THREADS, Runtime.getRuntime().availableProcessors());
    }

    public int getSocketRcvBuf() {
        assert canaryConfig != null;
        return canaryConfig.getInt(BrokerConstant.BROKER_SERVER_SOCKET_RCV_BUFFER, 1024);
    }

    public int getSocketSndBuf() {
        assert canaryConfig != null;
        return canaryConfig.getInt(BrokerConstant.BROKER_SERVER_SOCKET_SND_BUFFER, 1024);
    }

    public int getBrokerMQQueueSize() {
        assert canaryConfig != null;
        return canaryConfig.getInt(BrokerConstant.BROKER_SERVER_MQ_QUEUE_SIZE, 0);
    }

    public int getBrokerWorkerThreads() {
        assert canaryConfig != null;
        return canaryConfig.getInt(BrokerConstant.BROKER_SERVER_MQ_WORKER_THREAD, Runtime.getRuntime().availableProcessors() * 2);
    }



}
