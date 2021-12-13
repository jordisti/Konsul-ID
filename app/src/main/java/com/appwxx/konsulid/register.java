package com.appwxx.konsulid;

/**
 * Created by User on 31/10/2017.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

public class register extends Activity{
    private static final String TAG = register.class.getSimpleName();
    private Button btnRegister;
    private Button btnLinkToLogin;
    private EditText inputFullName;
    private EditText inputPassword,latitudes,longitudes;
    private EditText inputEmail;
    private EditText inputrt,inputrw,inputalamat;
    private EditText inputtlpn;
    private Spinner opsi;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;

    LocationManager locationManager;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //biar keyboard sembunyi
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        inputFullName = (EditText) findViewById(R.id.name);
        inputPassword = (EditText) findViewById(R.id.password);
        inputEmail = (EditText) findViewById(R.id.email);
        inputtlpn = (EditText) findViewById(R.id.tlpn);
        inputalamat = (EditText) findViewById(R.id.editText3);
        latitudes = (EditText) findViewById(R.id.editText26);
        longitudes = (EditText) findViewById(R.id.editText27);
        opsi = (Spinner) findViewById(R.id.spinner);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(register.this,
                    MainActivity.class);
            startActivity(intent);
            finish();
        }


        // Register Button Click event
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String name = inputFullName.getText().toString().trim();
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                String tlpn = inputtlpn.getText().toString().trim();
                String status = opsi.getSelectedItem().toString();
                String alamat = inputalamat.getText().toString().trim();
                String latitudenya = latitudes.getText().toString().trim();
                String longitudenya = longitudes.getText().toString().trim();

                if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty() && !tlpn.isEmpty()) {
                    registerUser(name, email, password,tlpn,alamat,latitudenya,longitudenya,status);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please enter your details!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        // Link to Login Screen
        btnLinkToLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        login.class);
                startActivity(i);
                finish();
            }
        });



    }

    private void registerUser(final String name, final String email,
                              final String password, final String tlpn, final String alamat,
                              final String latitudenya, final String longitudenya,final String status) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_register, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        Toast.makeText(getApplicationContext(), "Berhasil Registrasi, Silahkan masuk untuk melihat artikel kami!", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(
                                register.this,
                                login.class);
                        intent.putExtra("email", email);
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
                params.put("name", name);
                params.put("email", email);
                params.put("password", password);
                params.put("tlpn", tlpn);
                params.put("alamat", alamat);
                params.put("latitude", latitudenya);
                params.put("longitude", longitudenya);
                params.put("status", status);

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
