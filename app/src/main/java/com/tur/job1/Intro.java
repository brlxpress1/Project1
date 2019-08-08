package com.tur.job1;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.Toast;

import com.tur.job1.job_seeker.Job_Seeker_CV_Upload;
import com.tur.job1.job_seeker.Job_Seeker_Dashboard;
import com.tur.job1.job_seeker.Job_Seeker_Verify_1;
import com.tur.job1.others.Connectivity;
import com.tur.job1.others.Skill_Selector;

import es.dmoral.toasty.Toasty;


public class Intro extends AppCompatActivity {

    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.6F);

    private String TAG = "Intro";
    private Button hire_now;
    private Button find_job;
    private boolean isConnectedToNet;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        hire_now = (Button) findViewById(R.id.hire_now_button);
        find_job = (Button) findViewById(R.id.find_job_button);

        //-- other call

        //----------

        hire_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                hire_now.startAnimation(buttonClick);



            }
        });

        find_job.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                find_job.startAnimation(buttonClick);

                if (Connectivity.isConnected(Intro.this)) {


                    Intent openJobSeekerSignUp = new Intent(Intro.this, Job_Seeker_Verify_1.class);
                    startActivity(openJobSeekerSignUp);
                    finish();


                } else {

                    Toasty.error(Intro.this, "You have no internet access! Please turn on your WiFi or mobile data.", Toast.LENGTH_LONG, true).show();

                }
            }
        });
    }
}



