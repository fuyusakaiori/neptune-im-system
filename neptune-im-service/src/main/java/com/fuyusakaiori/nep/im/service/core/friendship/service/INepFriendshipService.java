package com.fuyusakaiori.nep.im.service.core.friendship.service;

import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.normal.*;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.normal.*;

public interface INepFriendshipService {

    NepAddFriendshipResponse addFriendship(NepAddFriendshipRequest request);

    NepEditFriendshipRemarkResponse editFriendshipRemark(NepEditFriendshipRemarkRequest request);

    NepReleaseFriendshipResponse releaseFriendship(NepReleaseFriendshipRequest request);

    NepReleaseFriendshipResponse releaseAllFriendship(NepReleaseAllFriendshipRequest request);

    NepCheckFriendshipResponse checkFriendship(NepCheckFriendshipRequest request);


}
