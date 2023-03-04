package com.fuyusakaiori.nep.im.service.core.user.entity.dto;


import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString
public class NepEditUser
{

    //=========================== 查询条件 ===========================
    private Integer userId;

    //========================== 更新内容 ============================
    private String userNickName;

    private String userPassword;

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

}
