package com.fuyusakaiori.nep.im.service.core.message.entity.dto;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString
public class NepChatP2PHistoryMessage {

    private Long messageKey;

    private Integer messageSenderId;

    private Integer messageReceiverId;

    private Long messageSequence;

    private Long messageSendTime;

    private String messageBody;

}
