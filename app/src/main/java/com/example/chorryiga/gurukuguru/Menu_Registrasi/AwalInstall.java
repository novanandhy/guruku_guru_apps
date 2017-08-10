package com.example.chorryiga.gurukuguru.Menu_Registrasi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class AwalInstall extends AppCompatActivity {
    private SessionManager sessionManager;
    private ProgressDialog progressDialog;
    private SplashActivity splashActivity;

    private EditText Edit_alamat_email;
    private EditText Edit_password;
    private TextView registrasi;
    private Button button;
    private Context context;
    private RelativeLayout relativeLayout;

    private String email,password;
    private String TAG = "TAG_AwalInstallActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_awal_install);
        this.setTitle("Login");
        context = this;
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);

        //membuat objek progress dialog
        progressDialog = new ProgressDialog(context);

        splashActivity = new SplashActivity();

        Edit_alamat_email = (EditText) findViewById(R.id.Edit_alamat_email);
        Edit_password = (EditText) findViewById(R.id.Edit_password);

        registrasi = (TextView) findViewById(R.id.registrasi);
        button = (Button) findViewById(R.id.button_login);

        //membuat session login
        sessionManager = new SessionManager(context);
        if (sessionManager.isLoggedIn()){
            Intent intent = new Intent(AwalInstall.this,SplashActivity.class);
            startActivity(intent);
            finish();
        }

        //cek apakah terkoneksi atau tidak
        //jika tidak akan ada anjuran untuk mereload activity
        if (isOnline()){
            button.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    email = Edit_alamat_email.getText().toString().trim();
                    password = Edit_password.getText().toString().trim();

                    //cek apakah form telah terisi semua atau belum
                    if (!email.isEmpty() && !password.isEmpty()){
                        Login_guru(email,password);
                    }else{
                        Toast.makeText(context, "mohon isi semua form", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else {
            Snackbar snackbar = Snackbar
                    .make(relativeLayout, "Tolong cek kembali koneksi", Snackbar.LENGTH_LONG)
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

        registrasi.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(AwalInstall.this, Registrasi.class);
                startActivity(intent);
            }
        });
    }

    private void Login_guru(final String email, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        progressDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Server.LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        String uid = jObj.getString("uid");
                        Log.d(TAG, "uid: "+uid);

                        //membuat session login
                        sessionManager.setLogin(true,uid);
                        Intent intent = new Intent(AwalInstall.this, SplashActivity.class);
                        startActivity(intent);
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
                //mengirim paramater yang dibutuhkan untuk login
                Map<String, String> params = new HashMap<String, String>();

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
