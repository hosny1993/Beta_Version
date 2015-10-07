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
 * Created by prof on 10/3/15.
 */
public class reviewsAdapter extends ArrayAdapter<String>{

    int mReviewsCount;
    String[] reviewsUsers;
    String[] reviewsContents;
    Context mContext;

    public reviewsAdapter(Context context, String[] reviewsU, String[] reviewsC, int reviewsCount) {

        super(context, reviewsCount, reviewsCount);
        this.reviewsUsers = reviewsU;
        this.reviewsContents = reviewsC;
        this.mContext = context;
        this.mReviewsCount = reviewsCount;
    }

    @Override
    public int getCount() {
        Log.v("COUNT", "" + mReviewsCount);
        return mReviewsCount;
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

        if(convertView == null) {
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.review_list_item,
                    parent, false);
        }
        TextView user = (TextView) convertView.findViewById(R.id.review_user_text_view);
        TextView review   = (TextView) convertView.findViewById(R.id.review_content_text_view);

        user.setText(reviewsUsers[position]);
        review.setText(reviewsContents[position]);

        return convertView;
    }
}
