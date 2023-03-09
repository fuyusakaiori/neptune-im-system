package com.example.neptune.im.common.enums.status;

public enum NepConnectStatus {


    ONLINE(1),

    OFFLINE(2);

    private final int status;

    NepConnectStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
