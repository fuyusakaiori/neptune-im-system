package com.example.nep.im.common.enums.status;

public enum NepFriendshipBlackStatus {

    WHITE(0),

    BLACK(1);

    private final int status;

    NepFriendshipBlackStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
