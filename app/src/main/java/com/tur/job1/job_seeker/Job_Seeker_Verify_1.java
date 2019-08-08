package com.tur.job1.job_seeker;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
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
import com.hbb20.CountryCodePicker;
import com.tur.job1.Intro;
import com.tur.job1.R;
import com.tur.job1.models.LoginInformationResponse;
import com.tur.job1.models.PhoneNumberCheck;
import com.tur.job1.models.SignUpResponse;
import com.tur.job1.others.API_Retrofit;
import com.tur.job1.others.Connectivity;
import com.tur.job1.others.ConstantsHolder;
import com.tur.job1.others.ServiceGenerator;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;

public class Job_Seeker_Verify_1 extends AppCompatActivity {

    private String TAG = "Job_Seeker_Verify_1";
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.6F);



    private Button next;
    private EditText name;
    private TextView countryCode;
    private EditText phone_number;
    private LinearLayout countryCodePanel;


    private CountryCodePicker ccp;
    private Dialog dialog;



    //public String otpID;
    private String userName;
    private String userPhone;
    private String userPurePhone;


    private  TextView login_helper_txt;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_seeker_verify_1);


        next = (Button)findViewById(R.id.next_button);
        name = (EditText)findViewById(R.id.name_input);
        countryCode = (TextView)findViewById(R.id.country_code_input);
        phone_number = (EditText)findViewById(R.id.phone_number_input);

        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        countryCodePanel = (LinearLayout)findViewById(R.id.country_code_panel);

        login_helper_txt = (TextView)findViewById(R.id.login_helper_txt);

        //-- initial calls
