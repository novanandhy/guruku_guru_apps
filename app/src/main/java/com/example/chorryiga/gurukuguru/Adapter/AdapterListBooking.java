package com.example.chorryiga.gurukuguru.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
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
import com.example.chorryiga.gurukuguru.Model.ModelBooking;
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
 * Created by Novan on 06/08/2017.
 */

public class AdapterListBooking extends RecyclerView.Adapter<AdapterListBooking.ViewHolder>{
    private ArrayList<ModelBooking> rvData;
    private Context context;
    private SessionManager sessionManager;
    private SplashActivity splashActivity;

    private String TAG = "TAG_AdapterListLowonganDiambil";


    public AdapterListBooking(Context context, ArrayList<ModelBooking> inputData) {
        rvData = inputData;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        // di tutorial ini kita hanya menggunakan data String untuk tiap item
        public TextView nama_ortu;
        public TextView keterangan;
        public Button btn_konfirmasi;
        public CircleImageView image;

        public ViewHolder(View v) {
            super(v);
            nama_ortu = (TextView) v.findViewById(R.id.nama_ortu);
            keterangan = (TextView) v.findViewById(R.id.booking_keterangan);
            btn_konfirmasi = (Button) v.findViewById(R.id.konfirmasi);
            image = (CircleImageView) v.findViewById(R.id.foto_ortu);
        }
    }
    @Override
    public AdapterListBooking.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // membuat view baru
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_booking, parent, false);
        // mengeset ukuran view, margin, padding, dan parameter layout lainnya
        AdapterListBooking.ViewHolder vh = new AdapterListBooking.ViewHolder(v);

        sessionManager = new SessionManager(context);
        splashActivity = new SplashActivity();
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - mengambil elemen dari dataset (ArrayList) pada posisi tertentu
        // - mengeset isi view dengan elemen dari dataset tersebut
        if (rvData.get(position).getStatus().contains("1")){
            holder.btn_konfirmasi.setText("TERKONFIRMASI");
            holder.btn_konfirmasi.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.btn_konfirmasi.setBackgroundColor(context.getResources().getColor(android.R.color.white));
        }else{
            holder.btn_konfirmasi.setText("KONFIRMASI");
            holder.btn_konfirmasi.setTextColor(context.getResources().getColor(android.R.color.white));
            holder.btn_konfirmasi.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        }


        holder.nama_ortu.setText(rvData.get(position).getNama());
        holder.keterangan.setText(rvData.get(position).getKeterangan());
        Picasso.with(context).invalidate(Server.URLpath+"upload/"+rvData.get(position).getFoto());
        Picasso.with(context).load(Server.URLpath+"upload/"+rvData.get(position).getFoto()).into(holder.image);
        holder.btn_konfirmasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                if (rvData.get(position).getStatus().contains("1")){
                    alertDialogBuilder.setMessage("Batalkan konfirmasi ?");
                }else{
                    alertDialogBuilder.setMessage("Konfirmasi pesanan ini ?");
                }
                alertDialogBuilder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        konfirmasiBooking(rvData.get(position).getId_user(),sessionManager.getKeyId(),position,rvData.get(position).getStatus());
                    }
                });
                alertDialogBuilder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
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

    private void konfirmasiBooking(final String id_user, final String id_guru, final int position, final String status) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Server.BOOKING_UPDATE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "get transaksi Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        if (status.contains("1")){
                            SplashActivity.mBooking.get(position).setStatus("0");
                        }else{
                            SplashActivity.mBooking.get(position).setStatus("1");
                        }


                        splashActivity.GetBooking(sessionManager.getKeyId());
                        notifyDataSetChanged();


                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(context, "tidak dapat megkonfirmasi", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                String status_pass;
                if (status.contains("1")){
                    status_pass ="0";
                }else {
                    status_pass = "1";
                }

                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();

                params.put("id_guru",id_guru);
                params.put("id_user", id_user);
                params.put("status",status_pass);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}
