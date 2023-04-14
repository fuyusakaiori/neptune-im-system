package com.fuyusakaiori.nep.im.service.core.group.entity.dto;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString
public class NepJoinedGroup {

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
