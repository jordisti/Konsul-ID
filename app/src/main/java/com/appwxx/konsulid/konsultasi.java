package com.appwxx.konsulid;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.appwxx.konsulid.app.AppConfig;
import com.appwxx.konsulid.app.AppController;
import com.appwxx.konsulid.helper.SQLiteHandler;
import com.appwxx.konsulid.helper.SessionManager;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class konsultasi extends AppCompatActivity {
    private SessionManager session;
    private SQLiteHandler db;


    private RecyclerView recyclerView2;
    private List<Movie> itemsList2;
    private StoreAdapter2 mAdapters2;

    private static final String URL2 = AppConfig.URL_modul + "selectdokter.php";
    private static final String URL4 = AppConfig.URL_modul + "selectdokterfilter.php";

    private static final String TAG = konsultasi.class.getSimpleName();
    EditText cari;
    private FloatingActionButton fab;

    TextView filter;
    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;
    Spinner biaya, rating,kategori;
    String biayas, ratings, kategoris;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konsultasi);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView2 = (RecyclerView) findViewById(R.id.recycler_views);
        itemsList2 = new ArrayList<>();
        mAdapters2 = new konsultasi.StoreAdapter2(getApplicationContext(), itemsList2);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView2.setLayoutManager(mLayoutManager);
        recyclerView2.addItemDecoration(new konsultasi.GridSpacingItemDecoration(2, dpToPx(2), true));
        recyclerView2.setItemAnimator(new DefaultItemAnimator());
        recyclerView2.setAdapter(mAdapters2);
        recyclerView2.setNestedScrollingEnabled(false);

        fetchStoreItems2();
        cari = (EditText) findViewById(R.id.cari);
        filter = (TextView) findViewById(R.id.filter);

        cari.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    String caris = cari.getText().toString().trim();
                    fetchStoreItems3(caris);
                    return true;
                }
                return false;
            }
        });

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterdialog();
            }
        });


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(konsultasi.this, riwayatjanjian.class);
                startActivity(intent);
            }
        });
    }

    private void filterdialog() {
        dialog = new AlertDialog.Builder(konsultasi.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.form_data, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setIcon(R.drawable.logokonsul);
        dialog.setTitle("FILTER BY");

        rating    = (Spinner) dialogView.findViewById(R.id.rating);
        biaya = (Spinner) dialogView.findViewById(R.id.biaya);
        kategori = (Spinner) dialogView.findViewById(R.id.kategori);

        dialog.setPositiveButton("CARI", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                ratings    = rating.getSelectedItem().toString();
                biayas    = biaya.getSelectedItem().toString();
                kategoris  = kategori.getSelectedItem().toString();

                caridata(ratings,biayas,kategoris);

                dialog.dismiss();
            }
        });

        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    private void caridata(final String rating,final String biaya,final String kategori) {
        //mSwipeRefreshLayout.setRefreshing(true);

        db = new SQLiteHandler(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        String idUser = user.get("uid");

        JsonArrayRequest request = new JsonArrayRequest(URL4+"?rating="+rating+"&biaya="+biaya+"&kategori="+kategori,
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

    private void fetchStoreItems3(final String caris) {
        //mSwipeRefreshLayout.setRefreshing(true);

        db = new SQLiteHandler(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        String idUser = user.get("uid");

        JsonArrayRequest request = new JsonArrayRequest(URL2+"?caris="+caris,
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

    private void fetchStoreItems2() {
        //mSwipeRefreshLayout.setRefreshing(true);

        db = new SQLiteHandler(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        String idUser = user.get("uid");

        JsonArrayRequest request = new JsonArrayRequest(URL2+"?caris=",
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
            public TextView name, price, latTempat, longTempat, ids,biayaa;
            public ImageView thumbnail;
            public Button tambahs,pluss,mins;
            public EditText qtys;
            public AppCompatRatingBar rating;

            public MyViewHolder(View view) {
                super(view);
                ids = (TextView) view.findViewById(R.id.ids);
                name = (TextView) view.findViewById(R.id.title);
                price = (TextView) view.findViewById(R.id.price);
                latTempat = (TextView) view.findViewById(R.id.latTempat);
                longTempat = (TextView) view.findViewById(R.id.longTempat);
                thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
                biayaa = (TextView) view.findViewById(R.id.biaya);
                qtys = (EditText) view.findViewById(R.id.jumlah);
                rating = (AppCompatRatingBar) view.findViewById(R.id.rating);


            }
        }


        public StoreAdapter2(Context context, List<Movie> movieList) {
            this.context = context;
            this.movieList = movieList;
        }

        @Override
        public StoreAdapter2.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.store_item_lists, parent, false);

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
            holder.qtys.setText(movie.getJumlah());
            holder.biayaa.setText(movie.getBiaya());

            String ratinynya = movie.getRating();

            if(ratinynya.equals("1")){
                holder.rating.setRating(Float.parseFloat("1.0"));
            }else if(ratinynya.equals("2")){
                holder.rating.setRating(Float.parseFloat("2.0"));
            }else if(ratinynya.equals("3")){
                holder.rating.setRating(Float.parseFloat("3.0"));
            }else if(ratinynya.equals("4")){
                holder.rating.setRating(Float.parseFloat("4.0"));
            }else if(ratinynya.equals("5")){
                holder.rating.setRating(Float.parseFloat("5.0"));
            }else{
                holder.rating.setRating(Float.parseFloat("0"));
            }

            Glide.with(context)
                    .load(movie.getImage())
                    .into(holder.thumbnail);

            holder.thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String idss = movie.getIds();
                    String nama = movie.getTitle();
                    String gambar = movie.getImage();
                    String str = movie.getPrice();
                    String spesialis = movie.getLatTempat();
                    String pendidikan = movie.getLongTempat();
                    String praktek = movie.getJumlah();
                    String biayad = movie.getBiaya();
                    String ratinynya = movie.getRating();

                    Intent intent = new Intent(getApplicationContext(), detail.class);
                    intent.putExtra("nama", nama);
                    intent.putExtra("gambar", gambar);
                    intent.putExtra("str", str);
                    intent.putExtra("idDokter", idss);
                    intent.putExtra("spesialis", spesialis);
                    intent.putExtra("pendidikan", pendidikan);
                    intent.putExtra("praktek", praktek);
                    intent.putExtra("biaya", biayad);
                    intent.putExtra("penilaian", ratinynya);
                    startActivity(intent);
                }
            });
            holder.name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String idss = movie.getIds();
                    String nama = movie.getTitle();
                    String gambar = movie.getImage();
                    String str = movie.getPrice();
                    String spesialis = movie.getLatTempat();
                    String pendidikan = movie.getLongTempat();
                    String praktek = movie.getJumlah();
                    String biayad = movie.getBiaya();
                    String ratinynya = movie.getRating();

                    Intent intent = new Intent(getApplicationContext(), detail.class);
                    intent.putExtra("nama", nama);
                    intent.putExtra("gambar", gambar);
                    intent.putExtra("str", str);
                    intent.putExtra("idDokter", idss);
                    intent.putExtra("spesialis", spesialis);
                    intent.putExtra("pendidikan", pendidikan);
                    intent.putExtra("praktek", praktek);
                    intent.putExtra("biaya", biayad);
                    intent.putExtra("penilaian", ratinynya);
                    startActivity(intent);
                }
            });
            holder.price.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String idss = movie.getIds();
                    String nama = movie.getTitle();
                    String gambar = movie.getImage();
                    String str = movie.getPrice();
                    String spesialis = movie.getLatTempat();
                    String pendidikan = movie.getLongTempat();
                    String praktek = movie.getJumlah();
                    String biayad = movie.getBiaya();
                    String ratinynya = movie.getRating();

                    Intent intent = new Intent(getApplicationContext(), detail.class);
                    intent.putExtra("nama", nama);
                    intent.putExtra("gambar", gambar);
                    intent.putExtra("str", str);
                    intent.putExtra("idDokter", idss);
                    intent.putExtra("spesialis", spesialis);
                    intent.putExtra("pendidikan", pendidikan);
                    intent.putExtra("praktek", praktek);
                    intent.putExtra("biaya", biayad);
                    intent.putExtra("penilaian", ratinynya);
                    startActivity(intent);
                }
            });
            holder.latTempat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String idss = movie.getIds();
                    String nama = movie.getTitle();
                    String gambar = movie.getImage();
                    String str = movie.getPrice();
                    String spesialis = movie.getLatTempat();
                    String pendidikan = movie.getLongTempat();
                    String praktek = movie.getJumlah();
                    String biayad = movie.getBiaya();
                    String ratinynya = movie.getRating();

                    Intent intent = new Intent(getApplicationContext(), detail.class);
                    intent.putExtra("nama", nama);
                    intent.putExtra("gambar", gambar);
                    intent.putExtra("str", str);
                    intent.putExtra("idDokter", idss);
                    intent.putExtra("spesialis", spesialis);
                    intent.putExtra("pendidikan", pendidikan);
                    intent.putExtra("praktek", praktek);
                    intent.putExtra("biaya", biayad);
                    intent.putExtra("penilaian", ratinynya);
                    startActivity(intent);
                }
            });


        }

        @Override
        public int getItemCount() {
            return movieList.size();
        }
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
