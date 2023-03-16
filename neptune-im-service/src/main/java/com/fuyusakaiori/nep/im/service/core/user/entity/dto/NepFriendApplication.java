package com.fuyusakaiori.nep.im.service.core.user.entity.dto;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString
public class NepFriendApplication {

    private Integer friendApplicationId;

    private Integer friendFromId;

    private Integer friendToId;

    private String friendAccount;

    private Integer readStatus;

    private Integer approveStatus;

    private String additionalInfo;

    private String friendSource;

    private String friendNickName;

    private String friendAvatarAddress;
}
