package com.fuyusakaiori.nep.im.service.core.group.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.example.nep.im.common.entity.request.NepRequestHeader;
import com.example.nep.im.common.enums.status.NepGroupEnterType;
import com.example.nep.im.common.enums.status.NepGroupExitType;
import com.example.nep.im.common.enums.status.NepGroupMemberType;
import com.example.nep.im.common.enums.status.NepGroupType;
import com.fuyusakaiori.nep.im.service.core.group.entity.NepGroup;
import com.fuyusakaiori.nep.im.service.core.group.entity.NepGroupMember;
import com.fuyusakaiori.nep.im.service.core.group.entity.request.*;
import com.fuyusakaiori.nep.im.service.core.group.mapper.INepGroupMapper;
import com.fuyusakaiori.nep.im.service.core.group.mapper.INepGroupMemberMapper;
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
public class NepGroupServiceImpl {

    @Autowired
    private INepUserMapper userMapper;

    @Autowired
    private INepGroupMapper groupMapper;

    @Autowired
    private INepGroupMemberMapper groupMemberMapper;


    @Transactional
    public int doCreateGroup(NepCreateGroupRequest request) {
        // 0. 获取变量
        Integer appId = request.getHeader().getAppId();
        String groupNumber = request.getGroupNumber();
        Integer groupOwnerId = request.getGroupOwnerId();
        // 1. 查询用户
        NepUser user = userMapper.queryUserById(appId, groupOwnerId);
        // 2. 校验用户是否存在
        if (Objects.isNull(user)){
            log.error("NepGroupServiceImpl doCreateGroup: 创建群组的用户不存在 - request: {}", request);
            return 0;
        }
        // 3. 查询分组
        NepGroup group = groupMapper.queryGroupByNumber(appId, groupNumber);
        // 4. 校验分组是否存在
        if (Objects.nonNull(group)){
            log.error("NepGroupServiceImpl doCreateGroup: 创建的群组被分配的群号发生重复 - request: {}", request);
            return 0;
        }
        // 5. 创建分组
        int isCreateGroup = groupMapper.createGroup(appId, BeanUtil.copyProperties(request, NepGroup.class, "header"));
        if (isCreateGroup <= 0){
            log.error("NepGroupServiceImpl doCreateGroup: 创建群组的过程中发生异常");
            return isCreateGroup;
        }
        // 6. 将创建群聊的用户设置为群主
        int isAddLeader = groupMemberMapper.addGroupMember(appId,
                Collections.singletonList(getNepGroupMember(appId, groupNumber)));
        if(isAddLeader <= 0){
            log.error("NepGroupServiceImpl doCreateGroup: 向群组中添加群主失败");
        }
        return isAddLeader;
    }

    private NepGroupMember getNepGroupMember(Integer appId, String groupNumber) {
        NepGroup newGroup = groupMapper.queryGroupByNumber(appId, groupNumber);
        return new NepGroupMember()
                       .setGroupMemberEnterTime(System.currentTimeMillis())
                       .setGroupId(newGroup.getGroupId()).setGroupMemberId(newGroup.getGroupOwnerId())
                       .setGroupMemberType(NepGroupMemberType.LEADER.getType()).setGroupMemberEnterType(NepGroupEnterType.APPLY.getType());
    }

    @Transactional
    public int doEditGroupInfo(NepEditGroupRequest request) {
        // 0. 获取变量
        Integer appId = request.getHeader().getAppId();
        Integer userId = request.getUserId();
        Integer groupId = request.getGroupId();
        // 1. 查询群组
        NepGroup group = groupMapper.queryGroupById(appId, groupId);
        // 2. 校验群组是否存在
        if (Objects.isNull(group)){
            log.error("NepGroupServiceImpl doEditGroupInfo: 需要更新信息的群组不存在 - request: {}", request);
            return 0;
        }
        // 3. 查询用户
        NepUser user = userMapper.queryUserById(appId, userId);
        // 4. 校验用户是否存在
        if (Objects.isNull(user)){
            log.error("NepGroupServiceImpl doEditGroupInfo: 发出更新群组信息的用户不存在 - request: {}", request);
            return 0;
        }
        // 5. 校验用户权限
        NepGroupMember groupMember = groupMemberMapper.queryGroupMember(appId, groupId, userId);
        if (groupMember.getGroupMemberType() == NepGroupMemberType.MEMBER.getType()){
            log.error("NepGroupServiceImpl doEditGroupInfo: 用户没有权限更新群组信息 - request: {}", request);
            return 0;
        }
        // 6. 直接更新群组
        int result = groupMapper.updateGroupInfo(appId, BeanUtil.copyProperties(request, NepGroup.class, "header"));
        if (result <= 0){
            log.error("NepGroupServiceImpl doEditGroupInfo: 更新群组信息失败 - request: {}", request);
        }
        return result;
    }

