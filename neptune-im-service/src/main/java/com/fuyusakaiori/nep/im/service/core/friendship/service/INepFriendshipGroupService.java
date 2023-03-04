package com.fuyusakaiori.nep.im.service.core.friendship.service;

import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.group.NepAddFriendshipGroupRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.group.NepDeleteFriendshipGroupRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.group.NepQueryAllFriendshipGroupRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.group.NepModifyFriendshipGroupResponse;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.group.NepQueryFriendshipGroupResponse;
import lombok.Data;
import lombok.ToString;


public interface INepFriendshipGroupService {

    NepModifyFriendshipGroupResponse addFriendshipGroup(NepAddFriendshipGroupRequest request);

    NepModifyFriendshipGroupResponse deleteFriendshipGroup(NepDeleteFriendshipGroupRequest request);

    NepQueryFriendshipGroupResponse queryAllFriendshipGroup(NepQueryAllFriendshipGroupRequest request);

}
