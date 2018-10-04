package com.example.ron.gifapp.vm;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.ron.gifapp.model.GifRepo;
import com.example.ron.gifapp.model.GifResponse;


public class GifViewModel extends ViewModel {
    private MutableLiveData<GifResponse> gifResponseLiveData;
    private GifRepo gifRepo;

    public GifViewModel(){
        this.gifResponseLiveData = new MutableLiveData<>();
        this.gifRepo = new GifRepo();
    }

    public LiveData<GifResponse> getGIfsLiveData() {
        return gifResponseLiveData;
    }

    public void searchGifs(String query){
        gifRepo.loadSearchGifs(gifResponseLiveData,query);
    }

}
