package com.lanmo.canary.mq.msg;

import java.io.Serializable;

/**
 * @author bo5.wang@56qq.com
 * @version 1.0
 * @desc 消息基础类
 * @date 2017/4/20
 */
public interface Message extends Serializable {

    public String getMsgId();
}
