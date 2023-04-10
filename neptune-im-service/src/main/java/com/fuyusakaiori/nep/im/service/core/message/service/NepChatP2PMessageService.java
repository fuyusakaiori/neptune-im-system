package com.fuyusakaiori.nep.im.service.core.message.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.IdUtil;
import com.example.nep.im.common.entity.proto.message.NepChatAckMessage;
import com.example.nep.im.common.entity.proto.message.NepChatP2PMessage;
import com.example.nep.im.common.entity.proto.message.NepChatConfirmAckMessage;
import com.example.nep.im.common.entity.session.NepUserSessionInfo;
import com.example.nep.im.common.enums.INepBaseResponseCode;
import com.example.nep.im.common.enums.code.NepBaseResponseCode;
import com.example.nep.im.common.enums.code.NepMessageResponseCode;
import com.example.nep.im.common.enums.message.NepChatMessageType;
import com.fuyusakaiori.nep.im.service.core.message.mq.NepServiceToGateWayMessageProducer;
import com.fuyusakaiori.nep.im.service.util.NepUserSessionTaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class NepChatP2PMessageService {

    @Autowired
    private NepServiceToGateWayMessageProducer messageSender;

    @Autowired
    private NepCheckSendMessageService checkSendMessageService;

    @Autowired
    private NepStoreSendMessageService storeMessageService;

    @Autowired
    private NepUserSessionTaker sessionTaker;

    /**
     * <h3>线程池并发处理消息</h3>
     */
    private final ThreadPoolExecutor threadPool;

    // 核心线程数
    public static final int MESSAGE_THREAD_POOL_CORE_POOL_SIZE = 10;

    // 最大线程数
    private static final int MESSAGE_THREAD_POOL_MAXIMUM_POOL_SIZE = 20;

    // 非核心线程存活时间
    public static final int MESSAGE_THREAD_POOL_KEEP_ALIVE_TIME = 60;

    // 阻塞队列大小
    public static final int MESSAGE_THREAD_POOL_CAPACITY = 1000;

    // 初始化线程池
    {
        this.threadPool = new ThreadPoolExecutor(
                        MESSAGE_THREAD_POOL_CORE_POOL_SIZE,
                        MESSAGE_THREAD_POOL_MAXIMUM_POOL_SIZE,
                        MESSAGE_THREAD_POOL_KEEP_ALIVE_TIME,
                        TimeUnit.SECONDS,
                        new LinkedBlockingQueue<>(MESSAGE_THREAD_POOL_CAPACITY),
                        new ThreadFactory(){
                            private final AtomicInteger index = new AtomicInteger(0);
                            @Override
                            public Thread newThread(@Nonnull Runnable runnable) {
                                // 1. 创建线程
                                Thread thread = new Thread(runnable);
                                // 2. 设置线程为守护线程
                                thread.setDaemon(true);
                                // 3. 设置线程名称
                                thread.setName("message-thread-pool-name-" + index.getAndIncrement());
                                // 4. 返回新建的线程
                                return thread;
                            }
                        },
                new ThreadPoolExecutor.DiscardPolicy());
    }

    /**
     * <h3>处理单聊消息</h3>
     */
    public void handleMessage(NepChatP2PMessage message){
        // 1. 获取消息中的变量
        int appId = message.getAppId();
        int senderId = message.getSenderId();
        int receiverId = message.getReceiverId();
        // 2. 校验参数是否合理
        if (appId <= 0 || senderId <= 0 || receiverId <= 0){
            log.error("NepChatP2PMessageService handleMessage: 发送消息没有通过参数校验 - message: {}", message);
            // 注: 参数校验失败依然需要告知客户端发送失败, 不能只是打印日志后返回
            sendAckMessage(message, NepBaseResponseCode.CHECK_PARAM_FAIL);
            return;
        }
        // 3. 发送消息的前置校验
        boolean isAllowSendMessage = checkSendMessageService.checkChatP2PMessageSend(appId, senderId, receiverId);
        if (!isAllowSendMessage){
            log.error("NepChatP2PMessageService handleMessage: 单聊消息没有通过校验 - message: {}", message);
            // 注: 发送消息的前置校验失败依然需要告知客户端发送失败, 不能只是打印日志后返回
            sendAckMessage(message, NepMessageResponseCode.CHECK_RELATION_FAIL);
            return;
        }
        // 4. 线程池异步处理消息分发, 当前线程返回继续从消息队列中取出新的消息
        threadPool.execute(() -> {
            // 4.1 持久化消息
            storeMessage(message);
            // 4.2 响应 ACK 消息给客户端
            sendAckMessage(message, NepBaseResponseCode.SUCCESS);
            // 4.3 同步消息给自己的所有客户端
            sendSyncMessage(message);
            // 4.4 发送消息给对方的所有客户端
            sendP2PMessage(message);
        });
    }

    /**
     * <h3>处理消息接收者返回的 ACK 消息</h3>
     */
    public void handleMessage(NepChatConfirmAckMessage message){
        // TODO 参数校验之类的没有做
        sendConfirmAckMessage(message);
    }

    /**
     * <h3>持久化消息</h3>
     */
    private void storeMessage(NepChatP2PMessage message) {
        storeMessageService.storeMessage(message);
    }

    /**
     * <h3>发送 ACK 消息给发送方</h3>
     */
    private void sendAckMessage(NepChatP2PMessage message, INepBaseResponseCode response){
        // 1. 封装生成响应消息
        NepChatAckMessage chatAckMessage = (NepChatAckMessage) new NepChatAckMessage()
                                             .setCode(response.getCode()).setMessage(response.getMessage()).setMessageId(message.getMessageId())
                                             .setAppId(message.getAppId()).setClientType(message.getClientType()).setImei(message.getImei())
                                             .setMessageType(NepChatMessageType.P2P_MESSAGE_ACK.getMessageType());
        // 2. 发送消息
        messageSender.sendMessage(message.getAppId(), message.getSenderId(), message.getClientType(), message.getImei(),
                chatAckMessage.getMessageType(), chatAckMessage, false);
    }

    /**
     * <h3>发送确认 ACK 消息给发送方</h3>
     */
    private void sendConfirmAckMessage(NepChatConfirmAckMessage message){
        messageSender.sendMessage(message.getAppId(), message.getReceiverId(),
                message.getClientType(), message.getImei(), message.getMessageType(), message);
    }

    /**
     * <h3>发送同步消息给自己的所有客户端</h3>
     */
    private void sendSyncMessage(NepChatP2PMessage message){
        messageSender.sendMessage(message.getAppId(), message.getSenderId(), message.getClientType(), message.getImei(),
                message.getMessageType(), message, true);
    }

    /**
     * <h3>发送单聊消息给对方</h3>
     */
    private void sendP2PMessage(NepChatP2PMessage message){
        // 1. 查询对方客户端是否在线
        List<NepUserSessionInfo> userSessionList =
                sessionTaker.getUserSessionList(message.getAppId(), message.getReceiverId());
        if (CollectionUtil.isEmpty(userSessionList)){
            // 2. 如果对方没有客户端在线, 那么服务端返回相应的 ACK
            sendConfirmAckMessage(generateChatConfirmAckMessage(message));
        }
        // 3. 把消息发送给客户端
        messageSender.sendMessage(message.getAppId(), message.getReceiverId(),
                message.getMessageType(), message);
    }

    private static NepChatConfirmAckMessage generateChatConfirmAckMessage(NepChatP2PMessage message) {
        return (NepChatConfirmAckMessage) new NepChatConfirmAckMessage()
                       .setServerSend(true)
                       .setSenderId(message.getReceiverId())
                       .setReceiverId(message.getSenderId())
                       .setMessageType(NepChatMessageType.P2P_MESSAGE_RECEIVE_ACK.getMessageType())
                       .setAppId(message.getAppId())
                       .setClientType(message.getClientType())
                       .setImei(message.getImei());
    }


}
