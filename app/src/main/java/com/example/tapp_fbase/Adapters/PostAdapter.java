package com.example.tapp_fbase.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tapp_fbase.Models.PostModel;
import com.example.tapp_fbase.R;
import com.example.tapp_fbase.databinding.PostItemBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import static com.example.tapp_fbase.AppUtils.*;
import static com.example.tapp_fbase.AppUtils.getMonthName;

public class PostAdapter extends   RecyclerView.Adapter<PostAdapter.PostViewHolder> {
      Context mContext;
      ArrayList<PostModel> postsList ;
      FirebaseUser cUser = FirebaseAuth.getInstance().getCurrentUser();
  public PostAdapter(Context mContext, @Nonnull ArrayList<PostModel> postsList) {
      this.mContext = mContext;
      this.postsList = postsList;
      }

@NonNull
@Override
public PostAdapter.PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      PostItemBinding pb = PostItemBinding.inflate( LayoutInflater.from(parent.getContext()));
      return new PostAdapter.PostViewHolder(pb);
      }

@Override
public void onBindViewHolder(@NonNull PostAdapter.PostViewHolder holder, int position) {
      holder.setData(position);
      }

@Override
public int getItemCount() {
      return postsList.size();
      }

  class PostViewHolder extends RecyclerView.ViewHolder{

  RecyclerView tagsRv ;
  ImageView postIv ;
  TextView postDesc ;
  PostItemBinding pb ;


  ListenerRegistration likesLR , commentsLR , sharesLR ;


  String cPostID ;
  public PostViewHolder(@NonNull PostItemBinding pb) {
    super(pb.getRoot());
    View itemView = pb.getRoot();
    this.pb = pb;

    postDesc  = itemView.findViewById(R.id.postTextView_pL);
  //  tagsRv.setLayoutManager(new GridLayoutManager(mContext,1,RecyclerView.HORIZONTAL,false));

  }
  public void setData(int position){
    PostModel cPost = postsList.get(position);
   // cPostID = cPost.getPostID();

    Calendar calendar = Calendar.getInstance();
    calendar.setTime(cPost.getPostedTime());

    pb.userNameTvPL.setText(cPost.getpUserName());
    pb.userDnameTvPL.setText(cPost.getdUserName());
    pb.postAddDatePL.setText(calendar.get(Calendar.DATE) + " " + getMonthName(calendar.get(Calendar.MONTH)).substring(0,3) + " [ " + calendar.get(Calendar.HOUR) + " : " + calendar.get(Calendar.MINUTE) + " : " + calendar.get(Calendar.SECOND) + " : " + ( calendar.get(Calendar.AM_PM) == Calendar.AM ? "AM" : "PM") + " ]");
    pb.postTextViewPL.setText(cPost.getPostText()+"");


    /*if(cPost.getpostTags().size() > 0) {
      tagsRv.setAdapter(new TagsRvAdapter(mContext, cPost.getpostTags()));
    }*/



  }

 /* private void setPostsBtnFn() {
    // LIKE BTN FUnCTIONAlity

    fbFs.collection(KEY_LIKES).document(cPostID).get().addOnSuccessListener(documentSnapshot -> {
      if(documentSnapshot.exists()){
        if(documentSnapshot.getBoolean(cUser.getUid()) != null){
          pb.likeBtnPL.setColorFilter(ContextCompat.getColor(mContext, R.color.likePink), android.graphics.PorterDuff.Mode.MULTIPLY);
        }
      }
    });
    if(likesLR != null){
      likesLR.remove();
    }
    likesLR =  fbFs.collection(KEY_LIKES).document(cPostID).addSnapshotListener((value, error) -> {

      if(error!=null){
        Toast.makeText(mContext,"Error : "+ error.getMessage(),Toast.LENGTH_LONG ).show();
      }
      else{
        if(value.getData() != null){
          pb.likesTvPL.setText(value.getData().size() > 0 ? value.getData().size() +"" : "" +"");
        }
      }

    });
    fbFs.collection(KEY_LIKES).get().addOnSuccessListener(queryDocumentSnapshots -> {
      if(queryDocumentSnapshots.isEmpty()){
      }
    });
    //CLICK Listener
     pb.likeBtnPL.setOnClickListener(v -> {
      pb.likeBtnPL.setClickable(false);
      fbFs.collection(KEY_LIKES).document(cPostID).get()
            .addOnSuccessListener(documentSnapshot -> {
              Map<String, Object> data = new HashMap<>();
              if(documentSnapshot.exists()){
                if(documentSnapshot.getBoolean(cUser.getUid()) != null){
                  pb.likeBtnPL.setColorFilter(ContextCompat.getColor(mContext, R.color.gray), android.graphics.PorterDuff.Mode.MULTIPLY);
                  fbFs.collection(KEY_LIKES).document(cPostID).update(cUser.getUid(), FieldValue.delete());
                }else{
                  data.put(cUser.getUid(), true);
                  fbFs.collection(KEY_LIKES).document(cPostID).set(data, SetOptions.merge());
                  pb.likeBtnPL.setColorFilter(ContextCompat.getColor(mContext, R.color.likePink), android.graphics.PorterDuff.Mode.MULTIPLY);
                }
              }else{
                data.put(cUser.getUid(), true);
                fbFs.collection(KEY_LIKES).document(cPostID).set(data, SetOptions.merge());
                pb.likeBtnPL.setColorFilter(ContextCompat.getColor(mContext, R.color.likePink), android.graphics.PorterDuff.Mode.MULTIPLY);


              }
              pb.likeBtnPL.setClickable(true);
            }).addOnFailureListener(e -> pb.likeBtnPL.setClickable(true));
    });
  }*/

}

  public void additem(PostModel post){
    postsList.add(0,post);
    notifyItemInserted(0);
    //  notifyDataSetChanged();
  }
}