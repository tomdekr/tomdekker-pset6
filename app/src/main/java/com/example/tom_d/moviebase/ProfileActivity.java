package com.example.tom_d.moviebase;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class ProfileActivity extends AppCompatActivity {

    private static final int CHOOSE_IMAGE = 1 ;
    ImageView imageView;
    EditText editText;
    Button item;


    Uri uriProfileImage;
    ProgressBar progressBar;

    String profileImageURL;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        editText = findViewById(R.id.editTextUsername);
        imageView = findViewById(R.id.imageView);

        progressBar = findViewById(R.id.progressbar);

        mAuth = FirebaseAuth.getInstance();

        //This wil load all filled in (working atm) information
        loadUserInformation();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageChooser();
            }
        });

        findViewById(R.id.buttonSaveUsername).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserInformation();

            }
        });

        findViewById(R.id.buttonLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logout();

            }
        });

        findViewById(R.id.buttonContinue).setOnClickListener(new View.OnClickListener() {
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
        }



    private void Logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user == null){
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
        // If user already has a display name, hide 'save username' button
//        if (user.getDisplayName() !=null){
//            Button itemSave = findViewById(R.id.buttonSaveUsername);
//            itemSave.setVisibility(View.GONE);
//        }
    }

    private void loadUserInformation() {
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            if (user.getDisplayName() != null) {
                editText.setText(user.getDisplayName());
            }

        }
    }

    private void saveUserInformation() {
        String displayName = editText.getText().toString();

        if(displayName.isEmpty()){
            editText.setError("Name required");
            editText.requestFocus();
            return;
        }

        FirebaseUser user = mAuth.getCurrentUser();

        if(user !=null){
            progressBar.setVisibility(View.VISIBLE);
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName)
                  // TODO: FIX THIS BECAUSE IT MAKES 'SAVE USER INFO' CRASH!!!
                 //   .setPhotoUri(Uri.parse(profileImageURL))
                    .build();

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



    // Not working properly yet
    private void uploadImageToFirebaseStorage() {
        final StorageReference profileImageReference = FirebaseStorage.getInstance().getReference("profilepictures/"+System.currentTimeMillis()+".jpg");

        if(uriProfileImage != null){
            progressBar.setVisibility(View.VISIBLE);
            profileImageReference.putFile(uriProfileImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>(){

                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressBar.setVisibility(View.GONE);
                            profileImageURL = taskSnapshot.getDownloadUrl().toString();

                        }
                    })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressBar.setVisibility(View.GONE);
                        Toast.makeText(ProfileActivity.this, e.getMessage(),Toast.LENGTH_SHORT).show();

                }
            });
        }
        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        if(requestCode == CHOOSE_IMAGE && requestCode == RESULT_OK && data != null && data.getData() != null){
//            uriProfileImage = data.getData();
//            try {
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriProfileImage);
//                imageView.setImageBitmap(bitmap);
//
//                uploadImageToFirebaseStorage();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

        // Fixed thanks to: https://stackoverflow.com/questions/28155972/imageview-not-showing-image-selected-from-gallery

        if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && null != data && data.getData() !=null) {
            Uri uriProfileImage = data.getData();
            try {
                imageView.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), uriProfileImage));
                uploadImageToFirebaseStorage();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    // Not working properly yet
        private void showImageChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent. setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "select profile image"), CHOOSE_IMAGE);
    }




}
