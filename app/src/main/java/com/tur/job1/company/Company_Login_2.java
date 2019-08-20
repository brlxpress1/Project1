package com.tur.job1.company;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.tur.job1.R;
import com.tur.job1.job_seeker.Job_Seeker_Dashboard;
import com.tur.job1.job_seeker.Job_Seeker_Login;
import com.tur.job1.job_seeker.Job_Seeker_Login_Verify;
import com.tur.job1.others.Connectivity;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import es.dmoral.toasty.Toasty;

public class Company_Login_2 extends AppCompatActivity {

    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.6F);
    private String TAG = "Company_Login_2";


    private Dialog dialog;
    private EditText otpCodeInput;
    private Button verifyButton;
    private TextView resendButton;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks

            verificationCallbacks;
    private PhoneAuthCredential credential;

    private String otpID;
    private FirebaseAuth fbAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_login_2);


        otpCodeInput = (EditText)findViewById(R.id.otp_code_input);
        verifyButton = (Button)findViewById(R.id.verify_button);
        resendButton = (TextView)findViewById(R.id.resend_button);


        //-- Changing selection effect of input fields
        otpCodeInput.setOnFocusChangeListener((view, b) -> {
            if (view.isFocused()) {
                // Do whatever you want when the EditText is focused
                // Example:
                //editTextFrom.setText("Focused!");
                otpCodeInput.setBackgroundResource(R.drawable.edittext_box_border);
            }
            else{
                otpCodeInput.setBackgroundResource(R.drawable.shape_with_border1);
            }
        });



        //-----------------




        fbAuth = FirebaseAuth.getInstance();

        showLoadingBarAlert();

        SharedPreferences prefs = getSharedPreferences("CompanyData", MODE_PRIVATE);

        String userPhone = prefs.getString("userphone", "");


        send_otp_by_firebase(userPhone);

        resendButton.setText("Did not get the OTP code? "+ Html.fromHtml("<p><u>Resend</u></p>"));

        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                verifyButton.startAnimation(buttonClick);

                if (Connectivity.isConnected(Company_Login_2.this)) {


                    verifyCode();

                } else {

                    Toasty.error(Company_Login_2.this, "You have no internet access! Please turn on your WiFi or mobile data.", Toast.LENGTH_LONG, true).show();

                }

            }
        });

        resendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                resendButton.startAnimation(buttonClick);

                if (Connectivity.isConnected(Company_Login_2.this)) {


                    resendCode();

                } else {

                    Toasty.error(Company_Login_2.this, "You have no internet access! Please turn on your WiFi or mobile data.", Toast.LENGTH_LONG, true).show();

                }

            }
        });




    }

    private void showLoadingBarAlert(){


        dialog = new Dialog(Company_Login_2.this);

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

    //-- OTP related task

    //--

    private void send_otp_by_firebase(String number){


        SharedPreferences prefs = getSharedPreferences("CompanyData", MODE_PRIVATE);
        //Log.d(TAG,prefs.getString("userid", "null"));

        String userName = prefs.getString("username", "");
        String userPhone = prefs.getString("userphone", "");
        Log.d(TAG,"From shared preference : "+userName+"--------------"+userPhone);

        if(userName.equalsIgnoreCase("")){

            // Go to sign up page
        }else {

            if(userPhone.equalsIgnoreCase("")){

                // Go to sign up page
            }else {


                //registerUser(userName,userPhone);



                verificationCallBack();

                PhoneAuthProvider.getInstance().verifyPhoneNumber(

                        number,        // Phone number to verify

                        60,                 // Timeout duration

                        TimeUnit.SECONDS,   // Unit of timeout

                        this,               // Activity (for callback binding)

                        verificationCallbacks);





                //--


            }
        }







    }



    private void verificationCallBack(){



        verificationCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {



            @Override

            public void onVerificationCompleted(PhoneAuthCredential credential) {

                // This callback will be invoked in two situations:

                // 1 - Instant verification. In some cases the phone number can be instantly

                //     verified without needing to send or enter a verification code.

                // 2 - Auto-retrieval. On some devices Google Play services can automatically

                //     detect the incoming verification SMS and perform verification without

                //     user action.

                Log.d(TAG, "onVerificationCompleted:" + credential);



                //credential = PhoneAuthProvider.getCredential(verificationId, code);

                signInWithPhoneAuthCredential(credential);

            }



            @Override

            public void onVerificationFailed(FirebaseException e) {

                // This callback is invoked in an invalid request for verification is made,

                // for instance if the the phone number format is not valid.

                Log.w(TAG, "onVerificationFailed", e);



                //Toast.makeText(Verification.this, "Enter Correct Number.", Toast.LENGTH_SHORT).show();

                Toasty.error(Company_Login_2.this, "Enter a valid phone number!", Toast.LENGTH_LONG, true).show();


                SharedPreferences.Editor editor = getSharedPreferences("CompanyData", MODE_PRIVATE).edit();
                editor.putString("userid", "");
                //editor.putString("userphone", userPhone);
                editor.apply();

                Intent openSecondVerifier = new Intent(Company_Login_2.this, Job_Seeker_Login.class);
                startActivity(openSecondVerifier);
                finish();



                if (e instanceof FirebaseAuthInvalidCredentialsException) {

                    // Invalid request

                    // ...

                } else if (e instanceof FirebaseTooManyRequestsException) {

                    // The SMS quota for the project has been exceeded

                    // ...

                }



                // Show a message and update the UI

                // ...

            }



            @Override
            public void onCodeSent(String verificationId,

                                   PhoneAuthProvider.ForceResendingToken token) {

                // The SMS verification code has been sent to the provided phone number, we

                // now need to ask the user to enter the code and then construct a credential

                // by combining the code with a verification ID.

                Log.d(TAG, "onCodeSent:" + verificationId);



                // Save verification ID and resending token so we can use them later



                Log.d(TAG,verificationId+"------------"+token);

                otpID = verificationId;

                Toasty.info(Company_Login_2.this, "OTP code sent to your phone number, please enter the code.", Toast.LENGTH_LONG, true).show();
                hideLoadingBar();






                // ...

            }

        };

    }






    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        fbAuth.signInWithCredential(credential)

                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override

                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information

                            Log.d(TAG, "signInWithCredential:success");



                            //--
                            Intent openSecondVerifier = new Intent(Company_Login_2.this,Company_Dashboard.class);
                            startActivity(openSecondVerifier);
                            finish();

                            //---------------------

                            // registerUser(userNameLocal,userPhoneLocal);





                        } else {

                            // Sign in failed, display a message and update the UI

                            Log.w(TAG, "signInWithCredential:failure", task.getException());

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {

                                // The verification code entered was invalid

                                //Toast.makeText(Verification.this, "Verification code is invalid!", Toast.LENGTH_LONG).show();

                                Toasty.error(Company_Login_2.this, "OTP code is invalid! Please enter correct OTP code.", Toast.LENGTH_LONG, true).show();
                                hideLoadingBar();





                            }

                        }

                    }

                });

    }



    private void verifyCode(){


        //showLoadingBarAlert();
        String temp = otpCodeInput.getText().toString().trim();

        if(temp.equals("")){



            //Toast.makeText(Verification.this, "You must enter the verification code!", Toast.LENGTH_SHORT).show();

            Toasty.error(Company_Login_2.this, "Enter the verification code first!", Toast.LENGTH_LONG, true).show();



        }else {



            showLoadingBarAlert();



            credential = PhoneAuthProvider.getCredential(otpID, temp);

            signInWithPhoneAuthCredential(credential);

        }



    }



    public void resendCode() {



        SharedPreferences prefs = getSharedPreferences("CompanyData", MODE_PRIVATE);
        //Log.d(TAG,prefs.getString("userid", ""));


        String userPhone = prefs.getString("userphone", "");

        String temp = userPhone;



        if(temp.equalsIgnoreCase("")){

        }else {


            //Toasty.success(Job_Seeker_Login_Verify.this, "OTP code was sent to your phone number, please check.", Toast.LENGTH_LONG, true).show();

            send_otp_by_firebase(temp);



        }

    }



    //-------------------------






    //---------------

    @Override
    public void onBackPressed() {



        Intent openSecondVerifier = new Intent(Company_Login_2.this,Company_Login_1.class);
        startActivity(openSecondVerifier);
        finish();

    }


}

