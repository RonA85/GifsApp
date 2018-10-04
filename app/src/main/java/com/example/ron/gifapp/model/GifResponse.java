package com.example.ron.gifapp.model;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class GifResponse implements Serializable{

    @SerializedName("data")
    private List<GIF> gifs;

    public List<GIF> getGifs() {
        return gifs;
    }

    public void setGifs(List<GIF> gifs) {
        this.gifs = gifs;
    }
}
