package com.fuyusakaiori.nep.im.service.util.check;


import cn.hutool.core.util.StrUtil;
import com.example.nep.im.common.entity.request.NepRequestHeader;
import com.example.nep.im.common.util.NepCheckBaseParamUtil;
import com.example.nep.im.common.enums.status.NepFriendshipCheckType;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.normal.*;

import java.util.Objects;

public class NepCheckFriendshipParamUtil {

    public static boolean checkNepAddFriendshipRequestParam(NepAddFriendshipRequest request){
        NepRequestHeader header = request.getHeader();
        Integer friendFromId = request.getFriendFromId();
        Integer friendToId = request.getFriendToId();
        if (!NepCheckBaseParamUtil.checkNeptuneRequestBaseParam(request.getHeader())){
            return false;
        }
        if (Objects.isNull(friendFromId) || friendFromId <= 0 || Objects.isNull(friendToId) || friendToId <= 0){
            return false;
        }
        return true;
    }

    public static boolean checkNepReleaseFriendshipRequestParam(NepReleaseFriendshipRequest request){
        NepRequestHeader header = request.getHeader();
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

    public static boolean checkNepReleaseAllFriendshipRequestParam(NepReleaseAllFriendshipRequest request){
        NepRequestHeader header = request.getHeader();
        Integer friendFromId = request.getFriendFromId();
        if (!NepCheckBaseParamUtil.checkNeptuneRequestBaseParam(header)){
            return false;
        }
        if (Objects.isNull(friendFromId) || friendFromId <= 0){
            return false;
        }
        return true;
    }

    public static boolean checkNepEditFriendshipRequestParam(NepEditFriendshipRemarkRequest request){
        NepRequestHeader header = request.getHeader();
        Integer friendFromId = request.getFriendFromId();
        Integer friendToId = request.getFriendToId();
        String friendRemark = request.getFriendRemark();
        if (!NepCheckBaseParamUtil.checkNeptuneRequestBaseParam(header)){
            return false;
        }
        if (Objects.isNull(friendFromId) || friendFromId <= 0 || Objects.isNull(friendToId) || friendToId <= 0){
            return false;
        }
        if (StrUtil.isEmpty(friendRemark)){
            return false;
        }
        return true;
    }

    public static boolean checkNepVerifyFriendshipRequestParam(NepCheckFriendshipRequest request) {
        NepRequestHeader header = request.getRequestHeader();
        Integer friendFromId = request.getFriendFromId();
        Integer friendToId = request.getFriendToId();
        Integer checkType = request.getCheckType();
        if (!NepCheckBaseParamUtil.checkNeptuneRequestBaseParam(header)){
            return false;
        }
        if (Objects.isNull(friendFromId) || friendFromId <= 0 || Objects.isNull(friendToId) || friendToId <= 0){
            return false;
        }
        if (Objects.isNull(checkType) || !NepFriendshipCheckType.isIllegalFriendshipCheckType(checkType)){
            return false;
        }
        return true;
    }
}
