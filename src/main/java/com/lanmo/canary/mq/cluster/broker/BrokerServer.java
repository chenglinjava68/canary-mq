package com.lanmo.canary.mq.cluster.broker;

import com.lanmo.canary.mq.common.CanaryConfig;
import com.lanmo.canary.mq.netty.server.CanaryNettyServer;
import com.lanmo.canary.mq.zookeeper.ZkClientContext;
import com.lanmo.canary.mq.zookeeper.ZkConfig;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * @author bo5.wang@56qq.com
 * @version 1.0
 * @desc
 * @date 2017/4/24
 */
@Slf4j
public class BrokerServer implements Server {

    private static final Logger logger= LoggerFactory.getLogger(BrokerServer.class);

    @Getter
    private String brokerId;

    @Getter
    private CanaryNettyServer canaryNettyServer;

    private BrokerConfig brokerConfig;

    private ZkConfig zkConfig;
    @Getter
    private ZkClientContext zkClientContext;

    @Getter
    private ExecutorService executorService;

    public BrokerServer(CanaryConfig mqConfig) {
        this.brokerConfig = new BrokerConfig(mqConfig);
        this.zkConfig = new ZkConfig(mqConfig);
    }

    /**
     * 开启
     *
     * @throws Exception
     */
    @Override
    public void start() throws Exception {

    }


    private void doStartServer(){
        int poolSize=brokerConfig.getBrokerWorkerThreads();
        int queueSize=brokerConfig.getBrokerMQQueueSize();
       //初始化阻塞队列--为线程池 做准备
        BlockingQueue<Runnable> queue;
        if(queueSize==0){
            queue = new LinkedBlockingQueue<>();
        }else {
            queue=new ArrayBlockingQueue<Runnable>(queueSize);
        }
        executorService=new ThreadPoolExecutor(poolSize,poolSize,5,TimeUnit.MINUTES,queue);



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
