package com.example.tapp_fbase.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.tapp_fbase.Activities.HomeActivity;
import com.example.tapp_fbase.R;
import com.example.tapp_fbase.databinding.FragmentProfileBinding;

import java.util.concurrent.atomic.AtomicReference;

public class ProfileFrag extends Fragment {



  FragmentProfileBinding profileBinding;
  HomeActivity homeActivity;
  LinearLayout homeTitleBarHA ;

  public static AtomicReference<ProfileFrag> profileFragInstance ;
  private ProfileFrag() {
    // Required empty public constructor
  }
  public static ProfileFrag getProfileFragInstance( ){
    if(profileFragInstance == null){
      profileFragInstance = new AtomicReference<>(new ProfileFrag());
    }
    return profileFragInstance.get();
  }


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    Log.e("Frag"," PROFILE on CREATE VIEW ");

    return inflater.inflate(R.layout.fragment_profile, container, false);
  }
}