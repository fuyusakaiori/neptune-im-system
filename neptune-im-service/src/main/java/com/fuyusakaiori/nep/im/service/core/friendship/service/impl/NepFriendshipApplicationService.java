package com.fuyusakaiori.nep.im.service.core.friendship.service.impl;

import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.apply.NepAddFriendshipApplicationRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.apply.NepApproveFriendshipApplicationRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.apply.NepModifyFriendshipApplicationResponse;
import com.fuyusakaiori.nep.im.service.core.friendship.mapper.INepFriendshipApplicationMapper;
import com.fuyusakaiori.nep.im.service.core.friendship.service.INepFriendshipApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NepFriendshipApplicationService implements INepFriendshipApplicationService {

    @Autowired
    private INepFriendshipApplicationMapper applicationMapper;

    @Override
    public NepModifyFriendshipApplicationResponse addFriendshipApplication(NepAddFriendshipApplicationRequest request)
    {
        return null;
    }

    @Override
    public NepModifyFriendshipApplicationResponse approveFriendshipApplication(NepApproveFriendshipApplicationRequest request)
    {
        return null;
    }
}
