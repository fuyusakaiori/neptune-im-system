package com.fuyusakaiori.nep.im.service.core.friendship.entity.dto;


import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <h3>新增好友关系的必要信息</h3>
 */
@Data
@Accessors(chain = true)
@ToString
public class NepAddFriendship
{

    private Integer friendFromId;

    /**
     * <h3>需要添加的好友 ID</h3>
     */
    private Integer friendToId;

    /**
     * <h3>需要添加的好友的备注</h3>
     */
    private String friendRemark;

    /**
     * <h3>好友申请的来源</h3>
     */
    private String friendshipSource;

    /**
     * <h3>好友申请的附加信息</h3>
     */
    private String additionalInfo;

    /**
     * <h3>拓展字段</h3>
     */
    private String friendshipExtra;

}
