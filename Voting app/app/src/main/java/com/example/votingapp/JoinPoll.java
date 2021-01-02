package com.example.votingapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.votingapp.Retrofit.RetrofitClient;
import com.example.votingapp.Retrofit.RetrofitInterface;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class JoinPoll extends Activity {

    RetrofitInterface RetrofitInterface;

    EditText pollCodeEditText;
    Button submitbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_poll);

        Retrofit retrofitClient = RetrofitClient.getInstance();
        RetrofitInterface = retrofitClient.create(RetrofitInterface.class);

        pollCodeEditText = findViewById(R.id.pollCodeEditText);
        submitbtn = findViewById(R.id.submitbtn);

        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap<String, String> map = new HashMap<>();

                map.put("pollcode", pollCodeEditText.getText().toString());

                Call<Void> call = RetrofitInterface.executepollCode(map);

                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.code() == 200){
                            Toast.makeText(JoinPoll.this, "Fetching poll data", Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(JoinPoll.this, Voting.class);
                            intent.putExtra("code", pollCodeEditText.getText().toString());
                            startActivity(intent);

                        }else if(response.code() == 400){
                            Toast.makeText(JoinPoll.this, "Poll does not exist", Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(JoinPoll.this, t.getMessage(), Toast.LENGTH_LONG).show();
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