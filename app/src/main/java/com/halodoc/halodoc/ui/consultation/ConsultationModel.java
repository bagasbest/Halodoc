package com.halodoc.halodoc.ui.consultation;

import android.os.Parcel;
import android.os.Parcelable;

public class ConsultationModel implements Parcelable {

    private String consultationUid;
    private String userUid;
    private String doctorUid;
    private String userName;
    private String doctorName;
    private String userDp;
    private String doctorDp;
    private String status;
    private String service;
    private String price;
    private String notes;

    public ConsultationModel () {}

    protected ConsultationModel(Parcel in) {
        consultationUid = in.readString();
        userUid = in.readString();
        doctorUid = in.readString();
        userName = in.readString();
        doctorName = in.readString();
        userDp = in.readString();
        doctorDp = in.readString();
        status = in.readString();
        service = in.readString();
        price = in.readString();
        notes = in.readString();
    }

    public static final Creator<ConsultationModel> CREATOR = new Creator<ConsultationModel>() {
        @Override
        public ConsultationModel createFromParcel(Parcel in) {
            return new ConsultationModel(in);
        }

        @Override
        public ConsultationModel[] newArray(int size) {
            return new ConsultationModel[size];
        }
    };

    public String getConsultationUid() {
        return consultationUid;
    }

    public void setConsultationUid(String consultationUid) {
        this.consultationUid = consultationUid;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getDoctorUid() {
        return doctorUid;
    }

    public void setDoctorUid(String doctorUid) {
        this.doctorUid = doctorUid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getUserDp() {
        return userDp;
    }

    public void setUserDp(String userDp) {
        this.userDp = userDp;
    }

    public String getDoctorDp() {
        return doctorDp;
    }

    public void setDoctorDp(String doctorDp) {
        this.doctorDp = doctorDp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(consultationUid);
        parcel.writeString(userUid);
        parcel.writeString(doctorUid);
        parcel.writeString(userName);
        parcel.writeString(doctorName);
        parcel.writeString(userDp);
        parcel.writeString(doctorDp);
        parcel.writeString(status);
        parcel.writeString(service);
        parcel.writeString(price);
        parcel.writeString(notes);
    }
}
