package com.example.ron.gifapp.api;


import com.example.ron.gifapp.model.GifResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("v1/gifs/search")
    Call<GifResponse> getAllGifs(@Query("api_key") String api_key, @Query("q") String query);

}
