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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
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
import com.bumptech.glide.request.RequestOptions;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.google.android.material.navigation.NavigationView;
import com.tur.job1.Intro;
import com.tur.job1.R;
import com.tur.job1.Splash;
import com.tur.job1.adapters.SearchResultExample;
import com.tur.job1.adapters.SkillsSetAdapter;
import com.tur.job1.job_seeker.Job_Seeker_Dashboard;
import com.tur.job1.job_seeker.Job_Seeker_Modified_Dashboard;
import com.tur.job1.job_seeker.Job_Seeker_Verify_1;
import com.tur.job1.job_seeker.Job_Seeker_Verify_2;
import com.tur.job1.others.Connectivity;
import com.tur.job1.others.ConstantsHolder;
import com.tur.job1.others.Dialogue_Helper;
import com.tur.job1.others.Skill_Selector;

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
    ArrayList<String> jobSeekerSkillSet = new ArrayList<>();

    ArrayList<String> jobSeekerCVUrl = new ArrayList<>();

    private LinearLayout panel_1_normal_search;
    private LinearLayout panel_2_search_window;
    private LinearLayout panel_3_advanceSearchWindow;
    private ListView searchView;

    private int backButtonBehaviour = 0;

    ArrayList<String> skillIdList = new ArrayList<String>();
    ArrayList<String> skillNameList = new ArrayList<String>();

    private LinearLayout drawerOpener;
    private LinearLayout basicSearchClick;

    private DrawerLayout drawer_layout;

    private Button basic_search_option;
    private Button advance_search_option;
    private TextView search_title_name;

    private AutoCompleteTextView secondarySearchBox;
    private EditText salaryBox;
    private Spinner genderBox;
    private EditText ageBox;
    private EditText experienceBox;
    private EditText locationBox;
    private Button secondarySearchClick;
    private LinearLayout gender_input2;


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
        panel_3_advanceSearchWindow = (LinearLayout)findViewById(R.id.panel_3_advanced_filters);
        searchView = (ListView)findViewById(R.id.searchView);

        drawerOpener = (LinearLayout)findViewById(R.id.draweropener);
        basicSearchClick = (LinearLayout)findViewById(R.id.basicSearchButton);
        drawer_layout = (DrawerLayout)findViewById(R.id.drawer_layout);

        basic_search_option = (Button)findViewById(R.id.basic_search_option);
        advance_search_option = (Button)findViewById(R.id.advance_search_option);
        search_title_name = (TextView)findViewById(R.id.search_title_name);



        secondarySearchBox = (AutoCompleteTextView)findViewById(R.id.secondarySearchBox);
        salaryBox = (EditText)findViewById(R.id.salaryBox);
        genderBox = (Spinner)findViewById(R.id.genderbox);
        ageBox = (EditText)findViewById(R.id.ageBox);
        experienceBox = (EditText)findViewById(R.id.expBox);
        locationBox= (EditText)findViewById(R.id.locationBox);
        secondarySearchClick = (Button)findViewById(R.id.searchbutton);
        gender_input2 = (LinearLayout)findViewById(R.id.gender_input);




        //readyProfesionBox(autoCompleteTextView);
        fetch_skill_list();




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


        avatarPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                avatarPhoto.startAnimation(buttonClick);

                Intent openJobSeekerSignUp = new Intent(Company_SearchBoard.this, Company_Dashboard.class);
                startActivity(openJobSeekerSignUp);
                finish();



            }
        });

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos,
                                    long id) {

                //Toast.makeText(Company_SearchBoard.this," selected", Toast.LENGTH_LONG).show();
                showSearchResult(autoCompleteTextView.getText().toString(),0,100);
                //Log.d(TAG,autoCompleteTextView.getText().toString());


            }
        });

        basicSearchClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                basicSearchClick.startAnimation(buttonClick);
                showSearchResult(autoCompleteTextView.getText().toString(),0,100);

            }
        });

        drawerOpener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                drawer_layout.openDrawer(Gravity.LEFT);
            }
        });


        basic_search_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                showBasicSearchWindow();
            }
        });

        advance_search_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                showAdvanceSearchWindow();
            }
        });

        showBasicSearchWindow();


        //------------


        gender_input2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                genderBox.performClick();
            }
        });

        locationBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openLocationInput(locationBox);

            }
        });


        secondarySearchClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                secondarySearchClick.startAnimation(buttonClick);

                // to do task
                String s1 = secondarySearchBox.getText().toString();

                if(s1.equalsIgnoreCase("") || s1 == null){

                    Toasty.error(Company_SearchBoard.this, "Please choose a skill first!", Toast.LENGTH_LONG, true).show();

                }else {


                    String tempSalary = salaryBox.getText().toString().trim();
                    if(tempSalary.equalsIgnoreCase("") || tempSalary == null){

                        tempSalary = "100000";

                    }

                    String tempGender = genderBox.getSelectedItem().toString();
                    if(tempGender .equalsIgnoreCase("") || tempGender  == null || tempGender.equalsIgnoreCase("All")){

                        tempGender = "";

                    }

                    String tempAge = ageBox.getText().toString().trim();
                    if(tempAge .equalsIgnoreCase("") || tempAge  == null){

                        tempAge  = "10";
                    }

                    String tempExperience = experienceBox.getText().toString().trim();
                    if(tempExperience .equalsIgnoreCase("") || tempExperience  == null){

                        tempExperience  = "0";
                    }


                    String tempLocation = locationBox.getText().toString().trim();
                    if(tempLocation.equalsIgnoreCase("") || tempLocation == null){

                        tempLocation = "";
                    }

                    showSearchResult2(0,tempAge,tempLocation,s1,tempExperience,tempSalary,tempGender);


                }
            }
        });






    }

    public void openLocationInput(EditText editText){

        Dialogue_Helper dh = new Dialogue_Helper();
        dh.showLocationSearch(this,editText,this);
    }


    private void showSearchResult(String profession, int increamentor,int size) {


        showLoadingBarAlert();

        if(increamentor == 0){

            jobSeekerId.clear();
            jobSeekerPhotoUrl.clear();
            jobSeekerName.clear();
            jobSeekerDesignation.clear();
            jobSeekerExperience.clear();
            jobSeekerExpectedSalary.clear();
            jobSeekerCVUrl.clear();
            jobSeekerSkillSet.clear();
        }

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

                        Log.d(TAG,respo);



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
                        jobSeekerCVUrl.add(listData.optString("cvUrl"));

                        parseSkillSetFromJsonArray(listData.getJSONArray("skillsList"));

                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }



                backButtonBehaviour = 1;
                search_title_name.setText(autoCompleteTextView.getText().toString());
                // panel_1_normal_search.getLayoutParams().height = 0;
                //searchView.setAdapter(null);
                showSearchResultWindow();
                searchView.setAdapter(new SearchResultExample(this,jobSeekerId.size(),jobSeekerId,jobSeekerPhotoUrl,jobSeekerName,jobSeekerDesignation,jobSeekerExperience,jobSeekerExpectedSalary));

                //-------------------


            }




            hideLoadingBar();





        }else {
            // Go to Login
        }
    }

    private void parseSkillSetFromJsonArray(JSONArray sd) {

        String tempS = "";


        if(sd == null || sd.toString().equalsIgnoreCase("null") || sd.toString().equalsIgnoreCase("")){

            tempS = "N/A";
        }else {



            try {
               // JSONArray skills = sd;

                // Log.d(TAG,skills.toString());
                for(int i=0; i<sd.length(); i++){

                    JSONObject listData = sd.getJSONObject(i);

                   // tempS =  tempS +",\n"+skillNameList.get(listData.optInt("skillId"));
                    if(i > 0){

                        tempS = tempS +","+ skillNameList.get(listData.optInt("skillId") - 1);

                    }else {

                        tempS = skillNameList.get(listData.optInt("skillId") - 1);
                    }






                }






            } catch (JSONException e) {
                e.printStackTrace();
            }



        }


        Log.d(TAG, tempS);
        jobSeekerSkillSet.add(tempS);
        //Log.d("5555",sd.toString());




    }

    private void showSearchResult2(int increamentor,String age, String location, String skill, String experience, String salary, String gender) {


        showLoadingBarAlert();

        if(increamentor == 0){

            jobSeekerId.clear();
            jobSeekerPhotoUrl.clear();
            jobSeekerName.clear();
            jobSeekerDesignation.clear();
            jobSeekerExperience.clear();
            jobSeekerExpectedSalary.clear();
            jobSeekerCVUrl.clear();
            jobSeekerSkillSet.clear();
        }




        JSONObject parameters = new JSONObject();
        try {
            parameters.put("age", Integer.parseInt(age));
            parameters.put("location", location);
            parameters.put("skills", skill);
            parameters.put("experince", Integer.parseInt( experience));
            parameters.put("salary", Integer.parseInt(salary));
            parameters.put("gender", gender);



        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(TAG,parameters.toString());

        RequestQueue rq = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, ConstantsHolder.rawServer+ConstantsHolder.advanceSearch, parameters, new com.android.volley.Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String respo=response.toString();
                        Log.d(TAG,respo);

                        Log.d(TAG,respo);



                        if(response.toString().contains("\"status\": 500,")){

                            Toasty.error(Company_SearchBoard.this, "Server error,please check your internet connection!", Toast.LENGTH_LONG, true).show();
                            hideLoadingBar();

                        }else {

                            parseAdvanceSearchData(response);
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

    private void parseAdvanceSearchData(JSONObject jobj){



        if(jobj != null){

            int totalElements =jobj.optInt("totalElement");
            if(totalElements < 1) {

                Toasty.info(Company_SearchBoard.this, "No result found! Please try later.", Toast.LENGTH_LONG, true).show();
                //panel_1_normal_search.getLayoutParams().height = 0;

            }else {

                //--

                // Getting the Job Seeker info
                //JSONObject skills = jobj.optJSONObject("skills");
                try {
                    JSONArray skills = jobj.getJSONArray("jobsSeekers");
                    //Log.d("5555",skills.toString());

                    // Log.d(TAG,skills.toString());
                    for(int i=0; i<skills.length(); i++){

                        JSONObject listData = skills.getJSONObject(i);

                        jobSeekerId.add(listData.optInt("id"));
                        jobSeekerPhotoUrl.add(listData.optString("photoUrl"));
                        jobSeekerName.add(listData.optString("fullName"));
                        jobSeekerDesignation.add(listData.optString("fullName"));
                        jobSeekerExperience.add(listData.optInt("experience"));
                        jobSeekerExpectedSalary.add(listData.optInt("expectedSalary"));
                        jobSeekerCVUrl.add(listData.optString("cvUrl"));
                        parseSkillSetFromJsonArray(listData.getJSONArray("skillsList"));

                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }



                backButtonBehaviour = 2;
                search_title_name.setText(autoCompleteTextView.getText().toString());
                // panel_1_normal_search.getLayoutParams().height = 0;
                //searchView.setAdapter(null);
                showSearchResultWindow();
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

    private void readyProfesionBox(AutoCompleteTextView autoCompleteTextView ) {

        //Creating the instance of ArrayAdapter containing list of language names
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this,android.R.layout.select_dialog_item,professions);
        //Getting the instance of AutoCompleteTextView
        //AutoCompleteTextView actv =  (AutoCompleteTextView)findViewById(R.id.autoCompleteTextView);
        autoCompleteTextView.setThreshold(1);//will start working from first character
        autoCompleteTextView.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        //autoCompleteTextView.setTextColor(Color.RED);
    }

    private void readyProfesionBox2(AutoCompleteTextView autoCompleteTextView ) {

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

/*
                    Glide.with(this)
                            .load(photoUrl)
                            .centerCrop()
                            .placeholder(R.drawable.default_avatar)
                            .into(avatarPhoto);
                            */

                    Glide
                            .with(this)
                            .load(photoUrl)
                            .apply(new RequestOptions()
                                    .placeholder(R.drawable.default_avatar)
                                    .fitCenter())
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


    private void showBasicSearchWindow(){

        panel_1_normal_search.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        panel_2_search_window.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
        panel_3_advanceSearchWindow.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
    }

    private void showSearchResultWindow(){

        panel_1_normal_search.setLayoutParams(new LinearLayout.LayoutParams(0,0));
        panel_2_search_window.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        panel_3_advanceSearchWindow.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
    }

    private void showAdvanceSearchWindow(){

        panel_1_normal_search.setLayoutParams(new LinearLayout.LayoutParams(0,0));
        panel_2_search_window.setLayoutParams(new LinearLayout.LayoutParams(0,0));
        panel_3_advanceSearchWindow.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
    }

    //-----------------

    //--
    private void fetch_skill_list() {

        showLoadingBarAlert();



        RequestQueue rq = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, ConstantsHolder.rawServer+ConstantsHolder.fetchSkillList, null, new com.android.volley.Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        String respo=response.toString();
                        Log.d(TAG,respo);

                        //Log.d(TAG,respo);


                        parseSkillListData(response);



                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Toasty.error(Company_SearchBoard.this, "Server error,please check your internet connection!", Toast.LENGTH_LONG, true).show();
                        //Toast.makeText(Login_A.this, "Something wrong with Api", Toast.LENGTH_SHORT).show();

                    }
                });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rq.getCache().clear();
        rq.add(jsonObjectRequest);

        //-----------------

    }

    private void parseSkillListData(JSONObject jobj){

        skillIdList.clear();
        skillNameList.clear();

        //skillIdList.add("0");
        //skillNameList.add("Select a skill...");

        if(jobj != null){

            int status =jobj.optInt("status");
            if(status == 200){

                // Getting the Job Seeker info
                //JSONObject skills = jobj.optJSONObject("skills");
                try {
                    JSONArray skills = jobj.getJSONArray("skills");

                    // Log.d(TAG,skills.toString());
                    for(int i=0; i<skills.length(); i++){

                        JSONObject listData = skills.getJSONObject(i);



                        String id =  listData.optString("id");
                        String name =  listData.optString("name");
                        Log.d(TAG,id+"------------"+name);

                        skillIdList.add(id);
                        skillNameList.add(name);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                professions = skillNameList.toArray(new String[skillNameList.size()]);
                readyProfesionBox(autoCompleteTextView);
                readyProfesionBox2(secondarySearchBox);

                hideLoadingBar();


            }else {
                // Go to Login
            }
        }else {

            // Go to Login
        }




    }

    public void searchItemClick(int position, LinearLayout linearLayout,int userID, String photoUrl, String userName, String email, String experience, String expectedSalary){

       // linearLayout.startAnimation(buttonClick);
        SharedPreferences.Editor editor = getSharedPreferences("UserDetailsData", MODE_PRIVATE).edit();
        editor.putString("userid", String.valueOf(userID));
        editor.putString("photourl", photoUrl);
        editor.putString("username", userName);
        editor.putString("email", email);
        editor.putString("experience", experience);
        editor.putString("expectedsalary", expectedSalary);
        editor.putString("cvurl", jobSeekerCVUrl.get(position));
        editor.putString("skillset", jobSeekerSkillSet.get(position));
        editor.apply();

        Intent openSecondVerifier = new Intent(Company_SearchBoard.this,Employee_Details.class);
        startActivity(openSecondVerifier);
        //finish();




    }



    //-----------------

    @Override
    public void onBackPressed() {



        if(backButtonBehaviour == 1){

            showBasicSearchWindow();
            backButtonBehaviour = 0;

        }else if(backButtonBehaviour == 2){

            //to do
           showSearchResultWindow();
            backButtonBehaviour = 1;

        }else if(backButtonBehaviour == 0 ){

            //to do
            showExitDialogue();

        }

        //Toast.makeText(this,String.valueOf(backButtonBehaviour),Toast.LENGTH_LONG).show();
    }

    public void showExitDialogue(){

        new MaterialStyledDialog.Builder(this)
                .setIcon(R.drawable.logout_icon)
                .setHeaderColor(R.color.error_red)
                .setTitle("Exit?")
                .setDescription("Do you want to exit from this app?")

                .setCancelable(false)
                .setPositiveText("Exit")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {


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


}


