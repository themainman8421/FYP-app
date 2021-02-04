package com.example.votingapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.votingapp.Retrofit.RetrofitClient;
import com.example.votingapp.Retrofit.RetrofitInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Voting extends Activity {

    RetrofitInterface RetrofitInterface;
    RadioButton option1, option2, option3;
    TextView titleTextView;
    RadioGroup radioGroup;
    Button submitbtn;

    Options options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.voting);

        Retrofit retrofitClient = RetrofitClient.getInstance();
        RetrofitInterface = retrofitClient.create(RetrofitInterface.class);

        String code = getIntent().getStringExtra("code");

        titleTextView = findViewById(R.id.titleTextView);
        option1 = findViewById(R.id.radioButton);
        option2 = findViewById(R.id.radioButton2);
        option3 = findViewById(R.id.radioButton3);
        submitbtn = findViewById(R.id.submitbtn);
        radioGroup = findViewById(R.id.radioGroup);

        HashMap<String, String> map = new HashMap<>();
        map.put("code", code);

        Call<List<Poll>> call = RetrofitInterface.getPoll(map);

        call.enqueue(new Callback<List<Poll>>() {
            @Override
            public void onResponse(Call<List<Poll>> call, Response<List<Poll>> response) {

                if (response.code() == 200 ){

                    List<Poll> polls = response.body();

                    for(Poll poll : polls){


                        titleTextView.setText(poll.getTitle());
                        option1.setText(poll.getOptions().getOption1());
                        option2.setText(poll.getOptions().getOption2());
                        option3.setText(poll.getOptions().getOption3());
                    }
                }

            }

            @Override
            public void onFailure(Call<List<Poll>> call, Throwable t) {
                Toast.makeText(Voting.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap<String, String> map = new HashMap<>();
                map.put("code", code);
                int radioButtonID = radioGroup.getCheckedRadioButtonId();
                View radioButton = radioGroup.findViewById(radioButtonID);
                int idx = radioGroup.indexOfChild(radioButton);
                Log.d("radio", "onCreate() returned: " + idx);
                if(idx == 0){
                    map.put("optionchosen", "option1");
                } else if (idx == 1){
                    map.put("optionchosen", "option2");
                }else if (idx == 2){
                    map.put("optionchosen", "option3");
                }
                Log.d("radio", "onCreate() returned: " + map);

                Call<Void> call = RetrofitInterface.increaseVote(map);

                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.code() == 200){
                            Toast.makeText(Voting.this, "Vote successfully placed", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(Voting.this, Results.class);
                            intent.putExtra("code", code);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(Voting.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });


    }

    public void switchScreen(View v)
    {
        finish();
    }
}
