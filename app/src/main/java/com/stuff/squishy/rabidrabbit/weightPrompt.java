package com.stuff.squishy.rabidrabbit;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class weightPrompt extends AppCompatActivity {

    private Button saveButt;
    private EditText currentWeight;
    private Connections conn = new Connections();
    private FirebaseAuth fb_auth;
    private FirebaseDatabase fb_database;
    private DatabaseReference ref;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_prompt);

        saveButt = findViewById(R.id.btn_saveWeight);
        currentWeight = findViewById(R.id.eTxt_CurrentWeight);
        fb_auth = conn.getFb_authInstance();
        fb_database = conn.getFb_database();
        ref = fb_database.getReference(fb_auth.getCurrentUser().getUid());

        ref.child("Profile").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                user = dataSnapshot.getValue(User.class);
                currentWeight.setText(user.getWeight());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });

        saveButt.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                user.setWeight(currentWeight.getText().toString());
                ref.child("Profile").setValue(user);
                finish();
            }
        });

    }
}
