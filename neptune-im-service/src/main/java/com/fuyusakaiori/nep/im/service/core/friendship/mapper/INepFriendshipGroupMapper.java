package com.fuyusakaiori.nep.im.service.core.friendship.mapper;

import com.fuyusakaiori.nep.im.service.core.friendship.entity.NepFriendshipGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface INepFriendshipGroupMapper {

    /**
     * <h3>创建分组: 对外调用</h3>
     */
    int createFriendshipGroup(@Param("appId") int appId, @Param("group") NepFriendshipGroup group);

    /**
     * <h3>删除分组: 对外调用</h3>
     */
    int deleteFriendshipGroup(@Param("appId") int appId, @Param("groupId") int groupId, @Param("updateTime") long updateTime);

    /**
     * <h3>查询好友分组: 内部调用</h3>
     */
    NepFriendshipGroup queryFriendshipGroupById(@Param("appId") int appId, @Param("groupId") int groupId);

    /**
     * <h3>查询好友分组: 根据成员 ID 和创建者 ID</h3>
     */
    NepFriendshipGroup queryFriendshipGroupByMemberIdAndOwnerId(@Param("appId") int appId, @Param("memberId") int memberId, @Param("ownerId") int ownerId);


    /**
     * <h3>查询所有分组: 对外调用</h3>
     */
    List<NepFriendshipGroup> queryAllFriendshipGroup(@Param("appId") int appId, @Param("ownerId") int ownerId);



}
