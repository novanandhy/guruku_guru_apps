package com.example.chorryiga.gurukuguru;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Space;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.chorryiga.gurukuguru.GlobalUse.Server;
import com.example.chorryiga.gurukuguru.Menu_Registrasi.AwalInstall;
import com.example.chorryiga.gurukuguru.Model.ModelBooking;
import com.example.chorryiga.gurukuguru.Model.ModelGuru;
import com.example.chorryiga.gurukuguru.Model.ModelJadwal;
import com.example.chorryiga.gurukuguru.Model.ModelLowongan;
import com.example.chorryiga.gurukuguru.Model.ModelRating;
import com.example.chorryiga.gurukuguru.Model.ModelRatingGuru;
import com.example.chorryiga.gurukuguru.Model.ModelSkill;
import com.example.chorryiga.gurukuguru.Model.ModelTransaksi;
import com.example.chorryiga.gurukuguru.Util.AppController;
import com.example.chorryiga.gurukuguru.Util.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SplashActivity extends AppCompatActivity {
    private SessionManager sessionManager;
    private static Context context;
    private CoordinatorLayout constraintLayout;

    public static ArrayList<ModelGuru> mGuru;
    public static ArrayList<ModelLowongan> mLowongan;
    public static ArrayList<ModelRatingGuru> mRating;
    public static ArrayList<ModelRating> mRatingReview;
    public static ArrayList<ModelSkill> mSkill;
    public static ArrayList<ModelJadwal> mJadwal;
    public static ArrayList<ModelBooking> mBooking;
    public static ArrayList<ModelTransaksi> mTransaksi;

    public static String ID_GURU;
    private static Activity act;
    private static String TAG = "TAG_SplashScreen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        context = this;
        act = this;

//        constraintLayout = (CoordinatorLayout) findViewById(R.id.constraintLayout);

        sessionManager = new SessionManager(context);

        mGuru = new ArrayList<>();
        mLowongan = new ArrayList<>();
        mRating = new ArrayList<>();
        mRatingReview = new ArrayList<>();
        mSkill = new ArrayList<>();
        mJadwal = new ArrayList<>();
        mBooking = new ArrayList<>();
        mTransaksi = new ArrayList<>();


        //cek apakah terkoneksi atau tidak
        if (isOnline()){
            //cek apakah user pernah login atau belum
            if (sessionManager.isLoggedIn()){
                ID_GURU = sessionManager.getKeyId();
                //masuk ke halaman home jika telah mengambil semua data
                GetAllData(ID_GURU);

            }
            //jika user belum pernah login, masuk ke halaman login
            else{
                Intent intent = new Intent(SplashActivity.this, AwalInstall.class);
                startActivity(intent);
                finish();
            }
        }
        //jika tidak terkoneksi internet, maka akan muncul pemberitahuan
        else {
            buatSnackbar();
        }
    }

    public void buatSnackbar() {
        Snackbar snackbar = Snackbar
                .make(constraintLayout, "Tolong cek kembali koneksi", Snackbar.LENGTH_LONG)
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

    public void GetAllData(String ID_GURU) {
        GetDataGuru(ID_GURU);
//        GetRating(ID_GURU);
//        GetRatingReview(ID_GURU);
//        GetSkill(ID_GURU);
//        GetJadwal(ID_GURU);
//        GetBooking(ID_GURU);
//        GetTransaksi(ID_GURU);
    }

    private static void GetDataGuru(final String id_guru) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";
        

        //merequest JSON dengan URL yang telah disediakan dengan method POST
        StringRequest strReq = new StringRequest(Request.Method.POST,
                Server.GURU_SELECT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "get data guru Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    //cek apakah JSON memiliki indeks error yang true atau tidak
                    if (!error) {
                        // menangkap indeks array JSON "user"
                        JSONObject details = jObj.getJSONObject("user");
                        mGuru.clear();

                        //mengambil setiap data di setiap indeks JSON
                            ModelGuru mGu = new ModelGuru();

                            mGu.setId_guru(id_guru);
                            mGu.setNama(details.getString("nama"));
                            mGu.setAlamat(details.getString("alamat"));
                            mGu.setNo_telp(details.getString("no_telp"));
                            mGu.setKelamin(details.getString("kelamin"));
                            mGu.setEmail(details.getString("email"));
                            mGu.setPendidikan(details.getString("pendidikan"));
                            mGu.setPengalaman(details.getInt("pengalaman"));
                            mGu.setDeskripsi(details.getString("deskripsi"));
                            mGu.setFoto(details.getString("foto"));
                            mGu.setLat(details.getString("lat"));
                            mGu.setLng(details.getString("lng"));
                            mGu.setUsia(details.getString("usia"));
                            mGu.setIpk(details.getDouble("ipk"));
                            mGu.setKampus(details.getString("kampus"));
                            mGu.setJurusan(details.getString("jurusan"));

                            mGuru.add(mGu);

                            //mengambil lowongan berdasarkan lokasi
                            GetAllLowongan(details.getString("lat"),details.getString("lng"),id_guru);


                    } else {

                        //terjadi kesalahan saat mengambil JSON. misal data pada database tidak ada
                        String errorMsg = jObj.getString("error_msg");
                        
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //jika tidak ada respon dari URL. atau lebih tepatnya tidak ada internet
                error.printStackTrace();
                Toast.makeText(context, "terjadi kesalahan", Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // memasukkan parameter untuk merequest JSON
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_guru",id_guru);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        
    }

    private static void GetAllLowongan(final String latitude, final String longitude, final String id_guru) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";
        

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Server.LOWONGAN_GET_ALL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "get lowogan Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        JSONArray list = jObj.getJSONArray("user");
                        mLowongan.clear();

                        for (int i = 0 ; i < list.length() ; i++){
                            JSONObject details = list.getJSONObject(i);

                            ModelLowongan mLo = new ModelLowongan();

                            mLo.setId_lowongan(details.getInt("id"));
                            mLo.setId_user(details.getString("id_user"));
                            mLo.setSubjek(details.getString("subjek"));
                            mLo.setDeskripsi(details.getString("description"));
                            mLo.setNama(details.getString("nama"));
                            mLo.setFoto(details.getString("foto"));
                            mLo.setAlamat(details.getString("alamat"));
                            mLo.setNo_telp(details.getString("no_telp"));
                            mLo.setEmail(details.getString("email"));
                            mLo.setLat(details.getString("lat"));
                            mLo.setLng(details.getString("lng"));
                            mLo.setJarak(Float.valueOf(details.getString("dist")));

                            mLowongan.add(mLo);

                        }
                        GetRating(ID_GURU);


                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(context, "terjadi kesalahan", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                
                error.printStackTrace();
                
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();

                params.put("latitude",latitude);
                params.put("longitude",longitude);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        
    }

    private static void GetRating(final String id_guru) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";
        

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Server.RATING_GET, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "get rating guru Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        JSONArray list = jObj.getJSONArray("user");
                        mRating.clear();

                        for (int i = 0 ; i < list.length() ; i++){
                            JSONObject details = list.getJSONObject(i);

                            ModelRatingGuru mRat = new ModelRatingGuru();

                            mRat.setRating(details.getDouble("rating"));

                            mRating.add(mRat);
                        }
                        GetRatingReview(ID_GURU);


                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(context, "terjadi kesalahan", Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_guru",id_guru);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        
    }

    private static void GetRatingReview(final String id_guru) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";
        

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Server.RATING_GET_REVIEW, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "get rating & review Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        JSONArray list = jObj.getJSONArray("user");
                        mRatingReview.clear();
                        if(list!=null && list.length() > 0) {
                            for (int i = 0; i < list.length(); i++) {
                                JSONObject details = list.getJSONObject(i);

                                ModelRating mRat = new ModelRating();

                                mRat.setId_user(details.getString("id_user"));
                                mRat.setRating(details.getDouble("rating"));
                                mRat.setReview(details.getString("review"));
                                mRat.setNama(details.getString("nama"));
                                mRat.setFoto(details.getString("foto"));

                                mRatingReview.add(mRat);
                            }
                        }
                        else mRatingReview = new ArrayList<>();
                        GetSkill(ID_GURU);


                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(context, "terjadi kesalahan", Toast.LENGTH_SHORT).show();
                
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                Log.d("Guru","id_guru :"+id_guru);
                params.put("id_guru",id_guru);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        
    }

    public static void GetSkill(final String id_guru) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";
        

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Server.SKILL_GET_BY, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "get skill Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        JSONArray list = jObj.getJSONArray("user");
                        mSkill.clear();

                        for (int i = 0 ; i < list.length() ; i++){
                            JSONObject details = list.getJSONObject(i);

                            ModelSkill mSki = new ModelSkill();

                            mSki.setId_skill(details.getInt("id"));
                            mSki.setId_guru(details.getString("id_guru"));
                            mSki.setJenjang(details.getString("jenjang"));
                            mSki.setMapel(details.getString("mapel"));
                            mSki.setBiaya(details.getString("biaya"));

                            mSkill.add(mSki);
                        }
                        GetJadwal(ID_GURU);


                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(context, "terjadi kesalahan", Toast.LENGTH_SHORT).show();
                
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_guru",id_guru);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        
    }

    public static void GetJadwal(final String id_guru) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";
        

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Server.JADWAL_GET_BY, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "get jadwal Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        JSONArray list = jObj.getJSONArray("user");
                        mJadwal.clear();

                        for (int i = 0 ; i < list.length() ; i++){
                            JSONObject details = list.getJSONObject(i);

                            ModelJadwal mJa = new ModelJadwal();

                            mJa.setId_jadwal(details.getInt("id"));
                            mJa.setId_guru(details.getString("id_guru"));
                            mJa.setHari(details.getString("hari"));
                            mJa.setJam_mulai(details.getString("jam_mulai"));
                            mJa.setJam_selesai(details.getString("jam_selesai"));

                            mJadwal.add(mJa);
                        }
                        GetBooking(ID_GURU);

                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(context, "terjadi kesalahan", Toast.LENGTH_SHORT).show();
                
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_guru",id_guru);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        
    }

    public static void GetBooking(final String id_guru) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";
        

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Server.BOOKING_GET, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "get booking Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        JSONArray list = jObj.getJSONArray("user");
                        mBooking.clear();

                        if(list!=null && list.length()>0) {
                            for (int i = 0; i < list.length(); i++) {
                                JSONObject details = list.getJSONObject(i);

                                ModelBooking mBo = new ModelBooking();

                                mBo.setId(details.getInt("id"));
                                mBo.setId_user(details.getString("id_user"));
                                mBo.setId_guru(details.getString("id_guru"));
                                mBo.setStatus(details.getString("status"));
//                                mBo.setKeterangan(details.getString("keterangan"));
                                mBo.setNama(details.getString("nama"));
                                mBo.setFoto(details.getString("foto"));
                                mBo.setAlamat(details.getString("alamat"));
                                mBo.setNo_telp(details.getString("no_telp"));
                                mBo.setEmail(details.getString("email"));
                                mBo.setLat(details.getString("lat"));
                                mBo.setLng(details.getString("lng"));

                                mBooking.add(mBo);
                            }
                        }
                        else mBooking = new ArrayList<>();
                        GetTransaksi(ID_GURU,false);

                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(context,"sdfsdf",Toast.LENGTH_SHORT).show();
                        
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(context, "terjadi kesalahan", Toast.LENGTH_SHORT).show();
                
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_user",id_guru);
                params.put("previllage","1");

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        
    }

    public static void GetTransaksi(final String id_guru, final boolean flag_source) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";
        Toast.makeText(context,"fdsf",Toast.LENGTH_SHORT).show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Server.TRANSAKSI_LOWONGAN_GET, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "get transaksi Response: " + response.toString());

                try {

                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        JSONArray list = jObj.getJSONArray("user");
                        mTransaksi.clear();
                        if(list!=null && list.length() > 0) {
                            for (int i = 0; i < list.length(); i++) {
                                JSONObject details = list.getJSONObject(i);

                                ModelTransaksi mTra = new ModelTransaksi();

                                mTra.setId(details.getInt("id"));
                                mTra.setId_lowongan(details.getInt("id_lowongan"));
                                mTra.setId_guru(details.getString("id_guru"));
                                mTra.setId_user(details.getString("id_user"));
                                mTra.setStatus(details.getString("status"));
                                mTra.setNama(details.getString("nama"));
                                mTra.setFoto(details.getString("foto"));
                                mTra.setAlamat(details.getString("alamat"));
                                mTra.setNo_telp(details.getString("no_telp"));
                                mTra.setEmail(details.getString("email"));
                                mTra.setLat(details.getString("lat"));
                                mTra.setLng(details.getString("lng"));
                                mTra.setSubjek(details.getString("subjek"));
                                mTra.setDescription(details.getString("description"));

                                mTransaksi.add(mTra);
                            }
                        }
                        else {
                            mTransaksi = new ArrayList<>();

                        }
                        if(!flag_source) {
                            Intent intent = new Intent(context, Home.class);
                            context.startActivity(intent);
                            act.finish();
                        }

                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(context, "terjadi kesalahan", Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_guru",id_guru);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
