package com.example.votingapp.objects;

//Nested Json Class for the alternative vote poll
public class SecondRound {

    private String option1secondroundtotalvotes;

    private String option2secondroundtotalvotes;

    private String option3secondroundtotalvotes;

    public SecondRound(String option1secondroundtotalvotes, String option2secondroundtotalvotes, String option3secondroundtotalvotes) {
        this.option1secondroundtotalvotes = option1secondroundtotalvotes;
        this.option2secondroundtotalvotes = option2secondroundtotalvotes;
        this.option3secondroundtotalvotes = option3secondroundtotalvotes;
    }

    public String getOption1secondroundtotalvotes() {
        return option1secondroundtotalvotes;
    }

    public String getOption2secondroundtotalvotes() {
        return option2secondroundtotalvotes;
    }

    public String getOption3secondroundtotalvotes() {
        return option3secondroundtotalvotes;
    }
}
