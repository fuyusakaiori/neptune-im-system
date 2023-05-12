package com.fuyusakaiori.nep.im.service.core.group.mapper;

import com.fuyusakaiori.nep.im.service.core.group.entity.NepGroupMember;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface INepGroupMemberMapper {

    /**
     * <h3>向群组中添加群成员</h3>
     */
    int addGroupMember(@Param("appId") int appId, @Param("groupMemberList") List<NepGroupMember> groupMemberList);

    /**
     * <h3>更新群组中的成员信息: 更新成员昵称</h3>
     */
    int updateGroupMemberInfo(@Param("appId") int appId,  @Param("groupId") int groupId, @Param("memberId") int memberId, @Param("nickname") String nickname);

    int rejoinGroupMember(@Param("appId") int appId, @Param("groupId") int groupId, @Param("memberId") int memberId, @Param("enterType") int enterType, @Param("enterTime") long enterTime);

    /**
     * <h3>更改成员类型: 群主、管理员、普通群员</h3>
     */
    int changeGroupMemberType(@Param("appId") int appId, @Param("groupId") int groupId, @Param("memberId") int memberId, @Param("type") int type);

    /**
     * <h3>禁言成员</h3>
     */
    int muteGroupMemberChat(@Param("appId") int appId, @Param("groupId") int groupId, @Param("memberId") int memberId, @Param("muteEndTime") long muteEndTime);

    /**
     * <h3>撤销成员禁言</h3>
     */
    int revokeGroupMemberChat(@Param("appId") int appId, @Param("groupId") int groupId, @Param("memberId") int memberId);

    /**
     * <h3>退出群组</h3>
     */
    int exitGroupMember(@Param("appId") int appId, @Param("groupId") int groupId, @Param("memberId") int memberId, @Param("exitType") int exitType, @Param("exitTime") long exitTime);

    /**
     * <h3>清空群组中的所有成员</h3>
     */
    int clearGroupMember(@Param("appId") int appId, @Param("groupId") int groupId, @Param("exitType") int exitType, @Param("exitTime") long exitTime);

    /**
     * <h3>查询群聊中的成员数量</h3>
     */
    int queryGroupMemberCount(@Param("appId") int appId, @Param("groupId") int groupId);

    /**
     * <h3>查询群组成员信息</h3>
     */
    NepGroupMember queryGroupMember(@Param("appId") int appId, @Param("groupId") int groupId, @Param("memberId") int memberId);

    /**
     * <h3>查询用户在多个群组中的信息</h3>
     */
    List<NepGroupMember> queryGroupMemberList(@Param("appId") int appId, @Param("groupIdList") List<Integer> groupIdList, @Param("memberId") int memberId);

    /**
     * <h3>查询群聊中的所有管理员</h3>
     */
    List<NepGroupMember> queryAllGroupAdmin(@Param("appId") int appId, @Param("groupId") int groupId);

    /**
     * <h3>查询群组中的所有成员</h3>
     */
    List<NepGroupMember> queryAllGroupMember(@Param("appId") int appId, @Param("groupId") int groupId);

    /**
     * <h3>查询用户加入的所有群聊</h3>
     */
    List<NepGroupMember> queryGroupMemberListByMemberId(@Param("appId") int appId, @Param("memberId") int memberId);

}
