package com.example.votingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

//adapter class for the recycler view
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<Poll> pollList;
    private OnPollListener onPollListener;

    //constructor
    public RecyclerAdapter(Context mContext, ArrayList<Poll> pollList, OnPollListener onPollListener) {
        this.mContext = mContext;
        this.pollList = pollList;
        this.onPollListener = onPollListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //inflating the recycler view with the layout created
        View v;
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        v = layoutInflater.inflate(R.layout.adapter_list_layout, parent, false);
        return  new MyViewHolder(v, onPollListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        //adding text to the layout and getting the info from the poll data received
        String title = "<strong>Title: </strong> " + pollList.get(position).getTitle();
        String winner = "<strong>Winner: </strong> " + pollList.get(position).getWinner();
        String votingMethod = "<strong>Voting method used: </strong> " + pollList.get(position).getVotingmethod();
        String code = "<strong>Poll code: </strong> " + pollList.get(position).getCode();


        holder.title.setText(HtmlCompat.fromHtml(title, HtmlCompat.FROM_HTML_MODE_LEGACY));
        holder.winner.setText(HtmlCompat.fromHtml(winner, HtmlCompat.FROM_HTML_MODE_LEGACY));
        holder.votingmethod.setText(HtmlCompat.fromHtml(votingMethod, HtmlCompat.FROM_HTML_MODE_LEGACY));
        holder.code.setText(HtmlCompat.fromHtml(code, HtmlCompat.FROM_HTML_MODE_LEGACY));



    }

    //counting the size of the poll list
    @Override
    public int getItemCount() {
        return pollList.size();
    }

    //finding the views in the layout adapted to the recycler view and adding a onclick listener
    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView title, votingmethod, code, winner;
        OnPollListener onPollListener;

        public MyViewHolder(@NonNull View itemView, OnPollListener onPollListener) {
            super(itemView);
            
                    
            title = itemView.findViewById(R.id.title);
            votingmethod = itemView.findViewById(R.id.votingmethod);
            winner = itemView.findViewById(R.id.winner);
            code = itemView.findViewById(R.id.code);
            this.onPollListener = onPollListener;

            itemView.setOnClickListener(this);

        }

        //getting the position of the row clicked
        @Override
        public void onClick(View v) {
            onPollListener.onPollClick(getAdapterPosition());
        }
    }

    //interface listening to get the position clicked
    public interface OnPollListener{
        void onPollClick(int position);
    }




}
