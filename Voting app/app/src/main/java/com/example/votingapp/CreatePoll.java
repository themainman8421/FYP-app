package com.example.votingapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.votingapp.Retrofit.RetrofitClient;
import com.example.votingapp.Retrofit.RetrofitInterface;

import java.util.HashMap;

import retrofit2.Retrofit;

public class CreatePoll extends Activity {

    RetrofitInterface RetrofitInterface;

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
                HashMap<String, String> map = new HashMap<>();

                map.put("title", title.getText().toString());
                map.put("option1", option1.getText().toString());
                map.put("option2", option2.getText().toString());
                map.put("option3", option3.getText().toString());
                map.put("code", pollcodeedittext.getText().toString());
                map.put("votingmethod", radioGroup.getTransitionName().toString());

            }
        });

    }

    public void switchScreen(View v)
    {
        finish();
    }
}