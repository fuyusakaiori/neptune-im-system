package com.fuyusakaiori.nep.im.service.core.friendship.mapper;

import com.fuyusakaiori.nep.im.service.core.friendship.entity.NepFriendshipApplication;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface INepFriendshipApplicationMapper
{

    /**
     * <h3>新增好友申请</h3>
     */
    int addFriendshipApplication(@Param("appId") int appId, @Param("application") NepFriendshipApplication application, @Param("createTime") long createTime, @Param("updateTime") long updateTime);

    /**
     * <h3>审批好友请求是否通过</h3>
     */
    int approveFriendshipApplication(@Param("appId") int appId, @Param("applyId") int applyId, @Param("status") int status, @Param("updateTime") long updateTime);


    /**
     * <h3>已读所有向自己发出的申请</h3>
     */
    int readAllFriendshipApplication(@Param("appId") int appId, @Param("applyIdList") List<Integer> applyIdList);

    /**
     * <h3>查询所有向自己发出的好友申请</h3>
     */
    int queryAllFriendshipApplication(@Param("appId") int appId, @Param("userId") int userId);

}
