package com.example.votingapp;

import java.util.ArrayList;
import java.util.List;

public class Poll {

    private String title;

    private String winner;

    private String votingmethod;

    private Options options;

    private Votes votes;


    public Poll(String title, String winner, Options options, Votes votes, String votingmethod) {
        this.title = title;
        this.winner = winner;
        this.options = options;
        this.votes = votes;
        this.votingmethod = votingmethod;
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

    public String getVotingmethod() {
        return votingmethod;
    }
}
