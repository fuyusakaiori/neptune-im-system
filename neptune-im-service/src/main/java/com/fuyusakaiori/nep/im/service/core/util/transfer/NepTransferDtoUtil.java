package com.fuyusakaiori.nep.im.service.core.util.transfer;

import com.example.nep.im.common.enums.status.NepFriendshipStatus;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.NepFriendshipApplication;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.dto.NepAddFriendship;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.dto.NepEditFriendship;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.dto.NepSendFriendshipApplication;

public class NepTransferDtoUtil {


    public static NepEditFriendship transferToEditFriendship(Integer friendFromId, Integer friendToId, String remark, String source, String additionalInfo) {
        return new NepEditFriendship()
                       .setFriendFromId(friendFromId)
                       .setFriendToId(friendToId)
                       .setFriendshipStatus(NepFriendshipStatus.FRIENDSHIP_NORMAL.getStatus())
                       .setFriendRemark(remark)
                       .setFriendshipSource(source)
                       .setAdditionalInfo(additionalInfo);
    }

    public static NepAddFriendship transferToAddFriendship(NepFriendshipApplication application){
        return new NepAddFriendship()
                       .setFriendFromId(application.getFriendshipFromId())
                       .setFriendToId(application.getFriendshipToId())
                       .setFriendshipSource(application.getApplySource())
                       .setFriendRemark(application.getApplyRemark())
                       .setAdditionalInfo(application.getApplyAdditionalInfo());
    }

    public static NepSendFriendshipApplication transferToFriendshipApplication(NepAddFriendship body) {
        return new NepSendFriendshipApplication()
                       .setFriendFromId(body.getFriendFromId())
                       .setFriendToId(body.getFriendToId())
                       .setRemark(body.getFriendRemark())
                       .setAdditionalInfo(body.getAdditionalInfo())
                       .setSource(body.getFriendshipSource());
    }

}
