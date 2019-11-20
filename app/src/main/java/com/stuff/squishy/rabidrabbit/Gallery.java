package com.stuff.squishy.rabidrabbit;

//----Activity class to manage items in the galaery, including user uploads and a gridview of images----------------------
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Gallery extends AppCompatActivity
{
    //-------------------Set Class variable---------------------------------------------------------
    Button butt_info;
    ImageView iv_addImg;
    Connections conn = new Connections();
    FirebaseAuth fb_auth = conn.getFb_authInstance();
    FirebaseDatabase fb_database = conn.getFb_database();
    List<String> urls;
    //----------------------------------------------------------------------------------------------


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        //---------------------find views and assign them to class variables------------------------
        butt_info = findViewById(R.id.btn_infoBlack);
        iv_addImg = findViewById(R.id.img_addPic);
        //------------------------------------------------------------------------------------------


        //On touch listener for image view to add images to users storage account
        iv_addImg.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 2);
                return false;
            }
        });

        //on click finish activity
        butt_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Gallery.this, ManageInfo.class);
                startActivity(intent);
                finish();
            }
        });

        DatabaseReference ref = fb_database.getReference().child(fb_auth.getCurrentUser().getUid()).child("Gallery");

        //event listener for data change of in Gallery database reference to populate gridview
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
                GridView gridView = (GridView)findViewById(R.id.gv_gallery);
                gridView.setAdapter(new GalleryAdapter(getApplicationContext(), urls));

                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                    {
                        Intent intent = new Intent(Gallery.this, ImageFullScreen.class);
                        intent.putExtra("URI", urls.get(position));
                        startActivity(intent);
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //reacts to the addition of file to Firebase storage
    @Override
    protected void onActivityResult(int i, final int r, Intent data)
    {
        super.onActivityResult(i,r,data);

        if(i == 2 && r == RESULT_OK)
        {
            Uri uri = data.getData();
            final StorageReference fb_storageRef = conn.getFb_StorageRef().child(fb_auth.getCurrentUser().getUid()).child("Gallery").child(uri.getLastPathSegment());
            final DatabaseReference ref = fb_database.getReference().child(fb_auth.getCurrentUser().getUid()).child("Gallery").child(uri.getLastPathSegment());


            //_________________________code attribution_______________________
            // The following method was adapted from StackOverFlow:
            //Author :Ananth Raj Singh
            //Link: https://stackoverflow.com/questions/43641941/how-to-get-file-url-after-uploading-them-to-firebase/50986113
            fb_storageRef.putFile(uri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fb_storageRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>()
            {
                @Override
                public void onComplete(@NonNull Task<Uri> task)
                {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(Gallery.this, "Upload Success.", Toast.LENGTH_LONG).show();
                        Uri downloadUri = task.getResult();
                        ref.setValue(downloadUri.toString());
                    }
                    else
                        {
                            Toast.makeText(Gallery.this, "upload failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                }
            });


        }
    }

}
