package com.fuyusakaiori.nep.im.service.core.friendship.service.impl;

import com.example.nep.im.common.entity.request.NepRequestHeader;
import com.example.nep.im.common.enums.code.NepBaseResponseCode;
import com.example.nep.im.common.enums.code.NepFriendshipBlackResponseCode;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.apply.NepApproveFriendshipApplicationResponse;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.apply.NepApproveFriendshipApplicationRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.service.INepFriendshipApplicationService;
import com.fuyusakaiori.nep.im.service.util.check.NepCheckFriendshipApplicationParamUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NepFriendshipApplicationService implements INepFriendshipApplicationService {

    @Autowired
    private NepFriendshipApplicationServiceImpl friendshipApplicationServiceImpl;

    @Override
    public NepApproveFriendshipApplicationResponse approveFriendshipApplication(NepApproveFriendshipApplicationRequest request) {
        // 0. 准备返回结果
        NepApproveFriendshipApplicationResponse response = new NepApproveFriendshipApplicationResponse();
        // 1. 参数校验
        if (NepCheckFriendshipApplicationParamUtil.checkApproveFriendshipApplicationRequestParam(request)){
            log.error("NepFriendshipApplicationService approveFriendshipApplication: 参数校验失败 - request: {}", request);
            return response.setCode(NepBaseResponseCode.CHECK_PARAM_FAIL.getCode())
                           .setMessage(NepBaseResponseCode.CHECK_PARAM_FAIL.getMessage());
        }
        // 2. 获取变量
        NepRequestHeader header = request.getRequestHeader();
        Integer applyId = request.getApplyId();
        Integer approveStatus = request.getApproveStatus();
        // 3. 审批好友申请
        int result = friendshipApplicationServiceImpl.doApproveFriendshipApplication(header, applyId, approveStatus);
        if (result <= 0){
            log.error("NepFriendshipApplicationService approveFriendshipApplication: 审批好友请求失败 - request: {}", request);
            return response.setCode(NepFriendshipBlackResponseCode.FRIEND_BLACK_FAIL.getCode())
                           .setMessage(NepFriendshipBlackResponseCode.FRIEND_BLACK_FAIL.getMessage());
        }
        return response.setCode(NepBaseResponseCode.SUCCESS.getCode())
                       .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
    }



}
