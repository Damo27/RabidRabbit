package com.stuff.squishy.rabidrabbit;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.squareup.picasso.Picasso;

public class VideoFullscreen extends AppCompatActivity
{

    private VideoView vv_fullscreen;
    private Uri uri;
    private String clickedImg;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_fullscreen);

        vv_fullscreen = findViewById(R.id.videoView_fullscreen);

        Intent parent = getIntent();
        if(parent != null)
        {
            clickedImg = parent.getStringExtra("URI");
            uri = Uri.parse(clickedImg);
            if(uri != null && vv_fullscreen != null)
            {
                MediaController vidControl = new MediaController(this);
                Toast.makeText(this, "Loading Video",Toast.LENGTH_LONG).show();
                vv_fullscreen.setVideoURI(uri);
                vv_fullscreen.setMediaController(vidControl);
                vidControl.setAnchorView(vv_fullscreen);
                vv_fullscreen.start();
            }
        }
    }
}
