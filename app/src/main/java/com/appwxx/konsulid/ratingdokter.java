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
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.appwxx.konsulid.adapter.adapterlima;
import com.appwxx.konsulid.app.AppConfig;
import com.appwxx.konsulid.app.AppController;
import com.appwxx.konsulid.data.data;
import com.appwxx.konsulid.helper.SQLiteHandler;
import com.appwxx.konsulid.helper.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ratingdokter extends AppCompatActivity {
    private static final String TAG = register.class.getSimpleName();
    private Button btnRegister;
    private EditText komentars;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;

    ListView list;
    List<data> itemList = new ArrayList<data>();
    adapterlima adapter;
    private static String url_select     = AppConfig.URL_modul + "review.php";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //biar keyboard sembunyi
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        final RatingBar simpleRatingBar = (RatingBar) findViewById(R.id.simpleRatingBar);
        komentars = (EditText) findViewById(R.id.komentar);
        btnRegister = (Button) findViewById(R.id.b_upload);


        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        // Session manager

        btnRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String rating = "" + simpleRatingBar.getRating();
                // Check for empty data in the form
                if (!rating.isEmpty() ) {
                    // login user
                    String komentar = komentars.getText().toString().trim();

                    registerUser(rating,komentar);

                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Masukan rating anda", Toast.LENGTH_LONG)
                            .show();
                }
            }

        });

        list    = (ListView) findViewById(R.id.list);
        adapter = new adapterlima(ratingdokter.this, itemList);
        list.setAdapter(adapter);

        callVolley();

    }

    private void callVolley(){
        itemList.clear();
        adapter.notifyDataSetChanged();

        Intent intent = getIntent();
        String idKonsultasi = intent.getStringExtra("idKonsultasi");

        db = new SQLiteHandler(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        String uid = user.get("uid");

        // membuat request JSON
        JsonArrayRequest jArr = new JsonArrayRequest(url_select+"?idKonsultasi="+idKonsultasi +"&idUser="+uid, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());

                // Parsing json
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);

                        data item = new data();

                        item.setIdAkun2(obj.getString("idReview"));
                        item.setNameAkun(obj.getString("komentarReview"));
                        item.setIdAkun1(obj.getString("ratingReview"));

                        // menambah item ke array
                        itemList.add(item);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                // notifikasi adanya perubahan data pada adapterakun
                adapter.notifyDataSetChanged();


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

    private void registerUser(final String rating,final String komentar) {
        String tag_string_req = "req_register";

        pDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_modul+"rating.php", new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {

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

                Intent intent = getIntent();
                String idKonsultasi = intent.getStringExtra("idKonsultasi");

                params.put("rating", rating);
                params.put("komentar", komentar);
                params.put("idUser", idUser);
                params.put("idKonsultasi", idKonsultasi);

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
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
