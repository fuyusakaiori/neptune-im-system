package com.fuyusakaiori.nep.im.service.util.check;

import cn.hutool.core.util.StrUtil;
import com.example.nep.im.common.entity.request.NepRequestHeader;
import com.example.nep.im.common.check.NepCheckBaseParamUtil;
import com.fuyusakaiori.nep.im.service.core.user.entity.request.normal.*;

import java.util.Objects;

public class NepCheckUserParamUtil {

    /**
     * <h3>校验注册用户的请求</h3>
     */
    public static boolean checkNepRegisterUserRequestParam(NepRegisterUserRequest request){
        // 1. 获取参数
        NepRequestHeader header = request.getHeader();
        String username = request.getUsername();
        String password = request.getPassword();
        Integer gender = request.getGender();
        // 2. 校验请求头
        if (!NepCheckBaseParamUtil.checkNeptuneRequestBaseParam(header)){
            return false;
        }
        // 3. 校验参数
        if (StrUtil.isEmpty(username) || StrUtil.isEmpty(password) || Objects.isNull(gender)){
            return false;
        }
        return true;
    }

    /**
     * <h3>校验更新用户的请求</h3>
     */
    public static boolean checkNepEditUserRequestParam(NepEditUserInfoRequest request){
        // 1. 获取变量
        NepRequestHeader requestHeader = request.getHeader();
        Integer userId = request.getUserId();
        // 2. 校验请求头
        if (!NepCheckBaseParamUtil.checkNeptuneRequestBaseParam(requestHeader)){
            return false;
        }
        // 3. 校验用户 ID
        if(Objects.isNull(userId) || userId <= 0){
            return false;
        }
        return true;
    }

    /**
     * <h3>校验更新头像的请求</h3>
     */
    public static boolean checkNepEditUserAvatarRequestParam(NepEditUserAvatarRequest request) {
        return true;
    }


    /**
     * <h3>校验注销用户的请求</h3>
     */
    public static boolean checkNepCancelUserRequestParam(NepCancelUserRequest request){
        // 1. 获取变量
        NepRequestHeader requestHeader = request.getHeader();
        Integer userId = request.getUserId();
        // 2. 校验请求头
        if (!NepCheckBaseParamUtil.checkNeptuneRequestBaseParam(requestHeader)){
            return false;
        }
        // 3. 校验用户 ID
        if(Objects.isNull(userId) || userId <= 0){
            return false;
        }
        return true;
    }

    /**
     * <h3>校验通过账号查询用户的请求</h3>
     */
    public static boolean checkNepQueryUserByAccountRequestParam(NepQueryUserByUserNameRequest request){
        // 1. 获取变量
        NepRequestHeader header = request.getHeader();
        String userAccount = request.getUsername();
        // 2. 校验请求头
        if (!NepCheckBaseParamUtil.checkNeptuneRequestBaseParam(header)){
            return false;
        }
        // 3. 校验用户账号是否合法
        return !StrUtil.isEmpty(userAccount);
    }

    public static boolean checkNepQueryUserByNickNameRequestParam(NepQueryUserByNickNameRequest request){
        // 1. 获取变量
        NepRequestHeader header = request.getHeader();
        String userNickName = request.getUserNickName();
        // 2. 校验请求头
        if (!NepCheckBaseParamUtil.checkNeptuneRequestBaseParam(header)){
            return false;
        }
        // 3. 校验用户昵称是否合法
        return !StrUtil.isEmpty(userNickName);
    }

    public static boolean checkNepLoginUserInImSystemRequestParam(NepLoginUserRequest request) {
        // 1. 获取变量
        NepRequestHeader header = request.getHeader();
        String username = request.getUsername();
        String password = request.getPassword();
        // 2. 校验请求头
        if (!NepCheckBaseParamUtil.checkNeptuneRequestBaseParam(header)){
            return false;
        }
        // 3. 校验账号密码
        if (StrUtil.isEmpty(username) || StrUtil.isEmpty(password)){
            return false;
        }
        return true;
    }
}
