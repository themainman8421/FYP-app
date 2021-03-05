package com.example.votingapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.votingapp.Retrofit.RetrofitClient;
import com.example.votingapp.Retrofit.RetrofitInterface;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class JoinPoll extends AppCompatActivity {

    RetrofitInterface RetrofitInterface;
    SharedPreferences sharedPreferences;
    EditText pollCodeEditText;
    Button submitbtn;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_poll);


        Retrofit retrofitClient = RetrofitClient.getInstance();
        RetrofitInterface = retrofitClient.create(RetrofitInterface.class);

        sharedPreferences = getApplicationContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);

        email = sharedPreferences.getString("email", "");

        pollCodeEditText = findViewById(R.id.pollCodeEditText);
        submitbtn = findViewById(R.id.submitbtn);

        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap<String, String> map = new HashMap<>();

                map.put("code", pollCodeEditText.getText().toString());
                map.put("email", email);

                Call<Void> call = RetrofitInterface.executepollCode(map);

                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.code() == 200){

                            Call<Poll> call2 = RetrofitInterface.getPoll(map);

                            call2.enqueue(new Callback<Poll>() {
                                @Override
                                public void onResponse(Call<Poll> call, Response<Poll> response) {
                                    if(response.code() == 200){
                                        Intent intent = new Intent(JoinPoll.this, Voting.class);
                                        intent.putExtra("code", pollCodeEditText.getText().toString());
                                        startActivity(intent);

                                    }else if(response.code() == 201){
                                        Intent intent = new Intent(JoinPoll.this, AlternativeVoteVoting.class);
                                        intent.putExtra("code", pollCodeEditText.getText().toString());
                                        startActivity(intent);
                                    }
                                }

                                @Override
                                public void onFailure(Call<Poll> call, Throwable t) {
                                    Toast.makeText(JoinPoll.this, t.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });

                        }else if(response.code() == 201){
                            Intent intent = new Intent(JoinPoll.this, AlternativeVoteResult.class);
                            intent.putExtra("code", pollCodeEditText.getText().toString());
                            startActivity(intent);

                        }else if(response.code() == 202){
                            Intent intent = new Intent(JoinPoll.this, Results.class);
                            intent.putExtra("code", pollCodeEditText.getText().toString());
                            startActivity(intent);
                        } else if(response.code() == 400){
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


    public  void switchScreen(View v){
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.topbar_menu, menu);
        this.setTitle("Join Poll");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.LogOut:
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("email", "");
                editor.putBoolean("Logged in", false);
                editor.apply();
                Intent intent = new Intent(JoinPoll.this, MainActivity.class);
                startActivity(intent);
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}