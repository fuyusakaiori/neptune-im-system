package com.fuyusakaiori.nep.im.service.core.friendship.service.impl;

import com.example.neptune.im.common.entity.request.NepRequestHeader;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.NepFriendshipApplication;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.dto.NepAddFriendship;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.dto.NepSendFriendshipApplication;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.apply.NepSendFriendshipApplicationRequest;
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
            result = friendshipApplicationMapper.updateFriendshipApplication(header.getAppId(), transferToFriendshipApplication(body));
            if (result <= 0){
                log.error("NepFriendshipApplicationServiceImpl doSendFriendshipApplication: {} 向 {} 发送好友申请失败 (更新) - body: {}", friendFromId, friendToId, body);
            }
        }
        return result;
    }


    private NepSendFriendshipApplication transferToFriendshipApplication(NepAddFriendship body) {
        return new NepSendFriendshipApplication()
                       .setFriendFromId(body.getFriendFromId())
                       .setFriendToId(body.getFriendToId())
                       .setRemark(body.getFriendRemark())
                       .setAdditionalInfo(body.getAdditionalInfo())
                       .setSource(body.getFriendshipSource());
    }

}
