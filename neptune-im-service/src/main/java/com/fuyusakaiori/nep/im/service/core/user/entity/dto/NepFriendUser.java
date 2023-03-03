package com.fuyusakaiori.nep.im.service.core.user.entity.dto;


import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString
public class NepFriendUser
{

    private Integer userId;

    private String nickName;

    private String selfSignature;

    private String avatarAddress;

}
