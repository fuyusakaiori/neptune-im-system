package com.fuyusakaiori.nep.im.service.core.group.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.example.nep.im.common.enums.code.NepBaseResponseCode;
import com.example.nep.im.common.enums.code.NepGroupResponseCode;
import com.fuyusakaiori.nep.im.service.core.group.entity.NepGroup;
import com.fuyusakaiori.nep.im.service.core.group.entity.request.*;
import com.fuyusakaiori.nep.im.service.core.group.entity.response.*;
import com.fuyusakaiori.nep.im.service.core.group.service.INepGroupService;
import com.fuyusakaiori.nep.im.service.core.group.entity.response.NepUploadGroupAvatarResponse;
import com.fuyusakaiori.nep.im.service.util.check.NepCheckGroupParamUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class NepGroupService implements INepGroupService {

    @Autowired
    private NepGroupServiceImpl groupServiceImpl;


    @Override
    @Transactional
    public NepCreateGroupResponse createGroup(NepCreateGroupRequest request) {
        NepCreateGroupResponse response = new NepCreateGroupResponse();
        if (!NepCheckGroupParamUtil.checkNepCreateGroupRequestParam(request)){
            response.setCode(NepBaseResponseCode.CHECK_PARAM_FAIL.getCode())
                    .setMessage(NepBaseResponseCode.CHECK_PARAM_FAIL.getMessage());
            log.error("NepGroupService createGroup: 参数校验失败 - request: {}, response: {}", request, response);
            return response;
        }
        try {
            int result = groupServiceImpl.doCreateGroup(request);
            if (result <= 0){
                response.setCode(NepGroupResponseCode.CREATE_GROUP_FAIL.getCode())
                        .setMessage(NepGroupResponseCode.CREATE_GROUP_FAIL.getMessage());
                log.error("NepGroupService createGroup: 创建群组失败 - request: {}, response: {}", request, response);
                return response;
            }
            response.setCode(NepBaseResponseCode.SUCCESS.getCode())
                    .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
            log.info("NepGroupService createGroup: 创建群组成功 - request: {}, response: {}", request, response);
            return response;
        }catch (Exception exception){
            response.setCode(NepBaseResponseCode.UNKNOWN_ERROR.getCode())
                    .setMessage(NepBaseResponseCode.UNKNOWN_ERROR.getMessage());
            log.error("NepGroupService createGroup: 创建群组时发生异常 - request: {}, response: {}", request, response, exception);
            return response;
        }
    }

    @Override
    @Transactional
    public NepEditGroupResponse editGroupInfo(NepEditGroupRequest request) {
        NepEditGroupResponse response = new NepEditGroupResponse();
        if (!NepCheckGroupParamUtil.checkNepEditGroupRequestParam(request)){
            response.setCode(NepBaseResponseCode.CHECK_PARAM_FAIL.getCode())
                    .setMessage(NepBaseResponseCode.CHECK_PARAM_FAIL.getMessage());
            log.error("NepGroupService editGroupInfo: 参数校验失败 - request: {}, response: {}", request, response);
            return response;
        }
        try {
            int result = groupServiceImpl.doEditGroupInfo(request);
            if (result <= 0){
                response.setCode(NepGroupResponseCode.UPDATE_GROUP_FAIL.getCode())
                        .setMessage(NepGroupResponseCode.UPDATE_GROUP_FAIL.getMessage());
                log.error("NepGroupService editGroupInfo: 更新群组信息失败 - request: {}, response: {}", request, response);
                return response;
            }
            response.setCode(NepBaseResponseCode.SUCCESS.getCode())
                    .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
            log.info("NepGroupService editGroupInfo: 更新群组信息成功 - request: {}, response: {}", request, response);
            return response;
        }catch (Exception exception){
            response.setCode(NepBaseResponseCode.UNKNOWN_ERROR.getCode())
                    .setMessage(NepBaseResponseCode.UNKNOWN_ERROR.getMessage());
            log.error("NepGroupService editGroupInfo: 更新群组信息发生异常 - request: {}, response: {}", request, response, exception);
            return response;
        }
    }

    @Override
    public NepUploadGroupAvatarResponse updateGroupAvatar(NepUploadGroupAvatarRequest request) {
        NepUploadGroupAvatarResponse response = new NepUploadGroupAvatarResponse();
        if (!NepCheckGroupParamUtil.checkNepUploadGroupAvatarRequestParam(request)){
            response.setCode(NepBaseResponseCode.CHECK_PARAM_FAIL.getCode())
                    .setMessage(NepBaseResponseCode.CHECK_PARAM_FAIL.getMessage());
            log.error("NepGroupService updateGroupAvatar: 参数校验失败 - request: {}, response: {}", request, response);
            return response;
        }
        try {
            int result = groupServiceImpl.doUpdateGroupAvatar(request);
            if (result <= 0){
                response.setCode(NepGroupResponseCode.UPDATE_GROUP_AVATAR_FAIL.getCode())
                        .setMessage(NepGroupResponseCode.UPDATE_GROUP_AVATAR_FAIL.getMessage());
                log.error("NepGroupService updateGroupAvatar: 更新群组头像失败 - request: {}, response: {}", request, response);
                return response;
            }
            response.setCode(NepBaseResponseCode.SUCCESS.getCode())
                    .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
            log.info("NepGroupService updateGroupAvatar: 更新群组头像成功 - request: {}, response: {}", request, response);
            return response;
        }catch (Exception exception){
            response.setCode(NepBaseResponseCode.UNKNOWN_ERROR.getCode())
                    .setMessage(NepBaseResponseCode.UNKNOWN_ERROR.getMessage());
            log.error("NepGroupService updateGroupAvatar: 更新群组头像出现异常 - request: {}, response: {}", request, response, exception);
            return response;
        }
    }

    @Override
    @Transactional
    public NepDissolveGroupResponse dissolveGroup(NepDissolveGroupRequest request) {
        NepDissolveGroupResponse response = new NepDissolveGroupResponse();
        if (!NepCheckGroupParamUtil.checkNepDissolveGroupRequestParam(request)){
            response.setCode(NepBaseResponseCode.CHECK_PARAM_FAIL.getCode())
                    .setMessage(NepBaseResponseCode.CHECK_PARAM_FAIL.getMessage());
            log.error("NepGroupService dissolveGroup: 参数校验失败 - request: {}, response: {}", request, response);
            return response;
        }
        try {
            int result = groupServiceImpl.doDissolveGroup(request);
            if (result <= 0){
                response.setCode(NepGroupResponseCode.DISSOLVE_GROUP_FAIL.getCode())
                        .setMessage(NepGroupResponseCode.DISSOLVE_GROUP_FAIL.getMessage());
                log.error("NepGroupService dissolveGroup: 解散群组失败 - request: {}, response: {}", request, response);
                return response;
            }
            response.setCode(NepBaseResponseCode.SUCCESS.getCode())
                    .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
            log.info("NepGroupService dissolveGroup: 解散群组成功 - request: {}, response: {}", request, response);
            return response;
        }catch (Exception exception){
            response.setCode(NepBaseResponseCode.UNKNOWN_ERROR.getCode())
                    .setMessage(NepBaseResponseCode.UNKNOWN_ERROR.getMessage());
            log.error("NepGroupService dissolveGroup: 解散群组时发生异常 - request: {}, response: {}", request, response, exception);
            return response;
        }
    }

    @Override
    @Transactional
    public NepMuteGroupResponse muteGroupChat(NepMuteGroupRequest request) {
        NepMuteGroupResponse response = new NepMuteGroupResponse();
        if (!NepCheckGroupParamUtil.checkNepMuteGroupRequestParam(request)){
            response.setCode(NepBaseResponseCode.CHECK_PARAM_FAIL.getCode())
                    .setMessage(NepBaseResponseCode.CHECK_PARAM_FAIL.getMessage());
            log.error("NepGroupService muteGroupChat: 参数校验失败 - request: {}, response: {}", request, response);
            return response;
        }
        try {
            int result = groupServiceImpl.doMuteGroupChat(request);
            if (result <= 0){
                response.setCode(NepGroupResponseCode.MUTE_GROUP_FAIL.getCode())
                        .setMessage(NepGroupResponseCode.MUTE_GROUP_FAIL.getMessage());
                log.error("NepGroupService muteGroupChat: 全局禁言开启失败 - request: {}, response: {}", request, response);
                return response;
            }
            response.setCode(NepBaseResponseCode.SUCCESS.getCode())
                    .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
            log.info("NepGroupService muteGroupChat: 全局禁言开启成功 - request: {}, response: {}", request, response);
            return response;
        }catch (Exception exception){
            response.setCode(NepBaseResponseCode.UNKNOWN_ERROR.getCode())
                    .setMessage(NepBaseResponseCode.UNKNOWN_ERROR.getMessage());
            log.error("NepGroupService muteGroupChat: 全局禁言开启出现异常 - request: {}, response: {}", request, response, exception);
            return response;
        }
    }

    @Override
    @Transactional
    public NepTransferGroupOwnerResponse transferGroupOwner(NepTransferGroupOwnerRequest request) {
        NepTransferGroupOwnerResponse response = new NepTransferGroupOwnerResponse();
        if (!NepCheckGroupParamUtil.checkNepTransferGroupOwnerRequestParam(request)){
            response.setCode(NepBaseResponseCode.CHECK_PARAM_FAIL.getCode())
                    .setMessage(NepBaseResponseCode.CHECK_PARAM_FAIL.getMessage());
            log.error("NepGroupService transferGroupOwner: 参数校验失败 - request: {}, response: {}", request, response);
            return response;
        }
        try {
            int result = groupServiceImpl.doTransferGroupOwner(request);
            if (result <= 0){
                response.setCode(NepGroupResponseCode.TRANSFER_GROUP_FAIL.getCode())
                        .setMessage(NepGroupResponseCode.TRANSFER_GROUP_FAIL.getMessage());
                log.error("NepGroupService transferGroupOwner: 转让群主失败 - request: {}, response: {}", request, response);
                return response;
            }
            response.setCode(NepBaseResponseCode.SUCCESS.getCode())
                    .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
            log.info("NepGroupService transferGroupOwner: 转让群主成功 - request: {}, response: {}", request, response);
            return response;
        }catch (Exception exception){
            response.setCode(NepBaseResponseCode.UNKNOWN_ERROR.getCode())
                    .setMessage(NepBaseResponseCode.UNKNOWN_ERROR.getMessage());
            log.error("NepGroupService transferGroupOwner: 转让群主出现异常 - request: {}, response: {}", request, response, exception);
            return response;
        }
    }

    @Override
    public NepQueryGroupResponse queryGroup(NepQueryGroupRequest request) {
        NepQueryGroupResponse response = new NepQueryGroupResponse();
        if (!NepCheckGroupParamUtil.checkNepQueryGroupRequestParam(request)){
            response.setGroupList(Collections.emptyList())
                    .setCode(NepBaseResponseCode.CHECK_PARAM_FAIL.getCode())
                    .setMessage(NepBaseResponseCode.CHECK_PARAM_FAIL.getMessage());
            log.error("NepGroupService queryGroup: 参数校验失败 - request: {}, response: {}", request, response);
            return response;
        }
        try {
            List<NepGroup> groupList = groupServiceImpl.doQueryGroup(request);
            if (CollectionUtil.isEmpty(groupList)){
                response.setGroupList(Collections.emptyList())
                        .setCode(NepGroupResponseCode.GROUP_NOT_EXIST.getCode())
                        .setMessage(NepGroupResponseCode.GROUP_NOT_EXIST.getMessage());
                log.info("NepGroupService queryGroup: 没有查询到群组 - request: {}, response: {}", request, response);
                return response;
            }
            response.setGroupList(groupList)
                    .setCode(NepBaseResponseCode.SUCCESS.getCode())
                    .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
            log.info("NepGroupService queryGroup: 成功查询到群组 - request: {}, response: {}", request, response);
            return response;
        }catch (Exception exception){
            response.setGroupList(Collections.emptyList())
                    .setCode(NepBaseResponseCode.UNKNOWN_ERROR.getCode())
                    .setMessage(NepBaseResponseCode.UNKNOWN_ERROR.getMessage());
            log.error("NepGroupService queryGroup: 查询群组时出现异常 - request: {}, response: {}", request, response, exception);
            return response;
        }
    }

    @Override
    public NepQueryAllJoinedGroupResponse queryAllJoinedGroup(NepQueryAllJoinedGroupRequest request) {
        NepQueryAllJoinedGroupResponse response = new NepQueryAllJoinedGroupResponse();
        if (!NepCheckGroupParamUtil.checkNepQueryAllJoinedGroupRequestParam(request)){
            response.setCode(NepBaseResponseCode.CHECK_PARAM_FAIL.getCode())
                    .setMessage(NepBaseResponseCode.CHECK_PARAM_FAIL.getMessage());
            log.error("NepGroupService queryAllJoinedGroup: 参数校验失败 - request: {}, response: {}", request, response);
            return response;
        }
        try {
            List<NepGroup> groupList = groupServiceImpl.doQueryAllJoinedGroup(request);
            if (CollectionUtil.isEmpty(groupList)){
                response.setGroupList(Collections.emptyList())
                        .setCode(NepGroupResponseCode.GROUP_NOT_EXIST.getCode())
                        .setMessage(NepGroupResponseCode.GROUP_NOT_EXIST.getMessage());
                log.info("NepGroupService queryAllJoinedGroup: 没有查询到群组 - request: {}, response: {}", request, response);
                return response;
            }
            response.setGroupList(groupList)
                    .setCode(NepBaseResponseCode.SUCCESS.getCode())
                    .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
            log.info("NepGroupService queryAllJoinedGroup: 成功查询到用户加入的所有群组 - request: {}, response: {}", request, response);
            return response;
        }catch (Exception exception){
            response.setGroupList(Collections.emptyList())
                    .setCode(NepBaseResponseCode.UNKNOWN_ERROR.getCode())
                    .setMessage(NepBaseResponseCode.UNKNOWN_ERROR.getMessage());
            log.error("NepGroupService queryAllJoinedGroup: 查询加入群组的过程中出现异常 - request: {}, response: {}", request, response, exception);
            return response;
        }
    }
}
