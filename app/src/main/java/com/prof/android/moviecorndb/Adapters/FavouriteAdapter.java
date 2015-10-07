package com.prof.android.moviecorndb.Adapters;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.prof.android.moviecorndb.R;
import com.prof.android.moviecorndb.Utility.AndroidFiles;
import com.prof.android.moviecorndb.data.MoviesContract;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by prof on 10/4/15.
 */
public class FavouriteAdapter extends ArrayAdapter<Long> {

    //private safeLoadToSDCard safeData;
    public final static String LOG_TAG = FavouriteAdapter.class.getSimpleName();
    private Context mContext;
    int mCount;
    ArrayList movieid;
    ImageView imageView;

    Uri uri = Uri.parse(MoviesContract.BASE_CONTENT_URI + "/" +
            MoviesContract.MOVIE.TABLE_NAME);

    public FavouriteAdapter(Context context, int Count, ArrayList ids) {
        super(context, 0, 0, ids);
        this.mContext = context;
        this.mCount = Count;
        movieid=new ArrayList();
        movieid.clear();
        movieid.addAll(ids);
    }

    @Override
    public int getCount() {
        Log.d(LOG_TAG, "COUNT: " + this.mCount);
        if (movieid != null)
            return movieid.size();
        else
            return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        Log.v(LOG_TAG, "ENTA MESH HENA: " + position);
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.image_list_item,
                    parent, false);


        imageView = (ImageView) convertView.findViewById(R.id.image_view_item_1);
        convertView.setTag(movieid.get(position));
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        File root = AndroidFiles.createFile((Long) movieid.get(position));
        Log.d(LOG_TAG, "FILE " + root);
        Picasso.with(mContext).load(root).into(imageView);

        return convertView;
    }

}
