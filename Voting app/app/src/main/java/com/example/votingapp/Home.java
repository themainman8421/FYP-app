package com.example.votingapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Home extends Activity {

    Button Create_new_poll, Join_poll, All_Polls;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        Create_new_poll = (Button)findViewById(R.id.Create_new_poll);
        Join_poll = (Button)findViewById(R.id.Join_poll);
        All_Polls = (Button)findViewById(R.id.All_Polls);

        findViewById(R.id.Create_new_poll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, CreatePoll.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.Join_poll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, JoinPoll.class);
                startActivity(intent);
            }
        });
    }
}
