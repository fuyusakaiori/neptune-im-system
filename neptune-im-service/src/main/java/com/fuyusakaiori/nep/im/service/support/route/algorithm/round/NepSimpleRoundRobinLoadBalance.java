package com.fuyusakaiori.nep.im.service.support.route.algorithm.round;

import com.fuyusakaiori.nep.im.service.support.route.NepAbstractLoadBalance;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * <h3>完全轮询</h3>
 */
@Slf4j
public class NepSimpleRoundRobinLoadBalance extends NepAbstractLoadBalance
{

    private static final AtomicLong cnt = new AtomicLong(0);

    @Override
    public String doSelect(List<String> invokers, String invocation) {
        return invokers.get((int)(cnt.getAndIncrement() % invokers.size()));
    }
}
