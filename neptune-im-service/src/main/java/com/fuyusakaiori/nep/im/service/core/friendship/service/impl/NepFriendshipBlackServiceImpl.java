package com.fuyusakaiori.nep.im.service.core.friendship.service.impl;

import com.example.nep.im.common.entity.request.NepRequestHeader;
import com.example.nep.im.common.enums.status.NepFriendshipBlackCheckType;
import com.example.nep.im.common.enums.status.NepFriendshipBlackStatus;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.NepFriendship;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.black.NepAddFriendshipBlackRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.black.NepRemoveFriendshipBlackRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.mapper.INepFriendshipBlackMapper;
import com.fuyusakaiori.nep.im.service.core.friendship.mapper.INepFriendshipMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class NepFriendshipBlackServiceImpl {

    @Autowired
    private INepFriendshipMapper friendshipMapper;

    @Autowired
    private INepFriendshipBlackMapper friendshipBlackMapper;


    public int doAddFriendInBlackList(NepAddFriendshipBlackRequest request){
        // 0. 获取变量
        NepRequestHeader header = request.getHeader();
        Integer friendFromId = request.getFriendFromId();
        Integer friendToId = request.getFriendToId();
        // 1. 查询好友关系
        NepFriendship friendship = friendshipMapper.queryFriendshipById(header.getAppId(), friendFromId, friendToId);
        // 2. 校验好友关系是否存在
        if (Objects.isNull(friendship)){
            log.error("NepFriendshipBlackServiceImpl doAddFriendInBlackList: 好友关系不存在 - fromId: {}, toId: {}", friendFromId, friendToId);
            return 0;
        }
        // 3. 校验是否已经被拉黑
        int blackStatus = friendshipBlackMapper.checkFriendInBlackList(header.getAppId(), friendFromId, friendToId);
        if (NepFriendshipBlackStatus.BLACK.getStatus() == blackStatus){
            log.error("NepFriendshipBlackServiceImpl doAddFriendInBlackList: 好友已经被拉黑, 不要重复拉黑 - fromId: {}, toId: {}", friendFromId, friendToId);
            return 0;
        }
        // 6. 拉黑好友
        return friendshipBlackMapper.addFriendInBlackList(header.getAppId(), friendFromId, friendToId, System.currentTimeMillis());
    }

    public int doRemoveFriendInBlackList(NepRemoveFriendshipBlackRequest request){
        // 0. 获取变量
        NepRequestHeader header = request.getHeader();
        Integer friendFromId = request.getFriendFromId();
        Integer friendToId = request.getFriendToId();
        // 1. 查询好友关系
        NepFriendship friendship = friendshipMapper.queryFriendshipById(header.getAppId(), friendFromId, friendToId);
        // 2. 校验好友关系是否存在
        if (Objects.isNull(friendship)){
            log.error("NepFriendshipBlackServiceImpl doRemoveFriendInBlackList: 好友关系不存在 - fromId: {}, toId: {}", friendFromId, friendToId);
            return 0;
        }
        // 3. 校验是否已经被拉黑
        int blackStatus = friendshipBlackMapper.checkFriendInBlackList(header.getAppId(), friendFromId, friendToId);
        if (NepFriendshipBlackStatus.WHITE.getStatus() == blackStatus){
            log.error("NepFriendshipBlackServiceImpl doRemoveFriendInBlackList: 好友没有被拉黑, 请不要撤销未拉黑的用户 - fromId: {}, toId: {}", friendFromId, friendToId);
            return 0;
        }
        // 4. 撤销好友拉黑
        return friendshipBlackMapper.removeFriendInBlackList(header.getAppId(), friendFromId, friendToId, System.currentTimeMillis());
    }

}
