package com.example.votingapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.votingapp.Retrofit.RetrofitClient;
import com.example.votingapp.Retrofit.RetrofitInterface;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

//class that allows a user to register
public class Register extends Activity {

    RetrofitInterface RetrofitInterface;

    EditText emailEdit, passwordEdit, nameEdit;
    Button registerbtn;
    TextView login;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        //creating a retrofit instance
        Retrofit retrofitClient = RetrofitClient.getInstance();
        RetrofitInterface = retrofitClient.create(RetrofitInterface.class);


        //getting the views from the layout file
        nameEdit = (EditText)findViewById(R.id.nameEdit);
        emailEdit = (EditText)findViewById(R.id.emailEdit);
        passwordEdit = (EditText)findViewById(R.id.passwordEdit);
        registerbtn = (Button) findViewById(R.id.registerbtn);
        login = (TextView) findViewById(R.id.login);

        nameEdit.setSingleLine(true);
        emailEdit.setSingleLine(true);
        passwordEdit.setSingleLine(true);

        //text to add
        String text = ("Already have an Account? Click here to log in");

        //making the text a spannable string
        SpannableString spannableString = new SpannableString(text);

        //making the spannable string clickable
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                //create a new intent for the main activity
                Intent intent = new Intent(Register.this, MainActivity.class);
                //starting the activity
                startActivity(intent);
            }
        };

        //setting where on the spannable string is clickable
        spannableString.setSpan(clickableSpan, 25, 45, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        //setting the text
        login.setText(spannableString);
        login.setMovementMethod(LinkMovementMethod.getInstance());

        //email pattern
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        //adding a onclick listener to the register button
        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //checking that there is text in the name field
                if(nameEdit.length() == 0){
                    nameEdit.setError("Name must be entered");
                }
                //check that the email entered matches the pattern
                else if (!emailEdit.getText().toString().trim().matches(emailPattern)) {
                    emailEdit.setError("Must provide a valid email");
                }
                //checking that the password is of length 8 or more
                else if(passwordEdit.length() < 8){
                    passwordEdit.setError("Password must be at least 8 characters long");
                }else {

                    //creating a new hashmap
                    HashMap<String, String> map = new HashMap<>();

                    //adding info to the hashmap
                    map.put("name", nameEdit.getText().toString());
                    map.put("email", emailEdit.getText().toString());
                    map.put("password", passwordEdit.getText().toString());

                    //creating a new retrofit call to sign up the user
                    Call<Void> call = RetrofitInterface.executeSignup(map);

                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {

                            //if everything went okay
                            if (response.code() == 200) {
                                //toast message to say they have signed up
                                Toast.makeText(Register.this, "Signed up successful please validate your email", Toast.LENGTH_LONG).show();
                                //create a new intent for the successfulregister activity
                                Intent intent = new Intent(Register.this, SuccessfulRegister.class);
                                //starting the activity
                                startActivity(intent);

                            }
                            //if the email is in use send a toast message
                            else if (response.code() == 404) {
                                emailEdit.setError("Email already in use");
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            //toast message if an error occurs
                            Toast.makeText(Register.this, t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }    }
        });
    }
}
