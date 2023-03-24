package com.fuyusakaiori.nep.im.service.core.message.entity;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString
public class NepChatGroupMessageHeader {

    private Long messageKey;

    private Integer messageSenderId;

    private Integer messageReceiverGroupId;

    private Long messageSequence;

    private Long messageSendTime;

    private Long messageCreateTime;

}
