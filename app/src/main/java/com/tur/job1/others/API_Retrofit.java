package com.tur.job1.others;


import com.tur.job1.models.DateResponse;
import com.tur.job1.models.LoginInformationResponse;
import com.tur.job1.models.PhoneNumberCheck;
import com.tur.job1.models.SignUpResponse;
import com.tur.job1.models.UploadFileResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface API_Retrofit {


    // User exist or not check
    @Headers("Content-Type: application/json")
    @POST("UserCheck")
    Call<PhoneNumberCheck> userExistCheck(
            @Body JSONObject rawJson);


    // Registration api for both job seeker & company
    @Headers("Content-Type: application/json")
    @POST("UserSignUp")
    Call<SignUpResponse> signUpJobSeeker(
            @Body JSONObject rawJson);

    // Registration api for both job seeker & company
    @Headers("Content-Type: application/json")
    @POST("FindUserDetails")
    Call<LoginInformationResponse> fetchUserData(
            @Body JSONObject rawJson);


    //
    @Headers("Content-Type: application/json")
    @POST("UserDetails/JobSeeker/BirthDayUpdate")
    Call<DateResponse> updateBirthDate(
            @Body JSONObject rawJson);


}


