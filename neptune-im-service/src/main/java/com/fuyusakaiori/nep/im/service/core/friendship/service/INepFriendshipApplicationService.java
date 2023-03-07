package com.fuyusakaiori.nep.im.service.core.friendship.service;


import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.apply.NepApproveFriendshipApplicationResponse;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.apply.NepSendFriendshipApplicationRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.apply.NepApproveFriendshipApplicationRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.apply.NepSendFriendshipApplicationResponse;

public interface INepFriendshipApplicationService {

    NepApproveFriendshipApplicationResponse approveFriendshipApplication(NepApproveFriendshipApplicationRequest request);

}
