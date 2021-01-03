package com.example.tapp_fbase;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;


import com.example.tapp_fbase.Fragments.HomeFrag;
import com.example.tapp_fbase.Fragments.ProfileFrag;
import com.example.tapp_fbase.Fragments.SearchFrag;
import com.example.tapp_fbase.Models.PostModel;
import com.example.tapp_fbase.Models.UserModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

import com.example.tapp_fbase.R;

public class AppUtils {

  /// NAME CONVENTIONS
  // COLLECTION NAVE HAVE ALL_CAPS with underScroll
  // DOCUMENT HAVE  Camel_Case_With_Under_Scroll
  // FIELD HAVE CamelCase
  // OTHER URI,URL,LOCATION,REFERENCES  etc have their own Values

  // FIREBASE CURRENT LOGIN USER
  public static  FirebaseUser cUser = FirebaseAuth.getInstance().getCurrentUser();
  // FIREBASE FIRESTORE
  public static final FirebaseFirestore fbFs = FirebaseFirestore.getInstance();
 /* public static final Fire fbS = FirebaseStorage.getInstance();
  public static final StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();*/
      //Current USer  FOLLOWING AND FOLLOWERS ARRAYLISt
    private static ArrayList<String> cFollowingList = new ArrayList<>();
    private static ArrayList<String> cFollowersList = new ArrayList<>();
    private static Boolean isFollowingChLAttach = false;
    private static Boolean isFollowersChLAttach = false;

    // Current User NewsFeed
  private static ArrayList<PostModel> userNewsFeed = new ArrayList<>() ;


    public static final String KEY_USER_DISPLAY_NAME = "dUserName";
    public static final String KEY_USER_PROFILE_NAME = "pUserName";
    public static final String KEY_USER_UID = "userUid";
    public static final String KEY_USER_PROFILE_IMG_URI =  "userProfileUri";

    public static final String KEY_USER_JOIN_DATE = "uJoinDate";
    public static final String KEY_USER_BIO= "uBi♦o";
    public static final String KEY_USERS = "USERS";
    public static final String KEY_USERS_PROFILE_DATA = "USERS_PROFILE_DATA";
    public static final String KEY_USERS_PROFILE_NAME = "Users_Profile_Name";
    public static final String  KEY_USER_DEFAULT_P_IMG_URL = "https://firebasestorage.googleapis.com/v0/b/insta-poster-555fb.appspot.com/o/images%2Fd_profile_img.png?alt=media&token=adde428d-5d94-4f2f-880c-a70d7d91bd1c";

    // USER POST MODEL FILES CONSTANT
    public static final String KEY_POSTS = "POSTS";
    public static final String KEY_POST_USER_DISPLAY_NAME = "dUserName";
    public static final String KEY_POST_USER_PROFILE_NAME = "pUserName";
    public static final String KEY_POST_POSTER_UID = "posterUid";
    public static final String KEY_POST_TIME = "postedTime";
    public static final String KEY_POST_TAGS = "postTags";
    public static final String KEY_POST_DESCRIPTION = "postDescription";
    public static final String KEY_POST_USER_PROFILE_IMG_URI =  "userProfileUri";
    public static final String KEY_POST_IMG_URI = "postImgUri";
    public static final String KEY_POST_ID = "postID";
    //POSTS BTNS
    public static final String KEY_LIKES = "LIKES";

    /// FOLLOWER FOLLOWING
    public static final String KEY_FOLLOWERS = "FOLLOWERS" ;
    public static final String KEY_FOLLOWINGS = "FOLLOWINGS" ;



  public static String   setpUserName( String uName) {
    String pUserName  = getpUserName(uName);
    Map<String, String> uNameMap = new HashMap<>();
    uNameMap.put(FirebaseAuth.getInstance().getCurrentUser().getUid(), pUserName);
    fbFs.collection(KEY_USERS_PROFILE_DATA).document(KEY_USERS_PROFILE_NAME).set(uNameMap, SetOptions.merge());
    Log.e("PNAME---",  " : MAP -pUserName- SIZE  : " + pUserName);
      return pUserName;
  }

  public static String getpUserName(String uName){
    String pUserName  = uName.trim().toLowerCase();
    Task<DocumentSnapshot> t = fbFs.collection(KEY_USERS_PROFILE_DATA).document(KEY_USERS_PROFILE_NAME).get();
    while (!t.isSuccessful()){ }
    AtomicReference<Boolean> nameExist = new AtomicReference<>(true);
    DocumentSnapshot ds = t.getResult();
    if( ds.exists()){
      Map<String, Object> pUserNames = Objects.requireNonNull(ds.getData());
      Log.e("NAME---RETURN >>>> ",  Thread.currentThread().getName() + "  IF (((  this Error -2--pUserName.get()  : " + pUserName);
      nameExist.set(isNameExist(pUserName, pUserNames));
      int round = 1;
      while (nameExist.get()) {
        pUserName = generateUserName(uName, round);
        nameExist.set(isNameExist(pUserName, pUserNames));
        round++;
        Log.e("NAME---RETURN >>>> ", "WHILEE  this Error -2--pUserName.get()  : " + pUserName);
      }
    }
    return pUserName;
  }

