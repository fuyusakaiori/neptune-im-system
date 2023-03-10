package com.fuyusakaiori.nep.im.service.core.user.entity.request.friend;

import com.example.nep.im.common.entity.request.NepRequestHeader;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <h3>查询每个好友分组中的成员的请求</h3>
 */
@Data
@Accessors(chain = true)
@ToString
public class NepQueryAllFriendGroupMemberRequest {

    /**
     * <h3>请求头</h3>
     */
    private NepRequestHeader requestHeader;

    /**
     * <h3>好友分组的创建 ID</h3>
     */
    private Integer userId;

}
