package com.fuyusakaiori.nep.im.service.core.friendship.mapper;

import com.fuyusakaiori.nep.im.service.core.friendship.entity.NepFriendshipGroupMember;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface INepFriendshipGroupMemberMapper {

    /**
     * <h3>向好友分组中添加成员: 对外调用</h3>
     */
    int addFriendshipGroupMember(@Param("appId") int appId, @Param("groupId") int groupId, @Param("memberId") int memberId, @Param("createTime") long createTime, @Param("updateTime") long updateTime);

    /**
     * <h3>移除好友分组中的成员并添加到默认分组中去: 对外调用</h3>
     */
    int moveFriendshipGroupMember(@Param("appId") int appId, @Param("oldGroupId") int oldGroupId, @Param("newGroupId") int newGroupId, @Param("memberId") int memberId, @Param("updateTime") long updateTime);

    /**
     * <h3>移除好友所在分组: 外部调用</h3>
     */
    int removeFriendshipGroupMember(@Param("appId") int appId, @Param("groupId") int groupId, @Param("memberId") int memberId);

    /**
     * <h3>解散好友分组中的所有成员: 内部调用</h3>
     */
    int clearFriendshipGroupMember(@Param("appId") int appId, @Param("groupId") int groupId);

    /**
     * <h3>查询分组下的所有成员</h3>
     */
    List<NepFriendshipGroupMember> queryAllFriendshipGroupMemberInGroup(@Param("appId") int appId, @Param("groupId") int groupId);

    /**
     * <h3>查询分组成员: 根据分组创建者和分组成员的 ID</h3>
     */
    List<NepFriendshipGroupMember> queryFriendshipGroupMemberList(@Param("appId") int appId, @Param("ownerId") int owner, @Param("memberIdList") List<Integer> memberIdList);

}
