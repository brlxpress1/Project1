package com.tur.job1.others;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SaveImage {

    public String TAG = "SaveImage";

    public File saveBitMap(Context context, Activity act, Bitmap Final_bitmap,String imageName) {
        File pictureFileDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Project1");
        if (!pictureFileDir.exists()) {
            boolean isDirectoryCreated = pictureFileDir.mkdirs();
            if (!isDirectoryCreated)
                Log.i(TAG, "Can't create directory to save the image");
            return null;
        }
        String filename = pictureFileDir.getPath() + File.separator + imageName+".jpg";//pictureFileDir.getPath() + File.separator + System.currentTimeMillis() + ".jpg";
        File pictureFile = new File(filename);
        try {
            pictureFile.createNewFile();
            FileOutputStream oStream = new FileOutputStream(pictureFile);
            Final_bitmap.compress(Bitmap.CompressFormat.JPEG, 60, oStream);
            oStream.flush();
            oStream.close();
            //Toast.makeText(act, "Save Image Successfully..", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, "There was an issue saving the image.");
        }
        scanGallery(context,act, pictureFile.getAbsolutePath());
        return pictureFile;
    }
    private void scanGallery(Context cntx,Activity act, String path) {
        try {
            MediaScannerConnection.scanFile(cntx, new String[]{path}, null, new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String path, Uri uri) {
                    //Toast.makeText(act, "Save Image Successfully..", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("TAG", "There was an issue scanning gallery.");
        }
    }
}
