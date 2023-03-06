package com.fuyusakaiori.nep.im.service.util;

import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.group.NepCreateFriendshipGroupRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.group.NepDeleteFriendshipGroupRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.group.NepQueryAllFriendshipGroupRequest;

public class NepCheckFriendGroupParamUtil {
    public static boolean checkCreateFriendshipGroupRequestParam(NepCreateFriendshipGroupRequest request) {
        return true;
    }

    public static boolean checkDeleteFriendshipGroupRequestParam(NepDeleteFriendshipGroupRequest request) {
        return true;
    }

    public static boolean checkQueryAllFriendshipGroupRequestParam(NepQueryAllFriendshipGroupRequest request) {
        return true;
    }
}
