package com.fuyusakaiori.nep.im.service.util.check;

import com.example.nep.im.common.entity.request.NepRequestHeader;
import com.example.nep.im.common.util.NepCheckBaseParamUtil;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.group.NepAddFriendshipGroupMemberRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.group.NepDeleteFriendshipGroupMemberRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.group.NepMoveFriendshipGroupMemberRequest;

import java.util.Objects;

public class NepCheckFriendGroupMemberParamUtil {


    public static boolean checkAddFriendshipGroupMemberRequestParam(NepAddFriendshipGroupMemberRequest request) {
        NepRequestHeader header = request.getHeader();
        Integer groupId = request.getGroupId();
        Integer groupMemberId = request.getGroupMemberId();
        if (!NepCheckBaseParamUtil.checkNeptuneRequestBaseParam(header)){
            return false;
        }
        if (Objects.isNull(groupId) || groupId <= 0 || Objects.isNull(groupMemberId) || groupMemberId <= 0){
            return false;
        }
        return true;
    }

    public static boolean checkMoveFriendshipGroupMemberRequestParam(NepMoveFriendshipGroupMemberRequest request) {
        NepRequestHeader header = request.getHeader();
        Integer groupId = request.getNewGroupId();
        Integer groupMemberId = request.getGroupMemberId();
        if (!NepCheckBaseParamUtil.checkNeptuneRequestBaseParam(header)){
            return false;
        }
        if (Objects.isNull(groupId) || groupId <= 0 || Objects.isNull(groupMemberId) || groupMemberId <= 0){
            return false;
        }
        return true;
    }

    public static boolean checkDeleteFriendshipGroupMemberRequestParam(NepDeleteFriendshipGroupMemberRequest request) {
        NepRequestHeader header = request.getHeader();
        Integer groupId = request.getGroupId();
        Integer groupMemberId = request.getGroupMemberId();
        if (!NepCheckBaseParamUtil.checkNeptuneRequestBaseParam(header)){
            return false;
        }
        if (Objects.isNull(groupId) || groupId <= 0 || Objects.isNull(groupMemberId) || groupMemberId <= 0){
            return false;
        }
        return true;
    }
}
