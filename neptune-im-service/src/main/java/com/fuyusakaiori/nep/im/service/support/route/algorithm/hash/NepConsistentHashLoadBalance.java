package com.fuyusakaiori.nep.im.service.support.route.algorithm.hash;

import com.fuyusakaiori.nep.im.service.support.route.NepAbstractLoadBalance;

import java.util.List;

public class NepConsistentHashLoadBalance extends NepAbstractLoadBalance {

    private NepAbstractConsistentHash consistentHash;

    @Override
    public String doSelect(List<String> invokers, String invocation) {
        return consistentHash.select(invokers, invocation);
    }
}
