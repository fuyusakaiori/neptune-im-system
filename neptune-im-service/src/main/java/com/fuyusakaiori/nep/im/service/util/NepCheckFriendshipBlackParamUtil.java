package com.fuyusakaiori.nep.im.service.util;


import com.example.neptune.im.common.entity.request.NepRequestHeader;
import com.example.neptune.im.common.util.NepCheckBaseParamUtil;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.black.NepAddFriendshipBlackRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.black.NepCheckFriendshipBlackRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.black.NepRemoveFriendshipBlackRequest;

import java.util.Objects;

public class NepCheckFriendshipBlackParamUtil {
    public static boolean checkAddFriendInBlackListRequestParam(NepAddFriendshipBlackRequest request) {
        NepRequestHeader header = request.getRequestHeader();
        Integer friendFromId = request.getFriendFromId();
        Integer friendToId = request.getFriendToId();
        if (!NepCheckBaseParamUtil.checkNeptuneRequestBaseParam(header)){
            return false;
        }
        if (Objects.isNull(friendFromId) || friendFromId <= 0 || Objects.isNull(friendToId) || friendToId <= 0){
            return false;
        }
        return true;
    }

    public static boolean checkRemoveFriendInBlackListRequestParam(NepRemoveFriendshipBlackRequest request) {
        return true;
    }

    public static boolean checkVerifyFriendInBlackListRequestParam(NepCheckFriendshipBlackRequest request) {
        return true;
    }
}
