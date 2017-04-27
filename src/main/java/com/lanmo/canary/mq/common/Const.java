package com.lanmo.canary.mq.common;

/**
 * @author bo5.wang@56qq.com
 * @version 1.0
 * @desc
 * @date 2017/4/27
 */
public class Const {

    // Broker Server注册节点路径
    public static final String ZK_BROKER_PATH = "/canaryMQ/broker";

    // 主题消息注册节点路径
    public static final String ZK_TOPIC_META_PATH = "/canaryMQ/topic";

    // 主题消息存储Broker节点路径
    public static final String ZK_MSG_DATA_PATH = "/canaryMQ/message";

    // 主题消息订阅节点路径
    public static final String ZK_MSG_CONSUME_PATH = "/canaryMQ/consume";

    public static final String ZK_MQ_LOCK_PATH = "/canaryMQ/lock";
}
