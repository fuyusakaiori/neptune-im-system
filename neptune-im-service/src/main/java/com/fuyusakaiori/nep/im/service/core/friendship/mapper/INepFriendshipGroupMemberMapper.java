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
    int addFriendshipGroupMember(@Param("appId") int appId, @Param("groupId") int groupId, @Param("memberIdList") List<Integer> memberIdList, @Param("createTime") Long createTime, @Param("updateTime") Long updateTime);

    /**
     * <h3>移除好友分组中的成员并添加到默认分组中去: 对外调用</h3>
     */
    int moveFriendshipGroupMember(@Param("appId") int appId, @Param("groupId") int groupId, @Param("memberIdList") List<Integer> memberId, @Param("updateTime") long updateTime);

    /**
     * <h3>移除好友所在分组: 外部调用</h3>
     */
    int removeFriendshipGroupMember(@Param("appId") int appId, @Param("groupId") int groupId, @Param("memberId") int memberId);

    /**
     * <h3>解散好友分组中的所有成员: 内部调用</h3>
     */
    int clearFriendshipGroupMember(@Param("appId") int appId, @Param("groupId") int groupId, @Param("updateTime") long updateTime);

    /**
     * <h3>查询用户所在的好友分组</h3>
     */
    List<Integer> queryFriendshipGroupMemberByMemberIdList(@Param("appId") int appId, @Param("memberIdList") List<Integer> memberIdList);

    /**
     * <h3>查询自己创建的所有好友分组: 内部调用</h3>
     */
    List<NepFriendshipGroupMember> queryAllFriendshipGroupMember(@Param("appId") int appId, @Param("groupIdList") List<Integer> groupIdList);

}
