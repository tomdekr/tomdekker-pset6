package com.example.tom_d.moviebase;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

        final ArrayList<String> listdata = new ArrayList<String>();

        // Instantiate the RequestQueue.
        final RequestQueue queue = Volley.newRequestQueue(this);

        findViewById(R.id.imageView4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.imageView4:
                        finish();
                        startActivity(new Intent(MoviebaseActivity.this, FavoriteActivity.class));
                        break;
                }
            }
        });


        findViewById(R.id.buttonGET).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Thanks to: https://stackoverflow.com/questions/43095334/java-convert-string-to-use-in-url-as-part-of-a-get
                EditText inputText = (EditText)findViewById(R.id.editTextInput);
                final String input = inputText.getText().toString();
                final String url = new String("http://www.omdbapi.com/?apikey=338560c0&s="+input);

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
                            private JSONArray categoriesArray = null;

                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    object = (JSONObject) new JSONObject(response.toString());
                                    categoriesArray = object.getJSONArray("Search");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                ArrayList<String> listdata = new ArrayList<String>();
                                for (int i = 0; i < categoriesArray.length(); i++) {
                                    try {
                                        JSONObject object2 = categoriesArray.getJSONObject(i);
                                        String movieTitle = object2.getString("Title");
                                        listdata.add(movieTitle);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                ArrayAdapter<String> adapter =
                                        new ArrayAdapter<String>(
                                                getApplicationContext(),
                                                R.layout.row_layout,
                                                listdata
                                        );
                                ListView mListView = findViewById(R.id.list);
                                mListView.setAdapter(adapter);

                                // Creates the availability to click on movies and 'add' to favorites
                                try {
                                    mListView.setOnItemClickListener(

                                            new AdapterView.OnItemClickListener() {

                                                SharedPreferences settings = MoviebaseActivity.this.getSharedPreferences("movie list", MODE_PRIVATE);
                                                String item = settings.getString("movie list", "[]");

                                                JSONArray movie_list = new JSONArray(item);


                                                @Override
                                                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                                                    String categoryPicked = "You added " +
                                                            String.valueOf(adapterView.getItemAtPosition(position)) + " to your order";
                                                    Toast.makeText(MoviebaseActivity.this, categoryPicked, Toast.LENGTH_SHORT).show();

                                                    SharedPreferences settings = MoviebaseActivity.this.getSharedPreferences("order", MODE_PRIVATE);
                                                    SharedPreferences.Editor editor = settings.edit();

                                                    movie_list.put(String.valueOf(adapterView.getItemAtPosition(position)));
                                                    editor.putString("movie list", String.valueOf(movie_list));
                                                    editor.commit();

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

        });}


}

