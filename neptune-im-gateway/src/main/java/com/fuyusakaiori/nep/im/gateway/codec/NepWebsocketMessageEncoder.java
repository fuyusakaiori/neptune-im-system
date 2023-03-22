package com.fuyusakaiori.nep.im.gateway.codec;

import com.example.nep.im.common.entity.proto.NepProtocol;
import com.fuyusakaiori.nep.im.gateway.util.NepProtocolToByteUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

import java.util.List;


public class NepWebsocketMessageEncoder extends MessageToMessageEncoder<NepProtocol> {

    @Override
    protected void encode(ChannelHandlerContext context, NepProtocol protocol, List<Object> out) throws Exception {
        // 1. 将协议实体对象转换为字节数据
        ByteBuf buf = NepProtocolToByteUtil.transfer(protocol);
        // 2. 创建字节数据
        BinaryWebSocketFrame frame = new BinaryWebSocketFrame(buf);
        // 3. 写入到结果集中
        out.add(frame);
    }
}
