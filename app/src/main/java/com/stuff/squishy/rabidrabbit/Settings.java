package com.stuff.squishy.rabidrabbit;

//----------------------Activity class to view and alter the users settings ----------------

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;

public class Settings extends AppCompatActivity {

    private ImageView menu;
    private Switch sw_units;
    private FirebaseDatabase fb_database;
    private FirebaseAuth fb_auth;
    private DatabaseReference ref;
    private Connections conn = new Connections();
    private boolean isImperial;
    private User user;
    private int weightAtGoal;
    private DecimalFormat df = new DecimalFormat("#");
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        fb_auth = conn.getFb_authInstance();
        fb_database = conn.getFb_database();

        menu = findViewById(R.id.img_menu_settings);
        sw_units = findViewById(R.id.switch_units);
        drawerLayout = findViewById(R.id.drawer_Settings);
        navigationView = findViewById(R.id.settings_Nav);

        ref = fb_database.getReference(fb_auth.getCurrentUser().getUid());

        ref.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                user = dataSnapshot.child("Profile").getValue(User.class);
                if(user != null)
                {
                    if(user.isImperial())
                    {
                        sw_units.setChecked(true);
                    }
                }
                if(dataSnapshot.hasChild("WeightAtGoalSet"))
                {
                    weightAtGoal = dataSnapshot.child("WeightAtGoalSet").getValue(int.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });

        sw_units.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(user != null)
                {
                    if (isChecked && !user.isImperial()) {
                        user.setImperial(true);
                        user.setWeightGoal((int) (user.getWeightGoal() * 2.2));
                        user.setWeight(df.format(Double.parseDouble(user.getWeight()) * 2.2) + "");
                        user.setHeight(df.format(Double.parseDouble(user.getHeight()) * 0.4) + "");
                        weightAtGoal = (int)(weightAtGoal * 2.2);
                    } else
                        if(!isChecked && user.isImperial())
                        {
                        user.setImperial(false);
                        user.setWeightGoal((int) (user.getWeightGoal() / 2.2));
                        user.setWeight(df.format(Double.parseDouble(user.getWeight()) / 2.2) + "");
                        user.setHeight(df.format(Double.parseDouble(user.getHeight()) / 0.4) + "");
                        weightAtGoal = (int)(weightAtGoal / 2.2);
                    }
                    ref.child("Profile").setValue(user);
                    ref.child("WeightAtGoalSet").setValue(weightAtGoal);
                }
            }
        });

        menu.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                drawerLayout.openDrawer(Gravity.LEFT);
                return false;
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
            {
                Intent intent;
                switch (menuItem.getItemId())
                {
                    case R.id.Fitness:
                    {
                        //Fitness
                        intent = new Intent(Settings.this, Home.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.Wellness:
                    {
                        //Wellness
                        intent = new Intent(Settings.this, Wellness.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.MyStuff:
                    {
                        //My Stuff
                        intent = new Intent(Settings.this, MyStuff.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.Gallery:
                    {
                        //My Stuff
                        intent = new Intent(Settings.this, Gallery.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.Manage:
                    {
                        //My Stuff
                        intent = new Intent(Settings.this, ManageInfo.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.Settings:
                    {
                        //Logout
                        drawerLayout.closeDrawers();
                        break;
                    }
                    case R.id.Logout:
                    {
                        //Logout
                        fb_auth.signOut();
                        intent = new Intent(Settings.this, Login.class);
                        startActivity(intent);
                        break;
                    }
                    default:
                    {
                        break;
                    }
                }

                return false;
            }
        });


    }
}
