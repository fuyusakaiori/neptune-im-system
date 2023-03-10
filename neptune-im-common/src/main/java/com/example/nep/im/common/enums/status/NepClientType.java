package com.example.nep.im.common.enums.status;

public enum NepClientType {


    WEB_API(0, "web-api"),

    WEB(1, "web"),

    IOS(2, "ios"),

    ANDROID(3, "android"),

    WINDOWS(4, "windows"),

    MAC(5, "mac");


    private final int type;

    private final String description;

    NepClientType(int type, String description) {
        this.type = type;
        this.description = description;
    }

    public int getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }
}
