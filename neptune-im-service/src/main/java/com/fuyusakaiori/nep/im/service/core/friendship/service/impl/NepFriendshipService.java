package com.fuyusakaiori.nep.im.service.core.friendship.service.impl;

import com.example.nep.im.common.entity.request.NepRequestHeader;
import com.example.nep.im.common.enums.code.NepBaseResponseCode;
import com.example.nep.im.common.enums.code.NepFriendshipResponseCode;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.normal.*;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.normal.NepAddFriendshipResponse;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.normal.NepCheckFriendshipResponse;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.normal.NepEditFriendshipRemarkResponse;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.normal.NepReleaseFriendshipResponse;
import com.fuyusakaiori.nep.im.service.core.friendship.service.INepFriendshipService;
import com.fuyusakaiori.nep.im.service.core.user.entity.dto.NepFriend;
import com.fuyusakaiori.nep.im.service.util.check.NepCheckFriendshipParamUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@Service
public class NepFriendshipService implements INepFriendshipService {

    @Autowired
    private NepFriendshipServiceImpl friendshipServiceImpl;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public NepAddFriendshipResponse addFriendship(NepAddFriendshipRequest request) {
        // 0. 准备响应结果
        NepAddFriendshipResponse response = new NepAddFriendshipResponse();
        // 1. 参数校验
        if (!NepCheckFriendshipParamUtil.checkNepAddFriendshipRequestParam(request)){
            response.setNewFriend(null)
                    .setCode(NepBaseResponseCode.CHECK_PARAM_FAIL.getCode())
                    .setMessage(NepBaseResponseCode.CHECK_PARAM_FAIL.getMessage());
            log.error("NeptuneFriendshipService addFriendship: 参数校验失败 - request: {}, response: {}", request, response);
            return response;
        }
        // 2. 执行好友添加
        try {
            NepFriend newFriend = friendshipServiceImpl.doAddFriendship(request);
            if (Objects.isNull(newFriend)){
                response.setNewFriend(null)
                        .setCode(NepFriendshipResponseCode.FRIENDSHIP_ADD_FAIL.getCode())
                        .setMessage(NepFriendshipResponseCode.FRIENDSHIP_ADD_FAIL.getMessage());
                log.error("NeptuneFriendshipService addFriendship: 发起好友添加失败 - request: {}, response: {}", request, response);
            }else{
                // TODO 3. 执行回调
                response.setNewFriend(newFriend)
                        .setCode(NepBaseResponseCode.SUCCESS.getCode())
                        .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
                log.info("NeptuneFriendshipService addFriendship: 发起好友添加成功 - request: {}, response: {}", request, response);
            }
            return response;
        }catch (Exception exception){
            response.setNewFriend(null)
                    .setCode(NepBaseResponseCode.UNKNOWN_ERROR.getCode())
                    .setMessage(NepBaseResponseCode.UNKNOWN_ERROR.getMessage());
            log.error("NeptuneFriendshipService addFriendship: 发起好友添加出现异常 - request: {}, response: {}", request, response, exception);
            return response;
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public NepEditFriendshipRemarkResponse editFriendshipRemark(NepEditFriendshipRemarkRequest request) {
        // 0. 准备响应结果
        NepEditFriendshipRemarkResponse response = new NepEditFriendshipRemarkResponse();
        // 1. 参数校验
        if (!NepCheckFriendshipParamUtil.checkNepEditFriendshipRequestParam(request)){
            log.error("NeptuneFriendshipService editFriendship: 参数校验失败 - request: {}", request);
            return response.setCode(NepBaseResponseCode.CHECK_PARAM_FAIL.getCode())
                    .setMessage(NepBaseResponseCode.CHECK_PARAM_FAIL.getMessage());
        }
        try {
            int result = friendshipServiceImpl.doEditFriendshipRemark(request);
            if (result <= 0){
                response.setCode(NepFriendshipResponseCode.FRIENDSHIP_UPDATE_FAIL.getCode())
                        .setMessage(NepFriendshipResponseCode.FRIENDSHIP_UPDATE_FAIL.getMessage());
                log.error("NeptuneFriendshipService editFriendshipRemark: 更新好友关系失败 - request: {}, response: {}", request, response);
                return response;
            }
            response.setCode(NepBaseResponseCode.SUCCESS.getCode())
                    .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
            log.info("NeptuneFriendshipService editFriendshipRemark: 更新好友关系成功 - request: {}, response: {}", request, response);
            return response;
        }catch (Exception exception){
            response.setCode(NepBaseResponseCode.UNKNOWN_ERROR.getCode())
                    .setMessage(NepBaseResponseCode.UNKNOWN_ERROR.getMessage());
            log.error("NeptuneFriendshipService editFriendshipRemark: 编辑好友备注出现异常 - request: {}, response: {}", request, response, exception);
            return response;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public NepReleaseFriendshipResponse releaseFriendship(NepReleaseFriendshipRequest request) {
        // 0. 准备响应结果
        NepReleaseFriendshipResponse response = new NepReleaseFriendshipResponse();
        // 1. 参数校验
        if (!NepCheckFriendshipParamUtil.checkNepReleaseFriendshipRequestParam(request)){
            response.setCode(NepBaseResponseCode.CHECK_PARAM_FAIL.getCode())
                    .setMessage(NepBaseResponseCode.CHECK_PARAM_FAIL.getMessage());
            log.error("NeptuneFriendshipService releaseFriendship: 参数校验失败 - request: {}", request);
            return response;
        }
        // 2. 删除好友关系
        try {
            int result = friendshipServiceImpl.doReleaseFriendship(request);
            if (result <= 0){
                response.setCode(NepFriendshipResponseCode.FRIENDSHIP_RELEASE_FAIL.getCode())
                        .setMessage(NepFriendshipResponseCode.FRIENDSHIP_RELEASE_FAIL.getMessage());
                log.error("NeptuneFriendshipService releaseFriendship: 删除好友关系失败 - request: {}, response: {}", request, response);
                return response;
            }
            response.setCode(NepBaseResponseCode.SUCCESS.getCode())
                    .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
            log.info("NeptuneFriendshipService releaseFriendship: 删除好友关系成功 - request: {}, response: {}", request, response);
            return response;
        }catch (Exception exception){
            response.setCode(NepBaseResponseCode.UNKNOWN_ERROR.getCode())
                    .setMessage(NepBaseResponseCode.UNKNOWN_ERROR.getMessage());
            log.error("NeptuneFriendshipService releaseFriendship: 删除好友关系出现异常 - request: {}, response: {}", request, response, exception);
            return response;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public NepReleaseFriendshipResponse releaseAllFriendship(NepReleaseAllFriendshipRequest request) {
        // 0. 准备响应结果
        NepReleaseFriendshipResponse response = new NepReleaseFriendshipResponse();
        // 1. 参数校验
        if (!NepCheckFriendshipParamUtil.checkNepReleaseAllFriendshipRequestParam(request)){
            response.setCode(NepBaseResponseCode.CHECK_PARAM_FAIL.getCode())
                    .setMessage(NepBaseResponseCode.CHECK_PARAM_FAIL.getMessage());
            log.error("NeptuneFriendshipService releaseAllFriendship: 参数校验失败 - request: {}, response: {}", request, response);
            return response;
        }
        // 2. 删除所有好友
        try {
            int result = friendshipServiceImpl.doReleaseAllFriendship(request);
            if (result <= 0){
                response.setCode(NepFriendshipResponseCode.FRIENDSHIP_RELEASE_ALL_FAIL.getCode())
                        .setMessage(NepFriendshipResponseCode.FRIENDSHIP_RELEASE_ALL_FAIL.getMessage());
                log.error("NeptuneFriendshipService releaseAllFriendship: 清空好友关系失败 - request: {}, response: {}", request, response);
                return response;
            }
            response.setCode(NepBaseResponseCode.SUCCESS.getCode())
                    .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
            log.error("NeptuneFriendshipService releaseAllFriendship: 成功清空好友关系- request: {}, response: {}", request, response);
            return response;
        }catch (Exception exception){
            response.setCode(NepBaseResponseCode.UNKNOWN_ERROR.getCode())
                    .setMessage(NepBaseResponseCode.UNKNOWN_ERROR.getMessage());
            log.error("NeptuneFriendshipService releaseAllFriendship: 清空好友关系出现异常 - request: {}, response: {}", request, response, exception);
            return response;
        }

    }

    @Override
    public NepCheckFriendshipResponse checkFriendship(NepCheckFriendshipRequest request) {
        NepCheckFriendshipResponse response = new NepCheckFriendshipResponse();
        // 1. 参数校验
        if (!NepCheckFriendshipParamUtil.checkNepVerifyFriendshipRequestParam(request)){
            log.error("NeptuneFriendshipService checkFriendship: 参数校验失败 - request: {}", request);
            return response.setCode(NepBaseResponseCode.CHECK_PARAM_FAIL.getCode())
                           .setMessage(NepBaseResponseCode.CHECK_PARAM_FAIL.getMessage());
        }
        // 2. 获取变量
        NepRequestHeader header = request.getRequestHeader();
        Integer friendFromId = request.getFriendFromId();
        Integer friendToId = request.getFriendToId();
        Integer checkType = request.getCheckType();
        // 3. 设置返回结果中的默认值
        response.setFriendFromId(friendFromId)
                .setFriendToId(friendToId);
        // 4. 执行校验方法
        int checkStatus = friendshipServiceImpl.doCheckFriendship(header.getAppId(), friendFromId, friendToId, checkType);
        // 5. 校验执行结果
        if (checkStatus < 0){
            log.error("NeptuneFriendshipService checkFriendship: 校验好友关系失败 - checkStatus: {}", checkStatus);
            return response.setStatus(checkStatus)
                           .setCode(NepFriendshipResponseCode.FRIENDSHIP_CHECK_FAIL.getCode())
                           .setMessage(NepFriendshipResponseCode.FRIENDSHIP_CHECK_FAIL.getMessage());
        }
        return response.setStatus(checkStatus)
                       .setCode(NepBaseResponseCode.SUCCESS.getCode())
                       .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
    }
}
