package com.example.votingapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.votingapp.Retrofit.RetrofitClient;
import com.example.votingapp.Retrofit.RetrofitInterface;
import com.github.mikephil.charting.charts.PieChart;


import org.w3c.dom.Text;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Results extends Activity {

    RetrofitInterface RetrofitInterface;
    TextView winner, option1, option2, option3, option1votes, option2votes, option3votes, candidate, votes, method_used;
    PieChart piechart;

//    String[] options = {
//            "one", "two", "three"
//    };
//
//    String[] vote = {
//            "1", "10", "100"
//    };




//    public class MyCustomAdapter extends ArrayAdapter<String>{
//
//        public MyCustomAdapter(Context context, int textViewResourceId, String[] options, String[] votes){
//            super(context, textViewResourceId, options, votes);
//        }
//
//        public View getView(int position, View convertView, ViewGroup parent){
//            LayoutInflater inflater = getLayoutInflater();
//            View row = inflater.inflate(R.layout.results_row, parent, false);
//
//            TextView label = (TextView)row.findViewById(R.id.op);
//            label.setText(options[position]);
//
//            TextView votes = (TextView)row.findViewById(R.id.votes);
//            votes.setText(vote[position]);
//
//            return row;
//        }
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results);
//        setListAdapter(new MyCustomAdapter(Results.this, R.layout.results_row, options));

        winner = (TextView)findViewById(R.id.winner);
        option1 = (TextView)findViewById(R.id.option1);
        option2 = (TextView)findViewById(R.id.option2);
        option3 = (TextView)findViewById(R.id.option3);
        option1votes = (TextView)findViewById(R.id.option1votes);
        option2votes = (TextView)findViewById(R.id.option2votes);
        option3votes = (TextView)findViewById(R.id.option3votes);
        candidate = (TextView)findViewById(R.id.candidate);
        votes = (TextView)findViewById(R.id.votes);
        method_used = (TextView)findViewById(R.id.method_used);
//        piechart = (PieChart)findViewById(R.id.barChart);

//        winner.setText("i won!!!");
//        option1.setText("hgfvd");
//        option2.setText("hgazdfgh");
//        option3.setText("ggfdsdfgh");
//        option1votes.setText("gfdashjmnhg");
//        option2votes.setText("mkloiytfgb");
//        option3votes.setText("nbvcsaertyui");
        candidate.setText("Candidate");
        votes.setText("Number of Votes");

        String code = getIntent().getStringExtra("code");
//        String code = "623";

        Retrofit retrofitClient = RetrofitClient.getInstance();
        RetrofitInterface = retrofitClient.create(RetrofitInterface.class);

        HashMap<String, String> map = new HashMap<>();
        map.put("code", code);

        Call<Poll> call = RetrofitInterface.getResults(map);

        call.enqueue(new Callback<Poll>() {
            @Override
            public void onResponse(Call<Poll> call, Response<Poll> response) {
                if(response.code() == 200 || response.code() == 201 || response.code() == 202 || response.code() == 203
                        || response.code() == 204 || response.code() == 205 || response.code() == 206 || response.code() == 207
                || response.code() == 208 || response.code() == 209 || response.code() == 210){

                    Poll polls = response.body();

                    winner.setText("The Current winner of the poll is " + polls.getWinner());
                    option1.setText(polls.getOptions().getOption1());
                    option2.setText(polls.getOptions().getOption2());
                    option3.setText(polls.getOptions().getOption3());
                    option1votes.setText(polls.getVotes().getOption1votes());
                    option2votes.setText(polls.getVotes().getOption2votes());
                    option3votes.setText(polls.getVotes().getOption3votes());
                    method_used.setText("The method used for this poll was: " + polls.getVotingmethod());



                }
            }

            @Override
            public void onFailure(Call<Poll> call, Throwable t) {
                Toast.makeText(Results.this, t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("TAG", "onFailure: ", t);
            }
        });


    }

    public void FinishScreen(View v)
    {
        finish();
    }
}
