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
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.votingapp.Retrofit.RetrofitClient;
import com.example.votingapp.Retrofit.RetrofitInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Voting extends AppCompatActivity {

    RetrofitInterface RetrofitInterface;
    SharedPreferences sharedPreferences;
    RadioButton option1, option2, option3;
    TextView titleTextView;
    RadioGroup radioGroup;
    Button submitbtn;
    String votingMethod, id, code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.voting);

        Retrofit retrofitClient = RetrofitClient.getInstance();
        RetrofitInterface = retrofitClient.create(RetrofitInterface.class);

        sharedPreferences = getApplicationContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);

        id = sharedPreferences.getString("_id", "");

        code = getIntent().getStringExtra("code");

        titleTextView = findViewById(R.id.titleTextView);
        option1 = findViewById(R.id.radioButton);
        option2 = findViewById(R.id.radioButton2);
        option3 = findViewById(R.id.radioButton3);
        submitbtn = findViewById(R.id.submitbtn);
        radioGroup = findViewById(R.id.radioGroup);

        HashMap<String, String> map = new HashMap<>();
        map.put("code", code);
        map.put("id", id);

        Call<Poll> call = RetrofitInterface.getPoll(map);

        call.enqueue(new Callback<Poll>() {
            @Override
            public void onResponse(Call<Poll> call, Response<Poll> response) {
                if(response.code() == 200){

                    Poll poll = response.body();

                    titleTextView.setText(poll.getTitle());
                    option1.setText(poll.getOptions().getOption1());
                    option2.setText(poll.getOptions().getOption2());
                    option3.setText(poll.getOptions().getOption3());
                    votingMethod = poll.getVotingmethod();
                }
            }

            @Override
            public void onFailure(Call<Poll> call, Throwable t) {
                Toast.makeText(Voting.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int radioButtonID = radioGroup.getCheckedRadioButtonId();
                View radioButton = radioGroup.findViewById(radioButtonID);
                int idx = radioGroup.indexOfChild(radioButton);
                Log.d("radio", "onCreate() returned: " + idx);
                if(idx == -1){
                    Toast.makeText(Voting.this, "You must select an option", Toast.LENGTH_LONG).show();
                }else {
                    if (idx == 0) {
                        map.put("optionchosen", "option1");
                    } else if (idx == 1) {
                        map.put("optionchosen", "option2");
                    } else if (idx == 2) {
                        map.put("optionchosen", "option3");
                    }
                    Log.d("radio", "onCreate() returned: " + map);

                    Call<Void> call = RetrofitInterface.increaseVote(map);

                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.code() == 200) {

                                Call<Void> call2 = RetrofitInterface.addPollVotedOn(map);

                                call2.enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        if (response.code() == 200) {
                                            Toast.makeText(Voting.this, "Vote successfully placed", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(Voting.this, Results.class);
                                            intent.putExtra("code", code);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {
                                        Toast.makeText(Voting.this, t.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });

//                            Toast.makeText(Voting.this, "Vote successfully placed", Toast.LENGTH_LONG).show();
//                            Intent intent = new Intent(Voting.this, Results.class);
//                            intent.putExtra("code", code);
//                            startActivity(intent);
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(Voting.this, t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });


    }

    public void switchScreen(View v)
    {
        Intent intent = new Intent(Voting.this, Home.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.topbar_menu, menu);
        this.setTitle("Voting");
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
                Intent intent = new Intent(Voting.this, MainActivity.class);
                startActivity(intent);
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
