package com.fuyusakaiori.neptune.im.service.util;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.example.neptune.im.common.entity.request.NeptuneRequestHeader;
import com.example.neptune.im.common.util.NeptuneCheckBaseParamUtil;
import com.fuyusakaiori.neptune.im.service.core.user.entity.dto.NeptuneCancelUser;
import com.fuyusakaiori.neptune.im.service.core.user.entity.dto.NeptuneRegisterUser;
import com.fuyusakaiori.neptune.im.service.core.user.entity.dto.NeptuneQueryUser;
import com.fuyusakaiori.neptune.im.service.core.user.entity.dto.NeptuneEditUser;
import com.fuyusakaiori.neptune.im.service.core.user.entity.request.NeptuneCancelUserRequest;
import com.fuyusakaiori.neptune.im.service.core.user.entity.request.NeptuneRegisterUserRequest;
import com.fuyusakaiori.neptune.im.service.core.user.entity.request.NeptuneQueryUserRequest;
import com.fuyusakaiori.neptune.im.service.core.user.entity.request.NeptuneEditUserRequest;

import java.util.List;
import java.util.Objects;

public class NeptuneCheckUserParamUtil {

    public static boolean checkNeptuneRegisterUserRequestParam(NeptuneRegisterUserRequest request){
        // 1. 获取参数
        NeptuneRequestHeader header = request.getRequestHeader();
        List<NeptuneRegisterUser> body = request.getRequestBody();
        // 2. 校验请求头
        if (!NeptuneCheckBaseParamUtil.checkNeptuneRequestBaseParam(header)){
            return false;
        }
        // 3. 校验集合是否为空
        if (CollectionUtil.isEmpty(body)){
            return false;
        }
        // 4. 校验集合中每个新增用户的参数是否合理
        for (NeptuneRegisterUser user : body) {
            // 4.1 校验用户昵称
            if (StrUtil.isEmpty(user.getUserNickName())){
                return false;
            }
            // 4.2 其余参数都可以不填: 因为有些参数是在注册的时候不必要的, 有些参数则是有默认值的
        }
        return true;
    }

    public static boolean checkNeptuneCancelUserRequestParam(NeptuneCancelUserRequest request){
        // 1. 获取参数
        NeptuneRequestHeader header = request.getRequestHeader();
        List<NeptuneCancelUser> body = request.getRequestBody();
        // 2. 校验请求头
        if (!NeptuneCheckBaseParamUtil.checkNeptuneRequestBaseParam(header)){
            return false;
        }
        // 3. 校验集合是否为空
        if (CollectionUtil.isEmpty(body)){
            return false;
        }
        // 4. 校验每个参数是否合理
        for (NeptuneCancelUser user : body){
            if (Objects.isNull(user.getUserId()) && StrUtil.isEmpty(user.getUserNickName())){
                return false;
            }
            if (Objects.nonNull(user.getUserId()) && user.getUserId() < 0){
                return false;
            }
        }
        return true;
    }

    public static boolean checkNeptuneEditUserRequestParam(NeptuneEditUserRequest request){
        // 1. 获取变量
        NeptuneRequestHeader requestHeader = request.getRequestHeader();
        NeptuneEditUser requestBody = request.getRequestBody();
        // 2. 校验请求头
        if (!NeptuneCheckBaseParamUtil.checkNeptuneRequestBaseParam(requestHeader)){
            return false;
        }
        // 3. 校验更新数据
        return !Objects.isNull(requestBody.getUserId()) && requestBody.getUserId() >= 0;
    }

    public static boolean checkNeptuneQueryUserRequestParam(NeptuneQueryUserRequest request){
        // 1. 获取变量
        NeptuneRequestHeader header = request.getRequestHeader();
        NeptuneQueryUser body = request.getRequestBody();
        // 2. 校验请求头
        if (!NeptuneCheckBaseParamUtil.checkNeptuneRequestBaseParam(header)){
            return false;
        }
        // 3. 校验查询条件
        return (!Objects.nonNull(body.getUserId()) || body.getUserId() > 0)
                       && (!Objects.nonNull(body.getUserType()) || body.getUserType() >= 0)
                       && (!Objects.nonNull(body.getUserGender()) || body.getUserGender() >= 0);
    }


}
