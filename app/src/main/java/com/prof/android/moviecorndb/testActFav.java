//package com.prof.android.moviecorndb;
//
//import android.content.ContentResolver;
//import android.content.Intent;
//import android.database.Cursor;
//import android.net.Uri;
//import android.os.Bundle;
//import android.app.Fragment;
//import android.support.v7.app.ActionBarActivity;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.GridView;
//import android.widget.Toast;
//
//import com.prof.android.moviecorndb.Adapters.FavouriteAdapter;
//import com.prof.android.moviecorndb.data.MoviesContract;
//
///**
// * Created by prof on 10/7/15.
// */
//public class testActFav extends Fragment {
//
//    FavouriteAdapter myFavouriteAdapter;
//    GridView gridView;
//    ContentResolver resolver;
//    Cursor cursor;
//
//    Uri uri = Uri.parse(MoviesContract.BASE_CONTENT_URI + "/" +
//            MoviesContract.MOVIE.TABLE_NAME);
//
//    public testActFav() {
////        getFragmentManager().beginTransaction().
////                replace(R.id.fragment_movies, new favouriteFragment()).commit();
//        // Required empty public constructor
//    }
//
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        //inflater.inflate(R.menu.menu_favourite, menu);
//    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        return true;
//    }
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setHasOptionsMenu(true);
//
//        resolver = getActivity().getContentResolver();
//        cursor = resolver.query(uri, null, null, null, null);
//
//        long[] ids = new long[cursor.getCount()];
//
//        while (cursor.moveToNext()){
//            ids[cursor.getPosition()] = cursor.getLong(
//                    cursor.getColumnIndex(MoviesContract.MOVIE.MOVIE_ID));
//        }
//
//        myFavouriteAdapter = new FavouriteAdapter(getActivity(), cursor.getCount(), ids);
//
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View rootView =  inflater.inflate(R.layout.fragment_favourite, container, false);
//        gridView = (GridView) rootView.findViewById(R.id.list_item_favourite);
//
//        gridView.setAdapter(myFavouriteAdapter);
//
//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getActivity(), "id: " + (long) view.getTag(),
//                        Toast.LENGTH_SHORT).show();
//                long movie_id = (long) view.getTag();
//                Intent intent = new Intent(getActivity(), favourite_movie_detail.class);
//                intent.putExtra("movie_id", movie_id);
//                startActivity(intent);
//            }
//        });
//        return rootView;
//    }
//}
