package com.example.votingapp;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.votingapp.Retrofit.RetrofitClient;
import com.example.votingapp.Retrofit.RetrofitInterface;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AllPolls extends Activity implements RecyclerAdapter.OnPollListener {

    RetrofitInterface RetrofitInterface;

    RecyclerView recyclerView;
    List<Poll> pollList;
    Button back;
    String email;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_polls);

        Retrofit retrofitClient = RetrofitClient.getInstance();
        RetrofitInterface = retrofitClient.create(RetrofitInterface.class);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);

        email = sharedPreferences.getString("email", "");

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        pollList = new ArrayList<>();
        back = (Button)findViewById(R.id.back);

        Call<List<Poll>> call = RetrofitInterface.getallPolls();

        call.enqueue(new Callback<List<Poll>>() {
            @Override
            public void onResponse(Call<List<Poll>> call, Response<List<Poll>> response) {
                if(response.code() == 200){
                    List<Poll> polls = response.body();

                    for(Poll poll: polls){

                        pollList.add(poll);
                    }

                    PutDataIntoRecyclerView(pollList);
                }
            }

            @Override
            public void onFailure(Call<List<Poll>> call, Throwable t) {
                Toast.makeText(AllPolls.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void PutDataIntoRecyclerView(List<Poll> pollList) {
        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(this, pollList, this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(recyclerAdapter);
    }

    public void switchScreen(View view) {
        finish();
    }

    @Override
    public void onPollClick(int position) {
//        Log.i("TAG", "onPollClick: " + pollList.get(position).getCode());
        String code = String.valueOf(pollList.get(position).getCode());

        HashMap<String, String> map = new HashMap<>();

        map.put("code", code);
        map.put("email", email);

        Call<Void> call = RetrofitInterface.executepollCode(map);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.code() == 200){

                    Call<Poll> call2 = RetrofitInterface.getPoll(map);

                    call2.enqueue(new Callback<Poll>() {
                        @Override
                        public void onResponse(Call<Poll> call, Response<Poll> response) {
                            if(response.code() == 200){
                                Intent intent = new Intent(AllPolls.this, Voting.class);
                                intent.putExtra("code", code);
                                startActivity(intent);

                            }else if(response.code() == 201){
                                Intent intent = new Intent(AllPolls.this, AlternativeVoteVoting.class);
                                intent.putExtra("code", code);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onFailure(Call<Poll> call, Throwable t) {
                            Toast.makeText(AllPolls.this, t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

                }else if(response.code() == 201){
                    Intent intent = new Intent(AllPolls.this, AlternativeVoteResult.class);
                    intent.putExtra("code", code);
                    startActivity(intent);

                }else if(response.code() == 202){
                    Intent intent = new Intent(AllPolls.this, Results.class);
                    intent.putExtra("code", code);
                    startActivity(intent);
                } else if(response.code() == 400){
                    Toast.makeText(AllPolls.this, "Poll does not exist", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(AllPolls.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }
}
