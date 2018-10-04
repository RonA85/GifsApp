package com.example.ron.gifapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GIF implements Serializable {

    @SerializedName("id")
    private String id;

    @SerializedName("slug")
    private String slug;

    @SerializedName("username")
    private String username;

    @SerializedName("source")
    private String source;

    @SerializedName("rating")
    private String rating;

    @SerializedName("images")
    private Images images;

    public GIF() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public Images getImages() {
        return images;
    }

    public void setImages(Images images) {
        this.images = images;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }
}
