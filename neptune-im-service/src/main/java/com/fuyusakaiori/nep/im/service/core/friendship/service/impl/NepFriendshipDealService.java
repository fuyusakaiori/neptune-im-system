package com.fuyusakaiori.nep.im.service.core.friendship.service.impl;

import com.example.nep.im.common.enums.status.NepFriendshipStatus;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.NepFriendship;
import com.fuyusakaiori.nep.im.service.core.friendship.mapper.INepFriendshipMapper;
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
    private INepFriendshipMapper friendshipMapper;


    @Transactional(rollbackFor = Exception.class)
    public int doAddFriendshipDirectly(int appId, NepFriendship friendship){
        // 0. 获取变量
        Integer friendFromId = friendship.getFriendFromId();
        Integer friendToId = friendship.getFriendToId();
        // 1. 查询两个用户之间的关系
        NepFriendship friendshipFrom = friendshipMapper.queryFriendshipById(appId, friendFromId, friendToId);
        // 2. 判断两个用户此前是否添加过好友: 理论上两个关系只能同时存在或者同时不存在, 不可能出现只有一条关系的情况
        if (Objects.isNull(friendshipFrom)){
            // 2.1 如果没有添加过好友, 那么需要插入新的记录: 数据库中需要插入两条数据 -> 直接 SQL 中插入两条
            int result = friendshipMapper.addFriendship(appId, friendship.setCreateTime(System.currentTimeMillis()).setUpdateTime(System.currentTimeMillis()));
            if (result <= 0){
                log.error("NeptuneFriendshipService doAddFriendshipDirectly: 添加好友关系失败 - fromId: {}, toId: {}", friendFromId, friendToId);
            }
            return result;
        }else{
            // 2.2.1 如果已经添加过好友, 那么检查好友关系是否已经被删除:
            if (friendshipFrom.getFriendshipStatus() == NepFriendshipStatus.FRIENDSHIP_NORMAL.getStatus()){
                log.error("NeptuneFriendshipService doAddFriendshipDirectly: 用户: {} 和 用户: {} 已经是好友了", friendFromId, friendToId);
                return 0;
            }
            // 2.2.3 如果好友关系没有被删除, 那么重新更新好友关系
            int result = friendshipMapper.editFriendship(appId, friendship.setUpdateTime(System.currentTimeMillis()));
            if (result <= 0){
                log.error("NeptuneFriendshipService addFriendship: 更新好友关系失败 - fromId: {}, toId: {}", friendFromId, friendToId);
            }
            return result;
        }
        // TODO 通知当前用户的其他客户端和被添加好友的所有客户端
    }

}
