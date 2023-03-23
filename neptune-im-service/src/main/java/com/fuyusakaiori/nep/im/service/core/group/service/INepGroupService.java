package com.fuyusakaiori.nep.im.service.core.group.service;

import com.fuyusakaiori.nep.im.service.core.group.entity.request.*;
import com.fuyusakaiori.nep.im.service.core.group.entity.response.*;

public interface INepGroupService {

    NepCreateGroupResponse createGroup(NepCreateGroupRequest request);

    NepEditGroupResponse editGroupInfo(NepEditGroupRequest request);

    NepUploadGroupAvatarResponse updateGroupAvatar(NepUploadGroupAvatarRequest request);

    NepDissolveGroupResponse dissolveGroup(NepDissolveGroupRequest request);

    NepMuteGroupResponse muteGroupChat(NepMuteGroupRequest request);

    NepTransferGroupOwnerResponse transferGroupOwner(NepTransferGroupOwnerRequest request);

    NepQueryGroupResponse queryGroup(NepQueryGroupRequest request);

    NepQueryAllJoinedGroupResponse queryAllJoinedGroup(NepQueryAllJoinedGroupRequest request);

}
