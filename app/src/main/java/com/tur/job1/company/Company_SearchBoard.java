package com.tur.job1.company;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.tur.job1.adapters.SearchResultExample;
import com.tur.job1.adapters.SkillsSetAdapter;
import com.tur.job1.job_seeker.Job_Seeker_Dashboard;
import com.tur.job1.job_seeker.Job_Seeker_Verify_1;
import com.tur.job1.others.Connectivity;
import com.tur.job1.others.ConstantsHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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



    String[] professions ={"Android Developer","Java","Software Engineer","3D Artist","2D Artist","Marketing Assitant","Human Resource"};
    private AutoCompleteTextView autoCompleteTextView;

    private Dialog dialog;

    ArrayList<Integer> jobSeekerId = new ArrayList<>();
    ArrayList<String> jobSeekerPhotoUrl = new ArrayList<>();
    ArrayList<String> jobSeekerName = new ArrayList<>();
    ArrayList<String> jobSeekerDesignation = new ArrayList<>();
    ArrayList<Integer> jobSeekerExperience = new ArrayList<>();
    ArrayList<Integer> jobSeekerExpectedSalary = new ArrayList<>();

    private LinearLayout panel_1_normal_search;
    private LinearLayout panel_2_search_window;
    private ListView searchView;


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
        autoCompleteTextView = (AutoCompleteTextView)findViewById(R.id.autoCompleteTextView2);

        panel_1_normal_search = (LinearLayout)findViewById(R.id.panel_1_normal_search);
        panel_2_search_window = (LinearLayout)findViewById(R.id.panel_2_search_window);
        searchView = (ListView)findViewById(R.id.searchView);




        readyProfesionBox(autoCompleteTextView);




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

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos,
                                    long id) {

                //Toast.makeText(Company_SearchBoard.this," selected", Toast.LENGTH_LONG).show();
                showSearchResult(autoCompleteTextView.getText().toString(),0,20);


            }
        });








    }

    private void showSearchResult(String profession, int increamentor,int size) {


        showLoadingBarAlert();

        JSONObject parameters = new JSONObject();
        try {
            parameters.put("skill", profession);
            parameters.put("index", increamentor);
            parameters.put("size", size);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(TAG,parameters.toString());

        RequestQueue rq = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, ConstantsHolder.rawServer+ConstantsHolder.basicSearch, parameters, new com.android.volley.Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String respo=response.toString();
                        Log.d(TAG,respo);

                        Log.d("5555",respo);



                        if(response.toString().contains("\"status\": 500,")){

                            Toasty.error(Company_SearchBoard.this, "Server error,please check your internet connection!", Toast.LENGTH_LONG, true).show();
                            hideLoadingBar();

                        }else {

                            parseBasicSearchData(response);
                        }



                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Toasty.error(Company_SearchBoard.this, "Server error,please check your internet connection!", Toast.LENGTH_LONG, true).show();
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

    private void parseBasicSearchData(JSONObject jobj){

        if(jobj != null){

            int totalElements =jobj.optInt("totalElements");
            if(totalElements < 1) {

                Toasty.info(Company_SearchBoard.this, "No result found! Please try later.", Toast.LENGTH_LONG, true).show();
                //panel_1_normal_search.getLayoutParams().height = 0;

            }else {

                //--

                // Getting the Job Seeker info
                //JSONObject skills = jobj.optJSONObject("skills");
                try {
                    JSONArray skills = jobj.getJSONArray("content");

                    // Log.d(TAG,skills.toString());
                    for(int i=0; i<skills.length(); i++){

                        JSONObject listData = skills.getJSONObject(i);

                        jobSeekerId.add(listData.optInt("id"));
                        jobSeekerPhotoUrl.add(listData.optString("photoUrl"));
                        jobSeekerName.add(listData.optString("fullName"));
                        jobSeekerDesignation.add(listData.optString("fullName"));
                        jobSeekerExperience.add(listData.optInt("experience"));
                        jobSeekerExpectedSalary.add(listData.optInt("expectedSalary"));

                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }



                panel_1_normal_search.getLayoutParams().height = 0;
                searchView.setAdapter(new SearchResultExample(this,jobSeekerId.size(),jobSeekerId,jobSeekerPhotoUrl,jobSeekerName,jobSeekerDesignation,jobSeekerExperience,jobSeekerExpectedSalary));

                //-------------------


            }




            hideLoadingBar();





            }else {
                // Go to Login
            }
        }





    private void showLoadingBarAlert(){


        dialog = new Dialog(Company_SearchBoard.this);

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

    private void readyProfesionBox(AutoCompleteTextView autoCompleteTextView) {

        //Creating the instance of ArrayAdapter containing list of language names
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this,android.R.layout.select_dialog_item,professions);
        //Getting the instance of AutoCompleteTextView
        //AutoCompleteTextView actv =  (AutoCompleteTextView)findViewById(R.id.autoCompleteTextView);
        autoCompleteTextView.setThreshold(1);//will start working from first character
        autoCompleteTextView.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        //autoCompleteTextView.setTextColor(Color.RED);
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


