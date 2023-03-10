package com.fuyusakaiori.nep.im.service.core.friendship.service.impl;

import com.example.nep.im.common.entity.request.NepRequestHeader;
import com.example.nep.im.common.enums.status.NepFriendshipApplicationApproveStatus;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.NepFriendshipApplication;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.dto.NepAddFriendship;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.dto.NepSendFriendshipApplication;
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
    private NepFriendshipServiceImpl friendshipServiceImpl;

    public int doSendFriendshipApplication(NepRequestHeader header, NepAddFriendship body) {
        // 1. 获取变量
        Integer friendFromId = body.getFriendFromId();
        Integer friendToId = body.getFriendToId();
        // 2. 查询此前用户是否向对方发送过好友请求
        NepFriendshipApplication application =
                friendshipApplicationMapper.queryFriendshipApplicationByUserId(header.getAppId(), friendFromId, friendToId);
        // 3. 校验好友申请
        int result = 0;
        if (Objects.isNull(application)){
            // 3.1 如果好友申请不存在, 那么向数据库中插入新的好友申请
            result = friendshipApplicationMapper.sendFriendshipApplication(header.getAppId(), transferToFriendshipApplication(body),
                    System.currentTimeMillis(), System.currentTimeMillis());
            if (result <= 0){
                log.error("NepFriendshipApplicationServiceImpl doSendFriendshipApplication: {} 向 {} 发送好友申请失败 - body: {}", friendFromId, friendToId, body);
            }
        }else{
            // 3.2 如果好友申请存在, 那么就更新好友申请的信息
            result = friendshipApplicationMapper.updateFriendshipApplication(header.getAppId(), transferToFriendshipApplication(body), System.currentTimeMillis());
            if (result <= 0){
                log.error("NepFriendshipApplicationServiceImpl doSendFriendshipApplication: {} 向 {} 发送好友申请失败 (更新) - body: {}", friendFromId, friendToId, body);
            }
        }
        return result;
    }

    public int doApproveFriendshipApplication(NepRequestHeader header, int applyId, int approveStatus){
        // 1. 查询是否有好友请求
        NepFriendshipApplication application = friendshipApplicationMapper.queryFriendshipApplicationById(header.getAppId(), applyId);
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
        int isApprove = friendshipApplicationMapper.approveFriendshipApplication(header.getAppId(), applyId, approveStatus, System.currentTimeMillis());
        if (isApprove <= 0){
            log.error("NepFriendshipApplicationService doApproveFriendshipApplication: {} 审批 {} 发送的好友申请失败 - applyId: {}",
                    application.getFriendshipToId(), application.getFriendshipFromId(), applyId);
            return isApprove;
        }
        // 4. 如果同意好友申请, 那么执行好友添加
        return friendshipServiceImpl.doAddFriendship(header, transferToAddFriendship(application));
    }


    private NepSendFriendshipApplication transferToFriendshipApplication(NepAddFriendship body) {
        return new NepSendFriendshipApplication()
                       .setFriendFromId(body.getFriendFromId())
                       .setFriendToId(body.getFriendToId())
                       .setRemark(body.getFriendRemark())
                       .setAdditionalInfo(body.getAdditionalInfo())
                       .setSource(body.getFriendshipSource());
    }

    private NepAddFriendship transferToAddFriendship(NepFriendshipApplication application){
        return new NepAddFriendship()
                       .setFriendFromId(application.getFriendshipFromId())
                       .setFriendToId(application.getFriendshipToId())
                       .setFriendshipSource(application.getApplySource())
                       .setFriendRemark(application.getApplyRemark())
                       .setAdditionalInfo(application.getApplyAdditionalInfo());
    }

}
