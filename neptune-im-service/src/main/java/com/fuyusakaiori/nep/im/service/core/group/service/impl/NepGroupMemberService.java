package com.fuyusakaiori.nep.im.service.core.group.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.example.nep.im.common.enums.code.NepBaseResponseCode;
import com.example.nep.im.common.enums.code.NepGroupMemberResponseCode;
import com.fuyusakaiori.nep.im.service.core.group.entity.dto.NepGroupMemberUser;
import com.fuyusakaiori.nep.im.service.core.group.entity.request.*;
import com.fuyusakaiori.nep.im.service.core.group.entity.response.*;
import com.fuyusakaiori.nep.im.service.core.group.service.INepGroupMemberService;
import com.fuyusakaiori.nep.im.service.util.check.NepCheckGroupMemberParamUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class NepGroupMemberService implements INepGroupMemberService {


    @Autowired
    private NepGroupMemberServiceImpl groupMemberServiceImpl;


    @Override
    @Transactional
    public NepAddGroupMemberResponse addGroupMember(NepAddGroupMemberRequest request) {
        NepAddGroupMemberResponse response = new NepAddGroupMemberResponse();
        if (!NepCheckGroupMemberParamUtil.checkNepAddGroupMemberRequestParam(request)){
            response.setCode(NepBaseResponseCode.CHECK_PARAM_FAIL.getCode())
                    .setMessage(NepBaseResponseCode.CHECK_PARAM_FAIL.getMessage());
            log.error("NepGroupMemberService addGroupMember: 参数校验失败 - request: {}, response: {}", request, response);
            return response;
        }
        try {
            int result = groupMemberServiceImpl.doAddGroupMember(request);
            if (result <= 0){
                response.setCode(NepGroupMemberResponseCode.ADD_GROUP_MEMBER_FAIL.getCode())
                        .setMessage(NepGroupMemberResponseCode.ADD_GROUP_MEMBER_FAIL.getMessage());
                log.error("NepGroupMemberService addGroupMember: 用户加入群组失败 - request: {}, response: {}", request, response);
                return response;
            }
            response.setCode(NepBaseResponseCode.SUCCESS.getCode())
                    .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
            log.info("NepGroupMemberService addGroupMember: 用户成功加入群组 - request: {}, response: {}", request, response);
            return response;
        }catch (Exception exception){
            response.setCode(NepBaseResponseCode.UNKNOWN_ERROR.getCode())
                    .setMessage(NepBaseResponseCode.UNKNOWN_ERROR.getMessage());
            log.error("NepGroupMemberService addGroupMember: 用户加入群组出现异常 - request: {}, response: {}", request, response, exception);
            return response;
        }
    }

    @Override
    @Transactional
    public NepUpdateGroupMemberResponse updateGroupMember(NepUpdateGroupMemberRequest request) {
        NepUpdateGroupMemberResponse response = new NepUpdateGroupMemberResponse();
        if (!NepCheckGroupMemberParamUtil.checkNepUpdateGroupMemberRequestParam(request)){
            response.setCode(NepBaseResponseCode.CHECK_PARAM_FAIL.getCode())
                    .setMessage(NepBaseResponseCode.CHECK_PARAM_FAIL.getMessage());
            log.error("NepGroupMemberService updateGroupMember: 参数校验失败 - request: {}, response: {}", request, response);
            return response;
        }
        try {
            int result = groupMemberServiceImpl.doUpdateGroupMember(request);
            if (result <= 0){
                response.setCode(NepGroupMemberResponseCode.UPDATE_GROUP_MEMBER_FAIL.getCode())
                        .setMessage(NepGroupMemberResponseCode.UPDATE_GROUP_MEMBER_FAIL.getMessage());
                log.error("NepGroupMemberService updateGroupMember: 更新群组成员资料失败 - request: {}, response: {}", request, response);
                return response;
            }
            response.setCode(NepBaseResponseCode.SUCCESS.getCode())
                    .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
            log.info("NepGroupMemberService updateGroupMember: 更新群组成员资料成功 - request: {}, response: {}", request, response);
            return response;
        }catch (Exception exception){
            response.setCode(NepBaseResponseCode.UNKNOWN_ERROR.getCode())
                    .setMessage(NepBaseResponseCode.UNKNOWN_ERROR.getMessage());
            log.error("NepGroupMemberService updateGroupMember: 更新群组成员资料出现异常 - request: {}, response: {}", request, response, exception);
            return response;
        }
    }

    @Override
    @Transactional
    public NepChangeGroupMemberTypeResponse changeGroupMemberType(NepChangeGroupMemberTypeRequest request) {
        NepChangeGroupMemberTypeResponse response = new NepChangeGroupMemberTypeResponse();
        if (!NepCheckGroupMemberParamUtil.checkNepChangeGroupMemberTypeRequestParam(request)){
            response.setCode(NepBaseResponseCode.CHECK_PARAM_FAIL.getCode())
                    .setMessage(NepBaseResponseCode.CHECK_PARAM_FAIL.getMessage());
            log.error("NepGroupMemberService changeGroupMemberType: 参数校验失败 - request: {}, response: {}", request, response);
            return response;
        }
        try {
            int result = groupMemberServiceImpl.doChangeGroupMemberType(request);
            if (result <= 0){
                response.setCode(NepGroupMemberResponseCode.CHANGE_GROUP_MEMBER_TYPE_FAIL.getCode())
                        .setMessage(NepGroupMemberResponseCode.CHANGE_GROUP_MEMBER_TYPE_FAIL.getMessage());
                log.error("NepGroupMemberService changeGroupMemberType: 改变群组成员类型失败 - request: {}, response: {}", request, response);
                return response;
            }
            response.setCode(NepBaseResponseCode.SUCCESS.getCode())
                    .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
            log.info("NepGroupMemberService changeGroupMemberType: 成功改变群组成员类型 - request: {}, response: {}", request, response);
            return response;
        }catch (Exception exception){
            response.setCode(NepBaseResponseCode.UNKNOWN_ERROR.getCode())
                    .setMessage(NepBaseResponseCode.UNKNOWN_ERROR.getMessage());
            log.error("NepGroupMemberService changeGroupMemberType: 改变成员类型出现异常 - request: {}, response: {}", request, response, exception);
            return response;
        }
    }

    @Override
    @Transactional
    public NepMuteGroupMemberResponse muteGroupMemberChat(NepMuteGroupMemberRequest request) {
        NepMuteGroupMemberResponse response = new NepMuteGroupMemberResponse();
        if (!NepCheckGroupMemberParamUtil.checkNepMuteGroupMemberRequestParam(request)){
            response.setCode(NepBaseResponseCode.CHECK_PARAM_FAIL.getCode())
                    .setMessage(NepBaseResponseCode.CHECK_PARAM_FAIL.getMessage());
            log.error("NepGroupMemberService muteGroupMemberChat: 参数校验失败 - request: {}, response: {}", request, response);
            return response;
        }
        try {
            int result = groupMemberServiceImpl.doMuteGroupMemberChat(request);
            if (result <= 0){
                response.setCode(NepGroupMemberResponseCode.MUTE_GROUP_MEMBER_FAIL.getCode())
                        .setMessage(NepGroupMemberResponseCode.MUTE_GROUP_MEMBER_FAIL.getMessage());
                log.error("NepGroupMemberService muteGroupMemberChat: 禁言群成员失败 - request: {}, response: {}", request, response);
                return response;
            }
            response.setCode(NepBaseResponseCode.SUCCESS.getCode())
                    .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
            log.info("NepGroupMemberService muteGroupMemberChat: 成功禁言群成员 - request: {}, response: {}", request, response);
            return response;
        }catch (Exception exception){
            response.setCode(NepBaseResponseCode.UNKNOWN_ERROR.getCode())
                    .setMessage(NepBaseResponseCode.UNKNOWN_ERROR.getMessage());
            log.error("NepGroupMemberService muteGroupMemberChat: 禁言群成员出现异常 - request: {}, response: {}", request, response, exception);
            return response;
        }
    }

    @Override
    @Transactional
    public NepRevokeGroupMemberResponse revokeGroupMemberChat(NepRevokeGroupMemberRequest request) {
        NepRevokeGroupMemberResponse response = new NepRevokeGroupMemberResponse();
        if (!NepCheckGroupMemberParamUtil.checkNepRevokeGroupMemberRequestParam(request)){
            response.setCode(NepBaseResponseCode.CHECK_PARAM_FAIL.getCode())
                    .setMessage(NepBaseResponseCode.CHECK_PARAM_FAIL.getMessage());
            log.error("NepGroupMemberService revokeGroupMemberChat: 参数校验失败 - request: {}, response: {}", request, response);
            return response;
        }
        try {
            int result = groupMemberServiceImpl.doRevokeGroupMemberChat(request);
            if (result <= 0){
                response.setCode(NepGroupMemberResponseCode.REVOKE_GROUP_MEMBER_MUTE_FAIL.getCode())
                        .setMessage(NepGroupMemberResponseCode.REVOKE_GROUP_MEMBER_MUTE_FAIL.getMessage());
                log.error("NepGroupMemberService revokeGroupMemberChat: 撤销用户禁言失败 - request: {}, response: {}", request, response);
                return response;
            }
            response.setCode(NepBaseResponseCode.SUCCESS.getCode())
                    .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
            log.info("NepGroupMemberService revokeGroupMemberChat: 成功撤销用户禁言 - request: {}, response: {}", request, response);
            return response;
        }catch (Exception exception){
            response.setCode(NepBaseResponseCode.UNKNOWN_ERROR.getCode())
                    .setMessage(NepBaseResponseCode.UNKNOWN_ERROR.getMessage());
            log.error("NepGroupMemberService revokeGroupMemberChat: 撤销用户禁言出现异常 - request: {}, response: {}", request, response, exception);
            return response;
        }
    }

    @Override
    @Transactional
    public NepExitGroupMemberResponse exitGroupMember(NepExitGroupMemberRequest request) {
        NepExitGroupMemberResponse response = new NepExitGroupMemberResponse();
        if (!NepCheckGroupMemberParamUtil.checkNepExitGroupMemberRequestParam(request)){
            response.setCode(NepBaseResponseCode.CHECK_PARAM_FAIL.getCode())
                    .setMessage(NepBaseResponseCode.CHECK_PARAM_FAIL.getMessage());
            log.error("NepGroupMemberService exitGroupMember: 参数校验失败 - request: {}, response: {}", request, response);
            return response;
        }
        try {
            int result = groupMemberServiceImpl.doExitGroupMember(request);
            if (result <= 0){
                response.setCode(NepGroupMemberResponseCode.EXIT_GROUP_MEMBER_FAIL.getCode())
                        .setMessage(NepGroupMemberResponseCode.EXIT_GROUP_MEMBER_FAIL.getMessage());
                log.error("NepGroupMemberService exitGroupMember: 用户退出群组失败 - request: {}, response: {}", request, response);
                return response;
            }
            response.setCode(NepBaseResponseCode.SUCCESS.getCode())
                    .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
            log.info("NepGroupMemberService exitGroupMember: 用户成功退出群组 - request: {}, response: {}", request, response);
            return response;
        }catch (Exception exception){
            response.setCode(NepBaseResponseCode.UNKNOWN_ERROR.getCode())
                    .setMessage(NepBaseResponseCode.UNKNOWN_ERROR.getMessage());
            log.error("NepGroupMemberService exitGroupMember: 用户退出群组出现异常 - request: {}, response: {}", request, response, exception);
            return response;
        }
    }

    @Override
    public NepQueryAllGroupMemberResponse queryAllGroupMember(NepQueryAllGroupMemberRequest request) {
        NepQueryAllGroupMemberResponse response = new NepQueryAllGroupMemberResponse();
        if (!NepCheckGroupMemberParamUtil.checkNepQueryAllGroupMemberRequestParam(request)){
            response.setUserList(Collections.emptyList())
                    .setCode(NepBaseResponseCode.CHECK_PARAM_FAIL.getCode())
                    .setMessage(NepBaseResponseCode.CHECK_PARAM_FAIL.getMessage());
            log.error("NepGroupMemberService queryAllGroupMember: 参数校验失败 - request: {}, response: {}", request, response);
            return response;
        }
        try {
            List<NepGroupMemberUser> userList = groupMemberServiceImpl.doQueryAllGroupMember(request);
            if (CollectionUtil.isEmpty(userList)){
                response.setUserList(Collections.emptyList())
                        .setCode(NepGroupMemberResponseCode.GROUP_MEMBER_NOT_EXIST.getCode())
                        .setMessage(NepGroupMemberResponseCode.GROUP_MEMBER_NOT_EXIST.getMessage());
                log.info("NepGroupMemberService queryAllGroupMember: 没有查询到用户群成员 - request: {}, response: {}", request, response);
                return response;
            }
            response.setUserList(userList)
                    .setCode(NepBaseResponseCode.SUCCESS.getCode())
                    .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
            log.info("NepGroupMemberService queryAllGroupMember: 用户成功退出群组 - request: {}, response: {}", request, response);
            return response;
        }catch (Exception exception){
            response.setCode(NepBaseResponseCode.UNKNOWN_ERROR.getCode())
                    .setMessage(NepBaseResponseCode.UNKNOWN_ERROR.getMessage());
            log.error("NepGroupMemberService queryAllGroupMember: 用户退出群组出现异常 - request: {}, response: {}", request, response, exception);
            return response;
        }
    }

}
