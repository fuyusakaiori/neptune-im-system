package com.fuyusakaiori.nep.im.service.core.message.mapper;

import com.fuyusakaiori.nep.im.service.core.message.entity.NepChatGroupMessageHeader;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface INepChatGroupMessageHeaderMapper {

    /**
     * <h3>持久化群聊消息头</h3>
     */
    int storeChatGroupMessageHeader(@Param("appId") int appId, @Param("messageHeader") NepChatGroupMessageHeader messageHeader);

    /**
     * <h3>查询所有群聊消息</h3>
     */
    List<NepChatGroupMessageHeader> loadChatGroupMessageHeader(@Param("appId") int appId, @Param("groupId") int groupId);

    /**
     * <h3>查询某个群成员的所有发言记录</h3>
     */
    List<NepChatGroupMessageHeader> loadChatGroupMemberMessageHeader(@Param("appId") int appId, @Param("groupId") int groupId, @Param("memberId") int memberId);

}
