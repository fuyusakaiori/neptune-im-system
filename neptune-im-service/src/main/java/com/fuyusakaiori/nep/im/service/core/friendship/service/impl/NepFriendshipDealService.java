package com.fuyusakaiori.nep.im.service.core.friendship.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.example.nep.im.common.enums.status.NepFriendshipStatus;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.NepFriendship;
import com.fuyusakaiori.nep.im.service.core.friendship.mapper.INepFriendshipMapper;
import com.fuyusakaiori.nep.im.service.core.message.mq.NepServiceToGateWayMessageProducer;
import com.fuyusakaiori.nep.im.service.core.user.entity.NepUser;
import com.fuyusakaiori.nep.im.service.core.user.entity.dto.NepFriend;
import com.fuyusakaiori.nep.im.service.core.user.mapper.INepUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@Service
public class NepFriendshipDealService {

    @Autowired
    private INepUserMapper userMapper;

    @Autowired
    private INepFriendshipMapper friendshipMapper;

    @Autowired
    private NepServiceToGateWayMessageProducer messageSender;

    @Transactional(rollbackFor = Exception.class)
    public NepFriend doAddFriendshipDirectly(int appId, NepFriendship friendship){
        // 0. 获取变量
        Integer friendFromId = friendship.getFriendFromId();
        Integer friendToId = friendship.getFriendToId();
        // 1. 查询两个用户之间的关系
        NepFriendship friendshipFrom = friendshipMapper.queryFriendshipById(appId, friendFromId, friendToId);
        // 2. 判断两个用户此前是否添加过好友: 理论上两个关系只能同时存在或者同时不存在, 不可能出现只有一条关系的情况
        if (Objects.isNull(friendshipFrom)){
            int isAddFriendship = friendshipMapper.addFriendship(appId,
                    friendship.setCreateTime(System.currentTimeMillis()).setUpdateTime(System.currentTimeMillis()));
            if (isAddFriendship <= 0){
                log.error("NepFriendshipDealService doAddFriendshipDirectly: 添加好友关系失败 - fromId: {}, toId: {}", friendFromId, friendToId);
                return null;
            }
        }else{
            if (friendshipFrom.getFriendshipStatus() == NepFriendshipStatus.FRIENDSHIP_NORMAL.getStatus()){
                log.error("NepFriendshipDealService doAddFriendshipDirectly: 用户双方已经是好友了 - friendFromId: {}, friendToId: {}", friendFromId, friendToId);
                return null;
            }
            int isEditFriendship = friendshipMapper.editFriendship(appId,
                    friendship.setFriendshipStatus(NepFriendshipStatus.FRIENDSHIP_NORMAL.getStatus())
                            .setUpdateTime(System.currentTimeMillis()));
            if (isEditFriendship <= 0){
                log.error("NepFriendshipDealService addFriendship: 更新好友关系失败 - fromId: {}, toId: {}", friendFromId, friendToId);
                return null;
            }
        }
        // 3. 查询新增的好友
        NepUser user = userMapper.queryUserById(appId, friendToId);
        // 4. 拼装返回结果
        NepFriend newFriend = BeanUtil.copyProperties(user, NepFriend.class)
                                   .setFriendRemark(friendship.getFriendRemark());
        // 5. TODO 推送给好友申请发起者
        return newFriend;
    }

}
