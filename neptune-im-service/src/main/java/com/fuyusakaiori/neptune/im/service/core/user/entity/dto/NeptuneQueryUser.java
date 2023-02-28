package com.fuyusakaiori.neptune.im.service.core.user.entity.dto;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString
public class NeptuneQueryUser {

    private Integer userId;

    private String userNickName;

    private Integer userGender;

    private Boolean isForbid;

    private Boolean isDelete;

    private Integer userType;

    private String userExtra;

}
