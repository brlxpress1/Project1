package com.tur.job1.others;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tur.job1.models.UploadFileResponse;

import org.json.JSONObject;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface FileUploadService {
    // declare a description explicitly
    // would need to declare
    @Multipart
    @POST("upload")
    Call<ResponseBody> uploadFile(
            @Part("description") RequestBody description,
            @Part MultipartBody.Part file);

    @Multipart
    @POST("upload")
    Call<ResponseBody> uploadFileWithPartMap(
            @PartMap() Map<String, RequestBody> partMap,
            @Part MultipartBody.Part file);

    @Multipart
    @POST("uploadFile")
    Call<UploadFileResponse> uploadImageWithId(
            @Part MultipartBody.Part file,
            @Query("userId") int userId,
            @Query("fileType") String fileType
            );
}

