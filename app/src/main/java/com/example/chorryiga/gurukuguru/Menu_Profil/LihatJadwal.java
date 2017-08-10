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

import com.example.chorryiga.gurukuguru.Adapter.AdapterJadwalAjar;
import com.example.chorryiga.gurukuguru.R;
import com.example.chorryiga.gurukuguru.SplashActivity;
import com.example.chorryiga.gurukuguru.Util.SessionManager;

public class LihatJadwal extends AppCompatActivity{
    private FloatingActionButton fab;
    private SessionManager sessionManager;
    private SplashActivity splashActivity;
    private Context context;

    private String TAG = "TAG LihatJadwal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lihat_jadwal);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = this;

        sessionManager = new SessionManager(context);
        splashActivity = new SplashActivity();

        fab = (FloatingActionButton) findViewById(R.id.tambah_jadwal);
        
        tampilJadwal();
        tambahJadwal();
    }

    private void tampilJadwal(){
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.list_jadwal);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm  = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        AdapterJadwalAjar adapter = new AdapterJadwalAjar(this, SplashActivity.mJadwal);
        recyclerView.setAdapter(adapter);
    }

    private void tambahJadwal() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LihatJadwal.this, TambahJadwal.class);
                startActivityForResult(intent,10);
            }
        });
    }

    @Override
    protected void onResume() {
        tampilJadwal();
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 10 && resultCode == RESULT_OK){
            splashActivity.GetJadwal(sessionManager.getKeyId());
            finish();
            startActivity(getIntent());

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
