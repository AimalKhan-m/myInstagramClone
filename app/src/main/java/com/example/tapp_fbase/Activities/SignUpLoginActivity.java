package com.example.tapp_fbase.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tapp_fbase.Models.UserModel;
import com.example.tapp_fbase.R;
import com.example.tapp_fbase.databinding.ActivitySignUpLoginBinding;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import static com.example.tapp_fbase.AppUtils.*;

public class SignUpLoginActivity extends AppCompatActivity {

  public static final String KEY_USER_DOB = "userDOB";

  TextView dateTextView;
  Calendar now;
  Boolean isSignUp = true;
  ActivitySignUpLoginBinding slBinding;
  FirebaseAuth auth = FirebaseAuth.getInstance();
  FirebaseFirestore fbFs = FirebaseFirestore.getInstance();
  FirebaseUser cUser;
  String uName = "", uDOB = "", uEmail = "", uPassword = "";
  ProgressDialog progressdialog;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    slBinding = ActivitySignUpLoginBinding.inflate(getLayoutInflater());
    setContentView(slBinding.getRoot());
    progressdialog = new ProgressDialog(SignUpLoginActivity.this);
  }

  @Override
  protected void onStart() {
    super.onStart();
    cUser = auth.getCurrentUser();
    if (cUser != null) {
      startActivity(new Intent(this, HomeActivity.class));
    }
  }

  public void onClick(View v) {

    switch (v.getId())  {
      case R.id.uDOBTV_sUp:
        slBinding.uDOBTVSUp.setClickable(false);
        //  dpd.show(getSupportFragmentManager(), "Datepickerdialog");
        break;
      case R.id.togleBtn_sigUp_Lin:
        switchBwLoginSignUp();
        break;
      case R.id.signUpBtn:

        signUpUser();
        // logINFO();
        break;
      case R.id.loginBtn:
        logInUser();

        break;
    }


  }

  private void logInUser() {
    slBinding.loginBtn.setClickable(false);
    progressdialog.setMessage("Signing In ...");
    progressdialog.setCanceledOnTouchOutside(false);
    progressdialog.show();
    if (isValidEmail(slBinding.uEmailTVLogIn.getText().toString())
          && slBinding.uPasswordTVLogIn.getText().toString().length() >= 6) {
      auth.signInWithEmailAndPassword(slBinding.uEmailTVLogIn.getText().toString(),
            slBinding.uPasswordTVLogIn.getText().toString())
            .addOnSuccessListener(result -> {
              Toast.makeText(this, "LogiIn with USer : " + result.getUser().getDisplayName(), Toast.LENGTH_SHORT).show();
              startActivity(new Intent(this, HomeActivity.class));
              slBinding.loginBtn.setClickable(true);
              if (progressdialog.isShowing()) {
                Log.e("PD SHOW", progressdialog.isShowing() + " THIS I S PGGGGGGGGGG ]]]]]DISSMISS]]]]]]] ");
                slBinding.signUpBtn.setClickable(true);
                progressdialog.dismiss();
              }
              finish();
            }).addOnFailureListener(e -> {

        Toast.makeText(SignUpLoginActivity.this, "Error :  " + e.getMessage() + " : Some Fields not set Properly", Toast.LENGTH_SHORT).show();
        if (progressdialog.isShowing()) {
          Log.e("PD SHOW", progressdialog.isShowing() + " THIS I S PGGGGGGGGGG ]]]]]DISSMISS]]]]]]] ");
          slBinding.signUpBtn.setClickable(true);
          progressdialog.dismiss();
        }
      });


    } else {
      if (progressdialog.isShowing()) {
        Log.e("PD SHOW", progressdialog.isShowing() + " THIS I S PGGGGGGGGGG ]]]]]DISSMISS]]]]]]] ");
        slBinding.loginBtn.setClickable(true);
        progressdialog.dismiss();
      }
    }
  }

  private void signUpUser() {
    Log.e("PD SHOW", progressdialog.isShowing() + " THIS I S PGGGGGGGGGG ]]]]]]]]]]]] ");
    progressdialog.setMessage("Signing Up ...");
    progressdialog.setCanceledOnTouchOutside(false);
    progressdialog.show();

    if (isAllSignUpFeildsOK()) {
      slBinding.signUpBtn.setClickable(false);
      auth.createUserWithEmailAndPassword(uEmail, uPassword)
            .addOnSuccessListener(task -> {

              String pUserName = uName.trim().toLowerCase();
              pUserName = pUserName.replaceAll("\\s+", "");
              pUserName = setpUserName(uName);
              Task<Void> rqstTask = null;
              rqstTask = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).updateProfile(new UserProfileChangeRequest.Builder()
                    .setDisplayName(pUserName)
                    .setPhotoUri(Uri.parse(KEY_USER_DEFAULT_P_IMG_URL))
                    .build());
              UserModel nUser = new UserModel();
              nUser.setpUserName(pUserName);
              nUser.setdUserName(uName);
              nUser.setUserUid(FirebaseAuth.getInstance().getUid());
              nUser.setuJoinDate(new Date(System.currentTimeMillis()));
              Log.e("PNAME---", Thread.currentThread().getName() + "   : MAP -pUserName- SIZE  : " + pUserName);

              fbFs.collection(KEY_USERS).document(FirebaseAuth.getInstance().getCurrentUser().getUid()).set(nUser);


              rqstTask.addOnSuccessListener(aVoid -> {
                startActivity(new Intent(SignUpLoginActivity.this, HomeActivity.class));
                Toast.makeText(this, "SIGNED UP with USer : " + FirebaseAuth.getInstance().getCurrentUser().getDisplayName(), Toast.LENGTH_SHORT).show();
                if (progressdialog.isShowing()) {
                  Log.e("PD SHOW", progressdialog.isShowing() + " THIS I S PGGGGGGGGGG ]]]]]DISSMISS]]]]]]] ");
                  slBinding.signUpBtn.setClickable(true);
                  progressdialog.dismiss();
                }
                finish();
              });
              rqstTask.addOnFailureListener(e -> {

                Toast.makeText(SignUpLoginActivity.this, "Error :  " + e.getMessage() + " : Some Fields not set Properly", Toast.LENGTH_SHORT).show();
                if (progressdialog.isShowing()) {
                  Log.e("PD SHOW", progressdialog.isShowing() + " THIS I S PGGGGGGGGGG ]]]]]DISSMISS]]]]]]] ");
                  slBinding.signUpBtn.setClickable(true);
                  progressdialog.dismiss();
                }
              });

            });
    } else {
      Toast.makeText(this, "Invalid Field Dedicated", Toast.LENGTH_SHORT).show();
    }


  }


  public static boolean isValidEmail(CharSequence target) {
    return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
  }

  private boolean isAllSignUpFeildsOK() {
    boolean nameOK = false, dobOK = false, emailOK = false, passwordOK = false;

    if (slBinding.uNameTVSUp.getText().toString().length() != 0) {
      uName = slBinding.uNameTVSUp.getText().toString();
      nameOK = true;
    } else {
      slBinding.uNameTVSUp.setError("UnValid Name");
    }

    if (slBinding.uDOBTVSUp.getText().toString().length() != 0) {
      uDOB = slBinding.uDOBTVSUp.getText().toString();
      dobOK = true;
    } else {
      slBinding.uDOBTVSUp.setError("UnValid Date of Birth");
    }
    if (isValidEmail(slBinding.uEmailTVSUp.getText().toString())) {
      uEmail = slBinding.uEmailTVSUp.getText().toString();
      emailOK = true;

    } else {
      slBinding.uEmailTVSUp.setError("UnValid Email");
    }

    if (slBinding.uPasswrodTVSUp.getText().toString().length() >= 6) {
      uPassword = slBinding.uPasswrodTVSUp.getText().toString();
      passwordOK = true;
    } else {
      slBinding.uPasswrodTVSUp.setError("Need 6 Character Password");
    }

    return (passwordOK && emailOK && nameOK);

  }

  private void switchBwLoginSignUp() {

    if (isSignUp) {
      isSignUp = false;
      slBinding.togleBtnSigUpLin.setText("LOGIN");
      slBinding.signUpLayout.setVisibility(View.VISIBLE);
      slBinding.logInLayout.setVisibility(View.GONE);
    } else {
      isSignUp = true;
      slBinding.togleBtnSigUpLin.setText("SignUp");
      slBinding.signUpLayout.setVisibility(View.GONE);
      slBinding.logInLayout.setVisibility(View.VISIBLE);
    }

  }

}