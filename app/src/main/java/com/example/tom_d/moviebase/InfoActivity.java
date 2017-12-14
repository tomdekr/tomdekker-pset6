package com.example.tom_d.moviebase;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class InfoActivity extends AppCompatActivity {

    public ArrayList<String> list = new ArrayList<String>();

    DatabaseReference databaseTitle;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    ListView listViewMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        mAuth = FirebaseAuth.getInstance();

        currentUser = mAuth.getCurrentUser();
        databaseTitle = FirebaseDatabase.getInstance().getReference();

        listViewMovies = findViewById(R.id.listViewMovies2);

        final ListView mListView = (ListView) findViewById(R.id.list);
        SharedPreferences settings = this.getSharedPreferences("movie title", MODE_PRIVATE);
        String item = settings.getString("movie title", "");
        final String input = settings.getString("movie title", "");
        final TextView text = (TextView) findViewById(R.id.text);


        final ArrayList<String> listdata = new ArrayList<String>();
        System.out.println("Check Value: " + input);

        findViewById(R.id.imageView4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFavorite();

//                switch (view.getId()) {
//                    case R.id.imageView4:
//
////                        startActivity(new Intent(InfoActivity.this, FavoriteActivity.class));
//                        break;
//                }
            }
        });


        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = ("http://www.omdbapi.com/?apikey=338560c0&t="+input);
        url = url.replace(" ", "%20");



        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject newObject = (JSONObject) new JSONObject(response);
                            String Title = newObject.getString("Title");
                            String Plot = newObject.getString("Plot");
                            String Genre = newObject.getString("Genre");
                            String Actors = newObject.getString("Actors");
                            String Poster = newObject.getString("Poster");
                            String Released = newObject.getString("Released");
                            String Type = newObject.getString("Type");

                            list.add(Title);
                            list.add("Plot: "+Plot);
                            list.add("Genre: "+Genre);
                            list.add("Actors: "+Actors);
                            list.add("Poster: "+Poster);
                            list.add("Year of release: "+Released);
                            list.add("Type: "+Type);





                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ArrayAdapter<String> adapter =
                                new ArrayAdapter<String>(
                                        getApplicationContext(),
                                        R.layout.row_layout,
                                        list
                                );
                        ListView mListView = findViewById(R.id.list);
                        mListView.setAdapter(adapter);

                        try {
                            mListView.setOnItemClickListener(

                                    new AdapterView.OnItemClickListener() {

                                        SharedPreferences settings = InfoActivity.this.getSharedPreferences("movie list", MODE_PRIVATE);
                                        String item = settings.getString("movie list", "[]");

                                        JSONArray movie_title = new JSONArray(item);


                                        @Override
                                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                                            String moviePicked = "Click on the heart to set the movie to your favorites";
                                            Toast.makeText(InfoActivity.this, moviePicked, Toast.LENGTH_LONG).show();

//                                            saveData();

//                                            addFavorite();



//
//                                            Intent i = new Intent(InfoActivity.this, FavoriteActivity.class);
//                                            i.putExtra("movie", list.get(0));
//
//                                            startActivity(i);
//
//                                            SharedPreferences settings = InfoActivity.this.getSharedPreferences("movie title", MODE_PRIVATE);
//                                            SharedPreferences.Editor editor = settings.edit();
//
//                                            movie_title.put(String.valueOf(adapterView.getItemAtPosition(position)));
//                                            editor.putString("movie title", String.valueOf(list));
//                                            editor.commit();
                                        }

                                    });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }}
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }


    private void addFavorite(){
        String title = list.get(0);


        String id = databaseTitle.push().getKey();
        FavoriteMovie favoritemovie = new FavoriteMovie(title);

        databaseTitle.child("FavoriteMovie").child(currentUser.getDisplayName()).push().setValue(favoritemovie);
           Toast.makeText(this,title, Toast.LENGTH_LONG).show();


    }

//    private void saveData(){
//        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        Gson gson = new Gson();
//        String json = gson.toJson(list);
//        editor.putString("movie list", json);
//        editor.apply();
//        editor.commit();
//    }
//
//    private void loadData() {
//
//        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
//        Gson gson = new Gson();
//        String json = sharedPreferences.getString("movie list", null);
//        Type type = new TypeToken<List>() {}.getType();
//        List infoList = (List) gson.fromJson(json, InfoActivity.class);
//
//        if (infoList == null){
//            infoList = new ArrayList<>();
//        }
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(InfoActivity.this, MoviebaseActivity.class));
    }
}