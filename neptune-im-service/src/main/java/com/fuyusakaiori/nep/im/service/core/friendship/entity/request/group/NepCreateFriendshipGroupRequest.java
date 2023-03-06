package com.fuyusakaiori.nep.im.service.core.friendship.entity.request.group;

import com.example.neptune.im.common.entity.request.NepRequestHeader;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.dto.NepCreateFriendshipGroup;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString
public class NepCreateFriendshipGroupRequest {

    private NepRequestHeader requestHeader;

    private Integer groupOwnerId;

    private String groupName;

}