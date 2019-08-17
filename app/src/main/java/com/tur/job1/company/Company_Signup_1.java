package com.tur.job1.company;

import android.os.Bundle;
import android.view.animation.AlphaAnimation;

import com.tur.job1.R;

import androidx.appcompat.app.AppCompatActivity;

public class Company_Signup_1 extends AppCompatActivity {

    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.6F);

    private String TAG = "Company_Signup_1";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_signup_1);


    }
}