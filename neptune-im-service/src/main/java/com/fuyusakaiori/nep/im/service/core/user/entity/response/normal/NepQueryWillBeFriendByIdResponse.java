package com.fuyusakaiori.nep.im.service.core.user.entity.response.normal;

import com.fuyusakaiori.nep.im.service.core.user.entity.dto.NepWillBeFriend;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString
public class NepQueryWillBeFriendByIdResponse {

    private int code;

    private String message;

    private NepWillBeFriend user;

}
