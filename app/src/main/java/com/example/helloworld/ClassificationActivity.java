package com.example.helloworld;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.helloworld.pojo.UploadResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClassificationActivity extends AppCompatActivity {

    Button uploadService;
    private static final String host = "http://192.168.0.214:5000/";
    Bitmap bmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Bundle extras = getIntent().getExtras();
        byte[] byteArray = extras.getByteArray("image");

        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        ImageView image = (ImageView) findViewById(R.id.view_image);
        image.setImageBitmap(bmp);
        this.bmp = bmp;

        uploadService = findViewById(R.id.UploadImage);
        uploadService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContextWrapper cw = new ContextWrapper(getApplicationContext());
                File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
                UUID uuid = UUID.randomUUID();
                String filename = uuid.toString();
                System.out.println("Filename = "+filename);
                File file = new File(directory, filename + ".jpg");
                // @Brijesh Thakur - https://stackoverflow.com/questions/17674634/saving-and-reading-bitmaps-images-from-internal-memory-in-android
                // Save bitmap image in internal memory
                if (!file.exists()) {
                    FileOutputStream fos = null;
                    try {
                        fos = new FileOutputStream(file);
                        bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                        fos.flush();
                        fos.close();
                    } catch (java.io.IOException e) {
                        e.printStackTrace();
                    }
                }
                // setup dialog box for success and failure scenario
                ProgressDialog progressDialog = new ProgressDialog(ClassificationActivity.this);
                progressDialog.setTitle("Uploading Image");
                progressDialog.setMessage("Image upload in progress...");
                progressDialog.setCancelable(false);
                progressDialog.setMax(50);
                progressDialog.show();
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(host)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                File image = new File(file.getPath());
                RequestBody imageBody = RequestBody.create(image, MediaType.parse("image/*"));
                MultipartBody.Part filePart = MultipartBody.Part.createFormData("image",image.getName(),imageBody);
//                MultipartBody.Part textPart =  MultipartBody.Part.createFormData("category",spinner.getSelectedItem().toString());
                UploadService service = retrofit.create(UploadService.class);
                // Call REST API using retrofit
                Call<UploadResponse> response = service.startUpload(filePart);
                response.enqueue(new Callback<UploadResponse>() {
                    @Override
                    public void onResponse(Call<UploadResponse> call, Response<UploadResponse> response) {
                        System.out.println("Successfully uploaded");
                        progressDialog.dismiss();
                        storeImage(Integer.valueOf(response.body().getCat()));
                        new AlertDialog.Builder(ClassificationActivity.this)
                                .setTitle("Upload Successful")
                                .setMessage("Your Image has been uploaded to the server!")
                                .setCancelable(false)
                                .setPositiveButton("ok", (DialogInterface.OnClickListener) (dialog, which) -> {
                                    // Redirect to main activity
                                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                }).show();
                    }

                    @Override
                    public void onFailure(Call<UploadResponse> call, Throwable t) {
                        System.out.println("Failed to upload");
                        progressDialog.dismiss();
                        new AlertDialog.Builder(ClassificationActivity.this)
                                .setTitle("Upload Failed")
                                .setMessage("Something went wrong... Please try after some time.")
                                .setCancelable(true)
                                .setPositiveButton("ok", (DialogInterface.OnClickListener) (dialog, which) -> {
                                    // stay at same activity
                                }).show();
                    }
                });
            }
        });
    }
    private void storeImage(int num){
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/MC/"+num+"/");
        if (!storageDir.exists())
            storageDir.mkdirs();
        try {
            File image = File.createTempFile(
                    timeStamp,                   /* prefix */
                    ".jpeg",                     /* suffix */
                    storageDir                   /* directory */
            );
            FileOutputStream out = new FileOutputStream(image);
            bmp.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}