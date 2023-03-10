package com.fuyusakaiori.nep.im.service.util;


import cn.hutool.core.util.StrUtil;
import com.example.nep.im.common.entity.request.NepRequestHeader;
import com.example.nep.im.common.util.NepCheckBaseParamUtil;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.normal.*;

public class NepCheckFriendshipParamUtil {

    /**
     * <h3>校验添加好友关系时的请求</h3>
     */
    public static boolean checkNepAddFriendshipRequestParam(NepAddFriendshipRequest request){
        return NepCheckBaseParamUtil.checkNeptuneRequestBaseParam(request.getRequestHeader())
                       && NepCheckCommonParamUtil.checkFriendshipUniqueId(request.getRequestBody().getFriendFromId(), request.getRequestBody().getFriendToId());
    }


    public static boolean checkNepReleaseFriendshipRequestParam(NeptuneReleaseFriendshipRequest request){
        NepRequestHeader header = request.getRequestHeader();
        Integer friendFromId = request.getFriendFromId();
        Integer friendToId = request.getFriendToId();

        return NepCheckBaseParamUtil.checkNeptuneRequestBaseParam(header)
                       && NepCheckCommonParamUtil.checkFriendshipUniqueId(friendFromId, friendToId);
    }

    public static boolean checkNepReleaseAllFriendshipRequestParam(NeptuneReleaseAllFriendshipRequest request){
        NepRequestHeader header = request.getRequestHeader();
        Integer friendFromId = request.getFriendFromId();
        return NepCheckBaseParamUtil.checkNeptuneRequestBaseParam(header)
                       && NepCheckCommonParamUtil.checkUserUniqueId(friendFromId);
    }

    public static boolean checkNepEditFriendshipRequestParam(NepEditFriendshipRemarkRequest request){
        NepRequestHeader header = request.getRequestHeader();
        Integer friendFromId = request.getFriendFromId();
        Integer friendToId = request.getFriendToId();
        String friendRemark = request.getFriendRemark();
        if (!NepCheckBaseParamUtil.checkNeptuneRequestBaseParam(header)){
            return false;
        }
        if (!NepCheckCommonParamUtil.checkFriendshipUniqueId(friendFromId, friendToId)){
            return false;
        }
        if (StrUtil.isEmpty(friendRemark)){
            return false;
        }
        return true;
    }

}
