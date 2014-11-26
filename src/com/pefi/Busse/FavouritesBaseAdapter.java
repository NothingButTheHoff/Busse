package com.pefi.Busse;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by pererikfinstad on 05/10/14.
 */
public class FavouritesBaseAdapter extends BaseAdapter {
    public static final String TAG = "FavouritesBaseAdapter";

    Context context;
    List<Favourite> rowItem;

    FavouritesBaseAdapter(Context context, List<Favourite> rowItem) {
        this.context = context;
        this.rowItem = rowItem;

    }


    @Override
    public int getCount() {

        return rowItem.size();
    }

    @Override
    public Object getItem(int position) {

        return rowItem.get(position);
    }

    @Override
    public long getItemId(int position) {

        return rowItem.indexOf(getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.favourites_list_item, null);
        }

        View lineColor    = (View) convertView.findViewById(R.id.lineColor);
        TextView lineName = (TextView) convertView.findViewById(R.id.lineName);
        TextView first    = (TextView) convertView.findViewById(R.id.firstArrival);
        TextView second   = (TextView) convertView.findViewById(R.id.secondArrival);
        TextView third    = (TextView) convertView.findViewById(R.id.thirdArrival);

        Favourite row_pos = rowItem.get(position);

        lineName.setText(row_pos.getLineName());
        first.setText(row_pos.getFirstArrival());
        second.setText(row_pos.getSecondArrival());
        third.setText(row_pos.getThirdArrival());

        String hex = "#" + row_pos.getLineColor();
        int i = Color.parseColor(hex);

        lineColor.setBackgroundColor(i);

        return convertView;

    }


} //end of class CustomListAdapter

