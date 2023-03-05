package com.fuyusakaiori.nep.im.service.core.user.entity.request.friend;

import com.example.neptune.im.common.entity.request.NepRequestHeader;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <h3>查询所有在黑名单中的好友的请求</h3>
 */
@Data
@Accessors(chain = true)
@ToString
public class NepQueryAllFriendBlackRequest
{

    /**
     * <h3></h3>
     */
    private NepRequestHeader requestHeader;

    /**
     * <h3>好友发起者的 ID</h3>
     */
    private Integer friendFromId;

}
