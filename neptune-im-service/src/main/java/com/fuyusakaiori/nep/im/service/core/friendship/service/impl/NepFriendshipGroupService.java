package com.fuyusakaiori.nep.im.service.core.friendship.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.example.nep.im.common.enums.code.NepBaseResponseCode;
import com.example.nep.im.common.enums.code.NepFriendshipGroupResponseCode;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.NepFriendshipGroup;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.group.NepCreateFriendshipGroupRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.group.NepDeleteFriendshipGroupRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.group.NepQueryAllFriendshipGroupRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.group.NepCreateFriendshipGroupResponse;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.group.NepDeleteFriendshipGroupResponse;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.group.NepQueryAllFriendshipGroupResponse;
import com.fuyusakaiori.nep.im.service.core.friendship.service.INepFriendshipGroupService;
import com.fuyusakaiori.nep.im.service.util.check.NepCheckFriendshipGroupParamUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class NepFriendshipGroupService implements INepFriendshipGroupService {

    @Autowired
    private NepFriendshipGroupServiceImpl friendshipGroupServiceImpl;

    @Override
    public NepCreateFriendshipGroupResponse createFriendshipGroup(NepCreateFriendshipGroupRequest request) {
        // 0. 准备返回结果
        NepCreateFriendshipGroupResponse response = new NepCreateFriendshipGroupResponse();
        // 1. 校验参数
        if (!NepCheckFriendshipGroupParamUtil.checkCreateFriendshipGroupRequestParam(request)){
            response.setFriendshipGroupList(Collections.emptyList())
                    .setCode(NepBaseResponseCode.CHECK_PARAM_FAIL.getCode())
                    .setMessage(NepBaseResponseCode.CHECK_PARAM_FAIL.getMessage());
            log.error("NepFriendshipGroupService createFriendshipGroup: 参数校验失败 - request: {}, response: {}", request, response);
            return response;
        }
        // 2. 创建好友分组
        try {
            List<NepFriendshipGroup> friendshipGroupList = friendshipGroupServiceImpl.doCreateFriendshipGroup(request);
            if (CollectionUtil.isEmpty(friendshipGroupList)){
                response.setFriendshipGroupList(Collections.emptyList())
                        .setCode(NepFriendshipGroupResponseCode.FRIEND_GROUP_CREATE_FAIL.getCode())
                        .setMessage(NepFriendshipGroupResponseCode.FRIEND_GROUP_CREATE_FAIL.getMessage());
                log.error("NepFriendshipGroupService createFriendshipGroup: 创建分组失败 - request: {}, response: {}", request, response);
                return response;
            }
            response.setFriendshipGroupList(friendshipGroupList)
                    .setCode(NepBaseResponseCode.SUCCESS.getCode())
                    .setMessage((NepBaseResponseCode.SUCCESS.getMessage()));
            log.info("NepFriendshipGroupService createFriendshipGroup: 创建分组成功 - request: {}, response: {}", request, response);
            return response;
        }catch (Exception exception){
            response.setFriendshipGroupList(Collections.emptyList())
                    .setCode(NepBaseResponseCode.UNKNOWN_ERROR.getCode())
                    .setMessage((NepBaseResponseCode.UNKNOWN_ERROR.getMessage()));
            log.info("NepFriendshipGroupService createFriendshipGroup: 创建分组出现异常 - request: {}, response: {}", request, response);
            return response;
        }

    }

    @Override
    public NepDeleteFriendshipGroupResponse deleteFriendshipGroup(NepDeleteFriendshipGroupRequest request) {
        // 0. 准备返回结果
        NepDeleteFriendshipGroupResponse response = new NepDeleteFriendshipGroupResponse();
        // 1. 校验参数
        if (!NepCheckFriendshipGroupParamUtil.checkDeleteFriendshipGroupRequestParam(request)){
            response.setCode(NepBaseResponseCode.CHECK_PARAM_FAIL.getCode())
                    .setMessage(NepBaseResponseCode.CHECK_PARAM_FAIL.getMessage());
            log.error("NepFriendshipGroupService deleteFriendshipGroup: 参数校验失败 - request: {}, response: {}", request, response);
            return response;
        }
        // 2. 删除好友分组
        try {
            int result = friendshipGroupServiceImpl.doDeleteFriendshipGroup(request);
            if (result <= 0){
                response.setCode(NepFriendshipGroupResponseCode.FRIEND_GROUP_DELETE_FAIL.getCode())
                        .setMessage(NepFriendshipGroupResponseCode.FRIEND_GROUP_DELETE_FAIL.getMessage());
                log.error("NepFriendshipGroupService deleteFriendshipGroup: 删除分组失败 - request: {}, response: {}", request, response);
                return response;
            }
            response.setCode(NepBaseResponseCode.SUCCESS.getCode())
                    .setMessage((NepBaseResponseCode.SUCCESS.getMessage()));
            log.info("NepFriendshipGroupService deleteFriendshipGroup: 删除分组成功 - request: {}, response: {}", request, response);
            return response;
        }catch (Exception exception){
            response.setCode(NepBaseResponseCode.UNKNOWN_ERROR.getCode())
                    .setMessage(NepBaseResponseCode.UNKNOWN_ERROR.getMessage());
            log.error("NepFriendshipGroupService deleteFriendshipGroup: 删除分组出现异常 - request: {}, response: {}", request, response, exception);
            return response;
        }
    }

    @Override
    public NepQueryAllFriendshipGroupResponse queryAllFriendshipGroup(NepQueryAllFriendshipGroupRequest request) {
        // 0. 准备返回结果
        NepQueryAllFriendshipGroupResponse response = new NepQueryAllFriendshipGroupResponse();
        // 1. 校验参数
        if (!NepCheckFriendshipGroupParamUtil.checkQueryAllFriendshipGroupRequestParam(request)){
            response.setCode(NepBaseResponseCode.CHECK_PARAM_FAIL.getCode())
                    .setMessage(NepBaseResponseCode.CHECK_PARAM_FAIL.getMessage());
            log.error("NepFriendshipGroupService queryAllFriendshipGroup: 参数校验失败 - request: {}, response: {}", request, response);
            return response;
        }
        // 2. 查询所有好友分组
        try {
            List<NepFriendshipGroup> friendshipGroupList = friendshipGroupServiceImpl.doQueryAllFriendshipGroup(request);
            if (CollectionUtil.isEmpty(friendshipGroupList)){
                response.setFriendshipGroupList(Collections.emptyList())
                        .setCode(NepFriendshipGroupResponseCode.FRIEND_GROUP_NOT_EXIST.getCode())
                        .setMessage(NepFriendshipGroupResponseCode.FRIEND_GROUP_NOT_EXIST.getMessage());
                log.info("NepFriendshipGroupService queryAllFriendshipGroup: 查询好友分组失败 - request: {}, response: {}", request, response);
                return response;
            }
            response.setFriendshipGroupList(friendshipGroupList)
                    .setCode(NepBaseResponseCode.SUCCESS.getCode())
                    .setMessage((NepBaseResponseCode.SUCCESS.getMessage()));
            log.info("NepFriendshipGroupService queryAllFriendshipGroup: 查询好友分组成功 - request: {}, response: {}", request, response);
            return response;
        }catch (Exception exception){
            response.setFriendshipGroupList(Collections.emptyList())
                    .setCode(NepBaseResponseCode.UNKNOWN_ERROR.getCode())
                    .setMessage(NepBaseResponseCode.UNKNOWN_ERROR.getMessage());
            log.error("NepFriendshipGroupService queryAllFriendshipGroup: 查询好友分组出现异常 - request: {}, response: {}", request, response, exception);
            return response;
        }

    }
}
