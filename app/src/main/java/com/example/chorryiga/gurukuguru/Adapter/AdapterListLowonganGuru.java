package com.example.chorryiga.gurukuguru.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.chorryiga.gurukuguru.GlobalUse.Server;
import com.example.chorryiga.gurukuguru.Menu_Beranda.ProfilPengguna;
import com.example.chorryiga.gurukuguru.Model.ModelLowongan;
import com.example.chorryiga.gurukuguru.R;
import com.example.chorryiga.gurukuguru.SplashActivity;
import com.example.chorryiga.gurukuguru.Util.AppController;
import com.example.chorryiga.gurukuguru.Util.SessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Chorry Iga S on 19/05/2017.
 */

public class AdapterListLowonganGuru extends RecyclerView.Adapter<AdapterListLowonganGuru.ViewHolder>{
    private ArrayList<ModelLowongan> rvData;
    private Context context;
    private SplashActivity splashActivity;
    private SessionManager sessionManager;

    private String TAG = "TAG AdapterListLowonganGuru";


    public AdapterListLowonganGuru(Context context, ArrayList<ModelLowongan> inputData) {
        rvData = inputData;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        // di tutorial ini kita hanya menggunakan data String untuk tiap item
        public TextView subjek_lowongan;
        public TextView des_lowongan;
        public TextView nama_lowongan;
        public TextView jarak;
        public Button btn_ambil;
        public CircleImageView image;

        public ViewHolder(View v) {
            super(v);
            subjek_lowongan = (TextView) v.findViewById(R.id.lowongan);
            des_lowongan = (TextView) v.findViewById(R.id.lowongan_deskripsi);
            nama_lowongan = (TextView) v.findViewById(R.id.nama_lowongan);
            jarak = (TextView) v.findViewById(R.id.measure);
            btn_ambil = (Button) v.findViewById(R.id.ambil_lowongan);
            image = (CircleImageView) v.findViewById(R.id.foto_lowongan);
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // membuat view baru
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_lowonganguru, parent, false);
        // mengeset ukuran view, margin, padding, dan parameter layout lainnya
        ViewHolder vh = new ViewHolder(v);

        splashActivity = new SplashActivity();
        sessionManager = new SessionManager(context);
        return vh;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - mengambil elemen dari dataset (ArrayList) pada posisi tertentu
        // - mengeset isi view dengan elemen dari dataset tersebut
        holder.subjek_lowongan.setText(rvData.get(position).getSubjek());
        holder.des_lowongan.setText(rvData.get(position).getDeskripsi());
        holder.nama_lowongan.setText(rvData.get(position).getNama());
        holder.jarak.setText(String.valueOf(rvData.get(position).getJarak()));
        Picasso.with(context).invalidate(Server.URLpath+"upload/"+rvData.get(position).getFoto());
        Picasso.with(context).load(Server.URLpath+"upload/"+rvData.get(position).getFoto()).into(holder.image);
        holder.btn_ambil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ambilLowongan(rvData.get(position).getId_lowongan(), sessionManager.getKeyId(),position);
            }
        });

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProfilPengguna.class);
                intent.putExtra("nama",rvData.get(position).getNama());
                intent.putExtra("telp",rvData.get(position).getNo_telp());
                intent.putExtra("alamat",rvData.get(position).getAlamat());
                intent.putExtra("lat",rvData.get(position).getLat());
                intent.putExtra("lng",rvData.get(position).getLng());
                intent.putExtra("image",rvData.get(position).getFoto());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        // menghitung ukuran dataset / jumlah data yang ditampilkan di RecyclerView
        return rvData.size();
    }

    private void ambilLowongan(final int id_lowongan, final String id_guru, final int position) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Server.TRANSAKSI_LOWONGAN_CREATE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        splashActivity.GetTransaksi(sessionManager.getKeyId(),true);
                        Toast.makeText(context, "Lowongan diambil", Toast.LENGTH_SHORT).show();
                        rvData.remove(position);
                        notifyDataSetChanged();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(context, "Lowongan telah ada", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_guru",id_guru);
                params.put("id_lowongan", String.valueOf(id_lowongan));

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}
