package com.fuyusakaiori.nep.im.service.core.friendship.mapper;


import com.fuyusakaiori.nep.im.service.core.friendship.entity.NepFriendship;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface INepFriendshipMapper {

    /**
     * <h3>双向添加好友关系</h3>
     */
    int addFriendship(@Param("appId") int appId, @Param("friendship") NepAddFriendship friendship, @Param("createTime") long createTime, @Param("updateTime") long updateTime);

    /**
     * <h3>更新好友关系</h3>
     */
    int editFriendship(@Param("appId") int appId, @Param("friendship") NepEditFriendship friendship, @Param("updateTime") long updateTime);

    /**
     * <h3>解除单个好友关系</h3>
     */
    int releaseFriendship(@Param("appId") int appId, @Param("fromId") int friendshipFromId, @Param("toId") int friendshipToId, @Param("updateTime") long updateTime);


    /**
     * <h3>解除所有好友关系</h3>
     */
    int releaseAllFriendship(@Param("appId") int appId, @Param("fromId") int friendshipFromId, @Param("updateTime") long updateTime);

    /**
     * <h3>查询用户: 通过 ID 查询</h3>
     */
    NepFriendship queryFriendshipById(@Param("appId") int appId, @Param("fromId") int friendshipFromId, @Param("toId") int friendshipToId);

    /**
     * <h3>查询用户: 通过 ID 集合查询</h3>
     */
    List<NepFriendship> queryFriendshipByIdList(@Param("appId") int appId, @Param("fromId") int friendshipFromId, @Param("list") List<Integer> friendshipToIdList);

    /**
     * <h3>查询用户: 通过备注查询</h3>
     */
    List<NepFriendship> queryFriendshipByRemark(@Param("appId") int appId, @Param("fromId") int friendshipFromId, @Param("remark") String friendRemark);

    /**
     * <h3>查询所有好友</h3>
     */
    List<NepFriendship> queryAllFriendship(@Param("appId") int appId, @Param("fromId") int friendFromId);


}
