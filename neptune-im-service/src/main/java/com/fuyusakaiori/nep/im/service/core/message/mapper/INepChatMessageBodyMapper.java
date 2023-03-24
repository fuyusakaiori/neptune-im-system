package com.fuyusakaiori.nep.im.service.core.message.mapper;

import com.fuyusakaiori.nep.im.service.core.message.entity.NepChatMessageBody;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface INepChatMessageBodyMapper {


    /**
     * <h3>持久化消息体</h3>
     */
    int storeChatMessageBody(@Param("appId") int appId, @Param("messageBody") NepChatMessageBody messageBody);

    /**
     * <h3>获取所有消息内容</h3>
     */
    List<NepChatMessageBody> loadChatMessageBody(@Param("appId") int appId, @Param("messageKeyList") List<Long> messageKeyList);

}
