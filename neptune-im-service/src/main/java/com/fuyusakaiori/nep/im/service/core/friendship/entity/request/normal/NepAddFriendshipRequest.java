package com.fuyusakaiori.nep.im.service.core.friendship.entity.request.normal;

import com.example.nep.im.common.entity.request.NepRequestHeader;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString
public class NepAddFriendshipRequest {

    private NepRequestHeader header;

    /**
     * <h3>发起好友添加的 ID</h3>
     */
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

}
