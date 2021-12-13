package com.appwxx.konsulid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.appwxx.konsulid.adapter.adapterdua;
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

public class riwayatjanjianketemu extends AppCompatActivity {

    LayoutInflater inflater;
    View dialogView;
    ListView list;
    List<data> itemList = new ArrayList<data>();
    adapterdua adapter;
    int success;
    private static final String TAG = riwayatjanjianketemu.class.getSimpleName();
    private static String url_cari     = AppConfig.URL_modul + "selectriwayatjanjian.php";

    private SessionManager session;
    private SQLiteHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayats);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        list    = (ListView) findViewById(R.id.list);
        // untuk mengisi data dari JSON ke dalam adapter
        adapter = new adapterdua(riwayatjanjianketemu.this, itemList);
        list.setAdapter(adapter);

        callVolley();
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                /*
                final String kdx = itemList.get(position).getIdAkun();
                final String idDokter = itemList.get(position).getIdPartner();
                final String namas = itemList.get(position).getNameAkun();
                final String spesialiss = itemList.get(position).getIsiPesan();
                final String gambars = itemList.get(position).getUrl();
                final String biaya = itemList.get(position).getHargaProduk();
                final String status = itemList.get(position).getKdAkun();

                Intent intent = new Intent(view.getContext(), riwayatchat.class);
                intent.putExtra("idKonsultasi", kdx);
                intent.putExtra("nama", namas);
                intent.putExtra("spesialis", spesialiss);
                intent.putExtra("gambar", gambars);
                intent.putExtra("biaya", biaya);

                startActivity(intent);*/

            }
        });

    }

    // untuk menampilkan semua data pada listview
    private void callVolley(){
        itemList.clear();
        adapter.notifyDataSetChanged();
        //swipe.setRefreshing(true);

        db = new SQLiteHandler(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        String uid = user.get("uid");
        // membuat request JSON
        JsonArrayRequest jArr = new JsonArrayRequest(url_cari+"?idUser="+uid, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());

                // Parsing json
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);

                        data item = new data();

                        item.setIdAkun(obj.getString("idKonsultasi"));
                        item.setNameAkun(obj.getString("dokter"));
                        item.setKategoriAkun(obj.getString("waktu"));
                        item.setDateTransaksi(obj.getString("dateKonsultasi"));
                        item.setIdAkun1(obj.getString("jamKonsultasi"));
                        item.setIdAkun2(obj.getString("selesaiKonsultasi"));
                        item.setUrl(obj.getString("gambar"));
                        item.setKdAkun(obj.getString("statusKonsultasi"));
                        item.setIdPartner(obj.getString("idDokter"));
                        item.setIsiPesan(obj.getString("spesialis"));
                        item.setHargaProduk(obj.getString("biaya"));

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
