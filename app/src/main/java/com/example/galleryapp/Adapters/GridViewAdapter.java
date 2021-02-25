package com.example.galleryapp.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.galleryapp.models.Image;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class GridViewAdapter extends BaseAdapter {

    private final Context context;
    private final ArrayList<Image> imagesList;
    private final OnClick listener;
    private int count = 0;


    public GridViewAdapter(Context context, ArrayList<Image> imagesList, OnClick listener) {
        this.context = context;
        this.imagesList = imagesList;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return imagesList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int pxWidth = displayMetrics.widthPixels;

        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(pxWidth / 4, pxWidth / 4));
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            imageView.setPadding(1, 1, 1, 1);
        } else {
            imageView = (ImageView) convertView;
        }

        if (imagesList.get(position).isSelected)
            imageView.setAlpha(0.5f);
        else
            imageView.setAlpha(1f);

        Glide.with(context)
                .load(imagesList.get(position).imageUri)
                .into(imageView);

        imageView.setOnClickListener(v -> {


            if (count > 0) {
                if (imagesList.get(position).isSelected) {
                    count--;
                    imageView.setAlpha(1f);
                    listener.onPicLongPress(position, false);
                    imagesList.get(position).isSelected = false;
                } else {
                    count++;
                    imagesList.get(position).isSelected = true;
                    imageView.setAlpha(0.5f);
                    listener.onPicLongPress(position, true);
                }
            } else {
                if (imagesList.get(position).isSelected) {
                    imageView.setAlpha(1f);
                    imagesList.get(position).isSelected = true;
                } else
                    listener.onPicClick(position, imagesList.get(position).imageUri, imagesList);
            }

        });

        imageView.setOnLongClickListener(v -> {
            if (imagesList.get(position).isSelected) {
                imageView.setAlpha(1f);
                count--;
                listener.onPicLongPress(position, false);
            } else {
                count++;
                imageView.setAlpha(0.5f);
                listener.onPicLongPress(position, true);
            }
            imagesList.get(position).isSelected = !imagesList.get(position).isSelected;
            return true;
        });

        return imageView;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        count = 0;
    }

    public interface OnClick {

        void onPicLongPress(int position, Boolean isSelected);
        void onPicClick(int position, String imageUri, ArrayList<Image> imagesList);
    }
}