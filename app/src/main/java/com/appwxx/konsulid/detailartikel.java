package com.appwxx.konsulid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class detailartikel extends AppCompatActivity {

    private static final String TAG = detailartikel.class.getSimpleName();
    private ProgressDialog pDialog;

    private TextView nama,kategori,keterangan;
    ImageView imgnya;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailartikel);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        Intent intent = getIntent();
        String idDokter = intent.getStringExtra("idInformasi");
        String namas = intent.getStringExtra("nama");
        String kategoris = intent.getStringExtra("kategori");
        String keterangans = intent.getStringExtra("keterangan");
        String gambars = intent.getStringExtra("gambar");


        nama = (TextView) findViewById(R.id.nama);
        kategori = (TextView) findViewById(R.id.kategori);
        keterangan = (TextView) findViewById(R.id.keterangan);
        imgnya = (ImageView) findViewById(R.id.backdrop);

        nama.setText(namas);
        kategori.setText(kategoris);
        //keterangan.setText(keterangans);

        Glide.with(getApplicationContext()).load(gambars).into(imgnya);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            keterangan.setText(Html.fromHtml(keterangans, Html.FROM_HTML_MODE_COMPACT));
        } else {
            keterangan.setText(Html.fromHtml(keterangans));
        }

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
