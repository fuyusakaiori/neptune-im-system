package com.fuyusakaiori.nep.im.service.core.friendship.entity;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString
public class NepFriendship {

    /**
     * <h3>好友关系发起者的唯一标识符</h3>
     */
    private Integer friendFromId;

    /**
     * <h3>好友关系接收者的唯一标识符</h3>
     */
    private Integer friendToId;

    /**
     * <h3>好友备注</h3>
     */
    private String friendRemark;

    /**
     * <h3>好友状态: 0 表示正常, 1 表示已经删除</h3>
     */
    private Integer friendshipStatus;

    /**
     * <h3>好友关系来源</h3>
     */
    private String friendshipSource;

    /**
     * <h3>添加好友时的附加信息</h3>
     */
    private String additionalInfo;

    /**
     * <h3>好友是否被拉黑: 0 表示没有拉黑, 1 表示已经拉黑</h3>
     */
    private boolean black;

    private long createTime;

    private long updateTime;
}
