package com.example.chorryiga.gurukuguru;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.chorryiga.gurukuguru.Menu_Beranda.FragmentHomeGuru;
import com.example.chorryiga.gurukuguru.Menu_Pengaturan.FragmentPengaturan;
import com.example.chorryiga.gurukuguru.Menu_Profil.FragmentProfilGuru;
import com.example.chorryiga.gurukuguru.Menu_Review.FragmentReviewGuru;


public class Home extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FragmentTransaction fragmentTransaction;
    Fragment fragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragment = FragmentHomeGuru.newInstance();
        fragmentTransaction.replace(R.id.fragment_container,fragment);
        fragmentTransaction.commit();
        //Bottom Navigation
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.btm_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.action_home:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragment = FragmentHomeGuru.newInstance();
                        fragmentTransaction.replace(R.id.fragment_container,fragment);
                        fragmentTransaction.commit();
                        break;
                    case R.id.action_profil:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragment = FragmentProfilGuru.newInstance();
                        fragmentTransaction.replace(R.id.fragment_container,fragment);
                        fragmentTransaction.commit();
                        break;
                    case R.id.action_review:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragment = FragmentReviewGuru.newInstance();
                        fragmentTransaction.replace(R.id.fragment_container,fragment);
                        fragmentTransaction.commit();
                        break;
                    case R.id.action_setting:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragment = FragmentPengaturan.newInstance();
                        fragmentTransaction.replace(R.id.fragment_container,fragment);
                        fragmentTransaction.commit();
                        break;
                }

                return true;
            }
        });
    }
}
