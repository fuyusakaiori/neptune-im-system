package com.fuyusakaiori.nep.im.service.core.friendship.mapper;

import com.fuyusakaiori.nep.im.service.core.friendship.entity.NepFriendshipApplication;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface INepFriendshipApplicationMapper {

    /**
     * <h3>新增好友申请: 外部调用</h3>
     */
    int sendFriendshipApplication(@Param("appId") int appId, @Param("application") NepFriendshipApplication application);

    /**
     * <h3>更新好友申请：内部调用</h3>
     */
    int updateFriendshipApplication(@Param("appId") int appId, @Param("application") NepFriendshipApplication application);

    /**
     * <h3>审批好友请求是否通过: 外部调用</h3>
     */
    int approveFriendshipApplication(@Param("appId") int appId, @Param("applyId") int applyId, @Param("status") int status, @Param("updateTime") long updateTime);


    /**
     * <h3>已读所有向自己发出的申请: 内部调用</h3>
     */
    int readAllFriendshipApplication(@Param("appId") int appId, @Param("applyIdList") List<Integer> applyIdList, @Param("updateTime") long updateTime);

    /**
     * <h3>查询好友申请: 通过好友请求 ID - 内部调用</h3>
     */
    NepFriendshipApplication queryFriendshipApplicationById(@Param("appId") int appId, @Param("applyId") int applyId);

    /**
     * <h3>查询好友申请: 通过双方用户 ID - 内部调用</h3>
     */
    NepFriendshipApplication queryFriendshipApplicationByUserId(@Param("appId") int appId, @Param("fromId") int fromId, @Param("toId") int toId);

    /**
     * <h3>查询所有向自己发出的好友申请 - 内部调用</h3>
     */
    List<NepFriendshipApplication> queryAllFriendshipApplication(@Param("appId") int appId, @Param("userId") int userId);

}
