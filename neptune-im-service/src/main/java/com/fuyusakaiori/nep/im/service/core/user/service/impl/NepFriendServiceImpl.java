package com.fuyusakaiori.nep.im.service.core.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.example.nep.im.common.entity.request.NepRequestHeader;
import com.example.nep.im.common.enums.code.*;
import com.example.nep.im.common.enums.status.NepFriendshipStatus;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.NepFriendship;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.NepFriendshipApplication;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.NepFriendshipGroup;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.NepFriendshipGroupMember;
import com.fuyusakaiori.nep.im.service.core.friendship.mapper.*;
import com.fuyusakaiori.nep.im.service.core.user.entity.NepUser;
import com.fuyusakaiori.nep.im.service.core.user.entity.dto.NepFriend;
import com.fuyusakaiori.nep.im.service.core.user.entity.dto.NepFriendApplication;
import com.fuyusakaiori.nep.im.service.core.user.entity.dto.NepFriendGroup;
import com.fuyusakaiori.nep.im.service.core.user.entity.request.friend.NepQueryAllFriendApplicationRequest;
import com.fuyusakaiori.nep.im.service.core.user.entity.request.friend.NepQueryAllFriendGroupMemberRequest;
import com.fuyusakaiori.nep.im.service.core.user.entity.request.friend.NepQueryAllFriendRequest;
import com.fuyusakaiori.nep.im.service.core.user.entity.request.friend.NepQueryFriendRequest;
import com.fuyusakaiori.nep.im.service.core.user.mapper.INepUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Slf4j
@Service
public class NepFriendServiceImpl {

    @Autowired
    private INepUserMapper userMapper;

    @Autowired
    private INepFriendshipMapper friendshipMapper;

    @Autowired
    private INepFriendshipApplicationMapper friendshipApplicationMapper;

    @Autowired
    private INepFriendshipGroupMapper friendshipGroupMapper;

    @Autowired
    private INepFriendshipGroupMemberMapper friendshipGroupMemberMapper;

    public List<NepFriend> doQueryAllFriend(NepQueryAllFriendRequest request) {
        // 1. 获取变量
        Integer appId = request.getHeader().getAppId();
        Integer friendFromId = request.getFriendFromId();
        // 2. 查询用户的好友
        List<NepFriendship> friendshipList = friendshipMapper.queryAllFriendship(appId, friendFromId);
        if (CollectionUtil.isEmpty(friendshipList)){
            log.error("NepFriendUserService doQueryAllFriend: 用户没有任何好友或者用户不存在 - request: {}", request);
            return Collections.emptyList();
        }
        // 3. 根据用户的好友关系查询每个好友的信息
        List<Integer> friendshipIdList = friendshipList.stream()
                                             .map(NepFriendship::getFriendToId)
                                             .collect(Collectors.toList());
        List<NepUser> userList = userMapper.queryUserByIdList(appId, friendshipIdList);
        // 4. 拼装查询结果
        return transferFriendList(friendshipList, userList);
    }

    public List<NepFriend> doQueryFriend(NepQueryFriendRequest request) {
        // 1. 获取变量
        Integer appId = request.getHeader().getAppId();
        Integer friendFromId = request.getFriendFromId();
        String username = request.getUsername();
        String nickname = request.getNickname();
        String remark = request.getFriendRemark();
        // 2. 查询用户
        NepUser currentUser = userMapper.queryUserById(appId, friendFromId);
        // 3. 检查用户是否存在
        if (Objects.isNull(currentUser) || currentUser.isDelete()){
            log.error("NepFriendUserService doQueryFriend: 用户不存在 - request: {}", request);
            return Collections.emptyList();
        }
        List<NepFriend> friendList = new ArrayList<>();
        // 4. 根据好友用户名查询好友
        NepUser user = userMapper.queryUserByUserName(appId, username);
        if (Objects.nonNull(user)){
            NepFriendship friendship = friendshipMapper.queryFriendshipById(appId, friendFromId, user.getUserId());
            if (Objects.nonNull(friendship) && friendship.getFriendshipStatus() == NepFriendshipStatus.FRIENDSHIP_NORMAL.getStatus()){
                NepFriend friend = new NepFriend().setUserId(user.getUserId()).setUsername(user.getUsername()).setNickname(user.getNickname())
                                              .setAvatarAddress(user.getAvatarAddress()).setSelfSignature(user.getSelfSignature())
                                              .setFriendRemark(friendship.getFriendRemark()).setBlack(friendship.isBlack());
                friendList.add(friend);
            }
        }
        // 5. 根据好友昵称查询好友
        List<NepFriend> friendListByNickName = getFriendByNickName(appId, friendFromId, nickname);
        if (CollectionUtil.isNotEmpty(friendListByNickName)){
            friendList.addAll(friendListByNickName);
        }
        // 6. 根据好友备注查询好友
        List<NepFriend> friendListByRemark = getFriendByRemark(appId, friendFromId, remark);
        if (CollectionUtil.isNotEmpty(friendListByRemark)){
            friendList.addAll(friendListByRemark);
        }
        // 7. 去除重复查询的结果
        Map<Integer, NepFriend> friendMap = new HashMap<>();
        for (NepFriend friend : friendList) {
            friendMap.putIfAbsent(friend.getUserId(), friend);
        }
        return new ArrayList<>(friendMap.values());
    }

