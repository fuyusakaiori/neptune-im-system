package com.fuyusakaiori.nep.im.service.core.group.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.example.nep.im.common.constant.NepRedisConstant;
import com.example.nep.im.common.enums.status.*;
import com.fuyusakaiori.nep.im.service.core.group.entity.NepGroup;
import com.fuyusakaiori.nep.im.service.core.group.entity.NepGroupMember;
import com.fuyusakaiori.nep.im.service.core.group.entity.dto.NepCombineGroup;
import com.fuyusakaiori.nep.im.service.core.group.entity.dto.NepSimpleGroupMember;
import com.fuyusakaiori.nep.im.service.core.group.entity.dto.NepJoinedGroup;
import com.fuyusakaiori.nep.im.service.core.group.entity.dto.NepSimpleGroup;
import com.fuyusakaiori.nep.im.service.core.group.entity.request.*;
import com.fuyusakaiori.nep.im.service.core.group.mapper.INepGroupMapper;
import com.fuyusakaiori.nep.im.service.core.group.mapper.INepGroupMemberMapper;
import com.fuyusakaiori.nep.im.service.core.user.entity.NepUser;
import com.fuyusakaiori.nep.im.service.core.user.mapper.INepUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
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

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    @Transactional
    public NepGroup doCreateGroup(NepCreateGroupRequest request) {
        // 0. 获取变量
        Integer appId = request.getHeader().getAppId();
        Integer groupOwnerId = request.getGroupOwnerId();
        List<Integer> groupMemberIdList = request.getGroupMemberIdList();
        // 1. 查询用户
        NepUser user = userMapper.queryUserById(appId, groupOwnerId);
        // 2. 校验用户是否存在
        if (Objects.isNull(user) || user.isDelete()){
            log.error("NepGroupServiceImpl doCreateGroup: 创建群组的用户不存在 - request: {}", request);
            return null;
        }
        // 3. 给群聊分配群号
        String groupNumber = String.valueOf(
                redisTemplate.opsForValue().increment(NepRedisConstant.GROUP_NUMBER));
        // 4. 查询该群聊
        NepGroup group = groupMapper.queryGroupByNumber(appId, groupNumber);
        // 5. 校验群聊是否存在
        if (Objects.nonNull(group) && !group.isDelete()){
            log.error("NepGroupServiceImpl doCreateGroup: 创建的群组被分配的群号发生重复 - request: {}", request);
            return null;
        }
        // 6. 创建群聊
        int isCreateGroup = groupMapper.createGroup(appId,
                BeanUtil.copyProperties(request, NepGroup.class)
                        .setGroupNumber(groupNumber).setCreateTime(System.currentTimeMillis()).setUpdateTime(System.currentTimeMillis()));
        if (isCreateGroup <= 0){
            log.error("NepGroupServiceImpl doCreateGroup: 创建群组的过程中发生异常 - request: {}", request);
            return null;
        }
        // 7. 将新建的群聊查询出来
        NepGroup newGroup = groupMapper.queryGroupByNumber(appId, groupNumber);
        // 8. 将创建群聊的用户设置为群主
        int isAddLeader = groupMemberMapper.addGroupMember(appId,
                Collections.singletonList(getNepGroupMember(
                        groupOwnerId, NepGroupMemberType.LEADER.getType(), NepGroupEnterType.DEFAULT.getType(), newGroup)));
        if(isAddLeader <= 0){
            log.error("NepGroupServiceImpl doCreateGroup: 向群组中添加群主失败 - request: {}", request);
            return null;
        }
        // 9. 如果请求中有携带成员 ID, 那么执行成员的添加
        if (CollectionUtil.isNotEmpty(groupMemberIdList)){
            List<NepGroupMember> groupMemberList = groupMemberIdList.stream().map(groupMemberId -> getNepGroupMember(
                    groupMemberId, NepGroupMemberType.MEMBER.getType(), NepGroupEnterType.INVITE.getType(), newGroup)).collect(Collectors.toList());
            int isAddGroupMember = groupMemberMapper.addGroupMember(appId, groupMemberList);
            if (isAddGroupMember <= 0){
                log.error("NepGroupServiceImpl doCreateGroup: 向群组中添加群聊成员失败 - request: {}", request);
                return null;
            }
        }
        return newGroup;
    }

    @Transactional
    public int doEditGroupInfo(NepEditGroupRequest request) {
        // 0. 获取变量
        Integer appId = request.getHeader().getAppId();
        Integer userId = request.getUserId();
        Integer groupId = request.getGroupId();
        Integer groupMemberType = request.getGroupMemberType();
        // 1. 查询群组
        NepGroup group = groupMapper.queryGroupById(appId, groupId);
        // 2. 校验群组是否存在
        if (Objects.isNull(group) || group.isDelete()){
            log.error("NepGroupServiceImpl doEditGroupInfo: 需要更新信息的群组不存在 - request: {}", request);
            return 0;
        }
        // 3. 查询用户
        NepUser user = userMapper.queryUserById(appId, userId);
        // 4. 校验用户是否存在
        if (Objects.isNull(user) || user.isDelete()){
            log.error("NepGroupServiceImpl doEditGroupInfo: 发出更新群组信息的用户不存在 - request: {}", request);
            return 0;
        }
        // 5. 校验用户权限
        NepGroupMember groupMember = groupMemberMapper.queryGroupMember(appId, groupId, userId);
        if(!groupMember.getGroupMemberType().equals(groupMemberType)){
            log.error("NepGroupServiceImpl doEditGroupInfo: 更新群组信息的用户权限和查询得到的用户权限不一致 - request: {}", request);
            return 0;
        }
        if (groupMember.getGroupMemberType() == NepGroupMemberType.MEMBER.getType()){
            log.error("NepGroupServiceImpl doEditGroupInfo: 用户没有权限更新群组信息 - request: {}", request);
            return 0;
        }
        // 6. 直接更新群组
        int isUpdate = groupMapper.updateGroupInfo(appId,
                BeanUtil.copyProperties(request, NepGroup.class));
        if (isUpdate <= 0){
            log.error("NepGroupServiceImpl doEditGroupInfo: 更新群组信息失败 - request: {}", request);
        }
        return isUpdate;
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
        Integer groupMemberType = request.getGroupMemberType();
        // 1. 查询群组
        NepGroup group = groupMapper.queryGroupById(appId, groupId);
        // 2. 校验分组是否存在
        if (Objects.isNull(group) || group.isDelete()){
            log.error("NepGroupServiceImpl doDissolveGroup: 需要解散的群组是不存在的 - request: {}", request);
            return 0;
        }
        // 3. 查询用户
        NepUser user = userMapper.queryUserById(appId, userId);
        // 4. 校验用户是否存在
        if (Objects.isNull(user) || user.isDelete()){
            log.error("NepGroupServiceImpl doDissolveGroup: 发出解散群组的用户是不存在的 - request: {}", request);
            return 0;
        }
        // 5. 查询用户的权限
        NepGroupMember groupMember = groupMemberMapper.queryGroupMember(appId, groupId, userId);
        // 6. 校验权限是否为管理员或者群主
        if (!groupMember.getGroupMemberType().equals(groupMemberType)){
            log.error("NepGroupServiceImpl doDissolveGroup: 解散群聊的用户权限和查询得到的用户权限不一致 - request: {}", request);
            return 0;
        }
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
        Integer groupMemberType = request.getGroupMemberType();
        Boolean mute = request.getMute();
        // 1. 查询用户
        NepUser user = userMapper.queryUserById(appId, userId);
        // 2. 校验用户是否存在
        if (Objects.isNull(user) || user.isDelete()){
            log.error("NepGroupServiceImpl doMuteGroupChat: 发出禁言群组的用户是不存在的 - request: {}", request);
            return 0;
        }
        // 3. 查询用户的权限
        NepGroupMember groupMember = groupMemberMapper.queryGroupMember(appId, groupId, userId);
        // 4. 校验权限是否为管理员或者群主
        if (!groupMember.getGroupMemberType().equals(groupMemberType)){
            log.error("NepGroupServiceImpl doMuteGroupChat: 禁言群聊的用户权限和查询得到的用户权限不一致 - request: {}", request);
            return 0;
        }
        if (groupMember.getGroupMemberType() == NepGroupMemberType.MEMBER.getType()){
            log.error("NepGroupServiceImpl doMuteGroupChat: 非群主和管理员是不可以禁言群组的 - request: {}", request);
            return 0;
        }
        // 5. 查询分组是否存在
        NepGroup group = groupMapper.queryGroupById(appId, groupId);
        // 6. 校验分组是否存在
        if (Objects.isNull(group) || group.isDelete()){
            log.error("NepGroupServiceImpl doMuteGroupChat: 需要解散的群组是不存在的 - request: {}", request);
            return 0;
        }
        // 7. 禁言群组
        int isMute = groupMapper.muteGroupChat(appId, groupId, mute, System.currentTimeMillis());
        if (isMute <= 0){
            log.error("NepGroupServiceImpl doMuteGroupChat: 禁言群组失败 - request: {}", request);
        }
        return isMute;
    }

    @Transactional
    public int doTransferGroupOwner(NepTransferGroupOwnerRequest request) {
        // 0. 获取变量
        Integer appId = request.getHeader().getAppId();
        Integer userId = request.getUserId();
        Integer groupId = request.getGroupId();
        Integer targetOwnerId = request.getTargetOwnerId();
        Integer groupMemberType = request.getGroupMemberType();
        // 1. 查询用户
        NepUser user = userMapper.queryUserById(appId, userId);
        // 2. 校验用户是否存在
        if (Objects.isNull(user) || user.isDelete()){
            log.error("NepGroupServiceImpl doTransferGroupOwner: 发出转让群组的请求是不存在的 - request: {}", request);
            return 0;
        }
        // 3. 查询分组是否存在
        NepGroup group = groupMapper.queryGroupById(appId, groupId);
        // 4. 校验分组是否存在
        if (Objects.isNull(group) || group.isDelete()){
            log.error("NepGroupServiceImpl doTransferGroupOwner: 需要转让的群组是不存在的 - request: {}", request);
            return 0;
        }
        // 5. 查询用户的权限
        NepGroupMember groupMember = groupMemberMapper.queryGroupMember(appId, groupId, userId);
        // 6. 校验权限是否为管理员或者群主
        if (!groupMember.getGroupMemberType().equals(groupMemberType)){
            log.error("NepGroupServiceImpl doTransferGroupOwner: 发起群主转让的用户权限和查询得到的用户权限不一致 - request: {}", request);
            return 0;
        }
        if (groupMember.getGroupMemberType() != NepGroupMemberType.LEADER.getType()){
            log.error("NepGroupServiceImpl doTransferGroupOwner: 群主之外都是不可以转让群的 - request: {}", request);
            return 0;
        }
        // 7. 转让群组
        int isTransfer = groupMapper.transferGroupOwner(appId, groupId, targetOwnerId, System.currentTimeMillis());
        if (isTransfer <= 0){
            log.error("NepGroupServiceImpl doTransferGroupOwner: 转让群组失败 - request: {}", request);
            return isTransfer;
        }
        // 8. 变更群聊成员的类型: 群主变普通成员, 普通成员变群主
        int isChangeOwner = groupMemberMapper.changeGroupMemberType(appId, groupId, userId, NepGroupMemberType.MEMBER.getType());
        if (isChangeOwner <= 0){
            log.error("NepGroupServiceImpl doTransferGroupOwner: 将群主变为普通群聊成员失败 - request: {}", request);
            return isChangeOwner;
        }
        int isChangeMember = groupMemberMapper.changeGroupMemberType(appId, groupId, targetOwnerId, NepGroupMemberType.LEADER.getType());
        if (isChangeMember <= 0){
            log.error("NepGroupServiceImpl doTransferGroupOwner: 将普通群聊成员变为群主是失败 - request: {}", request);
        }
        return isChangeMember;
    }

    public NepCombineGroup doQueryGroup(NepQueryGroupRequest request) {
        // 0. 获取变量
        Integer appId = request.getHeader().getAppId();
        Integer groupId = request.getGroupId();
        // 1. 查询群组的详细信息
        NepGroup group = groupMapper.queryGroupById(appId, groupId);
        // 2. 校验群聊是否存在
        if (Objects.isNull(group) || group.isDelete()){
            log.error("NepGroupServiceImpl doQueryGroup: 需要查询详细信息的群聊是不存在的 - request: {}", request);
            return null;
        }
        // 3. 查询群聊中的管理员 ID
        List<Integer> groupAdminIdList = groupMemberMapper.queryAllGroupAdmin(appId, groupId).stream()
                                                 .map(NepGroupMember::getGroupMemberId)
                                                 .collect(Collectors.toList());
        // 4. 校验管理员 ID 集合是否为空
        if (CollectionUtil.isEmpty(groupAdminIdList)){
            log.error("NepGroupServiceImpl doQueryGroup: 群聊中没有设置任何管理员 - request: {}", request);
            return null;
        }
        // 5. 查询所有管理员的详细信息
        List<NepUser> adminList = userMapper.queryUserByIdList(appId, groupAdminIdList);
        if (CollectionUtil.isEmpty(adminList)){
            log.error("NepGroupServiceImpl doQueryGroup: 没有查询到群聊管理员的详细信息 - request: {}", request);
            return null;
        }
        // 6. 拼装成返回信息
        List<NepSimpleGroupMember> groupAdminList = adminList.stream().map(this::getSimpleGroupMember).collect(Collectors.toList());
        // 7. 查询群聊中的成员数量
        int groupMemberCount = groupMemberMapper.queryGroupMemberCount(appId, groupId);
        if(groupMemberCount <= 0){
            log.error("NepGroupServiceImpl doQueryGroup: 群聊中的成员数量不合法 - request: {}", request);
            return null;
        }
        // 8. 拼装返回信息
        return BeanUtil.copyProperties(group, NepCombineGroup.class)
                       .setGroupAdminList(groupAdminList).setGroupMemberCount(groupMemberCount);
    }

    public List<NepSimpleGroup> doQueryGroupList(NepQueryGroupListRequest request) {
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
        // 4. 取出查询结果中的部分信息返回
        return BeanUtil.copyToList(groupList.stream().collect(Collectors.toMap(NepGroup::getGroupId, NepGroup -> NepGroup)).values(), NepSimpleGroup.class);
    }

    public List<NepJoinedGroup> doQueryAllJoinedGroup(NepQueryAllJoinedGroupRequest request) {
        // 0. 获取变量
        Integer appId = request.getHeader().getAppId();
        Integer userId = request.getUserId();
        // 1. 查询用户
        NepUser user = userMapper.queryUserById(appId, userId);
        if (Objects.isNull(user) || user.isDelete()){
            log.error("NepGroupServiceImpl doQueryAllJoinedGroup: 用户不存在 - request: {}", request);
            return Collections.emptyList();
        }
        // 2. 查询加入的群组
        List<NepGroupMember> groupMemberList = groupMemberMapper.queryGroupMemberListByMemberId(appId, userId);
        // 3. 校验集合是否为空
        if (CollectionUtil.isEmpty(groupMemberList)){
            log.info("NepGroupServiceImpl doQueryAllJoinedGroup: 该用户没有加入任何群聊 - request: {}", request);
            return Collections.emptyList();
        }
        // 4. 查询加入的群聊的信息
        List<NepGroup> groupList = groupMapper.queryGroupByIdList(appId,
                groupMemberList.stream().map(NepGroupMember::getGroupId).collect(Collectors.toList()));
        if (CollectionUtil.isEmpty(groupList) || groupList.size() != groupMemberList.size()){
            log.error("NepGroupServiceImpl doQueryAllJoinedGroup: 用户加入的群聊不存在 - request: {}", request);
            return Collections.emptyList();
        }
        // 5. 拼装返回信息: 因为每个用户在群聊中是有相应的权限的, 所以查询得到的群聊信息最好提前就查出来用户在群聊中的权限, 否则进入群聊设置页面的时候就不好控制
        Map<Integer, NepJoinedGroup> joinedGroupMap = groupList.stream()
                                                       .map(group -> BeanUtil.copyProperties(group, NepJoinedGroup.class))
                                                       .collect(Collectors.toMap(NepJoinedGroup::getGroupId, NepJoinedGroup -> NepJoinedGroup));
        groupMemberList.forEach(groupMember -> joinedGroupMap.get(groupMember.getGroupId())
                                                       .setGroupMemberType(groupMember.getGroupMemberType())
                                                       .setGroupMemberNickName(groupMember.getGroupMemberNickName()));
        // 6. 返回相应的结果
        return new ArrayList<>(joinedGroupMap.values());
    }

    private NepGroupMember getNepGroupMember(int groupMemberId, int groupMemberType, int groupJoinType, NepGroup newGroup) {
        return new NepGroupMember()
                       .setGroupId(newGroup.getGroupId())
                       .setGroupMemberId(groupMemberId)
                       .setGroupMemberType(groupMemberType)
                       .setGroupMemberEnterType(groupJoinType).setGroupMemberEnterTime(System.currentTimeMillis());

    }

    private NepSimpleGroupMember getSimpleGroupMember(NepUser user){
        return new NepSimpleGroupMember()
                       .setGroupMemberId(user.getUserId())
                       .setGroupMemberUserName(user.getUsername())
                       .setGroupMemberNickName(user.getNickname())
                       .setGroupMemberAvatarAddress(user.getAvatarAddress());
    }
}
