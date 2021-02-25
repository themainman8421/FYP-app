package com.example.votingapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PollingMethodsExplained extends Activity {

    Button back, submit;
    TextView about, popularvotetitle, popularvotetext, majorityvotetitle, majorityvotetext, rankedchoicetitle, rankedchoicetext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.polling_methods_explained);

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
}
