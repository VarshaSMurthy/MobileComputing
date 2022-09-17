package com.example.helloworld;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.UUID;

import android.view.MotionEvent;

public class ClassificationActivity extends AppCompatActivity {

    Button uploadService;
    String selectedCategory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Bundle extras = getIntent().getExtras();
        byte[] byteArray = extras.getByteArray("image");

        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        ImageView image = (ImageView) findViewById(R.id.view_image);
        image.setImageBitmap(bmp);

        Spinner spinner = findViewById(R.id.category_spinner);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Buildings");
        arrayList.add("Tree");
        arrayList.add("Flowers");
        arrayList.add("Cloths");
        arrayList.add("Book");
        arrayList.add("Person");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String tutorialsName = parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(), "Selected: " + tutorialsName, Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });
        uploadService = findViewById(R.id.UploadImage);
        uploadService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContextWrapper cw = new ContextWrapper(getApplicationContext());
                File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
                UUID uuid = UUID.randomUUID();
                String filename = uuid.toString();
                File file = new File(directory, filename + ".jpg");
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
                UploadAsyncTask uploadTask = new UploadAsyncTask(file.getPath());
                uploadTask.execute();
            }
        });
    }
}