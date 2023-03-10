package com.fuyusakaiori.nep.im.service.core.friendship.entity.request.group;

import com.example.nep.im.common.entity.request.NepRequestHeader;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString
public class NepQueryAllFriendshipGroupRequest {

    private NepRequestHeader requestHeader;

    private Integer groupOwnerId;
}
