package com.fuyusakaiori.nep.im.service.core.friendship.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.example.nep.im.common.constant.NepCallBackConstant;
import com.example.nep.im.common.entity.request.NepRequestHeader;
import com.example.nep.im.common.enums.code.NepBaseResponseCode;
import com.example.nep.im.common.enums.code.NepFriendshipResponseCode;
import com.example.nep.im.common.enums.code.NepUserResponseCode;
import com.example.nep.im.common.enums.status.NepFriendshipAllowType;
import com.example.nep.im.common.enums.status.NepFriendshipStatus;
import com.fuyusakaiori.nep.im.service.config.NepApplicationConfig;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.NepFriendship;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.dto.NepAddFriendship;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.normal.*;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.normal.NepCheckFriendshipResponse;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.normal.NepModifyFriendshipResponse;
import com.fuyusakaiori.nep.im.service.core.friendship.mapper.INepFriendshipMapper;
import com.fuyusakaiori.nep.im.service.core.friendship.service.INepFriendshipService;
import com.fuyusakaiori.nep.im.service.core.user.entity.NepUser;
import com.fuyusakaiori.nep.im.service.core.user.mapper.INepUserMapper;
import com.fuyusakaiori.nep.im.service.callback.INepCallBackService;
import com.fuyusakaiori.nep.im.service.util.check.NepCheckFriendshipParamUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
public class NepFriendshipService implements INepFriendshipService {

    @Autowired
    private NepApplicationConfig applicationConfig;

    @Autowired
    private INepCallBackService callBackService;

    @Autowired
    private INepUserMapper userMapper;

    @Autowired
    private INepFriendshipMapper friendshipMapper;
    @Autowired
    private NepFriendshipServiceImpl friendshipServiceImpl;

