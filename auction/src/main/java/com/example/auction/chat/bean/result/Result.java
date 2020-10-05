package com.example.auction.chat.bean.result;

import java.util.List;

public class Result {
    Intent intent;
    List<Results> results;

    public Result() {
    }

    public Result(Intent intent, List<Results> results) {
        this.intent = intent;
        this.results = results;
    }

    public Intent getIntent() {
        return intent;
    }

    public List<Results> getResults() {
        return results;
    }
}

