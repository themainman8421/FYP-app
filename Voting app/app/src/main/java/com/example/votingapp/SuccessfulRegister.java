package com.example.votingapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.EditText;
import android.widget.TextView;

public class SuccessfulRegister extends Activity {

    TextView success;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.successful_register);

        success = (TextView) findViewById(R.id.success);

//        https://stackoverflow.com/questions/6304035/how-to-display-an-activity-automatically-after-5-seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final Intent mainIntent = new Intent(SuccessfulRegister.this, MainActivity.class);
                SuccessfulRegister.this.startActivity(mainIntent);
                SuccessfulRegister.this.finish();
            }
        }, 5000);
    }
}
