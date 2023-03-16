package com.fuyusakaiori.nep.im.service.util.check;

import com.example.nep.im.common.entity.request.NepRequestHeader;
import com.example.nep.im.common.util.NepCheckBaseParamUtil;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.group.NepAddFriendshipGroupMemberRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.group.NepMoveFriendshipGroupMemberRequest;

import java.util.List;
import java.util.Objects;

public class NepCheckFriendGroupMemberParamUtil {


    public static boolean checkAddFriendshipGroupMemberRequestParam(NepAddFriendshipGroupMemberRequest request) {
        return checkFriendshipGroupMemberRequestParam(request.getHeader(), request.getGroupId(), request.getGroupMemberIdList());
    }

    public static boolean checkMoveFriendshipGroupMemberRequestParam(NepMoveFriendshipGroupMemberRequest request) {
        return checkFriendshipGroupMemberRequestParam(request.getHeader(), request.getGroupId(), request.getGroupMemberIdList());
    }

    private static boolean checkFriendshipGroupMemberRequestParam(NepRequestHeader header, Integer groupId, List<Integer> groupMemberIdList) {
        if (!NepCheckBaseParamUtil.checkNeptuneRequestBaseParam(header)){
            return false;
        }
        if (Objects.isNull(groupId) || groupId <= 0){
            return false;
        }
        for (Integer groupMemberId : groupMemberIdList) {
            if (Objects.isNull(groupMemberId) || groupMemberId <= 0){
                return false;
            }
        }
        return true;
    }
}
