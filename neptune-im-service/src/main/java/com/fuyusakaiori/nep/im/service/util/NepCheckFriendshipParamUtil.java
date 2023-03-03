package com.fuyusakaiori.nep.im.service.util;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.example.neptune.im.common.entity.request.NepRequestHeader;
import com.example.neptune.im.common.util.NepCheckBaseParamUtil;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.dto.NepAddFriendship;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.normal.*;

import java.util.Objects;

public class NepCheckFriendshipParamUtil
{

    public static boolean checkNepQueryFriendshipByIdRequestParam(NepQueryFriendshipByIdRequest request){
        // 0. 获取变量
        NepRequestHeader header = request.getRequestHeader();
        Integer fromId = request.getFriendFromId(), toId = request.getFriendToId();
        // 1. 校验请求头
        if (!NepCheckBaseParamUtil.checkNeptuneRequestBaseParam(header)){
            return false;
        }
        // 2. 校验用户 ID
        return !Objects.isNull(fromId) && !Objects.isNull(toId)
                       && fromId > 0 && toId > 0;
    }

    public static boolean checkNepQueryAllFriendshipRequestParam(NepQueryAllFriendshipRequest request){
        // 0. 获取变量
        NepRequestHeader header = request.getRequestHeader();
        Integer fromId = request.getFriendFromId();
        // 1. 校验请求头
        if (!NepCheckBaseParamUtil.checkNeptuneRequestBaseParam(header)){
            return false;
        }
        return !Objects.isNull(fromId) && fromId >= 0;
    }

    public static boolean checkNepAddFriendshipRequestParam(NepAddFriendshipRequest request){
        NepRequestHeader header = request.getRequestHeader();
        NepAddFriendship body = request.getRequestBody();
        Integer fromId = body.getFriendFromId();
        Integer toId = body.getFriendToId();
        // 注: 直接调用另一个校验方法校验参数
        return checkNepQueryFriendshipByIdRequestParam(
                new NepQueryFriendshipByIdRequest().setRequestHeader(header)
                        .setFriendFromId(fromId).setFriendToId(toId));
    }


    public static boolean checkNepReleaseFriendshipRequestParam(NeptuneReleaseFriendshipRequest request){
        return checkNepQueryFriendshipByIdRequestParam(
                BeanUtil.copyProperties(request, NepQueryFriendshipByIdRequest.class));
    }

    public static boolean checkNepReleaseAllFriendshipRequestParam(NeptuneReleaseAllFriendshipRequest request){
        return checkNepQueryAllFriendshipRequestParam(
                BeanUtil.copyProperties(request, NepQueryAllFriendshipRequest.class));
    }

    public static boolean checkNepEditFriendshipRequestParam(NepEditFriendshipRequest request){
        return checkNepQueryFriendshipByIdRequestParam(new NepQueryFriendshipByIdRequest()
                                                                   .setRequestHeader(request.getRequestHeader())
                                                                   .setFriendFromId(request.getRequestBody().getFriendFromId())
                                                                   .setFriendToId(request.getRequestBody().getFriendFromId()));
    }

}
