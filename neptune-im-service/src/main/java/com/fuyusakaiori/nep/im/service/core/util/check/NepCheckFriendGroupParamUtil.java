package com.fuyusakaiori.nep.im.service.core.util.check;

import cn.hutool.core.util.StrUtil;
import com.example.nep.im.common.entity.request.NepRequestHeader;
import com.example.nep.im.common.check.NepCheckBaseParamUtil;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.group.NepCreateFriendshipGroupRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.group.NepDeleteFriendshipGroupRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.group.NepQueryAllFriendshipGroupRequest;

import java.util.Objects;

public class NepCheckFriendGroupParamUtil {
    public static boolean checkCreateFriendshipGroupRequestParam(NepCreateFriendshipGroupRequest request) {
        NepRequestHeader header = request.getRequestHeader();
        String groupName = request.getGroupName();
        Integer groupOwnerId = request.getGroupOwnerId();
        if (!NepCheckBaseParamUtil.checkNeptuneRequestBaseParam(header)){
            return false;
        }
        if (StrUtil.isEmpty(groupName) || Objects.isNull(groupOwnerId) || groupOwnerId <= 0){
            return false;
        }
        return true;
    }

    public static boolean checkDeleteFriendshipGroupRequestParam(NepDeleteFriendshipGroupRequest request) {
        NepRequestHeader header = request.getRequestHeader();
        Integer groupId = request.getGroupId();
        if (!NepCheckBaseParamUtil.checkNeptuneRequestBaseParam(header)){
            return false;
        }
        if (Objects.isNull(groupId) || groupId <= 0){
            return false;
        }
        return true;
    }

    public static boolean checkQueryAllFriendshipGroupRequestParam(NepQueryAllFriendshipGroupRequest request) {
        NepRequestHeader header = request.getRequestHeader();
        Integer groupOwnerId = request.getGroupOwnerId();
        if (!NepCheckBaseParamUtil.checkNeptuneRequestBaseParam(header)){
            return false;
        }
        if (Objects.isNull(groupOwnerId) || groupOwnerId <= 0){
            return false;
        }
        return true;
    }
}
