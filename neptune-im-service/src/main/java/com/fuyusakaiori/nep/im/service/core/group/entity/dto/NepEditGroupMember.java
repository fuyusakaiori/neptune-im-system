package com.fuyusakaiori.nep.im.service.core.group.entity.dto;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString
public class NepEditGroupMember {

    private Integer groupId;

    private Integer groupMemberId;

    private Integer groupMemberType;

    private String groupMemberNickName;

}
