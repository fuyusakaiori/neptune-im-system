package com.fuyusakaiori.nep.im.gateway.handler;

import com.example.neptune.im.common.enums.message.NepSystemMessageType;
import com.fuyusakaiori.nep.im.codec.proto.NepMessageBody;
import com.fuyusakaiori.nep.im.codec.proto.NepMessageHeader;
import com.fuyusakaiori.nep.im.codec.proto.NepProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NepServerHandler extends SimpleChannelInboundHandler<NepProtocol> {

    @Override
    protected void channelRead0(ChannelHandlerContext context, NepProtocol protocol) throws Exception {
        NepMessageHeader messageHeader = protocol.getMessageHeader();
        NepMessageBody messageBody = protocol.getMessageBody();
        if (messageHeader.getMessageType() == NepSystemMessageType.LOGIN.getMessageType()){

        }
    }
}
