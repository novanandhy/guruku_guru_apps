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
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.chorryiga.gurukuguru.GlobalUse.Server;
import com.example.chorryiga.gurukuguru.Menu_Profil.EditJadwal;
import com.example.chorryiga.gurukuguru.Model.ModelJadwal;
import com.example.chorryiga.gurukuguru.R;
import com.example.chorryiga.gurukuguru.SplashActivity;
import com.example.chorryiga.gurukuguru.Util.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Choy on 7/31/2017.
 */

public class AdapterJadwalAjar extends RecyclerView.Adapter<AdapterJadwalAjar.ViewHolder> {
    private ArrayList<ModelJadwal> dataJadwal;
    private Context context;

    private String TAG = "TAG_AdapterJadwalAjar";

    public AdapterJadwalAjar(Context context, ArrayList<ModelJadwal> inputData) {
        this.dataJadwal = inputData;
        this.context = context;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        // di tutorial ini kita hanya menggunakan data String untuk tiap item
        public TextView hari_ajar;
        public TextView jam_mulai;
        public TextView jam_selesai;
        public TextView ubah_jadwal;
        public TextView hapus_jadwal;

        public ViewHolder(View v) {
            super(v);
            hari_ajar = (TextView) v.findViewById(R.id.hari_ajar);
            jam_mulai = (TextView) v.findViewById(R.id.jam_mulai);
            jam_selesai = (TextView) v.findViewById(R.id.jam_selesai);
            ubah_jadwal = (TextView) v.findViewById(R.id.edit_jadwal);
            hapus_jadwal = (TextView) v.findViewById(R.id.hapus_jadwal);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // membuat view baru
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_jadwal, parent, false);
        // mengeset ukuran view, margin, padding, dan parameter layout lainnya
        AdapterJadwalAjar.ViewHolder vh = new AdapterJadwalAjar.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.hari_ajar.setText(dataJadwal.get(position).getHari());
        holder.jam_mulai.setText(dataJadwal.get(position).getJam_mulai());
        holder.jam_selesai.setText(dataJadwal.get(position).getJam_selesai());
        holder.ubah_jadwal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditJadwal.class);
                intent.putExtra("id",dataJadwal.get(position).getId_jadwal());
                intent.putExtra("id_guru",dataJadwal.get(position).getId_guru());
                intent.putExtra("hari",dataJadwal.get(position).getHari());
                intent.putExtra("jam_mulai",dataJadwal.get(position).getJam_mulai());
                intent.putExtra("jam_selesai",dataJadwal.get(position).getJam_selesai());
                intent.putExtra("position",position);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        holder.hapus_jadwal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage("Ingin menghapus daftar ini ?");
                alertDialogBuilder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteJadwal(position,dataJadwal.get(position).getId_guru(),dataJadwal.get(position).getId_jadwal());
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
    }

    @Override
    public int getItemCount() {
        return dataJadwal.size();
    }

    private void deleteJadwal(final int position, final String id_guru, final int id_jadwal) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Server.JADWAL_DELETE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        SplashActivity.mJadwal.remove(position);
                        notifyDataSetChanged();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Log.d(TAG, "error Message: "+errorMsg);
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
                // Posting params to register url

                Map<String, String> params = new HashMap<String, String>();
                params.put("id", String.valueOf(id_jadwal));
                params.put("id_guru",id_guru);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}
