package com.fuyusakaiori.nep.im.service.core.friendship.entity.dto;


import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <h3>更新好友关系的相关信息</h3>
 * <h4>1. 可以更新好友备注</h4>
 * <h4>2. 可以恢复好友关系</h4>
 */
@Data
@Accessors(chain = true)
@ToString
public class NepEditFriendship
{

    //=========================== 查询条件 ===========================
    /**
     * <h3>好友关系的发起者</h3>
     */
    private Integer friendFromId;

    /**
     * <h3>好友关系的接收者</h3>
     */
    private Integer friendToId;

    //=========================== 更新内容 ===========================
    /**
     * <h3>更新后的备注</h3>
     */
    private String friendRemark;

    /**
     * <h3>更新后的好友来源</h3>
     */
    private String friendshipSource;

    /**
     * <h3>更新后的附加信息</h3>
     */
    private String additionalInfo;

    /**
     * <h3>更新后的好友关系</h3>
     */
    private Integer friendshipStatus;

    private String friendshipExtra;

}
