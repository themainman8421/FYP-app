package com.example.votingapp;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

public class PageViewModel extends ViewModel {

    private MutableLiveData<Integer> mIndex = new MutableLiveData<>();
    private LiveData<String> mText = Transformations.map(mIndex, new Function<Integer, String>() {
        @Override
        public String apply(Integer input) {
            if(input == 1){
                return "The Popular Vote is one of the more simplistic voting methods due to the candidate only needing to receive a plurality of the vote (the most votes), not the majority (half of the votes +1). " +
                        "It is not necessary for the candidate to receive a majority as long as they have at least one more vote compared to the other candidates. " +
                        "If so, they are then declared the winner.\n\n " +
                        "e.g.\n " +
                        "Between a vote of three people, Jessica, James and Alex. Jessica receives 10 votes, James receives 5 votes and Alex receives 4 votes. " +
                        "Using the Popular vote system Jessica would win as she has received the most votes.";
            }else if(input == 2){
                return "The Majority Voting System is similar to the Plurality Voting System. " +
                        "However, instead of the candidate needing the plurality of the vote, they need the majority of the vote which is half of the votes plus one. " +
                        "Once a candidate receives this, they will be declared the winner. \n\n" +
                        "e.g.\n" +
                        "Between a vote of three people, Jessica, James and Alex. " +
                        "Jessica receives 10 votes, James receives 5 votes and Alex receives 4 votes. " +
                        "Using the Majority Voting System Jessica would win as he has a majority of all votes cast (10 + 5 + 4 = 19, 19/2 = 9.5).";
            }else if(input == 3){
                return "A Ranked Choice Voting Method uses aspects of the Majority Voting System and the single transferable voting system. " +
                        "Just like with the majority system, in order to win, you need the majority of the votes but it comes with a catch. " +
                        "After the first round of voting, the candidate with the least amount of votes is eliminated and the voters who voted for them will have their votes transferred to their second preferences.\n" +
                        "In this case, let’s say anyone who voted for Alex has Jessica as their second preference. " +
                        "All of the votes Alex had then gets transferred to Jessica. " +
                        "This brings Jessica's total to 14 and keeps James’s total at 5. Jessica will then win as he has more than enough for a majority. \n\n" +
                        "e.g.\n" +
                        "Between a vote of three people, Jessica, James and Alex. Jessica receives 10 votes, James receives 5 votes and Alex receives 4 votes. " +
                        "Alex will then be eliminated and the voter’s votes will then be transferred to whoever their second preference is. " +
                        "In this scenario, let’s say 3 of the people who voted for Alex had Jessica as their second preference and the other 1 had James. " +
                        "Jessica will then receive 3 more votes and James will receive 1, making Paul win.";
            }
            return null;
        }

    });

    public void setIndex(int index) {
        mIndex.setValue(index);
    }

    public LiveData<String> getText() {
        return mText;
    }
}
