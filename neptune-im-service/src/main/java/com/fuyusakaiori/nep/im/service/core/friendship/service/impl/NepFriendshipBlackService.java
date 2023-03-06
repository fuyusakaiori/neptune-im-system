package com.fuyusakaiori.nep.im.service.core.friendship.service.impl;

import com.example.neptune.im.common.entity.request.NepRequestHeader;
import com.example.neptune.im.common.enums.code.NepBaseResponseCode;
import com.example.neptune.im.common.enums.code.NepFriendshipBlackResponseCode;
import com.example.neptune.im.common.enums.code.NepFriendshipResponseCode;
import com.example.neptune.im.common.enums.status.NepFriendshipBlackCheckType;
import com.example.neptune.im.common.enums.status.NepFriendshipBlackStatus;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.NepFriendship;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.black.NepAddFriendshipBlackRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.black.NepCheckFriendshipBlackRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.black.NepRemoveFriendshipBlackRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.black.NepCheckFriendshipBlackResponse;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.normal.NepModifyFriendshipResponse;
import com.fuyusakaiori.nep.im.service.core.friendship.mapper.INepFriendshipBlackMapper;
import com.fuyusakaiori.nep.im.service.core.friendship.mapper.INepFriendshipMapper;
import com.fuyusakaiori.nep.im.service.core.friendship.service.INepFriendshipBlackService;
import com.fuyusakaiori.nep.im.service.util.NepCheckFriendshipBlackParamUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class NepFriendshipBlackService implements INepFriendshipBlackService {

    @Autowired
    private INepFriendshipBlackMapper friendshipBlackMapper;

    @Autowired
    private INepFriendshipMapper friendshipMapper;

    @Override
    public NepModifyFriendshipResponse addFriendInBlackList(NepAddFriendshipBlackRequest request) {
        // 0. 准备响应结果
        NepModifyFriendshipResponse response = new NepModifyFriendshipResponse();
        // 1. 参数校验
        if (NepCheckFriendshipBlackParamUtil.checkAddFriendInBlackListRequestParam(request)){
            log.error("NepFriendshipBlackService addFriendInBlackList: 参数校验失败 - request: {}", request);
            return response.setCode(NepBaseResponseCode.CHECK_PARAM_FAILURE.getCode())
                           .setMessage(NepBaseResponseCode.CHECK_PARAM_FAILURE.getMessage());
        }
        // 2. 获取变量
        NepRequestHeader header = request.getRequestHeader();
        Integer friendFromId = request.getFriendFromId();
        Integer friendToId = request.getFriendToId();
        // 3. 查询好友关系
        NepFriendship friendship = friendshipMapper.queryFriendshipById(header.getAppId(), friendFromId, friendToId);
        // 4. 校验好友关系是否存在
        if (Objects.isNull(friendship)){
            log.error("NepFriendshipBlackService addFriendInBlackList: 好友关系不存在 - request: {}", request);
            return response.setCode(NepFriendshipResponseCode.FRIENDSHIP_NOT_EXIST.getCode())
                           .setMessage(NepFriendshipResponseCode.FRIENDSHIP_NOT_EXIST.getMessage());
        }
        // 5. 校验是否已经被拉黑
        int blackStatus = friendshipBlackMapper.checkFriendInBlackList(header.getAppId(), friendFromId, friendToId);
        if (NepFriendshipBlackStatus.BLACK.getStatus() == blackStatus){
            log.error("NepFriendshipBlackService addFriendInBlackList: 好友已经被拉黑, 不要重复拉黑 - request: {}", request);
            return response.setCode(NepFriendshipBlackResponseCode.FRIEND_IS_BLACK.getCode())
                           .setMessage(NepFriendshipBlackResponseCode.FRIEND_IS_BLACK.getMessage());
        }
        // 6. 拉黑好友
        int result = friendshipBlackMapper.addFriendInBlackList(header.getAppId(), friendFromId, friendToId);
        if (result <= 0){
            log.error("NepFriendshipBlackService addFriendInBlackList: 好友拉黑失败 - request: {}", request);
            return response.setCode(NepFriendshipBlackResponseCode.FRIEND_BLACK_FAIL.getCode())
                           .setMessage(NepFriendshipBlackResponseCode.FRIEND_BLACK_FAIL.getMessage());
        }
        return response.setCode(NepBaseResponseCode.SUCCESS.getCode())
                       .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
    }

    @Override
    public NepModifyFriendshipResponse removeFriendInBlackList(NepRemoveFriendshipBlackRequest request) {
        // 0. 准备响应结果
        NepModifyFriendshipResponse response = new NepModifyFriendshipResponse();
        // 1. 参数校验
        if (NepCheckFriendshipBlackParamUtil.checkRemoveFriendInBlackListRequestParam(request)){
            log.error("NepFriendshipBlackService removeFriendInBlackList: 参数校验失败 - request: {}", request);
            return response.setCode(NepBaseResponseCode.CHECK_PARAM_FAILURE.getCode())
                           .setMessage(NepBaseResponseCode.CHECK_PARAM_FAILURE.getMessage());
        }
        // 2. 获取变量
        NepRequestHeader header = request.getRequestHeader();
        Integer friendFromId = request.getFriendFromId();
        Integer friendToId = request.getFriendToId();
        // 3. 查询好友关系
        NepFriendship friendship = friendshipMapper.queryFriendshipById(header.getAppId(), friendFromId, friendToId);
        // 4. 校验好友关系是否存在
        if (Objects.isNull(friendship)){
            log.error("NepFriendshipBlackService removeFriendInBlackList: 好友关系不存在 - request: {}", request);
            return response.setCode(NepFriendshipResponseCode.FRIENDSHIP_NOT_EXIST.getCode())
                           .setMessage(NepFriendshipResponseCode.FRIENDSHIP_NOT_EXIST.getMessage());
        }
        // 5. 校验是否已经被拉黑
        int blackStatus = friendshipBlackMapper.checkFriendInBlackList(header.getAppId(), friendFromId, friendToId);
        if (NepFriendshipBlackStatus.WHITE.getStatus() == blackStatus){
            log.error("NepFriendshipBlackService removeFriendInBlackList: 好友没有被拉黑, 请不要撤销未拉黑的用户 - request: {}", request);
            return response.setCode(NepFriendshipBlackResponseCode.FRIEND_IS_WHITE.getCode())
                           .setMessage(NepFriendshipBlackResponseCode.FRIEND_IS_WHITE.getMessage());
        }
        // 6. 撤销好友拉黑
        int result = friendshipBlackMapper.removeFriendInBlackList(header.getAppId(), friendFromId, friendToId);
        if (result <= 0){
            log.error("NepFriendshipBlackService removeFriendInBlackList: 撤销好友拉黑失败 - request: {}", request);
            return response.setCode(NepFriendshipBlackResponseCode.FRIEND_WHITE_FAIL.getCode())
                           .setMessage(NepFriendshipBlackResponseCode.FRIEND_WHITE_FAIL.getMessage());
        }
        return response.setCode(NepBaseResponseCode.SUCCESS.getCode())
                       .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
    }

    @Override
    public NepCheckFriendshipBlackResponse checkFriendInBlackList(NepCheckFriendshipBlackRequest request) {
        // 0. 准备响应结果
        NepCheckFriendshipBlackResponse response = new NepCheckFriendshipBlackResponse();
        // 1. 参数校验
        if (NepCheckFriendshipBlackParamUtil.checkVerifyFriendInBlackListRequestParam(request)){
            log.error("NepFriendshipBlackService checkFriendInBlackList: 参数校验失败 - request: {}", request);
            return response.setCode(NepBaseResponseCode.CHECK_PARAM_FAILURE.getCode())
                           .setMessage(NepBaseResponseCode.CHECK_PARAM_FAILURE.getMessage());
        }
        // 2. 获取变量
        NepRequestHeader header = request.getRequestHeader();
        Integer friendFromId = request.getFriendFromId();
        Integer friendToId = request.getFriendToId();
        Integer checkType = request.getCheckType();
        // 3. 查询好友关系
        NepFriendship friendship = friendshipMapper.queryFriendshipById(header.getAppId(), friendFromId, friendToId);
        // 4. 校验好友关系是否存在
        if (Objects.isNull(friendship)){
            log.error("NepFriendshipBlackService checkFriendInBlackList: 好友关系不存在 - request: {}", request);
            return response.setCode(NepFriendshipResponseCode.FRIENDSHIP_NOT_EXIST.getCode())
                           .setMessage(NepFriendshipResponseCode.FRIENDSHIP_NOT_EXIST.getMessage());
        }
        // 5. 校验拉黑状态
        int result = 0;
        if (NepFriendshipBlackCheckType.SINGLE.getType() == checkType){
            result = friendshipBlackMapper.checkFriendInBlackList(header.getAppId(), friendFromId, friendToId);
        }else{
            result = friendshipBlackMapper.checkBiFriendInBlackList(header.getAppId(), friendFromId, friendToId);
        }
        if (result <= 0){
            log.error("NepFriendshipBlackService checkFriendInBlackList: 校验好友关系的拉黑状态失败 - request: {}", request);
            return response.setCode(NepFriendshipBlackResponseCode.FRIEND_IS_BLACK.getCode())
                           .setMessage(NepFriendshipBlackResponseCode.FRIEND_IS_BLACK.getMessage());
        }
        return response.setStatus(result)
                       .setCode(NepBaseResponseCode.SUCCESS.getCode())
                       .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
    }

}
