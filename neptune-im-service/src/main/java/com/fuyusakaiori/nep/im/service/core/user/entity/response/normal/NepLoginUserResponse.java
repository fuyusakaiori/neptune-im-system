package com.fuyusakaiori.nep.im.service.core.user.entity.response.normal;


import com.fuyusakaiori.nep.im.service.core.user.entity.NepUser;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString
public class NepLoginUserResponse {

    private int code;

    private String message;

    private NepUser loginUser;

}
