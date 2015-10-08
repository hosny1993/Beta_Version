package com.prof.android.moviecorndb.Utility;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by prof on 10/2/15.
 */
public class fetchTextFromUrl {

    public static String fetchData(String urlString){

        final String LOG_TAG = fetchTextFromUrl.class.getSimpleName();

        HttpURLConnection urlConnection = null;
        InputStream inputStream         ;
        BufferedReader bufferedReader   = null;
        String jsonText                = null;

        URL url = null;
        try{

            try{
                url = new URL(urlString);
            }catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
            }catch (IOException e){
                Log.v("EXCCC", " "+e.getMessage());
                e.printStackTrace();
            }

            inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();

            if(inputStream == null){
                return null;
            }

            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;

            //read line by line and add to buffer because it's imutable
            while((line = bufferedReader.readLine()) != null){
                buffer.append(line+"\n");
            }

            if (buffer.length() == 0){
                return null;
            }

            jsonText = buffer.toString();

        }catch (Exception e){
            Log.e(LOG_TAG,"Network Exception ",e);
            return null;

        }finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        return jsonText;
    }
}
