package com.fuyusakaiori.nep.im.service.core.friendship.mapper;

import com.fuyusakaiori.nep.im.service.core.friendship.entity.NepFriendshipGroupMember;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.dto.NepAddFriendshipGroupMember;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface INepFriendshipGroupMemberMapper {

    /**
     * <h3>向好友分组中添加成员</h3>
     */
    int addFriendshipGroupMember(@Param("appId") int appId, @Param("memberList") List<NepAddFriendshipGroupMember> memberList, @Param("createTime") long createTime, @Param("updateTime") long updateTime);


    /**
     * <h3>移除好友分组中的成员并添加到默认分组中去</h3>
     */
    int removeFriendshipGroupMember(@Param("appId") int appId, @Param("memberId") int memberId, @Param("updateTime") long updateTime);

    /**
     * <h3>解散好友分组中的所有成员</h3>
     */
    int clearFriendshipGroupMember(@Param("appId") int appId, @Param("groupId") int groupId, @Param("updateTime") long updateTime);

    /**
     * <h3>查询自己创建的所有好友分组</h3>
     */
    Map<Integer, List<Integer>> queryAllFriendshipGroupMember(@Param("appId") int appId, @Param("groupIdList") List<Integer> groupIdList);

}
