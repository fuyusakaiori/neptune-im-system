package com.fuyusakaiori.nep.im.service.core.group.service;

import com.fuyusakaiori.nep.im.service.core.group.entity.request.*;
import com.fuyusakaiori.nep.im.service.core.group.entity.response.NepModifyGroupResponse;
import com.fuyusakaiori.nep.im.service.core.group.entity.response.NepQueryGroupResponse;

public interface INepGroupService {

    NepModifyGroupResponse createGroup(NepCreateGroupRequest request);

    NepModifyGroupResponse editGroup(NepEditGroupRequest request);

    NepModifyGroupResponse deleteGroup(NepDeleteGroupRequest request);

    NepModifyGroupResponse muteGroup(NepMuteGroupRequest request);

    NepModifyGroupResponse transferGroupOwner(NepTransferGroupOwnerRequest request);

    NepQueryGroupResponse querySimpleGroup(NepQuerySimpleGroupRequest request);

    NepQueryGroupResponse queryDetailedGroup(NepQueryDetailedGroupRequest request);

    NepQueryGroupResponse queryAllJoinedGroup(NepQueryAllJoinedGroupRequest request);

}
