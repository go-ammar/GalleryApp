package com.example.galleryapp.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Image implements Parcelable {
    public String imageUri;
    public boolean isSelected = false;
    public String fileDirectory;
    public String id;

    public Image(String image, String id){
        imageUri = image;
        this.id = id;
    }

    protected Image(Parcel in) {
        imageUri = in.readString();
        isSelected = in.readByte() != 0;
        fileDirectory = in.readString();
        id = in.readString();
    }

    public static final Creator<Image> CREATOR = new Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel in) {
            return new Image(in);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imageUri);
        dest.writeByte((byte) (isSelected ? 1 : 0));
        dest.writeString(fileDirectory);
        dest.writeString(id);
    }
}
