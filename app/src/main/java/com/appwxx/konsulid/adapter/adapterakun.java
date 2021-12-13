package com.appwxx.konsulid.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.appwxx.konsulid.R;
import com.appwxx.konsulid.data.data;

import java.util.List;

/**
 * Created by User on 01/11/2017.
 */

public class adapterakun extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<data> items;
    Context context;

    public adapterakun(Activity activity, List<data> items) {
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
            convertView = inflater.inflate(R.layout.list_rowakun, null);

        TextView idAkun = (TextView) convertView.findViewById(R.id.idAkun);
        TextView nameAkun = (TextView) convertView.findViewById(R.id.nameProduk);
        TextView kdAkun = (TextView) convertView.findViewById(R.id.kdAkun);
        TextView katAkun = (TextView) convertView.findViewById(R.id.kategoriakun);
        TextView saldo = (TextView) convertView.findViewById(R.id.saldo);

        data data = items.get(position);

        idAkun.setText(data.getIdAkun());
        nameAkun.setText(data.getNameAkun());
        kdAkun.setText(data.getKdAkun());
        katAkun.setText(data.getKategoriAkun());
        saldo.setText(data.getSaldoAkun());

        return convertView;
    }
}
