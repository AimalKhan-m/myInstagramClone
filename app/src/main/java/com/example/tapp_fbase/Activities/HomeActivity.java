package com.example.tapp_fbase.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.tapp_fbase.AppUtils;
import com.example.tapp_fbase.Fragments.HomeFrag;
import com.example.tapp_fbase.Fragments.ProfileFrag;
import com.example.tapp_fbase.Fragments.SearchFrag;
import com.example.tapp_fbase.Models.HomeViewModel;
import com.example.tapp_fbase.R;
import com.example.tapp_fbase.databinding.ActivityHomeBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements ViewModelStoreOwner {

  ActivityHomeBinding homeBinding;
  BottomNavigationView mainBNV ;
  HomeViewModel homeViewModel ;

  TextView cUserTv ;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    homeBinding = ActivityHomeBinding.inflate(getLayoutInflater());
    setContentView(homeBinding.getRoot());

    cUserTv = findViewById(R.id.userName_HA);


    homeViewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
      @NonNull
      @Override
      public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return null;
      }
    }).get(HomeViewModel.class);



  /*  mainBNV = findViewById(R.id.main_BNV);
    postsList = new ArrayList<>();
    mainBNV.setSelectedItemId(R.id.homeFrag);*/


    homeBinding.addPostFab.setOnClickListener(v -> {
        Log.e("FAB","FAB C:OCL ");
        startActivity(new Intent(HomeActivity.this,AddPostActivity.class));
    });

    FragmentTransaction fragmentTransaction0 = getSupportFragmentManager().beginTransaction().replace(R.id.mainFragContainer, HomeFrag.getHomeFragInstance(), "homeFrag");
    fragmentTransaction0.commit();
    mainBNV = findViewById(R.id.main_BNV);
    mainBNV.setSelectedItemId(R.id.homeFrag);
    mainBNV.setOnNavigationItemSelectedListener(item -> {

      // MAKE CHANGES ON HOMETITLEBAR
/*      if (item.getItemId() == R.id.profileFrag) {
        homeBinding.homeTitleBar.setVisibility(View.GONE);
      } else {
        homeBinding.homeTitleBar.setVisibility(View.VISIBLE);

      }*/


      Fragment selectedFrag = null;
      String fragTag = null;
      switch (item.getItemId()) {
        case R.id.homeFrag:
          fragTag = "homeFrag";
          selectedFrag = HomeFrag.getHomeFragInstance();
          break;
        case R.id.searchFrag:
          fragTag = "searchFrag";
          selectedFrag = SearchFrag.getSearchFragInstance();
          break;
        case R.id.profileFrag:
          fragTag = "profileFrag";
          selectedFrag = ProfileFrag.getProfileFragInstance();
          break;

      }
      Log.e("FragSELECTIOn"," SELECT FRAG "  + fragTag);

      FragmentTransaction fragmentTransaction = null;
      if (getSupportFragmentManager().findFragmentByTag(fragTag) == null) {
        fragmentTransaction = getSupportFragmentManager().beginTransaction().addToBackStack(fragTag).add(R.id.mainFragContainer, selectedFrag, fragTag);
      } else {
            fragmentTransaction = getSupportFragmentManager().beginTransaction().replace(R.id.mainFragContainer, selectedFrag, fragTag);

      }

      fragmentTransaction.commit();
      return true;
    });
    getSupportFragmentManager().addOnBackStackChangedListener(() -> {
      Log.e("H_BS", "BACK STACK " + getSupportFragmentManager().getBackStackEntryCount());
      for (int i = (getSupportFragmentManager().getBackStackEntryCount() - 1); i >= 0; i--) {
        Log.e("H_BS", i + " BACKSTACK AT i : " + getSupportFragmentManager().getBackStackEntryAt(i) + "\n");
      }
      Log.e("H_BS", "==========================================================================");

    });
    mainBNV.setOnNavigationItemReselectedListener(item -> {
      Fragment selectedFrag = null;

      String fragTag = null;
      switch (item.getItemId()) {
        case R.id.homeFrag:
          fragTag = "homeFrag";
          selectedFrag = HomeFrag.getHomeFragInstance();
          break;
        case R.id.searchFrag:
          fragTag = "searchFrag";
          selectedFrag = SearchFrag.getSearchFragInstance();
          break;
        case R.id.profileFrag:
          fragTag = "profileFrag";
          selectedFrag = ProfileFrag.getProfileFragInstance();
          break;
      }
      FragmentTransaction fragmentTransaction = null;
      if (getSupportFragmentManager().findFragmentByTag(fragTag) == null) {
        fragmentTransaction = getSupportFragmentManager().beginTransaction().addToBackStack(fragTag).add(R.id.mainFragContainer, selectedFrag, fragTag);
      } else {
        fragmentTransaction = getSupportFragmentManager().beginTransaction().replace(R.id.mainFragContainer, selectedFrag, fragTag);
      }
      fragmentTransaction.commit();
    });


  }


  @Override
  protected void onStart() {
    super.onStart();
    FirebaseUser cUser = FirebaseAuth.getInstance().getCurrentUser();
    if (cUser == null) {
      startActivity(new Intent(this, SignUpLoginActivity.class));
    } else {
      if (cUser.getDisplayName() != null && cUser.getDisplayName().trim().length() > 0) {
        homeBinding.userNameHA.setText(cUser.getDisplayName() + "");
      } else {
        homeBinding.userNameHA.setText(AppUtils.setpUserName(cUser.getUid().substring(0, 9)));
      }

    }
  }

  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.logOutBtn_HA:
        logOutcUser();
    }

  }

  private void logOutcUser() {
    // Nullifying all Static Veribles Which Hold data of current User
    AppUtils.nullifyAll();

    FirebaseAuth.getInstance().signOut();
    if (FirebaseAuth.getInstance().getCurrentUser() == null) {
      finish();
      //    Toast.makeText(this, FirebaseAuth.getInstance().getCurrentUser().getDisplayName().toString() + "is SignedOut", Toast.LENGTH_SHORT).show();
      startActivity(new Intent(this, SignUpLoginActivity.class));
    }
  }

}