package com.example.votingapp;

//Allows to get the firs round votes from the ranked choice vote
public class FirstRound {

    private String option1firstroundtotalvotes;

    private String option2firstroundtotalvotes;

    private String option3firstroundtotalvotes;

    public FirstRound(String option1firstroundtotalvotes, String option2firstroundtotalvotes, String option3firstroundtotalvotes) {
        this.option1firstroundtotalvotes = option1firstroundtotalvotes;
        this.option2firstroundtotalvotes = option2firstroundtotalvotes;
        this.option3firstroundtotalvotes = option3firstroundtotalvotes;
    }

    public String getOption1firstroundtotalvotes() {
        return option1firstroundtotalvotes;
    }

    public String getOption2firstroundtotalvotes() {
        return option2firstroundtotalvotes;
    }

    public String getOption3firstroundtotalvotes() {
        return option3firstroundtotalvotes;
    }
}
