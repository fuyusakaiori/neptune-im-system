package com.fuyusakaiori.nep.im.service.core.group.entity.dto;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString
public class NepSimpleGroupMember {

    private Integer groupMemberId;

    private String groupMemberUserName;

    private String groupMemberNickName;

    private String groupMemberRemark;

    private Integer groupMemberType;

    private String groupMemberAvatarAddress;

    private Long groupMemberMuteEndTime;

}
