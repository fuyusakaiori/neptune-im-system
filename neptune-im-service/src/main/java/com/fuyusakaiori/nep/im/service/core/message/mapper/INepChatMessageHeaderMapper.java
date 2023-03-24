package com.fuyusakaiori.nep.im.service.core.message.mapper;

import com.fuyusakaiori.nep.im.service.core.message.entity.NepChatP2PMessageHeader;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface INepChatMessageHeaderMapper {

    /**
     * <h3>持久化消息头: 每次持久化两条消息头</h3>
     */
    int storeChatMessageHeader(@Param("appId") int appId, @Param("messageHeaderList") List<NepChatP2PMessageHeader> messageHeaderList);

    /**
     * <h3>查询单聊聊天记录</h3>
     */
    List<NepChatP2PMessageHeader> loadChatMessageHeader(@Param("appId") int appId, @Param("senderId") int senderId, @Param("receiverId") int receiverId);

}
