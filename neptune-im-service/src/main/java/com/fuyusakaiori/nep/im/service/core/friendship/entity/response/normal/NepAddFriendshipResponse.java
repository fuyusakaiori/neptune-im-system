package com.fuyusakaiori.nep.im.service.core.friendship.entity.response.normal;

import com.fuyusakaiori.nep.im.service.core.user.entity.dto.NepFriend;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString
public class NepAddFriendshipResponse {

    private int code;

    private String message;

    private NepFriend newFriend;

}
