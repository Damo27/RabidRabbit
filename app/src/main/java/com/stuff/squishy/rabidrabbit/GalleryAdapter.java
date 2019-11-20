package com.stuff.squishy.rabidrabbit;

//---------------------Extention of data adapter class to populate grid view with images------------
//_________________________code attribution_______________________
// The following class was adapted from YouTube:
//Author :The Code Hard Truth
//Link: https://www.youtube.com/watch?v=cKevqBoV2ws
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class GalleryAdapter extends BaseAdapter
{
    protected Context mContext;

    protected List<String> mImgUrls;

    public GalleryAdapter(Context context, List<String> imgUrls)
    {
        this.mContext = context;
        this.mImgUrls = imgUrls;
    }

    @Override
    public int getCount()
    {
        return mImgUrls.size();
    }

    @Override
    public Object getItem(int position)
    {
        return mImgUrls.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ImageView imageView;
        if(convertView == null)
        {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(300,300));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        }
        else
            {
                imageView = (ImageView) convertView;
            }
        Picasso.get().load(mImgUrls.get(position)).into(imageView);

        return imageView;
    }
}
