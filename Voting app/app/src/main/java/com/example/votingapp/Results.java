package com.example.votingapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.votingapp.Retrofit.RetrofitClient;
import com.example.votingapp.Retrofit.RetrofitInterface;
import com.github.mikephil.charting.charts.PieChart;


import org.w3c.dom.Text;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Results extends AppCompatActivity {

    RetrofitInterface RetrofitInterface;
    SharedPreferences sharedPreferences;
    TextView winner, option1, option2, option3, option1votes, option2votes, option3votes, candidate, votes, method_used;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results);

        //getting the shared preferences
        sharedPreferences = getApplicationContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);

        //Getting the views from the layout file
        winner = (TextView)findViewById(R.id.winner);
        option1 = (TextView)findViewById(R.id.option1);
        option2 = (TextView)findViewById(R.id.option2);
        option3 = (TextView)findViewById(R.id.option3);
        option1votes = (TextView)findViewById(R.id.option1votes);
        option2votes = (TextView)findViewById(R.id.option2votes);
        option3votes = (TextView)findViewById(R.id.option3votes);
        candidate = (TextView)findViewById(R.id.candidate);
        votes = (TextView)findViewById(R.id.votes);
        method_used = (TextView)findViewById(R.id.method_used);

        //adding text
        candidate.setText("Candidate");
        votes.setText("Number of Votes");

        //getting the code from the intent
        String code = getIntent().getStringExtra("code");

        //creating a retrofit instance
        Retrofit retrofitClient = RetrofitClient.getInstance();
        RetrofitInterface = retrofitClient.create(RetrofitInterface.class);

        //creating a new hashmap and adding to it
        HashMap<String, String> map = new HashMap<>();
        map.put("code", code);

        //creating a new retrofit call to get the results for a popular or majority vote
        Call<Poll> call = RetrofitInterface.getResults(map);
        //queuing the call
        call.enqueue(new Callback<Poll>() {
            @Override
            public void onResponse(Call<Poll> call, Response<Poll> response) {
                //if everything is okay
                if(response.code() == 200 || response.code() == 201 || response.code() == 202 || response.code() == 203
                        || response.code() == 204 || response.code() == 205 || response.code() == 206 || response.code() == 207
                || response.code() == 208 || response.code() == 209 || response.code() == 210){

                    //get the poll info from the response body
                    Poll polls = response.body();

                    //setting the text from the poll info
                    winner.setText("The Current winner of the poll is " + polls.getWinner());
                    option1.setText(polls.getOptions().getOption1());
                    option2.setText(polls.getOptions().getOption2());
                    option3.setText(polls.getOptions().getOption3());
                    option1votes.setText(polls.getVotes().getOption1votes());
                    option2votes.setText(polls.getVotes().getOption2votes());
                    option3votes.setText(polls.getVotes().getOption3votes());
                    method_used.setText("The method used for this poll was: " + polls.getVotingmethod());



                }
            }

            @Override
            public void onFailure(Call<Poll> call, Throwable t) {
                //creating a toast message if an error occurs
                Toast.makeText(Results.this, t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("TAG", "onFailure: ", t);
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
                editor.putString("email", "");
                editor.putBoolean("Logged in", false);
                //apply the updates
                editor.apply();
                //create a new intent for the main activity(Log in page)
                Intent intent = new Intent(Results.this, MainActivity.class);
                //start the activity
                startActivity(intent);
                //finish this activity
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
