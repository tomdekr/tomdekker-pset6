package com.example.tom_d.moviebase;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class FavoriteActivity extends AppCompatActivity {


//    Not working yet TODO: Make the added movies appear in listview from this activity..

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        final ListView mListView = (ListView) findViewById(R.id.list);
        SharedPreferences settings = this.getSharedPreferences("movie list", MODE_PRIVATE);
        String item = settings.getString("movie list", "");

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(FavoriteActivity.this, MoviebaseActivity.class));
    }
}
