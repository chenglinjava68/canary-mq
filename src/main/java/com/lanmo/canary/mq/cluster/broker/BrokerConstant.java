package com.lanmo.canary.mq.cluster.broker;

/**
 * @author bo5.wang@56qq.com
 * @version 1.0
 * @desc boker的常量
 * @date 2017/4/24
 */
public class BrokerConstant {

    /**
     * broker server服务地址
     */
    public static final String BROKER_SERVER_HOST="canary.broker.server.host";
    /**
     *  broker server服务端口
     */
    public static final String BROKER_SERVER_PORT="canary.broker.server.port";

    /**
     * Broker Server Socket接收缓冲区
     */
    public static final String BROKER_SERVER_SOCKET_RCV_BUFFER = "canary.broker.server.socket.rcv.buf";
    /**
     * Broker Server Socket发送缓冲区
     */
    public static final String BROKER_SERVER_SOCKET_SND_BUFFER = "canary.broker.server.socket.snd.buf";
    /**
     * Broker Server Socket IO线程数[Netty WorkerBossGroup线程数]
     */
    public static final String BROKER_SERVER_SOCKET_IO_THREADS = "canary.broker.server.socket.io.threads";
    /**
     * Broker Server业务线程数
     */
    public static final String BROKER_SERVER_MQ_WORKER_THREAD = "canary.broker.server.mq.worker.threads";
    /**
     * Broker Server业务线程任务队列
     */
    public static final String BROKER_SERVER_MQ_QUEUE_SIZE = "canary.broker.server.mq.queue.size";

}
