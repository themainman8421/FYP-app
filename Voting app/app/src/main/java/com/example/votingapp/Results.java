package com.example.votingapp;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.votingapp.Retrofit.RetrofitClient;
import com.example.votingapp.Retrofit.RetrofitInterface;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Results extends ListActivity {

    RetrofitInterface RetrofitInterface;

    String[] options = {
            "one", "two", "three"
    };

    String[] vote = {
            "1", "10", "100"
    };


    public class MyCustomAdapter extends ArrayAdapter<String>{

        public MyCustomAdapter(Context context, int textViewResourceId, String[] objects){
            super(context, textViewResourceId, objects);
        }

        public View getView(int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = getLayoutInflater();
            View row = inflater.inflate(R.layout.results_row, parent, false);

            TextView label = (TextView)row.findViewById(R.id.option);
            label.setText(options[position]);

            TextView votes = (TextView)row.findViewById(R.id.votes);
            votes.setText(vote[position]);

            return row;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results);
        setListAdapter(new MyCustomAdapter(
                Results.this,
                R.layout.results, options
        ));

//        String code = getIntent().getStringExtra("code");
        String code = "623";

        Retrofit retrofitClient = RetrofitClient.getInstance();
        RetrofitInterface = retrofitClient.create(RetrofitInterface.class);

        HashMap<String, String> map = new HashMap<>();
        map.put("code", code);

        Call<List<Poll>> call = RetrofitInterface.getResults(map);

        call.enqueue(new Callback<List<Poll>>() {
            @Override
            public void onResponse(Call<List<Poll>> call, Response<List<Poll>> response) {
                if(response.code() == 200){

//                    List<Poll> polls = response.body();
//
//                    for (Poll poll : polls){
//                        TextView label = (TextView)row.findViewById(R.id.option);
//                        label.setText(options[position]);
//
//                        TextView votes = (TextView)row.findViewById(R.id.votes);
//                        votes.setText(vote[position]);
//                    }
                }
            }

            @Override
            public void onFailure(Call<List<Poll>> call, Throwable t) {

            }
        });


    }
}
