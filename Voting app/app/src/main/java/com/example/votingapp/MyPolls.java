package com.example.votingapp;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

public class MyPolls extends AppCompatActivity implements RecyclerAdapter.OnPollListener {

    RetrofitInterface RetrofitInterface;
    SharedPreferences sharedPreferences;
    RecyclerView recyclerView;
    ArrayList<Poll> pollList;
    Button back;
    String email;
    EditText search;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_polls);

        Retrofit retrofitClient = RetrofitClient.getInstance();
        RetrofitInterface = retrofitClient.create(RetrofitInterface.class);

        sharedPreferences = getApplicationContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);

        email = sharedPreferences.getString("email", "");

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        pollList = new ArrayList<>();
        back = (Button)findViewById(R.id.back);
        search = (EditText)findViewById(R.id.search);

        search.setSingleLine(true);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        HashMap<String, String> map = new HashMap<>();
        map.put("email", email);

        Call<List<Poll>> call = RetrofitInterface.getallUserPolls(map);

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
                Toast.makeText(MyPolls.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void filter(String toString) {
        try{
            ArrayList<Poll> pollFilteredList = new ArrayList<Poll>();
            for (Poll poll : pollList){
                if(poll.getTitle().toLowerCase().contains(toString.toLowerCase())){
                    pollFilteredList.add(poll);
                }
            }
            PutDataIntoRecyclerView(pollFilteredList);
        }catch (Exception e){
            Toast.makeText(MyPolls.this, e.getMessage(), Toast.LENGTH_LONG).show();
            Log.d("TAG", "error: " + e);
        }
    }

    private void PutDataIntoRecyclerView(ArrayList<Poll> pollList) {
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
                if(response.code() == 201){
                    Intent intent = new Intent(MyPolls.this, AlternativeVoteResult.class);
                    intent.putExtra("code", code);
                    startActivity(intent);

                }else if(response.code() == 202){
                    Intent intent = new Intent(MyPolls.this, Results.class);
                    intent.putExtra("code", code);
                    startActivity(intent);
                } else if(response.code() == 400){
                    Toast.makeText(MyPolls.this, "Poll does not exist", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MyPolls.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.topbar_menu, menu);
        this.setTitle("All polls I have voted on");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.LogOut:
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("email", "");
                editor.putBoolean("Logged in", false);
                editor.apply();
                Intent intent = new Intent(MyPolls.this, MainActivity.class);
                startActivity(intent);
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
