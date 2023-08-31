package com.flitzen.pinnacle.inventory_management.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.flitzen.pinnacle.R;

public class KitAdapter extends RecyclerView.Adapter<KitAdapter.ViewHolder>{

    int position;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.custom_kit_delivery_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }


    @Override
    public int getItemCount() {
       return 10;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtKitName;
        public ViewHolder(View itemView) {
            super(itemView);
            this.txtKitName =  itemView.findViewById(R.id.txtKitName);
        }
    }
}
