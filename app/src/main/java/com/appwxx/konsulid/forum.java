package com.appwxx.konsulid;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.appwxx.konsulid.adapter.adaptersiswa;
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

public class forum extends AppCompatActivity {
    int fragCount;
    ListView list;
    List<data> itemList = new ArrayList<data>();
    adaptersiswa adapter;

    private SessionManager session;
    private SQLiteHandler db;
    private static final String TAG = register.class.getSimpleName();

    private static String url_select     = AppConfig.URL_modul + "selectforum.php";

    private FloatingActionButton fab;
    EditText cari;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        list    = (ListView) findViewById(R.id.list);
        adapter = new adaptersiswa(this, itemList);
        list.setAdapter(adapter);

        callVolley();

        cari = (EditText) findViewById(R.id.cari);

        cari.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    String caris = cari.getText().toString().trim();
                    callVolley2(caris);
                    return true;
                }
                return false;
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String kdx = itemList.get(position).getIdPesan();
                final String nama = itemList.get(position).getTujuanPesan();
                final String judul = itemList.get(position).getIsiPesan();
                final String tanggal = itemList.get(position).getDatePesan();
                final String desc = itemList.get(position).getUrl();

                Intent intent = new Intent(view.getContext(), forumdetail.class);
                intent.putExtra("id", kdx);
                intent.putExtra("nama", nama);
                intent.putExtra("judul", judul);
                intent.putExtra("tanggal", tanggal);
                intent.putExtra("desc", desc);
                startActivity(intent);
            }
        });

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), buatforum.class);
                startActivity(intent);
            }
        });
    }

    private void callVolley(){
        itemList.clear();
        adapter.notifyDataSetChanged();

        db = new SQLiteHandler(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        String idUser = user.get("uid");
        // membuat request JSON
        JsonArrayRequest jArr = new JsonArrayRequest(url_select+"?idUser="+idUser, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());

                // Parsing json
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);

                        data item = new data();

                        item.setIdPesan(obj.getString("idForum"));
                        item.setTujuanPesan(obj.getString("pembuat"));
                        item.setIsiPesan(obj.getString("judulForum"));
                        item.setDatePesan(obj.getString("dateForum"));
                        item.setUrl(obj.getString("descForum"));

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

    private void callVolley2(final String caris){
        itemList.clear();
        adapter.notifyDataSetChanged();

        db = new SQLiteHandler(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        String idUser = user.get("uid");
        // membuat request JSON
        JsonArrayRequest jArr = new JsonArrayRequest(url_select+"?caris="+caris, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());

                // Parsing json
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);

                        data item = new data();

                        item.setIdPesan(obj.getString("idForum"));
                        item.setTujuanPesan(obj.getString("pembuat"));
                        item.setIsiPesan(obj.getString("judulForum"));
                        item.setDatePesan(obj.getString("dateForum"));
                        item.setUrl(obj.getString("descForum"));

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
