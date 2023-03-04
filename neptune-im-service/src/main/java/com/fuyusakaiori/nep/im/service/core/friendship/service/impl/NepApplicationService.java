package com.fuyusakaiori.nep.im.service.core.friendship.service.impl;

import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.apply.NepAddApplicationRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.apply.NepApproveApplicationRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.apply.NepModifyApplicationResponse;
import com.fuyusakaiori.nep.im.service.core.friendship.mapper.INepApplicationMapper;
import com.fuyusakaiori.nep.im.service.core.friendship.service.INepApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NepApplicationService implements INepApplicationService {

    @Autowired
    private INepApplicationMapper applicationMapper;

    @Override
    public NepModifyApplicationResponse addFriendshipApplication(NepAddApplicationRequest request)
    {
        return null;
    }

    @Override
    public NepModifyApplicationResponse approveFriendshipApplication(NepApproveApplicationRequest request)
    {
        return null;
    }
}
