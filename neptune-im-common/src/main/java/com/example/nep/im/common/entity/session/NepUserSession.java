package com.example.nep.im.common.entity.session;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <h3>保存在 Redis 中的信息</h3>
 */
@Data
@Accessors(chain = true)
@ToString
public class NepUserSession {

    private int appId;

    private int userId;

    private int clientType;

    /**
     * <h3>连接状态: 1 表示在线, 2 表示离线</h3>
     */
    private int connectStatus;

    private String imei;

    /**
     * <h3>服务的标识 ID</h3>
     */
    private int brokerId;

    /**
     * <h3>服务的 IP 地址</h3>
     */
    private String brokerHost;

}
