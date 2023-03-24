package com.fuyusakaiori.nep.im.service.core.message.entity;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString
public class NepChatP2PMessageHeader {

    private Integer messageOwnerId;

    /**
     * <h3>消息 ID: 关联消息体</h3>
     */
    private Long messageKey;

    /**
     * <h3>消息发送者 ID</h3>
     */
    private Integer messageSenderId;

    /**
     * <h3>消息接收者 ID</h3>
     */
    private Integer messageReceiverId;

    /**
     * <h3>消息序列号</h3>
     */
    private Long messageSequence;

    /**
     * <h3>消息发送时间</h3>
     */
    private Long messageSendTime;

    /**
     * <h3>消息队列持久化时间</h3>
     */
    private Long messageCreateTime;
}
