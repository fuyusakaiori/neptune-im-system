package com.fuyusakaiori.nep.im.service.core.friendship.service;

import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.black.NepAddBlackRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.black.NepQueryAllBlackRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.black.NepQueryBlackRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.black.NepRemoveBlackRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.NepModifyFriendshipResponse;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.NepQueryFriendshipResponse;

public interface INepFriendshipBlackService {


    NepModifyFriendshipResponse addFriendInBlackList(NepAddBlackRequest request);

    NepModifyFriendshipResponse removeFriendInBlackList(NepRemoveBlackRequest request);

    NepQueryFriendshipResponse queryFriendInBlackList(NepQueryBlackRequest request);

}
