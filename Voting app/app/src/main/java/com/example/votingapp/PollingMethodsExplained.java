package com.example.votingapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

//class that displays how the polls work
public class PollingMethodsExplained extends AppCompatActivity {

    Button submit;
    TextView about, popularvotetitle, popularvotetext, majorityvotetitle, majorityvotetext, rankedchoicetitle, rankedchoicetext;
    SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.polling_methods_explained);

        //getting the shared preferences
        sharedPreferences = getApplicationContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);


        //Getting the views in the layout file
        submit = (Button)findViewById(R.id.submit);
        about = (TextView)findViewById(R.id.About);
        popularvotetitle = (TextView)findViewById(R.id.PopularVoteTitle);
        popularvotetext = (TextView)findViewById(R.id.PopularVote);
        majorityvotetitle = (TextView)findViewById(R.id.MajorityVoteTitle);
        majorityvotetext = (TextView)findViewById(R.id.MajorityVote);
        rankedchoicetitle = (TextView)findViewById(R.id.AlternativeVoteTitle);
        rankedchoicetext = (TextView)findViewById(R.id.AlternativeVote);

        //adding a onclick listener on the submit button
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create a new intent for the create poll activity
                Intent intent = new Intent(PollingMethodsExplained.this, CreatePoll.class);
                //starting the activity
                startActivity(intent);
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
                editor.putString("email", "");
                editor.putBoolean("Logged in", false);
                //apply the updates
                editor.apply();
                //create a new intent for the main activity(Log in page)
                Intent intent = new Intent(PollingMethodsExplained.this, MainActivity.class);
                //start the activity
                startActivity(intent);
                //finish this activity
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