    public List<NepFriendApplication> doQueryAllFriendApplication(NepQueryAllFriendApplicationRequest request) {
        // 1. 获取变量
        Integer userId = request.getUserId();
        Integer appId = request.getHeader().getAppId();
        // 2. 获取向该用户发出的所有好友申请
        List<NepFriendshipApplication> applicationList = friendshipApplicationMapper.queryAllFriendshipApplication(appId, userId);
        if (CollectionUtil.isEmpty(applicationList)){
            log.error("NepFriendService doQueryAllFriendApplication: 用户没有接收到任何好友申请 - request: {}", request);
            return Collections.emptyList();
        }
        // TODO 3. 将所有未读的好友申请变为已读

        // 4. 查询发出申请的用户信息
        List<NepUser> userList = userMapper.queryUserByIdList(appId, applicationList.stream()
                                                                             .map(NepFriendshipApplication::getFriendFromId)
                                                                             .collect(Collectors.toList()));
        if(CollectionUtil.isEmpty(userList) || applicationList.size() != userList.size()){
            log.error("NepFriendService doQueryAllFriendApplication: 发出好友申请的用户不存在 - request: {}, userList: {}, applicationList: {}", request, userList, applicationList);
            return Collections.emptyList();
        }
        // 5. 拼装返回集合
        return transferFriendApplicationList(userId, userList, applicationList);
    }

    public Map<NepFriendGroup, List<NepFriend>> doQueryAllFriendGroupMember(NepQueryAllFriendGroupMemberRequest request) {
        // 1. 获取变量
        Integer userId = request.getUserId();
        Integer appId = request.getHeader().getAppId();
        // 2. 查询自己创建的所有好友分组
        List<NepFriendshipGroup> groupList = friendshipGroupMapper.queryAllFriendshipGroup(appId, userId);
        // 3. 如果好友分组为空, 那么直接返回空
        if (CollectionUtil.isEmpty(groupList)){
            log.info("NepFriendService doQueryAllFriendGroupMember: 该用户没有创建任何好友分组 - request: {}", request);
            return Collections.emptyMap();
        }
        // 4. 查询每个分组下的成员
        List<NepFriendshipGroupMember> groupMemberList = friendshipGroupMemberMapper.queryAllFriendshipGroupMember(appId,
                groupList.stream().map(NepFriendshipGroup::getGroupId).collect(Collectors.toList()));
        // 5. 如果所有分组下的成员为空, 那么返回分组
        if (CollectionUtil.isEmpty(groupMemberList)){
            log.info("NepFriendService doQueryAllFriendGroupMember: 用户创建的好友分组下没有任何成员 - request: {}", request);
            // 5.1 创建空哈希表
            Map<NepFriendGroup, List<NepFriend>> groupAndGroupMember = new HashMap<>();
            // 5.2 将每个分组都放入哈希表中
            groupList.forEach(group -> groupAndGroupMember.put(BeanUtil.copyProperties(group, NepFriendGroup.class), Collections.emptyList()));
            // 5.3 返回结果
            return groupAndGroupMember;
        }
        List<Integer> groupMemberIdList = groupMemberList.stream().map(NepFriendshipGroupMember::getFriendGroupMemberId).collect(Collectors.toList());
        // 6. 查询用户信息
        List<NepUser> userList = userMapper.queryUserByIdList(appId, groupMemberIdList);
        // 7. 查询好友关系信息
        List<NepFriendship> friendshipList = friendshipMapper.queryFriendshipByIdList(appId, userId, groupMemberIdList);
        // 8. 组合成好友信息
        Map<Integer, NepFriend> friendMap = transferFriendList(friendshipList, userList).stream()
                                                    .collect(Collectors.toMap(NepFriend::getUserId, NepFriend -> NepFriend));
        // 9. 拼装返回结果
        Map<NepFriendGroup, List<NepFriend>> groupAndGroupMember = new HashMap<>();
        // 9.1 将分组信息转换成哈希表
        Map<Integer, NepFriendshipGroup> groupMap = groupList.stream().collect(
                Collectors.toMap(NepFriendshipGroup::getGroupId, NepFriendshipGroup -> NepFriendshipGroup));
        // 9.2 遍历分组成员信息
        for (NepFriendshipGroupMember groupMember : groupMemberList) {
            // 9.2.1 生成返回的分组信息
            NepFriendGroup group = BeanUtil.copyProperties(groupMap.get(groupMember.getFriendGroupId()), NepFriendGroup.class);
            // 9.2.2 查询哈希表中是否有这个对象
            List<NepFriend> memberList = groupAndGroupMember.getOrDefault(group, new ArrayList<>());
            // 9.2.3 向集合中添加成员
            memberList.add(friendMap.get(groupMember.getFriendGroupMemberId()));
            // 9.2.4 将集合放入哈希表中
            groupAndGroupMember.put(group, memberList);
        }
        return groupAndGroupMember;
    }

