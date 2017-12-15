package com.example.tom_d.moviebase;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


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

        // Necessary stuff for functionality
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        databaseTitle = FirebaseDatabase.getInstance().getReference("FavoriteMovie");
        listViewMovies = findViewById(R.id.listViewMovies2);

        // Retrieves the saved movie title from 'movie title' from MoviebaseActivity
        final ListView mListView = (ListView) findViewById(R.id.list);
        SharedPreferences settings = this.getSharedPreferences("movie title", MODE_PRIVATE);
        String item = settings.getString("movie title", "");
        final String input = settings.getString("movie title", "");
        final TextView text = (TextView) findViewById(R.id.text);

        findViewById(R.id.imageView4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Adds the movie to favorites if image 'heart' is clicked
                addFavorite();
            }
        });


        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = ("http://www.omdbapi.com/?apikey=338560c0&t="+input);

        // Replaces any space in url (input) for underscore to prevent error
        url = url.replace(" ", "_");



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
                            String ID = newObject.getString("imdbID");


                            list.add(Title);
                            list.add("Plot: "+Plot);
                            list.add("Genre: "+Genre);
                            list.add("Actors: "+Actors);
                            list.add("Poster: "+Poster);
                            list.add("Year of release: "+Released);
                            list.add("Type: "+Type);
                            list.add("imdbID: "+ID);






                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // Makes the arraylist from api visible in a row_layout
                        ArrayAdapter<String> adapter =
                                new ArrayAdapter<String>(
                                        getApplicationContext(),
                                        R.layout.row_layout,
                                        list
                                );
                        ListView mListView = findViewById(R.id.list);
                        // Sets the adapter to make the final visualisation for the listview
                        mListView.setAdapter(adapter);

                        // Makes the listview item's clickable
                        try {
                            mListView.setOnItemClickListener(
                                    new AdapterView.OnItemClickListener() {
                                        @Override
                                        // Shows how to add the movie to favorites
                                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                                            String moviePicked = "Click on the heart to set the movie to your favorites";
                                            Toast.makeText(InfoActivity.this, moviePicked, Toast.LENGTH_LONG).show();
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

    // Method that is attached to the 'heart' button to add the movie from InfoActivity to favorites
    private void addFavorite(){
        // Gets the title from the list
        String title = list.get(0);

        // Creates a unique key for a movie title
        String id = databaseTitle.push().getKey();

        // Saves the title with username and unique key to Firebase Database
        databaseTitle.child(currentUser.getDisplayName()).child(id).setValue(title);

        // Shows toast
        Toast.makeText(this,"Favorite added", Toast.LENGTH_LONG).show();
    }

    // Make sure that when back button is pressed the right activity is displayed
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(InfoActivity.this, MoviebaseActivity.class));
    }
}