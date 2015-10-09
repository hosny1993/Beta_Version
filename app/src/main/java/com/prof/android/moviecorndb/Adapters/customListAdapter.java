package com.prof.android.moviecorndb.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.prof.android.moviecorndb.MainActivity;
import com.prof.android.moviecorndb.MainActivityFragment;
import com.prof.android.moviecorndb.R;
import com.prof.android.moviecorndb.Utility.AndroidFiles;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by prof on 8/19/15.
 */
public class customListAdapter extends BaseAdapter {

    private Context mContext;
    int state;
    int mCount;
    private String[] mThumbIds ;

    public customListAdapter(Context context, String[] param, int state, int count) {
        super();

        this.mContext  = context;
        mThumbIds = param;
        this.state = state;
        this.mCount = count;
    }

    @Override
    public int getCount(){
        if(mThumbIds != null) {
            return this.mCount;
        }
        else
            return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return  position;
    }

    @Override
    public View getView(final int position,View convertView,ViewGroup parent) {

        convertView = LayoutInflater.from(this.mContext).inflate(R.layout.image_list_item,
                    parent, false);

        ImageView imageView = (ImageView) convertView.findViewById(R.id.image_view_item_1);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        final String SCHEMA      = "http";
        final String AUTHORITY   = "image.tmdb.org";
        final String PATH        = "t/p/w342";
        String APPEND_PATH  = mThumbIds[position];

        Log.v("MTHUU", " "+mThumbIds[position]);

        Uri.Builder uri = new Uri.Builder();
        uri.scheme(SCHEMA).authority(AUTHORITY).path(PATH).build();

        String myUri = uri.toString()+APPEND_PATH;
        Log.v("uri", " :  " + position);

        if (this.state == 0) {
            try{
                Picasso.with(this.mContext).load(myUri).into(imageView);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        else {
            try {
                Picasso.with(this.mContext).load(myUri).resize(200,300).centerCrop().into(imageView);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        return convertView;
    }

}
