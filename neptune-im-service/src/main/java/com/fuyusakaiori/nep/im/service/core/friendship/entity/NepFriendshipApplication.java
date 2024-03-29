package com.fuyusakaiori.nep.im.service.core.friendship.entity;


import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString
public class NepFriendshipApplication {

    /**
     * <h3>好友申请 ID</h3>
     */
    private Integer friendshipApplyId;

    /**
     * <h3>好友申请的发起者</h3>
     */
    private Integer friendFromId;

    /**
     * <h3>好友申请的接收者</h3>
     */
    private Integer friendToId;

    /**
     * <h3>好友申请时填写的备注</h3>
     */
    private String friendRemark;

    /**
     * <h3>好友申请的来源</h3>
     */
    private String friendshipSource;

    /**
     * <h3>好友申请时的附加信息</h3>
     */
    private String additionalInfo;

    /**
     * <h3>好友申请是否已读: 0 表示未读, 1 表示已读</h3>
     */
    private Integer applyReadStatus;

    /**
     * <h3>好友申请是否通过: 0 表示已经通过, 1 表示未通过</h3>
     */
    private Integer applyApproveStatus;

    /**
     * <h3>创建时间</h3>
     */
    private long createTime;

    /**
     * <h3>更新时间</h3>
     */
    private long updateTime;

}
