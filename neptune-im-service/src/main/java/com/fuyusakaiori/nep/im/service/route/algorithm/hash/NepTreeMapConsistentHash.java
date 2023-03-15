package com.fuyusakaiori.nep.im.service.route.algorithm.hash;

import cn.hutool.core.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.SortedMap;
import java.util.TreeMap;

@Slf4j
public class NepTreeMapConsistentHash extends NepAbstractConsistentHash {

    /**
     * <h3>哈希环</h3>
     */
    private final TreeMap<Long, String> hashRing = new TreeMap<>();

    /**
     * <h3>虚拟结点数量</h3>
     */
    private static final int VIRTUAL_NODE_SIZE = 2;

    /**
     * <h3>虚拟结点前缀</h3>
     */
    private static final String VIRTUAL_NODE_PREFIX = "node";

    @Override
    protected void putNewNode(long key, String value) {
        // 1. 放入虚拟结点
        for (int index = 0; index < VIRTUAL_NODE_SIZE; index++) {
            hashRing.put(hash(VIRTUAL_NODE_PREFIX + key + index), value);
        }
        // 2. 放入真实结点
        hashRing.put(key, value);
    }

    @Override
    protected void sortCurrentNode() {

    }

    @Override
    protected String getMostRecentNode(String invocation) {
        // 1. 计算哈希值
        long hash = hash(invocation);
        // 2. 获取距离该哈希值最近的全部键值对
        SortedMap<Long, String> sortedMap = hashRing.tailMap(hash);
        // 3. 校验集合是否为空
        if(CollectionUtil.isNotEmpty(sortedMap)){
            // 4. 选择最近的第一个结点返回
            return sortedMap.get(sortedMap.firstKey());
        }
        // 5. 如果没有就返回哈希环中默认的
        return hashRing.firstEntry().getValue();
    }

    @Override
    protected void beforeProcess() {
        hashRing.clear();
    }
}
