package com.prof.android.moviecorndb.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Movie;
import android.util.Log;

import java.sql.SQLException;

/**
 * Created by prof on 10/3/15.
 */
public class MovieDBHelper extends SQLiteOpenHelper{

    public final static String LOG_TAG = MovieDBHelper.class.getSimpleName();

    public MovieDBHelper(Context context) {
        super(context, MoviesContract.DB_NAME, null, MoviesContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String movieSql = String.format(
                "CREATE TABLE %s (%s LONG PRIMARY KEY, " +
                        "%s TEXT NOT NULL, %s REAL NOT NULL, %s TEXT NOT NULL, " +
                        "%s TEXT NOT NULL, %s TEXT NOT NULL, %s INTEGER NOT NULL, %s LONG NOT NULL," +
                        "%s TEXT NOT NULL );",
                MoviesContract.MOVIE.TABLE_NAME,
                MoviesContract.MOVIE.MOVIE_ID,
                MoviesContract.MOVIE.MOVIE_NAME,
                MoviesContract.MOVIE.MOVIE_RATE,
                MoviesContract.MOVIE.MOVIE_RELEASE,
                MoviesContract.MOVIE.MOVIE_OVERVIEW,
                MoviesContract.MOVIE.MOVIE_TAGLINE,
                MoviesContract.MOVIE.MOVIE_RUNTIME,
                MoviesContract.MOVIE.MOVIE_REVENUE,
                MoviesContract.MOVIE.MOVIE_POSTER
        );

        String movieReviewsSql = String.format(
                "CREATE TABLE %s (%s INTEGER PRIMARY KEY, %s TEXT NOT NULL );",
                MoviesContract.MOVIE_REVIEWS.TABLE_NAME,
                MoviesContract.MOVIE_REVIEWS.MOVIE_ID_R,
                MoviesContract.MOVIE_REVIEWS.MOVIE_YOUTUBE

        );

        Log.v(LOG_TAG,"Movie: "+movieSql);
        Log.v(LOG_TAG,"MovieReviews: "+movieReviewsSql);

        try{
            db.execSQL(movieSql);
            db.execSQL(movieReviewsSql);
        }catch (Exception s){
            s.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("drop table if exists "+ MoviesContract.MOVIE.TABLE_NAME);
        db.execSQL("drop table if exists "+ MoviesContract.MOVIE_REVIEWS.TABLE_NAME);

        Log.v(LOG_TAG,"dropped  ");
        onCreate(db);
    }
}
