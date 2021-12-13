package com.appwxx.konsulid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.appwxx.konsulid.app.AppConfig;
import com.appwxx.konsulid.app.AppController;
import com.appwxx.konsulid.helper.SQLiteHandler;
import com.appwxx.konsulid.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class tentang extends AppCompatActivity {
        private static final String TAG = tentang.class.getSimpleName();
        private Button btnRegister;
        private SessionManager session;
        private SQLiteHandler db;
        EditText namas,emails,pesans;
        private ProgressDialog pDialog;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_about);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            pDialog = new ProgressDialog(this);
            pDialog.setCancelable(false);

            db = new SQLiteHandler(getApplicationContext());
            HashMap<String, String> user = db.getUserDetails();
            String nama = user.get("name");
            String email = user.get("email");

            namas = (EditText) findViewById(R.id.nama);
            emails = (EditText) findViewById(R.id.email);
            pesans = (EditText) findViewById(R.id.pesan);

            namas.setText(nama);
            emails.setText(email);

            btnRegister = (Button) findViewById(R.id.simpan);
            btnRegister.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {
                    String nama = namas.getText().toString().trim();
                    String email = emails.getText().toString().trim();
                    String pesan = pesans.getText().toString().trim();
                    kirim(nama,email,pesan);
                }
            });

        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()){
                case android.R.id.home:
                    Intent intent = new Intent(tentang.this,
                            profilpengguna.class);
                    startActivity(intent);
                    finish();
                    return true;

            }
            return super.onOptionsItemSelected(item);
        }

    private void kirim(final String nama, final String email, final String pesan) {
        String tag_string_req = "req_login";

        pDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_modul+"kritiksaran.php", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        Toast.makeText(getApplicationContext(), "Terimakasih sudah memberikan kritik atau saran anda sebagai evaluasi kami", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(tentang.this,
                                MainActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                db = new SQLiteHandler(getApplicationContext());
                HashMap<String, String> user = db.getUserDetails();
                String idUser = user.get("uid");

                params.put("email", email);
                params.put("nama", nama);
                params.put("pesan", pesan);
                params.put("idUser", idUser);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