    private List<NepFriend> getFriendByNickName(int appId, int friendFromId, String nickname) {
        // 1. 根据昵称查询用户
        List<NepUser> userList = userMapper.queryUserByNickName(appId, nickname);
        if (CollectionUtil.isEmpty(userList)){
            return Collections.emptyList();
        }
        // 2. 查询好友关系
        List<NepFriendship> friendshipList = friendshipMapper.queryFriendshipByIdList(appId, friendFromId,
                userList.stream().map(NepUser::getUserId).collect(Collectors.toList()));
        if (CollectionUtil.isEmpty(friendshipList)){
            return Collections.emptyList();
        }
        // 3. 拼装返回结果
        return transferFriendList(friendshipList, userList);
    }

    private List<NepFriend> getFriendByRemark(int appId, int friendFromId, String friendName) {
        // 1. 根据备注查询好友关系
        List<NepFriendship> friendshipList = friendshipMapper.queryFriendshipByRemark(appId, friendFromId, friendName);
        if (CollectionUtil.isEmpty(friendshipList)){
            return Collections.emptyList();
        }
        // 2. 通过好友关系查询对应的用户
        List<NepUser> userList = userMapper.queryUserByIdList(appId,
                friendshipList.stream().map(NepFriendship::getFriendToId).collect(Collectors.toList()));
        if (CollectionUtil.isEmpty(userList)){
            return Collections.emptyList();
        }
        // 3. 组合返回结果
        return transferFriendList(friendshipList, userList);
    }

    private List<NepFriend> transferFriendList(List<NepFriendship> friendshipList, List<NepUser> userList){
        // 1. 准备哈希表
        Map<Integer, NepFriend> friendMap = new HashMap<>();
        // 2. 根据好友关系填充哈希表
        for (NepFriendship friendship : friendshipList) {
            NepFriend friend = new NepFriend().setFriendRemark(friendship.getFriendRemark())
                                       .setUserId(friendship.getFriendToId()).setBlack(friendship.isBlack());
            friendMap.put(friendship.getFriendToId(), friend);
        }
        // 3. 根据用户信息补充返回实体
        for (NepUser user : userList) {
            friendMap.get(user.getUserId()).setUsername(user.getUsername()).setNickname(user.getNickname())
                    .setAvatarAddress(user.getAvatarAddress()).setSelfSignature(user.getSelfSignature());
        }
        // 4. 转换成集合
        return new ArrayList<>(friendMap.values());
    }

    private List<NepFriendApplication> transferFriendApplicationList(int friendToId, List<NepUser> userList, List<NepFriendshipApplication> applicationList) {
        // 1. 将发出好友申请的用户信息填充到返回结果中
        Map<Integer, NepFriendApplication> map = userList.stream()
                                                         .map(user -> new NepFriendApplication().setFriendToId(friendToId).setFriendFromId(user.getUserId())
                                                                              .setFriendAccount(user.getUsername()).setFriendNickName(user.getNickname())
                                                                              .setFriendAvatarAddress(user.getAvatarAddress()))
                                                         .collect(Collectors.toMap(NepFriendApplication::getFriendFromId, NepQueryFriendApplication -> NepQueryFriendApplication));
        // 2. 将好友申请中的信息填充到返回结果中
        for (NepFriendshipApplication application : applicationList) {
            if (map.containsKey(application.getFriendFromId())){
                NepFriendApplication friendApplication = map.get(application.getFriendFromId());
                friendApplication.setFriendApplicationId(application.getFriendshipApplyId())
                        .setFriendSource(application.getFriendshipSource())
                        .setAdditionalInfo(application.getAdditionalInfo())
                        .setReadStatus(application.getApplyReadStatus())
                        .setApproveStatus(application.getApplyApproveStatus());
            }
        }
        // 3. 返回集合
        return new ArrayList<>(map.values());
    }
}
