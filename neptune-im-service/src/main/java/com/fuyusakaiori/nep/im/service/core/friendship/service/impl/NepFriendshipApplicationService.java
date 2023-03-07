package com.fuyusakaiori.nep.im.service.core.friendship.service.impl;

import com.example.neptune.im.common.entity.request.NepRequestHeader;
import com.example.neptune.im.common.enums.code.NepBaseResponseCode;
import com.example.neptune.im.common.enums.code.NepFriendshipApplicationResponseCode;
import com.example.neptune.im.common.enums.code.NepFriendshipResponseCode;
import com.example.neptune.im.common.enums.status.NepFriendshipApplicationApproveStatus;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.NepFriendshipApplication;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.dto.NepAddFriendship;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.apply.NepApproveFriendshipApplicationResponse;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.apply.NepApproveFriendshipApplicationRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.mapper.INepFriendshipApplicationMapper;
import com.fuyusakaiori.nep.im.service.core.friendship.service.INepFriendshipApplicationService;
import com.fuyusakaiori.nep.im.service.util.NepCheckFriendshipApplicationParamUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class NepFriendshipApplicationService implements INepFriendshipApplicationService {

    @Autowired
    private INepFriendshipApplicationMapper friendshipApplicationMapper;

    @Autowired
    private NepFriendshipServiceImpl friendshipServiceImpl;


    @Override
    public NepApproveFriendshipApplicationResponse approveFriendshipApplication(NepApproveFriendshipApplicationRequest request) {
        // 0. 准备返回结果
        NepApproveFriendshipApplicationResponse response = new NepApproveFriendshipApplicationResponse();
        // 1. 参数校验
        if (NepCheckFriendshipApplicationParamUtil.checkApproveFriendshipApplicationRequestParam(request)){
            log.error("NepFriendshipApplicationService approveFriendshipApplication: 参数校验失败 - request: {}", request);
            return response.setCode(NepBaseResponseCode.CHECK_PARAM_FAILURE.getCode())
                           .setMessage(NepBaseResponseCode.CHECK_PARAM_FAILURE.getMessage());
        }
        // 2. 获取变量
        NepRequestHeader header = request.getRequestHeader();
        Integer applyId = request.getApplyId();
        Integer approveStatus = request.getApproveStatus();
        // 3. 查询是否有好友请求
        NepFriendshipApplication application = friendshipApplicationMapper.queryFriendshipApplicationById(header.getAppId(), applyId);
        // TODO 这里首先需要检验发起审批请求的是否是自己
        // 4. 校验好友请求是否合法
        if (Objects.isNull(application)){
            log.error("NepFriendshipApplicationService approveFriendshipApplication: 好友请求不存在 - request: {}", request);
            return response.setCode(NepFriendshipApplicationResponseCode.FRIEND_APPLICATION_NOT_EXIST.getCode())
                           .setMessage(NepFriendshipApplicationResponseCode.FRIEND_APPLICATION_NOT_EXIST.getMessage());
        }
        if (NepFriendshipApplicationApproveStatus.UNAPPROVED.getStatus() != application.getApplyApproveStatus()){
            log.error("NepFriendshipApplicationService approveFriendshipApplication: 好友请求已经审批过 - request: {}", request);
            return response.setCode(NepFriendshipApplicationResponseCode.FRIEND_APPLICATION_APPROVED.getCode())
                           .setMessage(NepFriendshipApplicationResponseCode.FRIEND_APPLICATION_APPROVED.getMessage());
        }
        // 5. 审批好友请求
        int isApprove = friendshipApplicationMapper.approveFriendshipApplication(header.getAppId(), applyId, approveStatus, System.currentTimeMillis());
        if (isApprove <= 0){
            log.error("NepFriendshipApplicationService approveFriendshipApplication: {} 审批 {} 发送的好友申请失败 - request: {}", application.getFriendshipToId(), application.getFriendshipFromId(), request);
            return response.setCode(NepFriendshipApplicationResponseCode.SEND_FRIEND_APPLICATION_FAIL.getCode())
                           .setMessage(NepFriendshipApplicationResponseCode.SEND_FRIEND_APPLICATION_FAIL.getMessage());
        }
        // 6. 如果同意好友申请, 那么执行好友添加
        int isAdd = friendshipServiceImpl.doAddFriendship(header, transferToAddFriendship(application));
        if (isAdd <= 0){
            return response.setCode(NepFriendshipResponseCode.FRIENDSHIP_ADD_FAIL.getCode())
                           .setMessage(NepFriendshipResponseCode.FRIENDSHIP_ADD_FAIL.getMessage());
        }
        return response.setCode(NepBaseResponseCode.SUCCESS.getCode())
                       .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
    }


    private NepAddFriendship transferToAddFriendship(NepFriendshipApplication application){
        return new NepAddFriendship()
                       .setFriendFromId(application.getFriendshipFromId())
                       .setFriendToId(application.getFriendshipToId())
                       .setFriendshipSource(application.getApplySource())
                       .setFriendRemark(application.getApplyRemark())
                       .setAdditionalInfo(application.getApplyAdditionalInfo());
    }


}
