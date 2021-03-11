package com.example.votingapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.votingapp.Retrofit.RetrofitClient;
import com.example.votingapp.Retrofit.RetrofitInterface;
import com.github.mikephil.charting.charts.PieChart;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

//This class get the results for chosen poll and displays them in the view
public class AlternativeVoteResult extends AppCompatActivity {

    com.example.votingapp.Retrofit.RetrofitInterface RetrofitInterface;
    TextView winner, option1, option2, option3, option1votes, option2votes,
            option3votes, candidate, votes, method_used, firstRound, secondRound,
            option1second, option2second, option3second, option1votessecond,
            option2votessecond, option3votessecond, candidate2, votes2;

    String option1text;
    String option2text;
    String option3text;

    SharedPreferences sharedPreferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alternative_vote_result);


        //creating a retrofit instance
        Retrofit retrofitClient = RetrofitClient.getInstance();
        RetrofitInterface = retrofitClient.create(RetrofitInterface.class);

        //getting the info within the shared preference
        sharedPreferences = getApplicationContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);

//        getting the views within the layout file
        winner = (TextView)findViewById(R.id.winner);
        firstRound = (TextView)findViewById(R.id.firstRound);
        secondRound = (TextView)findViewById(R.id.secondRound);

        option1 = (TextView)findViewById(R.id.option1);
        option2 = (TextView)findViewById(R.id.option2);
        option3 = (TextView)findViewById(R.id.option3);

        option1second = (TextView)findViewById(R.id.option1second);
        option2second = (TextView)findViewById(R.id.option2second);
        option3second = (TextView)findViewById(R.id.option3second);

        option1votes = (TextView)findViewById(R.id.option1votes);
        option2votes = (TextView)findViewById(R.id.option2votes);
        option3votes = (TextView)findViewById(R.id.option3votes);

        option1votessecond=(TextView)findViewById(R.id.option1votessecond);
        option2votessecond=(TextView)findViewById(R.id.option2votessecond);
        option3votessecond=(TextView)findViewById(R.id.option3votessecond);

        candidate = (TextView)findViewById(R.id.candidate);
        votes = (TextView)findViewById(R.id.votes);
        candidate2 = (TextView)findViewById(R.id.candidate2);
        votes2 = (TextView)findViewById(R.id.votes2);
        method_used = (TextView)findViewById(R.id.method_used);

        //setting the text for certain views
        candidate.setText("Candidate");
        votes.setText("Number of Votes");
        candidate2.setText("Candidate");
        votes2.setText("Number of Votes");
        firstRound.setText("Votes after the first round");
        secondRound.setText("Votes after the second round");


        //getting the poll code from the intent of the activity
        String code = getIntent().getStringExtra("code");

        //creating a new hashmap
        HashMap<String, String> map = new HashMap<>();

        //putting the poll code in the hashmap
        map.put("code", code);

        //creating a new retrofit call to get the Ranked choice votes
        Call<Poll> call = RetrofitInterface.getAVResults(map);

        //queuing the cal
        call.enqueue(new Callback<Poll>() {
            @Override
            public void onResponse(Call<Poll> call, Response<Poll> response) {
                //if the call went okay
                if(response.code() == 201){

                    //getting the response for the poll
                    Poll polls = response.body();

                    //putting the response text into the textviews on the layout page
                    option1text = polls.getOptions().getOption1();
                    option2text = polls.getOptions().getOption2();
                    option3text = polls.getOptions().getOption3();
                    winner.setText("The Current winner of the poll is " + polls.getWinner());
                    option1.setText(option1text);
                    option2.setText(option2text);
                    option3.setText(option3text);
                    option1votes.setText(polls.getFirstRound().getOption1firstroundtotalvotes());
                    option2votes.setText(polls.getFirstRound().getOption2firstroundtotalvotes());
                    option3votes.setText(polls.getFirstRound().getOption3firstroundtotalvotes());
                    option1second.setText(option1text);
                    option2second.setText(option2text);
                    option3second.setText(option3text);
                    option1votessecond.setText(polls.getSecondRound().getOption1secondroundtotalvotes());
                    option2votessecond.setText(polls.getSecondRound().getOption2secondroundtotalvotes());
                    option3votessecond.setText(polls.getSecondRound().getOption3secondroundtotalvotes());
                    method_used.setText("The method used for this poll was: " + polls.getVotingmethod());

                }
            }

            @Override
            public void onFailure(Call<Poll> call, Throwable t) {
                //toast message if an error occurs
                Toast.makeText(AlternativeVoteResult.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }


    //creating the top menu bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //creating a new menu inflater
        MenuInflater inflater = getMenuInflater();
        //setting which menu to inflate it with
        inflater.inflate(R.menu.topbar_menu, menu);
        return true;
    }

    //method for when an item is selected from the menu bar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            //if the id selected is for the log out button
            case R.id.LogOut:
                //get a shared preference editor
                SharedPreferences.Editor editor = sharedPreferences.edit();
                //update the values withing the preferences
                editor.putString("_id", "");
                editor.putString("email", "");
                editor.putBoolean("Logged in", false);
                //apply the updates
                editor.apply();
                //create a new intent for the main activity(Log in page)
                Intent intent = new Intent(AlternativeVoteResult.this, MainActivity.class);
                //start the activity
                startActivity(intent);
                //finish this activity
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
