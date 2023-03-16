package com.fuyusakaiori.nep.im.service.core.user.entity.response.friend;

import com.fuyusakaiori.nep.im.service.core.user.entity.dto.NepFriendApplication;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@ToString
public class NepQueryAllFriendApplicationResponse
{

    private int code;

    private String message;

    private List<NepFriendApplication> applicationList;

}
