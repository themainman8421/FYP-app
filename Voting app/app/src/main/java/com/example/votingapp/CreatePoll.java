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
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.votingapp.Retrofit.RetrofitClient;
import com.example.votingapp.Retrofit.RetrofitInterface;

import java.io.Console;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CreatePoll extends AppCompatActivity {

    RetrofitInterface RetrofitInterface;
    SharedPreferences sharedPreferences;
    EditText title, option1, option2, option3, pollcodeedittext;
    Button submitbtn;
    RadioButton radioButton, radioButton2, radioButton3;
    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_poll);

        Retrofit retrofitClient = RetrofitClient.getInstance();
        RetrofitInterface = retrofitClient.create(RetrofitInterface.class);

        sharedPreferences = getApplicationContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);

        title = findViewById(R.id.title);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        pollcodeedittext = findViewById(R.id.pollcodeedittext);
        submitbtn = findViewById(R.id.submitbtn);
        radioGroup = findViewById(R.id.radioGroup);
        radioButton = findViewById(R.id.radioButton);
        radioButton2 = findViewById(R.id.radioButton2);
        radioButton3 = findViewById(R.id.radioButton3);


        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //https://stackoverflow.com/questions/6440259/how-to-get-the-selected-index-of-a-radiogroup-in-android
                int radioButtonID = radioGroup.getCheckedRadioButtonId();
                View radioButton = radioGroup.findViewById(radioButtonID);
                int idx = radioGroup.indexOfChild(radioButton);


                RadioButton r = (RadioButton) radioGroup.getChildAt(idx);
                String selectedtext = r.getText().toString();


                HashMap<String, String> map = new HashMap<>();

                map.put("title", title.getText().toString());
                map.put("option1", option1.getText().toString());
                map.put("option2", option2.getText().toString());
                map.put("option3", option3.getText().toString());
                map.put("code", pollcodeedittext.getText().toString());
//                map.put("code", pollcodeedittext.getText().toString());
                map.put("winner", "there current is no winner");
                map.put("votingmethod", selectedtext);


                Call<Void> call = RetrofitInterface.pollInUse(map);

                call.enqueue((new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.code() == 400){
                            pollcodeedittext.setError("Poll code already in use");
                        }
                        else{

                            if(0 == idx || 1 == idx){
                                Call<Void> secondCall = RetrofitInterface.executenewPoll(map);

                                secondCall.enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        if(response.code() == 200){
                                            Toast.makeText(CreatePoll.this, "Poll created", Toast.LENGTH_LONG).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {
                                        Toast.makeText(CreatePoll.this, t.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });
                            } else if(2 == idx){
                                Call<Void> thirdCall = RetrofitInterface.createAlternativeVotePoll(map);

                                thirdCall.enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        if(response.code() == 200){
                                            Toast.makeText(CreatePoll.this, "Poll created", Toast.LENGTH_LONG).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {
                                        Toast.makeText(CreatePoll.this, t.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(CreatePoll.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }));


//                Call<Void> call = RetrofitInterface.executenewPoll(map);
//
//                call.enqueue(new Callback<Void>() {
//                    @Override
//                    public void onResponse(Call<Void> call, Response<Void> response) {
//
//                        if(response.code() == 200){
//                            Toast.makeText(CreatePoll.this, "Poll created", Toast.LENGTH_LONG).show();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<Void> call, Throwable t) {
//                        Toast.makeText(CreatePoll.this, t.getMessage(), Toast.LENGTH_LONG).show();
//                    }
//                });

            }
        });

    }

    public void switchScreen(View v)
    {
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.topbar_menu, menu);
        this.setTitle("Create Poll");
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
                Intent intent = new Intent(CreatePoll.this, MainActivity.class);
                startActivity(intent);
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}