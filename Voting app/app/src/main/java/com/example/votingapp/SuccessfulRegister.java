package com.example.votingapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.EditText;
import android.widget.TextView;

//class that shows the user they have registered successfully and need to verify
public class SuccessfulRegister extends Activity {

    TextView success;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.successful_register);

        //finding the text view in the layout file
        success = (TextView) findViewById(R.id.success);

//        https://stackoverflow.com/questions/6304035/how-to-display-an-activity-automatically-after-5-seconds
        //delaying the page from moving on
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //create a new intent for the main activity(Log in page)
                final Intent mainIntent = new Intent(SuccessfulRegister.this, MainActivity.class);
                //start the new activity
                SuccessfulRegister.this.startActivity(mainIntent);
                //finish this activity
                SuccessfulRegister.this.finish();
            }
        }, 5000);
    }
}
