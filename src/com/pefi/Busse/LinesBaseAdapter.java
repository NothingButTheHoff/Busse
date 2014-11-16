package com.pefi.Busse;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by pererikfinstad on 05/10/14.
 */
public class LinesBaseAdapter extends BaseAdapter {

    Context context;
    List<Line> rowItem;

    LinesBaseAdapter(Context context, List<Line> rowItem) {
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
            convertView = mInflater.inflate(R.layout.lines_list_item, null);
        }

        TextView name  = (TextView) convertView.findViewById(R.id.lineName);

        Line row_pos = rowItem.get(position);

        name.setText(row_pos.getName());

        return convertView;

    }

} //end of class CustomListAdapter

