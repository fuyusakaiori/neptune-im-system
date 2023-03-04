package com.fuyusakaiori.nep.im.service.core.friendship.entity.dto;


import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString
public class NepAddFriendshipApplication
{

    private Integer friendFromId;

    private Integer friendToId;

    private String additionalInfo;

    private String remark;

    private String source;

}
