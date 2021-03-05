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

public class PollingMethodsExplained extends AppCompatActivity {

    Button back, submit;
    TextView about, popularvotetitle, popularvotetext, majorityvotetitle, majorityvotetext, rankedchoicetitle, rankedchoicetext;
    SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.polling_methods_explained);

        sharedPreferences = getApplicationContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);

        back = (Button)findViewById(R.id.back);
        submit = (Button)findViewById(R.id.submit);
        about = (TextView)findViewById(R.id.About);
        popularvotetitle = (TextView)findViewById(R.id.PopularVoteTitle);
        popularvotetext = (TextView)findViewById(R.id.PopularVote);
        majorityvotetitle = (TextView)findViewById(R.id.MajorityVoteTitle);
        majorityvotetext = (TextView)findViewById(R.id.MajorityVote);
        rankedchoicetitle = (TextView)findViewById(R.id.AlternativeVoteTitle);
        rankedchoicetext = (TextView)findViewById(R.id.AlternativeVote);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PollingMethodsExplained.this, CreatePoll.class);
                startActivity(intent);
            }
        });

    }

    public void SwitchScreen(View view) {
        finish();
        Intent intent = new Intent(PollingMethodsExplained.this, Home.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.topbar_menu, menu);
        this.setTitle("Polling methods explained");
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
                Intent intent = new Intent(PollingMethodsExplained.this, MainActivity.class);
                startActivity(intent);
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
