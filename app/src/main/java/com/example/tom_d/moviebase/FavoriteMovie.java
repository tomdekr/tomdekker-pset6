package com.example.tom_d.moviebase;

import com.google.firebase.database.IgnoreExtraProperties;

import static android.os.Build.ID;

@IgnoreExtraProperties
public class FavoriteMovie {
    public String id;
    public String title;

    public FavoriteMovie(){

    }


    public FavoriteMovie(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
    public String getId() {
        return id;
    }

}
