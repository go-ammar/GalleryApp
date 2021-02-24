package com.example.galleryapp.utils;

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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class GridViewAdapter extends BaseAdapter {

    private static final String TAG = "GridViewAdapter";
    private final Context context;
    private final ArrayList<Image> imagesList;
    private final OnClick listener;


    public GridViewAdapter(Context context, ArrayList<Image> imagesList, OnClick listener) {
        this.context = context;
        this.imagesList = imagesList;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        Log.d(TAG, "getCount: " + imagesList.size());
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
        Log.d(TAG, "getView: " + imagesList.get(position).imageUri);

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int pxWidth = displayMetrics.widthPixels;

        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(pxWidth / 4, pxWidth / 4));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(1, 1, 1, 1);
        } else {
            imageView = (ImageView) convertView;
        }
        imageView.setAlpha(1f);

//        Bitmap bitmap = Bitmap.createBitmap(imageView.getWidth(), imageView.getHeight(), Bitmap.Config.ARGB_8888);
//
//        try{
//            File file = new File(context.getExternalCacheDir(), "meow.jpg");
//            FileOutputStream fOut = new FileOutputStream(file);
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
//            fOut.flush();
//            fOut.close();
//            file.setReadable(true, false);
//            final Intent intent = new Intent(Intent.ACTION_SEND);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
//            intent.setType("image/png");
//            context.startActivity(Intent.createChooser(intent, "Share image via"));
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        Log.d(TAG, "getView: " + imagesList.get(position).imageUri);
        Glide.with(context)
                .load(imagesList.get(position).imageUri)
                .into(imageView);


        imageView.setOnClickListener(v -> {
//            if (imagesList.get(position).isSelected)
//                imageView.setAlpha(1f);
//            else
//                listener.onPicClick(position);
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());

            Bitmap bitmap = Bitmap.createBitmap(imageView.getWidth(), imageView.getHeight(), Bitmap.Config.ARGB_8888);

            try{
                File file = new File(context.getExternalCacheDir(), imagesList.get(position).id+".jpg");
                FileOutputStream fOut = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.flush();
                fOut.close();
                file.setReadable(true, false);
                final Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Log.d(TAG, "getView: "+Uri.fromFile(file));
                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                intent.setType("image/png");
                context.startActivity(Intent.createChooser(intent, "Share image via"));

            } catch (Exception e) {
                e.printStackTrace();
            }

        });

        imageView.setOnLongClickListener(v -> {
            Log.d(TAG, "getView: " + imagesList.get(position).isSelected);
            if (imagesList.get(position).isSelected) {
                imageView.setAlpha(1f);
                listener.onPicLongPress(position, false);
            }else {
                imageView.setAlpha(0.5f);
                listener.onPicLongPress(position, true);
            }
            imagesList.get(position).isSelected = !imagesList.get(position).isSelected;
            return true;
        });

        return imageView;
    }

    public interface OnClick {
        void onPicClick(int position);

        void onPicLongPress(int position, Boolean isSelected);
    }
}
