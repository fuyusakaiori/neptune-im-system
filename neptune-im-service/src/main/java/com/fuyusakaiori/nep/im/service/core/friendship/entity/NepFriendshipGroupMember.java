package com.fuyusakaiori.nep.im.service.core.friendship.entity;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString
public class NepFriendshipGroupMember {

    /**
     * <h3>好友分组 ID</h3>
     */
    private Integer friendshipGroupId;

    /**
     * <h3>好友分组中的成员 ID</h3>
     */
    private Integer friendshipGroupMemberId;

    /**
     * <h3>好友分组中的成员是否被移除</h3>
     */
    private Boolean isDelete;

    /**
     * <h3>创建时间</h3>
     */
    private Long createTime;

    /**
     * <h3>更新时间</h3>
     */
    private Long updateTime;

}
