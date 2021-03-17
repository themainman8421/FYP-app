package com.example.votingapp;

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
import com.example.votingapp.objects.Poll;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

//activity that allows a user to vote on a popular or majority vote
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

        //creating a retrofit instance
        Retrofit retrofitClient = RetrofitClient.getInstance();
        RetrofitInterface = retrofitClient.create(RetrofitInterface.class);

        //getting shared preferences and whats stored in it
        sharedPreferences = getApplicationContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        id = sharedPreferences.getString("_id", "");
        code = getIntent().getStringExtra("code");

        //finding the views in the layout file
        titleTextView = findViewById(R.id.titleTextView);
        option1 = findViewById(R.id.radioButton);
        option2 = findViewById(R.id.radioButton2);
        option3 = findViewById(R.id.radioButton3);
        submitbtn = findViewById(R.id.submitbtn);
        radioGroup = findViewById(R.id.radioGroup);

        //creating a new hashmap and putting data into it
        HashMap<String, String> map = new HashMap<>();
        map.put("code", code);
        map.put("id", id);

        //creating a new retrofit call that gets the poll
        Call<Poll> call = RetrofitInterface.getPoll(map);

        //queuing the call
        call.enqueue(new Callback<Poll>() {
            @Override
            public void onResponse(Call<Poll> call, Response<Poll> response) {
                //if everything goes okay
                if(response.code() == 200){

                    //get the poll info from the request
                    Poll poll = response.body();

                    //adding the info from the reqest to the views
                    titleTextView.setText(poll.getTitle());
                    option1.setText(poll.getOptions().getOption1());
                    option2.setText(poll.getOptions().getOption2());
                    option3.setText(poll.getOptions().getOption3());
                    votingMethod = poll.getVotingmethod();
                }
            }

            @Override
            public void onFailure(Call<Poll> call, Throwable t) {
                //toast message if an error occurs
                Toast.makeText(Voting.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        //creating a onclick listener for the submit button
        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //getting the index of the radio button
                int radioButtonID = radioGroup.getCheckedRadioButtonId();
                View radioButton = radioGroup.findViewById(radioButtonID);
                int idx = radioGroup.indexOfChild(radioButton);
                Log.d("radio", "onCreate() returned: " + idx);

                //if no option has been selected send a toast message
                if(idx == -1){
                    Toast.makeText(Voting.this, "You must select an option", Toast.LENGTH_LONG).show();
                }
                //put the option chosen into the hashmap
                else {
                    if (idx == 0) {
                        map.put("optionchosen", "option1");
                    } else if (idx == 1) {
                        map.put("optionchosen", "option2");
                    } else if (idx == 2) {
                        map.put("optionchosen", "option3");
                    }
                    Log.d("radio", "onCreate() returned: " + map);

                    //creating a retrofit call that increases the vote
                    Call<Void> call = RetrofitInterface.increaseVote(map);

                    //queueing the call
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            //if everything goes okay
                            if (response.code() == 200) {

                                //create another call to add the poll voted on to the user info
                                Call<Void> call2 = RetrofitInterface.addPollVotedOn(map);

                                //queuing the call
                                call2.enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        //if everything went okay
                                        if (response.code() == 200) {
                                            //a toast message for successfully voting
                                            Toast.makeText(Voting.this, "Vote successfully placed", Toast.LENGTH_LONG).show();
                                            //create a new intent for the main activity(Log in page)
                                            Intent intent = new Intent(Voting.this, Results.class);
                                            //putting the poll code into the intent
                                            intent.putExtra("code", code);
                                            //starting the activity
                                            startActivity(intent);
                                            //finishing this one
                                            finish();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {
                                        //toast message if an error occurs
                                        Toast.makeText(Voting.this, t.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            //toast message if an error occurs
                            Toast.makeText(Voting.this, t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
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
                Intent intent = new Intent(Voting.this, MainActivity.class);
                //start the activity
                startActivity(intent);
                //finish this activity
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
