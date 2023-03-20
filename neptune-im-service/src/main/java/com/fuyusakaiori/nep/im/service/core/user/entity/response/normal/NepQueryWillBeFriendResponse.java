package com.fuyusakaiori.nep.im.service.core.user.entity.response.normal;

import com.fuyusakaiori.nep.im.service.core.user.entity.dto.NepWillBeFriend;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@ToString
public class NepQueryWillBeFriendResponse
{

    private List<NepWillBeFriend> userList;

    private int code;

    private String message;

}
