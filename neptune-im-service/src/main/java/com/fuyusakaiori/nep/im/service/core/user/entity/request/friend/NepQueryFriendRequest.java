package com.fuyusakaiori.nep.im.service.core.user.entity.request.friend;


import com.example.nep.im.common.entity.request.NepRequestHeader;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString
public class NepQueryFriendRequest {

    private NepRequestHeader header;

    private Integer friendFromId;

    private String username;

    private String nickname;

    private String friendRemark;

}
