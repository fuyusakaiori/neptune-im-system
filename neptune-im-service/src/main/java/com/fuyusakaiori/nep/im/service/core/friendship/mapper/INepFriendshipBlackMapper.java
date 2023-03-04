package com.fuyusakaiori.nep.im.service.core.friendship.mapper;

import com.fuyusakaiori.nep.im.service.core.friendship.entity.NepFriendship;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface INepFriendshipBlackMapper {


    int addFriendInBlackList(@Param("appId") int appId, @Param("fromId") int fromId, @Param("toId") int toId);

    int removeFriendInBlackList(@Param("appId") int appId, @Param("fromId") int fromId, @Param("toId") int toId);

    NepFriendship queryFriendInBlackList(@Param("appId") int appId, @Param("fromId") int fromId, @Param("toId") int toId);

}
