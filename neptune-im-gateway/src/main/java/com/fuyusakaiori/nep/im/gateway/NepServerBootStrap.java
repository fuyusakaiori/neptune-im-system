package com.fuyusakaiori.nep.im.gateway;

import com.fuyusakaiori.nep.im.gateway.server.NepTcpServer;

/**
 * <h3>服务端启动类</h3>
 */
public class NepServerBootStrap {

    public static void main(String[] args) {
        new NepTcpServer(2333);
    }


}
