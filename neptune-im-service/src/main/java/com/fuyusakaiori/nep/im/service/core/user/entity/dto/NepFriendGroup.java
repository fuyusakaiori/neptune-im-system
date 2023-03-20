package com.fuyusakaiori.nep.im.service.core.user.entity.dto;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class NepFriendGroup {

    private Integer groupId;

    private Integer groupOwnerId;

    private String groupName;

    @Override
    public String toString() {

        return "{" + "\"groupId\":" + groupId + ",\"groupOwnerId\":" + groupOwnerId + ",\"groupName\":\"" + groupName + "\"}";
    }
}
