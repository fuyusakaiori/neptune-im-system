package com.fuyusakaiori.nep.im.service.core.group.mapper;

import com.fuyusakaiori.nep.im.service.core.group.entity.NepGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface INepGroupMapper {

    /**
     * <h3>创建群组: 群号、群组名称、群主、群组类型、群组最大容量是必填的</h3>
     */
    int createGroup(@Param("appId") int appId, @Param("group") NepGroup group);

    /**
     * <h3>更新群组信息: 可以更新群组名称、群组简介</h3>
     */
    int updateGroupInfo(@Param("appId") int appId, @Param("group") NepGroup group);

    /**
     * <h3>更新群组头像</h3>
     */
    int updateGroupAvatar(@Param("appId") int appId, @Param("groupId") int groupId, @Param("avatar") String avatar);

    /**
     * <h3>解散群组: 权限验证 - 对外调用</h3>
     */
    int dissolveGroup(@Param("appId") int appId, @Param("groupId") int groupId, @Param("updateTime") long updateTime);

    /**
     * <h3>禁言群组: 权限验证 - 对外调用</h3>
     */
    int muteGroupChat(@Param("appId") int appId, @Param("groupId") int groupId, @Param("updateTime") long updateTime);

    /**
     * <h3>转让群组的群主: 权限验证 - 对外调用</h3>
     */
    int transferGroupOwner(@Param("appId") int appId, @Param("groupId") int groupId, @Param("ownerId") int ownerId, @Param("updateTime") long updateTime);

    /**
     * <h3>查询群组: 通过群组 ID 查询 - 内部调用</h3>
     */
    NepGroup queryGroupById(@Param("appId") int appId, @Param("groupId") int groupId);

    /**
     * <h3>查询群组: 通过群号查询 - 对外调用</h3>
     */
    NepGroup queryGroupByNumber(@Param("appId") int appId, @Param("groupNumber") String groupNumber);

    /**
     * <h3>查询群组: 通过群名称查询 - 对外调用</h3>
     */
    List<NepGroup> queryGroupByName(@Param("appId") int appId, @Param("groupName") String groupName);


    /**
     * <h3>查询用户加入的所有群组 - 外部调用</h3>
     */
    List<NepGroup> queryAllJoinedGroup(@Param("appId") int appId, @Param("userId") int userId);

}
