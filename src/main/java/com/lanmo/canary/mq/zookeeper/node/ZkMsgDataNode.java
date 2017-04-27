package com.lanmo.canary.mq.zookeeper.node;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author bo5.wang@56qq.com
 * @version 1.0
 * @desc
 * @date 2017/4/24
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ZkMsgDataNode {

    // 主题
    private String topic;

    // 存储Broker Server服务地址
    private String brokerServer;

    // Broker Server UUID
    private String brokerId;

    // 当前Topic最大消息序号
    private long currentMsgSequence;

}
