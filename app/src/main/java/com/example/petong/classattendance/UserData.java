package com.example.petong.classattendance;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class UserData implements Parcelable {
    @SerializedName("success")
    private boolean success;
    @SerializedName("AccountID")
    private String AccountID;
    @SerializedName("StudentCode")
    private String StudentCode;
    @SerializedName("userID")
    private String userID;
    @SerializedName("FullName")
    private String FullName;
    @SerializedName("PreName")
    private String PreName;
    @SerializedName("FirstName")
    private String FirstName;
    @SerializedName("lastName")
    private String lastName;
    @SerializedName("PicPath")
    private String PicPath;
    @SerializedName("AccountType")
    private String AccountType;
    @SerializedName("ImageLink")
    private String ImageLink;
    @SerializedName("isTeacher")
    private boolean isTeacher;
    @SerializedName("isExecutive")
    private boolean isExecutive;
    @SerializedName("OrganID")
    private String OrganID;
    @SerializedName("OrganizationNameHead")
    private String OrganizationNameHead;
    @SerializedName("organizationFullName")
    private String organizationFullName;
    @SerializedName("PositionNameExecutive")
    private String PositionNameExecutive;
    @SerializedName("OrganizationExecNameHead")
    private String OrganizationExecNameHead;
    @SerializedName("PositionNameTh")
    private String PositionNameTh;
    @SerializedName("AccessActivityID73")
    private String AccessActivityID73;
    @SerializedName("privateAccessActivityID74")
    private String privateAccessActivityID74;
    @SerializedName("AccessActivityID173")
    private String AccessActivityID173;
    @SerializedName("AccessActivityID174")
    private String AccessActivityID174;
    @SerializedName("CountStdMessage")
    private String CountStdMessage;

    protected UserData(Parcel in) {
        success = in.readByte() != 0;
        AccountID = in.readString();
        StudentCode = in.readString();
        userID = in.readString();
        FullName = in.readString();
        PreName = in.readString();
        FirstName = in.readString();
        lastName = in.readString();
        PicPath = in.readString();
        AccountType = in.readString();
        ImageLink = in.readString();
        isTeacher = in.readByte() != 0;
        isExecutive = in.readByte() != 0;
        OrganID = in.readString();
        OrganizationNameHead = in.readString();
        organizationFullName = in.readString();
        PositionNameExecutive = in.readString();
        OrganizationExecNameHead = in.readString();
        PositionNameTh = in.readString();
        AccessActivityID73 = in.readString();
        privateAccessActivityID74 = in.readString();
        AccessActivityID173 = in.readString();
        AccessActivityID174 = in.readString();
        CountStdMessage = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (success ? 1 : 0));
        dest.writeString(AccountID);
        dest.writeString(StudentCode);
        dest.writeString(userID);
        dest.writeString(FullName);
        dest.writeString(PreName);
        dest.writeString(FirstName);
        dest.writeString(lastName);
        dest.writeString(PicPath);
        dest.writeString(AccountType);
        dest.writeString(ImageLink);
        dest.writeByte((byte) (isTeacher ? 1 : 0));
        dest.writeByte((byte) (isExecutive ? 1 : 0));
        dest.writeString(OrganID);
        dest.writeString(OrganizationNameHead);
        dest.writeString(organizationFullName);
        dest.writeString(PositionNameExecutive);
        dest.writeString(OrganizationExecNameHead);
        dest.writeString(PositionNameTh);
        dest.writeString(AccessActivityID73);
        dest.writeString(privateAccessActivityID74);
        dest.writeString(AccessActivityID173);
        dest.writeString(AccessActivityID174);
        dest.writeString(CountStdMessage);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserData> CREATOR = new Creator<UserData>() {
        @Override
        public UserData createFromParcel(Parcel in) {
            return new UserData(in);
        }

        @Override
        public UserData[] newArray(int size) {
            return new UserData[size];
        }
    };

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getAccountID() {
        return AccountID;
    }

    public void setAccountID(String accountID) {
        AccountID = accountID;
    }

    public String getStudentCode() {
        return StudentCode;
    }

    public void setStudentCode(String studentCode) {
        StudentCode = studentCode;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getPreName() {
        return PreName;
    }

    public void setPreName(String preName) {
        PreName = preName;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPicPath() {
        return PicPath;
    }

    public void setPicPath(String picPath) {
        PicPath = picPath;
    }

    public String getAccountType() {
        return AccountType;
    }

    public void setAccountType(String accountType) {
        AccountType = accountType;
    }

    public String getImageLink() {
        return ImageLink;
    }

    public void setImageLink(String imageLink) {
        ImageLink = imageLink;
    }

    public boolean isTeacher() {
        return isTeacher;
    }

    public void setTeacher(boolean teacher) {
        isTeacher = teacher;
    }

    public boolean isExecutive() {
        return isExecutive;
    }

    public void setExecutive(boolean executive) {
        isExecutive = executive;
    }

    public String getOrganID() {
        return OrganID;
    }

    public void setOrganID(String organID) {
        OrganID = organID;
    }

    public String getOrganizationNameHead() {
        return OrganizationNameHead;
    }

    public void setOrganizationNameHead(String organizationNameHead) {
        OrganizationNameHead = organizationNameHead;
    }

    public String getOrganizationFullName() {
        return organizationFullName;
    }

    public void setOrganizationFullName(String organizationFullName) {
        this.organizationFullName = organizationFullName;
    }

    public String getPositionNameExecutive() {
        return PositionNameExecutive;
    }

    public void setPositionNameExecutive(String positionNameExecutive) {
        PositionNameExecutive = positionNameExecutive;
    }

    public String getOrganizationExecNameHead() {
        return OrganizationExecNameHead;
    }

    public void setOrganizationExecNameHead(String organizationExecNameHead) {
        OrganizationExecNameHead = organizationExecNameHead;
    }

    public String getPositionNameTh() {
        return PositionNameTh;
    }

    public void setPositionNameTh(String positionNameTh) {
        PositionNameTh = positionNameTh;
    }

    public String getAccessActivityID73() {
        return AccessActivityID73;
    }

    public void setAccessActivityID73(String accessActivityID73) {
        AccessActivityID73 = accessActivityID73;
    }

    public String getPrivateAccessActivityID74() {
        return privateAccessActivityID74;
    }

    public void setPrivateAccessActivityID74(String privateAccessActivityID74) {
        this.privateAccessActivityID74 = privateAccessActivityID74;
    }

    public String getAccessActivityID173() {
        return AccessActivityID173;
    }

    public void setAccessActivityID173(String accessActivityID173) {
        AccessActivityID173 = accessActivityID173;
    }

    public String getAccessActivityID174() {
        return AccessActivityID174;
    }

    public void setAccessActivityID174(String accessActivityID174) {
        AccessActivityID174 = accessActivityID174;
    }

    public String getCountStdMessage() {
        return CountStdMessage;
    }

    public void setCountStdMessage(String countStdMessage) {
        CountStdMessage = countStdMessage;
    }
}
