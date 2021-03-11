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

        Retrofit retrofitClient = RetrofitClient.getInstance();
        RetrofitInterface = retrofitClient.create(RetrofitInterface.class);

        sharedPreferences = getApplicationContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);

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

        candidate.setText("Candidate");
        votes.setText("Number of Votes");
        candidate2.setText("Candidate");
        votes2.setText("Number of Votes");
        firstRound.setText("Votes after the first round");
        secondRound.setText("Votes after the second round");


        String code = getIntent().getStringExtra("code");

        HashMap<String, String> map = new HashMap<>();
        map.put("code", code);

        Call<Poll> call = RetrofitInterface.getAVResults(map);

        call.enqueue(new Callback<Poll>() {
            @Override
            public void onResponse(Call<Poll> call, Response<Poll> response) {
                if(response.code() == 201){

                    Poll polls = response.body();

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
                Toast.makeText(AlternativeVoteResult.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }

    public void FinishScreen(View view) {
        Intent intent = new Intent(AlternativeVoteResult.this, Home.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.topbar_menu, menu);
        this.setTitle("Ranked Choice Result");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.LogOut:
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("_id", "");
                editor.putString("email", "");
                editor.putBoolean("Logged in", false);
                editor.apply();
                Intent intent = new Intent(AlternativeVoteResult.this, MainActivity.class);
                startActivity(intent);
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
