package com.stuff.squishy.rabidrabbit;

//------------------------------Activity class to manage the users profile section-----------------------
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ManageInfo extends AppCompatActivity
{
    //--------------------Class variables---------------------------------------
    EditText ed_name, ed_weight, ed_height;
    String name, weight, height;
    SeekBar seek_steps, seek_weight;
    TextView tv_steps, tv_weight;
    int steps, weightLoss, goalWeightDiff;
    Button butt_update, butt_gallery;
    FirebaseDatabase fb_database;
    FirebaseAuth fb_auth;
    DatabaseReference ref;
    Connections conn = new Connections();
    boolean isImperial = false;
    //--------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_manage_info);

            fb_auth = conn.getFb_authInstance();
            fb_database = conn.getFb_database();

        //------------------------------set views by id---------------------------------------------
        ed_name = findViewById(R.id.eTxt_name);
        ed_weight = findViewById(R.id.eTxt_Weight);
        ed_height = findViewById(R.id.eTxt_Height);
        seek_steps = findViewById(R.id.sb_steps);
        seek_weight = findViewById(R.id.sb_weightLoss);
        butt_update = findViewById(R.id.btn_update);
        butt_gallery = findViewById(R.id.btn_galleryBlack);
        tv_steps = findViewById(R.id.txt_stepsNum);
        tv_weight = findViewById(R.id.txt_weightNum);

        //----------------read Profile from current users db partition and extract a user obj------------
            ref = fb_database.getReference(fb_auth.getCurrentUser().getUid());
            ref.child("Profile").addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    User user = dataSnapshot.getValue(User.class);
                    if(user != null)
                    {
                        ed_name.setText(user.getName());
                        ed_weight.setText(user.getWeight());
                        ed_height.setText(user.getHeight());
                        seek_steps.setProgress(user.getStepGoal());
                        seek_weight.setProgress(user.getWeightGoal());
                        isImperial = user.isImperial();

                        weightLoss = user.getWeightGoal();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {

                }
            });

        //---------------------save the changes to users profile-----------------------------
        butt_update.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    User user = new User();
                    name = ed_name.getText().toString();
                    weight = ed_weight.getText().toString();
                    height = ed_height.getText().toString();
                    steps = seek_steps.getProgress();
                    if(weightLoss != seek_weight.getProgress())
                    {
                        ref.child("WeightAtGoalSet").setValue(Integer.parseInt(weight));
                    }
                    weightLoss = seek_weight.getProgress();
                    user.setName(name);
                    user.setWeight(weight);
                    user.setHeight(height);
                    user.setStepGoal(steps);
                    user.setWeightGoal(weightLoss);
                    user.setImperial(isImperial);
                    ref.child("Profile").setValue(user);
                    Toast.makeText(ManageInfo.this, "Profile Updated.", Toast.LENGTH_SHORT).show();
                    finish();
                }
                catch(Exception e)
                {
                    Toast.makeText(ManageInfo.this, "Unable to Update Profile.", Toast.LENGTH_SHORT).show();
                    Toast.makeText(ManageInfo.this, "Error message: " + e.getClass().toString(), Toast.LENGTH_LONG).show();
                }

            }
        });

        butt_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageInfo.this, Gallery.class);
                startActivity(intent);
            }
        });

        seek_weight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                tv_weight.setText(progress + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {

            }
        });

        seek_steps.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                tv_steps.setText(progress + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

}
