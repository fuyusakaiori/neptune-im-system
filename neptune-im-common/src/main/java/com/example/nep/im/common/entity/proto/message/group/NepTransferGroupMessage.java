package com.example.nep.im.common.entity.proto.message.group;

import com.example.nep.im.common.entity.proto.NepMessageBody;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ToString
public class NepTransferGroupMessage extends NepMessageBody {

    private Integer groupId;

    private Integer targetOwnerId;

    private Integer groupMemberType;

}