  public static Boolean isNameExist(String uName, Map<String, Object> pUserNames) {
    Log.e("NAME---",  " : c " + pUserNames.size());

    for (Map.Entry<String, Object> pUserName : pUserNames.entrySet()) {
      Log.e("NAME---",  uName + " uNAME -:"+pUserName.getValue().toString().equals(uName)+" :  VALUE NAME   : " + pUserName.getValue().toString());

      if (pUserName.getValue().toString().equals(uName) && !(FirebaseAuth.getInstance().getCurrentUser().getDisplayName().equals(uName)) ) {
        Log.e("NAME---",  uName + " uNAME i ma in FOR TURE" + pUserName.getValue().toString());

        return true;
      }
    }
    Log.e("NAME---",  uName + " uNAME i ma in FOR FALSE " );

    return false;
  }

  public static String generateUserName(String uName, int r) {
    String pName = uName.trim().toLowerCase();
    Random random = new Random();
    Log.e("NAME---", r + " === this Error -S--1  : " +pName);

    switch (r) {
      case 1:
        pName = pName + random.nextInt(10);
        Log.e("NAME---", "this Error -S--1  : " +pName);

        break;
      case 2:
        pName = pName + random.nextInt(100);
        Log.e("NAME---", "this Error -S--2  : " +pName);

        break;
      case 3:
        pName = pName + random.nextInt(1000);
        Log.e("NAME---", "this Error -S--3  : " +pName);

        break;
      default:
        pName = random.nextInt(1000) + "_" + pName + random.nextInt(1000);
        Log.e("NAME---", "this Error -S--D  : " +pName);


    }
    return pName;
  }
/*

  public static String getPostModelToString(PostModel postModel){

    return postModel.getdUserName().toString() +"  |  "+
          postModel.getpUserName().toString() +"  |  "+
          postModel.getuserProfileUri().toString() +"  |  "+
          postModel.getpostImgUri().toString() +"  |  "+
          postModel.getpostedTime().toString() +"  |  "+
          postModel.getpostDescription().toString() +"  |  "+
          postModel.getpostTags().toString() +"  |  " ;
  };
*/

  public static String getMonthName(int month){
    String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    return monthNames[month];
  }



  // FOLLOW UNFOLLOW FUNCTION

  public static void unfollowHim(Context mContext, UserModel userModel, Button ffBtn){
        fbFs.collection(KEY_FOLLOWINGS).document(FirebaseAuth.getInstance().getCurrentUser().getUid()).update(userModel.getUserUid(), FieldValue.delete());
        fbFs.collection(KEY_FOLLOWERS).document(userModel.getUserUid()).update(FirebaseAuth.getInstance().getCurrentUser().getUid(), FieldValue.delete());
        ffBtn.setText("FOLLOW");
        ffBtn.setBackgroundColor(ContextCompat.getColor(mContext,R.color.purple_500));

  }
  public static  void followHim(Context mContext, UserModel userModel, Button ffBtn){

    Map<String,Object> fingmap = new HashMap<>();
    fingmap.put(userModel.getUserUid(),true);

    Map<String,Object> fermap = new HashMap<>();
    fermap.put(FirebaseAuth.getInstance().getCurrentUser().getUid(),true);

    fbFs.collection(KEY_FOLLOWINGS).document(FirebaseAuth.getInstance().getCurrentUser().getUid()).set(fingmap,SetOptions.merge());
    fbFs.collection(KEY_FOLLOWERS).document(userModel.getUserUid()).set(fermap,SetOptions.merge());
    ffBtn.setText("FOLLOWING");
    ffBtn.setBackgroundColor(ContextCompat.getColor(mContext,R.color.gray));
  }

  public static ArrayList<String> getcFollowingList(){

    return cFollowingList ;
  }
  public static ArrayList<String> getcFollowersList(){
    if(!isFollowersChLAttach){
      fbFs.collection(KEY_FOLLOWERS).document(cUser.getUid()).addSnapshotListener((value, error) -> {
        Map<String, Object> fingMap = value.getData();
        for(Map.Entry<String,Object> item : fingMap.entrySet()){
          cFollowersList.add(item.getKey());
          Log.e("AppUtils","FOLLOWING ITEM :  " + item.getKey());
        }
      });
      isFollowersChLAttach = true ;
    }
    return cFollowersList ;
  }

  public static ArrayList<PostModel> getUserNewsFeed(){
    for(String f : cFollowingList){
      fbFs.collection(KEY_POSTS)
            .whereEqualTo(KEY_USER_UID,f)
            .get()
            .addOnSuccessListener(queryDocumentSnapshots -> {
              for(DocumentSnapshot ds : queryDocumentSnapshots){
                userNewsFeed.add(ds.toObject(PostModel.class));
              }
            });
    }
    return userNewsFeed;
  }



  // Nullify All Static Feilds which Holder Current LoginUser Data
  public static void nullifyAll(){
    HomeFrag.homeFragInstance = null;
    SearchFrag.searchFragInstance = null;
    ProfileFrag.profileFragInstance = null  ;
    AppUtils.cUser = null;
    cFollowersList.clear();
    cFollowingList.clear();
    userNewsFeed.clear();
  }

}
