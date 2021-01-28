package com.example.votingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.votingapp.Retrofit.RetrofitClient;
import com.example.votingapp.Retrofit.RetrofitInterface;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends Activity {

    RetrofitInterface RetrofitInterface;

    EditText emailEdit, passwordEdit;
    Button loginbtn;
    ImageView voteImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Retrofit retrofitClient = RetrofitClient.getInstance();
        RetrofitInterface = retrofitClient.create(RetrofitInterface.class);

        findViewById(R.id.Register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Register.class);
                startActivity(intent);
            }
        });

        emailEdit = (EditText)findViewById(R.id.emailEdit);
        passwordEdit = (EditText)findViewById(R.id.passwordEdit);
        loginbtn = (Button) findViewById(R.id.loginbtn);
        voteImage = (ImageView)findViewById(R.id.voteImage);

        voteImage.setImageResource(R.drawable.mobile_vote_image);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap<String, String> map = new HashMap<>();

                map.put("email", emailEdit.getText().toString());
                map.put("password", passwordEdit.getText().toString());

                Intent intent = new Intent(MainActivity.this, Home.class);
                startActivity(intent);

//                Call<Void> call = RetrofitInterface.executeLogin(map);
//
//                call.enqueue(new Callback<Void>() {
//                    @Override
//                    public void onResponse(Call<Void> call, Response<Void> response) {
//
//                        if (response.code() == 200){
//                            Toast.makeText(MainActivity.this, "You have successfully logged in", Toast.LENGTH_LONG).show();
//
//                            Intent intent = new Intent(MainActivity.this, Home.class);
//                            startActivity(intent);
//
//                        } else if (response.code() == 404){
//                            emailEdit.setError("Email is not registered");
//
//                        } else if (response.code() == 401){
//                            passwordEdit.setError("Password is incorrect");
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<Void> call, Throwable t) {
//                        Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
//                    }
//                });
            }
        });
    }






}