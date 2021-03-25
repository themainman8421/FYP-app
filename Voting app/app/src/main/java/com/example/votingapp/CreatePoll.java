package com.example.votingapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
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

//This activity will allow a user to create a poll
public class CreatePoll extends AppCompatActivity {

    RetrofitInterface RetrofitInterface;
    SharedPreferences sharedPreferences;
    EditText title, option1, option2, option3, pollcodeedittext;
    Button submitbtn;
    RadioButton radioButton, radioButton2, radioButton3;
    RadioGroup radioGroup;
    Switch switchButton;
    String switchstatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_poll);

        //creating a instance of retrofit
        Retrofit retrofitClient = RetrofitClient.getInstance();
        RetrofitInterface = retrofitClient.create(RetrofitInterface.class);

        //get the shared preferences
        sharedPreferences = getApplicationContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);

        //getting the views within the layout file
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
        switchButton = findViewById(R.id.switchButton);


        title.setSingleLine(true);
        option1.setSingleLine(true);
        option2.setSingleLine(true);
        option3.setSingleLine(true);
        pollcodeedittext.setSingleLine(true);

        option1.setTextColor(Color.HSVToColor(new float[]{ 30f, 86f, 86f } ));


        //setting a onclick listener for the submit button
        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //getting the id of the radio button
                int radioButtonID = radioGroup.getCheckedRadioButtonId();
                View radioButton = radioGroup.findViewById(radioButtonID);
                int idx = radioGroup.indexOfChild(radioButton);

                //getting the string of the text views
                String titlestr = title.getText().toString();
                String option1str = option1.getText().toString();
                String option2str = option2.getText().toString();
                String pollcodestr = pollcodeedittext.getText().toString();

                //checking that all fields have been filled before submitting the form
                if (titlestr.matches("") && option1str.matches("") && option2str.matches("") && pollcodestr.matches("")) {
                    title.setError("Title cannot be blank");
                    option1.setError("Option cannot be blank");
                    option2.setError("Option cannot be blank");
                    pollcodeedittext.setError("Poll code cannot be blank");
                }else if(option1str.matches("") && option2str.matches("") && pollcodestr.matches("")){
                    option1.setError("Option cannot be blank");
                    option2.setError("Option cannot be blank");
                    pollcodeedittext.setError("Poll code cannot be blank");
                }else if(option2str.matches("") && pollcodestr.matches("")){
                    option2.setError("Option cannot be blank");
                    pollcodeedittext.setError("Poll code cannot be blank");
                }else if(titlestr.matches("")){
                    title.setError("Title cannot be blank");
                }else if(option1str.matches("")){
                    option1.setError("Option cannot be blank");
                }else if(option2str.matches("")){
                    option2.setError("Option cannot be blank");
                }else if(pollcodestr.matches("")){
                    pollcodeedittext.setError("Poll code cannot be blank");
                }
                else if(idx == -1){
                    Toast.makeText(CreatePoll.this, "Please select a voting method", Toast.LENGTH_LONG).show();
                }
                else {

                    if(switchButton.isChecked()){
                        switchstatus = "true";
                    }else{
                        switchstatus = "false";
                    }

                    //https://stackoverflow.com/questions/6440259/how-to-get-the-selected-index-of-a-radiogroup-in-android



                    //getting the string contained within the selected button
                    RadioButton r = (RadioButton) radioGroup.getChildAt(idx);
                    String selectedtext = r.getText().toString();

                    //creating a new hashmap
                    HashMap<String, String> map = new HashMap<>();

                    //inserting into the text into the hashmap
                    map.put("title", title.getText().toString());
                    map.put("option1", option1.getText().toString());
                    map.put("option2", option2.getText().toString());
                    map.put("option3", option3.getText().toString());
                    map.put("code", pollcodeedittext.getText().toString());
                    map.put("winner", "there current is no winner");
                    map.put("votingmethod", selectedtext);
                    map.put("private", switchstatus);


                    //creating a retrofit call to check if the poll code entered is in use
                    Call<Void> call = RetrofitInterface.pollInUse(map);

                    //queuing the call
                    call.enqueue((new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            //if the code is in use send a toast message
                            if (response.code() == 400) {
                                pollcodeedittext.setError("Poll code already in use");
                            } else {

                                //if the radio button index is 0 or 1 create a new retrofit call to store the poll for Popular Vote or Majority Vote
                                if (0 == idx || 1 == idx) {
                                    //creating the call
                                    Call<Void> secondCall = RetrofitInterface.executenewPoll(map);

                                    //queuing the call
                                    secondCall.enqueue(new Callback<Void>() {
                                        @Override
                                        public void onResponse(Call<Void> call, Response<Void> response) {
                                            //send a toast messsage if the poll was created
                                            if (response.code() == 200) {
                                                Toast.makeText(CreatePoll.this, "Poll created", Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(CreatePoll.this, SuccessfulPollCreated.class);
                                                intent.putExtra("code", pollcodeedittext.getText().toString());
                                                startActivity(intent);
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Void> call, Throwable t) {
                                            //send toast message if an error occurred
                                            Toast.makeText(CreatePoll.this, t.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    });
                                    //if the radio index is 2 create a new retrofit index for storing an ranked choice vote
                                } else if (2 == idx) {
                                    //creating a new retrofit call to create an a ranked choice poll
                                    Call<Void> thirdCall = RetrofitInterface.createAlternativeVotePoll(map);

                                    //queuing the call
                                    thirdCall.enqueue(new Callback<Void>() {
                                        @Override
                                        public void onResponse(Call<Void> call, Response<Void> response) {
                                            //send toast message if everything went okay
                                            if (response.code() == 200) {
                                                Toast.makeText(CreatePoll.this, "Poll created", Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(CreatePoll.this, SuccessfulPollCreated.class);
                                                intent.putExtra("code", pollcodeedittext.getText().toString());
                                                startActivity(intent);
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Void> call, Throwable t) {
                                            //if theres an error create a toast message
                                            Toast.makeText(CreatePoll.this, t.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }

                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            //if theres an error create a toast message
                            Toast.makeText(CreatePoll.this, t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }));

                }
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
                editor.putString("_id", "");
                editor.putString("email", "");
                editor.putBoolean("Logged in", false);
                //apply the updates
                editor.apply();
                //create a new intent for the main activity(Log in page)
                Intent intent = new Intent(CreatePoll.this, MainActivity.class);
                //start the activity
                startActivity(intent);
                //finish this activity
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}