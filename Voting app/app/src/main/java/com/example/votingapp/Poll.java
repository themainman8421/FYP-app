package com.example.votingapp;

import java.util.ArrayList;
import java.util.List;

public class Poll {

    private String title;

    private String winner;

    private String votingmethod;

    private Options options;

    private Votes votes;

    private FirstRound firstRound;

    private SecondRound secondRound;


    public Poll(String title, String winner, String votingmethod, Options options, Votes votes, FirstRound firstRound, SecondRound secondRound) {
        this.title = title;
        this.winner = winner;
        this.votingmethod = votingmethod;
        this.options = options;
        this.votes = votes;
        this.firstRound = firstRound;
        this.secondRound = secondRound;
    }

    public String getTitle() {
        return title;
    }

    public String getWinner() {
        return winner;
    }

    public String getVotingmethod() {
        return votingmethod;
    }

    public Options getOptions() {
        return options;
    }

    public Votes getVotes() {
        return votes;
    }

    public FirstRound getFirstRound() {
        return firstRound;
    }

    public SecondRound getSecondRound() {
        return secondRound;
    }
}
