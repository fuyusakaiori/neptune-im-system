package com.fuyusakaiori.nep.im.service.core.friendship.service.impl;

import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.black.NepAddBlackRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.black.NepCheckBlackRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.black.NepRemoveBlackRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.black.NepCheckBlackResponse;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.normal.NepModifyFriendshipResponse;
import com.fuyusakaiori.nep.im.service.core.friendship.mapper.INepBlackMapper;
import com.fuyusakaiori.nep.im.service.core.friendship.mapper.INepFriendshipMapper;
import com.fuyusakaiori.nep.im.service.core.friendship.service.INepBlackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NepBlackService implements INepBlackService {

    @Autowired
    private INepBlackMapper blackMapper;

    @Autowired
    private INepFriendshipMapper friendshipMapper;

    @Override
    public NepModifyFriendshipResponse addFriendInBlackList(NepAddBlackRequest request) {

        return null;
    }

    @Override
    public NepModifyFriendshipResponse removeFriendInBlackList(NepRemoveBlackRequest request)
    {
        return null;
    }

    @Override
    public NepCheckBlackResponse checkFriendInBlackList(NepCheckBlackRequest request)
    {
        return null;
    }

    @Override
    public NepCheckBlackResponse checkBiFriendInBlackList(NepCheckBlackRequest request)
    {
        return null;
    }

}
