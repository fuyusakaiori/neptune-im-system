package com.fuyusakaiori.nep.im.gateway.util;

import com.example.nep.im.common.entity.proto.NepMessageBody;
import com.example.nep.im.common.entity.proto.NepMessageHeader;
import com.example.nep.im.common.entity.proto.NepProtocol;
import com.fuyusakaiori.nep.im.gateway.codec.serial.NepSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class NepProtocolToByteUtil {

    public static ByteBuf transfer(NepProtocol protocol){
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer();
        NepMessageHeader messageHeader = protocol.getMessageHeader();
        NepMessageBody messageBody = protocol.getMessageBody();
        // 1. 写入协议版本号
        byteBuf.writeInt(messageHeader.getVersion());
        // 2. 写入采用的序列化算法
        byteBuf.writeInt(messageHeader.getSerializeType());
        // 3. 写入应用 ID
        byteBuf.writeInt(messageHeader.getAppId());
        // 4. 写入指令类型
        byteBuf.writeInt(messageHeader.getMessageType());
        // 5. 写入客户端类型
        byteBuf.writeInt(messageHeader.getClientType());
        // 6. 写入 imei 长度
        byteBuf.writeInt(messageHeader.getImeiLength());
        // 7. 写入 imei 内容
        byteBuf.writeBytes(messageHeader.getImeiBody().getBytes(StandardCharsets.UTF_8));
        // 8. 序列化消息体
        byte[] bytes = NepSerializer.serialize(messageHeader.getSerializeType(), messageBody);
        if (Objects.isNull(bytes)){
            throw new RuntimeException("NepProtocolToByteUtil transfer: 序列化消息失败");
        }
        // 9. 写入消息长度
        byteBuf.writeInt(bytes.length);
        // 10. 写入消息
        byteBuf.writeBytes(bytes);
        // 11. 返回缓冲区
        return byteBuf;
    }


}
