package com.tur.job1.job_seeker;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.PRDownloaderConfig;
import com.downloader.Progress;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.krishna.fileloader.FileLoader;
import com.krishna.fileloader.listener.FileRequestListener;
import com.krishna.fileloader.pojo.FileResponse;
import com.krishna.fileloader.request.FileLoadRequest;
import com.suke.widget.SwitchButton;
import com.tur.job1.Intro;
import com.tur.job1.R;
import com.tur.job1.Splash;
import com.tur.job1.company.Company_Dashboard;
import com.tur.job1.company.Company_SearchBoard;
import com.tur.job1.models.DateResponse;
import com.tur.job1.models.LoginInformationResponse;
import com.tur.job1.models.PhoneNumberCheck;
import com.tur.job1.models.UploadFileResponse;
import com.tur.job1.others.API_Retrofit;
import com.tur.job1.others.ConstantsHolder;
import com.tur.job1.others.Dialogue_Helper;
import com.tur.job1.others.ExtraSkillSetFetcher;
import com.tur.job1.others.FileUploadService;
import com.tur.job1.others.ImagePickerActivity;
import com.tur.job1.others.SaveImage;
import com.tur.job1.others.ServiceGenerator;
import com.tur.job1.others.Skill_Selector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.core.content.ContextCompat;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class Job_Seeker_Dashboard extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.6F);

    public String TAG = "Job_Seeker_Dashboard";
    private int REQUEST_IMAGE = 100;

    private Dialog dialog;

    private TextView changeProfile;
    private CircleImageView profileImage;

    private SwitchButton visible_switch;



    private ImageView nameInputOpener;
    public EditText nameBox;

    private EditText phoneBox;

    private LinearLayout gender_input;
    private Spinner genderBox;

    private ImageView emailInputOpener;
    public EditText emailBox;

    private ImageView datelInputOpener;
    private EditText dateBox;

    private ImageView skillsInputOpener;
    private EditText skillsBox;

    private ImageView experienceInputOpener;
    private EditText experienceBox;

    private ImageView salaryInputOpener;
    private EditText salaryBox;

    private ImageView currentCompanyInputOpener;
    private EditText currentCompanyBox;

    private ImageView designationInputOpener;
    private EditText designationCompanyBox;

    private ImageView preferredInputOpener;
    private EditText preferredBox;

    List<String> skillIdList = new ArrayList<String>();

    List<String> skillNameList = new ArrayList<String>();

    private int genderSpineerFlag = 0;
    private JSONObject newObject;

    ArrayList<String> tempSkillId = new ArrayList<>();
    ArrayList<String> tempSkillList = new ArrayList<>();

    private String userIdLocal = "";
    private String cvDownloadUrl = "";


    private Button saveButtonClick;
    private Button cvDownloadClick;

    //-- Resume upload


    private Button uploadButton;

    private LinearLayout cvChooser;
    private EditText filePath;

    private static final int ACTIVITY_CHOOSE_FILE = 3;
    private String masterFilePath = "";

    Intent chooseFile;
    Intent intent;

    //---------------





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_seeker_dashboard);




        changeProfile = (TextView)findViewById(R.id.change_profile);
        profileImage = (CircleImageView)findViewById(R.id.profile_image);

        visible_switch = (SwitchButton)findViewById(R.id.visible_swith);

        nameInputOpener = (ImageView)findViewById(R.id.name_input);
        nameBox = (EditText)findViewById(R.id.namebox);

        phoneBox = (EditText)findViewById(R.id.phoneBox);

        gender_input = (LinearLayout)findViewById(R.id.gender_input);
        genderBox = (Spinner)findViewById(R.id.genderbox);

        emailInputOpener = (ImageView)findViewById(R.id.email_input);
        emailBox = (EditText)findViewById(R.id.emailbox);

        datelInputOpener = (ImageView)findViewById(R.id.date_input);
        dateBox = (EditText)findViewById(R.id.datebox);

        skillsInputOpener = (ImageView)findViewById(R.id.skill_input);
        skillsBox = (EditText)findViewById(R.id.skillBox);

        experienceInputOpener = (ImageView)findViewById(R.id.experienceInput);
        experienceBox = (EditText)findViewById(R.id.exprienceBox);

        salaryInputOpener = (ImageView)findViewById(R.id.salaryInput);
        salaryBox = (EditText)findViewById(R.id.salaryBox);

        currentCompanyInputOpener = (ImageView)findViewById(R.id.current_company_input);
        currentCompanyBox = (EditText)findViewById(R.id.currentCompanyBox);

        designationInputOpener = (ImageView)findViewById(R.id.designation_input);
        designationCompanyBox = (EditText)findViewById(R.id.designationBox);

        preferredInputOpener = (ImageView)findViewById(R.id.prepared_location_input);
        preferredBox = (EditText) findViewById(R.id.locationbox);

        cvDownloadClick = (Button)findViewById(R.id.cv_download_button);
        saveButtonClick = (Button)findViewById(R.id.save_button);

        //-- Resume upload

        cvChooser = (LinearLayout)findViewById(R.id.cv_select_panel);
        filePath = (EditText)findViewById(R.id.file_path);
        uploadButton = (Button)findViewById(R.id.upload_button);


        //---------------




        ImagePickerActivity.clearCache(this);

        //--
        changeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                changeProfile.startAnimation(buttonClick);
                onProfileImageClick();
                //onProfileImageClick();
            }
        });



        //Toasty.info(Job_Seeker_Dashboard.this, nameBox.getFocusable(), Toast.LENGTH_LONG, true).show();
        nameInputOpener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                nameInputOpener.startAnimation(buttonClick);
                nameBox.startAnimation(buttonClick);

                setTouchableEditText(nameBox);


            }
        });



        gender_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                genderBox.performClick();
            }
        });


        genderSpineerFlag = 0;
        genderBox.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                /*
                if(genderSpineerFlag >= 1){
                    UpdateGender(Integer.parseInt(userIdLocal),1,genderBox.getSelectedItem().toString());
                }else {
                    genderSpineerFlag++;
                }
                */



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                // sometimes you need nothing here
            }
        });


        emailInputOpener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //openEmailInput();

                emailInputOpener.startAnimation(buttonClick);
                emailBox.startAnimation(buttonClick);

                setTouchableEditText(emailBox);



            }
        });



        datelInputOpener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                 showDatePickerDialog();
            }
        });

        skillsInputOpener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openSkillInput();

            }
        });

        experienceInputOpener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //openExperienceInput();


                experienceInputOpener.startAnimation(buttonClick);
                experienceBox.startAnimation(buttonClick);

                setTouchableEditText(experienceBox);


            }
        });

        salaryInputOpener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //openSalaryInput();


                salaryInputOpener.startAnimation(buttonClick);
                salaryBox.startAnimation(buttonClick);

                setTouchableEditText(salaryBox);
            }
        });

        currentCompanyInputOpener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {





                currentCompanyInputOpener.startAnimation(buttonClick);
                currentCompanyBox.startAnimation(buttonClick);

                openCurrentCompanyInput();

                //setTouchableEditText(currentCompanyBox);

            }
        });

        designationInputOpener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                designationInputOpener.startAnimation(buttonClick);
                designationCompanyBox.startAnimation(buttonClick);

                openDesignationInput();

                //setTouchableEditText(designationCompanyBox);
            }
        });

        preferredInputOpener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openLocationInput();



            }
        });

        cvDownloadClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cvDownloadClick.startAnimation(buttonClick);

                //downloadCV(cvDownloadUrl,nameBox.getText().toString()+"_CV");

                /*
                String tempS = createDirectory(ConstantsHolder.directoryName);
                if(tempS != null && !tempS.equalsIgnoreCase("")) {

                    downloadCV(cvDownloadUrl,tempS,nameBox.getText().toString()+"_CV");

                }
                */

                if(cvDownloadUrl.equalsIgnoreCase("") || cvDownloadUrl == null){

                    Toasty.error(Job_Seeker_Dashboard.this,"Resume not available for download now! Try again later.",Toast.LENGTH_LONG, true).show();

                }else {
                    new DownloadFile().execute(cvDownloadUrl);
                }


            }
        });

        saveButtonClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveButtonClick.startAnimation(buttonClick);

                if(dateBox.getText().toString().equalsIgnoreCase("") || dateBox.getText() == null){

                    Toasty.error(Job_Seeker_Dashboard.this,"Please set your birthday first!",Toast.LENGTH_LONG, true).show();

                }else {

                    submitForm();
                }
            }
        });


        //-- Resume upload

        chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.setType("file/*");
        intent = Intent.createChooser(chooseFile, "Choose a file");

        //------------
        cvChooser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                cvChooser.startAnimation(buttonClick);

                if (ContextCompat.checkSelfPermission(Job_Seeker_Dashboard.this, Manifest.permission.READ_EXTERNAL_STORAGE)
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

                    Toasty.error(Job_Seeker_Dashboard.this, "Select a file first!", Toast.LENGTH_LONG, true).show();

                }else {


                    uploadImageWithId(masterFilePath,filePath.getText().toString());

                }

                //Toasty.success(Job_Seeker_CV_Upload.this,"Your CV uploaded successfully!",Toast.LENGTH_LONG, true).show();
            }
        });

        //---------------






        //-------------

        SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);


        userIdLocal = prefs.getString("userid", "");
        Log.d(TAG,"Trying to fetch user data with the user ID save in shared preference : "+userIdLocal);

        if(userIdLocal != null && !userIdLocal.equalsIgnoreCase("")){

            //fetch_user_info(Integer.parseInt(userID),0);
            fetch_user_info(Integer.parseInt(userIdLocal),0);

        }else{

            //Go to Log in
            Intent openJobSeekerSignUp = new Intent(this, Job_Seeker_Login.class);
            startActivity(openJobSeekerSignUp);
            finish();
        }



    }

    private void submitForm() {

       //--

         showLoadingBarAlert();

         Boolean checkFlag = false;
         if(visible_switch.isChecked()){

             checkFlag = true;

         }else {

             checkFlag = false;
         }

        JSONObject parameters = new JSONObject();
        try {

            parameters.put("userId", Integer.parseInt(userIdLocal));
            parameters.put("fullName", nameBox.getText().toString());
            parameters.put("visbile", checkFlag);
            parameters.put("gender", genderBox.getSelectedItem().toString());
            parameters.put("dateOfBirth", dateBox.getText().toString());
            parameters.put("email", emailBox.getText().toString());
            parameters.put("phone", phoneBox.getText().toString());
            parameters.put("expectedSalary", salaryExperience(salaryBox.getText().toString().trim()));
            parameters.put("experience", salaryExperience(experienceBox.getText().toString().trim()));
            parameters.put("preferLocation", preferredBox.getText().toString());
            parameters.put("photoUrl", "");
            parameters.put("cvUrl", "");
            parameters.put("skillsList", null);
            parameters.put("jobsHistories", null);



        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(TAG,parameters.toString());

        RequestQueue rq = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, ConstantsHolder.rawServer+ConstantsHolder.updateUserFullForm, parameters, new com.android.volley.Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String respo=response.toString();
                        Log.d(TAG,respo);

                        //Log.d(TAG,respo);


                        //parseFetchData(response);
                        int status = response.optInt("status");
                        if(status == 200){

                            Toasty.success(Job_Seeker_Dashboard.this,"Your info updated successfully!",Toast.LENGTH_LONG, true).show();


                        }else {


                            Toasty.error(Job_Seeker_Dashboard.this,"Can't update info! Please check your internet connection & try again.",Toast.LENGTH_LONG, true).show();
                        }

                        hideLoadingBar();




                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Toasty.error(Job_Seeker_Dashboard.this, "Server error,please check your internet connection!", Toast.LENGTH_LONG, true).show();
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

    private int salaryExperience(String st){

        int temp = 0;
        if(st.equalsIgnoreCase("") || st == null){

            temp = 0;
        }else{
            temp = Integer.parseInt(st);
        }

        return temp;
    }

    private void setTouchableEditText(EditText editText){


        editText.setFocusableInTouchMode(true);
        editText.setFocusable(true);

        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //genderSpineerFlag = 0;
    }

    @Override
    protected void onPause() {
        super.onPause();
        //genderSpineerFlag = 0;
    }

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

        //-- Resume

        String path     = "";
        if(requestCode == ACTIVITY_CHOOSE_FILE)
        {

            if (resultCode == Activity.RESULT_OK) {

                Uri uri = data.getData();
                String FilePath = getRealPathFromURI(uri);

                if(fileExtentionCheck(FilePath)){
                    //filePath.setText(fileNameSeperator(FilePath));
                    if(fileSizeFinder(FilePath)){

                        filePath.setText(fileNameSeperator(FilePath).trim());
                        masterFilePath = FilePath;

                    }else {

                        filePath.setText(fileNameSeperator(""));
                        Toasty.error(Job_Seeker_Dashboard.this, "File is too large! Maximum file size limit 5MB", Toast.LENGTH_LONG, true).show();
                    }
                }else {

                    Toasty.error(Job_Seeker_Dashboard.this, "File type not supported!\n Supported formats : pdf,docx,jpg,png", Toast.LENGTH_LONG, true).show();
                    filePath.setText("");
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

    public void loadProfile(String url) {
        Log.d(TAG, "Image cache path: " + url);

        Glide.with(this)
                .load(url)
                .centerCrop()
                .placeholder(R.drawable.default_avatar)
                .into(profileImage);
        //imgProfile.setColorFilter(ContextCompat.getColor(ct, android.R.color.transparent));


    }

    //------------------

    //--
    public void openNameInput(){


        Dialogue_Helper dh = new Dialogue_Helper();
        dh.askingForName(this,nameBox,this);
    }

    public void setName(){

        //nameBox.setText(name);
        //Toasty.success(Job_Seeker_Dashboard.this, "Successfully displayed the name!", Toast.LENGTH_LONG, true).show();
        UpdateUserName(Integer.parseInt(userIdLocal),1,nameBox.getText().toString());


    }

    public void openEmailInput(){


        Dialogue_Helper dh = new Dialogue_Helper();
        dh.askingForEmail(this,emailBox,this);

    }

    public void setEmail(){

        //Toasty.success(Job_Seeker_Dashboard.this, "Successfully displayed the Email!", Toast.LENGTH_LONG, true).show();
        UpdateUserEmail(Integer.parseInt(userIdLocal),1,emailBox.getText().toString());
    }

    public void openSkillInput(){

        Intent openCVwindow = new Intent(Job_Seeker_Dashboard.this, Skill_Selector.class);
        startActivity(openCVwindow);
        finish();
    }

    public void openExperienceInput(){

        Dialogue_Helper dh = new Dialogue_Helper();
        dh.askingForExperience(this,experienceBox,this);
    }

    public void setExperience(){

        UpdateUserExperience(Integer.parseInt(userIdLocal),1,experienceBox.getText().toString());
    }


    public void openSalaryInput(){

        Dialogue_Helper dh = new Dialogue_Helper();
        dh.askingForSalary(this,salaryBox,this);
    }

    public void setSalary(){

        UpdateUserSalary(Integer.parseInt(userIdLocal),1,salaryBox.getText().toString());
    }

    public void openCurrentCompanyInput(){

        Dialogue_Helper dh = new Dialogue_Helper();
        dh.askingForCurrentCompany(this,currentCompanyBox,this);
    }

    public void setCompany(){

        UpdateUserJobHistory(Integer.parseInt(userIdLocal),1,currentCompanyBox.getText().toString(),designationCompanyBox.getText().toString(),1);
    }

    public void openDesignationInput(){

        Dialogue_Helper dh = new Dialogue_Helper();
        dh.askingForDesignation(this,designationCompanyBox,this);
    }

    public void setDesignatiion(){

        UpdateUserJobHistory(Integer.parseInt(userIdLocal),1,currentCompanyBox.getText().toString(),designationCompanyBox.getText().toString(),2);
    }

    public void openLocationInput(){

        Dialogue_Helper dh = new Dialogue_Helper();
        dh.askingForPreperedLocation(this,preferredBox,this);
    }

    public void setLocation(){

        //UpdateUserLocation(Integer.parseInt(userIdLocal),1,preferredBox.getText().toString());
    }






    //-----------------
    public void showDatePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        //String date = "month/day/year: " + month + "/" + dayOfMonth + "/" + year;
       // String date2 = dayOfMonth+"-"+month+"-"+year;

        String day1 = completeNumber(dayOfMonth);
        String month1 = completeNumber(month);

        String niceFormat = day1+"-"+month1+"-"+year;

        dateBox.setText(niceFormat);

        //-- update date api
        //UpdateUserBirthdate(Integer.parseInt(userIdLocal),1,String.valueOf(dayOfMonth),String.valueOf(month),String.valueOf(year));

    }

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
                        Log.d("1212",respo);

                        //Log.d(TAG,respo);


                        parseFetchData(response);



                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Toasty.error(Job_Seeker_Dashboard.this, "Server error,please check your internet connection!", Toast.LENGTH_LONG, true).show();
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
                JSONObject jobSeekerModel = jobj.optJSONObject("jobSeekerModel");



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

                // 2. Showing the user visibility
                boolean visbile = jobSeekerModel.optBoolean("visbile");
                if(visbile){

                    visible_switch.setChecked(true);

                }else {

                    visible_switch.setChecked(false);

                }
                //-----------

                // 3.Printing the first name
                String fullName = jobSeekerModel.optString("fullName");
                if(fullName != null && !fullName.equalsIgnoreCase("") && !fullName.equalsIgnoreCase("null")){

                    nameBox.setText(fullName);
                }

                setTitle("Profile");
                //-----------

                // 3_1.Printing the gender type
                String gender = jobSeekerModel.optString("gender");
                if(gender != null && !gender.equalsIgnoreCase("") && !gender.equalsIgnoreCase("null")){

                    if(gender.equalsIgnoreCase("male")){

                        genderBox.setSelection(0);

                    }else if(gender.equalsIgnoreCase("female")){

                        genderBox.setSelection(1);

                    }else if(gender.equalsIgnoreCase("other")){

                        genderBox.setSelection(2);


                    }else{

                        //genderBox.setSelection(0);
                    }
                    //nameBox.setText(fullName);
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

                // 5.Printing the Date of Birth
                String dateOfBirth = jobSeekerModel.optString("dateOfBirth");
                if(dateOfBirth != null && !dateOfBirth.equalsIgnoreCase("")  && !dateOfBirth.equalsIgnoreCase("null")){

                    dateBox.setText(dateOfBirth);
                }
                //-----------

                // 6.Printing skills set
                 // to do
                // Getting the Skill Set
                //JSONObject jobsHistories = jobSeekerModel.optJSONObject("jobsHistories");
                //-----------

                // 7.Printing the Experience
                String experience = jobSeekerModel.optString("experience");
                if(experience != null && !experience.equalsIgnoreCase("") && !experience.equalsIgnoreCase("0") ){

                    experienceBox.setText(experience);
                }
                //-----------

                // 7.Printing the Expected salary
                String expectedSalary = jobSeekerModel.optString("expectedSalary");
                if(expectedSalary != null && !expectedSalary.equalsIgnoreCase("") && !expectedSalary.equalsIgnoreCase("null") && !experience.equalsIgnoreCase("0")){

                    salaryBox.setText(expectedSalary );
                }
                //-----------


                // Getting the Job history info
                JSONObject jobsHistories = jobSeekerModel.optJSONObject("jobsHistories");

                if(jobsHistories != null){

                    // 8.Printing the Current company
                    String companyName = jobsHistories.optString("companyName");
                    if(companyName != null && !companyName.equalsIgnoreCase("") && !companyName.equalsIgnoreCase("null")){

                        currentCompanyBox.setText(companyName);
                    }
                    //-----------

                    // 9.Printing the Designation
                    String designation = jobsHistories.optString("designation");
                    if(designation != null && !designation.equalsIgnoreCase("") && !designation.equalsIgnoreCase("null")){

                        designationCompanyBox.setText(designation);
                    }
                    //-----------

                }


                // 10.Printing the Prepared location
                String preferLocation = jobSeekerModel.optString("preferLocation");
                if(preferLocation != null && !preferLocation.equalsIgnoreCase("") && !preferLocation.equalsIgnoreCase("null")){


                    preferredBox.setText(preferLocation);
                }
                //-----------

                //6.Printing skills set
                // Getting the Skill Set
                //JSONObject skillSet = jobSeekerModel.optJSONObject("skillsList");
                //-----------

                cvDownloadUrl = jobSeekerModel.optString("cvUrl");


               makeArrayAdapterFromJsonObj(jobSeekerModel);
                hideLoadingBar();




            }else {
                // Go to Login
            }
        }else {

            // Go to Login
        }




    }

    private void showLoadingBarAlert(){


        dialog = new Dialog(Job_Seeker_Dashboard.this);

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



    public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
        Toast.makeText(parent.getContext(),
                "OnItemSelectedListener : " + parent.getItemAtPosition(pos).toString(),
                Toast.LENGTH_SHORT).show();
    }

    public String getCurrentTimeStamp(){

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyyhmmss");
        String format = simpleDateFormat.format(new Date());

        return format;
    }

    //--------------------------------------

    //-- Update api calls


    //--

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
                        Toasty.success(Job_Seeker_Dashboard.this,"Profile image updated!",Toast.LENGTH_LONG, true).show();

                        // loading profile image from local cache
                        loadProfile(file1.getAbsolutePath());

                    }else{

                        Toasty.error(Job_Seeker_Dashboard.this,"Can't upload profile image at this time! Try again later.",Toast.LENGTH_LONG, true).show();
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

    //---------

    // Name update
    private void UpdateUserName(int userID,int userType, String userName) {

        showLoadingBarAlert();

        //
        String namePart = String.valueOf(userID)+"/"+userName;

        RequestQueue rq = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, ConstantsHolder.rawServer+ConstantsHolder.updateUserName+namePart, null, new com.android.volley.Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String respo=response.toString();
                        Log.d(TAG,respo);

                        //Log.d(TAG,respo);


                        //parseFetchData(response);
                        int status = response.optInt("status");
                        if(status == 200){

                            Toasty.success(Job_Seeker_Dashboard.this,"Name updated successfully!",Toast.LENGTH_LONG, true).show();

                        }else {


                            Toasty.error(Job_Seeker_Dashboard.this,"Can't update name! Please check your internet connection & try again.",Toast.LENGTH_LONG, true).show();
                        }

                        hideLoadingBar();




                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Toasty.error(Job_Seeker_Dashboard.this, "Server error,please check your internet connection!", Toast.LENGTH_LONG, true).show();
                        //Toast.makeText(Login_A.this, "Something wrong with Api", Toast.LENGTH_SHORT).show();
                        hideLoadingBar();

                    }
                });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rq.getCache().clear();
        rq.add(jsonObjectRequest);

        //-----------------

    }

    // Gender update
    private void UpdateGender(int userID,int userType, String genderName) {

       // showLoadingBarAlert();

        //
        String namePart = String.valueOf(userID)+"/"+genderName;

        RequestQueue rq = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, ConstantsHolder.rawServer+ConstantsHolder.updateGender+namePart, null, new com.android.volley.Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String respo=response.toString();
                        Log.d(TAG,respo);

                        //Log.d(TAG,respo);


                        //parseFetchData(response);
                        int status = response.optInt("status");
                        if(status == 200){

                           // Toasty.success(Job_Seeker_Dashboard.this,"Gender updated successfully!",Toast.LENGTH_LONG, true).show();

                        }else {


                          //  Toasty.error(Job_Seeker_Dashboard.this,"Can't update gender! Please check your internet connection & try again.",Toast.LENGTH_LONG, true).show();
                        }

                       // hideLoadingBar();




                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Toasty.error(Job_Seeker_Dashboard.this, "Server error,please check your internet connection!", Toast.LENGTH_LONG, true).show();
                        //Toast.makeText(Login_A.this, "Something wrong with Api", Toast.LENGTH_SHORT).show();
                       // hideLoadingBar();

                    }
                });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rq.getCache().clear();
        rq.add(jsonObjectRequest);

        //-----------------

    }

    // Email update
    private void UpdateUserEmail(int userID,int userType, String userEmail) {

        showLoadingBarAlert();

        //
        String namePart = String.valueOf(userID)+"/"+userEmail;

        RequestQueue rq = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, ConstantsHolder.rawServer+ConstantsHolder.updateUserEmail+namePart, null, new com.android.volley.Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String respo=response.toString();
                        Log.d(TAG,respo);

                        //Log.d(TAG,respo);


                        //parseFetchData(response);
                        int status = response.optInt("status");
                        if(status == 200){

                            Toasty.success(Job_Seeker_Dashboard.this,"Email updated successfully!",Toast.LENGTH_LONG, true).show();

                        }else {


                            Toasty.error(Job_Seeker_Dashboard.this,"Can't update Email! Please check your internet connection & try again.",Toast.LENGTH_LONG, true).show();
                        }

                        hideLoadingBar();




                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Toasty.error(Job_Seeker_Dashboard.this, "Server error,please check your internet connection!", Toast.LENGTH_LONG, true).show();
                        //Toast.makeText(Login_A.this, "Something wrong with Api", Toast.LENGTH_SHORT).show();
                        hideLoadingBar();

                    }
                });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rq.getCache().clear();
        rq.add(jsonObjectRequest);

        //-----------------

    }

    // Birthdate update
    private void UpdateUserBirthdate(int userID,int userType, String day, String month, String year) {



        showLoadingBarAlert();
        JSONObject parameters = new JSONObject();
        try {



            String day1 = completeNumber(Integer.parseInt(day));
            String month1 = completeNumber(Integer.parseInt(month));


            parameters.put("userId", userID);
            parameters.put("day", day1);
            parameters.put("month",month1);
            parameters.put("year",year);



        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(TAG,parameters.toString());

        //TURZO
        RequestQueue rq = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, ConstantsHolder.rawServer+ConstantsHolder.updateUserBirthdate, parameters, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String respo=response.toString();
                        Log.d(TAG,respo);

                        int status = response.optInt("status");

                        if(status == 200){

                            Toasty.success(Job_Seeker_Dashboard.this,"Birth-date updated successfully!",Toast.LENGTH_LONG, true).show();

                        }else{

                            Toasty.error(Job_Seeker_Dashboard.this,"Can't update Birth-date! Please check your internet connection & try again.",Toast.LENGTH_LONG, true).show();

                        }

                        hideLoadingBar();



                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Toasty.error(Job_Seeker_Dashboard.this, "Server error,please check your internet connection!", Toast.LENGTH_LONG, true).show();
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






    }

    private String completeNumber(int data){
        String temp = "";
        if(data < 10 ){

            temp = "0"+String.valueOf(data);
        }else {

            temp = String.valueOf(data);
        }

        return temp;
    }


    // Experience update
    private void UpdateUserExperience(int userID,int userType, String experience) {

        showLoadingBarAlert();

        //
        String extraPart = String.valueOf(userID)+"/"+experience;

        RequestQueue rq = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, ConstantsHolder.rawServer+ConstantsHolder.updateUserExperience+extraPart, null, new com.android.volley.Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String respo=response.toString();
                        Log.d(TAG,respo);

                        //Log.d(TAG,respo);


                        //parseFetchData(response);
                        int status = response.optInt("status");
                        if(status == 200){

                            Toasty.success(Job_Seeker_Dashboard.this,"Experience updated successfully!",Toast.LENGTH_LONG, true).show();

                        }else {


                            Toasty.error(Job_Seeker_Dashboard.this,"Can't update experience! Please check your internet connection & try again.",Toast.LENGTH_LONG, true).show();
                        }

                        hideLoadingBar();




                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Toasty.error(Job_Seeker_Dashboard.this, "Server error,please check your internet connection!", Toast.LENGTH_LONG, true).show();
                        //Toast.makeText(Login_A.this, "Something wrong with Api", Toast.LENGTH_SHORT).show();
                        hideLoadingBar();

                    }
                });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rq.getCache().clear();
        rq.add(jsonObjectRequest);

        //-----------------

    }

    // Salary update
    private void UpdateUserSalary(int userID,int userType, String salary) {

        showLoadingBarAlert();

        //
        String extraPart = String.valueOf(userID)+"/"+salary;

        RequestQueue rq = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, ConstantsHolder.rawServer+ConstantsHolder.updateUserSalary+extraPart, null, new com.android.volley.Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String respo=response.toString();
                        Log.d(TAG,respo);

                        //Log.d(TAG,respo);


                        //parseFetchData(response);
                        int status = response.optInt("status");
                        if(status == 200){

                            Toasty.success(Job_Seeker_Dashboard.this,"Salary updated successfully!",Toast.LENGTH_LONG, true).show();

                        }else {


                            Toasty.error(Job_Seeker_Dashboard.this,"Can't update Salary! Please check your internet connection & try again.",Toast.LENGTH_LONG, true).show();
                        }

                        hideLoadingBar();




                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Toasty.error(Job_Seeker_Dashboard.this, "Server error,please check your internet connection!", Toast.LENGTH_LONG, true).show();
                        //Toast.makeText(Login_A.this, "Something wrong with Api", Toast.LENGTH_SHORT).show();
                        hideLoadingBar();

                    }
                });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rq.getCache().clear();
        rq.add(jsonObjectRequest);

        //-----------------

    }

    // Preferred location update
    private void UpdateUserLocation(int userID,int userType, String location) {

        showLoadingBarAlert();

        //
        String extraPart = String.valueOf(userID)+"/"+location;

        RequestQueue rq = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, ConstantsHolder.rawServer+ConstantsHolder.updateUserLocation+extraPart, null, new com.android.volley.Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String respo=response.toString();
                        Log.d(TAG,respo);

                        //Log.d(TAG,respo);


                        //parseFetchData(response);
                        int status = response.optInt("status");
                        if(status == 200){

                            Toasty.success(Job_Seeker_Dashboard.this,"Location updated successfully!",Toast.LENGTH_LONG, true).show();

                        }else {


                            Toasty.error(Job_Seeker_Dashboard.this,"Can't update location! Please check your internet connection & try again.",Toast.LENGTH_LONG, true).show();
                        }

                        hideLoadingBar();




                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Toasty.error(Job_Seeker_Dashboard.this, "Server error,please check your internet connection!", Toast.LENGTH_LONG, true).show();
                        //Toast.makeText(Login_A.this, "Something wrong with Api", Toast.LENGTH_SHORT).show();
                        hideLoadingBar();

                    }
                });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rq.getCache().clear();
        rq.add(jsonObjectRequest);

        //-----------------

    }

    // Job History Update
    private void UpdateUserJobHistory(int userID,int userType, String companyName, String currentDesignation, int msgType) {

        //showLoadingBarAlert();

        JSONObject parameters = new JSONObject();
        try {

            parameters.put("userId", userIdLocal);
            parameters.put("companyName", companyName);
            parameters.put("designation", currentDesignation);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(TAG,parameters.toString());
        String extraPart = String.valueOf(userID);

        RequestQueue rq = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, ConstantsHolder.rawServer+ConstantsHolder.updateUserJobHistory, parameters, new com.android.volley.Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String respo=response.toString();
                        Log.d(TAG,respo);

                        //Log.d(TAG,respo);


                        //parseFetchData(response);
                        int status = response.optInt("status");
                        if(status == 200){

                            if(msgType == 1){


                                //Toasty.success(Job_Seeker_Dashboard.this,"Company updated successfully!",Toast.LENGTH_LONG, true).show();
                                Log.d(TAG,"Company updated successfully!");

                            }

                            if(msgType == 2){


                               // Toasty.success(Job_Seeker_Dashboard.this,"Designation name updated successfully!",Toast.LENGTH_LONG, true).show();
                                Log.d(TAG,"Designation updated successfully!");

                            }


                        }else {


                            //Toasty.error(Job_Seeker_Dashboard.this,"Can't update info! Please check your internet connection & try again.",Toast.LENGTH_LONG, true).show();
                            Log.d(TAG,"Can't update Company & Designation info! Please check your internet connection & try again.");
                        }

                        //hideLoadingBar();




                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Toasty.error(Job_Seeker_Dashboard.this, "Server error,please check your internet connection!", Toast.LENGTH_LONG, true).show();
                        //Toast.makeText(Login_A.this, "Something wrong with Api", Toast.LENGTH_SHORT).show();
                        //hideLoadingBar();

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


    private void makeArrayAdapterFromJsonObj(JSONObject skillSet){

        if(tempSkillId.size() >0){
            tempSkillId.clear();
        }

        try {


            JSONArray array = skillSet.getJSONArray("skillsList");
            if(array.length() > 0) {


                for(int i=0; i<array.length(); i++){

                    JSONObject object = array.getJSONObject(i);

                    String id = object.getString("skillId");

                    tempSkillId.add(id);

                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        if(tempSkillId.size()>0){

            Log.d(TAG,tempSkillId.toString());

            ExtraSkillSetFetcher extraSkillSetFetcher = new ExtraSkillSetFetcher();
            extraSkillSetFetcher.fetch_skill_list(tempSkillId,this,this);


        }else {

            Log.d(TAG,"Skill list is empty!");
            //hideLoadingBar();
        }

        /*
        JSONObject firstModel = skillSet.optJSONObject("skillsList");
        Log.d(TAG,firstModel.toString());
        if(firstModel == null){

            Log.d("123456","Array is null------------------------");
            hideLoadingBar();

        }else {

            Log.d(TAG,"Congratulation!!!!!!!!!!!!!!");
            try {


                JSONArray array = skillSet.getJSONArray("skillsList");
                if(array.length() > 0) {


                    for(int i=0; i<array.length(); i++){

                        JSONObject object = array.getJSONObject(i);

                        String id = object.getString("skillId");

                        tempSkillId.add(id);

                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            if(tempSkillId.size()>0){

                Log.d(TAG,tempSkillId.toString());

                ExtraSkillSetFetcher extraSkillSetFetcher = new ExtraSkillSetFetcher();
                extraSkillSetFetcher.fetch_skill_list(tempSkillId,this,this);


            }else {

                Log.d(TAG,"Skill list is empty!");
            }

        }
        */



        /*

         try {


            JSONArray array = skillSet.getJSONArray("skillsList");
            if(array.length() > 0 && array != null) {


                for(int i=0; i<array.length(); i++){

                    JSONObject object = array.getJSONObject(i);

                    String id = object.getString("skillId");

                    tempSkillId.add(id);

                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        if(tempSkillId.size()>0){

            Log.d(TAG,tempSkillId.toString());

            ExtraSkillSetFetcher extraSkillSetFetcher = new ExtraSkillSetFetcher();
            extraSkillSetFetcher.fetch_skill_list(tempSkillId,this,this);
        }else {

            Log.d(TAG,"Skill list is empty!");
        }
         */


    }


    public void fetchExtraSkillSet(String temp){

        skillsBox.setText(temp);
        hideLoadingBar();

    }

    public String createDirectory(String directoryName){

        File yourAppDir = new File(Environment.getExternalStorageDirectory()+File.separator+directoryName);

        String tempS = "";
        if(!yourAppDir.exists() && !yourAppDir.isDirectory())
        {
            // create empty directory
            if (yourAppDir.mkdirs())
            {
                Log.i("CreateDir","App dir created");
                tempS = Environment.getExternalStorageDirectory()+File.separator+directoryName;
            }
            else
            {
                Log.w("CreateDir","Unable to create app dir!");
            }
        }
        else
        {
            Log.i("CreateDir","App dir already exists");
            tempS = Environment.getExternalStorageDirectory()+File.separator+directoryName;
        }

        return tempS;


    }

    private void downloadCV(String url, String directory, String filename1){


        PRDownloader.initialize(getApplicationContext());

        // Enabling database for resume support even after the application is killed:
        PRDownloaderConfig config = PRDownloaderConfig.newBuilder()
                .setDatabaseEnabled(true)
                .setReadTimeout(30_000)
                .setConnectTimeout(30_000)
                .build();
        PRDownloader.initialize(getApplicationContext(), config);


        String fileName = filename1;
        int downloadId = PRDownloader.download(url, directory, fileName)
                .build()
                .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                    @Override
                    public void onStartOrResume() {

                    }
                })
                .setOnPauseListener(new OnPauseListener() {
                    @Override
                    public void onPause() {

                    }
                })
                .setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel() {

                    }
                })
                .setOnProgressListener(new OnProgressListener() {
                    @Override
                    public void onProgress(Progress progress) {

                    }
                })
                .start(new OnDownloadListener() {
                    @Override
                    public void onDownloadComplete() {

                    }

                    @Override
                    public void onError(Error error) {

                    }


                });

    }

    public void showExitDialogue(){

        new MaterialStyledDialog.Builder(this)
                .setIcon(R.drawable.logout_icon)
                .setHeaderColor(R.color.error_red)
                .setTitle("Log Out?")
                .setDescription("Do you want to log out from this app?")

                .setCancelable(false)
                .setPositiveText("Log Out")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        SharedPreferences.Editor editor = getSharedPreferences("UserData", MODE_PRIVATE).edit();
                        editor.putString("userid", "");
                        editor.apply();

                        Intent openJobSeekerSignUp = new Intent(Job_Seeker_Dashboard.this, Intro.class);
                        startActivity(openJobSeekerSignUp);
                        finish();
                    }
                })
                .setNegativeText("Cancel")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {


                    }
                })
                .show();
    }

    //-- Resume upload

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


                if(tempS.charAt(i) == ' '){
                    tempS2 = tempS2 + "_";
                }else {
                    tempS2 = tempS2 + tempS.charAt(i);
                }

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

    private void uploadImageWithId(String filePath, String shortFilePath) {

        showLoadingBarAlert();

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

        String timestamp = new SimpleDateFormat("HH_mm_ss").format(new Date());

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", timestamp+"_"+shortFilePath, requestFile);



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
                                       retrofit2.Response<UploadFileResponse> response) {
                    //Log.v("112233", response.body().getFileName()+"-------- "+response.body().getFileDownloadUri());
                    //Toasty.success(Job_Seeker_CV_Upload.this,response.body().toString(),Toast.LENGTH_LONG, true).show();
                    Log.d(TAG,response.body().getFileDownloadUri());


                    if(response.body().getStatus() == 200){

                        //--success
                        Toasty.success(Job_Seeker_Dashboard.this,"Resume uploaded successfully!",Toast.LENGTH_LONG, true).show();


                    }else{

                        Toasty.error(Job_Seeker_Dashboard.this,"User not created yet!",Toast.LENGTH_LONG, true).show();
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




    //------------------

    //-- Resume Download

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
            this.progressDialog = new ProgressDialog(Job_Seeker_Dashboard.this);
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

                String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

                //Extract file name from URL
                fileName = f_url[0].substring(f_url[0].lastIndexOf('/') + 1, f_url[0].length());

                //Append timestamp to file name
                fileName = "_" + fileName;//+"_"+timestamp;

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
            Toast.makeText(getApplicationContext(),
                    message, Toast.LENGTH_LONG).show();
        }
    }

    //--------------------

    @Override
    public void onBackPressed() {

        showExitDialogue();

        //super.onBackPressed();
    }

    //--------------------------------------


}
