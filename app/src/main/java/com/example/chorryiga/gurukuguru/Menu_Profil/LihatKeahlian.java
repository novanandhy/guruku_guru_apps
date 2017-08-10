package com.example.chorryiga.gurukuguru.Menu_Profil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.example.chorryiga.gurukuguru.Adapter.AdapterListKeahlianGuru;
import com.example.chorryiga.gurukuguru.R;
import com.example.chorryiga.gurukuguru.SplashActivity;
import com.example.chorryiga.gurukuguru.Util.SessionManager;

public class LihatKeahlian extends AppCompatActivity {
    private FloatingActionButton fab;
    private SplashActivity splashActivity;
    private SessionManager sessionManager;

    private Context context;

    private String TAG = "TAG LihatKeahlian";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lihat_keahlian);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = this;

        fab = (FloatingActionButton) findViewById(R.id.tambah_keahlian);

        splashActivity = new SplashActivity();
        sessionManager = new SessionManager(context);

        tampilKeahlian();
        tambahKeahlian();
    }

    public void tampilKeahlian(){
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.list_keahlian);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm  = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        AdapterListKeahlianGuru adapter = new AdapterListKeahlianGuru(this, SplashActivity.mSkill);
        recyclerView.setAdapter(adapter);
    }

    private void tambahKeahlian() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LihatKeahlian.this, TambahKeahlian.class);
                startActivityForResult(intent,10);
            }
        });
    }

    @Override
    public void onResume() {
        tampilKeahlian();
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 10 && resultCode == RESULT_OK){
            splashActivity.GetSkill(sessionManager.getKeyId());
            tampilKeahlian();

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
