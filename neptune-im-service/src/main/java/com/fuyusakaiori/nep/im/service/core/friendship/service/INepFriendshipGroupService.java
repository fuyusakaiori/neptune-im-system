package com.fuyusakaiori.nep.im.service.core.friendship.service;

import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.group.NepCreateFriendshipGroupRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.group.NepDeleteFriendshipGroupRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.group.NepQueryAllFriendshipGroupRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.group.NepCreateFriendshipGroupResponse;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.group.NepDeleteFriendshipGroupResponse;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.group.NepQueryFriendshipGroupResponse;


public interface INepFriendshipGroupService {

    NepCreateFriendshipGroupResponse createFriendshipGroup(NepCreateFriendshipGroupRequest request);

    NepDeleteFriendshipGroupResponse deleteFriendshipGroup(NepDeleteFriendshipGroupRequest request);

    NepQueryFriendshipGroupResponse queryAllFriendshipGroup(NepQueryAllFriendshipGroupRequest request);

}
