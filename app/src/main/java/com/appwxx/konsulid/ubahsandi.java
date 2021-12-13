package com.appwxx.konsulid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.appwxx.konsulid.app.AppConfig;
import com.appwxx.konsulid.app.AppController;
import com.appwxx.konsulid.helper.SQLiteHandler;
import com.appwxx.konsulid.helper.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ubahsandi extends AppCompatActivity {

    private static final String TAG = pengguna.class.getSimpleName();
    private static String url_cari     = AppConfig.URL_modul + "profile.php";
    private Button btnRegister,keluar;
    private EditText inputFullName;
    private EditText inputalamat,inputlahir;
    private EditText inputEmail;
    private EditText inputkontak;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubahsandi);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        callVolley();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        inputFullName = (EditText) findViewById(R.id.editText);
        inputalamat = (EditText) findViewById(R.id.alamat);
        inputEmail = (EditText) findViewById(R.id.harga);
        inputkontak = (EditText) findViewById(R.id.editText2);
        inputlahir = (EditText) findViewById(R.id.password);

        btnRegister = (Button) findViewById(R.id.ubah);
        keluar = (Button) findViewById(R.id.keluar);

        // Register Button Click event
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String name = inputFullName.getText().toString().trim();
                String email = inputEmail.getText().toString().trim();
                String alamat = inputalamat.getText().toString().trim();
                String kontak = inputkontak.getText().toString().trim();
                String password = inputlahir.getText().toString().trim();

                if (!name.isEmpty() && !email.isEmpty() && !alamat.isEmpty() && !kontak.isEmpty()) {
                    registerUser(name, email, alamat,kontak,password);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Isi secara lengkap!", Toast.LENGTH_LONG)
                            .show();
                }

            }
        });


        db = new SQLiteHandler(this);
        session = new SessionManager(this);
        if (!session.isLoggedIn()) {
            logoutUser();
        }

        keluar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                logoutUser();

            }
        });

    }


    private void logoutUser() {
        session.setLogin(false);
        db.deleteUsers();
        Intent intent = new Intent(this, login.class);
        startActivity(intent);
    }

    // untuk menampilkan semua data pada listview
    private void callVolley(){
        //swipe.setRefreshing(true);
        // membuat request JSON
        db = new SQLiteHandler(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        String idUser = user.get("uid");

        JsonArrayRequest jArr = new JsonArrayRequest(url_cari+"?idUser="+idUser, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());

                // Parsing json
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);

                        EditText name = (EditText) findViewById(R.id.editText);
                        EditText password = (EditText) findViewById(R.id.password);
                        EditText email = (EditText) findViewById(R.id.harga);
                        EditText kontak = (EditText) findViewById(R.id.editText2);
                        EditText alamat = (EditText) findViewById(R.id.alamat);

                        name.setText(obj.getString("nameUser"));
                        password.setText(obj.getString("passwordUser"));
                        email.setText(obj.getString("emailUser"));
                        kontak.setText(obj.getString("kontakUser"));
                        alamat.setText(obj.getString("alamatUser"));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                // swipe.setRefreshing(false);
            }
        });

        // menambah request ke request queue
        AppController.getInstance().addToRequestQueue(jArr);
    }

    private void registerUser(final String name, final String email,
                              final String alamat, final String kontak, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_modul + "insert.php", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {

                        Toast.makeText(getApplicationContext(), "Berhasil tersimpan!", Toast.LENGTH_LONG).show();

                        // Launch login activity
                        Intent intent = new Intent(
                                ubahsandi.this,
                                profilpengguna.class);
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
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();

                db = new SQLiteHandler(getApplicationContext());
                HashMap<String, String> user = db.getUserDetails();
                String idUser = user.get("uid");

                params.put("name", name);
                params.put("email", email);
                params.put("alamat", alamat);
                params.put("kontak", kontak);
                params.put("password", password);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}
