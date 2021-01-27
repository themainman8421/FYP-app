package com.example.votingapp;

import java.util.ArrayList;
import java.util.List;

public class Poll {

    private String title;

    private String winner;

    private Options options;

    private Votes votes;


    public Poll(String title, String winner, Options options, Votes votes) {
        this.title = title;
        this.winner = winner;
        this.options = options;
        this.votes = votes;
    }

    public String getTitle() {
        return title;
    }

    public String getWinner() {
        return winner;
    }

    public Options getOptions() {
        return options;
    }

    public Votes getVotes() {
        return votes;
    }
}
