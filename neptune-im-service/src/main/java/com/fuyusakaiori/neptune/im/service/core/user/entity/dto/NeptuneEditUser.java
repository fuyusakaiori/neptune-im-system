package com.fuyusakaiori.neptune.im.service.core.user.entity.dto;


import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString
public class NeptuneEditUser
{

    private Integer userId;

    private String userNickName;

    private Integer userGender;

    private Integer userAge;

    private Long userBirthday;

    private String userLocation;

    private String userSelfSignature;

    private Integer userFriendshipAllowType;

    private String userAvatarAddress;

    private Boolean isForbidApply;

    private Boolean isForbid;

    private Integer userType;

    private String userExtra;

    private Long updateTime;

}
