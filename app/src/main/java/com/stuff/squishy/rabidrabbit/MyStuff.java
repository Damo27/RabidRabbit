package com.stuff.squishy.rabidrabbit;

//----------------------Activity class to view the users profile info and gallery snip----------------
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MyStuff extends AppCompatActivity {

    Button manage;
    Button set;
    TextView tv_name, tv_weight, tv_height, tv_weightLoss;
    ImageView iv_profileOne, iv_profileTwo, iv_profileThree;
    ImageView menu;
    User user;
    FirebaseDatabase fb_database;
    FirebaseAuth fb_auth;
    DatabaseReference ref;
    Connections conn = new Connections();
    String weightUnit = " kg", heightUnit = " cm";
    int weightLossGoal = 75;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_stuff);

        iv_profileOne = findViewById(R.id.img_profilePic1);
        iv_profileTwo = findViewById(R.id.img_profilePic2);
        iv_profileThree = findViewById(R.id.img_profilePic3);
        tv_name = findViewById(R.id.txt_name);
        tv_weight = findViewById(R.id.txt_weight);
        tv_height = findViewById(R.id.txt_height);
        tv_weightLoss = findViewById(R.id.txt_weightLossNum);
        menu = findViewById(R.id.img_menu_stuff);
        drawerLayout = findViewById(R.id.drawer_stuff);
        navigationView = findViewById(R.id.myStuff_Nav);

        fb_auth = conn.getFb_authInstance();
        fb_database = conn.getFb_database();

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
                        intent = new Intent(MyStuff.this, Home.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.Wellness:
                    {
                        //Wellness
                        intent = new Intent(MyStuff.this, Wellness.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.MyStuff:
                    {
                        //Wellness
                        drawerLayout.closeDrawers();
                        break;
                    }
                    case R.id.Gallery:
                    {
                        //My Stuff
                        intent = new Intent(MyStuff.this, Gallery.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.Manage:
                    {
                        //My Stuff
                        intent = new Intent(MyStuff.this, ManageInfo.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.Settings:
                    {
                        //Settings
                        intent = new Intent(MyStuff.this, Settings.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.Logout:
                    {
                        //Logout
                        fb_auth.signOut();
                        intent = new Intent(MyStuff.this, Login.class);
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

        menu.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                drawerLayout.openDrawer(Gravity.LEFT);
                return false;
            }
        });

        //--------Populate information with current user info------------------------------
        ref = fb_database.getReference(fb_auth.getCurrentUser().getUid());
        ref.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                User user = dataSnapshot.child("Profile").getValue(User.class);
                if(user != null)
                {
                    if(user.isImperial())
                    {
                        weightUnit = " lbs";
                        heightUnit = " Inches";
                    }
                    tv_name.setText(user.getName());
                    tv_weight.setText(user.getWeight() + weightUnit);
                    tv_height.setText(user.getHeight() + heightUnit);
                }
                if(dataSnapshot.hasChild("WeightAtGoalSet"))
                {
                    weightLossGoal = dataSnapshot.child("WeightAtGoalSet").getValue(int.class);
                    tv_weightLoss.setText(weightLossGoal + weightUnit);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });

        //----------get list of img url's in users gallery in realtime db---------------------
        ref.child("Gallery").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                Iterable<DataSnapshot> images = dataSnapshot.getChildren();
                List<String> urls = new ArrayList<>();

                for(DataSnapshot image: images)
                {
                    urls.add(image.getValue().toString());
                }

                populateImages(urls);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });

        manage = findViewById(R.id.btn_manage);

        //--------------------if clicked open Manage stuff activity
        manage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MyStuff.this, ManageInfo.class);
                startActivity(intent);
            }
        });

    }

    //--------receives list of strings and populates gallery snip----------------------------
    //_________________________code attribution_______________________
    // The following method was adapted from GitHub:
    //Author :Picasso
    //Link: http://square.github.io/picasso/
    protected void populateImages(List<String> list)
    {
        list.size();
        switch(list.size())
        {
            case 0:
                {
                    break;
                }
            case 1:
            {
                Picasso.get().load(list.get(0)).into(iv_profileOne);
                break;
            }
            case 2:
                {
                    Picasso.get().load(list.get(0)).into(iv_profileOne);
                    Picasso.get().load(list.get(1)).into(iv_profileTwo);
                    break;
                }
                default:
                    {
                        Picasso.get().load(list.get(0)).into(iv_profileOne);
                        Picasso.get().load(list.get(1)).into(iv_profileTwo);
                        Picasso.get().load(list.get(2)).into(iv_profileThree);
                        break;
                    }
        }
    }
}
