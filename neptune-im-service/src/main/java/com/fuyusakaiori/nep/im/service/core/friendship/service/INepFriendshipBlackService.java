package com.fuyusakaiori.nep.im.service.core.friendship.service;

import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.black.NepAddFriendshipBlackRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.black.NepCheckFriendshipBlackRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.black.NepRemoveFriendshipBlackRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.black.NepBlackFriendshipResponse;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.black.NepCheckFriendshipBlackResponse;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.normal.NepReleaseFriendshipResponse;

public interface INepFriendshipBlackService {

    NepBlackFriendshipResponse addFriendInBlackList(NepAddFriendshipBlackRequest request);

    NepBlackFriendshipResponse removeFriendInBlackList(NepRemoveFriendshipBlackRequest request);

}
