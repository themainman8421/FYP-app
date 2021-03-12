package com.example.votingapp.Retrofit;

import com.example.votingapp.Poll;
import com.example.votingapp.User;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.QueryMap;

public interface RetrofitInterface {

    //allows a user to login
    @POST("/users/Login")
    Call<User> executeLogin(@Body HashMap<String, String> map);

    //allows a user to login
    @POST("/users/Register")
    Call<Void> executeSignup(@Body HashMap<String, String> map);

    //creates a new popular or majority poll
    @POST("/poll/createPluralityAndMajorityPoll")
    Call<Void> executenewPoll(@Body HashMap<String, String> map);

    //creates a new ranked choice poll
    @POST("/poll/createAlternativeVotePoll")
    Call<Void> createAlternativeVotePoll(
            @Body HashMap<String, String> map);

    //checks if the poll vode is in use
    @POST("/poll/pollInUse")
    Call<Void> pollInUse(@Body HashMap<String, String> map);

    //checks if the user has already voted on the poll
    @POST("/poll/pollCode")
    Call<Void> executepollCode(@Body HashMap<String, String> map);

    //get the poll
    @GET("/poll/getPoll")
    Call<Poll> getPoll(@QueryMap HashMap<String, String> map);

    //increases the vote of a popular or majority vote
    @POST("/poll/increaseVote")
    Call<Void> increaseVote(@QueryMap HashMap<String, String> map);

    //inserts users options into ranked choice
    @PUT("/poll/AVInsert")
    Call<Void> increaseAVVote(@QueryMap HashMap<String, String> map);

    //gets the results of a popular or majority vote
    @GET("/results/")
    Call<Poll> getResults(@QueryMap HashMap<String, String> map);

    //getting results for alternative vote
    @GET("/results/avResult")
    Call<Poll> getAVResults(@QueryMap HashMap<String, String> map);

    //adds the poll the user has voted on to their info
    @PUT("/poll/pollsVotedOn")
    Call<Void> addPollVotedOn(@QueryMap HashMap<String, String> map);

    //gets all polls
    @GET("/poll/allPolls")
    Call<List<Poll>> getallPolls();

    //gets all the users polls
    @GET("/poll/userPolls")
    Call<List<Poll>> getallUserPolls(@QueryMap HashMap<String, String> map);

}
