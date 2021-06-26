package com.halodoc.halodoc.ui.home.buatjanji;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class HospitalModel implements Parcelable {
    private String name;
    private String type;
    private String location;
    private String dp;
    private String about;
    private List<String> services;
    private String uid;

    public HospitalModel(){}

    protected HospitalModel(Parcel in) {
        name = in.readString();
        type = in.readString();
        location = in.readString();
        dp = in.readString();
        about = in.readString();
        services = in.createStringArrayList();
        uid = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(type);
        dest.writeString(location);
        dest.writeString(dp);
        dest.writeString(about);
        dest.writeStringList(services);
        dest.writeString(uid);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<HospitalModel> CREATOR = new Creator<HospitalModel>() {
        @Override
        public HospitalModel createFromParcel(Parcel in) {
            return new HospitalModel(in);
        }

        @Override
        public HospitalModel[] newArray(int size) {
            return new HospitalModel[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDp() {
        return dp;
    }

    public void setDp(String dp) {
        this.dp = dp;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public List<String> getServices() {
        return services;
    }

    public void setServices(List<String> services) {
        this.services = services;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}

