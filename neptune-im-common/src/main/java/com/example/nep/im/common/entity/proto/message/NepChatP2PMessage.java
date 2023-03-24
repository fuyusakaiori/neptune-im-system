package com.example.nep.im.common.entity.proto.message;

import com.example.nep.im.common.entity.proto.NepMessageBody;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ToString
public class NepChatP2PMessage extends NepMessageBody{

    private int messageId;

    private int senderId;

    private int receiverId;

    private String messageBody;

    private Long messageSendTime;

}
