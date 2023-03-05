package com.fuyusakaiori.nep.im.service.core.friendship.mapper;

import com.fuyusakaiori.nep.im.service.core.friendship.entity.NepFriendshipGroup;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.dto.NepAddFriendshipGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface INepFriendshipGroupMapper {

    /**
     * <h3>创建分组: 只需要传入创建者 ID 和分组名称</h3>
     */
    int addFriendshipGroup(@Param("appId") int appId, @Param("group") NepAddFriendshipGroup group, @Param("createTime") long createTime, @Param("updateTime") long updateTime);

    /**
     * <h3>删除分组: 通过分组 ID 删除</h3>
     */
    int deleteFriendshipGroup(@Param("appId") int appId, @Param("groupId") int groupId, @Param("updateTime") long updateTime);

    /**
     * <h3>查询所有分组: 通过分组的创建者 ID 查询</h3>
     */
    List<NepFriendshipGroup> queryAllFriendshipGroup(@Param("appId") int appId, @Param("ownerId") int ownerId);



}
