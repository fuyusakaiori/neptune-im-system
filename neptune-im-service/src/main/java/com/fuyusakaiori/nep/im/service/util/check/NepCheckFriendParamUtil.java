package com.fuyusakaiori.nep.im.service.util.check;

import cn.hutool.core.util.StrUtil;
import com.example.nep.im.common.entity.request.NepRequestHeader;
import com.example.nep.im.common.util.NepCheckBaseParamUtil;
import com.fuyusakaiori.nep.im.service.core.user.entity.request.friend.*;

import java.util.Objects;

public class NepCheckFriendParamUtil {


    /**
     * <h3>校验通过账号查询好友的请求</h3>
     */
    public static boolean checkNepQueryFriendByAccountRequestParam(NepQueryFriendRequest request) {
        // 1. 获取变量
        NepRequestHeader header = request.getHeader();
        Integer friendFromId = request.getFriendFromId();
        String username = request.getUsername();
        String nickname = request.getNickname();
        String remark = request.getFriendRemark();
        // 2. 校验请求头
        if (!NepCheckBaseParamUtil.checkNeptuneRequestBaseParam(header)){
            return false;
        }
        // 3. 检查字段是否合法
        if (Objects.isNull(friendFromId) || friendFromId <= 0){
            return false;
        }
        // 4. 检查查询条件是否都为空
        if (StrUtil.isEmpty(username) || StrUtil.isEmpty(nickname) || StrUtil.isEmpty(remark)){
            return false;
        }
        return true;
    }

    /**
     * <h3>校验查询所有好友的请求</h3>
     */
    public static boolean checkNepQueryAllFriendRequestParam(NepQueryAllFriendRequest request) {
        // 1. 获取变量
        NepRequestHeader header = request.getHeader();
        Integer userId = request.getFriendFromId();
        // 2. 校验请求头
        if (!NepCheckBaseParamUtil.checkNeptuneRequestBaseParam(header)){
            return false;
        }
        if (Objects.isNull(userId) || userId <= 0){
            return false;
        }
        return true;
    }

    public static boolean checkNepQueryAllFriendApplicationRequestParam(NepQueryAllFriendApplicationRequest request) {
        // 1. 获取变量
        NepRequestHeader header = request.getHeader();
        Integer userId = request.getUserId();
        // 2. 校验请求头
        if (!NepCheckBaseParamUtil.checkNeptuneRequestBaseParam(header)){
            return false;
        }
        if (Objects.isNull(userId) || userId <= 0){
            return false;
        }
        return true;
    }

    public static boolean checkNepQueryAllFriendGroupMemberRequestParam(NepQueryAllFriendGroupMemberRequest request) {
        // 1. 获取变量
        NepRequestHeader header = request.getHeader();
        Integer userId = request.getUserId();
        // 2. 校验请求头
        if (!NepCheckBaseParamUtil.checkNeptuneRequestBaseParam(header)){
            return false;
        }
        if (Objects.isNull(userId) || userId <= 0){
            return false;
        }
        return true;
    }
}
