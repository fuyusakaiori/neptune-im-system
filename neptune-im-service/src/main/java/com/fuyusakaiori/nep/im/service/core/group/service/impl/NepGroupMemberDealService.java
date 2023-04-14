package com.fuyusakaiori.nep.im.service.core.group.service.impl;

import com.example.nep.im.common.enums.status.NepGroupMemberType;
import com.fuyusakaiori.nep.im.service.core.group.entity.NepGroupMember;
import com.fuyusakaiori.nep.im.service.core.group.entity.dto.NepJoinedGroup;
import com.fuyusakaiori.nep.im.service.core.group.mapper.INepGroupMemberMapper;
import com.fuyusakaiori.nep.im.service.core.user.entity.NepUser;
import com.fuyusakaiori.nep.im.service.core.user.mapper.INepUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class NepGroupMemberDealService {

    @Autowired
    private INepUserMapper userMapper;

    @Autowired
    private INepGroupMemberMapper groupMemberMapper;


    public NepJoinedGroup addGroupMember(int appId, int groupId, int groupMemberId, int groupMemberEnterType){
        // 1. 查询群成员是否存在
        NepUser user = userMapper.queryUserById(appId, groupMemberId);
        if (Objects.isNull(user) || user.isDelete()){
            log.error("NepGroupMemberDealService addGroupMember: 想要加入群聊的用户不存在 - appId: {}, groupId: {}, groupMemberId: {}", appId, groupId, groupMemberId);
            return null;
        }
        // 2. 检查群成员是否在群中
        NepGroupMember groupMember = groupMemberMapper.queryGroupMember(appId, groupId, groupMemberId);
        if(Objects.nonNull(groupMember) &&
                   groupMember.getGroupMemberExitType() == null || groupMember.getGroupMemberExitTime() == null){
            log.error("NepGroupMemberDealService addGroupMember: 想要加入群聊的用户已经在群聊中 - appId: {}, groupId: {}, groupMemberId: {}", appId, groupId, groupMemberId);
            return null;
        }
        // 3. 加入群聊
        int isAdd = groupMemberMapper.addGroupMember(appId, getGroupMemberList(groupId, groupMemberId, groupMemberEnterType));
        if (isAdd <= 0){
            log.error("NepGroupMemberDealService addGroupMember: 用户加入群聊失败 - appId: {}, groupId: {}, groupMemberId: {}", appId, groupId, groupMemberId);
            return null;
        }
        // 4. 拼装返回信息
        return new NepJoinedGroup();
    }

    private List<NepGroupMember> getGroupMemberList(int groupId, int groupMemberId, int groupMemberEnterType) {
        return Collections.singletonList(new NepGroupMember()
                                                 .setGroupId(groupId).setGroupMemberId(groupMemberId)
                                                 .setGroupMemberType(NepGroupMemberType.MEMBER.getType())
                                                 .setGroupMemberEnterType(groupMemberEnterType)
                                                 .setGroupMemberEnterTime(System.currentTimeMillis()));
    }

}
