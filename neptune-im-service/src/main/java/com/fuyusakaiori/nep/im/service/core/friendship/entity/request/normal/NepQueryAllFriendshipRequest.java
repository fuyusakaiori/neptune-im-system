package com.fuyusakaiori.nep.im.service.core.friendship.entity.request.normal;

import com.example.neptune.im.common.entity.request.NepRequestHeader;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <h3>查询用户的所有好友请求</h3>
 */
@Data
@Accessors(chain = true)
@ToString
public class NepQueryAllFriendshipRequest
{

    private NepRequestHeader requestHeader;

    private Integer friendFromId;

}
