package com.example.helloworld;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import java.io.ByteArrayOutputStream;



public class MainActivity extends AppCompatActivity {

    // variables for camera button
    Button cam_open_id;
//    private static final int photo_id = 123;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get component by id assigned in XML file for camera button
        cam_open_id = findViewById(R.id.camera_button);
        //https://www.geeksforgeeks.org/how-to-open-camera-through-intent-and-display-captured-image-in-android/
        cam_open_id.setOnClickListener(v -> {
            // Create the intent to open the camera for capturing the image
            Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Start the activity
            startActivityForResult(camera_intent, 123);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Match the request photo id with requestCode
        if (requestCode == 123) {
            // Store the image in memory
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            // Intent to call the second activity and send the captured photo to second activity
            Intent intent = new Intent(this, ClassificationActivity.class);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG,100,stream);
            byte[] photoBytes = stream.toByteArray();
            intent.putExtra("image", photoBytes);
            // start the Intent
            startActivity(intent);
        }
    }
}