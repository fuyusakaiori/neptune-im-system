package com.example.nep.im.common.enums.status;

public enum NepLoadBalanceType {


    SIMPLE_RANDOM(1, "com.fuyusakaiori.nep.im.service.route.algorithm.random.NepSimpleRandomLoadBalance"),

    SIMPLE_ROUND_ROBIN(2,"com.fuyusakaiori.nep.im.service.route.algorithm.round.NepSimpleRoundRobinLoadBalance"),

    CONSISTENT_HASH(3,"com.fuyusakaiori.nep.im.service.route.algorithm.hash.NepConsistentHashLoadBalance"),
    ;


    private final int type;

    private final String clazz;


    NepLoadBalanceType(int type, String clazz) {
        this.type = type;
        this.clazz = clazz;
    }

    public int getType() {
        return type;
    }

    public String getClazz() {
        return clazz;
    }

    public static NepLoadBalanceType getLoadBalance(int loadBalanceType){
        NepLoadBalanceType[] values = NepLoadBalanceType.values();
        for (NepLoadBalanceType value : values) {
            if (value.type == loadBalanceType) {
                return value;
            }
        }
        return null;
    }
}
