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
    String id;
    EditText search;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_polls);

        //creating a retrofit instance
        Retrofit retrofitClient = RetrofitClient.getInstance();
        RetrofitInterface = retrofitClient.create(RetrofitInterface.class);

        //getting whats stored in the shared preference
        sharedPreferences = getApplicationContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        id = sharedPreferences.getString("_id", "");

        //getting the views within the layout file and setting a new array list
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        pollList = new ArrayList<>();
        search = (EditText)findViewById(R.id.search);

        //setting the search bar edit text to single line
        search.setSingleLine(true);

        //adding a text changed listener to the search bar
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //use the string entered to filter the recyclerView
                filter(s.toString());
            }
        });

        //creating a new hashmap and inserting into it
        HashMap<String, String> map = new HashMap<>();
        map.put("id", id);

        //creating a new retrofit call to get all the polls the user has entered in
        Call<List<Poll>> call = RetrofitInterface.getallUserPolls(map);

        //queueing the call
        call.enqueue(new Callback<List<Poll>>() {
            @Override
            public void onResponse(Call<List<Poll>> call, Response<List<Poll>> response) {
                //if everything went okay
                if(response.code() == 200){
                    //get a list of the responses body
                    List<Poll> polls = response.body();

                    for(Poll poll: polls){

                        //add the poll info to the list
                        pollList.add(poll);
                    }

                    //put the list into the recycler view
                    PutDataIntoRecyclerView(pollList);
                }
            }

            @Override
            public void onFailure(Call<List<Poll>> call, Throwable t) {
                //create a toast message if an error occurs
                Toast.makeText(MyPolls.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }
    //filtering the recyclerView with the string entered into the search bar
    private void filter(String toString) {
        try{
            //creating a new filtered array list
            ArrayList<Poll> pollFilteredList = new ArrayList<Poll>();
            for (Poll poll : pollList){
                //checking if the string entered into the search bar is in one of the titles within the recyclerView
                if(poll.getTitle().toLowerCase().contains(toString.toLowerCase())){
                    //adding the filtered poll to the list
                    pollFilteredList.add(poll);
                }
            }
            //populating the RecyclerView with the filtered list
            PutDataIntoRecyclerView(pollFilteredList);
        }catch (Exception e){
            Log.d("TAG", "error: " + e);
        }
    }

    //Method that puts info into the recyclerView
    private void PutDataIntoRecyclerView(ArrayList<Poll> pollList) {
        //creating a new recycler adapter
        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(this, pollList, this);

        //setting the layout for the recyler view
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //adding the adapter to the recycler view
        recyclerView.setAdapter(recyclerAdapter);
    }

    //Method that checks which row in the list has been clicked
    @Override
    public void onPollClick(int position) {
        //getting the poll code for the clicked row
        String code = String.valueOf(pollList.get(position).getCode());

        //creating a new hashmap
        HashMap<String, String> map = new HashMap<>();

        //inserting into the hashmap
        map.put("code", code);
        map.put("id", id);

        //creating a new retrofit call to check if the user has voted on the poll or not
        Call<Void> call = RetrofitInterface.executepollCode(map);

        //queuing the call
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                //if the user has voted on the poll and its for Ranked choice open this activity
                if(response.code() == 201){
                    //create a new intent for the alternative vote results activity
                    Intent intent = new Intent(MyPolls.this, AlternativeVoteResult.class);
                    //put the poll code into the intent
                    intent.putExtra("code", code);
                    //start the activity
                    startActivity(intent);

                    //if the user has voted on the poll and its for Popular or Majority vote open this activity
                }else if(response.code() == 202){
                    //creating a new intent for the results activity
                    Intent intent = new Intent(MyPolls.this, Results.class);
                    //put the poll code into the intent
                    intent.putExtra("code", code);
                    //start the activity
                    startActivity(intent);
                } else if(response.code() == 400){
                    //toast message if poll doesnt exist
                    Toast.makeText(MyPolls.this, "Poll does not exist", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                //toast message if theres an error
                Toast.makeText(MyPolls.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }

    //creating the top menu bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //creating a new menu inflater
        MenuInflater inflater = getMenuInflater();
        //setting which menu to inflate it with
        inflater.inflate(R.menu.topbar_menu, menu);
        return true;
    }

    //method for when an item is selected from the menu bar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            //if the id selected is for the log out button
            case R.id.LogOut:
                //get a shared preference editor
                SharedPreferences.Editor editor = sharedPreferences.edit();
                //update the values withing the preferences
                editor.putString("_id", "");
                editor.putString("email", "");
                editor.putBoolean("Logged in", false);
                //apply the updates
                editor.apply();
                //create a new intent for the main activity(Log in page)
                Intent intent = new Intent(MyPolls.this, MainActivity.class);
                //start the activity
                startActivity(intent);
                //finish this activity
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
