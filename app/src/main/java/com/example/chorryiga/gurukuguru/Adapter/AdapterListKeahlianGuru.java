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
import com.example.chorryiga.gurukuguru.Menu_Profil.EditKeahlian;
import com.example.chorryiga.gurukuguru.Model.ModelSkill;
import com.example.chorryiga.gurukuguru.R;
import com.example.chorryiga.gurukuguru.SplashActivity;
import com.example.chorryiga.gurukuguru.Util.AppController;
import com.example.chorryiga.gurukuguru.Util.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Choy on 7/30/2017.
 */

public class AdapterListKeahlianGuru extends RecyclerView.Adapter<AdapterListKeahlianGuru.ViewHolder>{
    private ArrayList<ModelSkill> dataKeahlian;
    private Context context;
    private SessionManager sessionManager;

    private String TAG = "TAG AdapterListKeahlianGuru";

    public AdapterListKeahlianGuru(Context context, ArrayList<ModelSkill> inputData) {
        this.dataKeahlian = inputData;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // membuat view baru
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_keahlian, parent, false);
        // mengeset ukuran view, margin, padding, dan parameter layout lainnya
        AdapterListKeahlianGuru.ViewHolder vh = new AdapterListKeahlianGuru.ViewHolder(v);

        sessionManager = new SessionManager(context);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.jenjang_pendidikan.setText(dataKeahlian.get(position).getJenjang());
        holder.mata_pelajaran.setText(dataKeahlian.get(position).getMapel());
        holder.harga_temuan.setText(dataKeahlian.get(position).getBiaya());

        holder.edit_keahlian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,EditKeahlian.class);
                intent.putExtra("jenjang",dataKeahlian.get(position).getJenjang());
                intent.putExtra("mapel",dataKeahlian.get(position).getMapel());
                intent.putExtra("biaya",dataKeahlian.get(position).getBiaya());
                intent.putExtra("id",dataKeahlian.get(position).getId_skill());
                intent.putExtra("postion",position);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        holder.hapus_keahlian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage("Ingin menghapus daftar ini ?");
                alertDialogBuilder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteSkill(position,sessionManager.getKeyId(),dataKeahlian.get(position).getId_skill());
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
        return dataKeahlian.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        // di tutorial ini kita hanya menggunakan data String untuk tiap item
        public TextView jenjang_pendidikan;
        public TextView mata_pelajaran;
        public TextView harga_temuan;
        public TextView edit_keahlian;
        public TextView hapus_keahlian;

        public ViewHolder(View v) {
            super(v);
            jenjang_pendidikan = (TextView) v.findViewById(R.id.jenjang_murid);
            mata_pelajaran = (TextView) v.findViewById(R.id.matpel_guru);
            harga_temuan = (TextView) v.findViewById(R.id.harga_temuan);
            edit_keahlian = (TextView) v.findViewById(R.id.edit_keahlian);
            hapus_keahlian = (TextView) v.findViewById(R.id.hapus_keahlian);
        }
    }

    private void deleteSkill(final int position, final String id_guru, final int id_skill) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Server.SKILL_DELETE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        SplashActivity.mSkill.remove(position);
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
                params.put("id", String.valueOf(id_skill));
                params.put("id_guru",id_guru);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}
