package com.fuyusakaiori.nep.im.service.core.group.entity;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString
public class NepGroupMember {

    private Integer groupId;

    private Integer groupMemberId;

    private Integer groupMemberType;

    private String groupMemberNickName;

    private Long groupMemberMuteEndTime;

    private Long groupMemberEnterTime;

    private Long groupMemberExitTime;

    private Integer groupMemberEnterType;

    private Integer groupMemberExitType;

    private String extra;
}
