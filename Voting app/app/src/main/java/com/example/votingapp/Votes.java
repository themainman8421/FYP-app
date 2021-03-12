package com.example.votingapp;

//nested JSON class that gets the votes for popular or majority votes
public class Votes {

    private String option1votes;

    private String option2votes;

    private String option3votes;

    public Votes(String option1votes, String option2votes, String option3votes) {
        this.option1votes = option1votes;
        this.option2votes = option2votes;
        this.option3votes = option3votes;
    }

    public String getOption1votes() {
        return option1votes;
    }

    public String getOption2votes() {
        return option2votes;
    }

    public String getOption3votes() {
        return option3votes;
    }
}
