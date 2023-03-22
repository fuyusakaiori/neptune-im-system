package com.fuyusakaiori.nep.im.gateway.codec;

import com.example.nep.im.common.entity.proto.NepProtocol;
import com.fuyusakaiori.nep.im.gateway.util.NepByteToProtocolUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;

@Slf4j
public class NepWebSocketMessageDecoder extends MessageToMessageDecoder<BinaryWebSocketFrame> {


    @Override
    protected void decode(ChannelHandlerContext context, BinaryWebSocketFrame frame, List<Object> out) throws Exception {
        // 1. 获取消息内容
        ByteBuf byteBuf = frame.content();
        // 2. 判断协议内容长度是否符合要求
        if (byteBuf.readableBytes() < NepProtocol.PROTOCOL_LENGTH){
            throw new RuntimeException("NepWebSocketMessageDecoder decode: 消息长度小于协议规定的固定长度");
        }
        // 3. 字节数据转换为协议对象
        NepProtocol protocol = NepByteToProtocolUtil.transfer(byteBuf);
        // 4. 校验对象是否为空
        if (Objects.isNull(protocol)){
            throw new RuntimeException("NepTcpMessageDecoder decode: 字节数据转换协议实体对象失败");
        }
        // 5. 添加到返回结果中
        out.add(protocol);

    }
}
