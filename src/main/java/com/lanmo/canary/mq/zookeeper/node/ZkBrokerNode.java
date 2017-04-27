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
public class ZkBrokerNode {

    // Broker Server服务地址[host:port]
    private String brokerAddress;

    // Broker Server ID
    private String brokerId;

}
