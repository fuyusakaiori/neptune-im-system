package com.fuyusakaiori.nep.im.service.core.friendship.entity.response.group;

import com.fuyusakaiori.nep.im.service.core.friendship.entity.NepFriendshipGroup;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@ToString
public class NepQueryFriendshipGroupResponse {

    private List<NepFriendshipGroup> groupList;

    private int code;

    private String message;

}
