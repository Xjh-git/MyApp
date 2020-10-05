package com.example.auction.chat.bean.result;

public class Results {
    int groupType;
    String resultType;
    Values values;

    public Results() {
    }

    public Results(int groupType, String resultType, Values values) {
        this.groupType = groupType;
        this.resultType = resultType;
        this.values = values;
    }

    public int getGroupType() {
        return groupType;
    }

    public String getResultType() {
        return resultType;
    }

    public Values getValues() {
        return values;
    }
}
