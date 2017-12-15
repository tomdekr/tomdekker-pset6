package com.example.tom_d.moviebase;

import android.content.Intent;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;


public class ProfileActivity extends AppCompatActivity {

    EditText editText;
    ProgressBar progressBar;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Necessary stuff for funcionality
        editText = findViewById(R.id.editTextUsername);
        progressBar = findViewById(R.id.progressbar);
        mAuth = FirebaseAuth.getInstance();

        //This wil load all filled in (working atm) information
        loadUserInformation();

        findViewById(R.id.buttonSaveUsername).setOnClickListener(new View.OnClickListener() {
            // Executes the profile update method called saveUserInformation
            @Override
            public void onClick(View view) {
                saveUserInformation();

            }
        });

        findViewById(R.id.buttonLogout).setOnClickListener(new View.OnClickListener() {
            // Executes the Logout method
            @Override
            public void onClick(View view) {
                Logout();

            }
        });

        findViewById(R.id.buttonContinue).setOnClickListener(new View.OnClickListener() {
            // Executes new activity to MoviebaseActivity
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.buttonContinue:
                        finish();
                        startActivity(new Intent(ProfileActivity.this, MoviebaseActivity.class));
                        break;
                }
            }
        });
        findViewById(R.id.buttonFavorites).setOnClickListener(new View.OnClickListener() {
            // Executes new activity to FavoriteActivity
            @Override
            public void onClick(View view) {

                switch (view.getId()) {
                    case R.id.buttonFavorites:
                        finish();
                        startActivity(new Intent(ProfileActivity.this, FavoriteActivity.class));
                        break;
                }
            }
        });

        findViewById(R.id.buttonUsers).setOnClickListener(new View.OnClickListener() {
              // Executes new activity to AllUsersActivity
              @Override
              public void onClick(View view) {

                  switch (view.getId()) {
                      case R.id.buttonUsers:
                          finish();
                          startActivity(new Intent(ProfileActivity.this, AllUsersActivity.class));
                          break;
                  }
              }
          }

        );}




    // The method that makes it able to log out as an user
    private void Logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


    @Override
    // This makes it able to check if the user is already logged in when opening app after killing it
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();

        // If the user is not logged in, go to login page
        if (user == null){
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    // This is the method to load the updated user information from Firebase
    private void loadUserInformation() {
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            if (user.getDisplayName() != null) {
                editText.setText(user.getDisplayName());
            }

        }
    }

    // This method makes it able to save the filled in username to the Firebase database
    private void saveUserInformation() {
        String displayName = editText.getText().toString();

        // If input is empty; make noticeable
        if(displayName.isEmpty()){
            editText.setError("Name required");
            editText.requestFocus();
            return;
        }

        // Check for currentuser authentication
        FirebaseUser user = mAuth.getCurrentUser();

        // Creates a request to change the user profile if input for username is not null
        if(user !=null){
            progressBar.setVisibility(View.VISIBLE);
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName)
                    .build();
            // Makes the actual update of the username happen
            user.updateProfile(profile)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressBar.setVisibility(View.GONE);
                            if(task.isSuccessful()){
                                Toast.makeText(ProfileActivity.this,"Profile is updated!",Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
        }
    }
}
