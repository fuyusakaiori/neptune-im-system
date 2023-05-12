package com.fuyusakaiori.nep.im.service.core.group.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.example.nep.im.common.entity.proto.NepMessageBody;
import com.example.nep.im.common.entity.proto.message.group.NepSendGroupApplicationMessage;
import com.example.nep.im.common.enums.message.NepGroupMessageType;
import com.example.nep.im.common.enums.status.NepGroupApplicationApproveStatus;
import com.example.nep.im.common.enums.status.NepGroupEnterType;
import com.example.nep.im.common.enums.status.NepGroupJoinType;
import com.example.nep.im.common.enums.status.NepGroupMemberType;
import com.fuyusakaiori.nep.im.service.core.group.entity.NepGroup;
import com.fuyusakaiori.nep.im.service.core.group.entity.NepGroupApplication;
import com.fuyusakaiori.nep.im.service.core.group.entity.NepGroupMember;
import com.fuyusakaiori.nep.im.service.core.group.entity.dto.NepCombineGroupApplication;
import com.fuyusakaiori.nep.im.service.core.group.entity.dto.NepJoinedGroup;
import com.fuyusakaiori.nep.im.service.core.group.entity.request.NepApproveGroupApplicationRequest;
import com.fuyusakaiori.nep.im.service.core.group.entity.request.NepQueryGroupApplicationRequest;
import com.fuyusakaiori.nep.im.service.core.group.entity.request.NepSendGroupApplicationRequest;
import com.fuyusakaiori.nep.im.service.core.group.mapper.INepGroupApplicationMapper;
import com.fuyusakaiori.nep.im.service.core.group.mapper.INepGroupMapper;
import com.fuyusakaiori.nep.im.service.core.group.mapper.INepGroupMemberMapper;
import com.fuyusakaiori.nep.im.service.core.message.mq.NepServiceToGateWayMessageProducer;
import com.fuyusakaiori.nep.im.service.core.user.entity.NepUser;
import com.fuyusakaiori.nep.im.service.core.user.mapper.INepUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class NepGroupApplicationServiceImpl {

    @Autowired
    private INepUserMapper userMapper;

    @Autowired
    private INepGroupMapper groupMapper;

    @Autowired
    private INepGroupMemberMapper groupMemberMapper;

    @Autowired
    private INepGroupApplicationMapper groupApplicationMapper;

    @Autowired
    private NepGroupMemberDealService groupMemberDealService;

    @Autowired
    private NepServiceToGateWayMessageProducer messageSender;

    @Transactional
    public int doSendGroupApplication(NepSendGroupApplicationRequest request) {
        // 1. 获取变量
        Integer appId = request.getHeader().getAppId();
        Integer userId = request.getGroupApplySenderId();
        Integer groupId = request.getGroupId();
        // 2. 查询用户是否存在
        NepUser user = userMapper.queryUserById(appId, userId);
        if (Objects.isNull(user) || user.isDelete()){
            log.error("NepGroupApplicationServiceImpl doSendGroupApplication: 发送入群申请的用户不存在 - request: {}", request);
            return 0;
        }
        // 3. 查询群组是否存在
        NepGroup group = groupMapper.queryGroupById(appId, groupId);
        if (Objects.isNull(group) || group.isDelete()){
            log.error("NepGroupApplicationServiceImpl doSendGroupApplication: 接收入群申请的群组不存在 - request: {}", request);
            return 0;
        }
        // 4. 查询用户是否在群组中
        NepGroupMember groupMember = groupMemberMapper.queryGroupMember(appId, groupId, userId);
        if (Objects.nonNull(groupMember)
                    && Objects.isNull(groupMember.getGroupMemberExitType())
                    && Objects.isNull(groupMember.getGroupMemberExitTime())){
            log.error("NepGroupApplicationServiceImpl doSendGroupApplication: 用户已经在群组中, 不可以继续发送入群申请 - request: {}", request);
            return 0;
        }
        // 5. 查询此前是否发送过入群申请
        NepGroupApplication groupApplication = groupApplicationMapper.queryGroupApplicationByUserAndGroupId(appId, userId, groupId);
        // 6. 校验入群申请是否存在
        int isSendApplication = 0;
        if (Objects.isNull(groupApplication)){
            // 6.1 插入入群申请
            NepGroupApplication sendApplication = BeanUtil.copyProperties(request, NepGroupApplication.class)
                                                          .setCreateTime(System.currentTimeMillis()).setUpdateTime(System.currentTimeMillis());
            isSendApplication = groupApplicationMapper.sendGroupApplication(appId, sendApplication);
            if (isSendApplication <= 0){
                log.error("NepGroupApplicationServiceImpl doSendGroupApplication: 发送入群申请失败 - request: {}", request);
            }
        }else{
            // 6.2 更新入群申请
            NepGroupApplication updateApplication = BeanUtil.copyProperties(request, NepGroupApplication.class)
                                                            .setUpdateTime(System.currentTimeMillis());
            isSendApplication = groupApplicationMapper.updateGroupApplication(appId, updateApplication);
            if (isSendApplication <= 0){
                log.error("NepGroupApplicationServiceImpl doSendGroupApplication: 更新入群失败 - request: {}", request);
            }
        }
        // 7. 向所有管理员和群主发送入群申请
        List<NepGroupMember> groupAdminList = groupMemberMapper.queryAllGroupAdmin(appId, groupId);
        for (NepGroupMember admin : groupAdminList) {
            int messageType = NepGroupMessageType.GROUP_APPLICATION_SEND.getMessageType();
            NepMessageBody messageBody = BeanUtil.copyProperties(request, NepSendGroupApplicationMessage.class)
                                                 .setMessageType(messageType);
            messageSender.sendMessage(appId, admin.getGroupMemberId(), messageType, messageBody);
        }
        return isSendApplication;
    }

    @Transactional
    public int doApproveApplication(NepApproveGroupApplicationRequest request) {
        // 1. 获取变量
        Integer appId = request.getHeader().getAppId();
        Integer applyId = request.getApplyId();
        Integer userId = request.getUserId();
        Integer status = request.getStatus();
        // 2. 查询是否有入群申请
        NepGroupApplication application = groupApplicationMapper.queryGroupApplicationById(appId, applyId);
        if (Objects.isNull(application)){
            log.error("NepGroupApplicationServiceImpl doApproveApplication: 入群申请不存在 - request: {}", request);
            return 0;
        }
        Integer groupId = application.getGroupId();
        Integer senderId = application.getGroupApplySenderId();
        // 3. 查询审批用户是否存在
        NepUser user = userMapper.queryUserById(appId, userId);
        if (Objects.isNull(user) || user.isDelete()){
            log.error("NepGroupApplicationServiceImpl doApproveApplication: 审批入群申请的用户不存在 - request: {}", request);
            return 0;
        }
        // 4. 查询审批用户是否在群组中
        NepGroupMember groupMember = groupMemberMapper.queryGroupMember(appId, groupId, userId);
        if (Objects.isNull(groupMember)
                    || Objects.nonNull(groupMember.getGroupMemberExitTime())
                    || Objects.nonNull(groupMember.getGroupMemberExitType())) {
            log.error("NepGroupApplicationServiceImpl doApproveApplication: 审批入群申请的用户不在群聊中 - request: {}", request);
            return 0;
        }
        // 5. 判断审批用户是否有权限
        if (NepGroupMemberType.MEMBER.getType() == groupMember.getGroupMemberType()){
            log.error("NepGroupApplicationServiceImpl doApproveApplication: 用户没有权限审批入群申请 - request: {}", request);
            return 0;
        }
        // 6. 判断入群申请是否已经审批过
        if (application.getGroupApplyApproveStatus() != NepGroupApplicationApproveStatus.UNAPPROVED.getStatus()){
            log.error("NepGroupApplicationServiceImpl doApproveApplication: 入群申请已经被审批过, 不要重复审批 - request: {}", request);
            return 0;
        }
        // 7. 审批入群申请
        int isApproveApplication = groupApplicationMapper.approveGroupApplication(appId, applyId, userId, status, System.currentTimeMillis());
        if (isApproveApplication <= 0){
            log.error("NepGroupApplicationServiceImpl doApproveApplication: 入群申请审批失败 - request: {}", request);
            return 0;
        }
        // 8. 如果拒绝入群申请, 那么直接返回
        if (status == NepGroupApplicationApproveStatus.REJECT.getStatus()){
            return NepGroupApplicationApproveStatus.REJECT.getStatus();
        }
        // 9. 如果同意入群申请, 那么执行加群
        if (status == NepGroupApplicationApproveStatus.AGREE.getStatus()){
            NepJoinedGroup joinedGroup = groupMemberDealService.addGroupMember(appId, groupId, senderId, NepGroupEnterType.APPLY.getType());
            if (Objects.isNull(joinedGroup)){
                log.error("NepGroupApplicationServiceImpl doApproveApplication: 用户加入群聊失败 - request: {}", request);
                return 0;
            }
            return NepGroupApplicationApproveStatus.AGREE.getStatus();
        }
        return 0;
    }

    public List<NepCombineGroupApplication> doQueryGroupApplicationList(NepQueryGroupApplicationRequest request) {
        // 1. 获取变量
        Integer appId = request.getHeader().getAppId();
        Integer userId = request.getUserId();
        // 2. 判断用户是否存在
        NepUser user = userMapper.queryUserById(appId, userId);
        if (Objects.isNull(user) || user.isDelete()){
            log.error("NepGroupApplicationServiceImpl doQueryGroupApplicationList: 用户不存在 - request: {}", request);
            return Collections.emptyList();
        }
        // 3. 查询用户加入的所有群组的成员信息
        List<NepGroupMember> joinedGroupMemberList = groupMemberMapper.queryGroupMemberListByMemberId(appId, userId);
        if (CollectionUtil.isEmpty(joinedGroupMemberList)){
            log.info("NepGroupApplicationServiceImpl doQueryGroupApplicationList: 用户没有加入任何群组 - request: {}", request);
            return Collections.emptyList();
        }
        // 4. 获取用户加入的所有群组的 ID 集合
        List<Integer> groupIdList = joinedGroupMemberList.stream()
                                            .map(NepGroupMember::getGroupId).collect(Collectors.toList());
        // 5. 获取用户加入的群组的信息
        List<NepGroup> joinedGroupList = groupMapper.queryGroupByIdList(appId, groupIdList);
        if (CollectionUtil.isEmpty(joinedGroupList) || joinedGroupList.size() != groupIdList.size()){
            log.error("NepGroupApplicationServiceImpl doQueryGroupApplicationList: 用户不存在他加入过的群组中 - request: {}", request);
            return Collections.emptyList();
        }
        // 6. 获取用户在哪些群组中是管理员或者群主
        List<NepGroupMember> groupAdminList = joinedGroupMemberList.stream()
                                                      .filter(groupMember -> groupMember.getGroupMemberType() != NepGroupMemberType.MEMBER.getType())
                                                      .collect(Collectors.toList());
        // 7. 获取用户担任管理员或者群主的群组 ID 集合
        List<Integer> groupAdminIdList = groupAdminList.stream()
                                                 .map(NepGroupMember::getGroupId).collect(Collectors.toList());
        // 8. 获取用户担任管理员或者群主的群组的入群申请
        List<NepGroupApplication> applicationList = groupApplicationMapper.queryGroupApplicationListByGroupIdList(appId, groupAdminIdList);
        if (CollectionUtil.isEmpty(applicationList)){
            log.info("NepGroupApplicationServiceImpl doQueryGroupApplicationList: 没有查询到任何入群申请 - request: {}", request);
            return Collections.emptyList();
        }
        // 9. 获取发出入群申请的用户的信息
        List<Integer> userIdList = applicationList.stream()
                                           .map(NepGroupApplication::getGroupApplySenderId)
                                           .distinct()
                                           .collect(Collectors.toList());
        List<NepUser> userList = userMapper.queryUserByIdList(appId, userIdList);
        if (CollectionUtil.isEmpty(userList)){
            log.error("NepGroupApplicationServiceImpl doQueryGroupApplicationList: 发出入群申请的用户都不存在 - request: {}", request);
            return Collections.emptyList();
        }
        // 10. 拼装返回信息
        return transferGroupApplicationList(userList, joinedGroupList, applicationList);
    }

    private List<NepCombineGroupApplication> transferGroupApplicationList(List<NepUser> userList, List<NepGroup> groupList, List<NepGroupApplication> applicationList){
        List<NepCombineGroupApplication> groupApplicationList = new ArrayList<>();
        // 1. 用户集合转换为用户列表
        Map<Integer, NepUser> userMap = userList.stream().collect(Collectors.toMap(NepUser::getUserId, NepUser -> NepUser));
        // 2. 群聊集合转换为群组列表
        Map<Integer, NepGroup> groupMap = groupList.stream().collect(Collectors.toMap(NepGroup::getGroupId, NepGroup -> NepGroup));
        // 3. 封装返回结果
        for (NepGroupApplication application : applicationList) {
            NepUser user = userMap.get(application.getGroupApplySenderId());
            NepGroup group = groupMap.get(application.getGroupId());
            NepCombineGroupApplication groupApplication = generateApplication(application, user, group);
            groupApplicationList.add(groupApplication);
        }
        return groupApplicationList;
    }

    private NepCombineGroupApplication generateApplication(NepGroupApplication application, NepUser user, NepGroup group) {
        return new NepCombineGroupApplication().setGroupApplicationId(application.getGroupApplyId())
                       .setGroupApplicationApproveStatus(application.getGroupApplyApproveStatus())
                       .setGroupApplicationAdditionalInfo(application.getGroupApplyAdditionalInfo())
                       .setGroupApplicationSource(application.getGroupApplySource())
                       .setSenderId(application.getGroupApplySenderId())
                       .setSenderUserName(user.getUsername())
                       .setSenderNickName(user.getNickname())
                       .setSenderAvatarAddress(user.getAvatarAddress())
                       .setGroupId(application.getGroupId())
                       .setGroupNumber(group.getGroupNumber())
                       .setGroupName(group.getGroupName());
    }
}
