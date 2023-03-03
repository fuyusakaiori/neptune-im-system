package com.fuyusakaiori.nep.im.service.core.user.entity.response;


import com.fuyusakaiori.nep.im.service.core.user.entity.dto.NepFriendUser;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@ToString
public class NepQueryFriendUserResponse {

    private int code;

    private String message;

    private List<NepFriendUser> friendUserList;

}
