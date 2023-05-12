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
public class NepChangeGroupMemberTypeMessage extends NepMessageBody {

    private Integer userId;

    private Integer groupOperatorType;

    private Integer groupId;

    private Integer groupMemberId;

    private Integer groupMemberType;


}