    @Transactional
    public int doUpdateGroupAvatar(NepUploadGroupAvatarRequest request) {
        return 0;
    }

    @Transactional
    public int doDissolveGroup(NepDissolveGroupRequest request) {
        // 0. 获取变量
        Integer appId = request.getHeader().getAppId();
        Integer groupId = request.getGroupId();
        Integer userId = request.getUserId();
        // 1. 查询群组
        NepGroup group = groupMapper.queryGroupById(appId, groupId);
        // 2. 校验分组是否存在
        if (Objects.isNull(group)){
            log.error("NepGroupServiceImpl doDissolveGroup: 需要解散的群组是不存在的 - request: {}", request);
            return 0;
        }
        // 3. 查询用户
        NepUser user = userMapper.queryUserById(appId, userId);
        // 4. 校验用户是否存在
        if (Objects.isNull(user)){
            log.error("NepGroupServiceImpl doDissolveGroup: 发出解散群组的用户是不存在的 - request: {}", request);
            return 0;
        }
        // 5. 查询用户的权限
        NepGroupMember groupMember = groupMemberMapper.queryGroupMember(appId, groupId, userId);
        // 6. 校验权限是否为管理员或者群主
        if (groupMember.getGroupMemberType() != NepGroupMemberType.LEADER.getType()){
            log.error("NepGroupServiceImpl doDissolveGroup: 非群主是不可以解散群组的 - request: {}", request);
            return 0;
        }
        // 7. 解散群组
        int isDissolve = groupMapper.dissolveGroup(appId, groupId, System.currentTimeMillis());
        // 8. 校验是否解散成功
        if (isDissolve <= 0){
            log.error("NepGroupServiceImpl doDissolveGroup: 解散群组失败 - request: {}", request);
            return isDissolve;
        }
        // 9. 解散群组中的所有成员
        int isClear = groupMemberMapper.clearGroupMember(appId, groupId, NepGroupExitType.DISSOLVE.getType(), System.currentTimeMillis());
        // 10. 校验是否成功清空
        if (isClear <= 0){
            log.error("NepGroupServiceImpl doDissolveGroup: 清空群组中的所有成员失败 - request: {}", request);
        }
        return isClear;
    }

    @Transactional
    public int doMuteGroupChat(NepMuteGroupRequest request) {
        // 0. 获取变量
        Integer appId = request.getHeader().getAppId();
        Integer userId = request.getUserId();
        Integer groupId = request.getGroupId();
        // 1. 查询用户
        NepUser user = userMapper.queryUserById(appId, userId);
        // 2. 校验用户是否存在
        if (Objects.isNull(user)){
            log.error("NepGroupServiceImpl doMuteGroupChat: 发出禁言群组的用户是不存在的 - request: {}", request);
            return 0;
        }
        // 3. 查询用户的权限
        NepGroupMember groupMember = groupMemberMapper.queryGroupMember(appId, groupId, userId);
        // 4. 校验权限是否为管理员或者群主
        if (groupMember.getGroupMemberType() == NepGroupMemberType.MEMBER.getType()){
            log.error("NepGroupServiceImpl doMuteGroupChat: 非群主和管理员是不可以禁言群组的 - request: {}", request);
            return 0;
        }
        // 5. 查询分组是否存在
        NepGroup group = groupMapper.queryGroupById(appId, groupId);
        // 6. 校验分组是否存在
        if (Objects.isNull(group)){
            log.error("NepGroupServiceImpl doMuteGroupChat: 需要解散的群组是不存在的 - request: {}", request);
            return 0;
        }
        // 7. 禁言群组
        int result = groupMapper.muteGroupChat(appId, groupId, System.currentTimeMillis());
        if (result <= 0){
            log.error("NepGroupServiceImpl doMuteGroupChat: 禁言群组失败 - request: {}", request);
        }
        return result;
    }

