package com.flitzen.pinnacle.inventory_management.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.flitzen.pinnacle.R;
import com.flitzen.pinnacle.inventory_management.model.LocationModel;

import java.util.ArrayList;

public class SpinnerLocationAdapter extends ArrayAdapter<LocationModel.Data> {
    private Context context;
    private ArrayList<LocationModel.Data> locationList;

    public SpinnerLocationAdapter(Context context, ArrayList<LocationModel.Data> locationList) {
        super(context, 0, locationList);
        this.context = context;
        this.locationList = locationList;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.custome_spinner_layout, parent, false);
        TextView txtLocationName = row.findViewById(R.id.txtLocationName);
        txtLocationName.setText(locationList.get(position).getLocationName());

        return row;
    }
}
