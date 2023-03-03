package com.fuyusakaiori.nep.im.service.core.friendship.service;

import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.normal.*;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.NepModifyFriendshipResponse;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.NepQueryFriendshipResponse;

public interface INepFriendshipService {

    NepModifyFriendshipResponse batchAddFriendship(NepImportFriendshipRequest request);

    NepModifyFriendshipResponse addFriendship(NepAddFriendshipRequest request);

    NepModifyFriendshipResponse editFriendship(NepEditFriendshipRequest request);

    NepModifyFriendshipResponse releaseFriendship(NeptuneReleaseFriendshipRequest request);

    NepModifyFriendshipResponse releaseAllFriendship(NeptuneReleaseAllFriendshipRequest request);

    NepQueryFriendshipResponse queryFriendshipById(NepQueryFriendshipByIdRequest request);

    NepQueryFriendshipResponse queryAllFriendship(NepQueryAllFriendshipRequest request);

}
