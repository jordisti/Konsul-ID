package com.appwxx.konsulid;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
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
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    private SessionManager session;
    private SQLiteHandler db;

    GridView androidGridView;

    String[] gridViewString = {
            "Konsultasi", "Artikel", "Buat Janji", "Forum",
    };
    int[] gridViewImageId = {
            R.drawable.konsultasi, R.drawable.artikel, R.drawable.janjian, R.drawable.forum,

    };

    private RecyclerView recyclerView2;
    private List<Movie> itemsList2;
    private StoreAdapter2 mAdapters2;

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String URL2 = AppConfig.URL_modul + "selectartikelhome.php";

    CarouselView carouselView;
    int[] sampleImages = {R.drawable.bg1, R.drawable.bg2, R.drawable.bg3};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        db = new SQLiteHandler(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        String status = user.get("created_at");
        String nama = user.get("name");


        EditText cari = (EditText) findViewById(R.id.cari);
        cari.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, konsultasi.class);
                startActivity(intent);

            }
        });

        ImageView profil = (ImageView) findViewById(R.id.imageView);
        profil.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, profilpengguna.class);
                startActivity(intent);

            }
        });

        carouselView = (CarouselView) findViewById(R.id.carouselView);
        carouselView.setPageCount(sampleImages.length);
        carouselView.setImageListener(imageListener);

        CustomGridViewActivity adapterViewAndroid = new CustomGridViewActivity(this, gridViewString, gridViewImageId);
        androidGridView = (GridView) findViewById(R.id.grid_view_image_text);
        androidGridView.setAdapter(adapterViewAndroid);
        androidGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int i, long id) {
                //Toast.makeText(MainActivity.this, "GridView Item: " + gridViewString[+i], Toast.LENGTH_LONG).show();

                String menu = gridViewString[+i];

                if (menu.equals("Konsultasi")) {
                    Intent intent = new Intent(
                            MainActivity.this,
                            konsultasi.class);
                    startActivity(intent);
                }else if (menu.equals("Artikel")) {
                    Intent intent = new Intent(
                            MainActivity.this,
                            artikel.class);
                    startActivity(intent);
                } else if (menu.equals("Forum")) {
                    Intent intent = new Intent(
                            MainActivity.this,
                            forum.class);
                    startActivity(intent);
                } else if (menu.equals("Buat Janji")) {
                    Intent intent = new Intent(
                            MainActivity.this,
                            buatjanji.class);
                    startActivity(intent);
                }
            }
        });

        recyclerView2 = (RecyclerView) findViewById(R.id.recycler_views);
        itemsList2 = new ArrayList<>();
        mAdapters2 = new MainActivity.StoreAdapter2(getApplicationContext(), itemsList2);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView2.setLayoutManager(mLayoutManager);
        recyclerView2.addItemDecoration(new MainActivity.GridSpacingItemDecoration(2, dpToPx(2), true));
        recyclerView2.setItemAnimator(new DefaultItemAnimator());
        recyclerView2.setAdapter(mAdapters2);
        recyclerView2.setNestedScrollingEnabled(false);

        fetchStoreItems2();
    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(sampleImages[position]);
        }
    };

    private void fetchStoreItems2() {
        //mSwipeRefreshLayout.setRefreshing(true);

        db = new SQLiteHandler(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        String idUser = user.get("uid");

        JsonArrayRequest request = new JsonArrayRequest(URL2,
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
            public Button tambahs,pluss,mins;
            public EditText qtys;

            public MyViewHolder(View view) {
                super(view);
                ids = (TextView) view.findViewById(R.id.ids);
                name = (TextView) view.findViewById(R.id.title);
                price = (TextView) view.findViewById(R.id.price);
                latTempat = (TextView) view.findViewById(R.id.latTempat);
                longTempat = (TextView) view.findViewById(R.id.longTempat);
                thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
                tambahs = (Button) view.findViewById(R.id.tambah);

                pluss = (Button) view.findViewById(R.id.plus);
                mins = (Button) view.findViewById(R.id.min);
                qtys = (EditText) view.findViewById(R.id.jumlah);


            }
        }


        public StoreAdapter2(Context context, List<Movie> movieList) {
            this.context = context;
            this.movieList = movieList;
        }

        @Override
        public StoreAdapter2.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.store_item_list, parent, false);

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

            Glide.with(context)
                    .load(movie.getImage())
                    .into(holder.thumbnail);

            holder.thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String idss = movie.getIds();
                    String nama = movie.getTitle();
                    String gambar = movie.getImage();
                    String kategori = movie.getPrice();
                    String keterangan = movie.getLongTempat();

                    Intent intent = new Intent(getApplicationContext(), detailartikel.class);
                    intent.putExtra("nama", nama);
                    intent.putExtra("gambar", gambar);
                    intent.putExtra("kategori", kategori);
                    intent.putExtra("idInformasi", idss);
                    intent.putExtra("keterangan", keterangan);
                    startActivity(intent);
                }
            });

            holder.name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String idss = movie.getIds();
                    String nama = movie.getTitle();
                    String gambar = movie.getImage();
                    String kategori = movie.getPrice();
                    String keterangan = movie.getLongTempat();

                    Intent intent = new Intent(getApplicationContext(), detailartikel.class);
                    intent.putExtra("nama", nama);
                    intent.putExtra("gambar", gambar);
                    intent.putExtra("kategori", kategori);
                    intent.putExtra("idInformasi", idss);
                    intent.putExtra("keterangan", keterangan);
                    startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return movieList.size();
        }
    }


}
