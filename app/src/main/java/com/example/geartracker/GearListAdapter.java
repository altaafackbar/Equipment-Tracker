package com.example.geartracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class GearListAdapter extends ArrayAdapter<Gear> {
    private Context mContext;
    private int mResource;

    public GearListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Gear> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //get relevent info from list
        String date = getItem(position).getDate();
        String des = getItem(position).getDescription();
        double price = getItem(position).getPrice();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);
        //get textviews of custom list view
        TextView tvDate = (TextView) convertView.findViewById(R.id.gearDate);
        TextView tvPrice = (TextView) convertView.findViewById(R.id.price);
        TextView tvDes = (TextView) convertView.findViewById(R.id.gearName);
        //set strings to info from list
        tvDate.setText(String.format("Date: %s", date));
        tvPrice.setText(String.format("Price: %s", String.valueOf(price)));
        tvDes.setText(String.format("Description: %s", des));

        return convertView;

    }
}
