package com.fuyusakaiori.nep.im.service.core.friendship.entity.dto;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString
public class NepAddFriendshipGroupMember {

    private Integer friendshipGroupId;

    private Integer friendshipGroupMemberId;

    private Long createTime;

    private Long updateTime;

}