    @Autowired
    private NepFriendshipApplicationServiceImpl friendshipApplicationServiceImpl;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public NepModifyFriendshipResponse addFriendship(NepAddFriendshipRequest request) {
        // 0. 准备响应结果
        NepModifyFriendshipResponse response = new NepModifyFriendshipResponse();
        // 1. 参数校验
        if (!NepCheckFriendshipParamUtil.checkNepAddFriendshipRequestParam(request)){
            log.error("NeptuneFriendshipService addFriendship: 参数校验失败 - request: {}", request);
            return response.setCode(NepBaseResponseCode.CHECK_PARAM_FAIL.getCode())
                    .setMessage(NepBaseResponseCode.CHECK_PARAM_FAIL.getMessage());
        }
        // 2. 获取变量
        NepRequestHeader header = request.getRequestHeader();
        NepAddFriendship body = request.getRequestBody();
        // TODO 2. 添加好友前执行回调
        if (applicationConfig.isAddFriendshipBeforeCallBack()){
            NepModifyFriendshipResponse callbackResponse = callBackService.beforeCallBack(header.getAppId(), NepCallBackConstant.FRIEND_ADD_BEFORE,
                    JSONUtil.toJsonStr(request), NepModifyFriendshipResponse.class);
            if (Objects.isNull(callbackResponse)){
                return response.setCode(NepBaseResponseCode.CALLBACK_FAIL.getCode())
                               .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
            }
        }
        // 4. 查询用户是否存在: 没有直接查关系表是因为需要判断用户是否可以添加好友
        NepUser fromUser = userMapper.querySimpleUserById(header.getAppId(), body.getFriendFromId());
        NepUser toUser = userMapper.querySimpleUserById(header.getAppId(), body.getFriendToId());
        if (Objects.isNull(fromUser) || fromUser.getIsDelete() || Objects.isNull(toUser) || toUser.getIsDelete()){
            log.error("NeptuneFriendshipService addFriendship: 新增的好友关系中有一方用户是不存在的 - request: {}", request);
            return response.setCode(NepUserResponseCode.USER_NOT_EXIST.getCode())
                    .setMessage(NepUserResponseCode.USER_NOT_EXIST.getMessage());
        }
        // 5. 检查用户是否可以添加好友
        Integer type = toUser.getUserFriendshipAllowType();
        if (Objects.isNull(type)){
            log.error("NeptuneFriendshipService addFriendship: 用户添加好友类型为空 - fromId: {},  toId: {}", fromUser.getUserId(), toUser.getUserId());
            return response.setCode(NepBaseResponseCode.UNKNOWN_ERROR.getCode())
                    .setMessage(NepBaseResponseCode.UNKNOWN_ERROR.getMessage());
        }
        // 5.1 如果对方不允许添加好友, 那么直接返回
        if (type == NepFriendshipAllowType.BAN.getType()){
            log.error("NeptuneFriendshipService addFriendship: 对方不允许添加好友 - from: {}, to: {}", fromUser, toUser);
            return response.setCode(NepFriendshipResponseCode.FRIENDSHIP_IS_BAN.getCode())
                           .setMessage(NepFriendshipResponseCode.FRIENDSHIP_IS_BAN.getMessage());
        }
        // 5.2 如果对方不需要验证就能添加好友, 那么直接添加
        if (type == NepFriendshipAllowType.ANY.getType()){
            int result = friendshipServiceImpl.doAddFriendship(header, body);
            if (result <= 0){
                return response.setCode(NepFriendshipResponseCode.FRIENDSHIP_ADD_FAIL.getCode())
                               .setMessage(NepFriendshipResponseCode.FRIENDSHIP_ADD_FAIL.getMessage());
            }
        }
        // 5.3 如果对方需要验证才能添加, 那么走验证后添加的逻辑: 1. 发送好友申请后就结束 2. 对方审批好友申请后执行添加方法
        if (type == NepFriendshipAllowType.VALIDATION.getType()){
            int result = friendshipApplicationServiceImpl.doSendFriendshipApplication(header, body);
            if (result <= 0){
                return response.setCode(NepFriendshipResponseCode.FRIENDSHIP_ADD_FAIL.getCode())
                               .setMessage(NepFriendshipResponseCode.FRIENDSHIP_ADD_FAIL.getMessage());
            }
        }
        // TODO 执行回调
        if (applicationConfig.isAddFriendshipAfterCallBack()){
            callBackService.afterCallBack(header.getAppId(), NepCallBackConstant.FRIEND_ADD_AFTER, JSONUtil.toJsonStr(request));
        }
        // 6. 填充响应结果
        return response.setCode(NepBaseResponseCode.SUCCESS.getCode())
                .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public NepModifyFriendshipResponse editFriendshipRemark(NepEditFriendshipRemarkRequest request) {
        // 0. 准备响应结果
        NepModifyFriendshipResponse response = new NepModifyFriendshipResponse();
        // 1. 参数校验
        if (!NepCheckFriendshipParamUtil.checkNepEditFriendshipRequestParam(request)){
            log.error("NeptuneFriendshipService editFriendship: 参数校验失败 - request: {}", request);
            return response.setCode(NepBaseResponseCode.CHECK_PARAM_FAIL.getCode())
                    .setMessage(NepBaseResponseCode.CHECK_PARAM_FAIL.getMessage());
        }
        // 2. 获取变量
        NepRequestHeader header = request.getRequestHeader();
        Integer friendFromId = request.getFriendFromId();
        Integer friendToId = request.getFriendToId();
        String friendRemark = request.getFriendRemark();
        // 3. 查询好友关系
        NepFriendship friendship = friendshipMapper.queryFriendshipById(header.getAppId(), friendFromId, friendToId);
        // 4. 检查好友关系是否存在
        if (Objects.nonNull(friendship) && friendship.getFriendshipStatus() == NepFriendshipStatus.FRIENDSHIP_NORMAL.getStatus()){
            // 4.1 如果好友关系双向存在, 那么直接更新
            int result = friendshipMapper.editFriendshipRemark(header.getAppId(), friendFromId, friendToId, friendRemark, System.currentTimeMillis());
            if (result <= 0){
                log.error("NeptuneFriendshipService editFriendship: 更新好友关系失败 - fromId: {}, toId: {}", friendFromId, friendToId);
                return response.setCode(NepFriendshipResponseCode.FRIENDSHIP_UPDATE_FAIL.getCode())
                        .setMessage(NepFriendshipResponseCode.FRIENDSHIP_UPDATE_FAIL.getMessage());
            }
            // TODO 执行回调
            if (applicationConfig.isEditFriendshipRemarkAfterCallBack()){
                callBackService.afterCallBack(header.getAppId(), NepCallBackConstant.FRIEND_EDIT_AFTER, JSONUtil.toJsonStr(request));
            }
            return response.setCode(NepBaseResponseCode.SUCCESS.getCode())
                    .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
        }else{
            // 4.2 如果好友关系不存在, 那么返回不存在
            log.error("NeptuneFriendshipService editFriendship: 好友关系不存在 - fromId: {}, toId: {}", friendFromId, friendToId);
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
            return response.setCode(NepBaseResponseCode.CHECK_PARAM_FAIL.getCode())
                    .setMessage(NepBaseResponseCode.CHECK_PARAM_FAIL.getMessage());
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
            // TODO 执行回调
            if (applicationConfig.isReleaseFriendshipAfterCallBack()){
                callBackService.afterCallBack(header.getAppId(), NepCallBackConstant.FRIEND_DELETE_AFTER, JSONUtil.toJsonStr(request));
            }
            return response.setCode(NepBaseResponseCode.SUCCESS.getCode())
                    .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
        }else{
            // 4.2 好友关系不存在
            log.error("NeptuneFriendshipService releaseFriendship: 好友关系不存在 - fromId: {}, toId: {}", fromId, toId);
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
            return response.setCode(NepBaseResponseCode.CHECK_PARAM_FAIL.getCode())
                    .setMessage(NepBaseResponseCode.CHECK_PARAM_FAIL.getMessage());
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
            return response.setCode(NepFriendshipResponseCode.FRIENDSHIP_RELEASE_ALL_FAIL.getCode())
                    .setMessage(NepFriendshipResponseCode.FRIENDSHIP_RELEASE_ALL_FAIL.getMessage());
        }
        // TODO 执行回调
        if (applicationConfig.isReleaseAllFriendshipAfterCallBack()){
            callBackService.afterCallBack(header.getAppId(), NepCallBackConstant.FRIEND_DELETE_AFTER, JSONUtil.toJsonStr(request));
        }
        return response.setCode(NepBaseResponseCode.SUCCESS.getCode())
                .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
    }

    @Override
    public NepCheckFriendshipResponse checkFriendship(NepCheckFriendshipRequest request) {
        return null;
    }
}
