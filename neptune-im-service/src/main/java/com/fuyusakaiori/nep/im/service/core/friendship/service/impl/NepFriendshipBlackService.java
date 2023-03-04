package com.fuyusakaiori.nep.im.service.core.friendship.service.impl;

import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.black.NepAddBlackRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.black.NepQueryAllBlackRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.black.NepQueryBlackRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.black.NepRemoveBlackRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.NepModifyFriendshipResponse;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.NepQueryFriendshipResponse;
import com.fuyusakaiori.nep.im.service.core.friendship.mapper.INepFriendshipBlackMapper;
import com.fuyusakaiori.nep.im.service.core.friendship.service.INepFriendshipBlackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NepFriendshipBlackService implements INepFriendshipBlackService {

    @Autowired
    private INepFriendshipBlackMapper friendshipBlackMapper;

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
    public NepQueryFriendshipResponse queryFriendInBlackList(NepQueryBlackRequest request)
    {
        return null;
    }

}
