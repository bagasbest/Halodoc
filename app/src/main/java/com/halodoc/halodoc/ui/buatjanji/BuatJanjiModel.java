package com.halodoc.halodoc.ui.buatjanji;

import android.os.Parcel;
import android.os.Parcelable;

public class BuatJanjiModel implements Parcelable {

    private String bookingDate;
    private String hospitalUid;
    private String notes;
    private String paymentProof;
    private String price;
    private String service;
    private String status;
    private String uid;
    private String userUid;
    private String name;
    private String location;
    private String type;
    private String dp;


    public BuatJanjiModel(){}

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getHospitalUid() {
        return hospitalUid;
    }

    public void setHospitalUid(String hospitalUid) {
        this.hospitalUid = hospitalUid;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getPaymentProof() {
        return paymentProof;
    }

    public void setPaymentProof(String paymentProof) {
        this.paymentProof = paymentProof;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDp() {
        return dp;
    }

    public void setDp(String dp) {
        this.dp = dp;
    }

    public static Creator<BuatJanjiModel> getCREATOR() {
        return CREATOR;
    }

    protected BuatJanjiModel(Parcel in) {
        bookingDate = in.readString();
        hospitalUid = in.readString();
        notes = in.readString();
        paymentProof = in.readString();
        price = in.readString();
        service = in.readString();
        status = in.readString();
        uid = in.readString();
        userUid = in.readString();
        name = in.readString();
        location = in.readString();
        type = in.readString();
        dp = in.readString();
    }

    public static final Creator<BuatJanjiModel> CREATOR = new Creator<BuatJanjiModel>() {
        @Override
        public BuatJanjiModel createFromParcel(Parcel in) {
            return new BuatJanjiModel(in);
        }

        @Override
        public BuatJanjiModel[] newArray(int size) {
            return new BuatJanjiModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(bookingDate);
        parcel.writeString(hospitalUid);
        parcel.writeString(notes);
        parcel.writeString(paymentProof);
        parcel.writeString(price);
        parcel.writeString(service);
        parcel.writeString(status);
        parcel.writeString(uid);
        parcel.writeString(userUid);
        parcel.writeString(name);
        parcel.writeString(location);
        parcel.writeString(type);
        parcel.writeString(dp);
    }
}
