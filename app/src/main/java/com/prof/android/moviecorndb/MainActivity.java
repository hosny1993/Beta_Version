package com.prof.android.moviecorndb;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.prof.android.moviecorndb.Utility.AndroidFiles;

import java.io.File;

public class MainActivity extends ActionBarActivity implements MainActivityFragment.CallBacks{

    protected int getResId(){
        return R.layout.activity_master_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getResId());

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_movies, new MainActivityFragment(), "TTGG")
                .commit();

        if(findViewById(R.id.fragment_details_frame) == null){

        }else {
            DetailActivityFragment myFragment = new DetailActivityFragment();

            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_details_frame,myFragment, "TTTGG")
                    .commit();
            Log.v("HHHTTTGGG", " THIS HAPPENS");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings){

        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            File root = new File(
                    Environment.getExternalStorageDirectory() +
                            File.separator +
                            "Picasso" +
                            File.separator
            );
            AndroidFiles files = new AndroidFiles(getBaseContext());
            boolean check = files.deleteDirectory(root);

        }catch (Exception e){

        }
    }

    @Override
    public void idSelected(long id) {

        if(findViewById(R.id.fragment_details_frame) == null) {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(Intent.EXTRA_TEXT, id);
            startActivity(intent);
        }else{
            ((DetailActivityFragment) getFragmentManager().
                    findFragmentByTag("TTTGG")).updateUI(id);
            Log.v("MMMID", " " + id);
        }
    }

    @Override
    public void getID(long id) {
        if(findViewById(R.id.fragment_details_frame )!= null)
        ((DetailActivityFragment) getFragmentManager().
                findFragmentByTag("TTTGG")).updateUI(id);
    }

    public void replace(android.app.Fragment fragment){
            getFragmentManager().beginTransaction().replace(R.id.fragment_movies,fragment).commit();
    }
}
