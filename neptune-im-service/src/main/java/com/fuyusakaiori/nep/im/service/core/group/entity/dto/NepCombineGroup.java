package com.fuyusakaiori.nep.im.service.core.group.entity.dto;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@ToString
public class NepCombineGroup {

    private Integer groupId;

    private Integer groupOwnerId;

    private String groupNumber;

    private String groupName;

    private String groupBriefInfo;

    private String groupAvatarAddress;

    private Integer groupApplyType;

    private Integer groupMemberCount;

    private List<NepSimpleGroupMember> groupAdminList;

    private long createTime;

}
