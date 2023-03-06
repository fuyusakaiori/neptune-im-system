package com.example.neptune.im.common.enums.status;

public enum NepFriendshipApplicationApproveStatus {

    UNAPPROVED(0),

    AGREE(1),

    REJECT(2);


    private final int status;

    NepFriendshipApplicationApproveStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public static boolean isIllegalStatus(int status){
        if (status != UNAPPROVED.getStatus() && status != AGREE.getStatus() && status != REJECT.getStatus()){
            return false;
        }
        return true;
    }
}
