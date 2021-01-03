package com.example.tapp_fbase.Fragments;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tapp_fbase.Adapters.AdapterFF;
import com.example.tapp_fbase.Models.UserModel;
import com.example.tapp_fbase.R;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import static com.example.tapp_fbase.AppUtils.*;

public class SearchFrag extends Fragment {


  ArrayList<UserModel> usersModel = new ArrayList<>();
  AdapterFF adapterff ;
  ListenerRegistration userLR ;
  public static AtomicReference<SearchFrag> searchFragInstance ;
  private SearchFrag() {
    // Required empty public constructor
  }
  public static SearchFrag getSearchFragInstance( ){
    if(searchFragInstance == null){
      Log.e("Frag"," SEARCh  on getSearchFragInstance VIEW ");

      searchFragInstance = new AtomicReference<>(new SearchFrag());
    }
    return searchFragInstance.get();
  }



  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment

    Log.e("Frag"," SEARCh  on CREATE VIEW ");
    View v =  inflater.inflate(R.layout.fragment_search, container, false);
    RecyclerView rV  = v.findViewById(R.id.mainRvN);
    adapterff= new AdapterFF(getActivity(),usersModel);
    rV.setAdapter(adapterff);
    rV.setLayoutManager(new LinearLayoutManager(getActivity()));
    return v;
  }

  @Override
  public void onStart() {
    super.onStart();
    Log.e("FRG_LC","THIS is ----NOTIFICATION----- ON START ");
    Log.e("FRG_LC","THIS is ---NOTIFICATION------ ON RESUME ");
 if(usersModel.size() ==0 ){
   fbFs.collection(KEY_USERS).whereNotEqualTo(KEY_USER_PROFILE_NAME, FirebaseAuth.getInstance().getCurrentUser().getDisplayName()).get().addOnSuccessListener(queryDocumentSnapshots -> {
     if(queryDocumentSnapshots != null){
       usersModel.clear();
       for(DocumentSnapshot ds : queryDocumentSnapshots){
         if(ds.exists()){
           if(ds.getString(KEY_USER_PROFILE_IMG_URI) == null || ds.getString(KEY_USER_PROFILE_IMG_URI) == ""){
             Task<Void> rqstTask = null;
             rqstTask = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).updateProfile(new UserProfileChangeRequest.Builder()
                   .setPhotoUri(Uri.parse(KEY_USER_DEFAULT_P_IMG_URL))
                   .build()).addOnSuccessListener(aVoid -> {
               Map<Object,String> uImgUriMap = new HashMap<>();
               uImgUriMap.put(KEY_USER_PROFILE_IMG_URI,KEY_USER_DEFAULT_P_IMG_URL);
               fbFs.collection(KEY_USERS).document(ds.getId()).set(uImgUriMap, SetOptions.merge());
             });

           }
           usersModel.add(0,ds.toObject(UserModel.class));
           adapterff.notifyItemInserted(0);
         }
       }
     }
   });

 }
  }

}