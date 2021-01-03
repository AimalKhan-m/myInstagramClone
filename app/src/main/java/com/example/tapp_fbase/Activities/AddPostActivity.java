package com.example.tapp_fbase.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tapp_fbase.Models.PostModel;
import com.example.tapp_fbase.Models.UserModel;
import com.example.tapp_fbase.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.Date;

import static com.example.tapp_fbase.AppUtils.*;

public class AddPostActivity extends AppCompatActivity {
  EditText postTv ;
  FirebaseUser cUser = FirebaseAuth.getInstance().getCurrentUser();
  OnFailureListener failureListener;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_post);

    postTv = findViewById(R.id.postETv);
    failureListener  = e -> {
      Toast.makeText(AddPostActivity.this,"Error : " +e.getMessage(),Toast.LENGTH_LONG).show();
    };
  }

  public void onClick(View view) {

    switch (view.getId()){
      case R.id.postCancelBtn:
        cFinish();
        break;
      case  R.id.addPostBtn:
        addPost();
        break;
    }

  }

  private void addPost() {

    if(postTv.getText().toString().length() > 1){

      PostModel aPost = new PostModel();
      fbFs.collection(KEY_USERS).document(cUser.getUid()).get().addOnSuccessListener(documentSnapshot -> {
        // After Success
        UserModel cUserM = documentSnapshot.toObject(UserModel.class);
        aPost.setpUserName(cUserM.getpUserName());
        aPost.setdUserName(cUserM.getdUserName());
        aPost.setPosterUid(cUserM.getUserUid());
        aPost.setPostedTime(new Date(System.currentTimeMillis()));
        aPost.setPostText(postTv.getText().toString());
          fbFs.collection(KEY_POSTS).document().set(aPost).addOnSuccessListener(aVoid -> cFinish())
          .addOnFailureListener(failureListener);

      })
      .addOnFailureListener(failureListener);
    }else {
      postTv.setError("Enter Some Text");
    }


  }

  private void cFinish() {
    finish();
  }
}