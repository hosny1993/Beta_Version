package com.prof.android.moviecorndb.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by prof on 10/3/15.
 */
public class MoviesContract {

    public final static String AUTHORITY = "com.prof.android.moviecorndb.data";
    public final static Uri BASE_CONTENT_URI  = Uri.parse("content://" + AUTHORITY);

    public final static String MOVIE_PATH = "movie";
    public final static String DETAILS_PATH = "movie_details";
    public final static String REVIEW_PATH = "reviews";
    public final static String FAVOURITE_PATH = "favourite";


    public final static String DB_NAME = "moviesCorn.db";
    public final static int DB_VERSION = 8;

    public static class MOVIE  implements BaseColumns{

        public final static Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(MOVIE_PATH).build();

        public final static int MOVIE_TYPE = 1;
        public final static int MOVIE_ITEM_TYPE = 2;

        public final static String CONTENT_TYPE =
                "vnd.android.cursor.dir/"+AUTHORITY+"/"+MOVIE_PATH;
        public final static String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/"+AUTHORITY+"/"+MOVIE_PATH;


        public final static String TABLE_NAME    = "movie";
        public final static String MOVIE_NAME = "name";
        public final static String MOVIE_RELEASE  = "release";
        public final static String MOVIE_RATE  = "rate";
        public final static String MOVIE_OVERVIEW  = "overview";
        public final static String MOVIE_ID  = "movie_id";
        public final static String MOVIE_TAGLINE = "tagline";
        public final static String MOVIE_RUNTIME  = "runtime";
        public final static String MOVIE_REVENUE  = "revenue";
        public final static String MOVIE_POSTER = "poster";
        public final static String SORT_ORDER = MOVIE_RATE + " DESC";

    }


    public static class MOVIE_REVIEWS  implements BaseColumns{

        public final static Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(REVIEW_PATH).build();

        public final static int MOVIE_REVIEWS_TYPE = 3;
        public final static int MOVIE_REVIEWSS_ITEM_TYPE = 4;

        public final static String CONTENT_TYPE =
                "vnd.android.cursor.dir/"+AUTHORITY+"/"+MOVIE_REVIEWS_TYPE;
        public final static String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/"+AUTHORITY+"/"+MOVIE_REVIEWSS_ITEM_TYPE;


        public final static String TABLE_NAME      = "movie_reviews";
        public final static String MOVIE_ID_R  = "key_movie_id";
        public final static String MOVIE_YOUTUBE  = "youtube_key";
    }

}
