package com.prof.android.moviecorndb.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.prof.android.moviecorndb.R;

/**
 * Created by prof on 10/2/15.
 */
public class trailersAdapter extends ArrayAdapter<String>{

    int mtrailersCount;
    String[] listOfTrailersNames;
    Context mContext;

    public trailersAdapter(Context context, String[] trailersNames, int trailersCount) {

        super(context, trailersCount, trailersCount);
        this.listOfTrailersNames = trailersNames;
        this.mContext = context;
        this.mtrailersCount = trailersCount;
    }

    @Override
    public int getCount() {
        Log.v("COUNT",""+mtrailersCount);
        return mtrailersCount;
    }

    @Override
    public void add(String object) {
        super.add(object);
        setNotifyOnChange(true);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Log.v("POS",""+position);

        if(convertView == null) {
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.trailer_list_item,
                    parent, false);
        }


        ImageView imageView = (ImageView) convertView.findViewById(R.id.image_view_list_item_trailer);
        TextView textView   = (TextView) convertView.findViewById(R.id.text_view_list_item_trailer);


        textView.setText(listOfTrailersNames[position]);
        Log.v("NAMEE",listOfTrailersNames[position]);

        return convertView;
    }
}
