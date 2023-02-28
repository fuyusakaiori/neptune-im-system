package com.fuyusakaiori.neptune.im.service.core.user.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.example.neptune.im.common.entity.request.NeptuneRequestHeader;
import com.example.neptune.im.common.enums.reponse.NeptuneBaseResponseCode;
import com.example.neptune.im.common.enums.reponse.NeptuneUserResponseCode;
import com.fuyusakaiori.neptune.im.service.core.user.entity.NeptuneUser;
import com.fuyusakaiori.neptune.im.service.core.user.entity.dto.NeptuneCancelUser;
import com.fuyusakaiori.neptune.im.service.core.user.entity.dto.NeptuneRegisterUser;
import com.fuyusakaiori.neptune.im.service.core.user.entity.dto.NeptuneQueryUser;
import com.fuyusakaiori.neptune.im.service.core.user.entity.request.NeptuneCancelUserRequest;
import com.fuyusakaiori.neptune.im.service.core.user.entity.request.NeptuneRegisterUserRequest;
import com.fuyusakaiori.neptune.im.service.core.user.entity.request.NeptuneQueryUserRequest;
import com.fuyusakaiori.neptune.im.service.core.user.entity.request.NeptuneEditUserRequest;
import com.fuyusakaiori.neptune.im.service.core.user.entity.response.NeptuneQueryUserResponse;
import com.fuyusakaiori.neptune.im.service.core.user.entity.response.NeptuneModifyUserResponse;
import com.fuyusakaiori.neptune.im.service.core.user.mapper.INeptuneUserMapper;
import com.fuyusakaiori.neptune.im.service.core.user.service.INeptuneUserService;
import com.fuyusakaiori.neptune.im.service.util.NeptuneCheckUserParamUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class NeptuneUserService implements INeptuneUserService {

    @Autowired
    INeptuneUserMapper userMapper;

    /**
     * <h3>单次插入用户资料的上限</h3>
     */
    private static final int SINGLE_INSERT_USER_COUNT_LIMIT = 1000;

    private static final int GROUP_COUNT = 100;

    @Override
    public NeptuneQueryUserResponse queryUser(NeptuneQueryUserRequest request) {
        // 0. 准备响应结果
        NeptuneQueryUserResponse response = new NeptuneQueryUserResponse();
        // 1. 参数校验
        if (!NeptuneCheckUserParamUtil.checkNeptuneQueryUserRequestParam(request)){
            log.error("NeptuneUserService queryUser: 请求头中的参数检查失败 - request: {}", request);
            response.setCode(NeptuneBaseResponseCode.CHECK_PARAM_FAILURE.getCode())
                    .setMessage(NeptuneBaseResponseCode.CHECK_PARAM_FAILURE.getMessage());
            return response;
        }
        // 2. 获取变量
        NeptuneRequestHeader requestHeader = request.getRequestHeader();
        NeptuneQueryUser requestBody = request.getRequestBody();
        // 3. 查询结果
        List<NeptuneUser> userList = userMapper.queryUser(requestHeader.getAppId(), requestBody);
        // 4. 检查结果
        if (CollectionUtil.isEmpty(userList)){
            log.info("NeptuneUserService queryUser: 没有查询到任何结果");
            response.setUserList(Collections.emptyList())
                    .setCode(NeptuneUserResponseCode.QUERY_USER_RESULT_EMPTY.getCode())
                    .setMessage(NeptuneUserResponseCode.QUERY_USER_RESULT_EMPTY.getMessage());
            return response;
        }
        response.setUserList(userList)
                .setCode(NeptuneBaseResponseCode.SUCCESS.getCode())
                .setMessage(NeptuneBaseResponseCode.SUCCESS.getMessage());
        return response;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public NeptuneModifyUserResponse updateUser(NeptuneEditUserRequest request) {
        // 0. 准备响应结果
        NeptuneModifyUserResponse response = new NeptuneModifyUserResponse();
        // 1. 参数校验
        if (!NeptuneCheckUserParamUtil.checkNeptuneEditUserRequestParam(request)){
            log.error("NeptuneUserService insertUser: 请求头中的参数检查失败 - request: {}", request);
            response.setCode(NeptuneBaseResponseCode.CHECK_PARAM_FAILURE.getCode())
                    .setMessage(NeptuneBaseResponseCode.CHECK_PARAM_FAILURE.getMessage());
            return response;
        }
        // 2. 查询用户是否存在
        List<NeptuneUser> users = userMapper.queryUser(request.getRequestHeader().getAppId(),
                new NeptuneQueryUser().setUserId(request.getRequestBody().getUserId()));
        if (CollectionUtil.isEmpty(users)){
            log.error("NeptuneUserService updateUser: 需要更新的用户不存在 - user: {}", request.getRequestBody());
            response.setCode(NeptuneUserResponseCode.EDIT_USER_FAIL.getCode())
                    .setMessage(NeptuneUserResponseCode.EDIT_USER_FAIL.getMessage());
            return response;
        }
        // 3. 更新用户资料
        int result = userMapper.editUser(request.getRequestHeader().getAppId(), request.getRequestBody());
        if (result <= 0){
            log.error("NeptuneUserService updateUser: 更新用户数据失败 - user: {}", request);
            response.setCode(NeptuneUserResponseCode.EDIT_USER_FAIL.getCode())
                    .setMessage(NeptuneUserResponseCode.EDIT_USER_FAIL.getMessage());
            return response;
        }
        // 4. 设置返回信息
        response.setCode(NeptuneBaseResponseCode.SUCCESS.getCode())
                .setMessage(NeptuneBaseResponseCode.SUCCESS.getMessage());
        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public NeptuneModifyUserResponse registerUser(NeptuneRegisterUserRequest request) {
        // 0. 准备响应体
        NeptuneModifyUserResponse response = new NeptuneModifyUserResponse();
        // 1. 获取请求中的变量
        NeptuneRequestHeader requestBase = request.getRequestHeader();
        List<NeptuneRegisterUser> userList = request.getRequestBody();
        // 2. 请求头参数校验
        if(!NeptuneCheckUserParamUtil.checkNeptuneRegisterUserRequestParam(request)){
            log.error("NeptuneUserService registerUser: 请求头中的参数检查失败 - request: {}", request);
            response.setCode(NeptuneBaseResponseCode.CHECK_PARAM_FAILURE.getCode())
                    .setMessage(NeptuneBaseResponseCode.CHECK_PARAM_FAILURE.getMessage());
            return response;
        }
        // 3. 赋给对象创建和修改时间
        userList.forEach(user ->
            user.setCreateTime(System.currentTimeMillis())
                    .setUpdateTime(System.currentTimeMillis()));
        // 4. 根据单个用户插入和批量用户插入选择不同的方法 (实际效果没有任何区别)
        if (userList.size() == 1){
            // 4.1 单次插入用户
            int result = userMapper.registerUser(requestBase.getAppId(), userList.get(0));
            if (result <= 0){
                log.error("NeptuneUserService singleInsertUser: 插入用户数据失败 - user: {}", userList.get(0));
                response.setCode(NeptuneUserResponseCode.REGISTER_USER_FAIL.getCode())
                        .setMessage(NeptuneUserResponseCode.REGISTER_USER_FAIL.getMessage());
                return response;
            }
        }else{
            // 4.2 批量插入用户
            // 4.2.1 如果集合数量没有超过限制, 那么直接插入
            if (userList.size() <= SINGLE_INSERT_USER_COUNT_LIMIT){
                int result = userMapper.batchRegisterUser(requestBase.getAppId(), userList);
                if (result < userList.size()){
                    log.error("NeptuneUserService registerUser: 成功插入数据 = {}, 失败插入数据 = {}", result, userList.size() - result);
                    response.setCode(NeptuneUserResponseCode.REGISTER_USER_FAIL.getCode())
                            .setMessage(NeptuneUserResponseCode.REGISTER_USER_FAIL.getMessage());
                    return response;
                }
            }else{
                // 3.2.2 如果插入用户的数量大于限制, 那么选择分批插入数据库中
                int count = (int) Math.ceil(userList.size() / (double)GROUP_COUNT);
                for (int index = 0; index <= count; index++) {
                    // 3.2.2.1 计算每个批次的起始和结束位置
                    int start = count * GROUP_COUNT, end = (count + 1) * GROUP_COUNT;
                    // 3.2.2.2 截取集合的子集然后插入数据
                    int result = userMapper.batchRegisterUser(requestBase.getAppId(), userList.subList(start, end));
                    // 3.2.2.3 判断是否所有数据都已经插入成功
                    if (result <= 0){
                        log.error("NeptuneUserService registerUser: 批次 = {} ~ {}, 成功插入数据 = {}, 失败插入数据 = {}",
                                start, end, result, GROUP_COUNT - result);
                        response.setCode(NeptuneUserResponseCode.REGISTER_USER_FAIL.getCode())
                                .setMessage(NeptuneUserResponseCode.REGISTER_USER_FAIL.getMessage());
                        return response;
                    }
                }
            }
        }
        // 5. 填充响应信息
        response.setCode(NeptuneBaseResponseCode.SUCCESS.getCode())
                .setMessage(NeptuneBaseResponseCode.SUCCESS.getMessage());
        return response;
    }

    /**
     * <h3>删除用户资料</h3>
     * <h4>注: 批量删除没有使用分组删除的方式: </h4>
     * <h4>1. 大批量删除用户的场景较少</h4>
     * <h4>2. 批量删除本质只是修改单个字段, 所以数据量大的情况应该也是可以抗住的</h4>
     * <h4>3. 我懒得写了</h4>
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public NeptuneModifyUserResponse cancelUser(NeptuneCancelUserRequest request) {
        // 0. 准备响应结果
        NeptuneModifyUserResponse response = new NeptuneModifyUserResponse();
        // 1. 参数校验
        if (!NeptuneCheckUserParamUtil.checkNeptuneCancelUserRequestParam(request)){
            log.error("NeptuneUserService cancelUser: 请求头中的参数检查失败 - request: {}", request);
            response.setCode(NeptuneBaseResponseCode.CHECK_PARAM_FAILURE.getCode())
                    .setMessage(NeptuneBaseResponseCode.CHECK_PARAM_FAILURE.getMessage());
            return response;
        }
        // 2. 获取变量
        NeptuneRequestHeader header = request.getRequestHeader();
        List<NeptuneCancelUser> body = request.getRequestBody();
        // 3. 查询要删除的用户是否存在
        List<NeptuneUser> userList = userMapper.queryUserById(header.getAppId(),
                body.stream().map(NeptuneCancelUser::getUserId)
                        .collect(Collectors.toList()));
        if (CollectionUtil.isEmpty(userList) || userList.size() != body.size()){
            log.error("NeptuneUserService cancelUser: 需要删除的所有或者部分用户不存在 - users: {}", body);
            response.setCode(NeptuneUserResponseCode.CANCEL_USER_FAIL.getCode())
                    .setMessage(NeptuneUserResponseCode.CANCEL_USER_FAIL.getMessage());
            return response;
        }
        // 4. 单次删除和批量删除
        if (body.size() == 1){
            int result = userMapper.cancelUser(header.getAppId(), body.get(0));
            if (result <= 0){
                log.error("NeptuneUserService cancelUser: 删除用户数据失败 - user: {}", body.get(0));
                response.setCode(NeptuneUserResponseCode.CANCEL_USER_FAIL.getCode())
                        .setMessage(NeptuneUserResponseCode.CANCEL_USER_FAIL.getMessage());
                return response;
            }
        }else{
            int result = userMapper.batchCancelUser(header.getAppId(), body);
            if (result <= 0){
                log.error("NeptuneUserService cancelUser: 删除用户数据失败 - users: {}", body);
                response.setCode(NeptuneUserResponseCode.CANCEL_USER_FAIL.getCode())
                        .setMessage(NeptuneUserResponseCode.CANCEL_USER_FAIL.getMessage());
                return response;
            }
        }
        // 5. 填充响应信息
        response.setCode(NeptuneBaseResponseCode.SUCCESS.getCode())
                .setMessage(NeptuneBaseResponseCode.SUCCESS.getMessage());
        return response;
    }

}
