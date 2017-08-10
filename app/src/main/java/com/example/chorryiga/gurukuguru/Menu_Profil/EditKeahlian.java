package com.example.chorryiga.gurukuguru.Menu_Profil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.chorryiga.gurukuguru.GlobalUse.Server;
import com.example.chorryiga.gurukuguru.R;
import com.example.chorryiga.gurukuguru.SplashActivity;
import com.example.chorryiga.gurukuguru.Util.AppController;
import com.example.chorryiga.gurukuguru.Util.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditKeahlian extends Activity {
    Button simpandata;

    private SessionManager sessionManager;
    private Context context;

    private Button button;
    private Spinner spinner;
    private EditText mapel_edit,biaya_edit;

    private int id_skill,position;
    private String jenjang,id_user;
    private String mapel = null;
    private String biaya = null;
    private String TAG = "TAG TambahKeahlian";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_keahlian);
        context = this;

        sessionManager = new SessionManager(context);

        spinner = (Spinner) findViewById(R.id.jenjang_murid);
        mapel_edit = (EditText) findViewById(R.id.matpel_guru);
        biaya_edit = (EditText) findViewById(R.id.harga_temuan);
        simpandata = (Button)findViewById(R.id.simpan_data);

        ambilDataIntent();
        buatDataSpinner();
        isiDataForm();
        simpanData();
    }

    private void isiDataForm() {
        mapel_edit.setText(mapel);
        biaya_edit.setText(biaya);
    }

    private void ambilDataIntent() {
        Intent intent = getIntent();
        id_user = sessionManager.getKeyId();
        id_skill = intent.getExtras().getInt("id");
        mapel = intent.getExtras().getString("mapel");
        jenjang = intent.getExtras().getString("jenjang");
        biaya = intent.getExtras().getString("biaya");
        position = intent.getExtras().getInt("postion");
    }

    private void buatDataSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.jenjang_murid, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        int spinnerSelection = adapter.getPosition(jenjang);
        spinner.setAdapter(adapter);
        spinner.setSelection(spinnerSelection);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //mengambil nilai jenjang
                jenjang = (String) parent.getItemAtPosition(position);
                Log.d(TAG, "jenjang: "+jenjang);

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void simpanData(){
        simpandata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapel = mapel_edit.getText().toString();
                biaya = biaya_edit.getText().toString();
                
                updateKeahlian(id_skill,id_user,jenjang,mapel,biaya);
            }
        });
    }

    private void updateKeahlian(final int id_skill, final String id_guru, final String jenjang, final String mapel, final String biaya) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Server.SKILL_UPDATE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "get rating & review Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        SplashActivity.mSkill.get(position).setBiaya(biaya);
                        SplashActivity.mSkill.get(position).setMapel(mapel);
                        SplashActivity.mSkill.get(position).setJenjang(jenjang);

                        finish();
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
                params.put("jenjang",jenjang);
                params.put("mapel",mapel);
                params.put("biaya",biaya);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}
