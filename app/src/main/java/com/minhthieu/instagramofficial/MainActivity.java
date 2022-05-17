package com.minhthieu.instagramofficial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.minhthieu.instagramofficial.Fragments.HomeFragment;
import com.minhthieu.instagramofficial.Fragments.NotificationFragment;
import com.minhthieu.instagramofficial.Fragments.ProfileFragment;
import com.minhthieu.instagramofficial.Fragments.SearchFragment;

import org.jetbrains.annotations.NotNull;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private Fragment selectedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Anh xa
        AnhXa();
        // ContainOnClikcListener
        ContainOnClikcListener();
    }

    private void ContainOnClikcListener() {
        // app bar LISTENER
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_home:
                        selectedFragment = new HomeFragment();
                        break;

                    case R.id.nav_search:
                        selectedFragment = new SearchFragment();
                        break;

                    case R.id.nav_video:

                        break;

                    case R.id.nav_heart:
                        selectedFragment = new NotificationFragment();
                        break;

                    case R.id.nav_profile:
                        selectedFragment = new ProfileFragment();
                        break;

                }
                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();


                }

                return true;
            }
        });
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();

    }

    private void AnhXa() {
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

    }


}