package com.example.votingapp;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

//the home screen for navigating the app
public class Home extends AppCompatActivity {

    Button Create_new_poll, Join_poll, All_Polls, My_Polls, LogOut;
    SharedPreferences sharedPreferences;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        //gets the shared preference info
        sharedPreferences = getApplicationContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);


        //getting the views within the layout file
        Create_new_poll = (Button)findViewById(R.id.Create_new_poll);
        Join_poll = (Button)findViewById(R.id.Join_poll);
        All_Polls = (Button)findViewById(R.id.All_Polls);
        My_Polls = (Button)findViewById(R.id.My_Polls);
        LogOut = (Button)findViewById(R.id.LogOut);



        //creating an onclick listener for creating a new poll button
        Create_new_poll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create a new intent for pollingmethods explained activity
                Intent intent = new Intent(Home.this, PollingMethodsExplained.class);
                //start the activity
                startActivity(intent);
            }
        });

        //creating an onclick listener for join poll button
        Join_poll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create a new intent for JoinPoll activity
                Intent intent = new Intent(Home.this, JoinPoll.class);
                //start the activity
                startActivity(intent);
            }
        });

        //creating an onclick listener for AllPolls button
        All_Polls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create a new intent for AllPolls activity
                Intent intent = new Intent(Home.this, AllPolls.class);
                //start the activity
                startActivity(intent);
            }
        });

        //creating an onclick listener for MyPolls button
        My_Polls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create a new intent for AllPolls activity
                Intent intent = new Intent(Home.this, MyPolls.class);
                //start the activity
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
                editor.putString("_id", "");
                editor.putString("email", "");
                editor.putBoolean("Logged in", false);
                //apply the updates
                editor.apply();
                //create a new intent for the main activity(Log in page)
                Intent intent = new Intent(Home.this, MainActivity.class);
                //start the activity
                startActivity(intent);
                //finish this activity
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
