package com.fuyusakaiori.nep.im.service.core.group.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.example.nep.im.common.entity.proto.NepMessageBody;
import com.example.nep.im.common.entity.proto.message.group.NepAddGroupMemberMessage;
import com.example.nep.im.common.enums.message.NepGroupMessageType;
import com.example.nep.im.common.enums.status.NepGroupEnterType;
import com.example.nep.im.common.enums.status.NepGroupMemberType;
import com.fuyusakaiori.nep.im.service.core.group.entity.NepGroup;
import com.fuyusakaiori.nep.im.service.core.group.entity.NepGroupMember;
import com.fuyusakaiori.nep.im.service.core.group.entity.dto.NepJoinedGroup;
import com.fuyusakaiori.nep.im.service.core.group.mapper.INepGroupMapper;
import com.fuyusakaiori.nep.im.service.core.group.mapper.INepGroupMemberMapper;
import com.fuyusakaiori.nep.im.service.core.message.mq.NepServiceToGateWayMessageProducer;
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
    private INepGroupMapper groupMapper;

    @Autowired
    private INepGroupMemberMapper groupMemberMapper;

    @Autowired
    private NepServiceToGateWayMessageProducer messageSender;


    public NepJoinedGroup addGroupMember(int appId, int groupId, int groupMemberId, int groupMemberEnterType){
        // 1. 查询群组是否存在
        NepGroup group = groupMapper.queryGroupById(appId, groupId);
        if (Objects.isNull(group) || group.isDelete()){
            log.error("NepGroupMemberDealService addGroupMember: 群组不存在 - appId: {}, groupId: {}, groupMemberId: {}", appId, groupId, groupMemberId);
            return null;
        }
        // 2. 判断成员是否已经在群聊中
        NepGroupMember groupMember = groupMemberMapper.queryGroupMember(appId, groupId, groupMemberId);
        if (Objects.nonNull(groupMember)
                    && Objects.isNull(groupMember.getGroupMemberEnterType())
                    && Objects.isNull(groupMember.getGroupMemberExitTime())){
            log.error("NepGroupMemberServiceImpl doAddGroupMember: 用户已经在群聊中, 不可以重复加入群聊 - appId: {}, groupId: {}, groupMemberId: {}", appId, groupId, groupMemberId);
            return null;
        }
        // 3. 加入群聊
        if (Objects.isNull(groupMember)){
            // 3.1 如果用户此前没有加入过, 那么插入新的记录
            int isAdd = groupMemberMapper.addGroupMember(appId, getGroupMemberList(groupId, groupMemberId, groupMemberEnterType));
            if (isAdd <= 0){
                log.error("NepGroupMemberDealService addGroupMember: 用户加入群聊失败 - appId: {}, groupId: {}, groupMemberId: {}", appId, groupId, groupMemberId);
                return null;
            }
        }else{
            // 3.2 如果此前用户加入过, 那么更新记录
            int isUpdate = groupMemberMapper.rejoinGroupMember(appId, groupId, groupMemberId, groupMemberEnterType, System.currentTimeMillis());
            if (isUpdate <= 0){
                log.error("NepGroupMemberDealService addGroupMember: 用户重新加入群聊失败 - appId: {}, groupId: {}, groupMemberId: {}", appId, groupId, groupMemberId);
                return null;
            }
        }
        // 3. 消息通知
        if (groupMemberEnterType == NepGroupEnterType.APPLY.getType()){
            int messageType = NepGroupMessageType.GROUP_MEMBER_ADD.getMessageType();
            NepMessageBody messageBody = BeanUtil.copyProperties(group, NepAddGroupMemberMessage.class)
                                                 .setGroupMemberType(NepGroupMemberType.MEMBER.getType()).setMessageType(messageType);
            messageSender.sendMessage(appId, groupMemberId, messageType, messageBody);
        }
        // 4. 拼装返回信息
        return BeanUtil.copyProperties(group, NepJoinedGroup.class)
                       .setGroupMemberType(NepGroupMemberType.MEMBER.getType());
    }

    private List<NepGroupMember> getGroupMemberList(int groupId, int groupMemberId, int groupMemberEnterType) {
        return Collections.singletonList(new NepGroupMember()
                                                 .setGroupId(groupId).setGroupMemberId(groupMemberId)
                                                 .setGroupMemberType(NepGroupMemberType.MEMBER.getType())
                                                 .setGroupMemberEnterType(groupMemberEnterType)
                                                 .setGroupMemberEnterTime(System.currentTimeMillis()));
    }

}
