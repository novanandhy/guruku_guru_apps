package com.example.chorryiga.gurukuguru.Menu_Registrasi;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.chorryiga.gurukuguru.GlobalUse.Server;
import com.example.chorryiga.gurukuguru.R;
import com.example.chorryiga.gurukuguru.Util.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Registrasi extends AppCompatActivity {
    private ProgressDialog progressDialog;

    private Button button;
    private EditText et_email;
    private EditText et_password;
    private EditText et_nama;
    private LinearLayout linearLayout;

    private String email, password, nama;
    private String TAG = "TAG_RegistrasiActivity";

    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrasi);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        context = this;

        //membuat objek progress dialog
        progressDialog = new ProgressDialog(context);

        button = (Button) findViewById(R.id.registrasi);
        et_email = (EditText)findViewById(R.id.alamat_email_reg);
        et_password = (EditText) findViewById(R.id.password_reg);
        et_nama = (EditText) findViewById(R.id.nama_guru_reg);

        //check apakah ada koneksi
        if (isOnline()){
            button.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    email = et_email.getText().toString().trim();
                    password = et_password.getText().toString().trim();
                    nama = et_nama.getText().toString().trim();

                    Log.d(TAG, "email ="+email);
                    Log.d(TAG, "password ="+password);
                    Log.d(TAG, "nama ="+nama);

                    if (!email.isEmpty() && !password.isEmpty() && !nama.isEmpty()){
                        Registrasi_guru(email,password,nama);
                    }else {
                        Toast.makeText(Registrasi.this, "Harap isi semua form", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else {
            Snackbar snackbar = Snackbar
                    .make(linearLayout, "Tolong cek kembali koneksi", Snackbar.LENGTH_LONG)
                    .setAction("CEK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //reload activity
                            finish();
                            startActivity(getIntent());
                        }
                    });

            snackbar.show();
        }
    }

    private void Registrasi_guru(final String email, final String password, final String nama) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        progressDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Server.REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL

                        Toast.makeText(getApplicationContext(), "akun telah terdaftar. silahkan log in",
                                Toast.LENGTH_LONG).show();

                        // Launch login activity
                        finish();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();

                //convert bitmap to string
                params.put("name", nama);
                params.put("previllage", "0");
                params.put("username", email);
                params.put("password", password);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
