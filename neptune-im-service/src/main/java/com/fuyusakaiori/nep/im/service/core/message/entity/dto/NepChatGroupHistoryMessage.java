package com.fuyusakaiori.nep.im.service.core.message.entity.dto;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString
public class NepChatGroupHistoryMessage {

    private Long messageKey;

    private Integer messageSenderId;

    private String avatarAddress;

    private Integer messageReceiverGroupId;

    private Long messageSequence;

    private Long messageSendTime;

    private String messageBody;
}
