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

    private static final String TAG = "GridViewAdapter";
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
        Log.d(TAG, "getCount: " + imagesList.size());
        return imagesList.size();
    }

    @Override
    public Object getItem(int position) {
        Log.d(TAG, "getItem: ");
        return null;
    }

    @Override
    public long getItemId(int position) {
        Log.d(TAG, "getItemId: ");
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
            if (imagesList.get(position).isSelected)
                imageView.setAlpha(1f);
            else
                listener.onPicClick(position, imagesList.get(position).imageUri, imagesList);

//            Picasso.with(context).load(imagesList.get(position).imageUri).into(new Target() {
//                @Override
//                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                    String fileUri ="";
//                    try {
//                        File mydir = new File(Environment.getExternalStorageDirectory() + "/11zon");
//                        if (!mydir.exists()) {
//                            mydir.mkdirs();
//                        }
            if (count > 0) {
                Log.d(TAG, "getView: 1" + imagesList.get(position).isSelected);
                if (imagesList.get(position).isSelected) {
                    Log.d(TAG, "getView: 2");
                    count--;
                    imageView.setAlpha(1f);
                    listener.onPicLongPress(position, false);
                    imagesList.get(position).isSelected = false;
                } else {
                    Log.d(TAG, "getView: 3");
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
                    listener.onPicClick(position);
            }
//            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
//            StrictMode.setVmPolicy(builder.build());

//            try {
//                URL url = new URL(imagesList.get(position).imageUri);
//                InputStream in = new BufferedInputStream(url.openStream());
//                ByteArrayOutputStream out = new ByteArrayOutputStream();
//                byte[] buf = new byte[1024];
//                int n = 0;
//                while (-1 != (n = in.read(buf))) {
//                    out.write(buf, 0, n);
//                }
//                out.close();
//                in.close();
//                byte[] response = out.toByteArray();
//                FileOutputStream fos = new FileOutputStream("C://borrowed_image.jpg");
//                fos.write(response);
//                fos.close();
//
//                final File file = new File(context.getFilesDir(), "webImage");
//                Uri weburi = Uri.fromFile(file);
//
//                final Intent intent = new Intent(Intent.ACTION_SEND);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                Log.d(TAG, "intent scnz uri: "+Uri.fromFile(file));
//                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
//                intent.setType("image/*");
//                context.startActivity(Intent.createChooser(intent, "Share image via"));
//            }catch (Exception e){
//
//            }

//            Bitmap bitmap = Bitmap.createBitmap(imageView.getWidth(), imageView.getHeight(), Bitmap.Config.ARGB_8888);

//            Log.d(TAG, "intent scnz: start");
//            try {
////                String root = Environment.getExternalStorageDirectory().toString();
//                String root = context.getFilesDir().getAbsolutePath();
//                File myDir = new File(root );
//
//                if (!myDir.exists()) {
//                    myDir.mkdirs();
//                }
//
//                String name =  "TestPhoto.jpeg";
//                myDir = new File(myDir, name);
//                FileOutputStream out = new FileOutputStream(myDir);
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
//
//                out.flush();
//                out.close();
//
//                final Intent intent = new Intent(Intent.ACTION_SEND);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                Log.d(TAG, "intent scnz uri: "+Uri.fromFile(myDir));
//                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(myDir));
//
//                String filenameArray[] =  Uri.fromFile(myDir).toString().split("\\.");
//                String extension = filenameArray[filenameArray.length-1];
//                System.out.println(extension);
//                Log.d(TAG, "intent scnz ext: "+extension);
//
//                intent.setType("image/*");
//                context.startActivity(Intent.createChooser(intent, "Share image via"));
//            } catch(Exception e){
//                Log.e(TAG, "getView: intent scnz exception ", e);
//                // some action
//            }


//            try{
//                File file = new File(context.getExternalCacheDir(), imagesList.get(position).id+".jpg");
//                FileOutputStream fOut = new FileOutputStream(file);
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
//                fOut.flush();
//                fOut.close();
////                file.setReadable(true, false);
//                final Intent intent = new Intent(Intent.ACTION_SEND);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                Log.d(TAG, "getView: "+Uri.fromFile(file));
//                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
//                intent.setType("image/png");
//                context.startActivity(Intent.createChooser(intent, "Share image via"));
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

        });

        imageView.setOnLongClickListener(v -> {
            Log.d(TAG, "getView: " + imagesList.get(position).isSelected);
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

    public interface OnClick {
        void onPicClick(int position, String imgUri);

        void onPicLongPress(int position, Boolean isSelected);

        void onPicClick(int position);

        void onPicClick(int position, String imageUri, ArrayList<Image> imagesList);
    }
}
