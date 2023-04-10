package com.fuyusakaiori.nep.im.service.core.friendship.entity.response.apply;

import com.fuyusakaiori.nep.im.service.core.user.entity.dto.NepFriend;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString
public class NepApproveFriendshipApplicationResponse {

    private int code;

    private String message;

    private NepFriend newFriend;



}
