package com.prof.android.moviecorndb;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.prof.android.moviecorndb.Adapters.reviewsAdapter;
import com.prof.android.moviecorndb.Utility.AndroidFiles;
import com.prof.android.moviecorndb.Utility.UtilityF;
import com.prof.android.moviecorndb.data.MoviesContract;
import com.squareup.picasso.Picasso;

import java.io.File;

public class favourite_movie_detail extends AppCompatActivity {

    long movie_id;
    ContentResolver resolver;
    Cursor cursor;
    TextView title;
    TextView release ;
    TextView rate ;
    ImageView imageView ;
    TextView tagline ;
    TextView revenue ;
    TextView time;
    TextView overvView ;
    ListView listView;

    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_movie_detail);

        resolver = getContentResolver();

        Intent intent = getIntent();
        movie_id = intent.getLongExtra("movie_id", 1);

        uri = Uri.parse(MoviesContract.BASE_CONTENT_URI + "/" +
                MoviesContract.MOVIE.TABLE_NAME + "/" + movie_id);

        cursor = resolver.query(uri, null, null, null, null);

        title   = (TextView) findViewById(R.id.title);
        release = (TextView) findViewById(R.id.release_date);
        rate    = (TextView) findViewById(R.id.user_rate);
        imageView = (ImageView) findViewById(R.id.imageView1);
        tagline = (TextView) findViewById(R.id.movie_tag_line);
        revenue = (TextView) findViewById(R.id.movie_revenue);
        overvView = (TextView) findViewById(R.id.overview);
        time = (TextView) findViewById(R.id.movie_time_lenght);
        listView = (ListView) findViewById(R.id.list_view_reviews);

        cursor.moveToFirst();
        title.setText(cursor.getString(cursor.getColumnIndex(MoviesContract.MOVIE.MOVIE_NAME)));
        release.setText(cursor.getString(cursor.getColumnIndex(MoviesContract.MOVIE.MOVIE_RELEASE)));
        rate.setText(cursor.getString(cursor.getColumnIndex(MoviesContract.MOVIE.MOVIE_RATE))+"/10");

        Picasso.with(getApplicationContext()).
                load(AndroidFiles.createFile(movie_id)).resize(200,300).
                centerCrop().
                into(imageView);
        String revenueParsing = UtilityF.parseRevenue(
                cursor.getString(cursor.getColumnIndex(MoviesContract.MOVIE.MOVIE_REVENUE)));
        revenue.setText("Revenue: \n"+revenueParsing + " $$");
        tagline.setText(cursor.getString(cursor.getColumnIndex(MoviesContract.MOVIE.MOVIE_TAGLINE)));
        time.setText(cursor.getString(
                cursor.getColumnIndex(MoviesContract.MOVIE.MOVIE_RUNTIME)) + " min");
        overvView.setText(cursor.getString(cursor.getColumnIndex(MoviesContract.MOVIE.MOVIE_OVERVIEW)));

        cursor.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_favourite_movie_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v("VVV", " " + cursor.getCount());
    }
}
