package com.fuyusakaiori.nep.im.service.core.friendship.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.example.nep.im.common.enums.code.NepFriendshipApplicationResponseCode;
import com.example.nep.im.common.enums.status.NepFriendshipApplicationApproveStatus;
import com.example.nep.im.common.enums.status.NepFriendshipStatus;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.NepFriendship;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.NepFriendshipApplication;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.apply.NepApproveFriendshipApplicationRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.apply.NepSendFriendshipApplicationRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.normal.NepAddFriendshipRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.mapper.INepFriendshipApplicationMapper;
import com.fuyusakaiori.nep.im.service.core.user.entity.dto.NepFriend;
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

    public int doSendFriendshipApplication(NepSendFriendshipApplicationRequest request) {
        // 1. 获取变量
        Integer appId = request.getHeader().getAppId();
        Integer friendFromId = request.getFriendFromId();
        Integer friendToId = request.getFriendToId();
        // 2. 查询此前用户是否向对方发送过好友请求
        NepFriendshipApplication application =
                friendshipApplicationMapper.queryFriendshipApplicationByUserId(appId, friendFromId, friendToId);
        // 3. 校验好友申请
        if (Objects.isNull(application)){
            // 3.1 如果好友申请不存在, 那么向数据库中插入新的好友申请
            int isSendApplication = friendshipApplicationMapper.sendFriendshipApplication(appId,
                    BeanUtil.copyProperties(request, NepFriendshipApplication.class)
                            .setCreateTime(System.currentTimeMillis()).setUpdateTime(System.currentTimeMillis()));
            if (isSendApplication <= 0){
                log.error("NepFriendshipApplicationServiceImpl doSendFriendshipApplication: 发送好友申请失败 - request: {}", request);
            }
            return isSendApplication;
        }else{
            // 3.2 如果好友申请存在, 那么就更新好友申请的信息
            int isUpdateApplication = friendshipApplicationMapper.updateFriendshipApplication(appId, BeanUtil.copyProperties(request, NepFriendshipApplication.class).setUpdateTime(System.currentTimeMillis()));
            if (isUpdateApplication <= 0){
                log.error("NepFriendshipApplicationServiceImpl doSendFriendshipApplication: 发送好友申请失败 - request: {}", request);
            }
            return isUpdateApplication;
        }
    }

    public NepFriend doApproveFriendshipApplication(NepApproveFriendshipApplicationRequest request){
        // 0. 获取变量
        Integer appId = request.getHeader().getAppId();
        Integer applyId = request.getApplyId();
        Integer approveStatus = request.getApproveStatus();
        // 1. 查询是否有好友请求
        NepFriendshipApplication application = friendshipApplicationMapper.queryFriendshipApplicationById(appId, applyId);
        // 2. 校验好友请求是否合法
        if (Objects.isNull(application)){
            log.error("NepFriendshipApplicationService doApproveFriendshipApplication: 好友请求不存在 - request: {}", request);
            return null;
        }
        if (NepFriendshipApplicationApproveStatus.UNAPPROVED.getStatus() != application.getApplyApproveStatus()){
            log.error("NepFriendshipApplicationService doApproveFriendshipApplication: 好友请求已经审批过 - request: {}", request);
            return null;
        }
        // 3. 审批好友请求
        int isApprove = friendshipApplicationMapper.approveFriendshipApplication(appId, applyId, approveStatus, System.currentTimeMillis());
        if (isApprove <= 0){
            log.error("NepFriendshipApplicationService doApproveFriendshipApplication: 好友请求审批失败 - request: {}", request);
            return null;
        }
        // 4. 如果拒绝好友申请, 返回空的对象
        if (NepFriendshipApplicationApproveStatus.REJECT.getStatus() == approveStatus){
            return new NepFriend();
        }
        // 5. 如果同意好友申请, 执行好友添加
        if (NepFriendshipApplicationApproveStatus.AGREE.getStatus() == approveStatus){
            NepFriend newFriend = friendshipDealService.doAddFriendshipDirectly(request.getHeader(),
                    BeanUtil.copyProperties(application, NepFriendship.class)
                            .setFriendFromId(application.getFriendToId()).setFriendToId(application.getFriendFromId()));
            // 6. 检验好友添加是否成功
            if (Objects.isNull(newFriend)){
                log.error("NepFriendshipApplicationService doApproveFriendshipApplication: 好友添加失败 - request: {}", request);
                return null;
            }
            return newFriend;
        }
        return null;
    }

}
