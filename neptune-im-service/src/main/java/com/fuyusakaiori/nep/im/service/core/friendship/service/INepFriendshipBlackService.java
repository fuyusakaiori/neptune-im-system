package com.fuyusakaiori.nep.im.service.core.friendship.service;

import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.black.NepAddFriendshipBlackRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.black.NepCheckFriendshipBlackRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.black.NepRemoveFriendshipBlackRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.black.NepCheckFriendshipBlackResponse;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.normal.NepModifyFriendshipResponse;

public interface INepFriendshipBlackService {

    NepModifyFriendshipResponse addFriendInBlackList(NepAddFriendshipBlackRequest request);

    NepModifyFriendshipResponse removeFriendInBlackList(NepRemoveFriendshipBlackRequest request);

    NepCheckFriendshipBlackResponse checkFriendInBlackList(NepCheckFriendshipBlackRequest request);

}
