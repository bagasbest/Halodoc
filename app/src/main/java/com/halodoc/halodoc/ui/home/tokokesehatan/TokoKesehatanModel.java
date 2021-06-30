package com.halodoc.halodoc.ui.home.tokokesehatan;

import android.os.Parcel;
import android.os.Parcelable;

public class TokoKesehatanModel implements Parcelable {
    private String name;
    private String price;
    private String description;
    private String dp;
    private String uid;
    private String type;

    public TokoKesehatanModel(){}

    protected TokoKesehatanModel(Parcel in) {
        name = in.readString();
        price = in.readString();
        description = in.readString();
        dp = in.readString();
        uid = in.readString();
        type = in.readString();
    }

    public static final Creator<TokoKesehatanModel> CREATOR = new Creator<TokoKesehatanModel>() {
        @Override
        public TokoKesehatanModel createFromParcel(Parcel in) {
            return new TokoKesehatanModel(in);
        }

        @Override
        public TokoKesehatanModel[] newArray(int size) {
            return new TokoKesehatanModel[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDp() {
        return dp;
    }

    public void setDp(String dp) {
        this.dp = dp;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(price);
        parcel.writeString(description);
        parcel.writeString(dp);
        parcel.writeString(uid);
        parcel.writeString(type);
    }
}
