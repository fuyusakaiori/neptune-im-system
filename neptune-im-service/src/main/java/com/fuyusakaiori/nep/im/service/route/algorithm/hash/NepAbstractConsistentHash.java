package com.fuyusakaiori.nep.im.service.route.algorithm.hash;

import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Slf4j
public abstract class NepAbstractConsistentHash {

    public static final String MD5 = "MD5";

    protected abstract void putNewNode(long key, String value);

    protected abstract void sortCurrentNode();

    protected abstract String getMostRecentNode(String invocation);

    protected abstract void beforeProcess();

    protected synchronized String select(List<String> invokers, String invocation){
        // 1. 执行前置处理
        beforeProcess();
        // 2. 添加每个元素
        for (String invoker : invokers) {
            putNewNode(hash(invoker), invoker);
        }
        // 3. 集合中的元素排序
        sortCurrentNode();
        // 4. 获取最近的一个结点
        return getMostRecentNode(invocation);
    }

    protected long hash(String invoker) {
        MessageDigest algorithm = null;
        // 1. 获取哈希算法类型
        try {
            algorithm = MessageDigest.getInstance(MD5);
        } catch (NoSuchAlgorithmException exception) {
            throw new RuntimeException("AbstractConsistentHash hash: 哈希计算出现异常", exception);
        }
        // 2. 重置消息摘要
        algorithm.reset();
        // 3. 字符串数据转换为字节数组
        byte[] keyBytes = invoker.getBytes(StandardCharsets.UTF_8);
        // 4. 设置字节数组
        algorithm.update(keyBytes);
        // 5. 生成型的消息摘要
        byte[] digest = algorithm.digest();
        // 6. 执行哈希运算
        long hashCode = ((long) (digest[3] & 0xFF) << 24)
                                | ((long) (digest[2] & 0xFF) << 16)
                                | ((long) (digest[1] & 0xFF) << 8)
                                | (digest[0] & 0xFF);

        return hashCode & 0xffffffffL;
    }

}
