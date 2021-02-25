package com.example.galleryapp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;

import com.bumptech.glide.Glide;
import com.example.galleryapp.Adapters.FullImageScrollAdapter;
import com.example.galleryapp.R;
import com.example.galleryapp.databinding.ActivityImageBinding;
import com.example.galleryapp.models.Image;

import java.util.ArrayList;

public class ImageActivity extends AppCompatActivity implements FullImageScrollAdapter.ImagesDataInterface {

    Context context;
    FullImageScrollAdapter fullImageScrollAdapter;
    ActivityImageBinding binding;
    ArrayList<Image> imagesList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_image);


        context = this;
        imagesList = new ArrayList<>();
        actionViews();


    }

    private void actionViews() {

        Intent i = getIntent();
        int position = i.getIntExtra("uri", 0);
        imagesList = i.getParcelableArrayListExtra("uriList");


        fullImageScrollAdapter = new FullImageScrollAdapter(context, imagesList, this );
        binding.horizontalRecyclerView.setAdapter(fullImageScrollAdapter);
        binding.horizontalRecyclerView.setLayoutManager(new LinearLayoutManager(context,
                LinearLayoutManager.HORIZONTAL, false));
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(binding.horizontalRecyclerView);
        binding.horizontalRecyclerView.scrollToPosition(position);


    }



    @Override
    public void onRemove(int position) {


    }


}