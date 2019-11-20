package com.stuff.squishy.rabidrabbit;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.VideoView;
import java.util.List;

public class VideoAdapter extends GalleryAdapter
{

    public VideoAdapter(Context context, List<String> imgUrls)
    {
        super(context, imgUrls);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        VideoView videoView;
        if(convertView == null)
        {
            videoView = new VideoView(mContext);
            videoView.setLayoutParams(new GridView.LayoutParams(300,300));
            videoView.setPadding(8, 8, 8, 8);
        }
        else
        {
            videoView = (VideoView) convertView;
        }
        videoView.setClickable(false);
        videoView.setFocusable(false);
        videoView.setFocusableInTouchMode(false);
        videoView.setVideoURI(Uri.parse(mImgUrls.get(position)));
        videoView.seekTo(100);

        return videoView;
    }
}
