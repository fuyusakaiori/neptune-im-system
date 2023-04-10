package com.fuyusakaiori.nep.im.service.core.friendship.service.impl;

import com.example.nep.im.common.entity.request.NepRequestHeader;
import com.example.nep.im.common.enums.code.NepBaseResponseCode;
import com.example.nep.im.common.enums.code.NepFriendshipBlackResponseCode;
import com.fuyusakaiori.nep.im.service.config.NepApplicationConfig;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.black.NepAddFriendshipBlackRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.black.NepCheckFriendshipBlackRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.black.NepRemoveFriendshipBlackRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.black.NepBlackFriendshipResponse;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.black.NepCheckFriendshipBlackResponse;
import com.fuyusakaiori.nep.im.service.core.friendship.service.INepFriendshipBlackService;
import com.fuyusakaiori.nep.im.service.util.callback.INepCallBackService;
import com.fuyusakaiori.nep.im.service.util.check.NepCheckFriendshipBlackParamUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NepFriendshipBlackService implements INepFriendshipBlackService {

    @Autowired
    private NepApplicationConfig applicationConfig;

    @Autowired
    private INepCallBackService callBackService;

    @Autowired
    private NepFriendshipBlackServiceImpl friendshipBlackServiceImpl;

    @Override
    public NepBlackFriendshipResponse addFriendInBlackList(NepAddFriendshipBlackRequest request) {
        // 0. 准备响应结果
        NepBlackFriendshipResponse response = new NepBlackFriendshipResponse();
        // 1. 参数校验
        if (!NepCheckFriendshipBlackParamUtil.checkAddFriendInBlackListRequestParam(request)){
            response.setCode(NepBaseResponseCode.CHECK_PARAM_FAIL.getCode())
                    .setMessage(NepBaseResponseCode.CHECK_PARAM_FAIL.getMessage());
            log.error("NepFriendshipBlackService addFriendInBlackList: 参数校验失败 - request: {}, response: {}", request, response);
            return response;
        }
        // 2. 拉黑用户
        try {
            int result = friendshipBlackServiceImpl.doAddFriendInBlackList(request);
            if (result <= 0){
                response.setCode(NepFriendshipBlackResponseCode.FRIEND_BLACK_FAIL.getCode())
                        .setMessage(NepFriendshipBlackResponseCode.FRIEND_BLACK_FAIL.getMessage());
                log.error("NepFriendshipBlackService addFriendInBlackList: 好友拉黑失败 - request: {}, response: {}", request, response);
                return response;
            }
            // TODO 3. 执行回调
            response.setCode(NepBaseResponseCode.SUCCESS.getCode())
                    .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
            log.info("NepFriendshipBlackService addFriendInBlackList: 好友拉黑成功 - request: {}, response: {}", request, response);
            return response;
        }catch (Exception exception){
            response.setCode(NepBaseResponseCode.UNKNOWN_ERROR.getCode())
                    .setMessage(NepBaseResponseCode.UNKNOWN_ERROR.getMessage());
            log.error("NepFriendshipBlackService addFriendInBlackList: 好友拉黑出现异常 - request: {}, response: {}", request, response);
            return response;
        }
    }

    @Override
    public NepBlackFriendshipResponse removeFriendInBlackList(NepRemoveFriendshipBlackRequest request) {
        // 0. 准备响应结果
        NepBlackFriendshipResponse response = new NepBlackFriendshipResponse();
        // 1. 参数校验
        if (!NepCheckFriendshipBlackParamUtil.checkRemoveFriendInBlackListRequestParam(request)){
            response.setCode(NepBaseResponseCode.CHECK_PARAM_FAIL.getCode())
                    .setMessage(NepBaseResponseCode.CHECK_PARAM_FAIL.getMessage());
            log.error("NepFriendshipBlackService removeFriendInBlackList: 参数校验失败 - request: {}, response: {}", request, response);
            return response;
        }
        // 2. 撤销拉黑
        try {
            int result = friendshipBlackServiceImpl.doRemoveFriendInBlackList(request);
            if (result <= 0){
                response.setCode(NepFriendshipBlackResponseCode.FRIEND_WHITE_FAIL.getCode())
                        .setMessage(NepFriendshipBlackResponseCode.FRIEND_WHITE_FAIL.getMessage());
                log.error("NepFriendshipBlackService removeFriendInBlackList: 好友撤销拉黑失败 - request: {}， response: {}", request, response);
                return response;
            }
            // TODO 3. 执行回调
            response.setCode(NepBaseResponseCode.SUCCESS.getCode())
                    .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
            log.info("NepFriendshipBlackService removeFriendInBlackList: 好友撤销拉黑成功 - request: {}， response: {}", request, response);
            return response;
        } catch (Exception exception) {
            response.setCode(NepBaseResponseCode.UNKNOWN_ERROR.getCode())
                    .setMessage(NepBaseResponseCode.UNKNOWN_ERROR.getMessage());
            log.error("NepFriendshipBlackService removeFriendInBlackList: 好友撤销拉黑出现异常 - request: {}, response: {}", request, response);
            return response;
        }
    }

    @Override
    public NepCheckFriendshipBlackResponse checkFriendInBlackList(NepCheckFriendshipBlackRequest request) {
        // 0. 准备响应结果
        NepCheckFriendshipBlackResponse response = new NepCheckFriendshipBlackResponse();
        // 1. 参数校验
        if (!NepCheckFriendshipBlackParamUtil.checkVerifyFriendInBlackListRequestParam(request)){
            log.error("NepFriendshipBlackService checkFriendInBlackList: 参数校验失败 - request: {}", request);
            return response.setCode(NepBaseResponseCode.CHECK_PARAM_FAIL.getCode())
                           .setMessage(NepBaseResponseCode.CHECK_PARAM_FAIL.getMessage());
        }
        // 2. 获取变量
        NepRequestHeader header = request.getHeader();
        Integer friendFromId = request.getFriendFromId();
        Integer friendToId = request.getFriendToId();
        Integer checkType = request.getCheckType();
        int result = friendshipBlackServiceImpl.doCheckFriendInBlackList(header.getAppId(), friendFromId, friendToId, checkType);
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
