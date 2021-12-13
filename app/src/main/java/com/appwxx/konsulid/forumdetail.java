package com.appwxx.konsulid;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.appwxx.konsulid.app.AppConfig;
import com.appwxx.konsulid.app.AppController;
import com.appwxx.konsulid.helper.SQLiteHandler;
import com.appwxx.konsulid.helper.SessionManager;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class forumdetail extends AppCompatActivity {

    private static final String url_select     = AppConfig.URL_modul + "selectforumdetail.php";

    private static final String TAG = forumdetail.class.getSimpleName();
    private ProgressDialog pDialog;

    private RecyclerView recyclerView2;
    private List<Movie> itemsList2;
    private StoreAdapter2 mAdapters2;
    private TextView judul,x,tanggal;
    Button kirim;
    EditText komentar;
    private SessionManager session;
    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forumdetail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String namas = intent.getStringExtra("nama");
        String juduls = intent.getStringExtra("judul");
        String tanggals = intent.getStringExtra("tanggal");
        String desc = intent.getStringExtra("desc");

        x = (TextView) findViewById(R.id.x);
        judul = (TextView) findViewById(R.id.judul);
        tanggal = (TextView) findViewById(R.id.tanggal);

        x.setText(namas);
        tanggal.setText(tanggals);
        judul.setText(juduls);

        komentar = (EditText) findViewById(R.id.komentar);


        kirim = (Button) findViewById(R.id.kirim);
        kirim.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String komentars = komentar.getText().toString().trim();
                kirim(komentars);
            }
        });


        recyclerView2 = (RecyclerView) findViewById(R.id.recycler_views);
        itemsList2 = new ArrayList<>();
        mAdapters2 = new forumdetail.StoreAdapter2(getApplicationContext(), itemsList2);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView2.setLayoutManager(mLayoutManager);
        recyclerView2.addItemDecoration(new forumdetail.GridSpacingItemDecoration(2, dpToPx(2), true));
        recyclerView2.setItemAnimator(new DefaultItemAnimator());
        recyclerView2.setAdapter(mAdapters2);
        recyclerView2.setNestedScrollingEnabled(false);
        fetchStoreItems2();
    }

    private void kirim(final String komentars) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_modul+"komentarforum.php", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {


                        Intent intent = getIntent();
                        String id = intent.getStringExtra("id");
                        String nama = intent.getStringExtra("nama");
                        String judul = intent.getStringExtra("judul");
                        String tanggal = intent.getStringExtra("tanggal");

                        Intent intents = new Intent(getApplicationContext(), forumdetail.class);
                        intents.putExtra("id", id);
                        intents.putExtra("nama", nama);
                        intents.putExtra("judul", judul);
                        intents.putExtra("tanggal", tanggal);
                        startActivity(intents);
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
                String status = user.get("created_at");

                Intent intent = getIntent();
                String id = intent.getStringExtra("id");

                params.put("komentar", komentars);
                params.put("id", id);
                params.put("idUser", idUser);
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


    private void fetchStoreItems2() {
        //mSwipeRefreshLayout.setRefreshing(true);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");

        JsonArrayRequest request = new JsonArrayRequest(url_select+"?id="+id,
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
            //holder.price.setText(movie.getPrice());
            holder.latTempat.setText(movie.getLatTempat());
            holder.longTempat.setText(movie.getLongTempat());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                holder.price.setText(Html.fromHtml(movie.getPrice(), Html.FROM_HTML_MODE_COMPACT));
            } else {
                holder.price.setText(Html.fromHtml(movie.getPrice()));
            }

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
