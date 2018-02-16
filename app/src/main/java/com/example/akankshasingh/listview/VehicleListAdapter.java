package com.example.akankshasingh.listview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Akanksha.Singh on 2/16/2018.
 */

public class VehicleListAdapter extends SimpleAdapter {
    private int[] colors = new int[] { 0x8027AE60  , 0x80F39C12 };

    public VehicleListAdapter(Context context, List<HashMap<String, String>> items, int resource, String[] from, int[] to) {
        super(context, items, resource, from, to);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        int colorPos = position % colors.length;
        view.setBackgroundColor(colors[colorPos]);
        return view;
    }
}