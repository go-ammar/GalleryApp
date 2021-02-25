package com.example.galleryapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.galleryapp.R;

public class ImageActivity extends AppCompatActivity {

    Context context;
    private static final String TAG = "ImageActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);


        context = this;
        actionViews();



    }

    private void actionViews() {

        Intent i = getIntent();
        String position = i.getStringExtra("uri");

        ImageView imageView = (ImageView) findViewById(R.id.fullSizeIv);

        try {
            Glide.with(context)
                    .load(position)
                    .into(imageView);

        }catch (Exception e ){
            Log.d(TAG, "actionViews: "+e);
        }
    }
}