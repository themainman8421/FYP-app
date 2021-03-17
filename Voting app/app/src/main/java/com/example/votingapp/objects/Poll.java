package com.example.votingapp.objects;

// Json Class for the poll
public class Poll {

    private String title;

    private String winner;

    private String votingmethod;

    private int code;

    private Options options;

    private Votes votes;

    private FirstRound firstRound;

    private SecondRound secondRound;


    public Poll(String title, String winner, String votingmethod, int code, Options options, Votes votes, FirstRound firstRound, SecondRound secondRound) {
        this.title = title;
        this.winner = winner;
        this.votingmethod = votingmethod;
        this.code = code;
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

    public int getCode() {
        return code;
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
