package com.fuyusakaiori.nep.im.service.core.friendship.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface INepFriendshipBlackMapper
{

    /**
     * <h3>拉黑好友: 外部调用</h3>
     */
    int addFriendInBlackList(@Param("appId") int appId, @Param("fromId") int fromId, @Param("toId") int toId, @Param("updateTime") long updateTime);

    /**
     * <h3>撤销拉黑: 外部调用</h3>
     */
    int removeFriendInBlackList(@Param("appId") int appId, @Param("fromId") int fromId, @Param("toId") int toId, @Param("updateTime") long updateTime);

    /**
     * <h3>单向校验好友拉黑状态: 外部调用 & 内部调用</h3>
     */
    int checkFriendInBlackList(@Param("appId") int appId, @Param("fromId") int fromId, @Param("toId") int toId);

    /**
     * <h3>双向校验好友拉黑状态: 外部调用 & 内部调用</h3>
     */
    int checkBiFriendInBlackList(@Param("appId") int appId, @Param("fromId") int fromId, @Param("toId") int toId);

    /**
     * <h3>查询所有在黑名单中的好友: 内部调用</h3>
     */
    List<Integer> queryAllFriendInBlackList(@Param("appId") int appId, @Param("fromId") int fromId);

}
