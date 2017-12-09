package com.example.tom_d.moviebase;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth mAuth;
    EditText editTextEmail, editTextPassword;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        progressBar = findViewById(R.id.progressbar);

        findViewById(R.id.textViewSignup).setOnClickListener(this);
        findViewById(R.id.buttonLogin).setOnClickListener(this);
    }

    private void userLogin(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        //Make sure email is entered
        if (email.isEmpty()) {
            editTextEmail.setError("Email is required!");
            editTextEmail.requestFocus();
            return;
        }

        //Check if email is valid
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Put in valid email please");
            editTextEmail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            editTextPassword.setError("Password is required!");
            editTextPassword.requestFocus();
            return;
        }
        if (password.length()<6){
            editTextEmail.setError("Need more characters than 6");
            editTextEmail.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        //Check if login is successfull
                        if(task.isSuccessful()) {
                            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }else{
                            //Show error; login failed
                            Toast.makeText(MainActivity.this,"Registration Error",Toast.LENGTH_LONG).show();
                        }
                    }
                });
                        }


    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.textViewSignup:
                startActivity(new Intent(this, SignupActivity.class));
                break;
            case R.id.buttonLogin:
                userLogin();
                break;
        }
    }
}
