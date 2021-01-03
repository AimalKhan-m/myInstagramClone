package com.example.tapp_fbase.Models;

import java.util.Date;
import java.util.List;

public class PostModel {

  private String   dUserName = "";
  private String pUserName  = "";
  private String posterUid  = "";
  private Date postedTime;
  private String postText ;
  private List<String> postTags ;
  private String postID  = "";

  public PostModel() {
  }

  public PostModel(String dUserName, String pUserName, Date postedTime, String postText, List<String> postTags, String postID) {
    this.dUserName = dUserName;
    this.pUserName = pUserName;
    this.postedTime = postedTime;
    this.postText = postText;
    this.postTags = postTags;
    this.postID = postID;
  }

  public String getdUserName() {
    return dUserName;
  }

  public void setdUserName(String dUserName) {
    this.dUserName = dUserName;
  }

  public String getPostText() {
    return postText;
  }

  public void setPostText(String postText) {
    this.postText = postText;
  }

  public String getpUserName() {
    return pUserName;
  }

  public void setpUserName(String pUserName) {
    this.pUserName = pUserName;
  }

  public Date getPostedTime() {
    return postedTime;
  }

  public void setPostedTime(Date postedTime) {
    this.postedTime = postedTime;
  }

  public List<String> getPostTags() {
    return postTags;
  }

  public void setPostTags(List<String> postTags) {
    this.postTags = postTags;
  }

  public String getPostID() {
    return postID;
  }

  public void setPostID(String postID) {
    this.postID = postID;
  }

  public String getPosterUid() {
    return posterUid;
  }

  public void setPosterUid(String posterUid) {
    this.posterUid = posterUid;
  }
}
