package com.stuff.squishy.rabidrabbit;

//------------------------------Activity class to manage the Fitness section-----------------------
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class Home extends AppCompatActivity implements SensorEventListener {

    //----------Class variables--------------------------
    //private Spinner menu;
    private ImageView menu;
    private TextView tv_steps;
    private TextView tv_stepsGoal;
    private TextView tv_startWeight;
    private TextView tv_goalWeight;
    private TextView tv_currentWeight;
    private Button butt_workouts;
    private Connections conn = new Connections();
    private FirebaseAuth fb_auth;
    private FirebaseDatabase fb_database;
    private DatabaseReference ref;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ProgressBar pb_steps, pb_weight;
    private boolean isRunning, dataPulled;
    private Sensor stepsSensor;
    private SensorManager sensorManager;
    private int sensorSteps, todaysSteps = 0, stepGoal = 10000, currentWeight = 0, startWeight = 0, goalWeight = 0;
    private Date savedDate, sysdate;
    private User user;
    private final SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
    int PERMISSIONS_ALL = 1;



    //---------------------------------------------------


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //---------------------find views and assign them to class variables------------------------
        fb_auth = conn.getFb_authInstance();
        fb_database = conn.getFb_database();
        ref = fb_database.getReference(fb_auth.getCurrentUser().getUid());
        butt_workouts = findViewById(R.id.btn_workoutsBlack);
        menu = findViewById(R.id.img_menu);
        drawerLayout = findViewById(R.id.drawer_Home);
        navigationView = findViewById(R.id.home_Nav);
        tv_steps = findViewById(R.id.txt_steps);
        tv_stepsGoal = findViewById(R.id.txt_stepsGoal);
        pb_steps = findViewById(R.id.progressBar_steps);
        pb_weight = findViewById(R.id.progressBar_weight);
        tv_startWeight = findViewById(R.id.txt_startWeight);
        tv_goalWeight = findViewById(R.id.txt_goalWeight);
        tv_currentWeight = findViewById(R.id.txt_currentWeight);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        //Array of permissions required
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.INTERNET, Manifest.permission.BODY_SENSORS, Manifest.permission.READ_CALENDAR, Manifest.permission.WAKE_LOCK};

        //if permissions are not set prompt for them
        if(!hasPermissions(this,permissions))
        {
            ActivityCompat.requestPermissions(this,permissions,PERMISSIONS_ALL);
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
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
                        //wellness
                        intent = new Intent(Home.this, Wellness.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.MyStuff:
                    {
                        //My Stuff
                        intent = new Intent(Home.this, MyStuff.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.Gallery:
                    {
                        //My Stuff
                        intent = new Intent(Home.this, Gallery.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.Manage:
                    {
                        //My Stuff
                        intent = new Intent(Home.this, ManageInfo.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.Settings:
                    {
                        //Settings
                        intent = new Intent(Home.this, Settings.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.Logout:
                    {
                        //Logout
                        fb_auth.signOut();
                        intent = new Intent(Home.this, Login.class);
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

        //------------------------------------------------------------------------------------------

        //if clicked new intent
        butt_workouts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, Workouts.class);
                startActivity(intent);
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

        ref.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.hasChild("Date") && dataSnapshot.hasChild("SensorSteps") && dataSnapshot.hasChild("TodaysSteps"))
                {
                    savedDate = dataSnapshot.child("Date").getValue(Date.class);
                    sensorSteps = dataSnapshot.child("SensorSteps").getValue(int.class);
                    todaysSteps = dataSnapshot.child("TodaysSteps").getValue(int.class);
                    tv_steps.setText(String.valueOf(todaysSteps));
                }
                else
                    {
                        Calendar cal = Calendar.getInstance();
                        cal.add(Calendar.DATE, -1);
                        savedDate = cal.getTime();
                        ref.child("Date").setValue(savedDate);
                        ref.child("TodaysSteps").setValue(todaysSteps);
                    }
                if(dataSnapshot.hasChild("Profile"))
                {
                    user = dataSnapshot.child("Profile").getValue(User.class);
                    stepGoal = user.getStepGoal();
                    goalWeight = user.getWeightGoal();
                    currentWeight = Integer.parseInt(user.getWeight());
                    startWeight = dataSnapshot.child("WeightAtGoalSet").getValue(int.class);
                    pb_steps.setMax(stepGoal);
                    pb_steps.setProgress(todaysSteps);
                    tv_steps.setText(String.valueOf(todaysSteps));
                    tv_stepsGoal.setText("/" + String.valueOf(stepGoal));
                    pb_weight.setMax(Math.abs(startWeight - goalWeight));
                    pb_weight.setProgress(Math.abs(startWeight - currentWeight));
                    tv_currentWeight.setText(String.valueOf(currentWeight));
                    tv_goalWeight.setText(String.valueOf(goalWeight));
                    tv_startWeight.setText(String.valueOf(startWeight));
                }
                else
                    {
                        user = new User();
                    }
                dataPulled = true;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });

    }



    @Override
    protected void onResume()
    {
        super.onResume();
        isRunning = true;
        sysdate = Calendar.getInstance().getTime();

        stepsSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);


        if(stepsSensor != null)
        {
            sensorManager.registerListener(this, stepsSensor,sensorManager.SENSOR_DELAY_UI);
        }

    }

    @Override
    protected void onPause()
    {
        super.onPause();
        ref.child("TodaysSteps").setValue(todaysSteps);
        isRunning = false;
    }



    @Override
    public void onSensorChanged(SensorEvent event)
    {
        if(isRunning)
        {
            if(dataPulled)
            {
                if (!df.format(sysdate).equals(df.format(savedDate)))
                {
                    ref.child("Date").setValue(sysdate);
                    savedDate = sysdate;
                    sensorSteps = (int) event.values[0];
                    ref.child("SensorSteps").setValue(sensorSteps);
                    Intent intent = new Intent(this, weightPrompt.class);
                    startActivity(intent);
                }
                todaysSteps = (int) event.values[0] - sensorSteps;
                tv_steps.setText(String.valueOf(todaysSteps));
                pb_steps.setProgress(todaysSteps);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {

    }

    public static boolean hasPermissions(Context context, String[] permissions)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null)
        {
            for(String permission : permissions)
            {
                if (ActivityCompat.checkSelfPermission(context,permission) != PackageManager.PERMISSION_GRANTED)
                {
                    return false;
                }
            }
        }
        return true;
    }
}
