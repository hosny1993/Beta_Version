package com.prof.android.moviecorndb.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by prof on 10/3/15.
 */
public class MovieContentProvider extends ContentProvider{

    public final static String LOG_TAG = MovieContentProvider.class.getSimpleName();
    private MovieDBHelper movieDBHelper;

    public final static UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatcher.addURI(
                MoviesContract.AUTHORITY,
                MoviesContract.MOVIE.TABLE_NAME,
                MoviesContract.MOVIE.MOVIE_TYPE
        );
        sUriMatcher.addURI(
                MoviesContract.AUTHORITY,
                MoviesContract.MOVIE.TABLE_NAME+"/#",
                MoviesContract.MOVIE.MOVIE_ITEM_TYPE
        );
        sUriMatcher.addURI(
                MoviesContract.AUTHORITY,
                MoviesContract.MOVIE_REVIEWS.TABLE_NAME,
                MoviesContract.MOVIE_REVIEWS.MOVIE_REVIEWS_TYPE
        );
        sUriMatcher.addURI(
                MoviesContract.AUTHORITY,
                MoviesContract.MOVIE_REVIEWS.TABLE_NAME+"/#",
                MoviesContract.MOVIE_REVIEWS.MOVIE_REVIEWSS_ITEM_TYPE
        );
    }

    @Override
    public boolean onCreate() {
        movieDBHelper = new MovieDBHelper(getContext());
        Log.d(LOG_TAG, "Created");
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
//        queryBuilder.setTables(
//                MoviesContract.MOVIE.TABLE_NAME + " INNER JOIN " +
//                        MoviesContract.MOVIE_REVIEWS.TABLE_NAME +
//                        " ON " + MoviesContract.MOVIE.TABLE_NAME +
//                        "." + MoviesContract.MOVIE.MOVIE_ID +
//                        " = " + MoviesContract.MOVIE_REVIEWS.TABLE_NAME +
//                        "." + MoviesContract.MOVIE_REVIEWS.MOVIE_ID_R
//        );
        String orderBy = (TextUtils.isEmpty(sortOrder) ?
                MoviesContract.MOVIE.SORT_ORDER
                :sortOrder);
        SQLiteDatabase database = movieDBHelper.getReadableDatabase();
        Cursor cursor = null;
        switch (sUriMatcher.match(uri)){
            case MoviesContract.MOVIE.MOVIE_TYPE:
                queryBuilder.setTables(
                        MoviesContract.MOVIE.TABLE_NAME
                );
                cursor = queryBuilder.query(database,projection,selection,
                        selectionArgs,null,null,orderBy);
                break;
            case MoviesContract.MOVIE.MOVIE_ITEM_TYPE:
                queryBuilder.setTables(
                        MoviesContract.MOVIE.TABLE_NAME
                );
                queryBuilder.appendWhere(MoviesContract.MOVIE.MOVIE_ID +
                        "=" + uri.getLastPathSegment());
                cursor = queryBuilder.query(database,projection,selection,
                        selectionArgs,null,null,orderBy);
                break;
            case MoviesContract.MOVIE_REVIEWS.MOVIE_REVIEWS_TYPE:
                queryBuilder.setTables(
                        MoviesContract.MOVIE_REVIEWS.TABLE_NAME
                );
                cursor = queryBuilder.query(database,projection,selection,
                        selectionArgs,null,null,null);
                break;
            case MoviesContract.MOVIE_REVIEWS.MOVIE_REVIEWSS_ITEM_TYPE:
                queryBuilder.setTables(
                        MoviesContract.MOVIE_REVIEWS.TABLE_NAME
                );
                queryBuilder.appendWhere(MoviesContract.MOVIE_REVIEWS.MOVIE_ID_R +
                        "=" + uri.getLastPathSegment());
                cursor = queryBuilder.query(database,projection,selection,
                        selectionArgs,null,null,null);
                break;
            default:
                throw new IllegalArgumentException("UNKWON URI "+uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        Log.d(LOG_TAG, "queried records: " + cursor.getCount());

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)){
            case MoviesContract.MOVIE.MOVIE_TYPE:
                Log.v(LOG_TAG, MoviesContract.MOVIE.CONTENT_TYPE);
                return MoviesContract.MOVIE.CONTENT_TYPE;

            case MoviesContract.MOVIE.MOVIE_ITEM_TYPE:
                Log.v(LOG_TAG, MoviesContract.MOVIE.CONTENT_ITEM_TYPE);
                return MoviesContract.MOVIE.CONTENT_ITEM_TYPE;

            case MoviesContract.MOVIE_REVIEWS.MOVIE_REVIEWS_TYPE:
                Log.v(LOG_TAG, MoviesContract.MOVIE_REVIEWS.CONTENT_TYPE);
                return MoviesContract.MOVIE_REVIEWS.CONTENT_TYPE;

            case MoviesContract.MOVIE_REVIEWS.MOVIE_REVIEWSS_ITEM_TYPE:
                Log.v(LOG_TAG, MoviesContract.MOVIE_REVIEWS.CONTENT_ITEM_TYPE);
                return MoviesContract.MOVIE_REVIEWS.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Uknown uri "+uri);
        }

    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri ret = null;

        if (sUriMatcher.match(uri) == MoviesContract.MOVIE.MOVIE_ITEM_TYPE)
            throw new IllegalArgumentException("Illegal Uri "+uri);
        else if (sUriMatcher.match(uri) == MoviesContract.MOVIE_REVIEWS.MOVIE_REVIEWSS_ITEM_TYPE)
            throw new IllegalArgumentException("Illegal Uri "+uri);

        SQLiteDatabase sqLiteDatabase = movieDBHelper.getWritableDatabase();
        long rowID = 0;

        switch (sUriMatcher.match(uri)){

            case MoviesContract.MOVIE.MOVIE_TYPE:
                rowID = sqLiteDatabase.insertWithOnConflict(MoviesContract.MOVIE.TABLE_NAME,
                        null, values, SQLiteDatabase.CONFLICT_IGNORE);

                if(rowID != -1 ){
                    int id = values.getAsInteger(MoviesContract.MOVIE.MOVIE_ID);
                    ret = ContentUris.withAppendedId(uri, id);
                    Log.d(LOG_TAG,"ret uri "+ret);
                    getContext().getContentResolver().notifyChange(uri,null);
                }
                break;

            case MoviesContract.MOVIE_REVIEWS.MOVIE_REVIEWS_TYPE:
                rowID = sqLiteDatabase.insertWithOnConflict(MoviesContract.MOVIE_REVIEWS.TABLE_NAME,
                        null, values, SQLiteDatabase.CONFLICT_IGNORE);
                if(rowID != -1 ){
                    long id = values.getAsLong(MoviesContract.MOVIE_REVIEWS.MOVIE_ID_R);
                    ret = ContentUris.withAppendedId(uri, id);
                    Log.d(LOG_TAG,"ret uri "+ret);
                    getContext().getContentResolver().notifyChange(uri,null);
                }
                break;
        }

        return ret;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        String where;
        long id = 0;
        SQLiteDatabase database = movieDBHelper.getWritableDatabase();
        int ret = 0;

        switch (sUriMatcher.match(uri)){
            case MoviesContract.MOVIE.MOVIE_TYPE:
                where = (selection == null ? "1" : selection);
                ret = database.delete(
                        MoviesContract.MOVIE.TABLE_NAME,
                        where,selectionArgs
                );
                break;

            case MoviesContract.MOVIE.MOVIE_ITEM_TYPE:
                 id = ContentUris.parseId(uri);
                where= MoviesContract.MOVIE.MOVIE_ID+
                        "=" +
                        id + (TextUtils.isEmpty(selection) ? "" : "and ( " + selection +" )");
                ret = database.delete(
                        MoviesContract.MOVIE.TABLE_NAME,
                        where,selectionArgs
                );
                break;

            case MoviesContract.MOVIE_REVIEWS.MOVIE_REVIEWS_TYPE:
                where = (selection == null ? "1" : selection);
                ret = database.delete(
                        MoviesContract.MOVIE_REVIEWS.TABLE_NAME,
                        where,selectionArgs
                );
                break;

            case MoviesContract.MOVIE_REVIEWS.MOVIE_REVIEWSS_ITEM_TYPE:
                id = ContentUris.parseId(uri);
                where= MoviesContract.MOVIE_REVIEWS.MOVIE_ID_R+
                        "=" +
                        id + (TextUtils.isEmpty(selection) ? "" : "and ( " + selection +" )");
                ret = database.delete(
                        MoviesContract.MOVIE_REVIEWS.TABLE_NAME,
                        where,selectionArgs
                );
                break;
            default:
                throw new IllegalArgumentException("UNKWON URI "+uri);
        }

        if (ret > 0)
            getContext().getContentResolver().notifyChange(uri,null);

        Log.d(LOG_TAG,"ret:  "+ret);
        return ret;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        String where;
        long id = 0;
        SQLiteDatabase database = movieDBHelper.getWritableDatabase();
        int ret = 0;

        switch (sUriMatcher.match(uri)){
            case MoviesContract.MOVIE.MOVIE_TYPE:
                where = (selection == null ? "1" : selection);
                ret = database.update(
                        MoviesContract.MOVIE.TABLE_NAME,
                        values,
                        where,
                        selectionArgs
                );
                break;

            case MoviesContract.MOVIE.MOVIE_ITEM_TYPE:
                id = ContentUris.parseId(uri);
                where= MoviesContract.MOVIE.MOVIE_ID+
                        "=" +
                        id + (TextUtils.isEmpty(selection) ? "" : "and ( " + selection +" )");
                ret = database.update(
                        MoviesContract.MOVIE.TABLE_NAME,
                        values,
                        where,
                        selectionArgs
                );
                break;

            case MoviesContract.MOVIE_REVIEWS.MOVIE_REVIEWS_TYPE:
                where = (selection == null ? "1" : selection);
                ret = database.update(
                        MoviesContract.MOVIE_REVIEWS.TABLE_NAME,
                        values,
                        where,
                        selectionArgs
                );
                break;

            case MoviesContract.MOVIE_REVIEWS.MOVIE_REVIEWSS_ITEM_TYPE:
                id = ContentUris.parseId(uri);
                where= MoviesContract.MOVIE_REVIEWS.MOVIE_ID_R+
                        "=" +
                        id + (TextUtils.isEmpty(selection) ? "" : "and ( " + selection +" )");
                ret = database.update(
                        MoviesContract.MOVIE_REVIEWS.TABLE_NAME,
                        values,
                        where,
                        selectionArgs
                );
                break;
            default:
                throw new IllegalArgumentException("UNKWON URI "+uri);
        }

        if (ret > 0)
            getContext().getContentResolver().notifyChange(uri,null);

        Log.d(LOG_TAG,"ret:  "+ret);
        return ret;
    }
}
