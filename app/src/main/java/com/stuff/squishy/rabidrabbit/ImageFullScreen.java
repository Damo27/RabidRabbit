package com.stuff.squishy.rabidrabbit;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ImageFullScreen extends AppCompatActivity
{

    private ImageView iv_fullscreen;
    private Uri uri;
    private String clickedImg;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_full_screen);

        iv_fullscreen = findViewById(R.id.img_fullscreen);

        Intent parent = getIntent();
        if(parent != null)
        {
            clickedImg = parent.getStringExtra("URI");
            uri = Uri.parse(clickedImg);
            if(uri != null && iv_fullscreen != null)
            {
                Picasso.get().load(uri).into(iv_fullscreen);
            }
        }
    }
}
