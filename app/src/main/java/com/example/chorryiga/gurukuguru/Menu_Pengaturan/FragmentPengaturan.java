package com.example.chorryiga.gurukuguru.Menu_Pengaturan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chorryiga.gurukuguru.Menu_Registrasi.AwalInstall;
import com.example.chorryiga.gurukuguru.R;
import com.example.chorryiga.gurukuguru.SplashActivity;
import com.example.chorryiga.gurukuguru.Util.SessionManager;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentPengaturan extends Fragment {
    private TextView edit_data;
    private TextView edit_keahlian;
    private TextView edit_jadwal;
    private TextView logout;
    private TextView nama;
    private TextView email;

    private CircleImageView image;
    private Context context;
    private SessionManager sessionManager;

    private String TAG = "TAG FragmentPengaturan";
    public static FragmentPengaturan newInstance(){
        FragmentPengaturan fragment = new FragmentPengaturan();
        return fragment;
    }

    public static FragmentPengaturan newInstance(String strArg){
        FragmentPengaturan fragment = new FragmentPengaturan();
        Bundle args = new Bundle();
        args.putString("strArg1", strArg);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.activity_pengaturan,null,false);
        context = getActivity().getApplicationContext();

        sessionManager = new SessionManager(context);

        edit_data = (TextView)view.findViewById(R.id.edit_data);
        logout = (TextView) view.findViewById(R.id.logout);
        nama = (TextView) view.findViewById(R.id.nama_guru);
        email = (TextView) view.findViewById(R.id.alamat_email);
        image = (CircleImageView) view.findViewById(R.id.foto_profil);



        setData();
        buttonEditDataDiri();
        buttonLogout();
        return view;
    }

    @Override
    public void onResume() {
        setData();
        Log.d(TAG, "onResume() ");
        super.onResume();
    }

    private void setData() {
        Picasso.with(context).invalidate(SplashActivity.mGuru.get(0).getFoto());

        //load gambar dari url via picasso
        Picasso.with(context).load(SplashActivity.mGuru.get(0).getFoto()).into(image);

        nama.setText(SplashActivity.mGuru.get(0).getNama());
        email.setText(SplashActivity.mGuru.get(0).getEmail());
    }

    private void buttonLogout() {
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.setLogin(false,sessionManager.getKeyId());
                Intent intent = new Intent(context, AwalInstall.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }

    private void buttonEditDataDiri(){
        edit_data.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getActivity(), EditGuru.class);
                startActivity(intent);
            }
        });
    }
}
