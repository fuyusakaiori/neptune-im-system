package com.fuyusakaiori.nep.im.service.core.friendship.mapper;

import com.fuyusakaiori.nep.im.service.core.friendship.entity.dto.NepAddFriendshipGroupMember;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface INepFriendshipGroupMemberMapper {

    /**
     * <h3>向好友分组中添加成员</h3>
     */
    int addFriendshipGroupMember(@Param("appId") int appId, @Param("member") NepAddFriendshipGroupMember member, @Param("createTime") long createTime, @Param("updateTime") long updateTime);


}
