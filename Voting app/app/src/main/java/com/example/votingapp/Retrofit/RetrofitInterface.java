package com.example.votingapp.Retrofit;

import com.example.votingapp.Poll;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.QueryMap;

public interface RetrofitInterface {

    @POST("/users/Login")
    Call<Void> executeLogin(@Body HashMap<String, String> map);

    @POST("/users/Register")
    Call<Void> executeSignup(@Body HashMap<String, String> map);

    @POST("/poll/createPluralityAndMajorityPoll")
    Call<Void> executenewPoll(@Body HashMap<String, String> map);

    @POST("/poll/createAlternativeVotePoll")
    Call<Void> createAlternativeVotePoll(@Body HashMap<String, String> map);

    @POST("/poll/pollCode")
    Call<Void> executepollCode(@Body HashMap<String, String> map);

//    @GET("/poll/getPoll")
//    Call<List<Poll>> getPoll(@QueryMap HashMap<String, String> map);
    @GET("/poll/getPoll")
    Call<Poll> getPoll(@QueryMap HashMap<String, String> map);

    @POST("/poll/increaseVote")
    Call<Void> increaseVote(@QueryMap HashMap<String, String> map);

    @PUT("/poll/AVInsert")
    Call<Void> increaseAVVote(@QueryMap HashMap<String, String> map);

    @GET("/results/")
    Call<Poll> getResults(@QueryMap HashMap<String, String> map);
}
