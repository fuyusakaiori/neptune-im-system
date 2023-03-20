package com.fuyusakaiori.nep.im.service.core.friendship.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.example.nep.im.common.enums.status.NepFriendshipApplicationApproveStatus;
import com.example.nep.im.common.enums.status.NepFriendshipStatus;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.NepFriendship;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.NepFriendshipApplication;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.apply.NepApproveFriendshipApplicationRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.normal.NepAddFriendshipRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.mapper.INepFriendshipApplicationMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class NepFriendshipApplicationServiceImpl {

    @Autowired
    private INepFriendshipApplicationMapper friendshipApplicationMapper;

    @Autowired
    private NepFriendshipDealService friendshipDealService;

    public int doSendFriendshipApplication(NepAddFriendshipRequest request) {
        // 1. 获取变量
        Integer appId = request.getHeader().getAppId();
        Integer friendFromId = request.getFriendFromId();
        Integer friendToId = request.getFriendToId();
        // 2. 查询此前用户是否向对方发送过好友请求
        NepFriendshipApplication application =
                friendshipApplicationMapper.queryFriendshipApplicationByUserId(appId, friendFromId, friendToId);
        // 3. 校验好友申请
        int result;
        if (Objects.isNull(application)){
            // 3.1 如果好友申请不存在, 那么向数据库中插入新的好友申请
            result = friendshipApplicationMapper.sendFriendshipApplication(appId, BeanUtil.copyProperties(request, NepFriendshipApplication.class)
                                                                                          .setCreateTime(System.currentTimeMillis()).setUpdateTime(System.currentTimeMillis()));
            if (result <= 0){
                log.error("NepFriendshipApplicationServiceImpl doSendFriendshipApplication: 用户-{} 向 用户-{} 发送好友申请失败 - request: {}", friendFromId, friendToId, result);
            }
        }else{
            // 3.2 如果好友申请存在, 那么就更新好友申请的信息
            result = friendshipApplicationMapper.updateFriendshipApplication(appId, BeanUtil.copyProperties(request, NepFriendshipApplication.class)
                                                                                            .setUpdateTime(System.currentTimeMillis()));
            if (result <= 0){
                log.error("NepFriendshipApplicationServiceImpl doSendFriendshipApplication: {} 向 {} 发送好友申请失败 (更新) - request: {}", friendFromId, friendToId, request);
            }
        }
        return result;
    }

    public int doApproveFriendshipApplication(NepApproveFriendshipApplicationRequest request){
        // 0. 获取变量
        Integer appId = request.getHeader().getAppId();
        Integer applyId = request.getApplyId();
        Integer approveStatus = request.getApproveStatus();
        // 1. 查询是否有好友请求
        NepFriendshipApplication application = friendshipApplicationMapper.queryFriendshipApplicationById(appId, applyId);
        // TODO 这里首先需要检验发起审批请求的是否是自己
        // 2. 校验好友请求是否合法
        if (Objects.isNull(application)){
            log.error("NepFriendshipApplicationService doApproveFriendshipApplication: 好友请求不存在 - applyId: {}", applyId);
            return 0;
        }
        if (NepFriendshipApplicationApproveStatus.UNAPPROVED.getStatus() != application.getApplyApproveStatus()){
            log.error("NepFriendshipApplicationService doApproveFriendshipApplication: 好友请求已经审批过 - applyId: {}", applyId);
            return 0;
        }
        // 3. 审批好友请求
        int isApprove = friendshipApplicationMapper.approveFriendshipApplication(appId, applyId, approveStatus, System.currentTimeMillis());
        if (isApprove <= 0){
            log.error("NepFriendshipApplicationService doApproveFriendshipApplication: {} 审批 {} 发送的好友申请失败 - applyId: {}",
                    application.getFriendToId(), application.getFriendFromId(), applyId);
            return isApprove;
        }
        // 4. 如果拒绝添加好友, 那么直接返回
        if (NepFriendshipApplicationApproveStatus.REJECT.getStatus() == approveStatus){
            return isApprove;
        }
        // 5. 如果同意好友申请, 那么执行好友添加
        return friendshipDealService.doAddFriendshipDirectly(appId, BeanUtil.copyProperties(application, NepFriendship.class,
                "friendshipApplyId", "applyReadStatus", "applyApproveStatus").setFriendshipStatus(NepFriendshipStatus.FRIENDSHIP_NORMAL.getStatus()));
    }

}