    @Transactional
    public int doTransferGroupOwner(NepTransferGroupOwnerRequest request) {
        // 0. 获取变量
        Integer appId = request.getHeader().getAppId();
        Integer userId = request.getUserId();
        Integer groupId = request.getGroupId();
        Integer targetOwnerId = request.getTargetOwnerId();
        // 1. 查询用户
        NepUser user = userMapper.queryUserById(appId, userId);
        // 2. 校验用户是否存在
        if (Objects.isNull(user)){
            log.error("NepGroupServiceImpl doTransferGroupOwner: 发出转让群组的请求是不存在的 - request: {}", request);
            return 0;
        }
        // 3. 查询用户的权限
        NepGroupMember groupMember = groupMemberMapper.queryGroupMember(appId, groupId, userId);
        // 4. 校验权限是否为管理员或者群主
        if (groupMember.getGroupMemberType() != NepGroupMemberType.LEADER.getType()){
            log.error("NepGroupServiceImpl doTransferGroupOwner: 群主之外都是不可以转让群的 - request: {}", request);
            return 0;
        }
        // 5. 查询分组是否存在
        NepGroup group = groupMapper.queryGroupById(appId, groupId);
        // 6. 校验分组是否存在
        if (Objects.isNull(group)){
            log.error("NepGroupServiceImpl doTransferGroupOwner: 需要转让的群组是不存在的 - request: {}", request);
            return 0;
        }
        // 7. 转让群组
        int result = groupMapper.transferGroupOwner(appId, groupId, targetOwnerId, System.currentTimeMillis());
        if (result <= 0){
            log.error("NepGroupServiceImpl doTransferGroupOwner: 转让群组失败 - request: {}", request);
        }
        return result;
    }

    public List<NepGroup> doQueryGroup(NepQueryGroupRequest request) {
        // 0. 获取变量
        Integer appId = request.getHeader().getAppId();
        String groupName = request.getGroupName();
        String groupNumber = request.getGroupNumber();
        List<NepGroup> groupList = new ArrayList<>();
        // 1. 根据群号查询群组
        NepGroup groupByNumber = groupMapper.queryGroupByNumber(appId, groupNumber);
        if (Objects.nonNull(groupByNumber)){
            groupList.add(groupByNumber);
        }
        // 2. 根据群组名称查询群组
        List<NepGroup> groupListByName = groupMapper.queryGroupByName(appId, groupName);
        if (CollectionUtil.isNotEmpty(groupListByName)){
            groupList.addAll(groupListByName);
        }
        // 3. 合并查询结果
        if (CollectionUtil.isEmpty(groupList)){
            return Collections.emptyList();
        }
        return new ArrayList<>(groupList.stream().collect(Collectors.toMap(NepGroup::getGroupId, NepGroup -> NepGroup)).values());
    }

    public List<NepGroup> doQueryAllJoinedGroup(NepQueryAllJoinedGroupRequest request) {
        // 0. 获取变量
        Integer appId = request.getHeader().getAppId();
        Integer userId = request.getUserId();
        // 1. 查询用户
        NepUser user = userMapper.queryUserById(appId, userId);
        if (Objects.isNull(user)){
            log.error("NepGroupServiceImpl doQueryAllJoinedGroup: 用户不存在 - request: {}", request);
            return Collections.emptyList();
        }
        // 2. 查询加入的群组
        List<NepGroup> groupList = groupMapper.queryAllJoinedGroup(appId, userId);
        if (CollectionUtil.isEmpty(groupList)){
            return Collections.emptyList();
        }
        return groupList;
    }

}
