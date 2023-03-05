package com.fuyusakaiori.nep.im.service.core.friendship.service.impl;

import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.black.NepAddFriendshipBlackRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.black.NepCheckFriendshipBlackRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.black.NepRemoveFriendshipBlackRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.black.NepCheckFriendshipBlackResponse;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.normal.NepModifyFriendshipResponse;
import com.fuyusakaiori.nep.im.service.core.friendship.mapper.INepFriendshipBlackMapper;
import com.fuyusakaiori.nep.im.service.core.friendship.mapper.INepFriendshipMapper;
import com.fuyusakaiori.nep.im.service.core.friendship.service.INepFriendshipBlackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NepFriendshipBlackService implements INepFriendshipBlackService {

    @Autowired
    private INepFriendshipBlackMapper blackMapper;

    @Autowired
    private INepFriendshipMapper friendshipMapper;

    @Override
    public NepModifyFriendshipResponse addFriendInBlackList(NepAddFriendshipBlackRequest request) {

        return null;
    }

    @Override
    public NepModifyFriendshipResponse removeFriendInBlackList(NepRemoveFriendshipBlackRequest request) {
        return null;
    }

    @Override
    public NepCheckFriendshipBlackResponse checkFriendInBlackList(NepCheckFriendshipBlackRequest request) {
        return null;
    }

    @Override
    public NepCheckFriendshipBlackResponse checkBiFriendInBlackList(NepCheckFriendshipBlackRequest request) {
        return null;
    }

}
