package com.example.votingapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.votingapp.Retrofit.RetrofitClient;
import com.example.votingapp.Retrofit.RetrofitInterface;
import com.example.votingapp.objects.Poll;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

//Spinner code from https://android--code.blogspot.com/2015/08/android-spinner-hint.html

//This class allows the user to vote on Ranked Choice voting methods
public class AlternativeVoteVoting extends AppCompatActivity {

    RetrofitInterface RetrofitInterface;
    SharedPreferences sharedPreferences;
    TextView title, option1, option2, option3;
    Button submitbtn;
    String option1choice;
    String option2choice;
    String option3choice;
    String firstPreference;
    String secondPreference;
    String thirdPreference;
    String id;
    String code;
    Spinner spinner, spinner2, spinner3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alternative_vote_voting);

        //creating a retrofit instance
        Retrofit retrofitClient = RetrofitClient.getInstance();
        RetrofitInterface = retrofitClient.create(RetrofitInterface.class);

        //getting what is stored within the shared preferences
        sharedPreferences = getApplicationContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        id = sharedPreferences.getString("_id", "");

        //getting the views withing the layout file
        title = findViewById(R.id.title);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        submitbtn = findViewById(R.id.submitbtn);

        //getting the poll code from the intent
        code = getIntent().getStringExtra("code");

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


        //creating a new hashmap
        HashMap<String, String> map = new HashMap<>();

        //putting data into the hashmap
        map.put("code", code);
        map.put("id", id);

        //creating a new list with the preferences
        final List<String> preferenceList = new ArrayList<>(Arrays.asList(preferences));

        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.spinner_item,preferenceList){

            //disabling the first option in the list
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

                //getting the position within the spinner and setting the first one to grey
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

        //setting the layout for the spinners
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(spinnerArrayAdapter);
        spinner2.setAdapter(spinnerArrayAdapter);
        spinner3.setAdapter(spinnerArrayAdapter);


        //setting an on item select listener for the spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                //get the text if the position is greater than 0
                if(position > 0){

                    //storing the text
                    option1choice = selectedItemText;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //setting an on item select listener for the spinner
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);

                //get the text if the position is greater than 0
                if(position > 0){
                    //storing the text
                    option2choice = selectedItemText;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //setting an on item select listener for the spinner
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);

                //get the text if the position is greater than 0
                if(position > 0){
                    //storing the text
                    option3choice = selectedItemText;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //creating a retrofit call that gets the poll
        Call<Poll> call = RetrofitInterface.getPoll(map);

        //queuing the call
        call.enqueue(new Callback<Poll>() {
            @Override
            public void onResponse(Call<Poll> call, Response<Poll> response) {

                //if everything was okay
                if(response.code() == 201){

                    //store the body of the response
                    Poll poll = response.body();

                    //set the text from the poll gotten
                    title.setText(poll.getTitle());
                    option1.setText(poll.getOptions().getOption1());
                    option2.setText(poll.getOptions().getOption2());
                    option3.setText(poll.getOptions().getOption3());
                }

            }

            @Override
            public void onFailure(Call<Poll> call, Throwable t) {
                //if error show a toast message
                Toast.makeText(AlternativeVoteVoting.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }


        });




        //create a onclick listener for the submit button
        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if not all options are selected post a toast message
                if (spinner.getSelectedItemPosition() == 0 || spinner2.getSelectedItemPosition() == 0 || spinner3.getSelectedItemPosition() == 0){
                    Toast.makeText(AlternativeVoteVoting.this, "All candidates must have a preferences", Toast.LENGTH_LONG).show();
                }
                //if the same option has been chosen more than once send a toast message
                else if(option1choice.equals(option2choice) || option1choice.equals(option3choice) || option2choice.equals(option3choice)){
                    Toast.makeText(AlternativeVoteVoting.this, "All candidates must have different preferences", Toast.LENGTH_LONG).show();
                }
                //get the text form the option chosen
                else{

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

                    //create a hash map
                    HashMap<String, String> map = new HashMap<>();
                    //put the data into the hash map
                    map.put("code", code);
                    map.put("firstPreference", firstPreference);
                    map.put("secondPreference", secondPreference);
                    map.put("thirdPreference", thirdPreference);
                    map.put("id", id);

                    //create a new retrofit call to increase the vote
                    Call<Void> call = RetrofitInterface.increaseAVVote(map);

                    //queue the call
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            //if everything is okay
                            if(response.code() == 200){

                                //create a new call which adds that poll code to the user info
                                Call<Void> call2 = RetrofitInterface.addPollVotedOn(map);
                                //queue the call
                                call2.enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        //if everything is okay
                                        if(response.code() == 200){
                                            //create a success toast message
                                            Toast.makeText(AlternativeVoteVoting.this, "Success!", Toast.LENGTH_LONG).show();
                                            //create a new intent
                                            Intent intent = new Intent(AlternativeVoteVoting.this, AlternativeVoteResult.class);
                                            //put the poll code into the intent
                                            intent.putExtra("code", code);
                                            //start the activity
                                            startActivity(intent);
                                            //finish this activity
                                            finish();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {
                                        //if an error create a toast message
                                        Toast.makeText(AlternativeVoteVoting.this, t.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });


                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            //if an error create a toast message
                            Toast.makeText(AlternativeVoteVoting.this, t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

                }


            }
        });

    }

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
                Intent intent = new Intent(AlternativeVoteVoting.this, MainActivity.class);
                //start the activity
                startActivity(intent);
                //finish this activity
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}


