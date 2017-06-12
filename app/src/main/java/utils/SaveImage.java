package utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by admin on 16/5/17.
 */

public class SaveImage extends AsyncTask<Bitmap, Void, Boolean> {
    Bitmap bmp;
    String path;
    String name;
    Context context;
    public SaveImage(Context context, String path, String name){
        this.context    = context;
        this.path       = path;
        this.name       = name;
    }
    @Override
    protected Boolean doInBackground(Bitmap... params) {
        Boolean saved=false;
        for (Bitmap img : params) {
            saved = saveToInternalStorage(img,path,name);
        }
        return saved;
    }

    private Boolean saveToInternalStorage(Bitmap bitmapImage, String Path, String name){
        Boolean saved=false;
        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir(Path, Context.MODE_PRIVATE);
        File mypath=new File(directory,name);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
                saved=true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ///data/user/0/org.dev4u.hv.mydiagnostic/app_Profile/profile.png
        Log.d("Se Guardo en : ",mypath.getAbsolutePath());
        //Toast.makeText(context,"Saved in : "+mypath.getAbsolutePath(),Toast.LENGTH_SHORT);
        return saved;
    }
}
