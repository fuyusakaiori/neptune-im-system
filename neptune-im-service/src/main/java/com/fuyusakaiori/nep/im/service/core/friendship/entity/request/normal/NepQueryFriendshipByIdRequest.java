package com.fuyusakaiori.nep.im.service.core.friendship.entity.request.normal;

import com.example.neptune.im.common.entity.request.NepRequestHeader;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <h3>通过 ID 查询用户特定好友</h3>
 */
@Data
@Accessors(chain = true)
@ToString
public class NepQueryFriendshipByIdRequest {

    private NepRequestHeader requestHeader;

    private Integer friendFromId;

    private Integer friendToId;
}
