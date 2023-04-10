package com.fuyusakaiori.nep.im.service.core.user.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.example.nep.im.common.enums.status.NepFriendshipStatus;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.NepFriendship;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.NepFriendshipApplication;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.NepFriendshipGroupMember;
import com.fuyusakaiori.nep.im.service.core.friendship.mapper.*;
import com.fuyusakaiori.nep.im.service.core.user.entity.NepUser;
import com.fuyusakaiori.nep.im.service.core.user.entity.dto.NepFriend;
import com.fuyusakaiori.nep.im.service.core.user.entity.dto.NepFriendApplication;
import com.fuyusakaiori.nep.im.service.core.user.entity.request.friend.NepQueryAllFriendApplicationRequest;
import com.fuyusakaiori.nep.im.service.core.user.entity.request.friend.NepQueryAllFriendRequest;
import com.fuyusakaiori.nep.im.service.core.user.entity.request.friend.NepQueryFriendRequest;
import com.fuyusakaiori.nep.im.service.core.user.mapper.INepUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
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
    private INepFriendshipGroupMemberMapper friendshipGroupMemberMapper;

    public List<NepFriend> doQueryAllFriend(NepQueryAllFriendRequest request) {
        // 1. 获取变量
        Integer appId = request.getHeader().getAppId();
        Integer userId = request.getUserId();
        // 2. 查询用户的好友
        List<NepFriendship> friendshipList = friendshipMapper.queryAllFriendship(appId, userId);
        if (CollectionUtil.isEmpty(friendshipList)){
            log.error("NepFriendUserService doQueryAllFriend: 用户没有任何好友或者用户不存在 - request: {}", request);
            return Collections.emptyList();
        }
        // 3. 根据好友关系实体生成 ID 集合
        List<Integer> friendshipIdList = friendshipList.stream()
                                             .map(NepFriendship::getFriendToId)
                                             .collect(Collectors.toList());
        // 4. 查询好友的详细信息
        List<NepUser> userList = userMapper.queryUserByIdList(appId, friendshipIdList);
        if (CollectionUtil.isEmpty(userList) || userList.size() != friendshipList.size()){
            log.error("NepFriendUserService doQueryAllFriend: 用户有好友的信息不存在! - request: {}", request);
            return Collections.emptyList();
        }
        // 5. 查询好友所在的分组信息
        List<NepFriendshipGroupMember> friendshipGroupMemberList = friendshipGroupMemberMapper.queryFriendshipGroupMemberList(appId, userId, friendshipIdList);
        if (CollectionUtil.isEmpty(friendshipGroupMemberList)){
            log.info("NepFriendUserService doQueryAllFriend: 该用户没有创建任何好友分组 - request: {}", request);
            return transferFriendList(friendshipList, userList, Collections.emptyList());
        }
        // 6. 拼装返回结果
        return transferFriendList(friendshipList, userList, friendshipGroupMemberList);
    }

    /**
     * <h3>暂时保留的方法</h3>
     */
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
        List<NepFriendshipGroupMember> friendshipGroupMemberList = friendshipGroupMemberMapper.queryFriendshipGroupMemberList(appId, friendFromId,
                friendshipList.stream().map(NepFriendship::getFriendToId).collect(Collectors.toList()));
        return transferFriendList(friendshipList, userList, friendshipGroupMemberList);
    }

    private List<NepFriend> getFriendByRemark(int appId, int friendFromId, String friendName) {
        // 1. 根据备注查询好友关系
        List<NepFriendship> friendshipList = friendshipMapper.queryFriendshipByRemark(appId, friendFromId, friendName);
        if (CollectionUtil.isEmpty(friendshipList)){
            return Collections.emptyList();
        }
        List<Integer> friendshipIdList = friendshipList.stream().map(NepFriendship::getFriendToId).collect(Collectors.toList());
        // 2. 通过好友关系查询对应的用户
        List<NepUser> userList = userMapper.queryUserByIdList(appId,
                friendshipIdList);
        if (CollectionUtil.isEmpty(userList)){
            return Collections.emptyList();
        }
        // 3. 组合返回结果
        List<NepFriendshipGroupMember> friendshipGroupMemberList = friendshipGroupMemberMapper.queryFriendshipGroupMemberList(appId, friendFromId, friendshipIdList);
        if (CollectionUtil.isEmpty(friendshipGroupMemberList)){
            return transferFriendList(friendshipList, userList, Collections.emptyList());
        }
        return transferFriendList(friendshipList, userList, friendshipGroupMemberList);
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

    private List<NepFriend> transferFriendList(List<NepFriendship> friendshipList, List<NepUser> userList, List<NepFriendshipGroupMember> friendshipGroupMemberList){
        // 1. 准备哈希表
        Map<Integer, NepFriend> friendMap = new HashMap<>();
        // 2. 根据好友关系填充哈希表
        for (NepFriendship friendship : friendshipList) {
            // 填充好友 ID、好友备注、好友是否被拉黑
            NepFriend friend = new NepFriend().setFriendRemark(friendship.getFriendRemark())
                                       .setUserId(friendship.getFriendToId()).setBlack(friendship.isBlack());
            friendMap.put(friendship.getFriendToId(), friend);
        }
        // 3. 根据用户信息补充返回实体
        for (NepUser user : userList) {
            // 填充好友用户名、好友昵称、好友性别、好友现居地址、好友头像、好友个性签名
            friendMap.get(user.getUserId()).setUsername(user.getUsername()).setNickname(user.getNickname())
                    .setGender(user.getGender()).setLocation(user.getLocation())
                    .setAvatarAddress(user.getAvatarAddress()).setSelfSignature(user.getSelfSignature());
        }
        // 4. 根据好友所在分组信息补充返回实体
        if (CollectionUtil.isNotEmpty(friendshipGroupMemberList)){
            for (NepFriendshipGroupMember friendshipGroupMember : friendshipGroupMemberList) {
                friendMap.get(friendshipGroupMember.getFriendGroupMemberId())
                        .setGroupId(friendshipGroupMember.getFriendGroupId()).setGroupName(friendshipGroupMember.getFriendGroupName());
            }
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
