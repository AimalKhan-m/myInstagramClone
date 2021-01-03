package com.example.tapp_fbase.Fragments;

import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.tapp_fbase.Adapters.PostAdapter;
import com.example.tapp_fbase.Models.HomeViewModel;
import com.example.tapp_fbase.Models.PostModel;
import com.example.tapp_fbase.R;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicReference;

import static com.example.tapp_fbase.AppUtils.*;

public class HomeFrag extends Fragment {

  HomeViewModel homeViewModel ;

  RecyclerView postRv ;
  ArrayList<PostModel> postsList = new ArrayList<>();
  PostAdapter postAdapter;

  Boolean isFistRun = true ;
  EventListener<QuerySnapshot> postChEventListener;
  ListenerRegistration postChEventListenerRegister ;
  public static AtomicReference<HomeFrag> homeFragInstance = null;
  public static HomeFrag getHomeFragInstance(){
    if(homeFragInstance==null){
      homeFragInstance = new AtomicReference<>(new HomeFrag());
    }
    return homeFragInstance.get();
  }

  private HomeFrag() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.e("Frag_HOME"," on CREATE  --------------  ");


   /* postChEventListener =  (value, error) -> {
      if(error!=null){
        Toast.makeText(getActivity(),"Error : "+ error.getMessage(),Toast.LENGTH_LONG ).show();
      }
      else{
        assert value != null;
        for (DocumentChange dc : value.getDocumentChanges()) {

          Log.e("loggg_home_change", value.getDocumentChanges().size() + " SIZE < - >  change " + dc.getType());
          switch (dc.getType()) {
            case ADDED:
              if(!isFistRun){
                PostModel postModel = dc.getDocument().toObject(PostModel.class);
                //  postAdapter.additem(postModel);
                postsList.add(0,postModel);
                postAdapter.notifyItemInserted(0);
                //postsAdapter.notifyDataSetChanged();
                Log.e("loggg_home_change", postsList.size() + " >>> POST LIST SIZE +++++++ ADDED DOC \n\n" +
                      "________________________________________________________________\n" +
                      //   AppUtils.getPostModelToString(dc.getDocument().toObject(PostModel.class)) +
                      postModel.getdUserName().toString() +"  |  "+
                      postModel.getpUserName().toString() +"  |  "+
                      postModel.getPostedTime().toString() +"  |  "+
                      postModel.getPostText().toString() +"  |  "+
                      "\n\n ________________________________________________________________"
                );

              }
              break;
            case REMOVED:
                                  *//*com.example.poster_1.PostModel pm = dc.getDocument().toObject(com.example.poster_1.PostModel.class);
                                  int i = 0;
                                  for (com.example.poster_1.PostModel pM : postList) {
                                      if ((pM.getPostText().equals( dc.getDocument().getString(KEY_POST_TEXT)) && pM.getUserID().equals( dc.getDocument().getString(KEY_USER_ID)))) {
                                          postList.remove(i);
                                          postsAdapter.notifyItemRemoved(i);

                                          Log.e("loggg_home_R_DATA", "\n ----- " +  pm.getPostText().trim()  + "\n " );

                                          Log.e("loggg_home_R_DATA", "\n ----- " +  pM.getPostText().trim()  + "\n " );

                                          Log.e("loggg_home_change_R",  postList.size() + "------ REMOVE REPOST AT ---- : " + i);

                                          //  postsAdapter.notifyDataSetChanged();

                                          break;
                                      }
                                      i++;
                                  }*//*
              break;

          }

        }
        if(isFistRun)
          isFistRun = false;
      }
    };
    postChEventListenerRegister =  fbFs.collection(KEY_POSTS)
          .addSnapshotListener(postChEventListener);*/
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View v = inflater.inflate(R.layout.fragment_home, container, false);
    Log.e("Frag_HOME"," on CREATE VIEW ");
    isFistRun = true;
    postAdapter = new PostAdapter(getActivity(),postsList);
    postRv = v.findViewById(R.id.postRv_hF);
    postRv.setAdapter(postAdapter);
    postRv.setLayoutManager(new GridLayoutManager(getActivity(),1));
    postRv.addItemDecoration(new SpacesItemDecoration(10));

    /*  if(isFistRun==true && postsList.size() < 1){
        loadPost();
      }*/
    return v;
  }

  private void loadPost() {

    /*fbFs.collection(KEY_POSTS)
          .whereEqualTo(KEY_USER_PROFILE_NAME,"a1")
          .get().addOnSuccessListener(queryDocumentSnapshots -> {
      if(!queryDocumentSnapshots.isEmpty()){
        postsList.clear();
        Log.e("HF_postLoad","SIZE items ..." + queryDocumentSnapshots.size());

        for(DocumentSnapshot ds : queryDocumentSnapshots){
          if(ds.exists()) {
            PostModel postModel = ds.toObject(PostModel.class);
            postsList.add(postsList.size(),postModel);
            Log.e("HF_postLoad","Loding items ...");
          }
        }
        if(postsList.size()>1){
          Collections.sort(postsList, (o1, o2) -> o1.getPostedTime().compareTo(o2.getPostedTime()));
          Collections.reverse(postsList);
        }
      }
    });*/

    postsList = getUserNewsFeed();
    postAdapter.notifyDataSetChanged();


  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);


    homeViewModel = new ViewModelProvider(getActivity())
          .get(HomeViewModel.class);


    homeViewModel.getUserNewsFeed_LD().observe(this, postModels -> {
        postsList.clear();
        postsList.addAll(postModels) ;
        Log.e("homeFrag","Data Changed NF : " + postModels.size());
        postAdapter.notifyDataSetChanged();
    });

  }

  @Override
  public void onPause() {
    super.onPause();

  }


  public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public SpacesItemDecoration(int space) {
      this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
      outRect.left = space;
      outRect.right = space;
      outRect.bottom = space;

      // Add top margin only for the first item to avoid double space between items
      if (parent.getChildLayoutPosition(view) == 0) {
        outRect.top = space;
      } else {
        outRect.top = 0;
      }
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    Log.e("Frag"," HOME FRAGon DEATTACH VIEW ");

  }

  @Override
  public void onStop() {
    super.onStop();

    Log.e("Frag"," HOME FRAGon STOP VIEW ");

  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    if (postChEventListenerRegister != null){
      postChEventListenerRegister.remove();
    }

    Log.e("Frag"," HOME FRAGon DESTORYED VIEW ");

  }
}