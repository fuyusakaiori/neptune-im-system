package com.example.nep.im.common.enums.status;

public enum NepMultiDeviceMode {


    /**
     * <h3>单端登录 仅允许 Windows、Web、Android 或 iOS 单端登录。</h3>
     */
    ONE(1,"单端登陆"),

    /**
     * <h3>双端登录 允许 Windows、Mac、Android 或 iOS 单端登录，同时允许与 Web 端同时在线。</h3>
     */
    TWO(2,"双端登陆"),

    /**
     * <h3>三端登录 允许 Android 或 iOS 单端登录(互斥)，Windows 或者 Mac 单聊登录(互斥)，同时允许 Web 端同时在线</h3>
     */
    THREE(3,"三端登陆"),

    /**
     * <h3>多端同时在线 允许 Windows、Mac、Web、Android 或 iOS 多端或全端同时在线登录</h3>
     */
    ALL(4,"DeviceMultiLoginEnum_ALL");


    private final int mode;

    private final String description;


    NepMultiDeviceMode(int mode, String description) {
        this.mode = mode;
        this.description = description;
    }

    public int getMode() {
        return mode;
    }

    public String getDescription() {
        return description;
    }
}
