package com.fuyusakaiori.nep.im.service.core.user.entity.response.normal;

import com.fuyusakaiori.nep.im.service.core.user.entity.NepUser;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@ToString
public class NepQueryUserResponse
{

    private List<NepUser> userList;

    private int code;

    private String message;

}
