package com.example.votingapp;

import java.util.ArrayList;
import java.util.List;

public class Poll {

    private String title;

    private Options options;

    private Votes votes;

    public Poll(String title, Options options, Votes votes) {
        this.title = title;
        this.options = options;
        this.votes = votes;
    }

    public String getTitle() {
        return title;
    }

    public Options getOptions() {
        return options;
    }

    public Votes getVotes() {
        return votes;
    }
}
