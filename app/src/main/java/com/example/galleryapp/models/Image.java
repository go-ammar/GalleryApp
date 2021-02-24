package com.example.galleryapp.models;

public class Image {
    public String imageUri;
    public boolean isSelected = false;
    public String fileDirectory;
    public String id;

    public Image(String image, String id){
        imageUri = image;
        this.id = id;
    }
}
