package com.fuyusakaiori.nep.im.service.core.friendship.service.impl;

import com.example.neptune.im.common.entity.request.NepRequestHeader;
import com.example.neptune.im.common.enums.code.NepFriendshipBlackResponseCode;
import com.example.neptune.im.common.enums.code.NepFriendshipResponseCode;
import com.example.neptune.im.common.enums.status.NepFriendshipBlackCheckType;
import com.example.neptune.im.common.enums.status.NepFriendshipBlackStatus;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.NepFriendship;
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


    public int doAddFriendInBlackList(NepRequestHeader header, int friendFromId, int friendToId){
        // 1. 查询好友关系
        NepFriendship friendship = friendshipMapper.queryFriendshipById(header.getAppId(), friendFromId, friendToId);
        // 2. 校验好友关系是否存在
        if (Objects.isNull(friendship)){
            log.error("NepFriendshipBlackService doAddFriendInBlackList: 好友关系不存在 - fromId: {}, toId: {}", friendFromId, friendToId);
            return 0;
        }
        // 3. 校验是否已经被拉黑
        int blackStatus = friendshipBlackMapper.checkFriendInBlackList(header.getAppId(), friendFromId, friendToId);
        if (NepFriendshipBlackStatus.BLACK.getStatus() == blackStatus){
            log.error("NepFriendshipBlackService doAddFriendInBlackList: 好友已经被拉黑, 不要重复拉黑 - fromId: {}, toId: {}", friendFromId, friendToId);
            return 0;
        }
        // 6. 拉黑好友
        return friendshipBlackMapper.addFriendInBlackList(header.getAppId(), friendFromId, friendToId, System.currentTimeMillis());
    }

    public int doRemoveFriendInBlackList(NepRequestHeader header, int friendFromId, int friendToId){
        // 1. 查询好友关系
        NepFriendship friendship = friendshipMapper.queryFriendshipById(header.getAppId(), friendFromId, friendToId);
        // 2. 校验好友关系是否存在
        if (Objects.isNull(friendship)){
            log.error("NepFriendshipBlackService doRemoveFriendInBlackList: 好友关系不存在 - fromId: {}, toId: {}", friendFromId, friendToId);
            return 0;
        }
        // 3. 校验是否已经被拉黑
        int blackStatus = friendshipBlackMapper.checkFriendInBlackList(header.getAppId(), friendFromId, friendToId);
        if (NepFriendshipBlackStatus.WHITE.getStatus() == blackStatus){
            log.error("NepFriendshipBlackService doRemoveFriendInBlackList: 好友没有被拉黑, 请不要撤销未拉黑的用户 - fromId: {}, toId: {}", friendFromId, friendToId);
            return 0;
        }
        // 4. 撤销好友拉黑
        return friendshipBlackMapper.removeFriendInBlackList(header.getAppId(), friendFromId, friendToId, System.currentTimeMillis());
    }

    public int checkFriendInBlackList(NepRequestHeader header, int friendFromId, int friendToId, int checkType){
        // 1. 查询好友关系
        NepFriendship friendship = friendshipMapper.queryFriendshipById(header.getAppId(), friendFromId, friendToId);
        // 2. 校验好友关系是否存在
        if (Objects.isNull(friendship)){
            log.error("NepFriendshipBlackService checkFriendInBlackList: 好友关系不存在 - fromId: {}, toId: {}", friendFromId, friendToId);
        }
        // 3. 校验拉黑状态
        if (NepFriendshipBlackCheckType.SINGLE.getType() == checkType){
            return friendshipBlackMapper.checkFriendInBlackList(header.getAppId(), friendFromId, friendToId);
        }else{
            return friendshipBlackMapper.checkBiFriendInBlackList(header.getAppId(), friendFromId, friendToId);
        }
    }

}
