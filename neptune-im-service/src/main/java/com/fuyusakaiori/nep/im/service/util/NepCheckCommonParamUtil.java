package com.fuyusakaiori.nep.im.service.util;

import java.util.Objects;

/**
 * <h3>校验请求的通用工具类</h3>
 */
public class NepCheckCommonParamUtil {

    /**
     * <h3>校验用户 ID 是否为空</h3>
     */
    public static boolean checkUserUniqueId(Integer userId){
        return !Objects.isNull(userId) && userId > 0;
    }

    /**
     * <h3>校验好友关系的双方 ID是否为空</h3>
     */
    public static boolean checkFriendshipUniqueId(Integer friendFromId, Integer friendToId){
        return !Objects.isNull(friendFromId) && friendFromId > 0 && !Objects.isNull(friendToId) && friendToId > 0;
    }


}
