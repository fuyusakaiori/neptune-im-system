package com.fuyusakaiori.nep.im.service.core.group.service;


import com.fuyusakaiori.nep.im.service.core.group.entity.request.NepApproveGroupApplicationRequest;
import com.fuyusakaiori.nep.im.service.core.group.entity.request.NepQueryGroupApplicationRequest;
import com.fuyusakaiori.nep.im.service.core.group.entity.request.NepSendGroupApplicationRequest;
import com.fuyusakaiori.nep.im.service.core.group.entity.response.NepApproveGroupApplicationResponse;
import com.fuyusakaiori.nep.im.service.core.group.entity.response.NepQueryGroupApplicationResponse;
import com.fuyusakaiori.nep.im.service.core.group.entity.response.NepSendGroupApplicationResponse;

public interface INepGroupApplicationService {

    NepSendGroupApplicationResponse sendGroupApplication(NepSendGroupApplicationRequest request);

    NepApproveGroupApplicationResponse approveGroupApplication(NepApproveGroupApplicationRequest request);

    NepQueryGroupApplicationResponse queryGroupApplication(NepQueryGroupApplicationRequest request);


}
