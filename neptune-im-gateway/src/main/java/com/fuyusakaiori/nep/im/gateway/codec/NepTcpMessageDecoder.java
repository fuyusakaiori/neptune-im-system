package com.fuyusakaiori.nep.im.gateway.codec;

import com.example.nep.im.common.entity.proto.NepMessageBody;
import com.example.nep.im.common.entity.proto.NepMessageHeader;
import com.example.nep.im.common.entity.proto.NepProtocol;
import com.fuyusakaiori.nep.im.gateway.codec.serial.NepSerializer;
import com.fuyusakaiori.nep.im.gateway.util.NepByteToProtocolUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

@Slf4j
public class NepTcpMessageDecoder extends ByteToMessageDecoder {


    @Override
    protected void decode(ChannelHandlerContext context, ByteBuf byteBuf, List<Object> out) throws Exception {
        // 1. 检查协议传输的消息长度是否符合预期
        if (byteBuf.readableBytes() < NepProtocol.PROTOCOL_LENGTH){
            throw new RuntimeException("NepTcpMessageDecoder decode: 消息长度小于协议规定的固定长度");
        }
        // 2. 字节数据转换为协议对象
        NepProtocol protocol = NepByteToProtocolUtil.transfer(byteBuf);
        // 3. 校验是否转换成功
        if (Objects.isNull(protocol)){
            throw new RuntimeException("NepTcpMessageDecoder decode: 字节数据转换协议实体对象失败");
        }
        // 4. 添加到返回结果中
        out.add(protocol);
    }
}
