package com.appwxx.konsulid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.appwxx.konsulid.helper.SQLiteHandler;
import com.appwxx.konsulid.helper.SessionManager;

import java.util.HashMap;

public class profilpengguna extends AppCompatActivity {
    private static final String TAG = profilpengguna.class.getSimpleName();

    private SessionManager session;
    private SQLiteHandler db;
    TextView nama,kontak,profils,ubahkatasandi,riwayat,riwayatjanji,tentang,faq,logout;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilpengguna);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        db = new SQLiteHandler(getApplicationContext());
        session = new SessionManager(this);

        HashMap<String, String> user = db.getUserDetails();
        String namas = user.get("name");
        String email = user.get("email");

        nama = (TextView) findViewById(R.id.nama);
        kontak = (TextView) findViewById(R.id.kontak);
        profils = (TextView) findViewById(R.id.profils);
        ubahkatasandi = (TextView) findViewById(R.id.ubahkatasandi);
        riwayat = (TextView) findViewById(R.id.riwayat);
        riwayatjanji = (TextView) findViewById(R.id.riwayatjanji);
        tentang = (TextView) findViewById(R.id.tentang);
        faq = (TextView) findViewById(R.id.faq);
        logout = (TextView) findViewById(R.id.logout);

        nama.setText(namas);
        kontak.setText(email);

        profils.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent = new Intent(profilpengguna.this, pengguna.class);
                startActivity(intent);
            }
        });
        ubahkatasandi.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent = new Intent(profilpengguna.this, ubahsandi.class);
                startActivity(intent);
            }
        });
        riwayat.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent = new Intent(profilpengguna.this, riwayatjanjian.class);
                startActivity(intent);
            }
        });
        riwayatjanji.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent = new Intent(profilpengguna.this, riwayatjanjianketemu.class);
                startActivity(intent);
            }
        });
        tentang.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent = new Intent(profilpengguna.this, tentang.class);
                startActivity(intent);
            }
        });
        faq.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent = new Intent(profilpengguna.this, faq.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent(profilpengguna.this,
                        MainActivity.class);
                startActivity(intent);
                finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

}
