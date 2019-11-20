package com.stuff.squishy.rabidrabbit;

//------------------------------Activity class to manage registration of new user-----------------------

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

public class Registration extends AppCompatActivity
{
    FirebaseAuth fb_auth;
    Button butt_reg;
    EditText ed_email;
    EditText ed_pass;
    EditText ed_confirm;
    String email;
    String password;
    String confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Connections conn = new Connections();
        fb_auth = conn.getFb_authInstance();

        butt_reg = findViewById(R.id.btn_reg);
        ed_email = findViewById(R.id.eTxt_mail);
        ed_pass = findViewById(R.id.eTxt_pass);
        ed_confirm = findViewById(R.id.eTxt_confirm);

        //-----------------------------if clicked and validated call registerUser()---------------------
        butt_reg.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                email = ed_email.getText().toString();
                password = ed_pass.getText().toString();
                confirm = ed_confirm.getText().toString();

                if(password.length() >= 6)
                {
                    if(password.equals(confirm))
                    {
                        registerUser();
                    }
                    else
                        {
                            Toast.makeText(Registration.this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
                        }

                }
                else
                    {
                        Toast.makeText(Registration.this, "Password must be a minimum of 6 characters.", Toast.LENGTH_SHORT).show();
                    }

            }
        });
    }

    //-----------------------------Method creates user with Firebase Auth email and password----------------
    private void registerUser()
    {
        try
        {
            fb_auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if (task.isSuccessful())
                            {
                                FirebaseUser user = fb_auth.getCurrentUser();
                                Toast.makeText(Registration.this, "Registration successful.", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            else
                                {
                                    Toast.makeText(Registration.this, "Registration failed.", Toast.LENGTH_SHORT).show();
                                }

                        }
                    });
        }
        catch(IllegalArgumentException e)
        {
            Toast.makeText(this, "Email field cannot be empty.", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e)
        {
            Toast.makeText(this, "Unable to login.", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Error message: " + e.getClass().toString(), Toast.LENGTH_LONG).show();
        }
    }
}
