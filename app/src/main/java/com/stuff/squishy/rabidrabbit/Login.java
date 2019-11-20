package com.stuff.squishy.rabidrabbit;

//------------------------------Activity class to manage the Login section-----------------------

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity implements View.OnClickListener
{

    FirebaseAuth fb_auth;
    EditText ed_email;
    EditText ed_password;
    String userEmail, userPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Connections conn = new Connections();
        fb_auth = conn.getFb_authInstance();

        ed_email = findViewById(R.id.eTxt_email);
        ed_password = findViewById(R.id.eTxt_password);
        findViewById(R.id.btn_login).setOnClickListener(this);
        findViewById(R.id.btn_register).setOnClickListener(this);

    }

    @Override
    public void onStart()
    {
        super.onStart();
        FirebaseUser currentUser = fb_auth.getCurrentUser();
        loggedIn(currentUser);
    }

    private void loggedIn(FirebaseUser user)
    {
        boolean loggedIn = (user != null);
        if(loggedIn)
        {
            Intent home = new Intent(this, Home.class);
            startActivity(home);

            Toast.makeText(this, "Logging in", Toast.LENGTH_SHORT).show();
        }
    }

    //_________________________code attribution_______________________
    // The following method was adapted from GitHub:
    //Author : Harshit Dwivedi
    //Link: https://github.com/firebase/quickstart-android/blob/master/auth/app/src/main/java/com/google/firebase/quickstart/auth/java/AnonymousAuthActivity.java
    @Override
    public void onClick(View v)
    {
        int i = v.getId();

        if (i == R.id.btn_login)
        {
            userEmail = ed_email.getText().toString();
            userPassword = ed_password.getText().toString();
            signIn();
        }
        else
            if (i == R.id.btn_register)
            {
                registration();
            }
    }

    //-----------------------------signns in with email authentication-----------------------------
    public void signIn()
    {
        try
        {
            fb_auth.signInWithEmailAndPassword(userEmail, userPassword)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if (task.isSuccessful())
                            {
                                FirebaseUser user = fb_auth.getCurrentUser();
                                loggedIn(user);
                            }
                                else
                                {
                                    Toast.makeText(Login.this, "Incorrect username and password.", Toast.LENGTH_SHORT).show();
                                }
                        }
                    });
        }
        catch(IllegalArgumentException e)
        {
            Toast.makeText(this, "Email and Password cannot be empty", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e)
        {
            Toast.makeText(this, "Unable to login.", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Error message: " + e.getClass().toString(), Toast.LENGTH_LONG).show();
        }
    }

    //------------------------calls registration activity-------------------------------------------
    public void registration()
    {
        Intent registration = new Intent(this, Registration.class);
        startActivity(registration);
    }
}
