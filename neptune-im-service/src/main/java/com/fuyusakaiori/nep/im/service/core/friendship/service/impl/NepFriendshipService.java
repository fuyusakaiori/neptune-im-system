package com.fuyusakaiori.nep.im.service.core.friendship.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.example.neptune.im.common.entity.request.NepRequestHeader;
import com.example.neptune.im.common.enums.code.NepBaseResponseCode;
import com.example.neptune.im.common.enums.code.NepFriendshipResponseCode;
import com.example.neptune.im.common.enums.code.NepUserResponseCode;
import com.example.neptune.im.common.enums.status.NepFriendshipAllowType;
import com.example.neptune.im.common.enums.status.NepFriendshipStatus;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.NepFriendship;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.dto.NepAddFriendship;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.dto.NepEditFriendship;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.normal.*;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.NepModifyFriendshipResponse;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.NepQueryFriendshipResponse;
import com.fuyusakaiori.nep.im.service.core.friendship.mapper.INepFriendshipMapper;
import com.fuyusakaiori.nep.im.service.core.friendship.service.INepFriendshipService;
import com.fuyusakaiori.nep.im.service.core.user.entity.NepUser;
import com.fuyusakaiori.nep.im.service.core.user.mapper.INepUserMapper;
import com.fuyusakaiori.nep.im.service.util.NepCheckFriendshipParamUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
public class NepFriendshipService implements INepFriendshipService {
    @Autowired
    private INepFriendshipMapper friendshipMapper;

