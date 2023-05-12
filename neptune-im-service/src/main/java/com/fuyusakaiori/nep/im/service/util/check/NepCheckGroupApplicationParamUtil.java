package com.fuyusakaiori.nep.im.service.util.check;

import cn.hutool.core.util.StrUtil;
import com.example.nep.im.common.entity.request.NepRequestHeader;
import com.example.nep.im.common.enums.status.NepGroupApplicationApproveStatus;
import com.example.nep.im.common.util.NepCheckBaseParamUtil;
import com.fuyusakaiori.nep.im.service.core.group.entity.request.NepApproveGroupApplicationRequest;
import com.fuyusakaiori.nep.im.service.core.group.entity.request.NepQueryGroupApplicationRequest;
import com.fuyusakaiori.nep.im.service.core.group.entity.request.NepSendGroupApplicationRequest;

import java.util.Objects;

public class NepCheckGroupApplicationParamUtil {


    public static boolean checkNepSendGroupApplicationRequestParam(NepSendGroupApplicationRequest request) {
        NepRequestHeader header = request.getHeader();
        Integer userId = request.getGroupApplySenderId();
        Integer groupId = request.getGroupId();
        String additionalInfo = request.getGroupApplyAdditionalInfo();
        String source = request.getGroupApplySource();
        if(!NepCheckBaseParamUtil.checkNeptuneRequestBaseParam(header)){
            return false;
        }
        if (Objects.isNull(userId) || userId <= 0 || Objects.isNull(groupId) || groupId <= 0){
            return false;
        }
        if (StrUtil.isEmpty(additionalInfo) || StrUtil.isEmpty(source)){
            return false;
        }
        return true;
    }

    public static boolean checkNepApproveGroupApplicationRequestParam(NepApproveGroupApplicationRequest request) {
        NepRequestHeader header = request.getHeader();
        Integer applyId = request.getApplyId();
        Integer userId = request.getUserId();
        Integer status = request.getStatus();
        if (!NepCheckBaseParamUtil.checkNeptuneRequestBaseParam(header)){
            return false;
        }
        if (Objects.isNull(applyId) || applyId <= 0 || Objects.isNull(userId) || userId <= 0){
            return false;
        }
        if (Objects.isNull(status) || !NepGroupApplicationApproveStatus.isIllegalStatus(status)){
            return false;
        }
        return true;
    }

    public static boolean checkNepQueryGroupApplicationRequestParam(NepQueryGroupApplicationRequest request) {
        NepRequestHeader header = request.getHeader();
        Integer userId = request.getUserId();
        if (!NepCheckBaseParamUtil.checkNeptuneRequestBaseParam(header)){
            return false;
        }
        if (Objects.isNull(userId) || userId <= 0){
            return false;
        }
        return true;
    }
}
