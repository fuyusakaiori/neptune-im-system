package com.fuyusakaiori.nep.im.service.core.user.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.example.nep.im.common.entity.request.NepRequestHeader;
import com.example.nep.im.common.enums.code.NepBaseResponseCode;
import com.example.nep.im.common.enums.code.NepUserResponseCode;
import com.fuyusakaiori.nep.im.service.core.user.entity.NepUser;
import com.fuyusakaiori.nep.im.service.core.user.entity.dto.NepEditUser;
import com.fuyusakaiori.nep.im.service.core.user.entity.dto.NepRegisterUser;
import com.fuyusakaiori.nep.im.service.core.user.entity.request.normal.*;
import com.fuyusakaiori.nep.im.service.core.user.entity.response.normal.NepQueryUserResponse;
import com.fuyusakaiori.nep.im.service.core.user.entity.response.normal.NepModifyUserResponse;
import com.fuyusakaiori.nep.im.service.core.user.mapper.INepUserMapper;
import com.fuyusakaiori.nep.im.service.core.user.service.INepUserService;
import com.fuyusakaiori.nep.im.service.util.NepCheckUserParamUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
public class NepUserService implements INepUserService {

    @Autowired
    INepUserMapper userMapper;

    /**
     * <h3>单次插入用户资料的上限</h3>
     */
    private static final int SINGLE_INSERT_USER_COUNT_LIMIT = 1000;

