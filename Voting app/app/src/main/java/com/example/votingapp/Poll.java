package com.example.votingapp;

import java.util.ArrayList;
import java.util.List;

public class Poll {

    private String title;

    private Options options;

    public Poll(String title, Options options) {
        this.title = title;
        this.options = options;
    }

    public String getTitle() {
        return title;
    }

    public Options getOptions() {
        return options;
    }
}
