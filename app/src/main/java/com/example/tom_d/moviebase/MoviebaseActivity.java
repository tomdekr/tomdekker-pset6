package com.example.tom_d.moviebase;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MoviebaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moviebase);

        // Instantiate the RequestQueue.
        final RequestQueue queue = Volley.newRequestQueue(this);

        // Does the 'Search' function on api when clicked on this button
        findViewById(R.id.buttonGET).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Thanks to: https://stackoverflow.com/questions/43095334/java-convert-string-to-use-in-url-as-part-of-a-get
                final EditText inputText = (EditText)findViewById(R.id.editTextInput);
                final String input = inputText.getText().toString();

                // Makes it able to search the url by the input by user
                String url = new String("http://www.omdbapi.com/?apikey=338560c0&s="+input);

                // Replaces any space in url (input) for underscore to prevent error
                url = url.replace(" ", "_");

                // Checks if there is a input by user
                if (input.isEmpty()){
                    inputText.setError("Input is required!");
                    inputText.requestFocus();
                    return;
                }

                // Request a string response from the provided URL.
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                        Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {

                            private JSONObject object = null;
                            private JSONArray array = null;

                            @Override
                            public void onResponse(JSONObject response) {
                                // Searches the api on array called 'Search'
                                try {
                                    object = (JSONObject) new JSONObject(response.toString());
                                    array = object.getJSONArray("Search");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                ArrayList<String> listdata = new ArrayList<String>();
                                try {
                                    // Reads the input by user if its not null
                                    if (array == null){
                                        inputText.setError("Valid input required");
                                        inputText.requestFocus();
                                        return;
                                    }
                                    // Checks if the array is not null
                                    if (array != null){
                                        // For every i in the array get the movie Title
                                    for (int i = 0; i < array.length(); i++) {

                                    JSONObject object2 = array.getJSONObject(i);
                                    String movieTitle = object2.getString("Title");
                                    listdata.add(movieTitle);
                                }
                                }}
                                catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                // Makes the arraylist from api visible in a row_layout
                                ArrayAdapter<String> adapter =
                                        new ArrayAdapter<String>(
                                                getApplicationContext(),
                                                R.layout.row_layout,
                                                listdata
                                        );
                                ListView mListView = findViewById(R.id.list);

                                //Sets the adapter to make the final visualisation for the listview
                                mListView.setAdapter(adapter);

                                // Creates the availability to click on movies and explains action.
                                try {
                                    mListView.setOnItemClickListener(

                                            new AdapterView.OnItemClickListener() {

                                                // Saves the movie title if onItemClick is used
                                                SharedPreferences settings = MoviebaseActivity.this.getSharedPreferences("movie list", MODE_PRIVATE);
                                                String item = settings.getString("movie list", "[]");

                                                JSONArray movie_title = new JSONArray(item);


                                                @Override
                                                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                                                    // Shows which item you clicked with toast
                                                    String moviePicked = "You selected " +
                                                            String.valueOf(adapterView.getItemAtPosition(position));
                                                    Toast.makeText(MoviebaseActivity.this, moviePicked, Toast.LENGTH_SHORT).show();

                                                    // Saves the clicked item into 'movie title'
                                                    SharedPreferences settings = MoviebaseActivity.this.getSharedPreferences("movie title", MODE_PRIVATE);
                                                    SharedPreferences.Editor editor = settings.edit();
                                                    movie_title.put(String.valueOf(adapterView.getItemAtPosition(position)));
                                                    editor.putString("movie title", String.valueOf(movie_title));
                                                    editor.commit();

                                                    // If item clicked, go to the item's info page
                                                    Intent intent = new Intent(MoviebaseActivity.this, InfoActivity.class);
                                                    intent.putExtra("InfoActivity",InfoActivity.class);
                                                    startActivity(intent);
                                                }
                                            });

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        // Add the request to the RequestQueue.
                queue.add(jsonObjectRequest);
                }
            }
        );
    }

    // Make sure that when back button is pressed the right activity is displayed
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(MoviebaseActivity.this, ProfileActivity.class));
    }
}




