package com.prof.android.moviecorndb;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.prof.android.moviecorndb.Adapters.customListAdapter;
import com.prof.android.moviecorndb.Utility.fetchTextFromUrl;
import com.prof.android.moviecorndb.data.MoviesContract;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment{

    final String API_KEY    =  "3b28bfa808bcbba743a3f81de1b68868";

    int state = 0;

    private int mPosition = 0;
    private int sPosition = 0;

    GridView mGridView;
    fetchMovieData movieData;
    customListAdapter customAdapter;

    SharedPreferences.Editor editor;
    SharedPreferences prefs;

    Uri uri = Uri.parse(MoviesContract.BASE_CONTENT_URI + "/" +
            MoviesContract.MOVIE.TABLE_NAME);

    private CallBacks mCallBacks;

    public interface CallBacks{
        void idSelected(long id);
        void getID(long id);
    }

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        editor = getActivity().
                getSharedPreferences("AG", Context.MODE_APPEND).edit();
        prefs  = getActivity().getSharedPreferences("AG", Context.MODE_PRIVATE);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.my_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.popular) {
            Log.v("RATED", " POP");
            updatePopulare(0);
            editor.putInt("STATE", 0);
        }
        else if (id == R.id.rated) {
            updateRated(0);
            state = 1;
            editor.putInt("STATE",1);
        }
        else if (id == R.id.theater) {
            updateTheatr(0);
            state = 2;
            editor.putInt("STATE",2);
        }
        else if (id == R.id.favourite_movie){
            updateFavourite(0);
            state = 3;
            editor.putInt("STATE",3);
        }

        editor.commit();
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = null;
        if(getActivity().findViewById(R.id.fragment_details_frame) != null){
            rootView = inflater.inflate(R.layout.tablet_three_posters, container, false);
            mGridView = (GridView) rootView.findViewById(R.id.list_item_tablet);
        }
        else {
            rootView = inflater.inflate(R.layout.fragment_main, container, false);
            mGridView = (GridView) rootView.findViewById(R.id.list_item);
        }

        return rootView;

    }

    @Override
    public void onStart() {

        super.onStart();
        int currentState = 0;

        if (sPosition != 0){
            currentState = sPosition;
        }else{
            currentState = mPosition;
        }

        if (prefs.getInt("STATE", 0) == 0)
            updatePopulare(currentState);
        else if (prefs.getInt("STATE", 0) == 1)
            updateRated(currentState);
        else if (prefs.getInt("STATE", 0) == 2)
            updateTheatr(currentState);
        else if (prefs.getInt("STATE", 0) == 3)
            updateFavourite(currentState);
        else
            updatePopulare(currentState);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {


        if (savedInstanceState != null && savedInstanceState.containsKey("SAVV") ) {
            Log.v("positon", " " + (int) savedInstanceState.get("SAVV"));
            mPosition = (int) savedInstanceState.get("SAVV");
        }
        if (savedInstanceState != null && savedInstanceState.containsKey("SEL")){
            Log.v("selection", " " + (int) savedInstanceState.get("SEL"));
            sPosition = (int) savedInstanceState.get("SEL");
        }

        else if ((getActivity().findViewById(R.id.fragment_details_frame)) == null) {
            try {
                mPosition = prefs.getInt("MMGG", 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        if (outState != null ) {
            outState.putInt("SAVV", mGridView.getFirstVisiblePosition());
            Log.v("SAVV", " " + mGridView.getFirstVisiblePosition());
        }
        if (mGridView.getTag() != null) {
            outState.putInt("SEL", (int) mGridView.getTag());
            Log.v("SEL", " " + (int) mGridView.getTag());
        }

        super.onSaveInstanceState(outState);
        Toast.makeText(getActivity(), "OnSaveInstanceBundle", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallBacks = (CallBacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallBacks = null;
    }

    public void updateRated(int m) {
        movieData = new fetchMovieData(m);
        movieData.execute("vote_average.desc", "US", "R", "", "");
    }

    public void updatePopulare(int m) {
        movieData = new fetchMovieData(m);
        movieData.execute("popularity.desc", "", "", "", "");
    }

    public void updateTheatr(int m) {
        movieData = new fetchMovieData(m);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());
        Log.v("Date ", formattedDate);
        movieData.execute("", "", "", "" + formattedDate, "" + formattedDate);
    }

    public void updateFavourite(int m){

        ContentResolver contentResolver = getActivity().getContentResolver();
        Cursor cursor = contentResolver.query(uri, null, null, null, null);

        final String SCHEMA      = "http";
        final String AUTHORITY   = "image.tmdb.org";
        final String PATH        = "t/p/w342";

        Uri.Builder uri = new Uri.Builder();
        uri.scheme(SCHEMA).authority(AUTHORITY).path(PATH);

        final String[] myIDs = new String[cursor.getCount()];
        final long[] Ids  = new long[cursor.getCount()];

        while (cursor.moveToNext()){

            myIDs[cursor.getPosition()] = cursor.getString(cursor.
                    getColumnIndex(MoviesContract.MOVIE.MOVIE_POSTER));

            Ids[cursor.getPosition()] = cursor.getLong(cursor.getColumnIndex(
                    MoviesContract.MOVIE.MOVIE_ID
            ));

        }
        int state = 0;
        if (getActivity().findViewById(R.id.fragment_details_frame) != null){
            state = 1;
        }
        else {
            state = 0;
        }
        try{
            if (mPosition > cursor.getCount())
                mCallBacks.getID(0);
            else
                mCallBacks.getID(Ids[mPosition]);
        }catch (Exception e){
            e.printStackTrace();
        }

        customAdapter = new customListAdapter(getActivity(), myIDs, state, myIDs.length);
        mGridView.setAdapter(customAdapter);
        mGridView.setClickable(true);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                mCallBacks.idSelected(Ids[position]);
            }

        });
    }

    public class fetchMovieData extends AsyncTask<String,Void,Void> {

        final String LOG_TAG = fetchMovieData.class.getSimpleName();
        long[] movieID;
        String[] posters;
        int selectPos = 0;

        public fetchMovieData(int selectedPosition){
            selectPos = selectedPosition;
            Log.v("selectPos", " "+selectPos);
        }
        @Override
        protected Void doInBackground(String... params) {
            final String LOG_TAG = fetchMovieData.class.getSimpleName();

            final String SCHEMA     = "http";
            final String AUTHORITY  = "api.themoviedb.org";
            final String PATH       = "3/discover/movie";
            final String SORT       = params[0];
            final String COUNTRAY   = params[1];
            final String CERT       = params[2];
            final String PRIM       = params[3];
            final String LATE       = params[4];

            Uri.Builder uri = new Uri.Builder();
            uri.scheme(SCHEMA).authority(AUTHORITY).path(PATH).
                    appendQueryParameter("api_key",API_KEY).
                    appendQueryParameter("sort_by",SORT).
                    appendQueryParameter("certification_country",COUNTRAY).
                    appendQueryParameter("certification",CERT).
                    appendQueryParameter("primary_release_date.gte",PRIM).
                    appendQueryParameter("primary_release_date.lte",LATE);
            String myUri = uri.toString();
            String jsonText = fetchTextFromUrl.fetchData(myUri);
            Log.v("DAAA",myUri);
            try {
                if(jsonText == null)
                    return null;
                fetchMoviesData(jsonText);
            }catch (JSONException e){
                e.printStackTrace();
            }
            return null;
        }

        public Void fetchMoviesData(String jsonString) throws JSONException{

            JSONObject json   = new JSONObject(jsonString);
            JSONArray results = json.getJSONArray("results");

            posters         = new String [results.length()];
            movieID         = new long   [results.length()];

            for(int i=0; i<results.length(); i++){

                posters[i]        = results.getJSONObject(i).getString("poster_path");
                movieID[i]        = results.getJSONObject(i).getLong("id");

                Log.v(" poster:  ",posters[i]);
                Log.v("IdI: ",""+movieID[i]);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            try{
                mCallBacks.getID(movieID[selectPos]);
            }catch (Exception e){
                e.printStackTrace();
            }

            int state = 0;
            try {

                if (getActivity().findViewById(R.id.fragment_details_frame) != null) {
                    state = 1;
                } else {
                    state = 0;
                }
            }catch (Exception e){

            }
            mGridView.setClickable(true);
            try {
                customAdapter = new customListAdapter(getActivity(),
                        posters, state, posters.length);
            }catch (Exception e){
                e.printStackTrace();
            }


            mGridView.setAdapter(customAdapter);
            mGridView.setSelection(selectPos);

            mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {

                    mCallBacks.idSelected(movieID[position]);
                    editor.putInt("MMGG", position);
                    editor.commit();
                    mGridView.setTag(position);
                }

            });
            //mGridView.deferNotifyDataSetChanged();
        }

    }

}