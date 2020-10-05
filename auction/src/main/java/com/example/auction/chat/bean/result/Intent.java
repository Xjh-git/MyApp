package com.example.auction.chat.bean.result;

public class Intent {
    String actionName;
    int code;
    String intentName;

    public Intent() {
    }

    public Intent(String actionName, int code, String intentName) {
        this.actionName = actionName;
        this.code = code;
        this.intentName = intentName;
    }

    public String getActionName() {
        return actionName;
    }

    public int getCode() {
        return code;
    }

    public String getIntentName() {
        return intentName;
    }
}
