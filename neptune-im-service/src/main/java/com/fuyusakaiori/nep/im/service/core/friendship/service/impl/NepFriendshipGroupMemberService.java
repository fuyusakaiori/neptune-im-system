package com.fuyusakaiori.nep.im.service.core.friendship.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.example.nep.im.common.entity.request.NepRequestHeader;
import com.example.nep.im.common.enums.code.NepBaseResponseCode;
import com.example.nep.im.common.enums.code.NepFriendshipGroupMemberResponseCode;
import com.example.nep.im.common.enums.code.NepFriendshipGroupResponseCode;
import com.example.nep.im.common.enums.code.NepUserResponseCode;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.NepFriendshipGroup;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.group.NepAddFriendshipGroupMemberRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.group.NepMoveFriendshipGroupMemberRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.group.NepModifyFriendshipGroupMemberResponse;
import com.fuyusakaiori.nep.im.service.core.friendship.mapper.INepFriendshipGroupMapper;
import com.fuyusakaiori.nep.im.service.core.friendship.mapper.INepFriendshipGroupMemberMapper;
import com.fuyusakaiori.nep.im.service.core.friendship.service.INepFriendshipGroupMemberService;
import com.fuyusakaiori.nep.im.service.core.user.entity.NepUser;
import com.fuyusakaiori.nep.im.service.core.user.mapper.INepUserMapper;
import com.fuyusakaiori.nep.im.service.core.util.check.NepCheckFriendGroupMemberParamUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class NepFriendshipGroupMemberService implements INepFriendshipGroupMemberService {

    @Autowired
    private INepUserMapper userMapper;

    @Autowired
    private INepFriendshipGroupMapper friendshipGroupMapper;

    @Autowired
    private INepFriendshipGroupMemberMapper friendshipGroupMemberMapper;

    @Override
    public NepModifyFriendshipGroupMemberResponse addFriendshipGroupMember(NepAddFriendshipGroupMemberRequest request) {
        // 0. ??????????????????
        NepModifyFriendshipGroupMemberResponse response = new NepModifyFriendshipGroupMemberResponse();
        // 1. ????????????
        if (!NepCheckFriendGroupMemberParamUtil.checkAddFriendshipGroupMemberRequestParam(request)){
            log.error("NepFriendshipApplicationService addFriendshipGroupMember: ?????????????????? - request: {}", request);
            return response.setCode(NepBaseResponseCode.CHECK_PARAM_FAIL.getCode())
                           .setMessage(NepBaseResponseCode.CHECK_PARAM_FAIL.getMessage());
        }
        // 2. ????????????
        NepRequestHeader header = request.getRequestHeader();
        Integer groupId = request.getGroupId();
        List<Integer> groupMemberIdList = request.getGroupMemberIdList();
        // 3. ????????????
        NepFriendshipGroup friendshipGroup = friendshipGroupMapper.queryFriendshipGroupById(header.getAppId(), groupId);
        // 4. ????????????????????????
        if (Objects.isNull(friendshipGroup)){
            log.error("NepFriendshipApplicationService addFriendshipGroupMember: ????????????????????? - request: {}", request);
            return response.setCode(NepFriendshipGroupResponseCode.FRIEND_GROUP_NOT_EXIST.getCode())
                           .setMessage(NepFriendshipGroupResponseCode.FRIEND_GROUP_NOT_EXIST.getMessage());
        }
        // 5. ??????????????????????????????
        List<Integer> groupJoinedMemberIdList = friendshipGroupMemberMapper.queryFriendshipGroupMemberByMemberIdList(header.getAppId(), groupMemberIdList);
        // 6. ???????????????????????????????????????
        List<Integer> groupDisjointMemberIdList = CollectionUtil.subtractToList(groupMemberIdList, groupJoinedMemberIdList);
        // 7. ???????????????????????????????????????????????????????????????
        if (CollectionUtil.isNotEmpty(groupDisjointMemberIdList)){
            // 7.1 ???????????????????????????????????????????????????
            List<NepUser> userList = userMapper.querySimpleUserByIdList(header.getAppId(), groupDisjointMemberIdList);
            if (CollectionUtil.isEmpty(userList) || userList.size() != groupDisjointMemberIdList.size()){
                log.error("NepFriendshipApplicationService addFriendshipGroupMember: ???????????????????????????????????????????????????????????? - request: {}", request);
                return response.setCode(NepUserResponseCode.USER_NOT_EXIST.getCode())
                               .setMessage(NepUserResponseCode.USER_NOT_EXIST.getMessage());
            }
            // 7.2 ??????????????????????????????
            int result = friendshipGroupMemberMapper.addFriendshipGroupMember(header.getAppId(), groupId, groupDisjointMemberIdList, System.currentTimeMillis(), System.currentTimeMillis());
            if (result <= 0){
                log.error("NepFriendshipApplicationService addFriendshipGroupMember: ???????????????????????? - request: {}", request);
                return response.setCode(NepFriendshipGroupMemberResponseCode.GROUP_MEMBER_ADD_FAIL.getCode())
                               .setMessage(NepFriendshipGroupMemberResponseCode.GROUP_MEMBER_ADD_FAIL.getMessage());
            }
        }
        // 8. ??????????????????????????????????????????
        if (CollectionUtil.isNotEmpty(groupJoinedMemberIdList)){
            int result = friendshipGroupMemberMapper.moveFriendshipGroupMember(header.getAppId(), groupId, groupJoinedMemberIdList, System.currentTimeMillis());
            if (result <= 0){
                log.error("NepFriendshipApplicationService addFriendshipGroupMember: ???????????????????????? - request: {}", request);
                return response.setCode(NepFriendshipGroupMemberResponseCode.GROUP_MEMBER_MOVE_FAIL.getCode())
                               .setMessage(NepFriendshipGroupMemberResponseCode.GROUP_MEMBER_MOVE_FAIL.getMessage());
            }
        }
        return response.setCode(NepBaseResponseCode.SUCCESS.getCode())
                       .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
    }

    @Override
    public NepModifyFriendshipGroupMemberResponse moveFriendshipGroupMember(NepMoveFriendshipGroupMemberRequest request) {
        // 0. ??????????????????
        NepModifyFriendshipGroupMemberResponse response = new NepModifyFriendshipGroupMemberResponse();
        // 1. ????????????
        if (!NepCheckFriendGroupMemberParamUtil.checkMoveFriendshipGroupMemberRequestParam(request)){
            log.error("NepFriendshipApplicationService moveFriendshipGroupMember: ?????????????????? - request: {}", request);
            return response.setCode(NepBaseResponseCode.CHECK_PARAM_FAIL.getCode())
                           .setMessage(NepBaseResponseCode.CHECK_PARAM_FAIL.getMessage());
        }
        // 2. ????????????
        NepRequestHeader header = request.getRequestHeader();
        Integer groupId = request.getGroupId();
        List<Integer> groupMemberIdList = request.getGroupMemberIdList();
        // 3. ????????????
        NepFriendshipGroup friendshipGroup = friendshipGroupMapper.queryFriendshipGroupById(header.getAppId(), groupId);
        // 4. ????????????????????????
        if (Objects.isNull(friendshipGroup)){
            log.error("NepFriendshipApplicationService moveFriendshipGroupMember: ????????????????????? - request: {}", request);
            return response.setCode(NepFriendshipGroupResponseCode.FRIEND_GROUP_NOT_EXIST.getCode())
                           .setMessage(NepFriendshipGroupResponseCode.FRIEND_GROUP_NOT_EXIST.getMessage());
        }
        // 5. ???????????????????????????
        List<Integer> groupJoinedMemberIdList = friendshipGroupMemberMapper.queryFriendshipGroupMemberByMemberIdList(header.getAppId(), groupMemberIdList);
        // 6. ??????????????????????????????
        if (CollectionUtil.isEmpty(groupJoinedMemberIdList) || groupJoinedMemberIdList.size() != groupMemberIdList.size()){
            log.error("NepFriendshipApplicationService moveFriendshipGroupMember: ??????????????????????????????, ???????????? - request: {}", request);
            return response.setCode(NepFriendshipGroupMemberResponseCode.GROUP_MEMBER_NOT_EXIST.getCode())
                           .setMessage(NepFriendshipGroupMemberResponseCode.GROUP_MEMBER_NOT_EXIST.getMessage());
        }
        // 7. ????????????
        int result = friendshipGroupMemberMapper.moveFriendshipGroupMember(header.getAppId(), groupId, groupMemberIdList, System.currentTimeMillis());
        if (result <= 0){
            log.error("NepFriendshipApplicationService moveFriendshipGroupMember: ?????????????????????????????? - request: {}", request);
            return response.setCode(NepFriendshipGroupMemberResponseCode.GROUP_MEMBER_MOVE_FAIL.getCode())
                           .setMessage(NepFriendshipGroupMemberResponseCode.GROUP_MEMBER_MOVE_FAIL.getMessage());
        }
        return response.setCode(NepBaseResponseCode.SUCCESS.getCode())
                       .setMessage(NepBaseResponseCode.SUCCESS.getMessage());
    }
}
