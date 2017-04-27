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
public class ZkConsumeMetaNode {

    private String topic;

    private String topicGroup;

    private String consumeAddress;

}
