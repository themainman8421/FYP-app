package com.example.votingapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

//This activity allows a use to join a poll
public class JoinPoll extends AppCompatActivity {

    RetrofitInterface RetrofitInterface;
    SharedPreferences sharedPreferences;
    EditText pollCodeEditText;
    Button submitbtn;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_poll);

        //Create a retrofit instance
        Retrofit retrofitClient = RetrofitClient.getInstance();
        RetrofitInterface = retrofitClient.create(RetrofitInterface.class);

        //get the info stored in shared preference
        sharedPreferences = getApplicationContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        id = sharedPreferences.getString("_id", "");

        //getting the views within the layout file
        pollCodeEditText = findViewById(R.id.pollCodeEditText);
        submitbtn = findViewById(R.id.submitbtn);

        //setting a onclick listener for the submit button
        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //creating a new hashmap
                HashMap<String, String> map = new HashMap<>();

                //putting the info in the hashmap
                map.put("code", pollCodeEditText.getText().toString());
                map.put("id", id);

                //create a new retrofit call
                Call<Void> call = RetrofitInterface.executepollCode(map);

                //queue the call
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        //if everything went okay and the user hasnt voted on this poll before
                        if(response.code() == 200){

                            //create a new retrofit call to check the kind of poll
                            Call<Poll> call2 = RetrofitInterface.getPoll(map);

                            //queue the poll
                            call2.enqueue(new Callback<Poll>() {
                                @Override
                                public void onResponse(Call<Poll> call, Response<Poll> response) {
                                    //if the poll is a Popular vote or Majority vote
                                    if(response.code() == 200){
                                        //start a new intent for the voting activity
                                        Intent intent = new Intent(JoinPoll.this, Voting.class);
                                        //put the code into the intent
                                        intent.putExtra("code", pollCodeEditText.getText().toString());
                                        //start the activity
                                        startActivity(intent);

                                        //if the poll is Ranked choice
                                    }else if(response.code() == 201){
                                        //start a new intent for the alternativevote activity
                                        Intent intent = new Intent(JoinPoll.this, AlternativeVoteVoting.class);
                                        //put the code into the intent
                                        intent.putExtra("code", pollCodeEditText.getText().toString());
                                        //start the activity
                                        startActivity(intent);
                                    }
                                }

                                @Override
                                public void onFailure(Call<Poll> call, Throwable t) {
                                    //if and error occurs send a toast message
                                    Toast.makeText(JoinPoll.this, t.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });

                            //else if the user has voted on the poll and its ranked choice
                        }else if(response.code() == 201){
                            //create a new intent for the alternative vote result
                            Intent intent = new Intent(JoinPoll.this, AlternativeVoteResult.class);
                            //put the code into the intent
                            intent.putExtra("code", pollCodeEditText.getText().toString());
                            //start the activity
                            startActivity(intent);

                            //else if the user has voted on the poll and its Popular or Majority vote
                        }else if(response.code() == 202){
                            //create a new intent for the  result
                            Intent intent = new Intent(JoinPoll.this, Results.class);
                            //put the code into the intent
                            intent.putExtra("code", pollCodeEditText.getText().toString());
                            //start the activity
                            startActivity(intent);
                            //if the poll code doesnt exist send a toast message
                        } else if(response.code() == 400){
                            Toast.makeText(JoinPoll.this, "Poll does not exist", Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        //if an error occurs send a toast message
                        Toast.makeText(JoinPoll.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
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
                Intent intent = new Intent(JoinPoll.this, MainActivity.class);
                //start the activity
                startActivity(intent);
                //finish this activity
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}