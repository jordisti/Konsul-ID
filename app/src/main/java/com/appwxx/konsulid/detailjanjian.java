package com.appwxx.konsulid;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.appwxx.konsulid.app.AppConfig;
import com.appwxx.konsulid.app.AppController;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class detailjanjian extends AppCompatActivity

    {

        private static final String url_select     = AppConfig.URL_modul + "selectdetail.php";
        private static final String TAG = detailartikel.class.getSimpleName();
        private ProgressDialog pDialog;

        private RecyclerView recyclerView2;
        private List<Movie> itemsList2;
        private StoreAdapter2 mAdapters2;
        private TextView nama,spesialis,str,pendidikan,praktek;
        ImageView imgnya;
        Button chat;
        AppCompatRatingBar penilaian;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailjanjian);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        Intent intent = getIntent();
        String idDokter = intent.getStringExtra("idDokter");
        String namas = intent.getStringExtra("nama");
        String spesialiss = intent.getStringExtra("spesialis");
        String strs = intent.getStringExtra("str");
        String pendidikans = intent.getStringExtra("pendidikan");
        String gambars = intent.getStringExtra("gambar");
        String prakteks = intent.getStringExtra("praktek");
        String penilaians = intent.getStringExtra("penilaian");


        nama = (TextView) findViewById(R.id.nama);
        spesialis = (TextView) findViewById(R.id.spesialis);
        penilaian = (AppCompatRatingBar) findViewById(R.id.rating);
        str = (TextView) findViewById(R.id.str);
        pendidikan = (TextView) findViewById(R.id.pendidikan);
        praktek = (TextView) findViewById(R.id.praktek);
        imgnya = (ImageView) findViewById(R.id.backdrop);

        nama.setText(namas);
        spesialis.setText(spesialiss);
        //penilaian.setText(penilaians);
        str.setText(strs);
        pendidikan.setText(pendidikans);
        praktek.setText(prakteks);

        String ratinynya = penilaians;

        if(ratinynya.equals("1")){
            penilaian.setRating(Float.parseFloat("1.0"));
        }else if(ratinynya.equals("2")){
            penilaian.setRating(Float.parseFloat("2.0"));
        }else if(ratinynya.equals("3")){
            penilaian.setRating(Float.parseFloat("3.0"));
        }else if(ratinynya.equals("4")){
            penilaian.setRating(Float.parseFloat("4.0"));
        }else if(ratinynya.equals("5")){
            penilaian.setRating(Float.parseFloat("5.0"));
        }else{
            penilaian.setRating(Float.parseFloat("0"));
        }

        Glide.with(getApplicationContext()).load(gambars).into(imgnya);

        chat = (Button) findViewById(R.id.chat);
        chat.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intents = getIntent();
                String idDokter = intents.getStringExtra("idDokter");
                String namas = intents.getStringExtra("nama");
                String spesialiss = intents.getStringExtra("spesialis");
                String gambars = intents.getStringExtra("gambar");

                Intent intent = new Intent(
                        detailjanjian.this,
                        daftarjanjian.class);
                intent.putExtra("idDokter", idDokter);
                intent.putExtra("nama", namas);
                intent.putExtra("spesialis", spesialiss);
                intent.putExtra("gambar", gambars);
                startActivity(intent);
                finish();
            }
        });

        recyclerView2 = (RecyclerView) findViewById(R.id.recycler_views);
        itemsList2 = new ArrayList<>();
        mAdapters2 = new detailjanjian.StoreAdapter2(getApplicationContext(), itemsList2);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView2.setLayoutManager(mLayoutManager);
        recyclerView2.addItemDecoration(new detailjanjian.GridSpacingItemDecoration(2, dpToPx(2), true));
        recyclerView2.setItemAnimator(new DefaultItemAnimator());
        recyclerView2.setAdapter(mAdapters2);
        recyclerView2.setNestedScrollingEnabled(false);
        fetchStoreItems2();
    }


        private void fetchStoreItems2() {
        //mSwipeRefreshLayout.setRefreshing(true);

        Intent intent = getIntent();
        String idDokter = intent.getStringExtra("idDokter");

        JsonArrayRequest request = new JsonArrayRequest(url_select+"?idDokter="+idDokter,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response == null) {
                            Toast.makeText(getApplicationContext(), "Couldn't fetch the store items! Pleas try again.", Toast.LENGTH_LONG).show();
                            return;
                        }

                        List<Movie> items = new Gson().fromJson(response.toString(), new TypeToken<List<Movie>>() {
                        }.getType());

                        itemsList2.clear();
                        itemsList2.addAll(items);

                        // refreshing recycler view
                        mAdapters2.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error in getting json
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        AppController.getInstance().addToRequestQueue(request);
    }

        public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

            private int spanCount;
            private int spacing;
            private boolean includeEdge;

            public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
                this.spanCount = spanCount;
                this.spacing = spacing;
                this.includeEdge = includeEdge;
            }

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int position = parent.getChildAdapterPosition(view); // item position
                int column = position % spanCount; // item column

                if (includeEdge) {
                    outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                    outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                    if (position < spanCount) { // top edge
                        outRect.top = spacing;
                    }
                    outRect.bottom = spacing; // item bottom
                } else {
                    outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                    outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                    if (position >= spanCount) {
                        outRect.top = spacing; // item top
                    }
                }
            }
        }

        /**
         * Converting dp to pixel
         */
        private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }



        class StoreAdapter2 extends RecyclerView.Adapter<StoreAdapter2.MyViewHolder> {
            private Context context;
            private List<Movie> movieList;

            public class MyViewHolder extends RecyclerView.ViewHolder {
                public TextView name, price, latTempat, longTempat, ids;
                public ImageView thumbnail;
                public EditText qtys;

                public MyViewHolder(View view) {
                    super(view);
                    ids = (TextView) view.findViewById(R.id.ids);
                    name = (TextView) view.findViewById(R.id.title);
                    price = (TextView) view.findViewById(R.id.price);
                    latTempat = (TextView) view.findViewById(R.id.latTempat);
                    longTempat = (TextView) view.findViewById(R.id.longTempat);
                    thumbnail = (ImageView) view.findViewById(R.id.thumbnail);


                }
            }


            public StoreAdapter2(Context context, List<Movie> movieList) {
                this.context = context;
                this.movieList = movieList;
            }

            @Override
            public StoreAdapter2.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.store_item_rowss, parent, false);

                return new StoreAdapter2.MyViewHolder(itemView);
            }

            @Override
            public void onBindViewHolder(StoreAdapter2.MyViewHolder holder, final int position) {
                final Movie movie = movieList.get(position);
                holder.ids.setText(movie.getIds());
                holder.name.setText(movie.getTitle());
                holder.price.setText(movie.getPrice());
                holder.latTempat.setText(movie.getLatTempat());
                holder.longTempat.setText(movie.getLongTempat());

                Glide.with(context)
                        .load(movie.getImage())
                        .into(holder.thumbnail);
            }

            @Override
            public int getItemCount() {
                return movieList.size();
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
