package com.fuyusakaiori.nep.im.service.core.friendship.service.impl;

import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.group.NepAddFriendshipGroupRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.group.NepDeleteFriendshipGroupRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.group.NepQueryAllFriendshipGroupRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.group.NepModifyFriendshipGroupResponse;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.group.NepQueryFriendshipGroupResponse;
import com.fuyusakaiori.nep.im.service.core.friendship.mapper.INepFriendshipGroupMapper;
import com.fuyusakaiori.nep.im.service.core.friendship.service.INepFriendshipGroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NepFriendshipGroupService implements INepFriendshipGroupService {

    @Autowired
    private INepFriendshipGroupMapper friendshipGroupMapper;

    @Override
    public NepModifyFriendshipGroupResponse addFriendshipGroup(NepAddFriendshipGroupRequest request) {
        return null;
    }

    @Override
    public NepModifyFriendshipGroupResponse deleteFriendshipGroup(NepDeleteFriendshipGroupRequest request)
    {
        return null;
    }

    @Override
    public NepQueryFriendshipGroupResponse queryAllFriendshipGroup(NepQueryAllFriendshipGroupRequest request)
    {
        return null;
    }
}
