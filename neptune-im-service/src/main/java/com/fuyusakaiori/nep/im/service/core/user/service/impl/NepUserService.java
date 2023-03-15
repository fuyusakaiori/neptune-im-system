package com.fuyusakaiori.nep.im.service.core.user.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.example.nep.im.common.enums.code.NepBaseResponseCode;
import com.example.nep.im.common.enums.code.NepUserResponseCode;
import com.fuyusakaiori.nep.im.service.core.user.entity.NepUser;
import com.fuyusakaiori.nep.im.service.core.user.entity.request.normal.*;
import com.fuyusakaiori.nep.im.service.core.user.entity.response.normal.*;
import com.fuyusakaiori.nep.im.service.core.user.service.INepUserService;
import com.fuyusakaiori.nep.im.service.util.check.NepCheckUserParamUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
public class NepUserService implements INepUserService {

    @Autowired
    private NepUserServiceImpl userServiceImpl;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public NepRegisterUserResponse registerUser(NepRegisterUserRequest request) {
        // 0. 准备响应体
        NepRegisterUserResponse response = new NepRegisterUserResponse();
        // 1. 参数校验
        if(!NepCheckUserParamUtil.checkNepRegisterUserRequestParam(request)){
            response.setCode(NepBaseResponseCode.CHECK_PARAM_FAIL.getCode())
                    .setMessage(NepBaseResponseCode.CHECK_PARAM_FAIL.getMessage());
            log.error("NeptuneUserService registerUser: 请求头中的参数检查失败 - request: {}, response: {}", request, response);
            return response;
        }
        // 2. 注册用户
        try {
            int result = userServiceImpl.doRegisterUser(request);
            if (result <= 0){
                response.setCode(NepUserResponseCode.REGISTER_USER_FAIL.getCode())
                        .setMessage(NepUserResponseCode.REGISTER_USER_FAIL.getMessage());
                log.error("NeptuneUserService registerUser: 注册用户失败 - request: {}, response: {}", request, response);
                return response;
            }
            response.setCode(NepBaseResponseCode.SUCCESS.getCode())
                    .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
            log.info("NeptuneUserService registerUser: 用户注册成功 - request: {}, response: {}", request, response);
            return response;
        }catch (Exception exception){
            log.error("NeptuneUserService registerUser: 用户注册失败 - request: {}, response: {}", request, response, exception);
            return response.setCode(NepBaseResponseCode.UNKNOWN_ERROR.getCode())
                           .setMessage(NepBaseResponseCode.UNKNOWN_ERROR.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public NepEditUserInfoResponse updateUserInfo(NepEditUserInfoRequest request) {
        // 0. 准备响应结果
        NepEditUserInfoResponse response = new NepEditUserInfoResponse();
        // 1. 参数校验
        if (!NepCheckUserParamUtil.checkNepEditUserRequestParam(request)){
            response.setCode(NepBaseResponseCode.CHECK_PARAM_FAIL.getCode())
                    .setMessage(NepBaseResponseCode.CHECK_PARAM_FAIL.getMessage());
            log.error("NeptuneUserService updateUserInfo: 请求头中的参数检查失败 - request: {}, response: {}", request, response);
            return response;
        }
        // 2. 更新用户资料
        try {
            NepUser user = userServiceImpl.doUpdateUserInfo(request);
            if (Objects.isNull(user)){
                response.setCode(NepUserResponseCode.EDIT_USER_FAIL.getCode())
                        .setMessage(NepUserResponseCode.EDIT_USER_FAIL.getMessage());
                log.error("NeptuneUserService updateUserInfo: 更新用户资料失败 - request: {}, response: {}", request, response);
                return response;
            }
            log.info("NeptuneUserService updateUserInfo: 更新用户资料成功 - request: {}, response: {}", request, response);
            return response.setNewUser(user)
                           .setCode(NepBaseResponseCode.SUCCESS.getCode())
                           .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
        }catch (Exception exception){
            log.error("NeptuneUserService updateUserInfo: 更新用户资料失败 - request: {}, response: {}", request, response, exception);
            return response.setCode(NepBaseResponseCode.UNKNOWN_ERROR.getCode())
                           .setMessage(NepBaseResponseCode.UNKNOWN_ERROR.getMessage());
        }
    }

    @Override
    public NepEditUserAvatarResponse updateUserAvatar(NepEditUserAvatarRequest request) {
        // 0. 准备响应结果
        NepEditUserAvatarResponse response = new NepEditUserAvatarResponse();
        // 1. 执行参数校验
        if (!NepCheckUserParamUtil.checkNepEditUserAvatarRequestParam(request)){
            response.setCode(NepBaseResponseCode.CHECK_PARAM_FAIL.getCode())
                    .setMessage(NepBaseResponseCode.CHECK_PARAM_FAIL.getMessage());
            log.error("NeptuneUserService updateUserAvatar: 请求头中的参数检查失败 - request: {}, response: {}", request, response);
            return response;
        }
        // 2. 更新头像
        try {
            NepUser user = userServiceImpl.doUpdateUserAvatarAddress(request);
            if (Objects.isNull(user)){
                response.setCode(NepUserResponseCode.EDIT_USER_FAIL.getCode())
                        .setMessage(NepUserResponseCode.EDIT_USER_FAIL.getMessage());
                log.error("NeptuneUserService updateUserAvatar: 更新用户头像失败 - request: {}, response: {}", request, response);
                return response;
            }
            response.setNewUser(user)
                    .setCode(NepBaseResponseCode.SUCCESS.getCode())
                    .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
            log.info("NeptuneUserService updateUserAvatar: 更新用户头像成功 - request: {}, response: {}", request, response);
            return response;
        }catch (Exception exception){
            response.setCode(NepBaseResponseCode.UNKNOWN_ERROR.getCode())
                    .setMessage(NepBaseResponseCode.UNKNOWN_ERROR.getMessage());
            log.error("NeptuneUserService updateUserAvatar: 更新用户头像失败 - request: {}, response: {}", request, response, exception);
            return response;
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public NepCancelUserResponse cancelUser(NepCancelUserRequest request) {
        // 0. 准备响应结果
        NepCancelUserResponse response = new NepCancelUserResponse();
        // 1. 参数校验
        if (!NepCheckUserParamUtil.checkNepCancelUserRequestParam(request)){
            response.setCode(NepBaseResponseCode.CHECK_PARAM_FAIL.getCode())
                    .setMessage(NepBaseResponseCode.CHECK_PARAM_FAIL.getMessage());
            log.error("NeptuneUserService cancelUser: 请求头中的参数检查失败 - request: {}, response: {}", request, response);
            return response;
        }
        // 2. 注销用户
        try {
            int result = userServiceImpl.doCancelUser(request);
            if (result <= 0){
                log.error("NeptuneUserService cancelUser: 用户注销失败 - request: {}, response: {}", request, response);
                response.setCode(NepUserResponseCode.CANCEL_USER_FAIL.getCode())
                        .setMessage(NepUserResponseCode.CANCEL_USER_FAIL.getMessage());
                return response;
            }
            response.setCode(NepBaseResponseCode.SUCCESS.getCode())
                    .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
            log.info("NeptuneUserService cancelUser: 注销用户成功 - request: {}, response: {}", request, response);
            return response;
        }catch (Exception exception){
            response.setCode(NepBaseResponseCode.UNKNOWN_ERROR.getCode())
                    .setMessage(NepBaseResponseCode.UNKNOWN_ERROR.getMessage());
            log.error("NeptuneUserService cancelUser: 用户注销失败 - request: {}, response: {}", request, response, exception);
            return response;
        }
    }

    @Override
    public NepLoginUserResponse loginUserInImSystem(NepLoginUserRequest request) {
        // 0. 准备响应结果
        NepLoginUserResponse response = new NepLoginUserResponse();
        // 1. 参数校验
        if (!NepCheckUserParamUtil.checkNepLoginUserInImSystemRequestParam(request)){
            response.setLoginUser(null)
                    .setCode(NepBaseResponseCode.CHECK_PARAM_FAIL.getCode())
                    .setMessage(NepBaseResponseCode.CHECK_PARAM_FAIL.getMessage());
            log.error("NeptuneUserService loginUserInImSystem: 请求头中的参数检查失败 - request: {}, response: {}", request, response);
            return response;

        }
        // 2. 查询用户
        try {
            NepUser user = userServiceImpl.doLoginUserInImSystem(request);
            if (Objects.isNull(user)){
                response.setLoginUser(null)
                        .setCode(NepBaseResponseCode.SUCCESS.getCode())
                        .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
                log.error("NeptuneUserService loginUserInImSystem: 用户登录失败 - request: {}, response: {}", request, response);
                return response;
            }
            response.setLoginUser(user)
                    .setCode(NepBaseResponseCode.SUCCESS.getCode())
                    .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
            log.info("NeptuneUserService loginUserInImSystem: 用户登录成功 - request: {}, response: {}", request, response);
            return response;
        } catch (Exception exception) {
            response.setCode(NepBaseResponseCode.UNKNOWN_ERROR.getCode())
                    .setMessage(NepBaseResponseCode.UNKNOWN_ERROR.getMessage());
            log.error("NeptuneUserService loginUserInImSystem: 根据用户账号查询用户失败 - request: {}, response: {}", request, response, exception);
            return response;
        }
    }

    @Override
    public NepQueryUserResponse queryUserByUserName(NepQueryUserByUserNameRequest request) {
        // 0. 准备响应结果
        NepQueryUserResponse response = new NepQueryUserResponse();
        // 1. 参数校验
        if (!NepCheckUserParamUtil.checkNepQueryUserByAccountRequestParam(request)){
            response.setUserList(Collections.emptyList())
                    .setCode(NepBaseResponseCode.CHECK_PARAM_FAIL.getCode())
                    .setMessage(NepBaseResponseCode.CHECK_PARAM_FAIL.getMessage());
            log.error("NeptuneUserService queryUserByUserName: 请求头中的参数检查失败 - request: {}, response: {}", request, response);
            return response;

        }
        // 2. 查询用户
        try {
            NepUser user = userServiceImpl.doQueryUserByUserName(request);
            if (Objects.isNull(user)){
                response.setUserList(Collections.emptyList())
                        .setCode(NepBaseResponseCode.SUCCESS.getCode())
                        .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
                log.error("NeptuneUserService queryUserByUserName: 没有根据用户账号查询到用户 - request: {}, response: {}", request, response);
                return response;
            }
            response.setUserList(Collections.singletonList(user))
                    .setCode(NepBaseResponseCode.SUCCESS.getCode())
                    .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
            log.info("NeptuneUserService queryUserByUserName: 成功根据用户账号查询到用户 - request: {}, response: {}", request, response);
            return response;
        } catch (Exception exception) {
            response.setCode(NepBaseResponseCode.UNKNOWN_ERROR.getCode())
                    .setMessage(NepBaseResponseCode.UNKNOWN_ERROR.getMessage());
            log.error("NeptuneUserService queryUserByUserName: 根据用户账号查询用户失败 - request: {}, response: {}", request, response, exception);
            return response;
        }
    }

    @Override
    public NepQueryUserResponse queryUserByNickName(NepQueryUserByNickNameRequest request) {
        // 0. 准备响应结果
        NepQueryUserResponse response = new NepQueryUserResponse();
        // 1. 参数校验
        if (!NepCheckUserParamUtil.checkNepQueryUserByNickNameRequestParam(request)){
            response.setUserList(Collections.emptyList())
                    .setCode(NepBaseResponseCode.CHECK_PARAM_FAIL.getCode())
                    .setMessage(NepBaseResponseCode.CHECK_PARAM_FAIL.getMessage());
            log.error("NeptuneUserService queryUserByNickName: 请求头中的参数检查失败 - request: {}, response: {}", request, response);
            return response;
        }
        try {
            List<NepUser> userList = userServiceImpl.doQueryUserByNickName(request);
            if (CollectionUtil.isEmpty(userList)){
                response.setUserList(Collections.emptyList())
                        .setCode(NepUserResponseCode.USER_LIST_EMPTY.getCode())
                        .setMessage(NepUserResponseCode.USER_LIST_EMPTY.getMessage());
                log.info("NeptuneUserService queryUserByNickName: 根据昵称没有查询到任何用户 - request: {}, response: {}", request, response);
                return response;
            }
            response.setUserList(userList)
                    .setCode(NepBaseResponseCode.SUCCESS.getCode())
                    .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
            log.info("NeptuneUserService queryUserByNickName: 根据昵称查询用户成功 - request: {}, response: {}", request, response);
            return response;
        }catch (Exception exception){
            response.setCode(NepBaseResponseCode.UNKNOWN_ERROR.getCode())
                           .setMessage(NepBaseResponseCode.UNKNOWN_ERROR.getMessage());
            log.error("NeptuneUserService queryUserByNickName: 根据用户昵称查询用户失败 - request: {}, response: {}", request, response, exception);
            return response;
        }
    }

}
