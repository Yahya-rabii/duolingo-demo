package com.example.dldemo.model;

import android.os.Parcel;
import android.os.Parcelable;

public class LangModel implements Parcelable {

    private String name;
    private String image;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }




    protected LangModel(Parcel in) {
        name = in.readString();
        image = in.readString();
    }

    public static final Creator<LangModel> CREATOR = new Creator<LangModel>() {
        @Override
        public LangModel createFromParcel(Parcel in) {
            return new LangModel(in);
        }

        @Override
        public LangModel[] newArray(int size) {
            return new LangModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(image);
    }
}
