package com.example.chorryiga.gurukuguru.Menu_Pengaturan;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.chorryiga.gurukuguru.GlobalUse.Server;
import com.example.chorryiga.gurukuguru.Model.ModelGuru;
import com.example.chorryiga.gurukuguru.R;
import com.example.chorryiga.gurukuguru.SplashActivity;
import com.example.chorryiga.gurukuguru.Util.AppController;
import com.example.chorryiga.gurukuguru.Util.SessionManager;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditGuru extends AppCompatActivity {
    private Geocoder geocoder;
    private List<Address> addressList;
    private ProgressDialog progressDialog;
    private Spinner spinner;
    private Context context;
    private SessionManager sessionManager;

    private Button simpan;
    private Button button_upload;
    private TextView buka_map;
    private EditText edit_email,edit_nama,edit_telp,edit_alamat,edit_jurusan,edit_kampus,
            edit_ipk,edit_pengalaman,edit_keterangan;
    private CircleImageView image;
    private Bitmap img;

    private static final int SELECT_PICTURE = 100;
    private String email,nama,telp,alamat,jurusan,jenjang,kampus,keterangan,image64;
    private String TAG = "TAG Registrasiguru1";
    private String pengalaman;
    private String ipk;
    private Double latitude;
    private Double longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_guru);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = this;

        simpan = (Button) findViewById(R.id.simpan_data);
        button_upload = (Button) findViewById(R.id.upload_foto);
        buka_map = (TextView) findViewById(R.id.open_map);
        spinner = (Spinner) findViewById(R.id.pend_guru);


        sessionManager = new SessionManager(context);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Memuat alamat");
        progressDialog.setTitle("Loading");
        geocoder = new Geocoder(this, Locale.getDefault());

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.kategori_guru, android.R.layout.simple_spinner_item);
        int spinnerSelection = adapter.getPosition(SplashActivity.mGuru.get(0).getPendidikan());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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

        image = (CircleImageView) findViewById(R.id.foto_profil);
        edit_email = (EditText) findViewById(R.id.alamat_email);
        edit_nama = (EditText) findViewById(R.id.nama_guru);
        edit_telp = (EditText) findViewById(R.id.no_telp);
        edit_alamat = (EditText) findViewById(R.id.alamat_rumah);
        edit_jurusan = (EditText) findViewById(R.id.jurusan_guru);
        edit_kampus = (EditText) findViewById(R.id.univ_guru);
        edit_ipk = (EditText) findViewById(R.id.ipk_guru);
        edit_pengalaman = (EditText) findViewById(R.id.lama_pengalaman);
        edit_keterangan = (EditText) findViewById(R.id.pengalaman_guru);

        setValueForm();
        buttonMap();
        buttonOpenImage();
        uploadData();
    }

    private void uploadData() {
        simpan.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //mendapatkan semua nilai dari form
                getValueForm();

                //Edit profil guru
                EditGuru(sessionManager.getKeyId(),nama,alamat,telp,email,jenjang,pengalaman,keterangan,latitude,longitude,ipk,kampus,jurusan,image64);

            }
        });
    }

    private void setValueForm() {
        edit_email.setText(SplashActivity.mGuru.get(0).getEmail());
        edit_nama.setText(SplashActivity.mGuru.get(0).getNama());
        edit_telp.setText(SplashActivity.mGuru.get(0).getNo_telp());
        edit_alamat.setText(SplashActivity.mGuru.get(0).getAlamat());
        edit_jurusan.setText(SplashActivity.mGuru.get(0).getJurusan());
        edit_kampus.setText(SplashActivity.mGuru.get(0).getKampus());
        edit_ipk.setText(""+SplashActivity.mGuru.get(0).getIpk());
        edit_pengalaman.setText(""+SplashActivity.mGuru.get(0).getPengalaman());
        edit_keterangan.setText(SplashActivity.mGuru.get(0).getDeskripsi());
        Picasso.with(context).load(SplashActivity.mGuru.get(0).getFoto()).into(image);
    }

    private void getValueForm() {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        byte[] byteArray = null;

        //mengambil nilai email
        email = edit_email.getText().toString();
        //mengambil nilai nama
        nama = edit_nama.getText().toString();
        //mengambil nilai telepon
        telp = edit_telp.getText().toString();
        //mengambil nilai alamat
        alamat = edit_alamat.getText().toString();
        //mengambil nilai jurusan
        jurusan = edit_jurusan.getText().toString();
        //mengambil nilai kampus
        kampus = edit_kampus.getText().toString();
        //mengambil nilai ipk
        ipk = edit_ipk.getText().toString();
        //mengambil nilai pengalaman
        pengalaman = edit_pengalaman.getText().toString();
        //mengambil nilai keterangan
        keterangan = edit_keterangan.getText().toString();

        //jika alamat kosong, maka akan diisi dengan data yang sudah ada
        if (latitude == null && longitude == null) {
            //mengambil nilai latitude
            latitude = Double.valueOf(SplashActivity.mGuru.get(0).getLat());
            //mengambil nilai longitude
            longitude = Double.valueOf(SplashActivity.mGuru.get(0).getLng());
        }

        //cek jika guru tidak memilih gambar
        if (img == null){
            BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
            Bitmap bitmap = drawable.getBitmap();
            bitmap.compress(Bitmap.CompressFormat.PNG,40,stream);
            byteArray = stream.toByteArray();
        }else{
            //change bitmap to byte array
            img.compress(Bitmap.CompressFormat.PNG,40,stream);
            byteArray = stream.toByteArray();
        }

        //mengambil nilai foto
        image64 = getStringImage(byteArray);

        Log.d(TAG, "getValueForm email: "+email);
        Log.d(TAG, "getValueForm nama: "+nama);
        Log.d(TAG, "getValueForm telp: "+telp);
        Log.d(TAG, "getValueForm alamat: "+alamat);
        Log.d(TAG, "getValueForm jurusan: "+jurusan);
        Log.d(TAG, "getValueForm kampus: "+kampus);
        Log.d(TAG, "getValueForm ipk: "+ipk);
        Log.d(TAG, "getValueForm pengalaman: "+pengalaman);
        Log.d(TAG, "getValueForm keterangan: "+keterangan);
        Log.d(TAG, "getValueForm latitude: "+latitude);
        Log.d(TAG, "getValueForm longitude: "+longitude);
        Log.d(TAG, "getValueForm jenjang: "+jenjang);
        Log.d(TAG, "getValueForm image: "+image64);
    }

    private void buttonOpenImage() {
        button_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageSource();
            }
        });
    }


    private void buttonMap(){
        buka_map.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(EditGuru.this, MapsActivity.class);
                startActivityForResult(intent,207);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //mengambil alamat fari google maps
        if(resultCode == RESULT_OK && requestCode == 207){
            LatLng latLng = data.getParcelableExtra("latlng");
            latitude = latLng.latitude;
            longitude = latLng.longitude;
            progressDialog.show();
            try {
                addressList = geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
                if(addressList.size() > 0){
                    String address = addressList.get(0).getAddressLine(0);
                    String district = addressList.get(0).getAddressLine(1);
                    String regency = addressList.get(0).getAddressLine(2);
                    String country = addressList.get(0).getAddressLine(3);
                    String completeAddress = address + " " + district + " " + regency + " " + country;
                    edit_alamat.setText(completeAddress);
                }
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(EditGuru.this,"Gagal menambahkan alamat, coba lagi",Toast.LENGTH_LONG).show();
            }
            progressDialog.dismiss();
        }

        //mengambil gambar dari galeri HP
        try{
            //when image is picked
            if (resultCode == RESULT_OK) {
                if (requestCode == SELECT_PICTURE) {
                    // Get the url from data
                    Uri selectedImageUri = data.getData();
                    if (null != selectedImageUri) {
                        img = MediaStore.Images.Media.getBitmap(getContentResolver(),selectedImageUri);
                        image.setImageBitmap(img);
                        img = scaleDownBitmap(img,70,context);
                    }
                }
            }else {
                Toast.makeText(this, "Anda belum memilih gambar", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e){
            Toast.makeText(this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
        }
    }

    private void openImageSource() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Pilih gambar"), SELECT_PICTURE);
    }

    public String getStringImage(byte[] bmp){
        String encodedImage = Base64.encodeToString(bmp, Base64.DEFAULT);
        return encodedImage;
    }

    public static Bitmap scaleDownBitmap(Bitmap photo, int newHeight, Context context) {

        final float densityMultiplier = context.getResources().getDisplayMetrics().density;

        int h= (int) (newHeight*densityMultiplier);
        int w= (int) (h * photo.getWidth()/((double) photo.getHeight()));

        photo=Bitmap.createScaledBitmap(photo, w, h, true);

        return photo;
    }

    private void EditGuru(final String id_guru, final String nama, final String alamat, final String telp, final String email,
                          final String pendidikan, final String pengalaman, final String keterangan, final Double latitude,
                          final Double longitude, final String ipk, final String kampus, final String jurusan, final String image) {
        final String jenjang, foto_path, kelamin, usia;

        //cek jika pendidikan memiliki nilai null
        if (pendidikan == null){
            jenjang = SplashActivity.mGuru.get(0).getPendidikan();
        }else{
            jenjang = pendidikan;
        }

        //mendapatkan url foto
        foto_path = SplashActivity.mGuru.get(0).getFoto();

        //mendapatkan nilai kelamin
        kelamin = SplashActivity.mGuru.get(0).getKelamin();

        //mendapat nilai usia
        usia = SplashActivity.mGuru.get(0).getUsia();

        // Tag used to cancel the request
        String tag_string_req = "req_register";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Server.GURU_UPDATE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "get rating & review Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {

                        //mengganti semua nilai data guru
                        SplashActivity.mGuru.clear();

                        ModelGuru mGu = new ModelGuru();

                        mGu.setNama(nama);
                        mGu.setAlamat(alamat);
                        mGu.setNo_telp(telp);
                        mGu.setEmail(email);
                        mGu.setPendidikan(jenjang);
                        mGu.setPengalaman(Integer.parseInt(pengalaman));
                        mGu.setDeskripsi(keterangan);
                        mGu.setLat(String.valueOf(latitude));
                        mGu.setLng(String.valueOf(longitude));
                        mGu.setIpk(Double.parseDouble(ipk));
                        mGu.setKampus(kampus);
                        mGu.setJurusan(jurusan);
                        mGu.setFoto(foto_path);
                        mGu.setKelamin(kelamin);
                        mGu.setUsia(usia);

                        SplashActivity.mGuru.add(mGu);

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
                params.put("id_guru",id_guru);
                params.put("nama",nama);
                params.put("alamat",alamat);
                params.put("no_telp",telp);
                params.put("email",email);
                params.put("pendidikan",jenjang);
                params.put("pengalaman",String.valueOf(pengalaman));
                params.put("deskripsi",keterangan);
                params.put("lat",String.valueOf(latitude));
                params.put("lng",String.valueOf(longitude));
                params.put("foto",image);
                params.put("ipk",String.valueOf(ipk));
                params.put("kampus",kampus);
                params.put("jurusan",jurusan);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}