package com.tur.job1.job_seeker;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.appcompat.app.AppCompatActivity;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.tur.job1.R;
import com.tur.job1.models.UploadFileResponse;
import com.tur.job1.others.FileUploadService;
import com.tur.job1.others.ServiceGenerator;




import java.io.File;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Job_Seeker_CV_Upload_2 extends AppCompatActivity {

    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.6F);
    private String TAG = "Job_Seeker_CV_Upload_2";


    private Button uploadButton;

    private LinearLayout cvChooser;
    private EditText filePath;

    private static final int ACTIVITY_CHOOSE_FILE = 3;
    private String masterFilePath = "";

    Intent chooseFile;
    Intent intent;

    private Dialog dialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_seeker_cv_upload);

        cvChooser = (LinearLayout)findViewById(R.id.cv_select_panel);
        filePath = (EditText)findViewById(R.id.file_path);
        uploadButton = (Button)findViewById(R.id.upload_button);

        //--


        chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.setType("file/*");
        intent = Intent.createChooser(chooseFile, "Choose a file");

        //--


        //SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        //Log.d(TAG,prefs.getString("userid", "null"));


        //------------
        cvChooser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                cvChooser.startAnimation(buttonClick);

                if (ContextCompat.checkSelfPermission(Job_Seeker_CV_Upload_2.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Permission is not granted

                    askForPermission();


                }else{

                    startActivityForResult(intent, ACTIVITY_CHOOSE_FILE);

                }









            }
        });






        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                uploadButton.startAnimation(buttonClick);

                if(masterFilePath.equalsIgnoreCase("")){

                    Toasty.error(Job_Seeker_CV_Upload_2.this, "Select a file first!", Toast.LENGTH_LONG, true).show();

                }else {

                    showLoadingBarAlert();
                    uploadImageWithId(masterFilePath,filePath.getText().toString());

                }

                //Toasty.success(Job_Seeker_CV_Upload_2.this,"Your CV uploaded successfully!",Toast.LENGTH_LONG, true).show();
            }
        });




    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;
        String path     = "";
        if(requestCode == ACTIVITY_CHOOSE_FILE)
        {
            Uri uri = data.getData();
            String FilePath = getRealPathFromURI(uri);

            if(fileExtentionCheck(FilePath)){
                //filePath.setText(fileNameSeperator(FilePath));
                if(fileSizeFinder(FilePath)){

                    filePath.setText(fileNameSeperator(FilePath));
                    masterFilePath = FilePath;

                }else {

                    filePath.setText(fileNameSeperator(""));
                    Toasty.error(Job_Seeker_CV_Upload_2.this, "File is too large! Maximum file size limit 5MB", Toast.LENGTH_LONG, true).show();
                }
            }else {

                Toasty.error(Job_Seeker_CV_Upload_2.this, "File type not supported!\n Supported formats : pdf,docx,jpg,png", Toast.LENGTH_LONG, true).show();
                filePath.setText("");
            }



        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        String [] proj      = {MediaStore.Images.Media.DATA};
        Cursor cursor       = getContentResolver().query( contentUri, proj, null, null,null);
        if (cursor == null) return null;
        int column_index    = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public boolean fileExtentionCheck(String st){

        Boolean tempFlag = false;

        if(st.endsWith(".pdf") || st.endsWith(".docx") || st.endsWith(".jpg") || st.endsWith(".jpeg") || st.endsWith(".png")
                || st.endsWith(".PDF") || st.endsWith(".DOCX") || st.endsWith(".JPG") || st.endsWith(".JPEG") || st.endsWith(".PNG")){

            tempFlag = true;
        }else{

            tempFlag = false;
        }

        return tempFlag;
    }

    public String fileNameSeperator(String st){

        String tempS = "";


        for(int i=st.length()-1; i >= 0; i--){

            if(st.charAt(i) == '/'){
                break;
            }else {
                tempS = tempS + st.charAt(i);
            }
        }

        if(tempS != null || !tempS.equalsIgnoreCase("")){

            String tempS2 = "";
            for(int i=tempS.length()-1; i>=0; i--){


                tempS2 = tempS2 + tempS.charAt(i);

            }

            tempS = tempS2;
        }



        return tempS;

    }

    public boolean fileSizeFinder(String pth){

        boolean tempFlag = false;
        File file = new File(pth);
        int file_size = Integer.parseInt(String.valueOf(file.length()/1024));
        if(file_size <= 5120){

            tempFlag = true;
        }else {
            tempFlag = false;
        }

        return tempFlag;
    }

    /*
    public void uploadMultipart(final Context context) {
        try {
            String uploadId =
                    new MultipartUploadRequest(context, "http://upload.server.com/path")
                            // starting from 3.1+, you can also use content:// URI string instead of absolute file
                            .addFileToUpload("/absolute/path/to/your/file", "your-param-name")
                            .setNotificationConfig(new UploadNotificationConfig())
                            .setMaxRetries(2)
                            .startUpload();
        } catch (Exception exc) {
            Log.e(TAG, exc.getMessage(), exc);
        }
    }
    */

    //--

    private void uploadImageWithId(String filePath, String shortFilePath) {
        // create upload service client
        FileUploadService service =
                ServiceGenerator.createService(FileUploadService.class);

        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        // use the FileUtils to get the actual file by uri
        //File file = FileUtils.getFile(this, fileUri);
        File file = new File(filePath);;//FileUtils.getFile(this, fileUri);
        Uri myUri = Uri.parse(filePath);



        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse(shortFilePath),
                        file
                );

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), requestFile);



        SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        String userID = prefs.getString("userid", "null");
        if(userID.equalsIgnoreCase(null)){

            //-- Go to sign up screen


        }else {
            //--
            Call<UploadFileResponse> call = service.uploadImageWithId(body,Integer.parseInt(userID),"CV");
            call.enqueue(new Callback<UploadFileResponse>() {
                @Override
                public void onResponse(Call<UploadFileResponse> call,
                                       Response<UploadFileResponse> response) {
                    //Log.v("112233", response.body().getFileName()+"-------- "+response.body().getFileDownloadUri());
                    //Toasty.success(Job_Seeker_CV_Upload_2.this,response.body().toString(),Toast.LENGTH_LONG, true).show();
                    Log.d(TAG,response.body().getFileDownloadUri());


                    if(response.body().getStatus() == 200){

                        //--success
                        Toasty.success(Job_Seeker_CV_Upload_2.this,"CV uploaded successfully!",Toast.LENGTH_LONG, true).show();
                        Intent openSecondVerifier = new Intent(Job_Seeker_CV_Upload_2.this,Job_Seeker_Dashboard.class);
                        startActivity(openSecondVerifier);
                        finish();

                    }else{

                        Toasty.error(Job_Seeker_CV_Upload_2.this,"User not created yet!",Toast.LENGTH_LONG, true).show();
                    }


                    hideLoadingBar();
                }

                @Override
                public void onFailure(Call<UploadFileResponse> call, Throwable t) {
                    Log.e(TAG, t.getMessage());
                    hideLoadingBar();
                }
            });


            //-------------------
        }


    }


    //---------------------------

    //---
    private void askForPermission(){

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override public void onPermissionGranted(PermissionGrantedResponse response) {

                        startActivityForResult(intent, ACTIVITY_CHOOSE_FILE);
                    }
                    @Override public void onPermissionDenied(PermissionDeniedResponse response) {

                        //askForPermission();
                    }
                    @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                        //askForPermission();
                        //Log.d(TAG,"----------------------- Read permission is not granted!");
                    }
                }).check();
    }

    private void showLoadingBarAlert(){


        dialog = new Dialog(Job_Seeker_CV_Upload_2.this);

        dialog.setContentView(R.layout.loading);

        dialog.setTitle("Please wait!");

        dialog.setCancelable(false);



        DisplayMetrics displayMetrics = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        dialog.show();

    }



    private void hideLoadingBar(){



        dialog.dismiss();

    }

    @Override
    public void onBackPressed() {

        Intent openSecondVerifier = new Intent(Job_Seeker_CV_Upload_2.this,Job_Seeker_Dashboard.class);
        startActivity(openSecondVerifier);
        finish();
    }

    //-------------------
}


