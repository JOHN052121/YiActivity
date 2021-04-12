package com.yiactivity.model;

import android.media.Image;

import java.io.Serializable;

public class User implements Serializable {

    private int userId;
    private String userName;
    private String phoneNum;
    private String password;
    private String gender;
    private String university;
    private String Email;
    private int point;
    private byte[] image;
    private int statement;
    private String sign;
    private String userImage;
    private int subscribeNum;
    private int trendNum;
    private int collectionNum;

    public int getSubscribeNum() {
        return subscribeNum;
    }

    public void setSubscribeNum(int subscribeNum) {
        this.subscribeNum = subscribeNum;
    }

    public int getTrendNum() {
        return trendNum;
    }

    public void setTrendNum(int trendNum) {
        this.trendNum = trendNum;
    }

    public int getCollectionNum() {
        return collectionNum;
    }

    public void setCollectionNum(int collectionNum) {
        this.collectionNum = collectionNum;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public int getStatement() {
        return statement;
    }

    public void setStatement(int statement) {
        this.statement = statement;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
