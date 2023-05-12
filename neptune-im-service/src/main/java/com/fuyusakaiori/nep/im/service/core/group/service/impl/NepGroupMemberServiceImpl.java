package com.fuyusakaiori.nep.im.service.core.group.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.BooleanUtil;
import com.example.nep.im.common.enums.status.NepGroupAllowType;
import com.example.nep.im.common.enums.status.NepGroupExitType;
import com.example.nep.im.common.enums.status.NepGroupJoinType;
import com.example.nep.im.common.enums.status.NepGroupMemberType;
import com.fuyusakaiori.nep.im.service.core.group.entity.NepGroup;
import com.fuyusakaiori.nep.im.service.core.group.entity.NepGroupMember;
import com.fuyusakaiori.nep.im.service.core.group.entity.dto.NepJoinedGroup;
import com.fuyusakaiori.nep.im.service.core.group.entity.dto.NepSimpleGroupMember;
import com.fuyusakaiori.nep.im.service.core.group.entity.request.*;
import com.fuyusakaiori.nep.im.service.core.group.mapper.INepGroupMapper;
import com.fuyusakaiori.nep.im.service.core.group.mapper.INepGroupMemberMapper;
import com.fuyusakaiori.nep.im.service.core.user.entity.NepUser;
import com.fuyusakaiori.nep.im.service.core.user.mapper.INepUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class NepGroupMemberServiceImpl {

    @Autowired
    private INepUserMapper userMapper;

    @Autowired
    private INepGroupMapper groupMapper;

    @Autowired
    private INepGroupMemberMapper groupMemberMapper;

    @Autowired
    private NepGroupMemberDealService groupMemberDealService;


    public Map<String, Object> doAddGroupMember(NepAddGroupMemberRequest request) {
        // 0. 获取变量
        Integer appId = request.getHeader().getAppId();
        Integer groupId = request.getGroupId();
        Integer groupMemberId = request.getGroupMemberId();
        Integer groupMemberEnterType = request.getGroupMemberEnterType();
        // 1. 判断群聊是否存在
        NepGroup group = groupMapper.queryGroupById(appId, groupId);
        if (Objects.isNull(group) || group.isDelete()){
            log.error("NepGroupMemberServiceImpl doAddGroupMember: 群聊不存在 - request: {}", request);
            return Collections.emptyMap();
        }
        // 2. 获取群聊的加入方式
        Integer groupApplyType = group.getGroupApplyType();
        // 3. 群聊禁止加入
        if (groupApplyType == NepGroupAllowType.BAN.getType()){
            log.info("NepGroupMemberServiceImpl doAddGroupMember: 群聊禁止任何人加入 - request: {}", request);
            return new HashMap<String, Object>(){{put("groupAllowType", NepGroupAllowType.BAN.getType());}};
        }
        // 4. 群聊允许验证后加入
        if(groupApplyType == NepGroupAllowType.VALIDATION.getType()){
            log.info("NepGroupMemberServiceImpl doAddGroupMember: 用户需要验证后加入群聊, 需要发送入群申请 - request: {}", request);
            return new HashMap<String, Object>(){{put("groupAllowType", NepGroupAllowType.VALIDATION.getType());}};
        }
        // 5. 群聊允许任何人加入
        if (groupApplyType == NepGroupAllowType.ANY.getType()){
            log.info("NepGroupMemberServiceImpl doAddGroupMember: 用户可以直接加入群聊 - request: {}", request);
            NepJoinedGroup joinedGroup = groupMemberDealService.addGroupMember(appId, groupId, groupMemberId, groupMemberEnterType);
            if (Objects.isNull(joinedGroup)){
                log.error("NepGroupMemberServiceImpl doAddGroupMember: 加入群聊失败 - request: {}", request);
                return Collections.emptyMap();
            }
            return new HashMap<String, Object>(){{put("groupAllowType", NepGroupAllowType.ANY.getType()); put("joinedGroup", joinedGroup);}};
        }
        return Collections.emptyMap();
    }

    public int doUpdateGroupMember(NepUpdateGroupMemberRequest request) {
        // 0. 获取变量
        Integer appId = request.getHeader().getAppId();
        Integer groupId = request.getGroupId();
        Integer groupMemberId = request.getGroupMemberId();
        String nickname = request.getGroupMemberNickName();
        // 1. 查询群聊是否存在
        NepGroup group = groupMapper.queryGroupById(appId, groupId);
        if (Objects.isNull(group) || group.isDelete()){
            log.error("NepGroupMemberServiceImpl doUpdateGroupMember: 群聊不存在 - request: {}", request);
            return 0;
        }
        // 2. 查询群成员是否存在
        NepUser user = userMapper.queryUserById(appId, groupMemberId);
        if (Objects.isNull(user) || user.isDelete()){
            log.error("NepGroupMemberServiceImpl doUpdateGroupMember: 更新资料的群成员不存在 - request: {}", request);
            return 0;
        }
        // 3. 检查群成员是否在群中
        NepGroupMember groupMember = groupMemberMapper.queryGroupMember(appId, groupId, groupMemberId);
        if(Objects.isNull(groupMember) ||
                   groupMember.getGroupMemberExitType() != null || groupMember.getGroupMemberExitTime() != null){
            log.error("NepGroupMemberServiceImpl doUpdateGroupMember: 更新资料的群成员不在群聊中或者已经退出群聊 - request: {}", request);
            return 0;
        }
        // 4. 更新群成员的资料: 任何人都可以更新
        int isUpdate = groupMemberMapper.updateGroupMemberInfo(appId, groupId, groupMemberId, nickname);
        if (isUpdate <= 0){
            log.error("NepGroupMemberServiceImpl doUpdateGroupMember: 更新群成员的资料失败 - request: {}", request);
            return isUpdate;
        }
        return isUpdate;
    }

    public int doChangeGroupMemberType(NepChangeGroupMemberTypeRequest request) {
        // 0. 获取变量
        Integer appId = request.getHeader().getAppId();
        Integer userId = request.getUserId();
        Integer groupId = request.getGroupId();
        Integer groupMemberId = request.getGroupMemberId();
        Integer groupMemberType = request.getGroupMemberType();
        Integer groupOperatorType = request.getGroupOperatorType();
        // 1. 查询群聊是否存在
        NepGroup group = groupMapper.queryGroupById(appId, groupId);
        if (Objects.isNull(group) || group.isDelete()){
            log.error("NepGroupMemberServiceImpl doChangeGroupMemberType: 群聊不存在 - request: {}", request);
            return 0;
        }
        // 2. 查询群成员是否存在
        NepUser user = userMapper.queryUserById(appId, groupMemberId);
        if (Objects.isNull(user) || user.isDelete()){
            log.error("NepGroupMemberServiceImpl doChangeGroupMemberType: 授予权限的群成员不存在 - request: {}", request);
            return 0;
        }
        // 3. 检查群成员是否在群中
        NepGroupMember groupMember = groupMemberMapper.queryGroupMember(appId, groupId, groupMemberId);
        if(Objects.isNull(groupMember) ||
                   groupMember.getGroupMemberExitType() != null || groupMember.getGroupMemberExitTime() != null){
            log.error("NepGroupMemberServiceImpl doChangeGroupMemberType: 授予权限的群成员不在群聊中或者已经退出群聊 - request: {}", request);
            return 0;
        }
        // 4. 校验用户是否可以授予权限
        NepGroupMember leaderOrAdmin = groupMemberMapper.queryGroupMember(appId, groupId, userId);
        if (Objects.isNull(leaderOrAdmin) ||
                    leaderOrAdmin.getGroupMemberExitType() != null || leaderOrAdmin.getGroupMemberExitTime() != null){
            log.error("NepGroupMemberServiceImpl doChangeGroupMemberType: 设置管理员的用户不存在或者已经退出群聊 - request: {}", request);
            return 0;
        }
        if (!leaderOrAdmin.getGroupMemberType().equals(groupOperatorType)){
            log.error("NepGroupMemberServiceImpl doChangeGroupMemberType: 设置管理员的用户的权限和查询得到的用户权限不一致 - request: {}", request);
            return 0;
        }
        if (leaderOrAdmin.getGroupMemberType() == NepGroupMemberType.MEMBER.getType()){
            log.error("NepGroupMemberServiceImpl doChangeGroupMemberType: 普通成员不可以给其他成员授予权限 - request: {}", request);
            return 0;
        }
        // 5. 给群成员授予权限
        int isChange = groupMemberMapper.changeGroupMemberType(appId, groupId, groupMemberId, groupMemberType);
        if (isChange <= 0){
            log.error("NepGroupMemberServiceImpl doChangeGroupMemberType: 给群成员授予权限失败 - request: {}", request);
            return isChange;
        }
        return isChange;
    }

    public int doMuteGroupMemberChat(NepMuteGroupMemberRequest request) {
        // 0. 获取变量
        Integer appId = request.getHeader().getAppId();
        Integer userId = request.getUserId();
        Integer groupId = request.getGroupId();
        Integer groupMemberId = request.getGroupMemberId();
        Integer groupMemberType = request.getGroupMemberType();
        Long muteEndTime = request.getMuteEndTime();
        boolean isMute = BooleanUtil.isTrue(request.getMute());
        // 1. 查询群聊是否存在
        NepGroup group = groupMapper.queryGroupById(appId, groupId);
        if (Objects.isNull(group) || group.isDelete()){
            log.error("NepGroupMemberServiceImpl doMuteGroupMemberChat: 群聊不存在 - request: {}", request);
            return 0;
        }
        // 2. 查询群成员是否存在
        NepUser user = userMapper.queryUserById(appId, groupMemberId);
        if (Objects.isNull(user) || user.isDelete()){
            log.error("NepGroupMemberServiceImpl doMuteGroupMemberChat: 禁言的成员不存在 - request: {}", request);
            return 0;
        }
        // 3. 检查群成员是否在群中
        NepGroupMember groupMember = groupMemberMapper.queryGroupMember(appId, groupId, groupMemberId);
        if(Objects.isNull(groupMember) ||
                   groupMember.getGroupMemberExitType() != null || groupMember.getGroupMemberExitTime() != null){
            log.error("NepGroupMemberServiceImpl doMuteGroupMemberChat: 禁言的群成员不在群聊中或者已经退出群聊 - request: {}", request);
            return 0;
        }
        // 4. 校验用户是否可以授予权限
        NepGroupMember leaderOrAdmin = groupMemberMapper.queryGroupMember(appId, groupId, userId);
        if (Objects.isNull(leaderOrAdmin) ||
                    leaderOrAdmin.getGroupMemberExitType() != null || leaderOrAdmin.getGroupMemberExitTime() != null){
            log.error("NepGroupMemberServiceImpl doMuteGroupMemberChat: 发起禁言的成员不存在 - request: {}", request);
            return 0;
        }
        if (!leaderOrAdmin.getGroupMemberType().equals(groupMemberType)){
            log.error("NepGroupMemberServiceImpl doMuteGroupMemberChat: 发起禁言的成员的权限和查询得到的权限不一致 - request: {}", request);
            return 0;
        }
        if (leaderOrAdmin.getGroupMemberType() == NepGroupMemberType.MEMBER.getType()){
            log.error("NepGroupMemberServiceImpl doMuteGroupMemberChat: 普通成员不可以禁言其他群成员 - request: {}", request);
            return 0;
        }
        // 5. 禁言或者撤销禁言
        if (isMute){
            int isMuteChat = groupMemberMapper.muteGroupMemberChat(appId, groupId, groupMemberId, muteEndTime);
            if (isMuteChat <= 0){
                log.error("NepGroupMemberServiceImpl doMuteGroupMemberChat: 禁言群成员失败 - request: {}", request);
            }
            return isMuteChat;
        }else{
            int isRevokeChat = groupMemberMapper.revokeGroupMemberChat(appId, groupId, groupMemberId);
            if (isRevokeChat <= 0){
                log.error("NepGroupMemberServiceImpl doMuteGroupMemberChat: 撤销群成员的禁言失败 - request: {}", request);
            }
            return isRevokeChat;
        }
    }

    public int doExitGroupMember(NepExitGroupMemberRequest request) {
        // 0. 获取变量
        Integer appId = request.getHeader().getAppId();
        Integer userId = request.getUserId();
        Integer groupId = request.getGroupId();
        Integer groupMemberId = request.getGroupMemberId();
        Integer groupMemberExitType = request.getGroupMemberExitType();
        // 1. 查询群聊是否存在
        NepGroup group = groupMapper.queryGroupById(appId, groupId);
        if (Objects.isNull(group) || group.isDelete()){
            log.error("NepGroupMemberServiceImpl doExitGroupMember: 群聊不存在 - request: {}", request);
            return 0;
        }
        // 2. 查询群成员是否存在
        NepUser user = userMapper.queryUserById(appId, groupMemberId);
        if (Objects.isNull(user) || user.isDelete()){
            log.error("NepGroupMemberServiceImpl doExitGroupMember: 用户不存在 - request: {}", request);
            return 0;
        }
        // 3. 检查群成员是否在群中
        NepGroupMember groupMember = groupMemberMapper.queryGroupMember(appId, groupId, groupMemberId);
        if(Objects.isNull(groupMember) ||
                   groupMember.getGroupMemberExitType() != null || groupMember.getGroupMemberExitTime() != null){
            log.error("NepGroupMemberServiceImpl doExitGroupMember: 群成员不在群聊中或者已经退出群聊 - request: {}", request);
            return 0;
        }
        // 4. 如果是被踢出群聊的, 那么需要检查权限
        if (groupMemberExitType == NepGroupExitType.KICK_OUT.getType()){
            NepGroupMember leaderOrAdmin = groupMemberMapper.queryGroupMember(appId, groupId, userId);
            if (Objects.isNull(leaderOrAdmin) ||
                        leaderOrAdmin.getGroupMemberExitType() != null || leaderOrAdmin.getGroupMemberExitTime() != null){
                log.error("NepGroupMemberServiceImpl doExitGroupMember: 踢出群成员的用户不存在 - request: {}", request);
                return 0;
            }
            if (leaderOrAdmin.getGroupMemberType() == NepGroupMemberType.MEMBER.getType()){
                log.error("NepGroupMemberServiceImpl doExitGroupMember: 普通成员不可以踢出其他成员 - request: {}", request);
                return 0;
            }
        }
        // 5. 退出群聊
        int isExit = groupMemberMapper.exitGroupMember(appId, groupId, groupMemberId,
                groupMemberExitType, System.currentTimeMillis());
        if (isExit <= 0){
            log.error("NepGroupMemberServiceImpl doExitGroupMember: 退出群聊失败 - request: {}", request);
            return 0;
        }
        return isExit;
    }

    public Map<Integer, List<NepSimpleGroupMember>> doQueryAllGroupMember(NepQueryAllGroupMemberRequest request) {
        // 0. 获取变量
        Integer appId = request.getHeader().getAppId();
        Integer groupId = request.getGroupId();
        // 1. 查询群聊是否存在
        NepGroup group = groupMapper.queryGroupById(appId, groupId);
        // 2. 校验群聊是否存在
        if (Objects.isNull(group) || group.isDelete()){
            log.error("NepGroupMemberServiceImpl doQueryAllGroupMember: 需要查询所有群聊成员的群聊是不存在的 - request: {}", request);
            return Collections.emptyMap();
        }
        // 3. 查询群聊中的所有成员
        List<NepGroupMember> groupMemberList = groupMemberMapper.queryAllGroupMember(appId, groupId);
        // 4. 校验群聊成员是否为空
        if (CollectionUtil.isEmpty(groupMemberList)){
            log.error("NepGroupMemberServiceImpl doQueryAllGroupMember: 群聊中不存在任何成员 - request: {}", request);
            return Collections.emptyMap();
        }
        // 5. 查询群聊成员的详细信息
        List<NepUser> userList = userMapper.queryUserByIdList(appId,
                groupMemberList.stream().map(NepGroupMember::getGroupMemberId).collect(Collectors.toList()));
        // 6. 校验用户信息是否为空
        if (CollectionUtil.isEmpty(userList) || userList.size() != groupMemberList.size()){
            log.error("NepGroupMemberServiceImpl doQueryAllGroupMember: 没有查询到群聊成员的用户信息 - request: {}", request);
            return Collections.emptyMap();
        }
        Map<Integer, NepUser> userMap = userList.stream().collect(Collectors.toMap(NepUser::getUserId, NepUser -> NepUser));
        // 7. 拼装为返回信息: 2 - 表示群主, 1 - 表示管理员, 0 - 表示普通群成员
        Map<Integer, List<NepSimpleGroupMember>> groupMemberMap = new HashMap<>();
        for (NepGroupMember groupMember : groupMemberList) {
            // 7.1 获取每种成员类型的链表
            List<NepSimpleGroupMember> groupSimpleMemberList = groupMemberMap.getOrDefault(groupMember.getGroupMemberType(), new ArrayList<>());
            // 7.2 添加成员
            groupSimpleMemberList.add(getNepSimpleGroupMember(groupMember, userMap.get(groupMember.getGroupMemberId())));
            // 7.3 重新更新哈希表
            groupMemberMap.put(groupMember.getGroupMemberType(), groupSimpleMemberList);
        }
        return groupMemberMap;
    }

    private  NepSimpleGroupMember getNepSimpleGroupMember(NepGroupMember groupMember, NepUser user) {
        return new NepSimpleGroupMember()
                       .setGroupMemberUserName(user.getUsername())
                       .setGroupMemberNickName(user.getNickname())
                       .setGroupMemberAvatarAddress(user.getAvatarAddress())
                       .setGroupMemberId(groupMember.getGroupMemberId())
                       .setGroupMemberRemark(groupMember.getGroupMemberNickName())
                       .setGroupMemberType(groupMember.getGroupMemberType())
                       .setGroupMemberMuteEndTime(groupMember.getGroupMemberMuteEndTime());
    }
}
