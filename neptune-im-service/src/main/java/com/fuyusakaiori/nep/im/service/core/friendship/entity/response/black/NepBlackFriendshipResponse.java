package com.fuyusakaiori.nep.im.service.core.friendship.entity.response.black;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString
public class NepBlackFriendshipResponse {

    private int code;

    private String message;

}
