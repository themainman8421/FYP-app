package com.example.votingapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

public class SuccessfulPollCreated extends Activity {

    TextView success;
    String pollcode;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.successful_poll_created);

        pollcode = getIntent().getStringExtra("code");

        //finding the text view in the layout file
        success = (TextView) findViewById(R.id.success);

        String text = getString(R.string.success_poll_created, pollcode);
        success.setText(text);

//        https://stackoverflow.com/questions/6304035/how-to-display-an-activity-automatically-after-5-seconds
        //delaying the page from moving on
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //create a new intent for the main activity(Log in page)
                final Intent mainIntent = new Intent(SuccessfulPollCreated.this, Home.class);
                //start the new activity
                SuccessfulPollCreated.this.startActivity(mainIntent);
                //finish this activity
                SuccessfulPollCreated.this.finish();
            }
        }, 5000);
    }
}
