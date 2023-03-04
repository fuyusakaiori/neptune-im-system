package com.fuyusakaiori.nep.im.service.core.friendship.entity.response.normal;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString
public class NepModifyFriendshipResponse {

    private int code;

    private String message;

}
