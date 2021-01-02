package com.example.votingapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.votingapp.Retrofit.RetrofitClient;
import com.example.votingapp.Retrofit.RetrofitInterface;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.QueryMap;

public class Voting extends Activity {

    RetrofitInterface RetrofitInterface;
    RadioButton option1, option2, option3;
    TextView titleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.voting);

        Retrofit retrofitClient = RetrofitClient.getInstance();
        RetrofitInterface = retrofitClient.create(RetrofitInterface.class);

        String code = getIntent().getStringExtra("code");

        titleTextView = findViewById(R.id.titleTextView);
        option1 = findViewById(R.id.radioButton);
        option2 = findViewById(R.id.radioButton2);
        option3 = findViewById(R.id.radioButton3);

//        option1.setText(code);



        HashMap<String, String> map = new HashMap<>();
        map.put("code", code);

        Call<List<Poll>> call = RetrofitInterface.getPoll(map);
        call.enqueue(new Callback<List<Poll>>() {
            @Override
            public void onResponse(Call<List<Poll>> call, Response<List<Poll>> response) {
                if (response.code() == 200){
                    List<Poll> polls = response.body();

                    for(Poll poll : polls){
                        titleTextView.setText(poll.getTitle());
                    }



                }
            }

            @Override
            public void onFailure(Call<List<Poll>> call, Throwable t) {
                Toast.makeText(Voting.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    public void switchScreen(View v)
    {
        finish();
    }
}
