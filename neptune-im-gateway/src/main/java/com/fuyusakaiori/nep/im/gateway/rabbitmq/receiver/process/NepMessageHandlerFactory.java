package com.fuyusakaiori.nep.im.gateway.rabbitmq.receiver.process;


public class NepMessageHandlerFactory {

    private static final NepBaseMessageHandler handler = new NepBaseMessageHandler() {};


    public static NepBaseMessageHandler getMessageHandler(int messageType){
        // TODO 根据不同的消息返回相应的处理器
        return handler;
    }

}
