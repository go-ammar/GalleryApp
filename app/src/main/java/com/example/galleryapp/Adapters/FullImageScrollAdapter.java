package com.example.galleryapp.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.galleryapp.R;
import com.example.galleryapp.databinding.ItemImageBinding;
import com.example.galleryapp.models.Image;

import java.util.ArrayList;

public class FullImageScrollAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



    Context context;
    ArrayList<Image> ImagesArrayList;
    private final ImagesDataInterface imagesDataInterface;

    public FullImageScrollAdapter (Context context, ArrayList<Image> ImagesArrayList, ImagesDataInterface imagesDataInterface){
        this.context = context;
        this.ImagesArrayList = ImagesArrayList;
        this.imagesDataInterface = imagesDataInterface;

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemImageBinding item_data_images = DataBindingUtil.inflate(layoutInflater, R.layout.item_image, parent, false);
        return new ImageDataViewHolder(item_data_images);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String imgUri = ImagesArrayList.get(position).imageUri;

        if(!ImagesArrayList.get(position).imageUri.isEmpty()){


            Glide.with(context)
                    .asBitmap()
                    .load(imgUri)
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            ((ImageDataViewHolder) holder).binding.singleIv.setImageBitmap(resource);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });

        }





        ((ImageDataViewHolder) holder).binding.fabRemove.setOnClickListener(v -> {
            onRemove(position);
            });


        ((ImageDataViewHolder) holder).binding.singleIv.setOnClickListener(v -> {
            if (((ImageDataViewHolder) holder).binding.fabRemove.getVisibility() == View.GONE) {
                ((ImageDataViewHolder) holder).binding.fabRemove.setVisibility(View.VISIBLE);
            }else {
                ((ImageDataViewHolder) holder).binding.fabRemove.setVisibility(View.GONE);
            }
        });




    }

    private void onRemove(int position) {
        ImagesArrayList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
    }

    @Override
    public int getItemCount() {
        return ImagesArrayList.size();
    }






    static class ImageDataViewHolder extends RecyclerView.ViewHolder {

        ItemImageBinding binding;

        ImageDataViewHolder(@NonNull ItemImageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }


    }




    public interface ImagesDataInterface {
        void onRemove(int position);


    }


}
