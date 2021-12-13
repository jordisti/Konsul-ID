package com.appwxx.konsulid.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.AppCompatRatingBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.appwxx.konsulid.R;
import com.appwxx.konsulid.data.data;

import java.util.List;

public class adapterlima extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<data> items;
    Context context;

    public adapterlima(Activity activity, List<data> items) {
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
            convertView = inflater.inflate(R.layout.list_rowrating, null);

        TextView id = (TextView) convertView.findViewById(R.id.idnya);
        TextView nama = (TextView) convertView.findViewById(R.id.namanya);
        AppCompatRatingBar rating = (AppCompatRatingBar) convertView.findViewById(R.id.rt_bar);

        data data = items.get(position);

        id.setText(data.getIdAkun2());
        nama.setText(data.getNameAkun());

        String ratinynya = data.getIdAkun1();

        if(ratinynya.equals("1")){
            rating.setRating(Float.parseFloat("1.0"));
        }else if(ratinynya.equals("2")){
            rating.setRating(Float.parseFloat("2.0"));
        }else if(ratinynya.equals("3")){
            rating.setRating(Float.parseFloat("3.0"));
        }else if(ratinynya.equals("4")){
            rating.setRating(Float.parseFloat("4.0"));
        }else if(ratinynya.equals("5")){
            rating.setRating(Float.parseFloat("5.0"));
        }else{
            rating.setRating(Float.parseFloat("0"));
        }

        return convertView;
    }
}
