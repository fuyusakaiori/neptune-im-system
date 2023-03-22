package com.fuyusakaiori.nep.im.gateway.codec;

import com.example.nep.im.common.entity.proto.NepProtocol;
import com.fuyusakaiori.nep.im.gateway.util.NepProtocolToByteUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class NepTcpMessageEncoder extends MessageToByteEncoder<NepProtocol> {


    @Override
    protected void encode(ChannelHandlerContext context, NepProtocol protocol, ByteBuf byteBuf) throws Exception {
        // 1. 将协议实体对象转换为字节数据
        ByteBuf buf = NepProtocolToByteUtil.transfer(protocol);
        // 2. 写入缓冲区中
        byteBuf.writeBytes(buf);
    }
}
