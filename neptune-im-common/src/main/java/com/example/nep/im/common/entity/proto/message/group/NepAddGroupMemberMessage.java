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
public class NepAddGroupMemberMessage extends NepMessageBody {

    private Integer groupId;

    private Integer groupOwnerId;

    private String groupNumber;

    private String groupName;

    private String groupBriefInfo;

    private String groupAvatarAddress;

    private Integer groupApplyType;

    private boolean mute;

    private long createTime;

    private Integer groupMemberType;

    private String groupMemberNickName;

}
