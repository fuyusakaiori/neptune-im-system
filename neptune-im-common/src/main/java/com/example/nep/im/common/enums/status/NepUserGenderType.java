package com.example.nep.im.common.enums.status;

public enum NepUserGenderType {

    SECRETE(0),

    MALE(1),

    FEMALE(2);

    private final int type;

    NepUserGenderType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public static boolean isIllegalGender(int type){
        for (NepUserGenderType genderType : NepUserGenderType.values()) {
            if (genderType.type == type){
                return true;
            }
        }
        return false;
    }
}
