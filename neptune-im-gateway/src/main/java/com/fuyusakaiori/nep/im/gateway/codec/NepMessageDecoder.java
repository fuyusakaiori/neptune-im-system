package com.fuyusakaiori.nep.im.gateway.codec;

import com.example.nep.im.common.entity.proto.NepMessageBody;
import com.example.nep.im.common.entity.proto.NepMessageHeader;
import com.example.nep.im.common.entity.proto.NepProtocol;
import com.fuyusakaiori.nep.im.gateway.codec.serial.NepSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

@Slf4j
public class NepMessageDecoder extends ByteToMessageDecoder {


    @Override
    protected void decode(ChannelHandlerContext context, ByteBuf byteBuf, List<Object> out) throws Exception {
        // 1. 检查协议传输的消息长度是否符合预期
        if (byteBuf.readableBytes() < NepProtocol.PROTOCOL_LENGTH){
            throw new RuntimeException("NepMessageDecoder decode: 消息长度小于协议规定的固定长度");
        }
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
            throw new RuntimeException("NepMessageDecoder decode: 消息体反序列化后为空");
        }
        // 13. 封装成消息
        out.add(transferToProtocol(version, serializeType, appId, messageType, clientType, imei, message));
    }

    private NepProtocol transferToProtocol(int version, int serializeType, int appId, int messageType, int clientType, String imei, NepMessageBody message) {
        NepMessageHeader header = new NepMessageHeader()
                                          .setVersion(version)
                                          .setAppId(appId)
                                          .setMessageType(messageType)
                                          .setClientType(clientType)
                                          .setSerializeType(serializeType)
                                          .setImeiBody(imei);
        message.setMessageType(messageType)
                .setAppId(appId)
                .setClientType(clientType)
                .setImei(imei);
        return new NepProtocol().setMessageHeader(header).setMessageBody(message);
    }
}
