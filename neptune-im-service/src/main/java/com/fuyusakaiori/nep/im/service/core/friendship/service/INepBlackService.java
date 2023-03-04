package com.fuyusakaiori.nep.im.service.core.friendship.service;

import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.black.NepAddBlackRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.black.NepCheckBlackRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.black.NepRemoveBlackRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.black.NepCheckBlackResponse;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.normal.NepModifyFriendshipResponse;

public interface INepBlackService {

    NepModifyFriendshipResponse addFriendInBlackList(NepAddBlackRequest request);

    NepModifyFriendshipResponse removeFriendInBlackList(NepRemoveBlackRequest request);

    NepCheckBlackResponse checkFriendInBlackList(NepCheckBlackRequest request);

    NepCheckBlackResponse checkBiFriendInBlackList(NepCheckBlackRequest request);

}
