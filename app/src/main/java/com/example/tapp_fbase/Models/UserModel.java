package com.example.tapp_fbase.Models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class UserModel {

  private String pUserName  = "";
  private String dUserName  = "";
  private String userProfileUri = "";
  private String userUid = "";
  @ServerTimestamp
  private Date uJoinDate ;
  private String uBio = "" ;

  public UserModel() {
  }

  public UserModel(String pUserName, String dUserName, String userProfileUri, Date uJoinDate, String uBio) {
    this.pUserName = pUserName;
    this.dUserName = dUserName;
    this.userProfileUri = userProfileUri;
    this.uJoinDate = uJoinDate;
    this.uBio = uBio;
  }

  public UserModel(String pUserName, String dUserName, String userProfileUri, String uBio) {
    this.pUserName = pUserName;
    this.dUserName = dUserName;
    this.userProfileUri = userProfileUri;
    this.uBio = uBio;
  }

  public String getpUserName() {
    return pUserName;
  }

  public void setpUserName(String pUserName) {
    this.pUserName = pUserName;
  }

  public String getdUserName() {
    return dUserName;
  }

  public void setdUserName(String dUserName) {
    this.dUserName = dUserName;
  }

  public String getUserProfileUri() {
    return userProfileUri;
  }

  public void setUserProfileUri(String userProfileUri) {
    this.userProfileUri = userProfileUri;
  }

  public Date getuJoinDate() {
    return uJoinDate;
  }

  public void setuJoinDate(Date uJoinDate) {
    this.uJoinDate = uJoinDate;
  }

  public String getuBio() {
    return uBio;
  }

  public void setuBio(String uBio) {
    this.uBio = uBio;
  }

  public String getUserUid() {
    return userUid;
  }

  public void setUserUid(String userUid) {
    this.userUid = userUid;
  }
}
