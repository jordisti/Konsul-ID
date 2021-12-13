package com.appwxx.konsulid.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.appwxx.konsulid.R;
import com.appwxx.konsulid.data.data;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by User on 24/06/2018.
 */

public class adapterdua extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<data> items;
    Context context;

    public adapterdua(Activity activity, List<data> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int location) {
        return items.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_rowkategori, null);

        TextView kategori = (TextView) convertView.findViewById(R.id.kategori);
        TextView iddetail = (TextView) convertView.findViewById(R.id.idnya);
        TextView judul = (TextView) convertView.findViewById(R.id.judul);
        TextView keterangan = (TextView) convertView.findViewById(R.id.keterangan);
        TextView tanggal = (TextView) convertView.findViewById(R.id.tanggal);
        ImageView gambar = (ImageView) convertView.findViewById(R.id.gambar);

        data data = items.get(position);

        iddetail.setText(data.getIdJual());
        kategori.setText(data.getKdAkun());
        judul.setText(data.getNameAkun());
        keterangan.setText(data.getKategoriAkun());
        tanggal.setText(data.getDateTransaksi());

        Glide.with(context).load(data.getUrl()).into(gambar);

        return convertView;
    }
}
