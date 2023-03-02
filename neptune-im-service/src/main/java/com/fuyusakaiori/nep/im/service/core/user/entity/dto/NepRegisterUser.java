package com.fuyusakaiori.nep.im.service.core.user.entity.dto;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString
public class NepRegisterUser
{

    private String userNickName;

    private String userPassword;

    private Integer userGender;

    private Integer userType;

    private String userExtra;

}
