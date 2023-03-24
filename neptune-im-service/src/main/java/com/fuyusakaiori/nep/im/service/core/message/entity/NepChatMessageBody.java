package com.fuyusakaiori.nep.im.service.core.message.entity;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString
public class NepChatMessageBody {

    /**
     * <h3>消息唯一索引: 关联消息头</h3>
     */
    private Long messageKey;

    /**
     *<h3>消息内容: 采用 JSON 存储</h3>
     */
    private String messageBody;

    /**
     * <h3>消息发送时间</h3>
     */
    private Long messageSendTime;

    /**
     * <h3>消息持久化时间</h3>
     */
    private Long messageCreateTime;

    /**
     * <h3>消息是否被删除</h3>
     */
    private boolean delete;

}