/*
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
        */



        //------------------




        //-- Changing selection effect of input fields
        name.setOnFocusChangeListener((view, b) -> {
            if (view.isFocused()) {
                // Do whatever you want when the EditText is focused
                // Example:
                //editTextFrom.setText("Focused!");
                name.setBackgroundResource(R.drawable.edittext_box_border);
            }
            else{

                name.setBackgroundResource(R.drawable.shape_with_border1);

            }
        });



        phone_number.setOnFocusChangeListener((view, b) -> {
            if (view.isFocused()) {
                // Do whatever you want when the EditText is focused
                // Example:
                //editTextFrom.setText("Focused!");
                phone_number.setBackgroundResource(R.drawable.edittext_box_border);
            }
            else{
                phone_number.setBackgroundResource(R.drawable.shape_with_border1);
            }
        });

        login_helper_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent openCVwindow = new Intent(Job_Seeker_Verify_1.this,Job_Seeker_Login.class);
                startActivity(openCVwindow);
                finish();
            }
        });



        //-----------------



       // showLoadingBarAlert();
        if(userName != null && !userName.equalsIgnoreCase("")){

            name.setText(userName);

        }

        if(userPurePhone != null && !userPurePhone.equalsIgnoreCase("")){

            phone_number.setText(userPurePhone);

        }

        countryCodePanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                countryCodePanel.startAnimation(buttonClick);

                ccp.launchCountrySelectionDialog();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                next.startAnimation(buttonClick);

                //-- Check if user is connected with the internet
                if (Connectivity.isConnected(Job_Seeker_Verify_1.this)) {


                    //-- Check if name input field is empty
                    String temp1 = name.getText().toString().trim();
                    if(temp1.equalsIgnoreCase("") || temp1 == null){

                        Toasty.error(Job_Seeker_Verify_1.this, "Please enter your name!", Toast.LENGTH_LONG, true).show();
                    }
                    else {

                        //-- Check if phone number input field is empty
                        String temp2 = phone_number.getText().toString().trim();
                        if(temp2.equalsIgnoreCase("") || temp2 == null){

                            Toasty.error(Job_Seeker_Verify_1.this, "Please enter your phone number!", Toast.LENGTH_LONG, true).show();
                        }else {


                            String tempName = name.getText().toString();
                            //String tempPhone = ccp.getSelectedCountryCodeWithPlus()+phone_number.getText().toString().trim();
                            String tempPhone = countryCode.getText().toString().trim()+phone_number.getText().toString().trim();

                            //Toasty.success(Job_Seeker_Verify_1.this, "Name : "+tempName+"\nPhone : "+tempPhone, Toast.LENGTH_LONG, true).show();

                            //showLoadingBarAlert();
                            //-- task
                            userName = tempName;
                            userPhone = tempPhone;
                            userPurePhone = removePlusFromPhone(userPhone);

                            SharedPreferences.Editor editor = getSharedPreferences("UserData", MODE_PRIVATE).edit();
                            editor.putString("username", userName);
                            editor.putString("userphone", userPhone);
                            editor.putString("userphonepure", userPurePhone);

                            editor.apply();

                            /*


                             */

                           // Intent openSecondVerifier = new Intent(Job_Seeker_Verify_1.this,Job_Seeker_Verify_2.class);
                           // startActivity(openSecondVerifier);
                           // finish();

                            phone_number_check(removePlusFromPhone(userPhone));
                        }

                    }


                } else {


                    Toasty.error(Job_Seeker_Verify_1.this, "You have no internet access! Please turn on your WiFi or mobile data.", Toast.LENGTH_LONG, true).show();

                }


            }
        });

        //--

        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {

            @Override

            public void onCountrySelected() {

                ccp.startAnimation(buttonClick);
                countryCode.setText(ccp.getSelectedCountryCodeWithPlus().toString().trim());



            }

        });

        //-----------------


    }

    private void showLoadingBarAlert(){


        dialog = new Dialog(Job_Seeker_Verify_1.this);

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

    // this method will store the info of user to  database
    private void phone_number_check(String userPhone) {


        //--
      /*
        API_Retrofit service =
                ServiceGenerator.createService(API_Retrofit.class);


        JSONObject parameters = new JSONObject();
        try {
            parameters.put("userPhoneNumber", userPhone);
            parameters.put("userType",0);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(TAG,parameters.toString());


        Call<PhoneNumberCheck> call = service.userExistCheck(parameters);
        call.enqueue(new Callback<PhoneNumberCheck>() {
            @Override
            public void onResponse(Call<PhoneNumberCheck> call,
                                   Response<PhoneNumberCheck> response) {

               Log.d(TAG,response.body().isUserExist()+"----------------"+response.body().isUserSobseeker());


                if(response.body().isUserExist()){

                    Toasty.error(Job_Seeker_Verify_1.this, "This phone number already exists! Try log in now.", Toast.LENGTH_LONG, true).show();


                }else {

                    //Toasty.success(Job_Seeker_Verify_1.this, "You can open a new account", Toast.LENGTH_LONG, true).show();



                    Log.d(TAG,"Ready to verify");

                }

               //                 hideLoadingBar();
            }

            @Override
            public void onFailure(Call<PhoneNumberCheck> call, Throwable t) {
                Log.e(TAG, t.getMessage());
               // hideLoadingBar();
            }
        });


        //-----------------
        */





        JSONObject parameters = new JSONObject();
        try {
            parameters.put("userPhoneNumber", userPhone);
            parameters.put("userType",0);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(TAG,parameters.toString());

        RequestQueue rq = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, ConstantsHolder.rawServer+ConstantsHolder.phoneCheck, parameters, new com.android.volley.Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String respo=response.toString();
                        Log.d(TAG,respo);

                        //Log.d(TAG,respo);


                        //parseFetchData(response);
                        boolean userExist = response.optBoolean("userExist");
                        if(userExist){

                            Toasty.error(Job_Seeker_Verify_1.this,"This phone number already exists! Try log in now.",Toast.LENGTH_LONG, true).show();

                        }else {


                           // Toasty.error(Job_Seeker_Verify_1.this,"Can't update location! Please check your internet connection & try again.",Toast.LENGTH_LONG, true).show();
                           // Log.d(TAG,"Ready to verify");

                            Intent openSecondVerifier = new Intent(Job_Seeker_Verify_1.this,Job_Seeker_Verify_2.class);
                            startActivity(openSecondVerifier);
                            finish();

                        }

                       // hideLoadingBar();




                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Toasty.error(Job_Seeker_Verify_1.this, "Server error,please check your internet connection!", Toast.LENGTH_LONG, true).show();
                        //Toast.makeText(Login_A.this, "Something wrong with Api", Toast.LENGTH_SHORT).show();
                       // hideLoadingBar();

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

    // if the signup successfull then this method will call and it store the user info in local
    public void parsePhoneCheckData(String loginData){
        try {
            JSONObject jsonObject=new JSONObject(loginData);
            String userExist =jsonObject.optString("userExist");



            if(userExist.equalsIgnoreCase("true")){

                Toasty.error(Job_Seeker_Verify_1.this, "This phone number already exists! Try log in now.", Toast.LENGTH_LONG, true).show();


            }else {

                //Toasty.success(Job_Seeker_Verify_1.this, "You can open a new account", Toast.LENGTH_LONG, true).show();
                Intent openSecondVerifier = new Intent(Job_Seeker_Verify_1.this,Job_Seeker_Verify_2.class);
                startActivity(openSecondVerifier);
                finish();

            }

        } catch (JSONException e) {

            Toasty.error(Job_Seeker_Verify_1.this, "Server error,please check your internet connection!", Toast.LENGTH_LONG, true).show();
            e.printStackTrace();
        }

    }


   private String removePlusFromPhone(String ph){

        String temp = "";

        for(int i=0; i<ph.length(); i++){

            if(i==0){

            }else{
                temp = temp + ph.charAt(i);
            }
        }

        return  temp;
   }



    @Override
    public void onBackPressed() {



        Intent introOpener = new Intent(Job_Seeker_Verify_1.this,Intro.class);
        startActivity(introOpener);
        finish();

    }



}
