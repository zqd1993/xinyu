package com.mshy.VInterestSpeed.common.bean;

import java.io.Serializable;

/**
 * @desc 用户在通话中
 */
public class UserInCallEvent implements Serializable {
    private boolean userInCall;
    private boolean isAudio;

    public UserInCallEvent() {
    }

    public UserInCallEvent(boolean userInCall, boolean isAudio) {
        this.userInCall = userInCall;
        this.isAudio = isAudio;
    }

    public boolean isUserInCall() {
        return userInCall;
    }

    public void setUserInCall(boolean userInCall) {
        this.userInCall = userInCall;
    }

    public boolean isAudio() {
        return isAudio;
    }

    public void setAudio(boolean audio) {
        isAudio = audio;
    }

    @Override
    public String toString() {
        return "UserInCallEvent{" +
                "userInCall=" + userInCall +
                ", isAudio=" + isAudio +
                '}';
    }
}
