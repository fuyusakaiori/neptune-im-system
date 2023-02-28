package com.fuyusakaiori.neptune.im.service.core.user.entity.dto;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString
public class NeptuneRegisterUser
{

    private String userNickName;

    private Integer userGender;

    private Integer userType;

    private String userExtra;

    private Long createTime;

    private Long updateTime;

}
