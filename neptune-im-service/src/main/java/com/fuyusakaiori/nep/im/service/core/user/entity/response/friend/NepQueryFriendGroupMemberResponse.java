package com.fuyusakaiori.nep.im.service.core.user.entity.response.friend;

import com.fuyusakaiori.nep.im.service.core.user.entity.dto.NepFriend;
import com.fuyusakaiori.nep.im.service.core.user.entity.dto.NepFriendGroup;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

@Data
@Accessors(chain = true)
@ToString
public class NepQueryFriendGroupMemberResponse {

    private int code;

    private String message;

    private Map<NepFriendGroup, List<NepFriend>> friendGroupMemberMap;

}
