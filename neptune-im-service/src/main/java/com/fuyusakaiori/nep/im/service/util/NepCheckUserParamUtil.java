package com.fuyusakaiori.nep.im.service.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.example.neptune.im.common.entity.request.NepRequestHeader;
import com.example.neptune.im.common.util.NepCheckBaseParamUtil;
import com.fuyusakaiori.nep.im.service.core.user.entity.dto.NepRegisterUser;
import com.fuyusakaiori.nep.im.service.core.user.entity.dto.NepEditUser;
import com.fuyusakaiori.nep.im.service.core.user.entity.request.*;
import com.fuyusakaiori.nep.im.service.core.user.entity.request.friend.NepQueryAllFriendUserRequest;
import com.fuyusakaiori.nep.im.service.core.user.entity.request.friend.NepQueryFriendUserRequest;

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
        if (StrUtil.isEmpty(body.getUserPassword())
                    || StrUtil.isEmpty(body.getUserNickName())
                    || Objects.isNull(body.getUserGender())
                    || Objects.isNull(body.getUserType())){
            return false;
        }
        return true;
    }

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

    public static boolean checkNepQueryUserRequestParam(NepQueryUserRequest request){
        // 1. 获取变量
        NepRequestHeader header = request.getRequestHeader();
        Integer userId = request.getUserId();
        String nickName = request.getNickName();
        // 2. 校验请求头
        if (!NepCheckBaseParamUtil.checkNeptuneRequestBaseParam(header)){
            return false;
        }
        // 3. 检查字段是否合法
        if ((Objects.isNull(userId) || userId < 0) && StrUtil.isEmpty(nickName)){
            return false;
        }
        return true;
    }

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

    public static boolean checkNepQueryFriendUserRequestParam(NepQueryFriendUserRequest request) {
        // 1. 获取变量
        NepRequestHeader header = request.getRequestHeader();
        Integer friendFromId = request.getFriendFromId();
        Integer friendToId = request.getFriendToId();
        String friendName = request.getFriendName();
        // 2. 校验请求头
        if (!NepCheckBaseParamUtil.checkNeptuneRequestBaseParam(header)){
            return false;
        }
        // 3. 检查字段是否合法
        if (Objects.isNull(friendFromId)){
            return false;
        }
        return (!Objects.isNull(friendToId) && friendToId > 0) || !Objects.isNull(friendName);
    }

    public static boolean checkNepQueryAllFriendUserRequestParam(NepQueryAllFriendUserRequest request) {
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
