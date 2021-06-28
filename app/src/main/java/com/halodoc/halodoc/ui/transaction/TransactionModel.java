package com.halodoc.halodoc.ui.transaction;

import android.os.Parcel;
import android.os.Parcelable;

public class TransactionModel implements Parcelable {
    private String name;
    private String dp;
    private String bookingDate;
    private String price;
    private String services;
    private String status;
    private String transactionType;
    private String uid;
    private String proofPayment;
    private String notes;
    private String userUid;

    public TransactionModel(){}

    protected TransactionModel(Parcel in) {
        name = in.readString();
        dp = in.readString();
        bookingDate = in.readString();
        price = in.readString();
        services = in.readString();
        status = in.readString();
        transactionType = in.readString();
        uid = in.readString();
        proofPayment = in.readString();
        notes = in.readString();
        userUid = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(dp);
        dest.writeString(bookingDate);
        dest.writeString(price);
        dest.writeString(services);
        dest.writeString(status);
        dest.writeString(transactionType);
        dest.writeString(uid);
        dest.writeString(proofPayment);
        dest.writeString(notes);
        dest.writeString(userUid);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TransactionModel> CREATOR = new Creator<TransactionModel>() {
        @Override
        public TransactionModel createFromParcel(Parcel in) {
            return new TransactionModel(in);
        }

        @Override
        public TransactionModel[] newArray(int size) {
            return new TransactionModel[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDp() {
        return dp;
    }

    public void setDp(String dp) {
        this.dp = dp;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getServices() {
        return services;
    }

    public void setServices(String services) {
        this.services = services;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getProofPayment() {
        return proofPayment;
    }

    public void setProofPayment(String proofPayment) {
        this.proofPayment = proofPayment;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }
}
