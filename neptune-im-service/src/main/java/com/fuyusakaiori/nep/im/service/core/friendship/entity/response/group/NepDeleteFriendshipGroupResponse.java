package com.fuyusakaiori.nep.im.service.core.friendship.entity.response.group;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString
public class NepDeleteFriendshipGroupResponse
{

    private int code;

    private String message;

}
