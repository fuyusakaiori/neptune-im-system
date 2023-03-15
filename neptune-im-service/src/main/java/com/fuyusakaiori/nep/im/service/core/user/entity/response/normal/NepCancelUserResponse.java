package com.fuyusakaiori.nep.im.service.core.user.entity.response.normal;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString
public class NepCancelUserResponse {


    private int code;

    private String message;

}
