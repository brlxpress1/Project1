package com.tur.job1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.tur.job1.company.Company_Dashboard;
import com.tur.job1.company.Company_Signup_1;
import com.tur.job1.job_seeker.Job_Seeker_Dashboard;
import com.tur.job1.job_seeker.Job_Seeker_Verify_1;
import com.tur.job1.others.Connectivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import es.dmoral.toasty.Toasty;

public class Splash extends AppCompatActivity {

    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.6F);

    private String TAG = "Splash";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (Connectivity.isConnected(Splash.this)) {



            SharedPreferences prefs1 = getSharedPreferences("UserType", MODE_PRIVATE);
            int userType = prefs1.getInt("type", 0);
            Log.d(TAG,"Logging with user type : "+ String.valueOf(userType));

            if(userType == 1){

                //--
                SharedPreferences prefs = getSharedPreferences("CompanyData", MODE_PRIVATE);
                String userIdLocal = prefs.getString("userid", "");
                Log.d(TAG,userIdLocal);

                if(userIdLocal != null && !userIdLocal.equalsIgnoreCase("")){

                    Intent openJobSeekerSignUp = new Intent(Splash.this, Company_Dashboard.class);
                    startActivity(openJobSeekerSignUp);
                    finish();

                }else {

                    Intent openJobSeekerSignUp = new Intent(Splash.this, Company_Signup_1.class);
                    startActivity(openJobSeekerSignUp);
                    finish();
                }
                //-------------

            }else if(userType == 2){

                //--
                SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
                String userIdLocal = prefs.getString("userid", "");
                Log.d(TAG,userIdLocal);

                if(userIdLocal != null && !userIdLocal.equalsIgnoreCase("")){

                    Intent openJobSeekerSignUp = new Intent(Splash.this, Job_Seeker_Dashboard.class);
                    startActivity(openJobSeekerSignUp);
                    finish();

                }else {

                    Intent openJobSeekerSignUp = new Intent(Splash.this, Job_Seeker_Verify_1.class);
                    startActivity(openJobSeekerSignUp);
                    finish();
                }
                //-------------

            }else {


                Intent openJobSeekerSignUp = new Intent(Splash.this, Intro.class);
                startActivity(openJobSeekerSignUp);
                finish();

            }





        } else {

            //Toasty.error(Splash.this, "You have no internet access! Please turn on your WiFi or mobile data.", Toast.LENGTH_LONG, true).show();
            showErrorDialogue();

        }


    }

    public void showErrorDialogue(){

        new MaterialStyledDialog.Builder(this)
                .setIcon(R.drawable.error_custom)
                .setHeaderColor(R.color.error_red)
                .setTitle("Error!")
                .setDescription("Seems like you don't have internet access. Please turn on your WiFi/Data & press 'Refresh' button.")
                .setPositiveText("Refresh")
                .setCancelable(false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                Intent openSecondVerifier = new Intent(Splash.this,Splash.class);
                startActivity(openSecondVerifier);
                finish();
            }
            })
                .show();
    }
}


