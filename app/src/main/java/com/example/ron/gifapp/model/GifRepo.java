package com.example.ron.gifapp.model;

import android.arch.lifecycle.MutableLiveData;
import android.util.Log;


import com.example.ron.gifapp.Cons;
import com.example.ron.gifapp.api.ApiService;
import com.example.ron.gifapp.api.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GifRepo {

    private final String TAG = getClass().getSimpleName();
    private ApiService apiService;

    public GifRepo() {
        this.apiService = RetrofitClient.getInstance().create(ApiService.class);
    }

    public void loadSearchGifs(final MutableLiveData<GifResponse> liveData, String query){
        Log.d(TAG,"loadSearchGifs");
        Call<GifResponse> call = apiService.getAllGifs(Cons.API_KEY,query);
        call.enqueue(new Callback<GifResponse>() {
            @Override
            public void onResponse(Call<GifResponse> call, Response<GifResponse> response) {
                Log.d(TAG,"" + response.code()+":"+response.body().getGifs().get(0).getId());
                liveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<GifResponse> call, Throwable t) {
                Log.d(TAG,t.getMessage());
            }
        });
    }


}
