package com.example.helloworld;

import android.app.ProgressDialog;
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
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Multipart;

public class UploadAsyncTask extends AsyncTask<String,String,String> {
    private static final String host = "http://10.0.2.2:5000/";
    private String filePath;
    private String fileCategory;
    private ProgressDialog progressDialog;
    public UploadAsyncTask(String filePath, String fileCategory,ProgressDialog progressDialog) {
        this.filePath = filePath;
        this.fileCategory = fileCategory;
        this.progressDialog = progressDialog;
    }

    @Override
    protected void onPreExecute(){
        super.onPreExecute();
        // display a progress dialog for good user experiance
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... strings) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(host)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        File image = new File(this.filePath);
        RequestBody imageBody = RequestBody.create(image,MediaType.parse("image/*"));
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("image",image.getName(),imageBody);
        RequestBody textRequest = RequestBody.create("category",MediaType.parse("text/plain"));
        MultipartBody.Part textPart =  MultipartBody.Part.createFormData("category",this.fileCategory);
        UploadService service = retrofit.create(UploadService.class);
        Call<UploadResponse> response = service.startUpload(filePart, textPart);
        response.enqueue(new Callback<UploadResponse>() {
            @Override
            public void onResponse(Call<UploadResponse> call, Response<UploadResponse> response) {
                System.out.println("Successfully uploaded");
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<UploadResponse> call, Throwable t) {
                System.out.println("Failed to upload");
                progressDialog.dismiss();
            }
        });
        return "Completed";
    }
}
