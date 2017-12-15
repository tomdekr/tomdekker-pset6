package com.example.tom_d.moviebase;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;


public class AllUsersActivity extends AppCompatActivity {


    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    ArrayList<String>  allTitels;
    FirebaseDatabase mDatabase;
    DatabaseReference myRef;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);

        // Necessary stuff for functionality
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        allTitels = new ArrayList<>();
        final String currentUserDisplay = currentUser.getDisplayName();
        mDatabase = FirebaseDatabase.getInstance();
        myRef = mDatabase.getReferenceFromUrl("https://moviebase-38f88.firebaseio.com/FavoriteMovie/");


        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            // Listens if there is made a change to the 'favorites' of the current user
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot children : dataSnapshot.getChildren()) {
                    Log.v("Eerste key", "   " + children.toString()); // // Log to check spot in branch

                    String child = children.getKey();
                    // if statement to add all users not equal to the current user to the arraylist
                    if (child != (currentUserDisplay)){
                        allTitels.add(children.getKey());
//                      // checking if statement, if the current users username is still in the arraylist, if so ; remove it
                        if (child.equals(currentUserDisplay)){
                            allTitels.remove(children.getKey());
                            }
                        }
                    }
                // Makes the arraylist from api visible in a row_layout
                ArrayAdapter<String> adapter =
                        new ArrayAdapter<String>(
                                getApplicationContext(),
                                R.layout.row_layout,
                                allTitels
                        );
                ListView mListView = findViewById(R.id.listViewMovies2);

                //Sets the adapter to make the final visualisation for the listview
                mListView.setAdapter(adapter);

                mListView.setOnItemClickListener(
                        new AdapterView.OnItemClickListener() {
                            @Override
                            // Listens if an username is clicked, if so give that username to the next activity with an intent
                            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                                String moviePicked = ("You selected the user");
                                Toast.makeText(AllUsersActivity.this, moviePicked, Toast.LENGTH_LONG).show();

                                Intent i = new Intent(AllUsersActivity.this, UsersFavoriteActivity.class);

                                // Give the clicked username value to the intent
                                i.putExtra("user", allTitels.get(position));

                                startActivity(i);
                            }
            });}


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // Make sure that when back button is pressed the right activity is displayed
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, InfoActivity.class);
        intent.putExtra("InfoActivity", InfoActivity.class);
        startActivity(new Intent(AllUsersActivity.this, ProfileActivity.class));
    }
}

