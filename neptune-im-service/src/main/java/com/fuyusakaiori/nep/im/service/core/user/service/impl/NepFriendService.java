package com.fuyusakaiori.nep.im.service.core.user.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.example.nep.im.common.enums.code.NepBaseResponseCode;
import com.example.nep.im.common.enums.code.NepFriendshipApplicationResponseCode;
import com.example.nep.im.common.enums.code.NepFriendshipResponseCode;
import com.fuyusakaiori.nep.im.service.core.user.entity.dto.NepFriend;
import com.fuyusakaiori.nep.im.service.core.user.entity.dto.NepFriendApplication;
import com.fuyusakaiori.nep.im.service.core.user.entity.request.friend.NepQueryAllFriendApplicationRequest;
import com.fuyusakaiori.nep.im.service.core.user.entity.request.friend.NepQueryAllFriendRequest;
import com.fuyusakaiori.nep.im.service.core.user.entity.request.friend.NepQueryFriendRequest;
import com.fuyusakaiori.nep.im.service.core.user.entity.response.friend.NepQueryAllFriendApplicationResponse;
import com.fuyusakaiori.nep.im.service.core.user.entity.response.friend.NepQueryFriendResponse;
import com.fuyusakaiori.nep.im.service.core.user.service.INepFriendService;
import com.fuyusakaiori.nep.im.service.util.check.NepCheckFriendParamUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class NepFriendService implements INepFriendService {

    @Autowired
    private NepFriendServiceImpl friendServiceImpl;

    @Override
    public NepQueryFriendResponse queryAllFriend(NepQueryAllFriendRequest request) {
        // 0. 准备响应结果
        NepQueryFriendResponse response = new NepQueryFriendResponse();
        // 1. 参数校验
        if (!NepCheckFriendParamUtil.checkNepQueryAllFriendRequestParam(request)){
            response.setCode(NepBaseResponseCode.CHECK_PARAM_FAIL.getCode())
                    .setMessage(NepBaseResponseCode.CHECK_PARAM_FAIL.getMessage());
            log.error("NepFriendUserService queryAllFriend: 请求头中的参数检查失败 - request: {}, response: {}", request, response);
            return response;
        }
        try {
            List<NepFriend> friendList = friendServiceImpl.doQueryAllFriend(request);
            if (CollectionUtil.isEmpty(friendList)){
                response.setFriendList(Collections.emptyList())
                        .setCode(NepFriendshipResponseCode.FRIENDSHIP_NOT_EXIST.getCode())
                        .setMessage(NepFriendshipResponseCode.FRIENDSHIP_NOT_EXIST.getMessage());
                log.info("NepFriendUserService queryAllFriend: 用户没有任何好友 - request: {}, response: {}", request, response);
                return response;
            }
            response.setFriendList(friendList)
                    .setCode(NepBaseResponseCode.SUCCESS.getCode())
                    .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
            log.info("NepFriendUserService queryAllFriend: 成功查询到用户的所有好友 - request: {}, response: {}", request, response);
            return response;
        }catch (Exception exception){
            response.setFriendList(Collections.emptyList())
                    .setCode(NepBaseResponseCode.UNKNOWN_ERROR.getCode())
                    .setMessage(NepBaseResponseCode.UNKNOWN_ERROR.getMessage());
            log.info("NepFriendUserService queryAllFriend: 查询到用户的所有好友出现异常 - request: {}, response: {}", request, response, exception);
            return response;
        }
    }

    @Override
    public NepQueryFriendResponse queryFriend(NepQueryFriendRequest request) {
        // 0. 准备响应结果
        NepQueryFriendResponse response = new NepQueryFriendResponse();
        // 1. 参数校验
        if (!NepCheckFriendParamUtil.checkNepQueryFriendByAccountRequestParam(request)){
            response.setCode(NepBaseResponseCode.CHECK_PARAM_FAIL.getCode())
                    .setMessage(NepBaseResponseCode.CHECK_PARAM_FAIL.getMessage());
            log.error("NepFriendUserService queryFriend: 参数校验失败 - request: {}, response: {}", request, response);
            return response;
        }
        try {
            List<NepFriend> friendList = friendServiceImpl.doQueryFriend(request);
            if (CollectionUtil.isEmpty(friendList)){
                response.setFriendList(Collections.emptyList())
                        .setCode(NepFriendshipResponseCode.FRIENDSHIP_NOT_EXIST.getCode())
                        .setMessage(NepFriendshipResponseCode.FRIENDSHIP_NOT_EXIST.getMessage());
                log.info("NepFriendUserService queryFriend: 没有查询到好友 - request: {}, response: {}", request, response);
                return response;
            }
            response.setFriendList(friendList)
                    .setCode(NepBaseResponseCode.SUCCESS.getCode())
                    .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
            log.info("NepFriendUserService queryFriend: 成功查询到好友 - request: {}, response: {}", request, response);
            return response;
        } catch (Exception exception) {
            response.setFriendList(Collections.emptyList())
                    .setCode(NepBaseResponseCode.UNKNOWN_ERROR.getCode())
                    .setMessage(NepBaseResponseCode.UNKNOWN_ERROR.getMessage());
            log.error("NepFriendUserService queryFriend: 查询好友出现异常 - request: {}, response: {}", request, response, exception);
            return response;
        }
    }

    @Override
    public NepQueryAllFriendApplicationResponse queryAllFriendApplication(NepQueryAllFriendApplicationRequest request) {
        // 0. 响应结果准备
        NepQueryAllFriendApplicationResponse response = new NepQueryAllFriendApplicationResponse();
        // 1. 参数校验
        if (!NepCheckFriendParamUtil.checkNepQueryAllFriendApplicationRequestParam(request)){
            response.setApplicationList(Collections.emptyList())
                           .setCode(NepBaseResponseCode.CHECK_PARAM_FAIL.getCode())
                           .setMessage(NepBaseResponseCode.CHECK_PARAM_FAIL.getMessage());
            log.error("NepFriendService queryAllFriendApplication: 参数校验失败 - request: {}， response: {}", request, response);
            return response;
        }
        try {
            List<NepFriendApplication> friendApplicationList = friendServiceImpl.doQueryAllFriendApplication(request);
            if (CollectionUtil.isEmpty(friendApplicationList)){
                response.setApplicationList(Collections.emptyList())
                        .setCode(NepFriendshipApplicationResponseCode.FRIEND_APPLICATION_NOT_EXIST.getCode())
                        .setMessage(NepFriendshipApplicationResponseCode.FRIEND_APPLICATION_NOT_EXIST.getMessage());
                log.info("NepFriendUserService queryAllFriendApplication: 没有查询到好友申请 - request: {}, response: {}", request, response);
                return response;
            }
            response.setApplicationList(friendApplicationList)
                    .setCode(NepBaseResponseCode.SUCCESS.getCode())
                    .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
            log.info("NepFriendUserService queryAllFriendApplication: 成功查询到好友申请 - request: {}, response: {}", request, response);
            return response;
        } catch (Exception exception) {
            response.setApplicationList(Collections.emptyList())
                    .setCode(NepBaseResponseCode.UNKNOWN_ERROR.getCode())
                    .setMessage(NepBaseResponseCode.UNKNOWN_ERROR.getMessage());
            log.error("NepFriendUserService queryAllFriendApplication: 查询好友申请出现异常 - request: {}, response: {}", request, response, exception);
            return response;
        }
    }
}
