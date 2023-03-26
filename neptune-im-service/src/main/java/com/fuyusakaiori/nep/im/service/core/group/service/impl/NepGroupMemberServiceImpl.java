package com.fuyusakaiori.nep.im.service.core.group.service.impl;

import com.example.nep.im.common.enums.status.NepGroupExitType;
import com.example.nep.im.common.enums.status.NepGroupJoinType;
import com.example.nep.im.common.enums.status.NepGroupMemberType;
import com.fuyusakaiori.nep.im.service.core.group.entity.NepGroup;
import com.fuyusakaiori.nep.im.service.core.group.entity.NepGroupMember;
import com.fuyusakaiori.nep.im.service.core.group.entity.dto.NepGroupMemberUser;
import com.fuyusakaiori.nep.im.service.core.group.entity.request.*;
import com.fuyusakaiori.nep.im.service.core.group.mapper.INepGroupMapper;
import com.fuyusakaiori.nep.im.service.core.group.mapper.INepGroupMemberMapper;
import com.fuyusakaiori.nep.im.service.core.user.entity.NepUser;
import com.fuyusakaiori.nep.im.service.core.user.mapper.INepUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

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


    public int doAddGroupMember(NepAddGroupMemberRequest request) {
        // 0. 获取变量
        Integer appId = request.getHeader().getAppId();
        Integer groupId = request.getGroupId();
        Integer groupMemberId = request.getGroupMemberId();
        Integer groupMemberEnterType = request.getGroupMemberEnterType();
        // 1. 判断群聊是否存在
        NepGroup group = groupMapper.queryGroupById(appId, groupId);
        if (Objects.isNull(group) || group.isDelete()){
            log.error("NepGroupMemberServiceImpl doAddGroupMember: 群聊不存在 - request: {}", request);
            return 0;
        }
        // 2. 判断群聊入群类型
        if(group.getGroupApplyType() == NepGroupJoinType.BAN.getType()){
            log.info("NepGroupMemberServiceImpl doAddGroupMember: 群聊禁止加入新的群成员 - request: {}", request);
            return NepGroupJoinType.BAN.getType();
        }else if (group.getGroupApplyType() == NepGroupJoinType.ANY.getType()){
            int isAdd = groupMemberDealService.addGroupMember(appId, groupId, groupMemberId, groupMemberEnterType);
            return isAdd <= 0 ? isAdd : NepGroupJoinType.ANY.getType();
        }else if (group.getGroupApplyType() == NepGroupJoinType.VALIDATION.getType()){
            // TODO
            return NepGroupJoinType.VALIDATION.getType();
        }
        return 0;
    }

    public int doUpdateGroupMember(NepUpdateGroupMemberRequest request) {
        // 0. 获取变量
        Integer appId = request.getHeader().getAppId();
        Integer groupId = request.getGroupId();
        Integer groupMemberId = request.getGroupMemberId();
        String nickname = request.getNickname();
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
            log.error("NepGroupMemberServiceImpl doUpdateGroupMember: 授予权限的群成员不在群聊中或者已经退出群聊 - request: {}", request);
            return 0;
        }
        // 4. 校验用户是否可以授予权限
        NepGroupMember leaderOrAdmin = groupMemberMapper.queryGroupMember(appId, groupId, userId);
        if (Objects.isNull(leaderOrAdmin) ||
                    leaderOrAdmin.getGroupMemberExitType() != null || leaderOrAdmin.getGroupMemberExitTime() != null){
            log.error("NepGroupMemberServiceImpl doUpdateGroupMember: 踢出群成员的用户不存在 - request: {}", request);
            return 0;
        }
        if (leaderOrAdmin.getGroupMemberType() == NepGroupMemberType.MEMBER.getType()){
            log.error("NepGroupMemberServiceImpl doUpdateGroupMember: 普通成员不可以给其他成员授予权限 - request: {}", request);
            return 0;
        }
        // 5. 给群成员授予权限
        int isChange = groupMemberMapper.changeGroupMemberType(appId, groupId, groupMemberId, groupMemberType);
        if (isChange <= 0){
            log.error("NepGroupMemberServiceImpl doUpdateGroupMember: 给群成员授予权限失败 - request: {}", request);
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
        Long muteEndTime = request.getMuteEndTime();
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
        if (leaderOrAdmin.getGroupMemberType() == NepGroupMemberType.MEMBER.getType()){
            log.error("NepGroupMemberServiceImpl doMuteGroupMemberChat: 普通成员不可以禁言其他群成员 - request: {}", request);
            return 0;
        }
        // 5. 禁言群成员
        int isMute = groupMemberMapper.muteGroupMemberChat(appId, groupId, groupMemberId, muteEndTime);
        if (isMute <= 0){
            log.error("NepGroupMemberServiceImpl doMuteGroupMemberChat: 禁言群成员失败 - request: {}", request);
        }
        return isMute;
    }

    public int doRevokeGroupMemberChat(NepRevokeGroupMemberRequest request) {
        // 0. 获取变量
        Integer appId = request.getHeader().getAppId();
        Integer userId = request.getUserId();
        Integer groupId = request.getGroupId();
        Integer groupMemberId = request.getGroupMemberId();
        // 1. 查询群聊是否存在
        NepGroup group = groupMapper.queryGroupById(appId, groupId);
        if (Objects.isNull(group) || group.isDelete()){
            log.error("NepGroupMemberServiceImpl doRevokeGroupMemberChat: 群聊不存在 - request: {}", request);
            return 0;
        }
        // 2. 查询群成员是否存在
        NepUser user = userMapper.queryUserById(appId, groupMemberId);
        if (Objects.isNull(user) || user.isDelete()){
            log.error("NepGroupMemberServiceImpl doRevokeGroupMemberChat: 撤销禁言的成员不存在 - request: {}", request);
            return 0;
        }
        // 3. 检查群成员是否在群中
        NepGroupMember groupMember = groupMemberMapper.queryGroupMember(appId, groupId, groupMemberId);
        if(Objects.isNull(groupMember) ||
                   groupMember.getGroupMemberExitType() != null || groupMember.getGroupMemberExitTime() != null){
            log.error("NepGroupMemberServiceImpl doRevokeGroupMemberChat: 撤销禁言的群成员不在群聊中或者已经退出群聊 - request: {}", request);
            return 0;
        }
        // 4. 校验用户是否可以授予权限
        NepGroupMember leaderOrAdmin = groupMemberMapper.queryGroupMember(appId, groupId, userId);
        if (Objects.isNull(leaderOrAdmin) ||
                    leaderOrAdmin.getGroupMemberExitType() != null || leaderOrAdmin.getGroupMemberExitTime() != null){
            log.error("NepGroupMemberServiceImpl doRevokeGroupMemberChat: 发起撤销禁言的成员不存在 - request: {}", request);
            return 0;
        }
        if (leaderOrAdmin.getGroupMemberType() == NepGroupMemberType.MEMBER.getType()){
            log.error("NepGroupMemberServiceImpl doRevokeGroupMemberChat: 普通成员不可以撤销其他群成员的禁言 - request: {}", request);
            return 0;
        }
        // 5. 禁言群成员
        int isRevoke = groupMemberMapper.revokeGroupMemberChat(appId, groupId, groupMemberId);
        if (isRevoke <= 0){
            log.error("NepGroupMemberServiceImpl doRevokeGroupMemberChat: 撤销群成员的禁言失败 - request: {}", request);
        }
        return isRevoke;
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
        return 0;
    }

    public List<NepGroupMemberUser> doQueryAllGroupMember(NepQueryAllGroupMemberRequest request) {
        return null;
    }
}
