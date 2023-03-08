package com.fuyusakaiori.nep.im.service.core.group.mapper;

import com.fuyusakaiori.nep.im.service.core.group.entity.NepGroupMember;
import com.fuyusakaiori.nep.im.service.core.group.entity.dto.NepAddGroupMember;
import com.fuyusakaiori.nep.im.service.core.group.entity.dto.NepEditGroupMember;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface INepGroupMemberMapper {

    /**
     * <h3>向群组中添加群成员: 分为申请加入和邀请加入两种</h3>
     */
    int addGroupMember(@Param("appId") int appId, @Param("groupMemberList") List<NepAddGroupMember> groupMemberList, @Param("enterTime") long enterTime);

    /**
     * <h3>更新群组中的成员信息</h3>
     */
    int editGroupMember(@Param("appId") int appId, @Param("groupMember") NepEditGroupMember groupMember);

    /**
     * <h3>退出群组: 分为主动退出和被踢出两种</h3>
     */
    int removeGroupMember(@Param("appId") int appId, @Param("groupId") int groupId, @Param("memberId") int memberId, @Param("exitType") int exitType, @Param("exitTime") int exitTime);

    /**
     * <h3>查询群组中的所有成员</h3>
     */
    List<NepGroupMember> queryAllGroupMember(@Param("appId") int appId, @Param("groupId") int groupId);

    /**
     * <h3>查询自己加入的所有群组</h3>
     */
    List<NepGroupMember> queryMemberJoinedGroupId(@Param("appId") int appId, @Param("memberId") int memberId);

    // TODO 根据昵称、备注、账号查询查询群友

}
