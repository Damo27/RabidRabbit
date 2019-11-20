package com.stuff.squishy.rabidrabbit;

//----------------------Activity class to manage the meditation section ---------------------------

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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Wellness extends AppCompatActivity {

    //private Button butt_sleep;
    private ImageView menu;
    private FirebaseAuth fb_auth;
    private Connections conn = new Connections();
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private FirebaseDatabase fb_database = conn.getFb_database();
    private List<String> urls;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wellness);
        fb_auth = conn.getFb_authInstance();
        ref = fb_database.getReference().child("Meditations");

        //butt_sleep = findViewById(R.id.btn_sleepBlack);
        menu = findViewById(R.id.img_menu_wellness);
        drawerLayout = findViewById(R.id.drawer_Wellness);
        navigationView = findViewById(R.id.wellness_Nav);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                Iterable<DataSnapshot> images = dataSnapshot.getChildren();
                urls = new ArrayList<>();
                for(DataSnapshot image: images)
                {
                    urls.add(image.getValue().toString());
                }

                //_________________________code attribution_______________________
                // The following method was adapted from YouTube:
                //Author :The Code Hard Truth
                //Link: https://www.youtube.com/watch?v=cKevqBoV2ws
                GridView gridView = (GridView)findViewById(R.id.gv_wellness);
                gridView.setAdapter(new VideoAdapter(getApplicationContext(), urls));

                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                    {
                        Intent intent = new Intent(Wellness.this, VideoFullscreen.class);
                        intent.putExtra("URI", urls.get(position));
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

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

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
            {
                Intent intent;
                switch (menuItem.getItemId())
                {
                    case R.id.Fitness:
                    {
                        //Fitness
                        intent = new Intent(Wellness.this, Home.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.Wellness:
                    {
                        //Wellness
                        drawerLayout.closeDrawers();
                        break;
                    }
                    case R.id.MyStuff:
                    {
                        //My Stuff
                        intent = new Intent(Wellness.this, MyStuff.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.Gallery:
                    {
                        //My Stuff
                        intent = new Intent(Wellness.this, Gallery.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.Manage:
                    {
                        //My Stuff
                        intent = new Intent(Wellness.this, ManageInfo.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.Settings:
                    {
                        //Settings
                        intent = new Intent(Wellness.this, Settings.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.Logout:
                    {
                        //Logout
                        fb_auth.signOut();
                        intent = new Intent(Wellness.this, Login.class);
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
        //on click new Sleep intent
//        butt_sleep.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                Intent intent = new Intent(Wellness.this, Sleep.class);
//                startActivity(intent);
//            }
//        });

    }
}
