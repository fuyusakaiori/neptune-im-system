package com.fuyusakaiori.nep.im.service.support.route.algorithm.random;

import com.fuyusakaiori.nep.im.service.support.route.NepAbstractLoadBalance;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * <h3>简单随机算法</h3>
 */
public class NepSimpleRandomLoadBalance extends NepAbstractLoadBalance
{


    @Override
    public String doSelect(List<String> invokers, String invocation) {
        return invokers.get(ThreadLocalRandom.current().nextInt(invokers.size()));
    }
}
