package com.fuyusakaiori.nep.im.service.core.friendship.entity.dto;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString
public class NepAddFriendshipGroup {

    private Integer groupOwnerId;

    private String groupName;

}
