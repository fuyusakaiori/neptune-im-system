package com.fuyusakaiori.nep.im.service.util;

import cn.hutool.core.util.StrUtil;
import com.example.nep.im.common.entity.request.NepRequestHeader;
import com.example.nep.im.common.util.NepCheckBaseParamUtil;
import com.fuyusakaiori.nep.im.service.core.user.entity.request.friend.*;

import java.util.Objects;

public class NepCheckFriendParamUtil {


    /**
     * <h3>校验通过账号查询好友的请求</h3>
     */
    public static boolean checkNepQueryFriendByAccountRequestParam(NepQueryFriendByAccountRequest request) {
        // 1. 获取变量
        NepRequestHeader header = request.getRequestHeader();
        Integer friendFromId = request.getFriendFromId();
        String userAccount = request.getUserAccount();
        // 2. 校验请求头
        if (!NepCheckBaseParamUtil.checkNeptuneRequestBaseParam(header)){
            return false;
        }
        // 3. 检查字段是否合法
        if (Objects.isNull(friendFromId) || friendFromId <= 0 || StrUtil.isEmpty(userAccount)){
            return false;
        }
        return true;
    }

    /**
     * <h3>校验通过昵称或者备注查询好友的请求</h3>
     */
    public static boolean checkNepQueryFriendByNameRequestParam(NepQueryFriendByNameRequest request) {
        // 1. 获取变量
        NepRequestHeader header = request.getRequestHeader();
        Integer friendFromId = request.getFriendFromId();
        String friendName = request.getFriendName();
        // 2. 校验请求头
        if (!NepCheckBaseParamUtil.checkNeptuneRequestBaseParam(header)){
            return false;
        }
        // 3. 检查字段是否合法
        if (Objects.isNull(friendFromId) || friendFromId <= 0 || StrUtil.isEmpty(friendName)){
            return false;
        }
        return true;
    }

    /**
     * <h3>校验查询所有好友的请求</h3>
     */
    public static boolean checkNepQueryAllFriendRequestParam(NepQueryAllFriendRequest request) {
        // 1. 获取变量
        NepRequestHeader header = request.getRequestHeader();
        Integer userId = request.getFriendFromId();
        // 2. 校验请求头
        if (!NepCheckBaseParamUtil.checkNeptuneRequestBaseParam(header)){
            return false;
        }
        if (Objects.isNull(userId) || userId < 0){
            return false;
        }
        return true;
    }

    public static boolean checkNepQueryAllFriendBlackListRequestParam(NepQueryAllFriendBlackRequest request) {

        return true;
    }

    public static boolean checkNepQueryAllFriendApplicationRequestParam(NepQueryAllFriendApplicationRequest request) {

        return true;
    }

    public static boolean checkNepQueryAllFriendGroupMemberRequestParam(NepQueryAllFriendGroupMemberRequest request) {
        return true;
    }
}
