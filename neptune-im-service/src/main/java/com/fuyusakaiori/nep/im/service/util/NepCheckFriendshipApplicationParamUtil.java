package com.fuyusakaiori.nep.im.service.util;

import com.example.neptune.im.common.entity.request.NepRequestHeader;
import com.example.neptune.im.common.enums.status.NepFriendshipApplicationApproveStatus;
import com.example.neptune.im.common.util.NepCheckBaseParamUtil;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.apply.NepApproveFriendshipApplicationRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.apply.NepSendFriendshipApplicationRequest;

import java.util.Objects;

public class NepCheckFriendshipApplicationParamUtil {


    /**
     * <h3>校验审批好友请求的参数: 仅校验好友请求 ID</h3>
     */
    public static boolean checkApproveFriendshipApplicationRequestParam(NepApproveFriendshipApplicationRequest request) {
        NepRequestHeader header = request.getRequestHeader();
        Integer applyId = request.getApplyId();
        Integer approveStatus = request.getApproveStatus();
        if (!NepCheckBaseParamUtil.checkNeptuneRequestBaseParam(header)){
            return false;
        }
        if (Objects.isNull(applyId) || applyId <= 0 ||
                    Objects.isNull(approveStatus) || NepFriendshipApplicationApproveStatus.isIllegalStatus(approveStatus)){
            return false;
        }
        return true;
    }
}
