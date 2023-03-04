package com.fuyusakaiori.nep.im.service.core.friendship.entity.request.normal;

import com.example.neptune.im.common.entity.request.NepRequestHeader;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString
public class NepCheckFriendshipRequest {

    private NepRequestHeader requestHeader;

    private Integer friendFromId;

    private Integer friendToId;

}
