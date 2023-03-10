package com.fuyusakaiori.nep.im.service.core.friendship.service.impl;

import com.example.nep.im.common.entity.request.NepRequestHeader;
import com.example.nep.im.common.enums.code.NepBaseResponseCode;
import com.example.nep.im.common.enums.code.NepFriendshipBlackResponseCode;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.black.NepAddFriendshipBlackRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.black.NepCheckFriendshipBlackRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.black.NepRemoveFriendshipBlackRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.black.NepCheckFriendshipBlackResponse;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.normal.NepModifyFriendshipResponse;
import com.fuyusakaiori.nep.im.service.core.friendship.service.INepFriendshipBlackService;
import com.fuyusakaiori.nep.im.service.util.NepCheckFriendshipBlackParamUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NepFriendshipBlackService implements INepFriendshipBlackService {

    @Autowired
    private NepFriendshipBlackServiceImpl friendshipBlackServiceImpl;

    @Override
    public NepModifyFriendshipResponse addFriendInBlackList(NepAddFriendshipBlackRequest request) {
        // 0. 准备响应结果
        NepModifyFriendshipResponse response = new NepModifyFriendshipResponse();
        // 1. 参数校验
        if (!NepCheckFriendshipBlackParamUtil.checkAddFriendInBlackListRequestParam(request)){
            log.error("NepFriendshipBlackService addFriendInBlackList: 参数校验失败 - request: {}", request);
            return response.setCode(NepBaseResponseCode.CHECK_PARAM_FAILURE.getCode())
                           .setMessage(NepBaseResponseCode.CHECK_PARAM_FAILURE.getMessage());
        }
        // 2. 获取变量
        NepRequestHeader header = request.getRequestHeader();
        Integer friendFromId = request.getFriendFromId();
        Integer friendToId = request.getFriendToId();
        // 3. 拉黑用户
        int result = friendshipBlackServiceImpl.doAddFriendInBlackList(header, friendFromId, friendToId);
        if (result <= 0){
            log.error("NepFriendshipBlackService addFriendInBlackList: 好友拉黑失败 - request: {}", request);
            return response.setCode(NepFriendshipBlackResponseCode.FRIEND_BLACK_FAIL.getCode())
                           .setMessage(NepFriendshipBlackResponseCode.FRIEND_BLACK_FAIL.getMessage());
        }
        return response.setCode(NepBaseResponseCode.SUCCESS.getCode())
                       .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
    }

    @Override
    public NepModifyFriendshipResponse removeFriendInBlackList(NepRemoveFriendshipBlackRequest request) {
        // 0. 准备响应结果
        NepModifyFriendshipResponse response = new NepModifyFriendshipResponse();
        // 1. 参数校验
        if (!NepCheckFriendshipBlackParamUtil.checkRemoveFriendInBlackListRequestParam(request)){
            log.error("NepFriendshipBlackService removeFriendInBlackList: 参数校验失败 - request: {}", request);
            return response.setCode(NepBaseResponseCode.CHECK_PARAM_FAILURE.getCode())
                           .setMessage(NepBaseResponseCode.CHECK_PARAM_FAILURE.getMessage());
        }
        // 2. 获取变量
        NepRequestHeader header = request.getRequestHeader();
        Integer friendFromId = request.getFriendFromId();
        Integer friendToId = request.getFriendToId();
        // 3. 撤销拉黑
        int result = friendshipBlackServiceImpl.doRemoveFriendInBlackList(header, friendFromId, friendToId);
        if (result <= 0){
            log.error("NepFriendshipBlackService removeFriendInBlackList: 好友撤销拉黑失败 - request: {}", request);
            return response.setCode(NepFriendshipBlackResponseCode.FRIEND_WHITE_FAIL.getCode())
                           .setMessage(NepFriendshipBlackResponseCode.FRIEND_WHITE_FAIL.getMessage());
        }
        return response.setCode(NepBaseResponseCode.SUCCESS.getCode())
                       .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
    }

    @Override
    public NepCheckFriendshipBlackResponse checkFriendInBlackList(NepCheckFriendshipBlackRequest request) {
        // 0. 准备响应结果
        NepCheckFriendshipBlackResponse response = new NepCheckFriendshipBlackResponse();
        // 1. 参数校验
        if (!NepCheckFriendshipBlackParamUtil.checkVerifyFriendInBlackListRequestParam(request)){
            log.error("NepFriendshipBlackService checkFriendInBlackList: 参数校验失败 - request: {}", request);
            return response.setCode(NepBaseResponseCode.CHECK_PARAM_FAILURE.getCode())
                           .setMessage(NepBaseResponseCode.CHECK_PARAM_FAILURE.getMessage());
        }
        // 2. 获取变量
        NepRequestHeader header = request.getRequestHeader();
        Integer friendFromId = request.getFriendFromId();
        Integer friendToId = request.getFriendToId();
        Integer checkType = request.getCheckType();
        int result = friendshipBlackServiceImpl.checkFriendInBlackList(header, friendFromId, friendToId, checkType);
        if (result < 0){
            log.error("NepFriendshipBlackService checkFriendInBlackList: 校验好友关系的拉黑状态失败 - fromId: {}, toId: {}", friendFromId, friendToId);
            return response.setCode(NepFriendshipBlackResponseCode.FRIEND_CHECK_BLACK_FAIL.getCode())
                           .setMessage(NepFriendshipBlackResponseCode.FRIEND_CHECK_BLACK_FAIL.getMessage());
        }
        return response.setStatus(result)
                       .setCode(NepBaseResponseCode.SUCCESS.getCode())
                       .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
    }

}
