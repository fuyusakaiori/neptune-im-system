package com.fuyusakaiori.nep.im.service.util.check;

import cn.hutool.core.util.StrUtil;
import com.example.nep.im.common.entity.request.NepRequestHeader;
import com.example.nep.im.common.enums.status.NepGroupEnterType;
import com.example.nep.im.common.enums.status.NepGroupExitType;
import com.example.nep.im.common.enums.status.NepGroupMemberQueryType;
import com.example.nep.im.common.enums.status.NepGroupMemberType;
import com.example.nep.im.common.util.NepCheckBaseParamUtil;
import com.fuyusakaiori.nep.im.service.core.group.entity.request.*;

import java.util.Objects;

public class NepCheckGroupMemberParamUtil {

    public static boolean checkNepAddGroupMemberRequestParam(NepAddGroupMemberRequest request) {
        NepRequestHeader header = request.getHeader();
        Integer groupId = request.getGroupId();
        Integer groupMemberId = request.getGroupMemberId();
        Integer groupMemberEnterType = request.getGroupMemberEnterType();
        if(!NepCheckBaseParamUtil.checkNeptuneRequestBaseParam(header)){
            return false;
        }
        if (Objects.isNull(groupId) || groupId <= 0 || Objects.isNull(groupMemberId) || groupMemberId <= 0){
            return false;
        }
        if (Objects.isNull(groupMemberEnterType) || !NepGroupEnterType.isIllegalGroupEnterType(groupMemberEnterType)){
            return false;
        }
        return true;
    }

    public static boolean checkNepUpdateGroupMemberRequestParam(NepUpdateGroupMemberRequest request) {
        NepRequestHeader header = request.getHeader();
        Integer groupId = request.getGroupId();
        Integer groupMemberId = request.getGroupMemberId();
        String nickname = request.getNickname();
        if(!NepCheckBaseParamUtil.checkNeptuneRequestBaseParam(header)){
            return false;
        }
        if (Objects.isNull(groupId) || groupId <= 0 || Objects.isNull(groupMemberId) || groupMemberId <= 0){
            return false;
        }
        if (StrUtil.isEmpty(nickname)){
            return false;
        }
        return true;
    }

    public static boolean checkNepChangeGroupMemberTypeRequestParam(NepChangeGroupMemberTypeRequest request) {
        NepRequestHeader header = request.getHeader();
        Integer groupId = request.getGroupId();
        Integer groupMemberId = request.getGroupMemberId();
        Integer groupMemberType = request.getGroupMemberType();
        if(!NepCheckBaseParamUtil.checkNeptuneRequestBaseParam(header)){
            return false;
        }
        if (Objects.isNull(groupId) || groupId <= 0 || Objects.isNull(groupMemberId) || groupMemberId <= 0){
            return false;
        }
        if (Objects.isNull(groupMemberType) || NepGroupMemberType.isIllegalGroupMemberType(groupMemberType)){
            return false;
        }
        return true;
    }

    public static boolean checkNepMuteGroupMemberRequestParam(NepMuteGroupMemberRequest request) {
        NepRequestHeader header = request.getHeader();
        Integer groupId = request.getGroupId();
        Integer groupMemberId = request.getGroupMemberId();
        Long muteEndTime = request.getMuteEndTime();
        if(!NepCheckBaseParamUtil.checkNeptuneRequestBaseParam(header)){
            return false;
        }
        if (Objects.isNull(groupId) || groupId <= 0 || Objects.isNull(groupMemberId) || groupMemberId <= 0){
            return false;
        }
        if (Objects.isNull(muteEndTime) || muteEndTime < System.currentTimeMillis()){
            return false;
        }
        return true;
    }

    public static boolean checkNepRevokeGroupMemberRequestParam(NepRevokeGroupMemberRequest request) {
        NepRequestHeader header = request.getHeader();
        Integer groupId = request.getGroupId();
        Integer groupMemberId = request.getGroupMemberId();
        if(!NepCheckBaseParamUtil.checkNeptuneRequestBaseParam(header)){
            return false;
        }
        if (Objects.isNull(groupId) || groupId <= 0 || Objects.isNull(groupMemberId) || groupMemberId <= 0){
            return false;
        }
        return true;
    }

    public static boolean checkNepExitGroupMemberRequestParam(NepExitGroupMemberRequest request) {
        NepRequestHeader header = request.getHeader();
        Integer groupId = request.getGroupId();
        Integer groupMemberId = request.getGroupMemberId();
        Integer groupMemberExitType = request.getGroupMemberExitType();
        if(!NepCheckBaseParamUtil.checkNeptuneRequestBaseParam(header)){
            return false;
        }
        if (Objects.isNull(groupId) || groupId <= 0 || Objects.isNull(groupMemberId) || groupMemberId <= 0){
            return false;
        }
        if (Objects.isNull(groupMemberExitType) || NepGroupExitType.isIllegalGroupExitType(groupMemberExitType)){
            return false;
        }
        return true;
    }

    public static boolean checkNepQueryAllGroupMemberRequestParam(NepQueryAllGroupMemberRequest request) {
        NepRequestHeader header = request.getHeader();
        Integer groupId = request.getGroupId();
        Integer queryType = request.getQueryType();
        if(!NepCheckBaseParamUtil.checkNeptuneRequestBaseParam(header)){
            return false;
        }
        if (Objects.isNull(groupId) || groupId <= 0 || Objects.isNull(queryType) || NepGroupMemberQueryType.isIllegalGroupMemberQueryType(queryType)){
            return false;
        }
        return true;
    }
}