    private static final int GROUP_COUNT = 100;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public NepModifyUserResponse registerUser(NepRegisterUserRequest request) {
        // 0. 准备响应体
        NepModifyUserResponse response = new NepModifyUserResponse();
        // 1. 获取请求中的变量
        NepRequestHeader header = request.getRequestHeader();
        NepRegisterUser body = request.getRequestBody();
        // 2. 请求头参数校验
        if(!NepCheckUserParamUtil.checkNepRegisterUserRequestParam(request)){
            log.error("NeptuneUserService registerUser: 请求头中的参数检查失败 - request: {}", request);
            return response.setCode(NepBaseResponseCode.CHECK_PARAM_FAILURE.getCode())
                           .setMessage(NepBaseResponseCode.CHECK_PARAM_FAILURE.getMessage());
        }
        // 3. 注册用户
        int result = userMapper.registerUser(header.getAppId(), body, System.currentTimeMillis(), System.currentTimeMillis());
        if (result <= 0){
            log.error("NeptuneUserService registerUser: 插入用户数据失败 - user: {}", body);
            return response.setCode(NepUserResponseCode.REGISTER_USER_FAIL.getCode())
                           .setMessage(NepUserResponseCode.REGISTER_USER_FAIL.getMessage());
        }
        // 4. 填充响应信息
        return response.setCode(NepBaseResponseCode.SUCCESS.getCode())
                       .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public NepModifyUserResponse updateUser(NepEditUserRequest request) {
        // 0. 准备响应结果
        NepModifyUserResponse response = new NepModifyUserResponse();
        // 1. 参数校验
        if (!NepCheckUserParamUtil.checkNepEditUserRequestParam(request)){
            log.error("NeptuneUserService updateUser: 请求头中的参数检查失败 - request: {}", request);
            return response.setCode(NepBaseResponseCode.CHECK_PARAM_FAILURE.getCode())
                           .setMessage(NepBaseResponseCode.CHECK_PARAM_FAILURE.getMessage());
        }
        // 2. 获取变量
        NepRequestHeader header = request.getRequestHeader();
        NepEditUser body = request.getRequestBody();
        // 3. 查询用户是否存在
        // 3.1 准备查询条件
        Integer userId = body.getUserId();
        // 3.2 查询用户
        NepUser user = userMapper.querySimpleUserById(header.getAppId(), userId);
        // 3.3 判断用户是否存在
        if (Objects.isNull(user) || user.getIsDelete()){
            log.error("NeptuneUserService updateUser: 需要更新的用户不存在或者用户已经注销 - user: {}, request: {}", user, request);
            return response.setCode(NepUserResponseCode.EDIT_USER_NOT_EXIST.getCode())
                    .setMessage(NepUserResponseCode.EDIT_USER_NOT_EXIST.getMessage());
        }
        // 4. 更新用户资料
        int result = userMapper.editUser(header.getAppId(), body, System.currentTimeMillis());
        if (result <= 0){
            log.error("NeptuneUserService updateUser: 更新用户数据失败 - user: {}", request);
            return response.setCode(NepUserResponseCode.EDIT_USER_FAIL.getCode())
                    .setMessage(NepUserResponseCode.EDIT_USER_FAIL.getMessage());
        }
        // 4. 设置返回信息
        return response.setCode(NepBaseResponseCode.SUCCESS.getCode())
                .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public NepModifyUserResponse cancelUser(NepCancelUserRequest request) {
        // 0. 准备响应结果
        NepModifyUserResponse response = new NepModifyUserResponse();
        // 1. 参数校验
        if (!NepCheckUserParamUtil.checkNepCancelUserRequestParam(request)){
            log.error("NeptuneUserService cancelUser: 请求头中的参数检查失败 - request: {}", request);
            return response.setCode(NepBaseResponseCode.CHECK_PARAM_FAILURE.getCode())
                           .setMessage(NepBaseResponseCode.CHECK_PARAM_FAILURE.getMessage());
        }
        // 2. 获取变量
        NepRequestHeader header = request.getRequestHeader();
        Integer userId = request.getUserId();
        // 3. 查询用户是否存在
        NepUser user = userMapper.querySimpleUserById(header.getAppId(), userId);
        if (Objects.isNull(user) || user.getIsDelete()){
            log.info("NeptuneUserService cancelUser: 需要删除的用户不存在 - user: {}, request: {}", user, request);
            return response.setCode(NepUserResponseCode.CANCEL_USER_NOT_EXIST.getCode())
                           .setMessage(NepUserResponseCode.CANCEL_USER_NOT_EXIST.getMessage());
        }
        // 4. 删除用户
        int result = userMapper.cancelUser(header.getAppId(), userId, System.currentTimeMillis());
        if (result <= 0){
            log.error("NeptuneUserService cancelUser: 删除用户数据失败 - userId: {}", userId);
            return response.setCode(NepUserResponseCode.CANCEL_USER_FAIL.getCode())
                           .setMessage(NepUserResponseCode.CANCEL_USER_FAIL.getMessage());
        }
        // 5. 填充响应信息
        return response.setCode(NepBaseResponseCode.SUCCESS.getCode())
                       .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
    }

    @Override
    public NepQueryUserResponse queryUserByAccount(NepQueryUserByAccountRequest request) {
        // 0. 准备响应结果
        NepQueryUserResponse response = new NepQueryUserResponse();
        // 1. 参数校验
        if (!NepCheckUserParamUtil.checkNepQueryUserByAccountRequestParam(request)){
            log.error("NeptuneUserService queryUserByAccount: 请求头中的参数检查失败 - request: {}", request);
            return response.setUserList(Collections.emptyList())
                           .setCode(NepBaseResponseCode.CHECK_PARAM_FAILURE.getCode())
                           .setMessage(NepBaseResponseCode.CHECK_PARAM_FAILURE.getMessage());
        }
        // 2. 获取变量
        NepRequestHeader header = request.getRequestHeader();
        String userAccount = request.getUserAccount();
        // 3. 查询结果
        NepUser user = userMapper.queryUserByAccount(header.getAppId(), userAccount);
        // 4. 检查结果
        if (Objects.isNull(user) || user.getIsDelete()){
            log.info("NeptuneUserService queryUserByAccount: 用户不存在或者已经注销 - request: {}", request);
            return response.setUserList(Collections.emptyList())
                           .setCode(NepUserResponseCode.USER_NOT_EXIST.getCode())
                           .setMessage(NepUserResponseCode.USER_NOT_EXIST.getMessage());
        }
        // 5. 填充响应结果
        return response.setUserList(Collections.singletonList(user))
                       .setCode(NepBaseResponseCode.SUCCESS.getCode())
                       .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
    }

    @Override
    public NepQueryUserResponse queryUserByNickName(NepQueryUserByNickNameRequest request) {
        // 0. 准备响应结果
        NepQueryUserResponse response = new NepQueryUserResponse();
        // 1. 参数校验
        if (!NepCheckUserParamUtil.checkNepQueryUserByNickNameRequestParam(request)){
            log.error("NeptuneUserService queryUserByNickName: 请求头中的参数检查失败 - request: {}", request);
            return response.setUserList(Collections.emptyList())
                           .setCode(NepBaseResponseCode.CHECK_PARAM_FAILURE.getCode())
                           .setMessage(NepBaseResponseCode.CHECK_PARAM_FAILURE.getMessage());
        }
        // 2. 获取变量
        NepRequestHeader header = request.getRequestHeader();
        String userNickName = request.getUserNickName();
        // 3. 查询结果
        List<NepUser> userList = userMapper.queryUserByNickName(header.getAppId(), userNickName);
        // 4. 检查结果
        if (CollectionUtil.isEmpty(userList)){
            log.info("NeptuneUserService queryUserByNickName: 用户不存在或者已经注销 - request: {}", request);
            return response.setUserList(Collections.emptyList())
                           .setCode(NepUserResponseCode.USER_NOT_EXIST.getCode())
                           .setMessage(NepUserResponseCode.USER_NOT_EXIST.getMessage());
        }
        // 5. 填充响应结果
        return response.setUserList(userList)
                       .setCode(NepBaseResponseCode.SUCCESS.getCode())
                       .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
    }

    //============================== 管理后台可能会使用的方法 ==============================
    @Override
    public NepModifyUserResponse batchRegisterUser(NepImportUserRequest request) {
        // 0. 准备响应体
        NepModifyUserResponse response = new NepModifyUserResponse();
        // 1. 获取请求中的变量
        NepRequestHeader header = request.getRequestHeader();
        List<NepRegisterUser> body = request.getRequestBody();
        // 2. 请求头参数校验
        if(!NepCheckUserParamUtil.checkNepImportUserRequestParam(request)){
            log.error("NeptuneUserService registerUser: 请求头中的参数检查失败 - request: {}", request);
            response.setCode(NepBaseResponseCode.CHECK_PARAM_FAILURE.getCode())
                    .setMessage(NepBaseResponseCode.CHECK_PARAM_FAILURE.getMessage());
            return response;
        }
        // 3 批量插入用户
        // 3.1如果集合数量没有超过限制, 那么直接插入
        if (body.size() <= SINGLE_INSERT_USER_COUNT_LIMIT){
            int result = userMapper.batchRegisterUser(header.getAppId(), body, System.currentTimeMillis(), System.currentTimeMillis());
            if (result < body.size()){
                log.error("NeptuneUserService registerUser: 成功插入数据 = {}, 失败插入数据 = {}", result, body.size() - result);
                response.setCode(NepUserResponseCode.REGISTER_USER_FAIL.getCode())
                        .setMessage(NepUserResponseCode.REGISTER_USER_FAIL.getMessage());
                return response;
            }
        }else{
            // 3.2 如果插入用户的数量大于限制, 那么选择分批插入数据库中
            int count = (int) Math.ceil(body.size() / (double)GROUP_COUNT);
            for (int index = 0; index <= count; index++) {
                // 3.2.1 计算每个批次的起始和结束位置
                int start = count * GROUP_COUNT, end = (count + 1) * GROUP_COUNT;
                // 3.2.2 截取集合的子集然后插入数据
                int result = userMapper.batchRegisterUser(header.getAppId(), body.subList(start, end), System.currentTimeMillis(), System.currentTimeMillis());
                // 3.2.3 判断是否所有数据都已经插入成功
                if (result <= 0){
                    log.error("NeptuneUserService registerUser: 批次 = {} ~ {}, 成功插入数据 = {}, 失败插入数据 = {}",
                            start, end, result, GROUP_COUNT - result);
                    response.setCode(NepUserResponseCode.REGISTER_USER_FAIL.getCode())
                            .setMessage(NepUserResponseCode.REGISTER_USER_FAIL.getMessage());
                    return response;
                }
            }
        }
        response.setCode(NepBaseResponseCode.SUCCESS.getCode())
                .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
        return response;
    }

}
