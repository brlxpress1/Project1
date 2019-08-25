package com.tur.job1.job_seeker;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.google.android.material.navigation.NavigationView;
import com.tur.job1.Intro;
import com.tur.job1.R;
import com.tur.job1.company.Company_Dashboard;
import com.tur.job1.company.Company_Login_1;
import com.tur.job1.company.Company_SearchBoard;
import com.tur.job1.company.Company_Signup_1;
import com.tur.job1.others.Connectivity;
import com.tur.job1.others.ConstantsHolder;
import com.tur.job1.others.ImagePickerActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class Job_Seeker_Modified_Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.6F);

    private String TAG = "Job_Seeker_Modified_Dashboard";


    private NavigationView navigationView;
    private String userIdLocal = "";

    private CircleImageView avatarPhoto;
    private TextView avatarName;
    private LinearLayout drawerOpener;
    private DrawerLayout drawer_layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_seeker_modified_dashboard);


        navigationView = (NavigationView) findViewById(R.id.navigationView);

        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
        }


        View header = navigationView.getHeaderView(0);
        avatarPhoto = (CircleImageView) header.findViewById(R.id.avatar_pic);
        avatarName = (TextView) header.findViewById(R.id.nav_header_textView);
        drawerOpener = (LinearLayout) findViewById(R.id.draweropener);
        drawer_layout = (DrawerLayout)findViewById(R.id.drawer_layout);


        /*
        SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);


        userIdLocal = prefs.getString("userid", "");
        Log.d(TAG, "Trying to fetch user data with the user ID save in shared preference : " + userIdLocal);

        if (userIdLocal != null && !userIdLocal.equalsIgnoreCase("")) {


           fetch_user_info(Integer.parseInt(userIdLocal),0);


        } else {

            //Go to Log in
            Intent openCompanySignup = new Intent(this, Job_Seeker_Login.class);
            startActivity(openCompanySignup);
            finish();

        }
*/

        fetch_user_info(88,0);


        drawerOpener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                drawer_layout.openDrawer(Gravity.LEFT);
            }
        });




    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        int id = menuItem.getItemId();

        if(id == R.id.my_profile){

            //Toasty.success(Company_SearchBoard.this, "My Profile", Toast.LENGTH_LONG, true).show();
            Intent openJobSeekerSignUp = new Intent(Job_Seeker_Modified_Dashboard.this, Job_Seeker_Dashboard.class);
            startActivity(openJobSeekerSignUp);
            finish();

        }else if(id == R.id.privacy_policy){

            Toasty.info(Job_Seeker_Modified_Dashboard.this, "Coming soon!", Toast.LENGTH_LONG, true).show();

        }else if(id == R.id.log_out){

            SharedPreferences.Editor editor = getSharedPreferences("UserData", MODE_PRIVATE).edit();
            editor.putString("userid", "");
            editor.apply();

            SharedPreferences.Editor typeEditor = getSharedPreferences("UserType", MODE_PRIVATE).edit();
            typeEditor.putInt("type", 0);
            typeEditor.apply();


            Intent openJobSeekerSignUp = new Intent(Job_Seeker_Modified_Dashboard.this, Intro.class);
            startActivity(openJobSeekerSignUp);
            finish();
        }

        return true;
    }

    // this method will store the info of user to  database
    private void fetch_user_info(int userID,int userType) {



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
                        //Toasty.error(Company_Dashboard.this, "Server error,please check your internet connection!", Toast.LENGTH_LONG, true).show();
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
                            .into(avatarPhoto);

                }

                //-----------



                // 3.Printing the company name
                String fullName = jobSeekerModel.optString("fullName");
                if(fullName != null && !fullName.equalsIgnoreCase("") && !fullName.equalsIgnoreCase("")){

                    avatarName.setText(fullName);
                }else {
                    avatarName.setText("");
                }

                //setTitle(modifyTitle(fullName));
                //-----------





            }else {
                // Go to Login
            }
        }else {

            // Go to Login
        }




    }
}