package com.fuyusakaiori.nep.im.service.core.user.service.impl;

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
import com.fuyusakaiori.nep.im.service.core.user.entity.dto.NepFriendGroup;
import com.fuyusakaiori.nep.im.service.core.user.entity.dto.NepQueryFriendApplication;
import com.fuyusakaiori.nep.im.service.core.user.entity.request.friend.*;
import com.fuyusakaiori.nep.im.service.core.user.entity.response.friend.NepQueryFriendApplicationResponse;
import com.fuyusakaiori.nep.im.service.core.user.entity.response.friend.NepQueryFriendGroupMemberResponse;
import com.fuyusakaiori.nep.im.service.core.user.entity.response.normal.NepQueryUserResponse;
import com.fuyusakaiori.nep.im.service.core.user.mapper.INepUserMapper;
import com.fuyusakaiori.nep.im.service.core.user.service.INepFriendService;
import com.fuyusakaiori.nep.im.service.core.util.check.NepCheckFriendParamUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class NepFriendService implements INepFriendService {

    @Autowired
    private INepUserMapper userMapper;

    @Autowired
    private INepFriendshipMapper friendshipMapper;

    @Autowired
    private INepFriendshipBlackMapper friendshipBlackMapper;

    @Autowired
    private INepFriendshipApplicationMapper friendshipApplicationMapper;

    @Autowired
    private INepFriendshipGroupMapper friendshipGroupMapper;

    @Autowired
    private INepFriendshipGroupMemberMapper friendshipGroupMemberMapper;

    @Override
    public NepQueryUserResponse queryAllFriend(NepQueryAllFriendRequest request) {
        // 0. ??????????????????
        NepQueryUserResponse response = new NepQueryUserResponse();
        // 1. ????????????
        if (!NepCheckFriendParamUtil.checkNepQueryAllFriendRequestParam(request)){
            log.error("NepFriendUserService queryAllFriendUser: ????????????????????????????????? - request: {}", request);
            return response.setCode(NepBaseResponseCode.CHECK_PARAM_FAIL.getCode())
                           .setMessage(NepBaseResponseCode.CHECK_PARAM_FAIL.getMessage());
        }
        // 2. ????????????
        NepRequestHeader header = request.getRequestHeader();
        Integer friendFromId = request.getFriendFromId();
        // 3. ?????????????????????
        List<NepFriendship> friendshipList = friendshipMapper.queryAllFriendship(header.getAppId(), friendFromId);
        if (CollectionUtil.isEmpty(friendshipList)){
            log.error("NepFriendUserService queryAllFriendUser: ???????????????????????? - request: {}", request);
            return response.setCode(NepFriendshipResponseCode.FRIENDSHIP_NOT_EXIST.getCode())
                           .setMessage(NepFriendshipResponseCode.FRIENDSHIP_NOT_EXIST.getMessage());
        }
        // 4. ????????????????????????????????????????????????????????????
        List<Integer> friendIdList = friendshipList.stream()
                                             .map(NepFriendship::getFriendshipToId)
                                             .collect(Collectors.toList());
        List<NepUser> friendList = userMapper.querySimpleUserByIdList(header.getAppId(), friendIdList);
        // 4. ????????????
        if (CollectionUtil.isEmpty(friendList)){
            log.info("NepFriendUserService queryAllFriendUser: ????????????????????????????????????????????????????????? - request: {}", request);
            return response.setUserList(Collections.emptyList())
                           .setCode(NepUserResponseCode.USER_LIST_EMPTY.getCode())
                           .setMessage(NepUserResponseCode.USER_LIST_EMPTY.getMessage());
        }
        // 5. ??????????????????
        return response.setUserList(friendList)
                       .setCode(NepBaseResponseCode.SUCCESS.getCode())
                       .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
    }

    @Override
    public NepQueryUserResponse queryFriendByAccount(NepQueryFriendByAccountRequest request) {
        // 0. ??????????????????
        NepQueryUserResponse response = new NepQueryUserResponse();
        // 1. ????????????
        if (!NepCheckFriendParamUtil.checkNepQueryFriendByAccountRequestParam(request)){
            log.error("NepFriendUserService queryFriendByAccount: ?????????????????? - request: {}", request);
            return response.setCode(NepBaseResponseCode.CHECK_PARAM_FAIL.getCode())
                           .setMessage(NepBaseResponseCode.CHECK_PARAM_FAIL.getMessage());
        }
        // 2. ????????????
        NepRequestHeader header = request.getRequestHeader();
        Integer friendFromId = request.getFriendFromId();
        String userAccount = request.getUserAccount();
        // ???: ????????????????????????????????????????????????, ?????????????????????, ??????????????????????????????
        // 3. ????????????
        NepUser user = userMapper.querySimpleUserByAccount(header.getAppId(), userAccount);
        // 4. ????????????????????????
        if (Objects.isNull(user)){
            log.error("NepFriendUserService queryFriendByAccount: ?????????????????? - request: {}", request);
            return response.setCode(NepUserResponseCode.USER_NOT_EXIST.getCode())
                           .setMessage(NepUserResponseCode.USER_NOT_EXIST.getMessage());
        }
        // 5. ??????????????????
        NepFriendship friendship = friendshipMapper.queryFriendshipById(header.getAppId(), friendFromId, user.getUserId());
        // 6. ??????????????????????????????
        if (Objects.isNull(friendship) || friendship.getFriendshipStatus() == NepFriendshipStatus.FRIENDSHIP_RELEASE.getStatus()){
            log.error("NepFriendUserService queryFriendByAccount: ????????????????????? - from: {}, to: {}, request: {}", friendFromId, user.getUserId(), request);
            return response.setCode(NepFriendshipResponseCode.FRIENDSHIP_NOT_EXIST.getCode())
                           .setMessage(NepFriendshipResponseCode.FRIENDSHIP_NOT_EXIST.getMessage());
        }
        // 7. ????????????, ???????????????????????????
        return response.setUserList(Collections.singletonList(user))
                       .setCode(NepBaseResponseCode.SUCCESS.getCode())
                       .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
    }

    @Override
    public NepQueryUserResponse queryFriendByName(NepQueryFriendByNameRequest request) {
        // 0. ??????????????????
        NepQueryUserResponse response = new NepQueryUserResponse();
        // 1. ????????????
        if (!NepCheckFriendParamUtil.checkNepQueryFriendByNameRequestParam(request)){
            log.error("NepFriendUserService queryFriendByName: ?????????????????? - request: {}", request);
            return response.setCode(NepBaseResponseCode.CHECK_PARAM_FAIL.getCode())
                           .setMessage(NepBaseResponseCode.CHECK_PARAM_FAIL.getMessage());
        }
        // 2. ????????????
        NepRequestHeader header = request.getRequestHeader();
        Integer friendFromId = request.getFriendFromId();
        String friendName = request.getFriendName();
        // ???: ???????????????????????????????????????, ?????????????????????????????????
        // 3. ??????????????????????????????
        List<NepUser> friendByRemark = getFriendByRemark(header, friendFromId, friendName);
        // ???: ??????????????????????????????????????????????????????, ??????????????????????????? (?????????????????????????????????????????????)
        // 5. ????????????????????????
        List<NepUser> friendByNickName = getFriendByNickName(header, friendFromId, friendName);
        // 6. ??????????????????
        List<NepUser> friends = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(friendByRemark)){
            friends.addAll(friendByRemark);
        }
        if (CollectionUtil.isNotEmpty(friendByNickName)){
            friends.addAll(friendByNickName);
        }
        // 7. ??????????????????
        if (CollectionUtil.isEmpty(friends)){
            log.error("NepFriendUserService queryFriendByName: ??????????????????????????? - fromId: {}, name: {}", friendFromId, friendName);
            return response.setUserList(Collections.emptyList())
                           .setCode(NepFriendshipResponseCode.FRIENDSHIP_NOT_EXIST.getCode())
                           .setMessage(NepFriendshipResponseCode.FRIENDSHIP_NOT_EXIST.getMessage());
        }
        return response.setUserList(friends)
                       .setCode(NepBaseResponseCode.SUCCESS.getCode())
                       .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
    }

    private List<NepUser> getFriendByNickName(NepRequestHeader header, Integer friendFromId, String friendName) {
        // 1. ????????????????????????
        List<NepUser> userList = userMapper.querySimpleUserByNickName(header.getAppId(), friendName);
        if (CollectionUtil.isEmpty(userList)){
            return Collections.emptyList();
        }
        // 2. ??????????????????
        List<NepFriendship> friendshipList = friendshipMapper.queryFriendshipByIdList(header.getAppId(), friendFromId,
                userList.stream().map(NepUser::getUserId).collect(Collectors.toList()));
        if (CollectionUtil.isEmpty(friendshipList)){
            return Collections.emptyList();
        }
        // 3. ????????????
        List<NepUser> friendByNickName = new ArrayList<>();
        Set<Integer> friendIdSet = friendshipList.stream()
                                           .map(NepFriendship::getFriendshipToId)
                                           .collect(Collectors.toSet());
        for (NepUser user : userList) {
            if (friendIdSet.contains(user.getUserId())){
                friendByNickName.add(user);
            }
        }
        return friendByNickName;
    }

    private List<NepUser> getFriendByRemark(NepRequestHeader header, Integer friendFromId, String friendName) {
        // 1. ??????????????????????????????
        List<Integer> friendIdListByRemark = friendshipMapper.queryFriendshipByRemark(header.getAppId(), friendFromId, friendName)
                                                   .stream().map(NepFriendship::getFriendshipToId)
                                                   .collect(Collectors.toList());
        if (CollectionUtil.isEmpty(friendIdListByRemark)){
            return Collections.emptyList();
        }
        // 2. ???????????????????????????????????????
        List<NepUser> friendByRemark = userMapper.querySimpleUserByIdList(header.getAppId(), friendIdListByRemark);
        if (CollectionUtil.isEmpty(friendByRemark)){
            return Collections.emptyList();
        }
        return friendByRemark;
    }

    @Override
    public NepQueryUserResponse queryAllFriendBlackList(NepQueryAllFriendBlackRequest request) {
        // 0. ??????????????????
        NepQueryUserResponse response = new NepQueryUserResponse();
        // 1. ????????????
        if (NepCheckFriendParamUtil.checkNepQueryAllFriendBlackListRequestParam(request)){
            log.error("NepFriendService queryAllFriendBlackList: ?????????????????? - request: {}", request);
            return response.setUserList(Collections.emptyList())
                           .setCode(NepBaseResponseCode.CHECK_PARAM_FAIL.getCode())
                           .setMessage(NepBaseResponseCode.CHECK_PARAM_FAIL.getMessage());
        }
        // 2. ????????????
        NepRequestHeader header = request.getRequestHeader();
        Integer friendFromId = request.getFriendFromId();
        // 3. ???????????????????????????????????? ID
        List<Integer> friendToIdList = friendshipBlackMapper.queryAllFriendInBlackList(header.getAppId(), friendFromId);
        if (CollectionUtil.isEmpty(friendToIdList)){
            log.error("NepFriendService queryAllFriendBlackList: ??????????????????????????? - request: {}", request);
            return response.setUserList(Collections.emptyList())
                           .setCode(NepFriendshipBlackResponseCode.FRIEND_BLACK_NOT_EXIST.getCode())
                           .setMessage(NepFriendshipBlackResponseCode.FRIEND_BLACK_NOT_EXIST.getMessage());
        }
        // 4. ????????????????????????????????????
        List<NepUser> blackUserList = userMapper.querySimpleUserByIdList(header.getAppId(), friendToIdList);
        if(CollectionUtil.isEmpty(blackUserList)){
            log.error("NepFriendService queryAllFriendBlackList: ?????????????????????????????? - request: {}", request);
            return response.setUserList(Collections.emptyList())
                           .setCode(NepUserResponseCode.USER_LIST_EMPTY.getCode())
                           .setMessage(NepUserResponseCode.USER_LIST_EMPTY.getMessage());
        }
        return response.setUserList(blackUserList)
                       .setCode(NepBaseResponseCode.SUCCESS.getCode())
                       .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
    }

    @Override
    public NepQueryFriendApplicationResponse queryAllFriendApplication(NepQueryAllFriendApplicationRequest request) {
        // 0. ??????????????????
        NepQueryFriendApplicationResponse response = new NepQueryFriendApplicationResponse();
        // 1. ????????????
        if (NepCheckFriendParamUtil.checkNepQueryAllFriendApplicationRequestParam(request)){
            log.error("NepFriendService queryAllFriendApplication: ?????????????????? - request: {}", request);
            return response.setApplicationList(Collections.emptyList())
                           .setCode(NepBaseResponseCode.CHECK_PARAM_FAIL.getCode())
                           .setMessage(NepBaseResponseCode.CHECK_PARAM_FAIL.getMessage());
        }
        // 2. ????????????
        NepRequestHeader header = request.getRequestHeader();
        Integer userId = request.getUserId();
        // 3. ?????????????????????????????????????????????
        List<NepFriendshipApplication> applicationList = friendshipApplicationMapper.queryAllFriendshipApplication(header.getAppId(), userId);
        if (CollectionUtil.isEmpty(applicationList)){
            log.error("NepFriendService queryAllFriendApplication: ??????????????????????????????????????? - request: {}", request);
            return response.setApplicationList(Collections.emptyList())
                           .setCode(NepFriendshipApplicationResponseCode.FRIEND_APPLICATION_NOT_EXIST.getCode())
                           .setMessage(NepFriendshipApplicationResponseCode.FRIEND_APPLICATION_NOT_EXIST.getMessage());
        }
        // TODO ??????????????????????????????????????????

        // 4. ?????????????????????????????????
        List<NepUser> userList = userMapper.querySimpleUserByIdList(header.getAppId(), applicationList.stream()
                                                                                               .map(NepFriendshipApplication::getFriendshipFromId)
                                                                                               .collect(Collectors.toList()));
        if(CollectionUtil.isEmpty(userList)){
            log.error("NepFriendService queryAllFriendApplication: ???????????????????????????????????? - request: {}", request);
            return response.setApplicationList(Collections.emptyList())
                           .setCode(NepUserResponseCode.USER_LIST_EMPTY.getCode())
                           .setMessage(NepUserResponseCode.USER_LIST_EMPTY.getMessage());
        }
        // 5. ??????????????????
        List<NepQueryFriendApplication> friendApplicationList = transferFriendApplicationList(userId, userList, applicationList);
        // 6. ??????????????????
        return response.setApplicationList(friendApplicationList)
                       .setCode(NepBaseResponseCode.SUCCESS.getCode())
                       .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
    }

    /**
     * <h3>?????????????????????????????????????????????????????????</h3>
     */
    private List<NepQueryFriendApplication> transferFriendApplicationList(int friendToId, List<NepUser> userList, List<NepFriendshipApplication> applicationList) {
        // 1. ????????????????????????????????????????????????????????????
        Map<Integer, NepQueryFriendApplication> map = userList.stream()
                                                              .map(user -> new NepQueryFriendApplication()
                                                                                   .setFriendFromId(user.getUserId())
                                                                                   .setFriendAccount(user.getUserAccount())
                                                                                   .setFriendNickName(user.getUserNickName())
                                                                                   .setFriendAvatarAddress(user.getUserAvatarAddress()))
                                                              .collect(Collectors.toMap(NepQueryFriendApplication::getFriendFromId, NepQueryFriendApplication -> NepQueryFriendApplication));
        // 2. ???????????????????????????????????????????????????
        for (NepFriendshipApplication application : applicationList) {
            if (map.containsKey(application.getFriendshipFromId())){
                NepQueryFriendApplication friendApplication = map.get(application.getFriendshipFromId());
                friendApplication.setFriendApplicationId(application.getFriendshipApplyId())
                         .setFriendToId(friendToId)
                         .setFriendSource(application.getApplySource())
                         .setAdditionalInfo(application.getApplyAdditionalInfo())
                         .setReadStatus(application.getApplyReadStatus())
                         .setApproveStatus(application.getApplyApproveStatus());
            }
        }
        // 3. ????????????
        return new ArrayList<>(map.values());
    }


    @Override
    public NepQueryFriendGroupMemberResponse queryAllFriendGroupMember(NepQueryAllFriendGroupMemberRequest request) {
        // 0. ??????????????????
        NepQueryFriendGroupMemberResponse response = new NepQueryFriendGroupMemberResponse();
        // 1. ????????????
        if (NepCheckFriendParamUtil.checkNepQueryAllFriendGroupMemberRequestParam(request)){
            log.error("NepFriendService queryAllFriendGroupMember: ?????????????????? - request: {}", request);
            return response.setFriendGroupMemberMap(Collections.emptyMap())
                           .setCode(NepBaseResponseCode.CHECK_PARAM_FAIL.getCode())
                           .setMessage(NepBaseResponseCode.CHECK_PARAM_FAIL.getMessage());
        }
        // 2. ????????????
        NepRequestHeader header = request.getRequestHeader();
        Integer userId = request.getUserId();
        // 3. ???????????????????????????????????????
        List<NepFriendshipGroup> groupList = friendshipGroupMapper.queryAllFriendshipGroup(header.getAppId(), userId);
        if (CollectionUtil.isEmpty(groupList)){
            log.error("NepFriendService queryAllFriendGroupMember: ??????????????????????????????????????? - request: {}", request);
            return response.setFriendGroupMemberMap(Collections.emptyMap())
                           .setCode(NepFriendshipGroupResponseCode.FRIEND_GROUP_NOT_EXIST.getCode())
                           .setMessage(NepFriendshipGroupResponseCode.FRIEND_GROUP_NOT_EXIST.getMessage());
        }
        // 4. ??????????????????????????????
        List<NepFriendshipGroupMember> friendshipGroupMemberList = friendshipGroupMemberMapper.queryAllFriendshipGroupMember(header.getAppId(),
                groupList.stream().map(NepFriendshipGroup::getGroupId).collect(Collectors.toList()));
        if (CollectionUtil.isEmpty(friendshipGroupMemberList)){
            // TODO
        }
        Map<Integer, List<Integer>> groupIdAndGroupMemberIdList = new HashMap<>();
        for (NepFriendshipGroupMember friendshipGroupMember : friendshipGroupMemberList) {
            List<Integer> groupMemberIdList = groupIdAndGroupMemberIdList.getOrDefault(friendshipGroupMember.getFriendshipGroupId(), new ArrayList<>());
            groupMemberIdList.add(friendshipGroupMember.getFriendshipGroupMemberId());
            groupIdAndGroupMemberIdList.put(friendshipGroupMember.getFriendshipGroupId(), groupMemberIdList);
        }
        // 5. ????????????????????????????????????
        Map<NepFriendGroup, List<NepFriend>> groupAndGroupMember = new HashMap<>();
        for(Map.Entry<Integer, List<Integer>> entry : groupIdAndGroupMemberIdList.entrySet()){
            Integer groupId = entry.getKey();
            List<Integer> groupMemberIdList = entry.getValue();
            List<NepUser> userList = userMapper.querySimpleUserByIdList(header.getAppId(), groupMemberIdList);
            NepFriendshipGroup friendshipGroup = groupList.get(groupId);
            NepFriendGroup friendGroup = transferFriendshipGroupToFriendGroup(friendshipGroup);
            List<NepFriend> friendGroupMemberList = userList.stream().map(this::transferUserToFriend).collect(Collectors.toList());
            groupAndGroupMember.put(friendGroup, friendGroupMemberList);
        }
        return response.setFriendGroupMemberMap(groupAndGroupMember)
                       .setCode(NepBaseResponseCode.SUCCESS.getCode())
                       .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
    }

    private NepFriendGroup transferFriendshipGroupToFriendGroup(NepFriendshipGroup friendshipGroup) {
        return new NepFriendGroup().setGroupId(friendshipGroup.getGroupId())
                       .setGroupOwnerId(friendshipGroup.getGroupOwnerId())
                       .setGroupName(friendshipGroup.getGroupName());
    }

    private NepFriend transferUserToFriend(NepUser user) {
        return new NepFriend()
                       .setUserId(user.getUserId())
                       .setUserAccount(user.getUserAccount())
                       .setUserNickName(user.getUserNickName())
                       .setUserAvatarAddress(user.getUserAvatarAddress())
                       .setUserSelfSignature(user.getUserSelfSignature());
    }
}
