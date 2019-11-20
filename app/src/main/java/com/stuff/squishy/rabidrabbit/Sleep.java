package com.stuff.squishy.rabidrabbit;

//----------------------Activity class to monitor and set sleep information-------------------------

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
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;

public class Sleep extends AppCompatActivity {

    private Button butt_meditation;
    private ImageView menu;
    private FirebaseAuth fb_auth;
    private Connections conn = new Connections();
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep);
        fb_auth = conn.getFb_authInstance();

        butt_meditation = findViewById(R.id.btn_meditationBlack);
        menu = findViewById(R.id.img_menu_sleep);
        drawerLayout = findViewById(R.id.drawer_Sleep);
        navigationView = findViewById(R.id.sleep_Nav);

        butt_meditation.setOnClickListener(new View.OnClickListener() {
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
                        intent = new Intent(Sleep.this, Home.class);
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
                        intent = new Intent(Sleep.this, MyStuff.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.Gallery:
                    {
                        //My Stuff
                        intent = new Intent(Sleep.this, Gallery.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.Manage:
                    {
                        //My Stuff
                        intent = new Intent(Sleep.this, ManageInfo.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.Settings:
                    {
                        //Settings
                        intent = new Intent(Sleep.this, Settings.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.Logout:
                    {
                        //Logout
                        fb_auth.signOut();
                        intent = new Intent(Sleep.this, Login.class);
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
