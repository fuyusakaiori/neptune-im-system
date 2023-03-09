package com.fuyusakaiori.nep.im.codec.proto;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString
public class NepMessageHeader
{

    /**
     * <h3>协议版本号</h3>
     */
    private int version;

    /**
     * <h3>协议序列化算法: 0 表示 JSON, 1 表示 XML, 2 表示 Protobuf</h3>
     */
    private int serializeType;

    /**
     * <h3>应用 ID</h3>
     */
    private int appId;

    /**
     * <h3>指令类型</h3>
     */
    private int messageType;

    /**
     * <h3>客户端类型: 0 表示 Web, 1 表示 Windows, 2 表示 Android </h3>
     */
    private int clientType;

    /**
     * <h3>区分同一平台不同设备的编号</h3>
     */
    private int imeiLength;

    private String imeiBody;

    /**
     * <h3>消息体长度</h3>
     */
    private int contentLength;

}
