package com.example.votingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.votingapp.Retrofit.RetrofitClient;
import com.example.votingapp.Retrofit.RetrofitInterface;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

//this activity is the login screen for the user
public class MainActivity extends Activity {

    RetrofitInterface RetrofitInterface;
    SharedPreferences sharedPreferences;

    EditText emailEdit, passwordEdit;
    Button loginbtn;
    ImageView voteImage;
    TextView register;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //creating a retrofit instance
        Retrofit retrofitClient = RetrofitClient.getInstance();
        RetrofitInterface = retrofitClient.create(RetrofitInterface.class);

        //getting the shared preference
        sharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);

        //if the users preference for "logged in" is set to true bring them to the home page
        if(sharedPreferences.getBoolean("Logged in", false) == true){
            //create a new intent for the home activity
            Intent intent = new Intent(MainActivity.this, Home.class);
            //start the activity
            startActivity(intent);
            //finish this activity
            finish();
        }

        //getting the views within the layout file
        emailEdit = (EditText)findViewById(R.id.emailEdit);
        passwordEdit = (EditText)findViewById(R.id.passwordEdit);
        loginbtn = (Button) findViewById(R.id.loginbtn);
        voteImage = (ImageView)findViewById(R.id.voteImage);
        register = (TextView)findViewById(R.id.register);
        voteImage.setImageResource(R.drawable.mobile_vote_image);

        //Creating a string for users who need to register
        String text = ("Dont have an account? Click here to Register");

        //making that string spannable
        SpannableString spannableString = new SpannableString(text);

        //making a clickable spannable string
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                //creating a new intent for the register activity
                Intent intent = new Intent(MainActivity.this, Register.class);
                //start the activity
                startActivity(intent);
            }
        };

        //setting the spannable string to make a link
        spannableString.setSpan(clickableSpan, 22, 44, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        //setting the text for the register textview as the spannable string
        register.setText(spannableString);
        //setting the movement method
        register.setMovementMethod(LinkMovementMethod.getInstance());

        //setting a onclick listener for the login button
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //creating a hash map
                HashMap<String, String> map = new HashMap<>();

                //getting the email from the edit text
                email = emailEdit.getText().toString();

                //putting the info in the hash map
                map.put("email", email);
                map.put("password", passwordEdit.getText().toString());

                //getting an editor shared preference
                SharedPreferences.Editor editor = sharedPreferences.edit();

                //creating a new retrofit call for login
                Call<User> call = RetrofitInterface.executeLogin(map);

                //queueing the call
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {

                        //if everything goes okay
                        if (response.code() == 200){
                            //getting the user info from the response
                            User user = response.body();
                            //creating a toast message for the successful login
                            Toast.makeText(MainActivity.this, "You have successfully logged in", Toast.LENGTH_LONG).show();

                            //making sure that user isnt null
                            assert user != null;
                            //getting the user id
                            String id = user.get_id();
                            //storing the info into the shared preference
                            editor.putString("_id", id);
                            editor.putBoolean("Logged in", true);
                            //committing it to the shared preference
                            editor.commit();

                            //creating a new intent for the home page
                            Intent intent = new Intent(MainActivity.this, Home.class);
                            //starting the intent
                            startActivity(intent);
                            //finish this activity
                            finish();

                            //if the email isnt valid set an error message
                        } else if (response.code() == 404){
                            emailEdit.setError("Email is not registered or verified");

                            //if password is incorrect set a error message
                        } else if (response.code() == 401){
                            passwordEdit.setError("Password is incorrect");
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        //if an error occurs set a toast message
                        Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
    

}