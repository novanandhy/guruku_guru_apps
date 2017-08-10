package com.example.chorryiga.gurukuguru.Menu_Beranda;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chorryiga.gurukuguru.Adapter.AdapterListLowonganGuru;
import com.example.chorryiga.gurukuguru.R;
import com.example.chorryiga.gurukuguru.SplashActivity;

public class FragmentHomeGuru extends Fragment {
    private RecyclerView rvView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private Context context;

    private String TAG = "TAG_FragmentHomeGuru";

    public static FragmentHomeGuru newInstance(){
        FragmentHomeGuru fragment = new FragmentHomeGuru();
        return fragment;
    }

    public static FragmentHomeGuru newInstance(String strArg){
        FragmentHomeGuru fragment = new FragmentHomeGuru();
        Bundle args = new Bundle();
        args.putString("strArg1", strArg);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home_guru,null,false);
        context = getActivity().getApplicationContext();

        rvView = (RecyclerView) view.findViewById(R.id.rv_main);
        rvView.setHasFixedSize(true);
        /**
         * Kita menggunakan LinearLayoutManager untuk list standar
         * yang hanya berisi daftar item
         * disusun dari atas ke bawah
         */
        layoutManager = new LinearLayoutManager(getActivity());
        rvView.setLayoutManager(layoutManager);

        adapter = new AdapterListLowonganGuru(context,SplashActivity.mLowongan);
        rvView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
