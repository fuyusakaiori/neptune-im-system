package com.fuyusakaiori.nep.im.service.core.friendship.service;


import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.apply.NepAddFriendshipApplicationRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.apply.NepApproveFriendshipApplicationRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.apply.NepModifyFriendshipApplicationResponse;

public interface INepFriendshipApplicationService
{

    NepModifyFriendshipApplicationResponse addFriendshipApplication(NepAddFriendshipApplicationRequest request);

    NepModifyFriendshipApplicationResponse approveFriendshipApplication(NepApproveFriendshipApplicationRequest request);

}
