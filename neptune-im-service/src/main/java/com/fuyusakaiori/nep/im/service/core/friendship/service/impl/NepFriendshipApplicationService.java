package com.fuyusakaiori.nep.im.service.core.friendship.service.impl;

import com.example.neptune.im.common.entity.request.NepRequestHeader;
import com.example.neptune.im.common.enums.code.NepBaseResponseCode;
import com.example.neptune.im.common.enums.code.NepFriendshipApplicationResponseCode;
import com.example.neptune.im.common.enums.status.NepFriendshipApplicationApproveStatus;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.NepFriendshipApplication;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.dto.NepAddFriendship;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.dto.NepSendFriendshipApplication;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.apply.NepApproveFriendshipApplicationResponse;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.apply.NepSendFriendshipApplicationRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.apply.NepApproveFriendshipApplicationRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.apply.NepSendFriendshipApplicationResponse;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.normal.NepModifyFriendshipResponse;
import com.fuyusakaiori.nep.im.service.core.friendship.mapper.INepFriendshipApplicationMapper;
import com.fuyusakaiori.nep.im.service.core.friendship.service.INepFriendshipApplicationService;
import com.fuyusakaiori.nep.im.service.util.NepCheckFriendshipApplicationParamUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@Service
public class NepFriendshipApplicationService implements INepFriendshipApplicationService {

    @Autowired
    private INepFriendshipApplicationMapper friendshipApplicationMapper;

    @Autowired
    private NepFriendshipServiceImpl friendshipServiceImpl;

    @Override
    @Transactional
    public NepSendFriendshipApplicationResponse sendFriendshipApplication(NepSendFriendshipApplicationRequest request) {
        // 0. 准备返回结果
        NepSendFriendshipApplicationResponse response = new NepSendFriendshipApplicationResponse();
        // 1. 参数校验
        if (NepCheckFriendshipApplicationParamUtil.checkSendFriendshipApplicationRequestParam(request)){
            log.error("NepFriendshipApplicationService sendFriendshipApplication: 参数校验失败 - request: {}", request);
            return response.setCode(NepBaseResponseCode.CHECK_PARAM_FAILURE.getCode())
                           .setMessage(NepBaseResponseCode.CHECK_PARAM_FAILURE.getMessage());
        }
        // 2. 获取变量
        NepRequestHeader header = request.getRequestHeader();
        Integer friendFromId = request.getFriendFromId();
        Integer friendToId = request.getFriendToId();
        // 3. 查询此前用户是否向对方发送过好友请求
        NepFriendshipApplication application = friendshipApplicationMapper.queryFriendshipApplicationByUserId(header.getAppId(), friendFromId, friendToId);
        // 4. 校验好友申请
        if (Objects.isNull(application)){
            // 4.1 如果好友申请不存在, 那么向数据库中插入新的好友申请
            int result = friendshipApplicationMapper.sendFriendshipApplication(header.getAppId(), transferToSendFriendshipApplication(request));
            if (result <= 0){
                log.error("NepFriendshipApplicationService sendFriendshipApplication: {} 向 {} 发送好友申请失败 - request: {}", friendFromId, friendToId, request);
                return response.setCode(NepFriendshipApplicationResponseCode.SEND_FRIEND_APPLICATION_FAIL.getCode())
                               .setMessage(NepFriendshipApplicationResponseCode.SEND_FRIEND_APPLICATION_FAIL.getMessage());
            }
        }else{
            // 4.2 如果好友申请存在, 那么就更新好友申请的信息
            int result = friendshipApplicationMapper.updateFriendshipApplication(header.getAppId(), transferToSendFriendshipApplication(request));
            if (result <= 0){
                log.error("NepFriendshipApplicationService sendFriendshipApplication: {} 向 {} 发送好友申请失败 (更新) - request: {}", friendFromId, friendToId, request);
                return response.setCode(NepFriendshipApplicationResponseCode.SEND_FRIEND_APPLICATION_FAIL.getCode())
                               .setMessage(NepFriendshipApplicationResponseCode.SEND_FRIEND_APPLICATION_FAIL.getMessage());
            }
        }
        return response.setCode(NepBaseResponseCode.SUCCESS.getCode())
                       .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
    }

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
        int result = friendshipApplicationMapper.approveFriendshipApplication(header.getAppId(), applyId, approveStatus, System.currentTimeMillis());
        if (result <= 0){
            log.error("NepFriendshipApplicationService approveFriendshipApplication: {} 审批 {} 发送的好友申请失败 - request: {}", application.getFriendshipToId(), application.getFriendshipFromId(), request);
            return response.setCode(NepFriendshipApplicationResponseCode.SEND_FRIEND_APPLICATION_FAIL.getCode())
                           .setMessage(NepFriendshipApplicationResponseCode.SEND_FRIEND_APPLICATION_FAIL.getMessage());
        }
        // 6. 如果同意好友申请, 那么执行好友添加
        return addFriendship(header, application, response);

    }

    private NepApproveFriendshipApplicationResponse addFriendship(NepRequestHeader header, NepFriendshipApplication application, NepApproveFriendshipApplicationResponse response) {
        // 1. 将好友申请中的信息填充到添加好友的实体类占用
        NepAddFriendship addFriendship = transferToAddFriendship(application);
        // 2. 执行好友添加
        NepModifyFriendshipResponse addFriendResponse = friendshipServiceImpl.addFriendship(header, addFriendship);
        // 3. 返回响应结果
        return response.setCode(addFriendResponse.getCode())
                .setMessage(addFriendResponse.getMessage());
    }

    private NepSendFriendshipApplication transferToSendFriendshipApplication(NepSendFriendshipApplicationRequest request){
        return new NepSendFriendshipApplication()
                       .setFriendFromId(request.getFriendFromId())
                       .setFriendToId(request.getFriendToId())
                       .setSource(request.getSource())
                       .setRemark(request.getRemark())
                       .setAdditionalInfo(request.getAdditionalInfo())
                       .setCreateTime(System.currentTimeMillis())
                       .setUpdateTime(System.currentTimeMillis());
    }

    private NepAddFriendship transferToAddFriendship(NepFriendshipApplication application){
        return new NepAddFriendship()
                       .setFriendFromId(application.getFriendshipFromId())
                       .setFriendToId(application.getFriendshipToId())
                       .setFriendshipSource(application.getApplySource())
                       .setFriendRemark(application.getApplyRemark())
                       .setAdditionalInfo(application.getApplyAdditionalInfo())
                       .setCreateTime(System.currentTimeMillis())
                       .setUpdateTime(System.currentTimeMillis());
    }
}
