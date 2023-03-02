package com.fuyusakaiori.nep.im.service.util;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.example.neptune.im.common.entity.request.NepRequestHeader;
import com.example.neptune.im.common.util.NepCheckBaseParamUtil;
import com.fuyusakaiori.nep.im.service.core.user.entity.dto.NepCancelUser;
import com.fuyusakaiori.nep.im.service.core.user.entity.dto.NepRegisterUser;
import com.fuyusakaiori.nep.im.service.core.user.entity.dto.NepQueryUser;
import com.fuyusakaiori.nep.im.service.core.user.entity.dto.NepEditUser;
import com.fuyusakaiori.nep.im.service.core.user.entity.request.*;

import java.util.List;
import java.util.Objects;

public class NepCheckUserParamUtil
{

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
                    || StrUtil.isEmpty(body.getUserNickName())){
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

    public static boolean checkNepCancelUserRequestParam(NepCancelUserRequest request){
        // 1. 获取参数
        NepRequestHeader header = request.getRequestHeader();
        NepCancelUser body = request.getRequestBody();
        // 2. 校验请求头
        if (!NepCheckBaseParamUtil.checkNeptuneRequestBaseParam(header)){
            return false;
        }
        // 3. 校验参数是否合理: 三个参数都为空或者用户 ID 不合法以及其他两个参数为空的情况
        if ((Objects.isNull(body.getUserId()) || (Objects.nonNull(body.getUserId()) && body.getUserId() <= 0))
                    && StrUtil.isEmpty(body.getUserNickName())){
            return false;
        }
        return true;
    }

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
        NepQueryUser body = request.getRequestBody();
        // 2. 校验请求头
        if (!NepCheckBaseParamUtil.checkNeptuneRequestBaseParam(header)){
            return false;
        }
        // 3. 校验查询条件
        return (!Objects.nonNull(body.getUserId()) || body.getUserId() > 0)
                       && (!Objects.nonNull(body.getUserType()) || body.getUserType() >= 0)
                       && (!Objects.nonNull(body.getUserGender()) || body.getUserGender() >= 0);
    }

    public static boolean checkNepQueryUserByIdListRequestParam(NepQueryUserByIdListRequest request){
        // 1. 获取变量
        NepRequestHeader header = request.getRequestHeader();
        List<Integer> body = request.getRequestBody();
        // 2. 校验请求头
        if (!NepCheckBaseParamUtil.checkNeptuneRequestBaseParam(header)){
            return false;
        }
        // 3. 检查集合是否为空
        return CollectionUtil.isNotEmpty(body);
    }

    public static boolean checkNepQueryUserByIdRequestParam(NepQueryUserByIdRequest request){
        // 1. 获取变量
        NepRequestHeader header = request.getRequestHeader();
        Integer userId = request.getUserId();
        // 2. 校验请求头
        if (!NepCheckBaseParamUtil.checkNeptuneRequestBaseParam(header)){
            return false;
        }
        // 3. 检查字段是否合法
        return !Objects.isNull(userId) && userId > 0;
    }


}