    @Autowired
    private INepUserMapper userMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public NepModifyFriendshipResponse batchAddFriendship(NepImportFriendshipRequest request) {
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public NepModifyFriendshipResponse addFriendship(NepAddFriendshipRequest request) {
        // 0. 准备响应结果
        NepModifyFriendshipResponse response = new NepModifyFriendshipResponse();
        // 1. 参数校验
        if (!NepCheckFriendshipParamUtil.checkNepAddFriendshipRequestParam(request)){
            log.error("NeptuneFriendshipService addFriendship: 参数校验失败 - request: {}", request);
            response.setCode(NepBaseResponseCode.CHECK_PARAM_FAILURE.getCode())
                    .setMessage(NepBaseResponseCode.CHECK_PARAM_FAILURE.getMessage());
            return response;
        }
        // 2. 获取变量
        NepRequestHeader header = request.getRequestHeader();
        NepAddFriendship body = request.getRequestBody();
        // 3. 查询用户是否存在
        NepUser fromUser = userMapper.querySimpleUserById(header.getAppId(), body.getFriendFromId());
        NepUser toUser = userMapper.querySimpleUserById(header.getAppId(), body.getFriendToId());
        if (Objects.isNull(fromUser) || Objects.isNull(toUser)){
            log.error("NeptuneFriendshipService addFriendship: 新增的好友关系中有一方用户是不存在的 - request: {}", request);
            response.setCode(NepUserResponseCode.QUERY_USER_NOT_EXIST.getCode())
                    .setMessage(NepUserResponseCode.QUERY_USER_NOT_EXIST.getMessage());
            return response;
        }
        // 4. 检查用户是否可以添加好友
        Integer type = toUser.getUserFriendshipAllowType();
        if (Objects.nonNull(type)){
            // 4.1 如果对方不允许添加好友, 那么直接返回
            if (type == NepFriendshipAllowType.BAN.getType()){
                log.info("NeptuneFriendshipService addFriendship: 对方不允许添加好友 - from: {}, to: {}", fromUser, toUser);
                return response.setCode(NepFriendshipResponseCode.FRIENDSHIP_IS_BAN.getCode())
                        .setMessage(NepFriendshipResponseCode.FRIENDSHIP_IS_BAN.getMessage());
            }
            // 4.2 如果对方不需要验证就能添加好友, 那么直接添加
            if (type == NepFriendshipAllowType.ANY.getType()){
                return doAddFriendship(header, body);
            }
            // TODO 4.3 如果对方需要验证才能添加, 那么走验证后添加的逻辑
            if (type == NepFriendshipAllowType.VALIDATION.getType()){

            }
        }
        // 5. 填充响应结果
        return response.setCode(NepBaseResponseCode.UNKNOWN_ERROR.getCode())
                .setMessage(NepBaseResponseCode.UNKNOWN_ERROR.getMessage());
    }

    @Transactional(rollbackFor = Exception.class)
    public NepModifyFriendshipResponse doAddFriendship(NepRequestHeader header, NepAddFriendship body){
        NepModifyFriendshipResponse response = new NepModifyFriendshipResponse();
        // 0. 获取变量
        Integer friendFromId = body.getFriendFromId();
        Integer friendToId = body.getFriendToId();
        String remark = body.getFriendRemark();
        String source = body.getFriendshipSource();
        String additionalInfo = body.getAdditionalInfo();
        String extra = body.getFriendshipExtra();
        // 1. 查询两个用户之间的关系
        NepFriendship friendshipFrom = friendshipMapper.queryFriendshipById(header.getAppId(), friendFromId, friendToId);
        // 2. 判断两个用户此前是否添加过好友: 理论上两个关系只能同时存在或者同时不存在, 不可能出现只有一条关系的情况
        if (Objects.isNull(friendshipFrom)){
            // 2.1 如果没有添加过好友, 那么需要插入新的记录
            // 2.1.1 插入 from -> to 的记录
            int result = friendshipMapper.addFriendship(header.getAppId(), body,
                    System.currentTimeMillis(), System.currentTimeMillis());
            if (result <= 0){
                log.error("NeptuneFriendshipService doAddFriendship: 添加 from -> to 好友关系失败 - fromId: {}, toId: {}", friendFromId, friendToId);
                return response.setCode(NepFriendshipResponseCode.FRIENDSHIP_ADD_FAIL.getCode())
                        .setMessage(NepFriendshipResponseCode.FRIENDSHIP_ADD_FAIL.getMessage());
            }
            // TODO 2.1.2 插入 to -> from 的记录
        }else{
            // 2.2 如果已经添加过好友, 那么检查好友关系是否已经被删除:
            if (friendshipFrom.getFriendshipStatus() == NepFriendshipStatus.FRIENDSHIP_NORMAL.getStatus()){
                log.error("NeptuneFriendshipService doAddFriendship: 用户: {} 和 用户: {} 已经是好友了", friendFromId, friendToId);
                return response.setCode(NepFriendshipResponseCode.FRIENDSHIP_EXIST.getCode())
                        .setMessage(NepFriendshipResponseCode.FRIENDSHIP_EXIST.getMessage());
            }
            // 需要重新更新好友关系的状态、备注、来源、附加信息、拓展字段
            NepEditFriendship friendship = new NepEditFriendship().setFriendshipStatus(NepFriendshipStatus.FRIENDSHIP_NORMAL.getStatus())
                                                                  .setFriendRemark(remark).setFriendshipSource(source)
                                                                  .setAdditionalInfo(additionalInfo).setFriendshipExtra(extra);
            // 2.2.1 更新 from -> to
            friendship.setFriendFromId(friendFromId).setFriendToId(friendToId);
            int result = friendshipMapper.editFriendship(header.getAppId(), friendship, System.currentTimeMillis());
            if (result <= 0){
                log.error("NeptuneFriendshipService doAddFriendship: 更新 from -> to 好友关系失败 - fromId: {}, toId: {}", friendFromId, friendToId);
                return response.setCode(NepFriendshipResponseCode.FRIENDSHIP_ADD_FAIL.getCode())
                        .setMessage(NepFriendshipResponseCode.FRIENDSHIP_ADD_FAIL.getMessage());
            }
            // TODO 2.2.2 更新 to -> from 的记录
        }
        return response.setCode(NepBaseResponseCode.SUCCESS.getCode())
                .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public NepModifyFriendshipResponse editFriendship(NepEditFriendshipRequest request) {
        // 0. 准备响应结果
        NepModifyFriendshipResponse response = new NepModifyFriendshipResponse();
        // 1. 参数校验
        if (!NepCheckFriendshipParamUtil.checkNepEditFriendshipRequestParam(request)){
            log.error("NeptuneFriendshipService editFriendship: 参数校验失败 - request: {}", request);
            return response.setCode(NepBaseResponseCode.CHECK_PARAM_FAILURE.getCode())
                    .setMessage(NepBaseResponseCode.CHECK_PARAM_FAILURE.getMessage());
        }
        // 2. 获取变量
        NepRequestHeader header = request.getRequestHeader();
        NepEditFriendship body = request.getRequestBody();
        // 3. 查询好友关系
        NepFriendship friendship = friendshipMapper.queryFriendshipById(
                header.getAppId(), body.getFriendFromId(), body.getFriendToId());
        // 4. 检查好友关系是否存在
        if (Objects.nonNull(friendship) && friendship.getFriendshipStatus() == NepFriendshipStatus.FRIENDSHIP_NORMAL.getStatus()){
            // 4.1 如果好友关系双向存在, 那么直接更新
            int result = friendshipMapper.editFriendship(header.getAppId(), body, System.currentTimeMillis());
            if (result <= 0){
                log.error("NeptuneFriendshipService editFriendship: 更新好友关系失败 - fromId: {}, toId: {}", body.getFriendFromId(), body.getFriendToId());
                return response.setCode(NepFriendshipResponseCode.FRIENDSHIP_UPDATE_FAIL.getCode())
                        .setMessage(NepFriendshipResponseCode.FRIENDSHIP_UPDATE_FAIL.getMessage());
            }
            return response.setCode(NepBaseResponseCode.SUCCESS.getCode())
                    .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
        }else{
            // 4.2 如果好友关系不存在, 那么返回不存在
            log.error("NeptuneFriendshipService editFriendship: 好友关系不存在 - fromId: {}, toId: {}", body.getFriendFromId(), body.getFriendToId());
            return response.setCode(NepFriendshipResponseCode.FRIENDSHIP_NOT_EXIST.getCode())
                    .setMessage(NepFriendshipResponseCode.FRIENDSHIP_NOT_EXIST.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public NepModifyFriendshipResponse releaseFriendship(NeptuneReleaseFriendshipRequest request) {
        // 0. 准备响应结果
        NepModifyFriendshipResponse response = new NepModifyFriendshipResponse();
        // 1. 参数校验
        if (!NepCheckFriendshipParamUtil.checkNepReleaseFriendshipRequestParam(request)){
            log.error("NeptuneFriendshipService releaseFriendship: 参数校验失败 - request: {}", request);
            return response.setCode(NepBaseResponseCode.CHECK_PARAM_FAILURE.getCode())
                    .setMessage(NepBaseResponseCode.CHECK_PARAM_FAILURE.getMessage());
        }
        // 2. 获取变量
        NepRequestHeader header = request.getRequestHeader();
        Integer fromId = request.getFriendFromId(), toId = request.getFriendToId();
        // 3. 查询好友关系: 双向查询
        NepFriendship friendship = friendshipMapper.queryFriendshipById(header.getAppId(), fromId, toId);
        // 4. 判断好友关系
        if (Objects.nonNull(friendship) && friendship.getFriendshipStatus() == NepFriendshipStatus.FRIENDSHIP_NORMAL.getStatus()){
            // 注: 查询得到的好友关系就是存在, 所以也就没必要去判断好友关系是否正常
            int result = friendshipMapper.releaseFriendship(header.getAppId(), fromId, toId, System.currentTimeMillis());
            if (result <= 0){
                log.error("NeptuneFriendshipService releaseFriendship: 好友关系删除失败 - fromId: {}, toId: {}", fromId, toId);
                return response.setCode(NepFriendshipResponseCode.FRIENDSHIP_RELEASE_FAIL.getCode())
                               .setMessage(NepFriendshipResponseCode.FRIENDSHIP_RELEASE_FAIL.getMessage());
            }
            return response.setCode(NepBaseResponseCode.SUCCESS.getCode())
                    .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
        }else{
            // 4.2 好友关系不存在
            return response.setCode(NepFriendshipResponseCode.FRIENDSHIP_NOT_EXIST.getCode())
                    .setMessage(NepFriendshipResponseCode.FRIENDSHIP_NOT_EXIST.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public NepModifyFriendshipResponse releaseAllFriendship(NeptuneReleaseAllFriendshipRequest request) {
        // 0. 准备响应结果
        NepModifyFriendshipResponse response = new NepModifyFriendshipResponse();
        // 1. 参数校验
        if (!NepCheckFriendshipParamUtil.checkNepReleaseAllFriendshipRequestParam(request)){
            log.error("NeptuneFriendshipService releaseAllFriendship: 参数校验失败 - request: {}", request);
            return response.setCode(NepBaseResponseCode.CHECK_PARAM_FAILURE.getCode())
                    .setMessage(NepBaseResponseCode.CHECK_PARAM_FAILURE.getMessage());
        }
        // 2. 获取变量
        NepRequestHeader header = request.getRequestHeader();
        Integer fromId = request.getFriendFromId();
        // TODO 思考: 是直接查询关系表判断是否需要删除？还是查询用户表通过用户是否存在判断是否删除？目前得出的答案：直接查关系表应该就可以，联合索引可以利用最左匹配原则继续查，不会全表扫描
        // 3. 检查用户的好友关系是否为空
        List<NepFriendship> friendshipList = friendshipMapper.queryAllFriendship(header.getAppId(), fromId);
        // 3.1 如果用户没有好友或者用户不存在, 那么直接返回
        if (CollectionUtil.isEmpty(friendshipList)){
            log.error("NeptuneFriendshipService releaseAllFriendship: 用户不存在好友关系或者用户不存在 - userId: {}", fromId);
            return response.setCode(NepFriendshipResponseCode.FRIENDSHIP_NOT_EXIST.getCode())
                    .setMessage(NepFriendshipResponseCode.FRIENDSHIP_NOT_EXIST.getMessage());
        }
        // 3.2 如果用户存在, 那么直接执行删除
        int result = friendshipMapper.releaseAllFriendship(header.getAppId(), fromId, System.currentTimeMillis());
        if (result <= 0){
            log.error("NeptuneFriendshipService releaseAllFriendship: 删除所有好友关系失败 - userId: {}", fromId);
            response.setCode(NepFriendshipResponseCode.FRIENDSHIP_RELEASE_ALL_FAIL.getCode())
                    .setMessage(NepFriendshipResponseCode.FRIENDSHIP_RELEASE_ALL_FAIL.getMessage());
            return response;
        }
        response.setCode(NepBaseResponseCode.SUCCESS.getCode())
                .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
        return response;
    }

    @Override
    public NepQueryFriendshipResponse queryFriendshipById(NepQueryFriendshipByIdRequest request) {
        // 0. 准备响应结果
        NepQueryFriendshipResponse response = new NepQueryFriendshipResponse();
        // 1. 参数校验
        if (!NepCheckFriendshipParamUtil.checkNepQueryFriendshipByIdRequestParam(request)){
            log.error("NeptuneFriendshipService queryFriendshipById: 参数校验失败 - request: {}", request);
            response.setCode(NepBaseResponseCode.CHECK_PARAM_FAILURE.getCode())
                    .setMessage(NepBaseResponseCode.CHECK_PARAM_FAILURE.getMessage());
        }
        // 2. 获取变量
        NepRequestHeader header = request.getRequestHeader();
        Integer fromId = request.getFriendFromId(), toId = request.getFriendToId();
        // 3. 执行查询
        NepFriendship friendship = friendshipMapper.queryFriendshipById(header.getAppId(), fromId, toId);
        // 4 校验查询结果
        if (Objects.isNull(friendship)){
            log.error("NeptuneFriendshipService queryFriendshipById: 用户间的好友关系不存在 - fromId: {}, toId: {}", fromId, toId);
            response.setCode(NepFriendshipResponseCode.FRIENDSHIP_NOT_EXIST.getCode())
                    .setMessage(NepFriendshipResponseCode.FRIENDSHIP_NOT_EXIST.getMessage());
            return response;
        }
        // 5. 填充响应结果
        response.setFriendshipList(Collections.singletonList(friendship))
                .setCode(NepBaseResponseCode.SUCCESS.getCode())
                .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
        return response;
    }

    @Override
    public NepQueryFriendshipResponse queryAllFriendship(NepQueryAllFriendshipRequest request) {
        // 0. 准备响应结果
        NepQueryFriendshipResponse response = new NepQueryFriendshipResponse();
        // 1. 参数校验
        if (!NepCheckFriendshipParamUtil.checkNepQueryAllFriendshipRequestParam(request)){
            log.error("NeptuneFriendshipService queryAllFriendship: 参数校验失败 - request: {}", request);
            response.setCode(NepBaseResponseCode.CHECK_PARAM_FAILURE.getCode())
                    .setMessage(NepBaseResponseCode.CHECK_PARAM_FAILURE.getMessage());
        }
        // 2. 获取变量
        NepRequestHeader header = request.getRequestHeader();
        Integer fromId = request.getFriendFromId();
        // 3. 执行查询
        List<NepFriendship> friendshipList = friendshipMapper.queryAllFriendship(header.getAppId(), fromId);
        // 4 校验查询结果
        if (CollectionUtil.isEmpty(friendshipList)){
            log.error("NeptuneFriendshipService queryAllFriendship: 用户没有任何好友 - fromId: {}", fromId);
            response.setCode(NepFriendshipResponseCode.FRIENDSHIP_NOT_EXIST.getCode())
                    .setMessage(NepFriendshipResponseCode.FRIENDSHIP_NOT_EXIST.getMessage());
            return response;
        }
        // 5. 填充响应结果
        response.setFriendshipList(friendshipList)
                .setCode(NepBaseResponseCode.SUCCESS.getCode())
                .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
        return response;
    }
}
