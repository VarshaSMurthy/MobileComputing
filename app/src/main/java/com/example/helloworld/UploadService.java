package com.example.helloworld;

import com.example.helloworld.pojo.UploadResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UploadService {
    @Multipart
    @POST("image/store")
    Call<UploadResponse> startUpload(@Part("image") MultipartBody.Part photo, @Part("category") MultipartBody.Part category);
}
