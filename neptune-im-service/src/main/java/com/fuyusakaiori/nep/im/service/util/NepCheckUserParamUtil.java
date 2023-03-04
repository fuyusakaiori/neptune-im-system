package com.fuyusakaiori.nep.im.service.util;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.example.neptune.im.common.entity.request.NepRequestHeader;
import com.example.neptune.im.common.util.NepCheckBaseParamUtil;
import com.fuyusakaiori.nep.im.service.core.user.entity.dto.NepRegisterUser;
import com.fuyusakaiori.nep.im.service.core.user.entity.dto.NepEditUser;
import com.fuyusakaiori.nep.im.service.core.user.entity.request.friend.NepQueryAllFriendRequest;
import com.fuyusakaiori.nep.im.service.core.user.entity.request.friend.NepQueryFriendByAccountRequest;
import com.fuyusakaiori.nep.im.service.core.user.entity.request.friend.NepQueryFriendByNameRequest;
import com.fuyusakaiori.nep.im.service.core.user.entity.request.normal.*;

import java.util.List;
import java.util.Objects;

public class NepCheckUserParamUtil {

    /**
     * <h3>校验注册用户的请求</h3>
     */
    public static boolean checkNepRegisterUserRequestParam(NepRegisterUserRequest request){
        // 1. 获取参数
        NepRequestHeader header = request.getRequestHeader();
        NepRegisterUser body = request.getRequestBody();
        // 2. 校验请求头
        if (!NepCheckBaseParamUtil.checkNeptuneRequestBaseParam(header)){
            return false;
        }
        // 3. 校验参数
        if (StrUtil.isEmpty(body.getUserAccount()) || StrUtil.isEmpty(body.getUserPassword())
                    || StrUtil.isEmpty(body.getUserNickName())
                    || Objects.isNull(body.getUserGender())){
            return false;
        }
        return true;
    }

    /**
     * <h3>校验批量注册用户的请求</h3>
     */
    public static boolean checkNepImportUserRequestParam(NepImportUserRequest request){
        // 1. 获取参数
        NepRequestHeader header = request.getRequestHeader();
        List<NepRegisterUser> body = request.getRequestBody();
        // 2. 校验请求头
        if (!NepCheckBaseParamUtil.checkNeptuneRequestBaseParam(header)){
            return false;
        }
        // 3. 校验参数
        if (CollectionUtil.isEmpty(body)){
            return false;
        }
        for (NepRegisterUser user : body) {
            if (StrUtil.isEmpty(user.getUserPassword())
                        || StrUtil.isEmpty(user.getUserNickName())){
                return false;
            }
        }
        return true;
    }

    /**
     * <h3>校验更新用户的请求</h3>
     */
    public static boolean checkNepEditUserRequestParam(NepEditUserRequest request){
        // 1. 获取变量
        NepRequestHeader requestHeader = request.getRequestHeader();
        NepEditUser requestBody = request.getRequestBody();
        // 2. 校验请求头
        if (!NepCheckBaseParamUtil.checkNeptuneRequestBaseParam(requestHeader)){
            return false;
        }
        // 3. 校验更新数据
        return !Objects.isNull(requestBody.getUserId()) && requestBody.getUserId() >= 0;
    }

    /**
     * <h3>校验注销用户的请求</h3>
     */
    public static boolean checkNepCancelUserRequestParam(NepCancelUserRequest request){
        // 1. 获取变量
        NepRequestHeader header = request.getRequestHeader();
        Integer userId = request.getUserId();
        // 2. 校验请求头
        if (!NepCheckBaseParamUtil.checkNeptuneRequestBaseParam(header)){
            return false;
        }
        if (Objects.isNull(userId) || userId < 0){
            return false;
        }
        return true;
    }

    /**
     * <h3>校验通过账号查询用户的请求</h3>
     */
    public static boolean checkNepQueryUserByAccountRequestParam(NepQueryUserByAccountRequest request){
        // 1. 获取变量
        NepRequestHeader header = request.getRequestHeader();
        String userAccount = request.getUserAccount();
        // 2. 校验请求头
        if (!NepCheckBaseParamUtil.checkNeptuneRequestBaseParam(header)){
            return false;
        }
        // 3. 检查字段是否合法
        return !StrUtil.isEmpty(userAccount);
    }

    /**
     * <h3>校验通过昵称查询用户的请求</h3>
     */
    public static boolean checkNepQueryUserByNickNameRequestParam(NepQueryUserByNickNameRequest request){
        // 1. 获取变量
        NepRequestHeader header = request.getRequestHeader();
        String nickName = request.getNickName();
        // 2. 校验请求头
        if (!NepCheckBaseParamUtil.checkNeptuneRequestBaseParam(header)){
            return false;
        }
        // 3. 检查字段是否合法
        return !StrUtil.isEmpty(nickName);
    }

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
}
