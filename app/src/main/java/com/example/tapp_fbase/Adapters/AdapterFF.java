package com.example.tapp_fbase.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.example.tapp_fbase.AppUtils;
import com.example.tapp_fbase.Models.UserModel;
import com.example.tapp_fbase.R;
import com.example.tapp_fbase.databinding.FollowFollowingItemBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

import static com.example.tapp_fbase.AppUtils.*;
import static com.example.tapp_fbase.AppUtils.fbFs;

public class AdapterFF extends RecyclerView.Adapter<AdapterFF.FFViewHodler>{

  Context mContext ;
  ArrayList<UserModel> usersList ;
  FirebaseUser cUser = FirebaseAuth.getInstance().getCurrentUser();


  public AdapterFF(@NonNull Context mContext, @NonNull ArrayList<UserModel> usersList) {
    this.mContext = mContext;
    this.usersList = usersList;
  }

  @NonNull
  @Override
  public FFViewHodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    FollowFollowingItemBinding ffBinding = FollowFollowingItemBinding.inflate(LayoutInflater.from(parent.getContext()));
    return new FFViewHodler(ffBinding);
  }

  @Override
  public void onBindViewHolder(@NonNull FFViewHodler holder, int position) {
        holder.setDate(position);
  }

  @Override
  public int getItemCount() {
    return usersList.size();
  }


  class FFViewHodler extends RecyclerView.ViewHolder{
    FollowFollowingItemBinding ffBinding ;
    public FFViewHodler(@NonNull FollowFollowingItemBinding ffBinding) {
      super(ffBinding.getRoot());
      this.ffBinding = ffBinding ;
    }

    public void setDate(int position) {
      UserModel cUserModel = usersList.get(position);



      String  cPname = cUserModel.getpUserName();
      String cDname = cUserModel.getdUserName();

      ffBinding.profileAvatarFfi.setImageResource(R.drawable.d_profile_img);
      ffBinding.dUserNameFfi.setText(cDname);
      ffBinding.pUserNameFfi.setText(cPname);

        setFFbtn(cUserModel);
    }

    private void setFFbtn(UserModel cUserModel) {
      Log.e("CLICL FF"," IT FUNCTION ");

    ffBinding.ffBTNFfi.setOnClickListener(v -> {
      Log.e("CLICL FF"," IT LAMDA UNDER  ");
      ffBinding.ffBTNFfi.setClickable(false);
      fbFs.collection(KEY_FOLLOWINGS).document(cUser.getUid()).get()
            .addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()){
              if(documentSnapshot.getBoolean(cUserModel.getUserUid()) != null){
                DialogInterface.OnClickListener ynClcik  = (d, w) -> {
                  switch(w){
                    case DialogInterface.BUTTON_POSITIVE:
                      unfollowHim(mContext,cUserModel,ffBinding.ffBTNFfi);
                      break;
                    case DialogInterface.BUTTON_NEGATIVE:

                      break ;
                  }
                };
                AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
                ab.setTitle("UnFollow " + cUserModel.getdUserName() + "?")
                      .setMessage("Are u Sure ?")
                      .setPositiveButton("UnFollow",ynClcik)
                      .setNegativeButton("Cancel",ynClcik)
                      .show();
                Log.e("CLICL FF"," IT LAMDA  if  ----   " + documentSnapshot.getBoolean(cUserModel.getUserUid()));

              }else{
                Log.e("CLICL FF"," IT LAMDA  if  ----   " + documentSnapshot.getBoolean(cUserModel.getUserUid()));
                followHim(mContext,cUserModel,ffBinding.ffBTNFfi);
              }
            }
            else{
              Log.e("CLICL FF"," IT LAMDA  if  ----   " + documentSnapshot.getBoolean(cUserModel.getUserUid()));
              followHim(mContext,cUserModel,ffBinding.ffBTNFfi);
            }
              ffBinding.ffBTNFfi.setClickable(true);
            })
            .addOnFailureListener(new OnFailureListener() {
              @Override
              public void onFailure(@NonNull Exception e) {
                ffBinding.ffBTNFfi.setClickable(true);
              }
            });
    });
      fbFs.collection(KEY_FOLLOWINGS).document(cUser.getUid()).get()
            .addOnSuccessListener(documentSnapshot -> {
        if (documentSnapshot.exists()){
          if(documentSnapshot.getBoolean(cUserModel.getUserUid()) != null){
            ffBinding.ffBTNFfi.setText("FOLLOWING");
            ffBinding.ffBTNFfi.setBackgroundColor(ContextCompat.getColor(mContext,R.color.gray));
          }else{
            ffBinding.ffBTNFfi.setText("FOLLOW");
            ffBinding.ffBTNFfi.setBackgroundColor(ContextCompat.getColor(mContext,R.color.purple_500));
          }
        }
      });


    }
  }

}




