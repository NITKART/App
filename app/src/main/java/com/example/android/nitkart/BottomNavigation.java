package com.example.android.nitkart;
//do not use
//import android.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

public class BottomNavigation extends AppCompatActivity {
    public ActionBar toolbar;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);

        toolbar = getSupportActionBar();

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        toolbar.setTitle("Store");
        loadFragment(new StoreFragment());
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_shop:
                    toolbar.setTitle("Store");
                    loadFragment(new StoreFragment());
                    return true;
                case R.id.navigation_cart:
//                    fragment = new SellFragment();
//                    loadFragment(fragment);
                    Intent intent=new Intent(getApplicationContext(),SellActivity.class);
                    startActivity(intent);
                    finish();
                    return true;
                case R.id.navigation_profile:
                    toolbar.setTitle("Profile");
                    loadFragment(new ProfileFragment());
                    return true;
            }
            return false;
        }
    };
//
//    @Override
//    public void onBackPressed() {
//        if (fragment == storeFragment) {
//            finish();
//        } else {
//            loadFragment(storeFragment);
//            fragment = storeFragment;
//        }
//
//    }
@Override
public void onBackPressed() {
    if (doubleBackToExitPressedOnce) {
        finish();
        return;
    }

    this.doubleBackToExitPressedOnce = true;
    Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

    new Handler().postDelayed(new Runnable() {

        @Override
        public void run() {
            doubleBackToExitPressedOnce=false;
        }
    }, 2000);
}

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
