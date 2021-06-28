package com.halodoc.halodoc.ui.home.konsultasi;

import android.os.Parcel;
import android.os.Parcelable;

public class ConsultationWithDoctorModel implements Parcelable {
    private String name;
    private String description;
    private String sertifikatKeahlian;
    private String experience;
    private String phone;
    private String dp;
    private String practice;
    private String specialist;
    private String like;
    private String price;
    private String uid;
    private String role;

    public ConsultationWithDoctorModel(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSertifikatKeahlian() {
        return sertifikatKeahlian;
    }

    public void setSertifikatKeahlian(String sertifikatKeahlian) {
        this.sertifikatKeahlian = sertifikatKeahlian;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDp() {
        return dp;
    }

    public void setDp(String dp) {
        this.dp = dp;
    }

    public String getPractice() {
        return practice;
    }

    public void setPractice(String practice) {
        this.practice = practice;
    }

    public String getSpecialist() {
        return specialist;
    }

    public void setSpecialist(String specialist) {
        this.specialist = specialist;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public static Creator<ConsultationWithDoctorModel> getCREATOR() {
        return CREATOR;
    }

    protected ConsultationWithDoctorModel(Parcel in) {
        name = in.readString();
        description = in.readString();
        sertifikatKeahlian = in.readString();
        experience = in.readString();
        phone = in.readString();
        dp = in.readString();
        practice = in.readString();
        specialist = in.readString();
        like = in.readString();
        price = in.readString();
        uid = in.readString();
        role = in.readString();
    }

    public static final Creator<ConsultationWithDoctorModel> CREATOR = new Creator<ConsultationWithDoctorModel>() {
        @Override
        public ConsultationWithDoctorModel createFromParcel(Parcel in) {
            return new ConsultationWithDoctorModel(in);
        }

        @Override
        public ConsultationWithDoctorModel[] newArray(int size) {
            return new ConsultationWithDoctorModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeString(sertifikatKeahlian);
        parcel.writeString(experience);
        parcel.writeString(phone);
        parcel.writeString(dp);
        parcel.writeString(practice);
        parcel.writeString(specialist);
        parcel.writeString(like);
        parcel.writeString(price);
        parcel.writeString(uid);
        parcel.writeString(role);
    }
}
