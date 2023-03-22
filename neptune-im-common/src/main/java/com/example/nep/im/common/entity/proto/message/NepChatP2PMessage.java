package com.example.nep.im.common.entity.proto.message;

import com.example.nep.im.common.entity.proto.NepMessageBody;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ToString
public class NepChatP2PMessage extends NepMessageBody{

    private int messageId;

    private int fromUserId;

    private int toUserId;

    private String messageBody;

}
