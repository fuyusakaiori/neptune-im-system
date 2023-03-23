package com.fuyusakaiori.nep.im.service.util.check;

import cn.hutool.core.util.StrUtil;
import com.example.nep.im.common.entity.request.NepRequestHeader;
import com.example.nep.im.common.enums.status.NepGroupJoinType;
import com.example.nep.im.common.enums.status.NepGroupType;
import com.example.nep.im.common.util.NepCheckBaseParamUtil;
import com.fuyusakaiori.nep.im.service.core.group.entity.request.*;

import java.util.Objects;

public class NepCheckGroupParamUtil {


    public static boolean checkNepCreateGroupRequestParam(NepCreateGroupRequest request) {
        NepRequestHeader header = request.getHeader();
        String groupName = request.getGroupName();
        Integer groupOwnerId = request.getGroupOwnerId();
        Integer groupType = request.getGroupType();
        String groupNumber = request.getGroupNumber();
        if (!NepCheckBaseParamUtil.checkNeptuneRequestBaseParam(header)){
            return false;
        }
        if (StrUtil.isEmpty(groupName) || Objects.isNull(groupOwnerId) || groupOwnerId <= 0 || StrUtil.isEmpty(groupNumber)){
            return false;
        }
        if (Objects.isNull(groupType) || !NepGroupType.isIllegalGroupType(groupType)){
            return false;
        }
        return true;
    }

    public static boolean checkNepEditGroupRequestParam(NepEditGroupRequest request) {
        NepRequestHeader header = request.getHeader();
        Integer userId = request.getUserId();
        Integer groupId = request.getGroupId();
        Integer groupApplyType = request.getGroupApplyType();
        String groupName = request.getGroupName();
        String groupBriefInfo = request.getGroupBriefInfo();
        if (!NepCheckBaseParamUtil.checkNeptuneRequestBaseParam(header)){
            return false;
        }
        if (Objects.isNull(groupId) || groupId <= 0 || Objects.isNull(userId) || userId <= 0){
            return false;
        }
        if (Objects.isNull(groupApplyType) || !NepGroupJoinType.isIllegalGroupApplyType(groupApplyType)){
            return false;
        }
        if (StrUtil.isEmpty(groupName) && StrUtil.isEmpty(groupBriefInfo)){
            return false;
        }
        return true;
    }

    public static boolean checkNepDissolveGroupRequestParam(NepDissolveGroupRequest request) {
        NepRequestHeader header = request.getHeader();
        Integer groupId = request.getGroupId();
        Integer userId = request.getUserId();
        if (!NepCheckBaseParamUtil.checkNeptuneRequestBaseParam(header)){
            return false;
        }
        if (Objects.isNull(groupId) || groupId <= 0 || Objects.isNull(userId) || userId <= 0){
            return false;
        }
        return true;
    }

    public static boolean checkNepMuteGroupRequestParam(NepMuteGroupRequest request) {
        NepRequestHeader header = request.getHeader();
        Integer userId = request.getUserId();
        Integer groupId = request.getGroupId();
        if (!NepCheckBaseParamUtil.checkNeptuneRequestBaseParam(header)){
            return false;
        }
        if (Objects.isNull(groupId) || groupId <= 0 || Objects.isNull(userId) || userId <= 0){
            return false;
        }
        return true;
    }

    public static boolean checkNepTransferGroupOwnerRequestParam(NepTransferGroupOwnerRequest request) {
        NepRequestHeader header = request.getHeader();
        Integer userId = request.getUserId();
        Integer groupId = request.getGroupId();
        Integer targetOwnerId = request.getTargetOwnerId();
        if (!NepCheckBaseParamUtil.checkNeptuneRequestBaseParam(header)){
            return false;
        }
        if (Objects.isNull(userId) || userId <= 0){
            return false;
        }
        if (Objects.isNull(groupId) || groupId <= 0 || Objects.isNull(targetOwnerId) || targetOwnerId <= 0){
            return false;
        }
        return true;
    }

    public static boolean checkNepQueryGroupRequestParam(NepQueryGroupRequest request) {
        NepRequestHeader header = request.getHeader();
        String groupName = request.getGroupName();
        String groupNumber = request.getGroupNumber();
        if (!NepCheckBaseParamUtil.checkNeptuneRequestBaseParam(header)){
            return false;
        }
        if (StrUtil.isEmpty(groupName) && StrUtil.isEmpty(groupNumber)){
            return false;
        }
        return true;
    }

    public static boolean checkNepQueryAllJoinedGroupRequestParam(NepQueryAllJoinedGroupRequest request) {
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

    public static boolean checkNepUploadGroupAvatarRequestParam(NepUploadGroupAvatarRequest request) {
        NepRequestHeader header = request.getHeader();
        Integer groupId = request.getGroupId();
        String groupAvatarAddress = request.getGroupAvatarAddress();
        if (!NepCheckBaseParamUtil.checkNeptuneRequestBaseParam(header)){
            return false;
        }
        if (Objects.isNull(groupId) || groupId <= 0 || StrUtil.isEmpty(groupAvatarAddress)){
            return false;
        }
        return true;
    }
}
