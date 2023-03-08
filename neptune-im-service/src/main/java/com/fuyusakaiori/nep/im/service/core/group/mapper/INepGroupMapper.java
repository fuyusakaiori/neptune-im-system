package com.fuyusakaiori.nep.im.service.core.group.mapper;

import com.fuyusakaiori.nep.im.service.core.group.entity.NepGroup;
import com.fuyusakaiori.nep.im.service.core.group.entity.dto.NepCreateGroup;
import com.fuyusakaiori.nep.im.service.core.group.entity.dto.NepEditGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface INepGroupMapper {

    /**
     * <h3>创建群组: 对外调用</h3>
     */
    int createGroup(@Param("appId") int appId, @Param("group") NepCreateGroup group, @Param("createTime") long createTime, @Param("updateTime") long updateTime);

    /**
     * <h3>更新群组信息: 权限验证 - 对外调用</h3>
     */
    int editGroup(@Param("appId") int appId, @Param("group") NepEditGroup group, @Param("updateTime") long updateTime);

    /**
     * <h3>解散群组: 权限验证 - 对外调用</h3>
     */
    int deleteGroup(@Param("appId") int appId, @Param("groupId") int groupId, @Param("updateTime") long updateTime);

    /**
     * <h3>禁言群组: 权限验证 - 对外调用</h3>
     */
    int muteGroup(@Param("appId") int appId, @Param("groupId") int groupId, @Param("updateTime") long updateTime);

    /**
     * <h3>转让群组的群主: 权限验证 - 对外调用</h3>
     */
    int transferGroupOwner(@Param("appId") int appId, @Param("groupId") int groupId, @Param("ownerId") int ownerId, @Param("updateTime") long updateTime);

    /**
     * <h3>查询群组: 通过群号查询 - 对外调用</h3>
     */
    NepGroup querySimpleGroupByNumber(@Param("appId") int appId, @Param("groupNumber") int groupNumber);

    /**
     * <h3>查询群组: 通过群名称查询 - 对外调用</h3>
     */
    List<NepGroup> querySimpleGroupByName(@Param("appId") int appId, @Param("groupName") String groupName);

    /**
     * <h3>查询群组: 通过群组 ID 查询 - 内部调用</h3>
     */
    NepGroup querySimpleGroupById(@Param("appId") int appId, @Param("groupId") int groupId);

    /**
     * <h3>查询群组: 通过群组 ID 查询 - 外部调用</h3>
     */
    NepGroup queryDetailedGroupById(@Param("appId") int appId, @Param("groupId") int groupId);

    /**
     * <h3>查询用户加入的所有群组 - 外部调用</h3>
     */
    List<NepGroup> queryAllJoinedGroup(@Param("appId") int appId, @Param("groupIdList") List<Integer> groupIdList);

}
