package com.prof.android.moviecorndb.Adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.prof.android.moviecorndb.R;
import com.prof.android.moviecorndb.Utility.AndroidFiles;
import com.squareup.picasso.Picasso;

/**
 * Created by prof on 8/19/15.
 */
public class customListAdapter extends ArrayAdapter<String> {

    //private safeLoadToSDCard safeData;
    private Context mContext;
    private String[] mThumbIds = null;
    int state;

    public customListAdapter(Context context, String[] param, int state) {
        super(context, 0, 0);
        this.mContext  = context;
        this.mThumbIds = param;
        this.state = state;
    }

    @Override
    public void add(String object) {
        super.add(object);
        setNotifyOnChange(true);
    }

    @Override
    public int getCount(){
        if(mThumbIds != null) {
            return mThumbIds.length;
        }
        else
            return 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position,View convertView,ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.image_list_item,
                    parent, false);
        }
        ImageView imageView = (ImageView) convertView.findViewById(R.id.image_view_item_1);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        final String SCHEMA      = "http";
        final String AUTHORITY   = "image.tmdb.org";
        final String PATH        = "t/p/w342";
        String APPEND_PATH = null;

        if(mThumbIds != null) {
            APPEND_PATH  = mThumbIds[position];
        }

        Uri.Builder uri = new Uri.Builder();
        uri.scheme(SCHEMA).authority(AUTHORITY).path(PATH);

        String myUri = uri.toString()+APPEND_PATH;
        Log.v("uri", " :  " + myUri);

        if (this.state == 0) {
            Picasso.with(this.mContext).load(myUri).into(imageView);
            return convertView;
        }
        else {
            Picasso.with(this.mContext).load(myUri).resize(200,300).centerCrop().into(imageView);
            return convertView;
        }


    }

}
