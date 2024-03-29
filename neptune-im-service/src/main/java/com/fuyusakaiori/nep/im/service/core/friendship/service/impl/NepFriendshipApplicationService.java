package com.fuyusakaiori.nep.im.service.core.friendship.service.impl;

import com.example.nep.im.common.entity.request.NepRequestHeader;
import com.example.nep.im.common.enums.code.NepBaseResponseCode;
import com.example.nep.im.common.enums.code.NepFriendshipApplicationResponseCode;
import com.example.nep.im.common.enums.code.NepFriendshipBlackResponseCode;
import com.example.nep.im.common.enums.status.NepFriendshipApplicationApproveStatus;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.apply.NepSendFriendshipApplicationRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.apply.NepApproveFriendshipApplicationResponse;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.apply.NepApproveFriendshipApplicationRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.apply.NepSendFriendshipApplicationResponse;
import com.fuyusakaiori.nep.im.service.core.friendship.service.INepFriendshipApplicationService;
import com.fuyusakaiori.nep.im.service.core.user.entity.dto.NepFriend;
import com.fuyusakaiori.nep.im.service.util.check.NepCheckFriendshipApplicationParamUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

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
            response.setNewFriend(null)
                    .setCode(NepBaseResponseCode.CHECK_PARAM_FAIL.getCode())
                    .setMessage(NepBaseResponseCode.CHECK_PARAM_FAIL.getMessage());
            log.error("NepFriendshipApplicationService approveFriendshipApplication: 参数校验失败 - request: {}, response: {}", request, response);
            return response;
        }
        // 2. 审批请求
        try {

            NepFriend newFriend = friendshipApplicationServiceImpl.doApproveFriendshipApplication(request);
            if (Objects.isNull(newFriend)){
                response.setNewFriend(null)
                        .setCode(NepFriendshipApplicationResponseCode.FRIEND_APPLICATION_APPROVED.getCode())
                        .setMessage(NepFriendshipApplicationResponseCode.FRIEND_APPLICATION_APPROVED.getMessage());
                log.error("NepFriendshipApplicationService approveFriendshipApplication: 审批好友请求失败 - request: {}, response: {}", request, response);
                return response;
            }
            response.setNewFriend(newFriend)
                    .setCode(NepBaseResponseCode.SUCCESS.getCode())
                    .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
            log.info("NepFriendshipApplicationService approveFriendshipApplication: 审批好友请求成功 - request: {}, response: {}", request, response);
            return response;
        }catch (Exception exception){
            response.setNewFriend(null)
                    .setCode(NepBaseResponseCode.UNKNOWN_ERROR.getCode())
                    .setMessage(NepBaseResponseCode.UNKNOWN_ERROR.getMessage());
            log.error("NepFriendshipApplicationService approveFriendshipApplication: 审批好友请求出现异常 - request: {}, response: {}", request, response, exception);
            return response;
        }
    }

    @Override
    public NepSendFriendshipApplicationResponse sendFriendshipApplication(NepSendFriendshipApplicationRequest request) {
        // 0. 准备返回结果
        NepSendFriendshipApplicationResponse response = new NepSendFriendshipApplicationResponse();
        // 1. 参数校验
        if (NepCheckFriendshipApplicationParamUtil.checkSendFriendshipApplicationRequestParam(request)){
            response.setCode(NepBaseResponseCode.CHECK_PARAM_FAIL.getCode())
                    .setMessage(NepBaseResponseCode.CHECK_PARAM_FAIL.getMessage());
            log.error("NepFriendshipApplicationService sendFriendshipApplication: 参数校验失败 - request: {}, response: {}", request, response);
            return response;
        }
        // 2. 发送好友申请
        try {
            int result = friendshipApplicationServiceImpl.doSendFriendshipApplication(request);
            if (result <= 0){
                response.setCode(NepFriendshipApplicationResponseCode.FRIEND_SEND_APPLICATION_FAIL.getCode())
                        .setMessage(NepFriendshipApplicationResponseCode.FRIEND_SEND_APPLICATION_FAIL.getMessage());
                log.error("NepFriendshipApplicationService sendFriendshipApplication: 好友申请发送失败 - request: {}, response: {}", request, response);
                return response;
            }
            response.setCode(NepBaseResponseCode.SUCCESS.getCode())
                    .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
            log.info("NepFriendshipApplicationService sendFriendshipApplication: 好友申请发送成功 - request: {}, response: {}", request, response);
            return response;
        }catch (Exception exception){
            response.setCode(NepBaseResponseCode.UNKNOWN_ERROR.getCode())
                    .setMessage(NepBaseResponseCode.UNKNOWN_ERROR.getMessage());
            log.error("NepFriendshipApplicationService sendFriendshipApplication: 好友申请发送出现异常 - request: {}, response: {}", request, response, exception);
            return response;
        }
    }


}
