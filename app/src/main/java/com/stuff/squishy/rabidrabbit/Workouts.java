package com.stuff.squishy.rabidrabbit;

//----------------------Activity class to manage the workouts section ---------------------------

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
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
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Workouts extends AppCompatActivity {

    private Button butt_activities;
    private ImageView menu;
    private FirebaseAuth fb_auth;
    private Connections conn = new Connections();
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private FirebaseDatabase fb_database = conn.getFb_database();
    private List<String> urls;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workouts);
        fb_auth = conn.getFb_authInstance();
        ref = fb_database.getReference().child("Workouts");

        butt_activities = findViewById(R.id.btn_activitiesBlack);
        menu = findViewById(R.id.img_menu_workouts);
        drawerLayout = findViewById(R.id.drawer_workouts);
        navigationView = findViewById(R.id.workouts_Nav);

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
                GridView gridView = (GridView)findViewById(R.id.gv_workouts);
                gridView.setAdapter(new VideoAdapter(getApplicationContext(), urls));


                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                    {

                        Intent intent = new Intent(Workouts.this, VideoFullscreen.class);
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
                        drawerLayout.closeDrawers();
                        break;
                    }
                    case R.id.Wellness:
                    {
                        //Wellness
                        intent = new Intent(Workouts.this, Wellness.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.MyStuff:
                    {
                        //My Stuff
                        intent = new Intent(Workouts.this, MyStuff.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.Gallery:
                    {
                        //My Stuff
                        intent = new Intent(Workouts.this, Gallery.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.Manage:
                    {
                        //My Stuff
                        intent = new Intent(Workouts.this, ManageInfo.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.Settings:
                    {
                        //Settings
                        intent = new Intent(Workouts.this, Settings.class);
                        startActivity(intent);
                        break;
                    }
                    case 4:
                    {
                        //Logout
                        fb_auth.signOut();
                        intent = new Intent(Workouts.this, Login.class);
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



        butt_activities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
    }

}
