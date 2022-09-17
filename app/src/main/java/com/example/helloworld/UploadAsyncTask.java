package com.example.helloworld;

import android.os.AsyncTask;

import com.example.helloworld.pojo.UploadResponse;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Multipart;

public class UploadAsyncTask extends AsyncTask<String,String,String> {
    private static final String host = "http://127.0.0.1:5000/image/store";
    private String filePath;

    public UploadAsyncTask(String filePath) {
        this.filePath = filePath;
    }

    @Override
    protected String doInBackground(String... strings) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(host).build();
        File image = new File(this.filePath);
        RequestBody imageBody = RequestBody.create(image,MediaType.parse("image/*"));
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("image",image.getName(),imageBody);
        RequestBody textRequest = RequestBody.create("category",MediaType.parse("text/plain"));
        MultipartBody.Part textPart =  MultipartBody.Part.createFormData("category","test");
        UploadService service = retrofit.create(UploadService.class);
        Call<UploadResponse> response = service.startUpload(filePart, textPart);
        response.enqueue(new Callback<UploadResponse>() {
            @Override
            public void onResponse(Call<UploadResponse> call, Response<UploadResponse> response) {
                System.out.println("Successfully uploaded");
            }

            @Override
            public void onFailure(Call<UploadResponse> call, Throwable t) {
                System.out.println("Failed to upload");
            }
        });
        return "Completed";
    }
}
