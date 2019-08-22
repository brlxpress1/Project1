package com.tur.job1.company;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.tur.job1.Intro;
import com.tur.job1.R;
import com.tur.job1.job_seeker.Job_Seeker_CV_Upload;
import com.tur.job1.job_seeker.Job_Seeker_Dashboard;
import com.tur.job1.job_seeker.Job_Seeker_Verify_1;
import com.tur.job1.others.Connectivity;
import com.tur.job1.others.ConstantsHolder;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class Employee_Details extends AppCompatActivity {

    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.6F);

    private String TAG = "Employee_Details";



    private CircleImageView profilePhotoDisplay;
    private TextView nameDisplay;

    private TextView emailDisplay;
    private TextView experienceDisplay;
    private TextView companyDisplay;
    private TextView skillsDisplay;
    private TextView expectedSalaryDisplay;
    private Button cvDownloadButtonClick;

    private String localPhotoUrl;
    private String localName;
    private String localEmail;
    private String localExperience;
    private String localSkills;
    private String localExpectedSalary;
    private String localCVUrl;

    private String downloadedFileName;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_details);

        profilePhotoDisplay = (CircleImageView)findViewById(R.id.profile_image);
        nameDisplay = (TextView)findViewById(R.id.nameDisplay);

        emailDisplay = (TextView)findViewById(R.id.emailDisplay);
        experienceDisplay = (TextView)findViewById(R.id.experienceDisplay);
        companyDisplay = (TextView)findViewById(R.id.companyDisplay);
        skillsDisplay = (TextView)findViewById(R.id.skillsDisplay);
        expectedSalaryDisplay = (TextView)findViewById(R.id.expectedSalary);
        cvDownloadButtonClick = (Button)findViewById(R.id.dowload_button);

        SharedPreferences prefs = getSharedPreferences("UserDetailsData", MODE_PRIVATE);
        //userIdLocal = prefs.getString("userid", "");
        /*
       SharedPreferences.Editor editor = getSharedPreferences("UserDetailsData", MODE_PRIVATE).edit();
        editor.putString("userid", String.valueOf(userID));
        editor.putString("photourl", photoUrl);
        editor.putString("username", userName);
        editor.putString("email", email);
        editor.putString("experience", experience);
        editor.putString("expectedsalary", expectedSalary);
        editor.apply();
         */

        localPhotoUrl = prefs.getString("photourl", "");
        localName = prefs.getString("username", "");

        localEmail = prefs.getString("email", "");
        localExperience = prefs.getString("experience", "");
        //localSkills = prefs.getString("photourl", "");
        localExpectedSalary = prefs.getString("expectedsalary", "");
        localCVUrl = prefs.getString("cvurl", "");



        if(localPhotoUrl != null || !localPhotoUrl.equalsIgnoreCase("")){


            Glide.with(this)
                    .load(localPhotoUrl)
                    .centerCrop()
                    .placeholder(R.drawable.default_avatar)
                    .into(profilePhotoDisplay);

        }

        if(localName != null && !localName.equalsIgnoreCase("")){

            nameDisplay.setText(localName);

        }else {

            nameDisplay.setText("N/A");
        }

        if(localEmail != null && !localEmail.equalsIgnoreCase("")){

            emailDisplay.setText(localEmail);

        }else {

            emailDisplay.setText("N/A");
        }


        if(localExperience != null && !localExperience.equalsIgnoreCase("") && !localExperience.equalsIgnoreCase("0")){

            experienceDisplay.setText(localExperience+" years");

        }else {

            experienceDisplay.setText("N/A");
        }

        if(localExpectedSalary != null && !localExpectedSalary.equalsIgnoreCase("") && !localExpectedSalary.equalsIgnoreCase("0")){

            expectedSalaryDisplay.setText(localExpectedSalary+" taka");

        }else {

            expectedSalaryDisplay.setText("N/A");
        }

        cvDownloadButtonClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ContextCompat.checkSelfPermission(Employee_Details.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Permission is not granted

                    askForPermission();


                }else{

                    startCVDownload();

                }


            }
        });






    }

    //---
    private void askForPermission(){

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override public void onPermissionGranted(PermissionGrantedResponse response) {

                        startCVDownload();
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

    /**
     * Async Task to download file from URL
     */
    private class DownloadFile extends AsyncTask<String, String, String> {

        private ProgressDialog progressDialog;
        private String fileName;
        private String folder;
        private boolean isDownloaded;

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.progressDialog = new ProgressDialog(Employee_Details.this);
            this.progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            this.progressDialog.setCancelable(false);
            this.progressDialog.show();
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();
                // getting file length
                int lengthOfFile = connection.getContentLength();


                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                //String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
                String timestamp = new SimpleDateFormat("dd_mm_yyyy").format(new Date());

                //Extract file name from URL
                fileName = f_url[0].substring(f_url[0].lastIndexOf('/') + 1, f_url[0].length());

                String[] separated = fileName.split(".");
                //Append timestamp to file name
                fileName = timestamp+"_"+fileName;//"_"+timestamp;
                Log.d(TAG,fileName);

                //External directory path to save file
                folder = Environment.getExternalStorageDirectory() + File.separator + ConstantsHolder.directoryName+"/";

                //Create androiddeft folder if it does not exist
                File directory = new File(folder);

                if (!directory.exists()) {
                    directory.mkdirs();
                }

                // Output stream to write file
                OutputStream output = new FileOutputStream(folder + fileName);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lengthOfFile));
                    Log.d(TAG, "Progress: " + (int) ((total * 100) / lengthOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();
                downloadedFileName = fileName;
                return "Downloaded at: " + folder + fileName;

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return "Something went wrong";
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            progressDialog.setProgress(Integer.parseInt(progress[0]));
        }


        @Override
        protected void onPostExecute(String message) {
            // dismiss the dialog after the file was downloaded
            this.progressDialog.dismiss();

            // Display File path after downloading
           // Toast.makeText(getApplicationContext(),
                   // message, Toast.LENGTH_LONG).show();

            if(message.contains("Downloaded at: ")){

                showFileDownloadedDialoue(downloadedFileName);
            }else {

                Toast.makeText(getApplicationContext(),
                message, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void startCVDownload(){

        // to do task
        if(localCVUrl.equalsIgnoreCase("") || localCVUrl == null){

            Toasty.error(Employee_Details.this,"Resume not available for download now! Try again later.",Toast.LENGTH_LONG, true).show();

        }else {

            new DownloadFile().execute(localCVUrl);
        }
    }



    //--------------------

    @Override
    public void onBackPressed() {
        finish();
    }

    public void showFileDownloadedDialoue(String msg){

        String a = "File name : "+ msg+"\n\n";
        String b = "Downloaded to : "+ConstantsHolder.directoryName+"\n";
        String c = a+b;


        new MaterialStyledDialog.Builder(this)
                .setIcon(R.drawable.file_icon)
                .setHeaderColor(R.color.successColor)
                .setTitle("Resume Downloaded!")
                .setDescription(c)

                .setCancelable(false)
                .setPositiveText("OK")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {


                    }
                })
                /*
                .setNegativeText("Cancel")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {


                    }
                })
                */
                .show();
    }
}
