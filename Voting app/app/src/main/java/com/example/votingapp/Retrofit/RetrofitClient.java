package com.example.votingapp.Retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//this class allows for the creation of a retrofit client
public class RetrofitClient {
    private static Retrofit instance;
    private static String BASE_URL =  "http://10.0.2.2:3000";
//    private static String BASE_URL =  "http://46.101.60.72:3000";

    //setting the url
    public static String getBASE_URL() {
        return BASE_URL;
    }

    public void setBASE_URL(String BASE_URL) {
        this.BASE_URL = BASE_URL;
    }

    //getting a retrofit instance
    public static Retrofit getInstance(){
        if(instance == null){
            instance = new Retrofit.Builder()
                    .baseUrl(getBASE_URL())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return instance;
    }
}
