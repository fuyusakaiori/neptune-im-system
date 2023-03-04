package com.fuyusakaiori.nep.im.service.core.friendship.service;


import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.apply.NepAddApplicationRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.apply.NepApproveApplicationRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.apply.NepModifyApplicationResponse;

public interface INepApplicationService {

    NepModifyApplicationResponse addFriendshipApplication(NepAddApplicationRequest request);

    NepModifyApplicationResponse approveFriendshipApplication(NepApproveApplicationRequest request);

}
