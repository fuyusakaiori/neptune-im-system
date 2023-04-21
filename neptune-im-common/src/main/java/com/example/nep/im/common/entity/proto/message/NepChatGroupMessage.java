package com.example.nep.im.common.entity.proto.message;

import com.example.nep.im.common.entity.proto.NepMessageBody;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ToString
public class NepChatGroupMessage extends NepMessageBody {

    private int messageId;

    private int senderId;

    private int groupId;

    private String avatarAddress;

    private String messageBody;

    private Long messageSendTime;

}
