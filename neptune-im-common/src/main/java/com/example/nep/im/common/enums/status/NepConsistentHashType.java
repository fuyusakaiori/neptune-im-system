package com.example.nep.im.common.enums.status;

public enum NepConsistentHashType {

    TREE(1,"com.fuyusakaiori.nep.im.service.route.algorithm.hash.NepTreeMapConsistentHash"),

    CUSTOMER(2, ""),

    ;


    private final int type;
    private final String clazz;

    NepConsistentHashType(int code, String clazz) {
        this.type = code;
        this.clazz = clazz;
    }

    public static NepConsistentHashType getConsistentHashType(int consistentHashType) {
        NepConsistentHashType[] values = NepConsistentHashType.values();
        for (NepConsistentHashType value : values) {
            if (value.getType() == consistentHashType){
                return value;
            }
        }
        return null;
    }

    public String getClazz() {
        return clazz;
    }

    public int getType() {
        return type;
    }

}
