package com.fuyusakaiori.nep.im.service.core.user.entity.dto;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString
public class NepQueryFriendGroupMember {

    //========================== 分组信息 ==========================
    private Integer friendGroupId;

    private String friendGroupName;

    //========================== 好友信息 ==========================

    private String userRemark;

    private String userNickName;

    private String userSelfSignature;

    private String userAvatarAddress;

}
