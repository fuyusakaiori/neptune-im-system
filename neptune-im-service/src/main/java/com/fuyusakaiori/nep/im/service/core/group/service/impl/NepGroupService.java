package com.fuyusakaiori.nep.im.service.core.group.service.impl;

import com.fuyusakaiori.nep.im.service.core.group.entity.request.*;
import com.fuyusakaiori.nep.im.service.core.group.entity.response.NepModifyGroupResponse;
import com.fuyusakaiori.nep.im.service.core.group.entity.response.NepQueryGroupResponse;
import com.fuyusakaiori.nep.im.service.core.group.mapper.INepGroupMapper;
import com.fuyusakaiori.nep.im.service.core.group.service.INepGroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NepGroupService implements INepGroupService {

    @Autowired
    private INepGroupMapper groupMapper;

    @Override
    public NepModifyGroupResponse createGroup(NepCreateGroupRequest request)
    {
        return null;
    }

    @Override
    public NepModifyGroupResponse editGroup(NepEditGroupRequest request)
    {
        return null;
    }

    @Override
    public NepModifyGroupResponse deleteGroup(NepDeleteGroupRequest request)
    {
        return null;
    }

    @Override
    public NepModifyGroupResponse muteGroup(NepMuteGroupRequest request)
    {
        return null;
    }

    @Override
    public NepModifyGroupResponse transferGroupOwner(NepTransferGroupOwnerRequest request)
    {
        return null;
    }

    @Override
    public NepQueryGroupResponse querySimpleGroup(NepQuerySimpleGroupRequest request)
    {
        return null;
    }

    @Override
    public NepQueryGroupResponse queryDetailedGroup(NepQueryDetailedGroupRequest request)
    {
        return null;
    }

    @Override
    public NepQueryGroupResponse queryAllJoinedGroup(NepQueryAllJoinedGroupRequest request)
    {
        return null;
    }
}
