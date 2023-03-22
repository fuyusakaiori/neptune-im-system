package com.fuyusakaiori.nep.im.gateway.util;

import com.example.nep.im.common.entity.proto.NepMessageBody;
import com.example.nep.im.common.entity.proto.NepMessageHeader;
import com.example.nep.im.common.entity.proto.NepProtocol;
import com.fuyusakaiori.nep.im.gateway.codec.serial.NepSerializer;
import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class NepByteToProtocolUtil {

    public static NepProtocol transfer(ByteBuf byteBuf){
        // 2. 读取协议版本号
        int version = byteBuf.readInt();
        // 3. 读取协议序列化算法
        int serializeType = byteBuf.readInt();
        // 4. 读取应用 ID
        int appId = byteBuf.readInt();
        // 5. 读取指令类型
        int messageType = byteBuf.readInt();
        // 6. 读取客户端类型
        int clientType = byteBuf.readInt();
        // 7. 读取 IMEI 长度
        int imeiLength = byteBuf.readInt();
        // 8. 读取 IMEI 内容
        byte[] imeiBody = new byte[imeiLength];
        byteBuf.readBytes(imeiBody);
        // 9. 解析 IMEI 内容
        String imei = new String(imeiBody, StandardCharsets.UTF_8);
        // 10. 读取消息体长度
        int contentLength = byteBuf.readInt();
        // 11. 读取消息内容
        byte[] contentBody = new byte[contentLength];
        byteBuf.readBytes(contentBody);
        // 12. 解析消息内容
        NepMessageBody message = NepSerializer.deserialize(serializeType, messageType, contentBody);
        if (Objects.isNull(message)){
            throw new RuntimeException("NepByteToProtocolUtil transfer: 消息体反序列化后为空");
        }
        return transferToProtocol(version, serializeType, appId, messageType, clientType, imeiLength, imei, contentLength, message);
    }

    private static NepProtocol transferToProtocol(int version, int serializeType, int appId, int messageType, int clientType, int imeiLength, String imei, int contentLength, NepMessageBody message) {
        NepMessageHeader header = new NepMessageHeader().setVersion(version).setSerializeType(serializeType)
                                          .setAppId(appId).setMessageType(messageType).setClientType(clientType)
                                          .setImeiLength(imeiLength).setImeiBody(imei).setContentLength(contentLength);
        message.setAppId(appId).setMessageType(messageType)
                .setClientType(clientType).setImei(imei);
        return new NepProtocol().setMessageHeader(header).setMessageBody(message);
    }


}
