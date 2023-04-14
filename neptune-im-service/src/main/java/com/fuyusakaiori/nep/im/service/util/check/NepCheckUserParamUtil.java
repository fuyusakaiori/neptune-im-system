package com.fuyusakaiori.nep.im.service.util.check;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.example.nep.im.common.entity.request.NepRequestHeader;
import com.example.nep.im.common.enums.status.NepFriendshipAllowType;
import com.example.nep.im.common.enums.status.NepUserGenderType;
import com.example.nep.im.common.util.NepCheckBaseParamUtil;
import com.fuyusakaiori.nep.im.service.core.user.entity.request.normal.*;

import java.lang.reflect.Field;
import java.util.Objects;

public class NepCheckUserParamUtil {

    /**
     * <h3>校验用户登录系统的请求</h3>
     */
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
        // 3. 校验用户名和密码是否为空
        if (StrUtil.isEmpty(username) || StrUtil.isEmpty(password)){
            return false;
        }
        // 4. 校验性别是否合法
        if (Objects.isNull(gender) || !NepUserGenderType.isIllegalGender(gender)){
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
        Integer age = request.getAge();
        Integer gender = request.getGender();
        Integer friendshipAllowType = request.getFriendshipAllowType();
        Long birthday = request.getBirthday();
        // 2. 至少更新一个字段
        try {
            boolean isFieldNull = true;
            for (Field field : NepEditUserInfoRequest.class.getDeclaredFields()) {
                // 2.1 设置字段的访问权限
                field.setAccessible(true);
                // 2.2 如果这个字段不为空那么直接跳出
                if (Objects.nonNull(field.get(request))){
                    isFieldNull = false;
                    break;
                }
            }
            // 2.3 如果所有字段都为空, 那么直接返回
            if (isFieldNull){
                return false;
            }
        } catch (IllegalAccessException exception) {
            throw new RuntimeException(exception);
        }
        // 3. 校验请求头
        if (!NepCheckBaseParamUtil.checkNeptuneRequestBaseParam(requestHeader)){
            return false;
        }
        // 4. 校验用户 ID
        if (Objects.isNull(userId) || userId <= 0){
            return false;
        }
        // 5. 校验年龄字段是否合法
        if (Objects.nonNull(age) && (age <= 0 || age > 200)){
            return false;
        }
        // 6. 校验性别字段是否合法
        if (Objects.nonNull(gender) && !NepUserGenderType.isIllegalGender(gender)) {
            return false;
        }
        // 7. 校验生日字段是否合法
        if (Objects.nonNull(birthday)
                    && (birthday <= DateUtil.parse("1900-01-01").getTime()
                                || birthday >= System.currentTimeMillis())){
            return false;
        }
        // 8. 校验添加好友方式是否为空
        if (Objects.nonNull(friendshipAllowType)
                    && !NepFriendshipAllowType.isIllegalFriendshipAllowType(friendshipAllowType)){
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
     * <h3>校验通过用户 ID 查询用户的请求</h3>
     */
    public static boolean checkNepQueryUserRequestParam(NepQueryWillBeFriendByIdRequest request) {
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
     * <h3>校验通过账号和昵称查询用户的请求</h3>
     */
    public static boolean checkNepQueryWillBeFriendRequestParam(NepQueryWillBeFriendRequest request){
        // 1. 获取变量
        NepRequestHeader header = request.getHeader();
        Integer userId = request.getUserId();
        String username = request.getUsername();
        String nickname = request.getNickname();
        // 2. 校验请求头
        if (!NepCheckBaseParamUtil.checkNeptuneRequestBaseParam(header)){
            return false;
        }
        if (Objects.isNull(userId) || userId <= 0){
            return false;
        }
        if (StrUtil.isEmpty(username) && StrUtil.isEmpty(nickname)){
            return false;
        }
        return true;
    }
}
