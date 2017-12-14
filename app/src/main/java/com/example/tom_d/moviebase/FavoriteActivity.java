package com.example.tom_d.moviebase;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.IDNA;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FavoriteActivity extends AppCompatActivity {

    DatabaseReference databaseTitle;
    ListView listViewMovies;
    List<FavoriteMovie> movieList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        databaseTitle = FirebaseDatabase.getInstance().getReference("title");
        listViewMovies = (ListView) findViewById(R.id.listViewMovies2);

    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseTitle.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                movieList.clear();
                for(DataSnapshot movieSnapshot : dataSnapshot.getChildren()) {
                    FavoriteMovie title = movieSnapshot.getValue(FavoriteMovie.class);
                    movieList = new ArrayList<>();

                    movieList.add(title);

                    FavoriteList adapter = new FavoriteList(FavoriteActivity.this, movieList);
                    listViewMovies.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, InfoActivity.class);
        intent.putExtra("InfoActivity", InfoActivity.class);
        startActivity(new Intent(FavoriteActivity.this, InfoActivity.class));
    }
}
