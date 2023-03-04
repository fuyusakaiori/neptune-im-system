package com.fuyusakaiori.nep.im.service.core.user.entity.dto;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString
public class NepRegisterUser {

    private String userAccount;

    private String userPassword;

    private String userNickName;

    private Integer userGender;

    private String userExtra;

}
