package com.example.chorryiga.gurukuguru.Menu_Beranda;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chorryiga.gurukuguru.GlobalUse.Server;
import com.example.chorryiga.gurukuguru.R;
import com.squareup.picasso.Picasso;

public class ProfilPengguna extends Activity {
    private Context context;

    private ImageView image_profil;
    private TextView nama_profil, alamat_profil, telp_profil;
    private ImageButton btn, btn_call;

    private String alamat, nama, telp, lat, lng, path;
    private String TAG = "TAG ProfilPengguna";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_pengguna);
        context = this;

        nama_profil = (TextView) findViewById(R.id.profil_nama);
        telp_profil = (TextView) findViewById(R.id.telepon_profil);
        alamat_profil = (TextView) findViewById(R.id.alamat_profil);
        image_profil = (ImageView) findViewById(R.id.image);
        btn = (ImageButton) findViewById(R.id.button_location);
        btn_call = (ImageButton) findViewById(R.id.button_call);

        ambilDataIntent();
        setData();
        buttonLocation();
        buttonCall();
    }

    private void buttonCall() {
        btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + telp));
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(callIntent);
            }
        });
    }

    private void buttonLocation() {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://www.google.com/maps/place/"+lat+","+lng);
                Log.d(TAG, "URI: "+uri);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }

    private void setData() {
        nama_profil.setText(nama);
        telp_profil.setText(telp);
        alamat_profil.setText(alamat);
        Picasso.with(context).load(Server.URLpath+"upload/"+path).into(image_profil);
    }

    private void ambilDataIntent() {
        Intent intent = getIntent();

        nama = intent.getExtras().getString("nama");
        telp = intent.getExtras().getString("telp");
        alamat = intent.getExtras().getString("alamat");
        lat = intent.getExtras().getString("lat");
        lng = intent.getExtras().getString("lng");
        path = intent.getExtras().getString("image");


    }
}
