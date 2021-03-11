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

public class Home extends AppCompatActivity {

    Button Create_new_poll, Join_poll, All_Polls, My_Polls, LogOut;
    SharedPreferences sharedPreferences;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        sharedPreferences = getApplicationContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);


        Create_new_poll = (Button)findViewById(R.id.Create_new_poll);
        Join_poll = (Button)findViewById(R.id.Join_poll);
        All_Polls = (Button)findViewById(R.id.All_Polls);
        My_Polls = (Button)findViewById(R.id.My_Polls);
        LogOut = (Button)findViewById(R.id.LogOut);



        Create_new_poll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, PollingMethodsExplained.class);
                startActivity(intent);
            }
        });

        Join_poll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, JoinPoll.class);
                startActivity(intent);
            }
        });

        All_Polls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, AllPolls.class);
                startActivity(intent);
            }
        });

        My_Polls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, MyPolls.class);
                startActivity(intent);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.topbar_menu, menu);
        this.setTitle("Home");
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
                Intent intent = new Intent(Home.this, MainActivity.class);
                startActivity(intent);
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
