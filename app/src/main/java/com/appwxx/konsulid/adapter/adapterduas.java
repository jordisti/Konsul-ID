package com.appwxx.konsulid.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.appwxx.konsulid.R;
import com.appwxx.konsulid.data.data;

import java.util.List;

public class adapterduas extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<data> items;
    Context context;

    public adapterduas(Activity activity, List<data> items) {
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
            convertView = inflater.inflate(R.layout.list_rowkategorisd, null);

        TextView nama = (TextView) convertView.findViewById(R.id.namanya);
        TextView iddetail = (TextView) convertView.findViewById(R.id.idnya);
        TextView pesan = (TextView) convertView.findViewById(R.id.textView19);
        ImageView imgnya = (ImageView) convertView.findViewById(R.id.imgnya);
        TextView jabatan = (TextView) convertView.findViewById(R.id.textView23);

        data data = items.get(position);

        iddetail.setText(data.getIdJual());
        nama.setText(data.getNameProduk());
        pesan.setText(data.getSubtotalJual());
        jabatan.setText(data.getDateJual());

        Glide.with(context).load(data.getIsiPesan()).into(imgnya);

        return convertView;
    }
}
