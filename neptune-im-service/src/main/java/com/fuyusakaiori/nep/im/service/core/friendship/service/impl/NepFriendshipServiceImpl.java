package com.fuyusakaiori.nep.im.service.core.friendship.service.impl;

import com.example.neptune.im.common.entity.request.NepRequestHeader;
import com.example.neptune.im.common.enums.code.NepBaseResponseCode;
import com.example.neptune.im.common.enums.code.NepFriendshipResponseCode;
import com.example.neptune.im.common.enums.status.NepFriendshipStatus;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.NepFriendship;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.dto.NepAddFriendship;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.dto.NepEditFriendship;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.normal.NepModifyFriendshipResponse;
import com.fuyusakaiori.nep.im.service.core.friendship.mapper.INepFriendshipMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@Service
public class NepFriendshipServiceImpl {

    @Autowired
    private INepFriendshipMapper friendshipMapper;

    @Transactional(rollbackFor = Exception.class)
    public NepModifyFriendshipResponse addFriendship(NepRequestHeader header, NepAddFriendship body){
        NepModifyFriendshipResponse response = new NepModifyFriendshipResponse();
        // 0. 获取变量
        Integer friendFromId = body.getFriendFromId();
        Integer friendToId = body.getFriendToId();
        String remark = body.getFriendRemark();
        String source = body.getFriendshipSource();
        String additionalInfo = body.getAdditionalInfo();
        String extra = body.getFriendshipExtra();
        // 1. 查询两个用户之间的关系
        NepFriendship friendshipFrom = friendshipMapper.queryFriendshipById(header.getAppId(), friendFromId, friendToId);
        // 2. 判断两个用户此前是否添加过好友: 理论上两个关系只能同时存在或者同时不存在, 不可能出现只有一条关系的情况
        if (Objects.isNull(friendshipFrom)){
            // 2.1 如果没有添加过好友, 那么需要插入新的记录: 数据库中需要插入两条数据 -> 直接 SQL 中插入两条
            int result = friendshipMapper.addFriendship(header.getAppId(), body,
                    System.currentTimeMillis(), System.currentTimeMillis());
            if (result <= 0){
                log.error("NeptuneFriendshipService addFriendship: 添加好友关系失败 - fromId: {}, toId: {}", friendFromId, friendToId);
                return response.setCode(NepFriendshipResponseCode.FRIENDSHIP_ADD_FAIL.getCode())
                               .setMessage(NepFriendshipResponseCode.FRIENDSHIP_ADD_FAIL.getMessage());
            }
        }else{
            // 2.2.1 如果已经添加过好友, 那么检查好友关系是否已经被删除:
            if (friendshipFrom.getFriendshipStatus() == NepFriendshipStatus.FRIENDSHIP_NORMAL.getStatus()){
                log.error("NeptuneFriendshipService addFriendship: 用户: {} 和 用户: {} 已经是好友了", friendFromId, friendToId);
                return response.setCode(NepFriendshipResponseCode.FRIENDSHIP_EXIST.getCode())
                               .setMessage(NepFriendshipResponseCode.FRIENDSHIP_EXIST.getMessage());
            }
            // 2.2.2: 需要重新更新好友关系的状态、备注、来源、附加信息、拓展字段
            NepEditFriendship friendship = transferToEditFriendship(friendFromId, friendToId, remark, source, additionalInfo, extra);
            // 2.2.3 如果好友关系没有被删除, 那么重新更新好友关系
            int result = friendshipMapper.editFriendship(header.getAppId(), friendship, System.currentTimeMillis());
            if (result <= 0){
                log.error("NeptuneFriendshipService addFriendship: 更新好友关系失败 - fromId: {}, toId: {}", friendFromId, friendToId);
                return response.setCode(NepFriendshipResponseCode.FRIENDSHIP_UPDATE_FAIL.getCode())
                               .setMessage(NepFriendshipResponseCode.FRIENDSHIP_UPDATE_FAIL.getMessage());
            }
        }
        return response.setCode(NepBaseResponseCode.SUCCESS.getCode())
                       .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
    }

    private NepEditFriendship transferToEditFriendship(Integer friendFromId, Integer friendToId, String remark, String source, String additionalInfo, String extra) {
        return new NepEditFriendship()
                       .setFriendFromId(friendFromId)
                       .setFriendToId(friendToId)
                       .setFriendshipStatus(NepFriendshipStatus.FRIENDSHIP_NORMAL.getStatus())
                       .setFriendRemark(remark)
                       .setFriendshipSource(source)
                       .setAdditionalInfo(additionalInfo)
                       .setFriendshipExtra(extra);
    }

}