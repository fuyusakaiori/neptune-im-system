package com.fuyusakaiori.nep.im.service.core.friendship.entity;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString
public class NepFriendshipGroup {

    /**
     * <h3>好友分组 ID</h3>
     */
    private Integer groupId;

    /**
     * <h3>好友分组所属用户 ID</h3>
     */
    private Integer groupOwnerId;

    /**
     * <h3>分组名称</h3>
     */
    private String groupName;

    /**
     * <h3>分组是否被删除</h3>
     */
    private Boolean isDelete;

    private Long createTime;

    private Long updateTime;

}
