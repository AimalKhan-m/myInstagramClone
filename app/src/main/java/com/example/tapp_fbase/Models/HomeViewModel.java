package com.example.tapp_fbase.Models;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.Map;

import static com.example.tapp_fbase.AppUtils.*;

public class HomeViewModel extends ViewModel {
/*

  //Current USer  FOLLOWING AND FOLLOWERS ARRAYLISt
  private static  cFollowingList = new ArrayList<>();
  private static ArrayList<String> cFollowersList = new ArrayList<>();
  private static Boolean isFollowingChLAttach = false;
  private static Boolean isFollowersChLAttach = false;


  private static ArrayList<PostModel> userNewsFeed = new ArrayList<>() ;
*/

  private ListenerRegistration nfShapShotChListener = null ;

  private MutableLiveData<ArrayList<String>> cFollowingList_LD = new MutableLiveData<>(new ArrayList<>());
  private MutableLiveData<ArrayList<String>> cFollowersList_LD = new MutableLiveData<>(new ArrayList<>());
  // Current User NewsFeed
  private MutableLiveData<ArrayList<PostModel>> userNewsFeed_LD = new MutableLiveData<>(new ArrayList<>());
  // EVent Change Listners Booleans
  private Boolean isFollowingChLAttach = false;
  private Boolean isFollowersChLAttach = false;
  private Boolean isNewsFeedChLAttach = false;



  public LiveData<ArrayList<String>> getcFollowingList_LD() {

    if (!isFollowingChLAttach) {
      fbFs.collection(KEY_FOLLOWINGS).document(cUser.getUid()).addSnapshotListener((value, error) -> {
        Map<String, Object> fingMap = value.getData();
        for (Map.Entry<String, Object> item : fingMap.entrySet()) {
          cFollowingList_LD.getValue().add(item.getKey());
          cFollowingList_LD.setValue(cFollowingList_LD.getValue());
          Log.e("AppUtils", "FOLLOWING ITEM :  " + item.getKey());
        }
      });
      isFollowingChLAttach = true;
    }

    return cFollowingList_LD;
  }

  public LiveData<ArrayList<String>> getcFollowersList_LD() {


    return cFollowersList_LD;
  }

  public LiveData<ArrayList<PostModel>> getUserNewsFeed_LD() {

  if(nfShapShotChListener == null){
   nfShapShotChListener = fbFs.collection(KEY_FOLLOWINGS).document(cUser.getUid()).addSnapshotListener((value, error) -> {
     Map<String, Object> fingMap = value.getData();
     for (Map.Entry<String, Object> item : fingMap.entrySet()) {
       cFollowingList_LD.getValue().add(item.getKey());
       cFollowingList_LD.setValue(cFollowingList_LD.getValue());
       Log.e("HomeView", cFollowingList_LD.getValue().size() + " size : FOLLOWING ITEM :  " + item.getKey());
     }
     if (cFollowingList_LD.getValue().size() > 0) {
       for (String f : cFollowingList_LD.getValue()) {
         fbFs.collection(KEY_POSTS)
               .whereEqualTo(KEY_POST_POSTER_UID, f)
               .get()
               .addOnSuccessListener(queryDocumentSnapshots -> {
                 Log.e("HomeView","in For loop  f : " + queryDocumentSnapshots.size());

                 ArrayList<PostModel>  pm = new ArrayList<>();
                 for (DocumentSnapshot ds : queryDocumentSnapshots) {
                   pm=  userNewsFeed_LD.getValue();
                   pm.add(ds.toObject(PostModel.class));
                   Log.e("HomeView", "POST ITEM :  "
                         + ds.toObject(PostModel.class).getPostText());

                 }

                 userNewsFeed_LD.setValue(pm);

               });
       }
     }


   });
    }
    isFollowingChLAttach = true;

      return userNewsFeed_LD;

}







}
