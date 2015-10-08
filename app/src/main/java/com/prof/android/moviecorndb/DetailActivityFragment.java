package com.prof.android.moviecorndb;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.prof.android.moviecorndb.Adapters.reviewsAdapter;
import com.prof.android.moviecorndb.Adapters.trailersAdapter;
import com.prof.android.moviecorndb.Utility.AndroidFiles;
import com.prof.android.moviecorndb.Utility.UtilityF;
import com.prof.android.moviecorndb.Utility.fetchTextFromUrl;
import com.prof.android.moviecorndb.data.MoviesContract;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment implements MainActivityFragment.CallBacks{

    TextView title;
    TextView rate;
    TextView overview;
    TextView date;
    TextView movieLenght;
    TextView movieRevenue;
    TextView movieTagLine;
    ImageView poster;
    Button favourite;
    String myUri = null;

    ListView trailerListView;
    ListView reviewsListView;

    trailersAdapter movieTrailersAdapter;
    reviewsAdapter reviewsAdapterList;

    ContentValues movieValues = new ContentValues();
    ContentValues reviewValues = new ContentValues();
    long movieId = 0;
    ContentResolver resolver;
    final String API_KEY = "3b28bfa808bcbba743a3f81de1b68868";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        resolver = getActivity().getContentResolver();

        trailerListView = (ListView) rootView.findViewById(R.id.trailers_list_view);
        trailerListView.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });


        reviewsListView = (ListView) rootView.findViewById(R.id.list_view_reviews);
        reviewsListView.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });


        title = (TextView) rootView.findViewById(R.id.title);
        rate  = (TextView) rootView.findViewById(R.id.user_rate);
        overview = (TextView) rootView.findViewById(R.id.overview);
        date = (TextView) rootView.findViewById(R.id.release_date);

        movieLenght = (TextView) rootView.findViewById(R.id.movie_time_lenght);
        movieRevenue = (TextView) rootView.findViewById(R.id.movie_revenue);
        movieTagLine = (TextView) rootView.findViewById(R.id.movie_tag_line);

        poster = (ImageView) rootView.findViewById(R.id.imageView1);
        poster.setScaleType(ImageView.ScaleType.MATRIX);

        favourite = (Button) rootView.findViewById(R.id.favourite_button);

        if(getActivity().findViewById(R.id.fragment_details_frame) == null){
            Intent intent = getActivity().getIntent();
            movieId = intent.getLongExtra(Intent.EXTRA_TEXT, 1);

            movieTrailersTask trailersTask = new movieTrailersTask();
            trailersTask.execute(movieId);

            movieDetails fetch = new movieDetails();
            fetch.execute(movieId);

            reviewsFromUsers usersReview = new reviewsFromUsers();
            usersReview.execute(movieId);
        }

        favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"Added",Toast.LENGTH_SHORT).show();

                Uri reviewUri = Uri.parse(MoviesContract.BASE_CONTENT_URI+"/"+
                        MoviesContract.MOVIE_REVIEWS.TABLE_NAME);
                Uri movieUri = Uri.parse(MoviesContract.BASE_CONTENT_URI+"/"+
                        MoviesContract.MOVIE.TABLE_NAME);
                movieUri  = resolver.insert(movieUri,movieValues);

                Log.v("movieUri:", movieUri + "");
                
                ImageView tmpImage = new ImageView(getActivity());

                Picasso.with(getActivity()).
                        load(myUri).
                        into(tmpImage);
                AndroidFiles.loadImagesToDirectory("Favourite_Movies", tmpImage, movieId);
            }
        });
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void idSelected(long id) {

    }

    @Override
    public void getID(long id) {

        movieTrailersTask trailersTask = new movieTrailersTask();
        trailersTask.execute(id);

        movieDetails fetch = new movieDetails();
        fetch.execute(id);

        reviewsFromUsers usersReview = new reviewsFromUsers();
        usersReview.execute(id);
    }

    public class movieDetails extends AsyncTask<Long, Void, Void>{

        String TITLE    = null;
        double RATE     = 0.0;
        String RELEASE  = null;
        String OVERVIEW = null;
        String RUNTIME  = null;
        String REVENUE  = null;
        String HOMEPAGE = null;
        String TAGLINE  = null;
        String POSTER   = null;
        long MOVIE_ID   = 0;

        @Override
        protected Void doInBackground(Long... params) {

            final String SCHEMA = "http";
            final String AUTHORITY = "api.themoviedb.org";
            final String PATH = "3/movie";
            final String ID = ""+params[0];



            Uri.Builder uri = new Uri.Builder();
            uri.scheme(SCHEMA).authority(AUTHORITY).path(PATH).
                    appendPath(ID).
                    appendQueryParameter("api_key", API_KEY);

            String myUri = uri.toString();
            String jsonText = fetchTextFromUrl.fetchData(myUri);

            try{
                fetchMovieData(jsonText);
            }catch (JSONException e){
                e.printStackTrace();
            }
            return null;
        }

        public void fetchMovieData(String jsonString) throws JSONException{
            JSONObject JSON   = new JSONObject(jsonString);

            TITLE    = JSON.getString("title");
            RATE     = JSON.getDouble("vote_average");
            RELEASE  = JSON.getString("release_date");
            OVERVIEW = JSON.getString("overview");
            RUNTIME  = JSON.getString("runtime");
            REVENUE  = JSON.getString("revenue");
            HOMEPAGE = JSON.getString("homepage");
            TAGLINE  = JSON.getString("tagline");
            POSTER   = JSON.getString("poster_path");
            MOVIE_ID = JSON.getLong("id");

            movieValues.put(MoviesContract.MOVIE.MOVIE_NAME, TITLE);
            movieValues.put(MoviesContract.MOVIE.MOVIE_RELEASE, RELEASE);
            movieValues.put(MoviesContract.MOVIE.MOVIE_RATE, RATE);
            movieValues.put(MoviesContract.MOVIE.MOVIE_OVERVIEW, OVERVIEW);
            movieValues.put(MoviesContract.MOVIE.MOVIE_ID, MOVIE_ID);
            movieValues.put(MoviesContract.MOVIE.MOVIE_TAGLINE, TAGLINE);
            movieValues.put(MoviesContract.MOVIE.MOVIE_RUNTIME, RUNTIME);
            movieValues.put(MoviesContract.MOVIE.MOVIE_REVENUE, REVENUE);
            movieValues.put(MoviesContract.MOVIE.MOVIE_POSTER, POSTER);


        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if(REVENUE.equals("0"))
                movieRevenue.setText("");
            else
                movieRevenue.setText(" " + UtilityF.parseRevenue(REVENUE) + "  $$ ");

            movieLenght.setText(" " + RUNTIME + " min ");
            movieTagLine.setText(TAGLINE);
            title.setText(TITLE);
            date.setText(RELEASE);
            overview.setText(OVERVIEW);
            rate.setText(RATE+"/10");

            final String SCHEMA      = "http";
            final String AUTHORITY   = "image.tmdb.org";
            final String PATH        = "t/p/w342";
            String APPEND_PATH = POSTER;


            Uri.Builder uri = new Uri.Builder();
            uri.scheme(SCHEMA).authority(AUTHORITY).path(PATH);

            myUri = uri.toString()+APPEND_PATH;
            Log.v("uri", " :  " + myUri);

            Picasso.with(getActivity()).
                    load(myUri).resize(200,300).centerInside().
                    into(poster);

        }
    }

    public class movieTrailersTask extends AsyncTask<Long, Void, Void>{

        String[] trailersName = null;
        String[] trailersUrls = null;
        long ID;
        int trailersCount = 0;

        @Override
        protected Void doInBackground(Long... params) {

            final String SCHEMA = "http";
            final String AUTHORITY = "api.themoviedb.org";
            final String PATH = "3/movie";
            final String ID = ""+params[0];


            Uri.Builder uri = new Uri.Builder();
            uri.scheme(SCHEMA).authority(AUTHORITY).
                    path(PATH).
                    appendPath(ID).
                    appendPath("videos").
                    appendQueryParameter("api_key", API_KEY);

            String myUri = uri.toString();
            Log.v("URII",myUri);
            String jsonText = fetchTextFromUrl.fetchData(myUri);

            try {
                fetchMovieData(jsonText);
            }catch (JSONException e){
                e.printStackTrace();
            }
            return null;
        }

        public void fetchMovieData(String jsonString) throws JSONException{

            JSONObject JSON   = new JSONObject(jsonString);
            ID = JSON.getLong("id");
            Log.v("movieiddd:"," "+ID);
            JSONArray jsonArray = JSON.getJSONArray("results");

            trailersCount = jsonArray.length();
            trailersUrls = new String[trailersCount];
            trailersName = new String[trailersCount];

            for (int i=0; i<trailersCount; i++){

                trailersUrls[i] = jsonArray.getJSONObject(i).getString("key");
                trailersName[i] = jsonArray.getJSONObject(i).getString("name");

            }

        }

        @Override
        protected void onPostExecute(Void aVoid) {

            movieTrailersAdapter = new trailersAdapter(getActivity(), trailersName, trailersCount);
            trailerListView.setAdapter(movieTrailersAdapter);

            trailerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    final String SCHEMA = "http";
                    final String AUTHORITY = "www.youtube.com";
                    final String PATH = "watch";
                    final String KEY  = "v";

                    Uri.Builder uri = new Uri.Builder();
                    uri.scheme(SCHEMA).authority(AUTHORITY).
                            path(PATH).
                            appendQueryParameter(KEY, trailersUrls[position]);
                    String myUri = uri.toString();

                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(myUri)));
                }
            });
            super.onPostExecute(aVoid);
        }
    }

    public class reviewsFromUsers extends AsyncTask<Long, Void, Void>{

        String[] authorsName = null;
        String[] reviewContent = null;
        String[] urls = null;
        long movieID;
        int reviewsCount = 0;

        @Override
        protected Void doInBackground(Long... params) {

            final String SCHEMA = "http";
            final String AUTHORITY = "api.themoviedb.org";
            final String PATH = "3/movie";
            final String ID = ""+params[0];


            Uri.Builder uri = new Uri.Builder();
            uri.scheme(SCHEMA).authority(AUTHORITY).
                    path(PATH).
                    appendPath(ID).
                    appendPath("reviews").
                    appendQueryParameter("api_key", API_KEY);

            String myUri = uri.toString();
            Log.v("URII", myUri);
            String jsonText = fetchTextFromUrl.fetchData(myUri);

            try {
                fetchMovieData(jsonText);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        public void fetchMovieData(String jsonString) throws JSONException{
            JSONObject JSON   = new JSONObject(jsonString);
            movieID = JSON.getLong("id");
            JSONArray jsonArray = JSON.getJSONArray("results");

            reviewsCount = jsonArray.length();
            authorsName = new String[reviewsCount];
            reviewContent = new String[reviewsCount];
            urls = new String[reviewsCount];

            for(int i=0; i<reviewsCount; i++){

                authorsName[i]   = jsonArray.getJSONObject(i).getString("author");
                reviewContent[i] = jsonArray.getJSONObject(i).getString("content");
                Log.d("AAAAAAA",authorsName[i]);
                urls[i]          = jsonArray.getJSONObject(i).getString("url");

                reviewValues.put(MoviesContract.MOVIE_REVIEWS.MOVIE_ID_R,movieID);
                reviewValues.put(MoviesContract.MOVIE_REVIEWS.REVIEW_USER,authorsName[i]);
                reviewValues.put(MoviesContract.MOVIE_REVIEWS.REVIEW_CONTENT,reviewContent[i]);
            };
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            reviewsAdapterList = new reviewsAdapter(getActivity(), authorsName,
                    reviewContent, reviewsCount);
            reviewsListView.setAdapter(reviewsAdapterList);

            reviewsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urls[position])));
                }
            });
            super.onPostExecute(aVoid);
        }
    }

    void updateUI (long id){

        movieTrailersTask trailersTask = new movieTrailersTask();
        trailersTask.execute(id);

        movieDetails fetch = new movieDetails();
        fetch.execute(id);

        reviewsFromUsers usersReview = new reviewsFromUsers();
        usersReview.execute(id);
    }

}
