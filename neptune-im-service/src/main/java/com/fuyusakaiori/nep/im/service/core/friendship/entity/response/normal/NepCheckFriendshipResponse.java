package com.fuyusakaiori.nep.im.service.core.friendship.entity.response.normal;


import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString
public class NepCheckFriendshipResponse {

    private Integer friendFromId;

    private Integer friendToId;

    private Integer status;

    private int code;

    private String message;

}
