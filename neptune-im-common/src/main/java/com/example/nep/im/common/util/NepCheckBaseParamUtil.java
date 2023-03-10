package com.example.nep.im.common.util;


import com.example.nep.im.common.entity.request.NepRequestHeader;

import java.util.Objects;

/**
 * <h2>校验请求头参数</h2>
 */
public class NepCheckBaseParamUtil
{

    /**
     * <h3>校验请求头中的 APP ID 是否为空</h3>
     */
    public static boolean checkNeptuneRequestBaseParam(NepRequestHeader request){
        // 1. 如果传入的上层应用 ID 不合理直接返回 false
        if (Objects.isNull(request.getAppId()) || request.getAppId() < 0){
            return false;
        }
        // TODO 2. 后续还会有其他参数需要校验


        return true;
    }
}
