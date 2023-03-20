package com.fuyusakaiori.nep.im.service.core.friendship.service.impl;

import com.example.nep.im.common.enums.code.NepBaseResponseCode;
import com.example.nep.im.common.enums.code.NepFriendshipGroupMemberResponseCode;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.group.NepAddFriendshipGroupMemberRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.group.NepDeleteFriendshipGroupMemberRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.group.NepDeleteFriendshipGroupRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.group.NepMoveFriendshipGroupMemberRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.group.NepAddFriendshipGroupMemberResponse;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.group.NepDeleteFriendshipGroupMemberResponse;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.group.NepDeleteFriendshipGroupResponse;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.group.NepMoveFriendshipGroupMemberResponse;
import com.fuyusakaiori.nep.im.service.core.friendship.service.INepFriendshipGroupMemberService;
import com.fuyusakaiori.nep.im.service.util.check.NepCheckFriendGroupMemberParamUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NepFriendshipGroupMemberService implements INepFriendshipGroupMemberService {

    @Autowired
    private NepFriendshipGroupMemberServiceImpl friendshipGroupMemberServiceImpl;

    @Override
    public NepAddFriendshipGroupMemberResponse addFriendshipGroupMember(NepAddFriendshipGroupMemberRequest request) {
        // 0. 准备返回结果
        NepAddFriendshipGroupMemberResponse response = new NepAddFriendshipGroupMemberResponse();
        // 1. 参数校验
        if (!NepCheckFriendGroupMemberParamUtil.checkAddFriendshipGroupMemberRequestParam(request)){
            response.setCode(NepBaseResponseCode.CHECK_PARAM_FAIL.getCode())
                    .setMessage(NepBaseResponseCode.CHECK_PARAM_FAIL.getMessage());
            log.error("NepFriendshipApplicationService addFriendshipGroupMember: 参数校验失败 - request: {}, response: {}", request, response);
            return response;
        }
        // 2. 移动好友所在的好友分组
        try {
            int result = friendshipGroupMemberServiceImpl.doAddFriendshipGroupMember(request);
            if (result <= 0){
                response.setCode(NepFriendshipGroupMemberResponseCode.GROUP_MEMBER_ADD_FAIL.getCode())
                        .setMessage(NepFriendshipGroupMemberResponseCode.GROUP_MEMBER_ADD_FAIL.getMessage());
                log.error("NepFriendshipApplicationService addFriendshipGroupMember: 将好友添加到分组中失败 - request: {}, response: {}", request, response);
                return response;
            }
            response.setCode(NepBaseResponseCode.SUCCESS.getCode())
                    .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
            log.info("NepFriendshipApplicationService addFriendshipGroupMember: 将好友添加到分组中成功 - request: {}, response: {}", request, response);
            return response;
        }catch (Exception exception){
            response.setCode(NepBaseResponseCode.UNKNOWN_ERROR.getCode())
                    .setMessage(NepBaseResponseCode.UNKNOWN_ERROR.getMessage());
            log.error("NepFriendshipApplicationService addFriendshipGroupMember: 将好友添加到分组中出现异常 - request: {}, response: {}", request, response);
            return response;
        }
    }

    @Override
    public NepMoveFriendshipGroupMemberResponse moveFriendshipGroupMember(NepMoveFriendshipGroupMemberRequest request) {
        // 0. 准备返回结果
        NepMoveFriendshipGroupMemberResponse response = new NepMoveFriendshipGroupMemberResponse();
        // 1. 参数校验
        if (!NepCheckFriendGroupMemberParamUtil.checkMoveFriendshipGroupMemberRequestParam(request)){
            response.setCode(NepBaseResponseCode.CHECK_PARAM_FAIL.getCode())
                    .setMessage(NepBaseResponseCode.CHECK_PARAM_FAIL.getMessage());
            log.error("NepFriendshipApplicationService moveFriendshipGroupMember: 参数校验失败 - request: {}, response: {}", request, response);
            return response;
        }
        // 2. 移动好友所在的好友分组
        try {
            int result = friendshipGroupMemberServiceImpl.doMoveFriendshipGroupMember(request);
            if (result <= 0){
                response.setCode(NepFriendshipGroupMemberResponseCode.GROUP_MEMBER_MOVE_FAIL.getCode())
                        .setMessage(NepFriendshipGroupMemberResponseCode.GROUP_MEMBER_MOVE_FAIL.getMessage());
                log.error("NepFriendshipApplicationService moveFriendshipGroupMember: 变更好友所在分组失败 - request: {}, response: {}", request, response);
                return response;
            }
            response.setCode(NepBaseResponseCode.SUCCESS.getCode())
                    .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
            log.info("NepFriendshipApplicationService moveFriendshipGroupMember: 变更好友所在分组成功 - request: {}, response: {}", request, response);
            return response;
        }catch (Exception exception){
            response.setCode(NepBaseResponseCode.UNKNOWN_ERROR.getCode())
                    .setMessage(NepBaseResponseCode.UNKNOWN_ERROR.getMessage());
            log.error("NepFriendshipApplicationService moveFriendshipGroupMember: 变更好友所在分组出现异常 - request: {}, response: {}", request, response);
            return response;
        }
    }

    @Override
    public NepDeleteFriendshipGroupMemberResponse deleteFriendshipGroupMember(NepDeleteFriendshipGroupMemberRequest request) {
        // 0. 准备返回结果
        NepDeleteFriendshipGroupMemberResponse response = new NepDeleteFriendshipGroupMemberResponse();
        // 1. 参数校验
        if (!NepCheckFriendGroupMemberParamUtil.checkDeleteFriendshipGroupMemberRequestParam(request)){
            response.setCode(NepBaseResponseCode.CHECK_PARAM_FAIL.getCode())
                    .setMessage(NepBaseResponseCode.CHECK_PARAM_FAIL.getMessage());
            log.error("NepFriendshipApplicationService deleteFriendshipGroupMember: 参数校验失败 - request: {}, response: {}", request, response);
            return response;
        }
        // 2. 移动好友所在的好友分组
        try {
            int result = friendshipGroupMemberServiceImpl.doDeleteFriendshipGroupMember(request);
            if (result <= 0){
                response.setCode(NepFriendshipGroupMemberResponseCode.GROUP_MEMBER_MOVE_FAIL.getCode())
                        .setMessage(NepFriendshipGroupMemberResponseCode.GROUP_MEMBER_MOVE_FAIL.getMessage());
                log.error("NepFriendshipApplicationService deleteFriendshipGroupMember: 删除好友所在分组失败 - request: {}, response: {}", request, response);
                return response;
            }
            response.setCode(NepBaseResponseCode.SUCCESS.getCode())
                    .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
            log.info("NepFriendshipApplicationService deleteFriendshipGroupMember: 删除好友所在分组成功 - request: {}, response: {}", request, response);
            return response;
        }catch (Exception exception){
            response.setCode(NepBaseResponseCode.UNKNOWN_ERROR.getCode())
                    .setMessage(NepBaseResponseCode.UNKNOWN_ERROR.getMessage());
            log.error("NepFriendshipApplicationService deleteFriendshipGroupMember: 删除好友所在分组出现异常 - request: {}, response: {}", request, response);
            return response;
        }
    }
}
