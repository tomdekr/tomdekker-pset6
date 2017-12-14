package com.example.tom_d.moviebase;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class FavoriteMovie {
    public String title;

    public FavoriteMovie(){

    }

    public FavoriteMovie(String title) {
        this.title = title;

    }

    public String getTitle() {
        return title;
    }
}
