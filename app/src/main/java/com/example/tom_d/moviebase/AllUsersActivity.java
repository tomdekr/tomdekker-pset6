package com.example.tom_d.moviebase;

import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.ContactsContract;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AllUsersActivity extends AppCompatActivity {


    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    ArrayAdapter<String> list;
    ArrayList<String> movieList, allTitels, lijst;
    ListView listView;
    FavoriteMovie favoriteMovie;
    FirebaseDatabase mDatabase;
    DatabaseReference myRef;

    List<FavoriteMovie> titleList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        movieList = new ArrayList<>();
        allTitels = new ArrayList<>();
        titleList = new ArrayList<>();
        final String currentUserDisplay = currentUser.getDisplayName();
        mDatabase = FirebaseDatabase.getInstance();
        myRef = mDatabase.getReferenceFromUrl("https://moviebase-38f88.firebaseio.com/FavoriteMovie/");


        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot children : dataSnapshot.getChildren()) {
                    Log.v("Eerste key", "   " + children.toString()); // hele database vanaf favorit
                    String child = children.getKey();
                    if (child != (currentUserDisplay)){
                        allTitels.add(children.getKey());
                        if (child.equals(currentUserDisplay)){
                            allTitels.remove(children.getKey());
                        }
                    }

                    }
                ArrayAdapter<String> adapter =
                        new ArrayAdapter<String>(
                                getApplicationContext(),
                                R.layout.row_layout,
                                allTitels
                        );
                ListView mListView = findViewById(R.id.listViewMovies2);
                mListView.setAdapter(adapter);

                mListView.setOnItemClickListener(

                        new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                                String moviePicked = ("You selected the user");
                                Toast.makeText(AllUsersActivity.this, moviePicked, Toast.LENGTH_LONG).show();

                                Intent i = new Intent(AllUsersActivity.this, UsersFavoriteActivity.class);


                                    i.putExtra("user", allTitels.get(position));

                                startActivity(i);
                            }
            });}


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
        startActivity(new Intent(AllUsersActivity.this, ProfileActivity.class));
    }
}

