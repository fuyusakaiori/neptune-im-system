package com.fuyusakaiori.nep.im.service.route;

import cn.hutool.core.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;

@Slf4j
public abstract class NepAbstractLoadBalance implements INepLoadBalance {

    private static final int ONLY_ONE_INVOKER = 1;

    @Override
    public String select(List<String> invokers, String invocation) {
        // 1. 如果服务的提供者集合为空, 那么就不需要轮询了
        if (CollectionUtil.isEmpty(invokers) || Objects.isNull(invocation)){
            throw new RuntimeException("AbstractNepLoadBalance select: 服务暂时不可用");
        }
        // 2. 如果服务的提供者集合仅有一个元素, 那么直接返回就可以了
        if (invokers.size() == ONLY_ONE_INVOKER){
            return invokers.get(0);
        }
        // 3. 实现负载均衡
        return doSelect(invokers, invocation);
    }

    public abstract String doSelect(List<String> invokers, String invocation);


}
