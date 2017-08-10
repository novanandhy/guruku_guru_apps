package com.example.chorryiga.gurukuguru.Menu_Profil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chorryiga.gurukuguru.R;
import com.example.chorryiga.gurukuguru.SplashActivity;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentProfilGuru extends Fragment {
    private ImageView lihat_keahlian;
    private ImageView lihat_jadwal;
    private ImageView lihat_lowongan;
    private ImageView lihat_booking;
    private CircleImageView foto_profil;
    private TextView nama_profil, nama, usia, kelamin, alamat, pendidikan, ipk;

    private Context context;

    public static FragmentProfilGuru newInstance(){
        FragmentProfilGuru fragment = new FragmentProfilGuru();
        return fragment;
    }

    public static FragmentProfilGuru newInstance(String strArg){
        FragmentProfilGuru fragment = new FragmentProfilGuru();
        Bundle args = new Bundle();
        args.putString("strArg1", strArg);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.activity_profil_guru,null,false);
        context = getActivity().getApplicationContext();

        lihat_keahlian = (ImageView)view.findViewById(R.id.lihat_keahlian);
        lihat_jadwal = (ImageView)view.findViewById(R.id.lihat_jadwal);
        lihat_lowongan = (ImageView)view.findViewById(R.id.lihat_lowongan);
        lihat_booking = (ImageView)view.findViewById(R.id.lihat_booking);

        foto_profil = (CircleImageView) view.findViewById(R.id.foto_profil);
        nama_profil = (TextView) view.findViewById(R.id.name_profil);
        nama = (TextView) view.findViewById(R.id.nama_guru);
        usia = (TextView) view.findViewById(R.id.usia_guru);
        kelamin = (TextView) view.findViewById(R.id.jenis_kelamin);
        alamat = (TextView) view.findViewById(R.id.alamat_guru);
        pendidikan = (TextView) view.findViewById(R.id.pend_guru);
        ipk = (TextView) view.findViewById(R.id.ipk_guru);

        SetData();
        lihat_keahlian.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getActivity(), LihatKeahlian.class);
                startActivity(intent);
            }
        });

        lihat_jadwal.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getActivity(), LihatJadwal.class);
                startActivity(intent);
            }
        });

        lihat_lowongan.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getActivity(), LihatLowongan.class);
                startActivity(intent);
            }
        });

        lihat_booking.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getActivity(), LihatBooking.class);
                startActivity(intent);
            }
        });



        return view;
    }

    @Override
    public void onResume() {
        SetData();
        super.onResume();
    }

    private void SetData() {
        String klmn;
        if (SplashActivity.mGuru.get(0).getKelamin().contains("L")){
            klmn = "Laki-laki";
        }else{
            klmn = "Perempuan";
        }

        nama_profil.setText(SplashActivity.mGuru.get(0).getNama());
        nama.setText(SplashActivity.mGuru.get(0).getNama());
        kelamin.setText(klmn);
        alamat.setText(SplashActivity.mGuru.get(0).getAlamat());
        pendidikan.setText(SplashActivity.mGuru.get(0).getPendidikan()+" "+SplashActivity.mGuru.get(0).getJurusan());
        usia.setText(""+SplashActivity.mGuru.get(0).getUsia());
        ipk.setText(""+SplashActivity.mGuru.get(0).getIpk());

        //membersihkan cache foto picasso
        Picasso.with(context).invalidate(SplashActivity.mGuru.get(0).getFoto());

        //load gambar dari url via picasso
        Picasso.with(context).load(SplashActivity.mGuru.get(0).getFoto()).into(foto_profil);
    }
}
