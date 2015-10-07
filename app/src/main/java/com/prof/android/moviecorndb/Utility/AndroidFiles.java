package com.prof.android.moviecorndb.Utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by prof on 10/1/15.
 */
public class AndroidFiles {

    private static Bitmap bm = null;
    private static OutputStream fOut = null;
    public static Uri outputFileUri;
    private static Context mContext;
    private static boolean dirResult = false;
    private static boolean compressResult = false;

    public AndroidFiles(Context context){
        this.mContext = context;
    }

    public static boolean loadImagesToDirectory( final String dir, final ImageView image, final long imageId){

        try {
            BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
            bm = drawable.getBitmap();
            bm = Bitmap.createBitmap(bm);
            File root = new File(
                    Environment.getExternalStorageDirectory()+
                            File.separator+
                            dir+
                            File.separator
            );
            if(root.exists());
            else
                dirResult = root.mkdir();
            File sdImageMainDirectory = new File(root, imageId+".png");
            if(sdImageMainDirectory.exists());
            else {
                outputFileUri = Uri.fromFile(sdImageMainDirectory);
                Log.d("File", "File Uri :  " + outputFileUri);
                fOut = new FileOutputStream(sdImageMainDirectory);
                compressResult = bm.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.flush();
                fOut.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        if(dirResult == true && compressResult == true)
            return true;
        else
            return false;
    }

    public static boolean deleteDirectory(File path) {
        if( path.exists() ) {
            File[] files = path.listFiles();
            if (files == null) {
                return true;
            }
            for(int i=0; i<files.length; i++) {
                if(files[i].isDirectory()) {
                    deleteDirectory(files[i]);
                }
                else {
                    files[i].delete();
                }
            }
        }
        return( path.delete() );
    }

    public static File createFile(long id){

        String file = id + ".png";

        File root = new File(
                Environment.getExternalStorageDirectory() +
                        File.separator +
                        "Favourite_Movies" +
                        File.separator+
                        file
        );
        return root;
    }

    public static void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }
}
