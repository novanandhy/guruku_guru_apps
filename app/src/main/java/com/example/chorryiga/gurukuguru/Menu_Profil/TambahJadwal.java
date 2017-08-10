package com.example.chorryiga.gurukuguru.Menu_Profil;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.chorryiga.gurukuguru.GlobalUse.Server;
import com.example.chorryiga.gurukuguru.R;
import com.example.chorryiga.gurukuguru.Util.AppController;
import com.example.chorryiga.gurukuguru.Util.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class TambahJadwal extends Activity {
    private LayoutInflater inflater;
    private LinearLayout container_jadwal;
    private SessionManager sessionManager;
    private Context context;

    private Button button;
    private TextView jam_mulai;
    private TextView jam_selesai;
    private LinearLayout klik_mulai;
    private LinearLayout klik_selesai;
    private TimePickerDialog timePickerDialog;
    private Spinner spinner;

    private String TAG = "TAG TambahJadwal";
    private String hari,jamMulai,jamSelesai,id_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_jadwal);
        context = this;

        sessionManager = new SessionManager(context);

        inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        jam_mulai = (TextView) findViewById(R.id.jam_mulai);
        jam_selesai = (TextView) findViewById(R.id.jam_selesai);
        klik_mulai = (LinearLayout)findViewById(R.id.klik_mulai);
        klik_selesai = (LinearLayout)findViewById(R.id.klik_selesai);
        spinner = (Spinner) findViewById(R.id.hari_ajar);

        buatDataSpinner();
        klikMulai();
        klikSelesai();
        buttonSave();
    }

    private void buatDataSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.hari, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //mengambil nilai jenjang
                hari = (String) parent.getItemAtPosition(position);
                Log.d(TAG, "hari: "+hari);

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void klikMulai(){
        klik_mulai.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                showTimeMulai();
            }
        });
    }

    private void klikSelesai(){
        klik_selesai.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                showTimeSelesai();
            }
        });
    }

    private void showTimeSelesai(){
        Calendar calendar = Calendar.getInstance();
        timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                jam_selesai.setText(hourOfDay+":"+minute);
            }
        },
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                DateFormat.is24HourFormat(this));

        timePickerDialog.show();
    }

    private void showTimeMulai(){
        Calendar calendar = Calendar.getInstance();
        timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                jam_mulai.setText(hourOfDay+":"+minute);
            }
        },
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                DateFormat.is24HourFormat(this));

        timePickerDialog.show();
    }


    private void buttonSave(){
        button = (Button) findViewById(R.id.simpan_data);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                jamMulai = jam_mulai.getText().toString();
                jamSelesai = jam_selesai.getText().toString();
                id_user = sessionManager.getKeyId();

                if (hari.contains("Pilih jenjang murid")){
                    Toast.makeText(context, "mohon isi semua form", Toast.LENGTH_SHORT).show();
                }else{
                    buatJadwal(id_user,hari,jamMulai,jamSelesai);
                }
            }
        });
    }

    private void buatJadwal(final String idUser, final String hari, final String jamMulai, final String jamSelesai) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Server.JADWAL_CREATE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "get jadwal Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {

                        setResult(RESULT_OK);
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
                params.put("id_guru",idUser);
                params.put("hari",hari);
                params.put("jam_mulai",jamMulai);
                params.put("jam_selesai",jamSelesai);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}
