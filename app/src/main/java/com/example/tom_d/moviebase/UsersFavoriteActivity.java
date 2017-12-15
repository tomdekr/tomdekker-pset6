package com.example.tom_d.moviebase;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UsersFavoriteActivity extends AppCompatActivity {


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
        setContentView(R.layout.activity_users_favorite);

        Intent intent = getIntent();
        String currentUserAfterSelection = intent.getStringExtra("user");


        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        movieList = new ArrayList<>();
        allTitels = new ArrayList<>();
        titleList = new ArrayList<>();
        final String currentUserDisplay = currentUser.getDisplayName();
        mDatabase = FirebaseDatabase.getInstance();
        myRef = mDatabase.getReferenceFromUrl("https://moviebase-38f88.firebaseio.com/FavoriteMovie/"+currentUserAfterSelection);



        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot movieSnapshot :  dataSnapshot.getChildren()){
//                    for (DataSnapshot titleSnapshot : movieSnapshot.getChildren()){
//                        FavoriteMovie movie = titleSnapshot.getValue(FavoriteMovie.class);
//                        titleList.add(movie);
                for (DataSnapshot children : dataSnapshot.getChildren()) {
                    Log.v("Eerste key", "   " + dataSnapshot.toString()); // hele database vanaf favorit
                    String values = null;



                    values = children.getValue().toString();

                    Log.v("Tweede key", "   " + children.toString()); // database vanaf tomdekr  // ?? waarom 3x ??
                    Log.v("Derde key", "   " + values); // database vanaf tomdekr  // ?? waarom 3x ??

//                        movieList.add(children.toString());  // werkt DEELS!



                    String[] lijst = values.split(",");
                    Log.v("Vierde key", "   " + Arrays.toString(lijst)); // database vanaf tomdekr  // ?? waarom 3x ??

                    for (int i = 0 ; i < lijst.length; i++ ){

                        String[] lijst2 = values.split("=");
                        allTitels.add(lijst2[i]);



                    }
                    Log.v("keyResult","   " + allTitels);
                    ArrayAdapter<String> adapter =
                            new ArrayAdapter<String>(
                                    getApplicationContext(),
                                    R.layout.row_layout,
                                    allTitels
                            );
                    ListView mListView = findViewById(R.id.listViewMovies2);
                    mListView.setAdapter(adapter);

//                    mListView.setAdapter(adapter);
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
        startActivity(new Intent(UsersFavoriteActivity.this, AllUsersActivity.class));
    }
}






