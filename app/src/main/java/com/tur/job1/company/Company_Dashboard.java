package com.tur.job1.company;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.tur.job1.Intro;
import com.tur.job1.R;
import com.tur.job1.job_seeker.Job_Seeker_Dashboard;
import com.tur.job1.job_seeker.Job_Seeker_Login;
import com.tur.job1.job_seeker.Job_Seeker_Verify_1;
import com.tur.job1.models.UploadFileResponse;
import com.tur.job1.others.Connectivity;
import com.tur.job1.others.ConstantsHolder;
import com.tur.job1.others.FileUploadService;
import com.tur.job1.others.ImagePickerActivity;
import com.tur.job1.others.SaveImage;
import com.tur.job1.others.ServiceGenerator;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class Company_Dashboard extends AppCompatActivity {

    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.6F);

    private String TAG = "Company_Dashboard";
    private int REQUEST_IMAGE = 100;

    private Dialog dialog;

    private TextView changeProfile;
    private CircleImageView profileImage;

    private EditText companyNameBox;
    private LinearLayout companyNameInput;

    private EditText emailBox;
    private LinearLayout emailInput;

    private EditText phoneBox;

    private EditText websiteBox;
    private LinearLayout websiteInput;

    private Button saveInfoClick;

    private String userIdLocal = "";
    private String cvDownloadUrl = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_dashboard);

        changeProfile = (TextView)findViewById(R.id.change_profile);
        profileImage = (CircleImageView)findViewById(R.id.profile_image);

        companyNameBox = (EditText)findViewById(R.id.companyNameBox);
        companyNameInput = (LinearLayout) findViewById(R.id.company_name_input);

        phoneBox = (EditText)findViewById(R.id.phoneBox);

        emailBox = (EditText)findViewById(R.id.emailbox);
        emailInput = (LinearLayout) findViewById(R.id.email_input);

        websiteBox = (EditText)findViewById(R.id.websiteBox);
        websiteInput = (LinearLayout) findViewById(R.id.websiteInput);

        saveInfoClick = (Button)findViewById(R.id.save_button);


        //--

        ImagePickerActivity.clearCache(this);

        //--
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                changeProfile.startAnimation(buttonClick);
                onProfileImageClick();
                //onProfileImageClick();
            }
        });

        companyNameInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                companyNameInput.startAnimation(buttonClick);
                companyNameBox.startAnimation(buttonClick);

                setTouchableEditText(companyNameBox);
            }
        });

        emailInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                emailInput.startAnimation(buttonClick);
                emailBox.startAnimation(buttonClick);

                setTouchableEditText(emailBox);
            }
        });

        websiteInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                websiteInput.startAnimation(buttonClick);
                websiteBox.startAnimation(buttonClick);

                setTouchableEditText(websiteBox);
            }
        });


        saveInfoClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveInfoClick.startAnimation(buttonClick);

                submitForm();
            }
        });

        //-------------

        SharedPreferences prefs = getSharedPreferences("CompanyData", MODE_PRIVATE);


        userIdLocal = prefs.getString("userid", "");
        Log.d(TAG,"Trying to fetch user data with the user ID save in shared preference : "+userIdLocal);

        if(userIdLocal != null && !userIdLocal.equalsIgnoreCase("")){

            //fetch_user_info(Integer.parseInt(userID),0);
            fetch_user_info(Integer.parseInt(userIdLocal),1);

        }else{

            //Go to Log in
            Intent openCompanySignup = new Intent(this, Company_Login_1.class);
            startActivity(openCompanySignup);
            finish();
        }




    }

    private void setTouchableEditText(EditText editText){


        editText.setFocusableInTouchMode(true);
        editText.setFocusable(true);

        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    private void submitForm() {

        //--

        showLoadingBarAlert();




        JSONObject parameters = new JSONObject();
        try {

            parameters.put("userId", userIdLocal);
            parameters.put("companyName", companyNameBox.getText().toString());
            parameters.put("email", emailBox.getText().toString());
            parameters.put("phone", phoneBox.getText().toString());
            parameters.put("webUrl", websiteBox.getText().toString());




        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(TAG,parameters.toString());

        RequestQueue rq = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, ConstantsHolder.rawServer+ConstantsHolder.companyInfoUpdate, parameters, new com.android.volley.Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String respo=response.toString();
                        Log.d(TAG,respo);

                        //Log.d(TAG,respo);


                        //parseFetchData(response);
                        int status = response.optInt("status");
                        if(status == 200){

                            Toasty.success(Company_Dashboard.this,"Your info updated successfully!",Toast.LENGTH_LONG, true).show();


                        }else {


                            Toasty.error(Company_Dashboard.this,"Can't update info! Please check your internet connection & try again.",Toast.LENGTH_LONG, true).show();
                        }

                        hideLoadingBar();




                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Toasty.error(Company_Dashboard.this, "Server error,please check your internet connection!", Toast.LENGTH_LONG, true).show();
                        //Toast.makeText(Login_A.this, "Something wrong with Api", Toast.LENGTH_SHORT).show();
                        hideLoadingBar();

                    }
                }){

            /** Passing some request headers* */
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Content-Type", "application/json");
                //headers.put("apiKey", "xxxxxxxxxxxxxxx");
                return headers;
            }
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rq.getCache().clear();
        rq.add(jsonObjectRequest);

        //-----------------


        //------------


    }

    private void showLoadingBarAlert(){


        dialog = new Dialog(Company_Dashboard.this);

        dialog.setContentView(R.layout.custom_profile_dashboard_loading1);

        dialog.setTitle("Please wait!");

        dialog.setCancelable(false);



        DisplayMetrics displayMetrics = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setGravity(Gravity.END);



        dialog.show();

    }



    private void hideLoadingBar(){



        dialog.dismiss();

    }

    //--

    // this method will store the info of user to  database
    private void fetch_user_info(int userID,int userType) {

        showLoadingBarAlert();

        JSONObject parameters = new JSONObject();
        try {
            parameters.put("userId", userID);
            parameters.put("userType", userType);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(TAG,parameters.toString());

        RequestQueue rq = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, ConstantsHolder.rawServer+ConstantsHolder.fetchUserData, parameters, new com.android.volley.Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String respo=response.toString();
                        Log.d(TAG,respo);

                        //Log.d(TAG,respo);


                        parseFetchData(response);


                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Toasty.error(Company_Dashboard.this, "Server error,please check your internet connection!", Toast.LENGTH_LONG, true).show();
                        //Toast.makeText(Login_A.this, "Something wrong with Api", Toast.LENGTH_SHORT).show();
                        hideLoadingBar();

                    }
                }){

            /** Passing some request headers* */
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Content-Type", "application/json");
                //headers.put("apiKey", "xxxxxxxxxxxxxxx");
                return headers;
            }
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rq.getCache().clear();
        rq.add(jsonObjectRequest);

        //-----------------

    }

    private void parseFetchData(JSONObject jobj){

        if(jobj != null){

            boolean userExists =jobj.optBoolean("userExist");
            if(userExists){

                // Getting the Job Seeker info
                JSONObject jobSeekerModel = jobj.optJSONObject("companyModel");



                // 1.Showing the image
                String photoUrl = jobSeekerModel.optString("photoUrl");
                if(photoUrl != null || !photoUrl.equalsIgnoreCase("")){


                    Glide.with(this)
                            .load(photoUrl)
                            .centerCrop()
                            .placeholder(R.drawable.default_avatar)
                            .into(profileImage);

                }

                //-----------



                // 3.Printing the company name
                String fullName = jobSeekerModel.optString("companyName");
                if(fullName != null && !fullName.equalsIgnoreCase("") && !fullName.equalsIgnoreCase("")){

                    companyNameBox.setText(fullName);
                }
                //-----------

                // 4.Printing the phone number
                String phone = jobSeekerModel.optString("phone");
                if(phone != null && !phone.equalsIgnoreCase("") && !phone.equalsIgnoreCase("null")){

                    phoneBox.setText(phone);
                }
                //-----------

                // 4.Printing the email address
                String email = jobSeekerModel.optString("email");
                if(email != null && !email.equalsIgnoreCase("")  && !email.equalsIgnoreCase("null")){

                    emailBox.setText(email);
                }
                //-----------

                // 5.Printing the Web URL
                String dateOfBirth = jobSeekerModel.optString("webUrl");
                if(dateOfBirth != null && !dateOfBirth.equalsIgnoreCase("")  && !dateOfBirth.equalsIgnoreCase("null")){

                    websiteBox.setText(dateOfBirth);
                }
                //-----------


                hideLoadingBar();




            }else {
                // Go to Login
            }
        }else {

            // Go to Login
        }




    }



    //----------------------

    //--

    //-- Image picker tasks
    void onProfileImageClick() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            showImagePickerOptions();
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(this, new ImagePickerActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                launchCameraIntent();
            }

            @Override
            public void onChooseGallerySelected() {
                launchGalleryIntent();
            }
        });
    }

    private void launchCameraIntent() {

        launchGalleryIntent();

/*
        Intent intent = new Intent(this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        startActivityForResult(intent, REQUEST_IMAGE);
        */
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getParcelableExtra("path");
                try {
                    // You can update this bitmap to your server
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);




                    SaveImage saveImage = new SaveImage();
                    File file = saveImage.saveBitMap(getApplicationContext(),this,bitmap,getCurrentTimeStamp());
                    uploadImageWithId(file, getCurrentTimeStamp());
                    //Log.d("11111",file.getAbsolutePath());






                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }





        //----------
    }

    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.dialog_permission_title));
        builder.setMessage(getString(R.string.dialog_permission_message));
        builder.setPositiveButton(getString(R.string.go_to_settings), (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton(getString(android.R.string.cancel), (dialog, which) -> dialog.cancel());
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    // Profile image upload
    private void uploadImageWithId(File file1, String shortFilePath) {

        showLoadingBarAlert();


        if(userIdLocal.equalsIgnoreCase("") || userIdLocal == null){

            //-- Go to sign up screen


        }else {

            // create upload service client
            FileUploadService service =
                    ServiceGenerator.createService(FileUploadService.class);

            // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
            // use the FileUtils to get the actual file by uri
            //File file = FileUtils.getFile(this, fileUri);
            File file = file1;//File file = new File(filePath);;//FileUtils.getFile(this, fileUri);
            // Uri myUri = Uri.parse(filePath);



            // create RequestBody instance from file
            RequestBody requestFile =
                    RequestBody.create(
                            MediaType.parse(userIdLocal+"_"+shortFilePath),
                            file
                    );

            // MultipartBody.Part is used to send also the actual file name
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("file", file.getName(), requestFile);

            //--
            Call<UploadFileResponse> call = service.uploadImageWithId(body,Integer.parseInt(userIdLocal),"PNG");

            call.enqueue(new Callback<UploadFileResponse>() {
                @Override
                public void onResponse(Call<UploadFileResponse> call,
                                       retrofit2.Response<UploadFileResponse> response) {
                    Log.v(TAG, response.body().getFileName()+"-------- "+response.body().getFileDownloadUri());
                    //Toasty.success(Job_Seeker_CV_Upload.this,response.body().toString(),Toast.LENGTH_LONG, true).show();
                    Log.d(TAG,response.body().getFileDownloadUri());


                    if(response.body().getStatus() == 200){

                        //--success
                        Toasty.success(Company_Dashboard.this,"Profile image updated!",Toast.LENGTH_LONG, true).show();

                        // loading profile image from local cache
                        loadProfile(file1.getAbsolutePath());

                    }else{

                        Toasty.error(Company_Dashboard.this,"Can't upload profile image at this time! Try again later.",Toast.LENGTH_LONG, true).show();
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

    public void loadProfile(String url) {
        Log.d(TAG, "Image cache path: " + url);

        Glide.with(this)
                .load(url)
                .centerCrop()
                .placeholder(R.drawable.default_avatar)
                .into(profileImage);
        //imgProfile.setColorFilter(ContextCompat.getColor(ct, android.R.color.transparent));


    }

    public String getCurrentTimeStamp(){

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyyhmmss");
        String format = simpleDateFormat.format(new Date());

        return format;
    }


    //---------------------
}