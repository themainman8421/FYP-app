package com.example.votingapp.Retrofit;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitInterface {

    @POST("/users/Login")
    Call<Void> executeLogin(@Body HashMap<String, String> map);

    @POST("/users/Register")
    Call<Void> executeSignup(@Body HashMap<String, String> map);

    @POST("/poll/newPoll")
    Call<Void> executenewPoll(@Body HashMap<String, String> map);
}
