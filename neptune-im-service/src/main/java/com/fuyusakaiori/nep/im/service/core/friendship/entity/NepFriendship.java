package com.fuyusakaiori.nep.im.service.core.friendship.entity;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString
public class NepFriendship
{

    /**
     * <h3>好友关系发起者的唯一标识符</h3>
     */
    private Integer friendshipFromId;

    /**
     * <h3>好友关系接收者的唯一标识符</h3>
     */
    private Integer friendshipToId;

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
    private String friendshipAdditionalInfo;

    /**
     * <h3>好友是否被拉黑: 0 表示没有拉黑, 1 表示已经拉黑</h3>
     */
    private Boolean isBlack;

    private String friendshipExtra;

    private Long createTime;

    private Long updateTime;
}
