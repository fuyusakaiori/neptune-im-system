package com.fuyusakaiori.nep.im.service.util.check;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.example.nep.im.common.check.NepCheckCommonParamUtil;
import com.example.nep.im.common.entity.request.NepRequestHeader;
import com.example.nep.im.common.check.NepCheckBaseParamUtil;
import com.fuyusakaiori.nep.im.service.core.user.entity.dto.NepRegisterUser;
import com.fuyusakaiori.nep.im.service.core.user.entity.request.normal.*;

import java.util.List;

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
                    || StrUtil.isEmpty(body.getUserNickName())){
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
        Integer userId = request.getRequestBody().getUserId();
        // 2. 校验请求头
        if (!NepCheckBaseParamUtil.checkNeptuneRequestBaseParam(requestHeader)){
            return false;
        }
        // 3. 校验用户 ID
        return NepCheckCommonParamUtil.checkUserUniqueId(userId);
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
        // 3. 校验用户 ID
        return NepCheckCommonParamUtil.checkUserUniqueId(userId);
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
        // 3. 校验用户账号是否合法
        return !StrUtil.isEmpty(userAccount);
    }

    public static boolean checkNepQueryUserByNickNameRequestParam(NepQueryUserByNickNameRequest request){
        // 1. 获取变量
        NepRequestHeader header = request.getRequestHeader();
        String userNickName = request.getUserNickName();
        // 2. 校验请求头
        if (!NepCheckBaseParamUtil.checkNeptuneRequestBaseParam(header)){
            return false;
        }
        // 3. 校验用户昵称是否合法
        return !StrUtil.isEmpty(userNickName);
    }
}
