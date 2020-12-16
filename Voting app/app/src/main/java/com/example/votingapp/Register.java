package com.example.votingapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.votingapp.Retrofit.RetrofitClient;
import com.example.votingapp.Retrofit.RetrofitInterface;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Register extends Activity {

    RetrofitInterface RetrofitInterface;

    EditText emailEdit, passwordEdit, nameEdit;
    Button registerbtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        Retrofit retrofitClient = RetrofitClient.getInstance();
        RetrofitInterface = retrofitClient.create(RetrofitInterface.class);

        findViewById(R.id.Login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, MainActivity.class);
                startActivity(intent);
            }
        });

        nameEdit = (EditText)findViewById(R.id.nameEdit);
        emailEdit = (EditText)findViewById(R.id.emailEdit);
        passwordEdit = (EditText)findViewById(R.id.passwordEdit);
        registerbtn = (Button) findViewById(R.id.registerbtn);

        String email = emailEdit.getText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(nameEdit.length() == 0){
                    nameEdit.setError("Name must be entered");
                }

                if (emailEdit.getText().toString().trim().matches(emailPattern)) {
                    //Toast.makeText(getApplicationContext(),"valid email address",Toast.LENGTH_SHORT).show();
                } else {
                    emailEdit.setError("Must provide a valid email");
                }

                if(passwordEdit.length() < 8){
                    passwordEdit.setError("Password must be at least 8 characters long");
                }else {

                    HashMap<String, String> map = new HashMap<>();

                    map.put("name", nameEdit.getText().toString());
                    map.put("email", emailEdit.getText().toString());
                    map.put("password", passwordEdit.getText().toString());

                    Call<Void> call = RetrofitInterface.executeSignup(map);

                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {

                            if (response.code() == 200) {
                                Toast.makeText(Register.this, "Signed up succesfully", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(Register.this, MainActivity.class);
                                startActivity(intent);

                            } else if (response.code() == 404) {
                                emailEdit.setError("Email already in use");
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(Register.this, t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }    }
        });
    }
}
