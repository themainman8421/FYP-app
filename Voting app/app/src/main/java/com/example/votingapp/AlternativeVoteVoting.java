package com.example.votingapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.votingapp.Retrofit.RetrofitClient;
import com.example.votingapp.Retrofit.RetrofitInterface;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

//Spinner code from https://android--code.blogspot.com/2015/08/android-spinner-hint.html

public class AlternativeVoteVoting extends AppCompatActivity {

    RetrofitInterface RetrofitInterface;
    SharedPreferences sharedPreferences;
    TextView title, option1, option2, option3;
    Button submitbtn, back;
    String option1choice;
    String option2choice;
    String option3choice;
    String firstPreference;
    String secondPreference;
    String thirdPreference;
    String email;
    String code;
    Spinner spinner, spinner2, spinner3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alternative_vote_voting);

        Retrofit retrofitClient = RetrofitClient.getInstance();
        RetrofitInterface = retrofitClient.create(RetrofitInterface.class);

        sharedPreferences = getApplicationContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        email = sharedPreferences.getString("email", "");

        title = findViewById(R.id.title);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        submitbtn = findViewById(R.id.submitbtn);
        back = findViewById(R.id.back);

        code = getIntent().getStringExtra("code");
        Log.d("TAG", code);

        // Spinner element
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        spinner3 = (Spinner) findViewById(R.id.spinner3);

        // Initializing a String Array
        String[] preferences = new String[]{
                "Select an item...",
                "First Preference",
                "Second Preference",
                "Third Preference"
        };


        HashMap<String, String> map = new HashMap<>();
        map.put("code", code);
        map.put("email", email);
        Log.d("TAG", "onCreate() returned: " +  map);



        final List<String> preferenceList = new ArrayList<>(Arrays.asList(preferences));

        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.spinner_item,preferenceList){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(spinnerArrayAdapter);
        spinner2.setAdapter(spinnerArrayAdapter);
        spinner3.setAdapter(spinnerArrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
//                if(position == 0){
//                    nochoice = selectedItemText;
//                }
                if(position > 0){

                    option1choice = selectedItemText;
                    Log.d("TAG", "onItemSelected() returned: " + option1choice);
                }else{
                    option2choice = "null";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);


                if(position > 0){
                    option2choice = selectedItemText;
                    Log.d("TAG", "onItemSelected() returned: " + option2choice);
                }else{
                    option2choice = "null";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);

                if(position > 0){
                    option3choice = selectedItemText;
                    Log.d("TAG", "onItemSelected() returned: " + option3choice);
                } else {
                    option3choice = "null";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Call<Poll> call = RetrofitInterface.getPoll(map);

        call.enqueue(new Callback<Poll>() {
            @Override
            public void onResponse(Call<Poll> call, Response<Poll> response) {

                if(response.code() == 201){

                    Poll poll = response.body();

                    title.setText(poll.getTitle());
                    option1.setText(poll.getOptions().getOption1());
                    option2.setText(poll.getOptions().getOption2());
                    option3.setText(poll.getOptions().getOption3());
                }

            }

            @Override
            public void onFailure(Call<Poll> call, Throwable t) {
                Toast.makeText(AlternativeVoteVoting.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }


        });




        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item = "0";
                if (spinner.getSelectedItemPosition() == 0 || spinner2.getSelectedItemPosition() == 0 || spinner3.getSelectedItemPosition() == 0){
                    Toast.makeText(AlternativeVoteVoting.this, "All candidates must have a preferences", Toast.LENGTH_LONG).show();
                }
                else if(option1choice.equals(option2choice) || option1choice.equals(option3choice) || option2choice.equals(option3choice)){
                    Toast.makeText(AlternativeVoteVoting.this, "All candidates must have different preferences", Toast.LENGTH_LONG).show();
                }else{

                    if (option1choice.equals("First Preference")){
                        firstPreference = (String) option1.getText();
                    }else if (option1choice.equals("Second Preference")){
                        secondPreference = (String) option1.getText();
                    }else if(option1choice.equals("Third Preference")){
                        thirdPreference = (String) option1.getText();
                    }

                    if (option2choice.equals("First Preference")){
                        firstPreference = (String) option2.getText();
                    }else if (option2choice.equals("Second Preference")){
                        secondPreference = (String) option2.getText();
                    }else if(option2choice == "Third Preference"){
                        thirdPreference = (String) option2.getText();
                    }

                    if (option3choice.equals("First Preference")){
                        firstPreference = (String) option3.getText();
                    }else if (option3choice.equals("Second Preference")){
                        secondPreference = (String) option3.getText();
                    }else if(option3choice.equals("Third Preference")){
                        thirdPreference = (String) option3.getText();
                    }

                    HashMap<String, String> map = new HashMap<>();
                    map.put("code", code);
                    map.put("firstPreference", firstPreference);
                    map.put("secondPreference", secondPreference);
                    map.put("thirdPreference", thirdPreference);
                    map.put("email", email);

                    Call<Void> call = RetrofitInterface.increaseAVVote(map);

                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if(response.code() == 200){

                                Call<Void> call2 = RetrofitInterface.addPollVotedOn(map);
                                call2.enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        if(response.code() == 200){
                                            Toast.makeText(AlternativeVoteVoting.this, "Success!", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(AlternativeVoteVoting.this, AlternativeVoteResult.class);
                                            intent.putExtra("code", code);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {
                                        Toast.makeText(AlternativeVoteVoting.this, t.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });


//                                Toast.makeText(AlternativeVoteVoting.this, "Success!", Toast.LENGTH_LONG).show();
//                                Intent intent = new Intent(AlternativeVoteVoting.this, AlternativeVoteResult.class);
//                                intent.putExtra("code", code);
//                                startActivity(intent);

                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(AlternativeVoteVoting.this, t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

                }


            }
        });

    }


    public void FinishScreen(View view) {
        Intent intent = new Intent(AlternativeVoteVoting.this, Home.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.topbar_menu, menu);
        this.setTitle("Alternative Vote");
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
                Intent intent = new Intent(AlternativeVoteVoting.this, MainActivity.class);
                startActivity(intent);
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}


