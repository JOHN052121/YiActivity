package com.yiactivity.model;

import android.media.Image;

import java.io.Serializable;

public class Sponsor implements Serializable {

    private int sponsorId;

    private String spon_Name;

    private String org_Name;

    private String password;

    private String university;

    private String phoneNum;

    private String proof;

    private String email;

    private int flag;

    private byte[] sponsorImage;

    private String sponsorIntro;

    private int activityNumOfSponsor;

    public int getActivityNumOfSponsor() {
        return activityNumOfSponsor;
    }

    public void setActivityNumOfSponsor(int activityNumOfSponsor) {
        this.activityNumOfSponsor = activityNumOfSponsor;
    }

    public String getSponsorIntro() {
        return sponsorIntro;
    }

    public void setSponsorIntro(String sponsorIntro) {
        this.sponsorIntro = sponsorIntro;
    }

    public byte[] getSponsorImage() {
        return sponsorImage;
    }

    public void setSponsorImage(byte[] sponsorImage) {
        this.sponsorImage = sponsorImage;
    }

    public int getSponsorId() {
        return sponsorId;
    }

    public void setSponsorId(int sponsorId) {
        this.sponsorId = sponsorId;
    }

    public String getSpon_Name() {
        return spon_Name;
    }

    public void setSpon_Name(String spon_Name) {
        this.spon_Name = spon_Name;
    }

    public String getOrg_Name() {
        return org_Name;
    }

    public void setOrg_Name(String org_Name) {
        this.org_Name = org_Name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getProof() {
        return proof;
    }

    public void setProof(String proof) {
        this.proof = proof;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
