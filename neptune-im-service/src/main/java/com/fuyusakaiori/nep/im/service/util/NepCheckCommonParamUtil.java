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


}
