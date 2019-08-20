package com.tur.job1.company;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

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
import com.tur.job1.Splash;
import com.tur.job1.job_seeker.Job_Seeker_Dashboard;
import com.tur.job1.job_seeker.Job_Seeker_Verify_1;
import com.tur.job1.others.Connectivity;
import com.tur.job1.others.ConstantsHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class Company_SearchBoard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.6F);

    private String TAG = "Company_SearchBoard";


    private NavigationView navigationView;
    private String userIdLocal = "";

    private CircleImageView avatarPhoto;
    private TextView avatarName;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_searchboard);


        navigationView = (NavigationView) findViewById(R.id.navigationView);

        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
        }



        View header = navigationView.getHeaderView(0);
        avatarPhoto = (CircleImageView) header.findViewById(R.id.avatar_pic);
        avatarName = (TextView) header.findViewById(R.id.nav_header_textView);



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


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        int id = menuItem.getItemId();

        if(id == R.id.my_profile){

            //Toasty.success(Company_SearchBoard.this, "My Profile", Toast.LENGTH_LONG, true).show();
            Intent openJobSeekerSignUp = new Intent(Company_SearchBoard.this, Company_Dashboard.class);
            startActivity(openJobSeekerSignUp);
            finish();

        }else if(id == R.id.privacy_policy){

            Toasty.info(Company_SearchBoard.this, "Coming soon!", Toast.LENGTH_LONG, true).show();

        }else if(id == R.id.log_out){

            SharedPreferences.Editor editor = getSharedPreferences("CompanyData", MODE_PRIVATE).edit();
            editor.putString("userid", "");
            editor.apply();

            SharedPreferences.Editor typeEditor = getSharedPreferences("UserType", MODE_PRIVATE).edit();
            typeEditor.putInt("type", 0);
            typeEditor.apply();


            Intent openJobSeekerSignUp = new Intent(Company_SearchBoard.this, Intro.class);
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
                JSONObject jobSeekerModel = jobj.optJSONObject("companyModel");



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
                String fullName = jobSeekerModel.optString("companyName");
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

    private String modifyTitle(String st){

        String temp = "Welcome ";

        if(!st.equalsIgnoreCase("") && st != null){

            for(int i=0; i<= st.length(); i++){

                if(st.charAt(i) == ' '){
                    break;
                }else {

                    temp = temp + st.charAt(i);
                }
            }
        }

        return  temp;
    }



    //----------------------
}


