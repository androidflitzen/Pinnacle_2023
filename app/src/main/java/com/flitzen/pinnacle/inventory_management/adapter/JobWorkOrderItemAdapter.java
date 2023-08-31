package com.flitzen.pinnacle.inventory_management.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import com.flitzen.pinnacle.R;

public class JobWorkOrderItemAdapter extends RecyclerView.Adapter<JobWorkOrderItemAdapter.ViewHolder> {

    int position;

    public JobWorkOrderItemAdapter(int position) {
        this.position = position;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.custom_ordered_item_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.txtOrderItemQty.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        int i = 0;
        if (position==1){
            i=1;
        }
        else if (position%2==0){
            i=2;
        }
        else if(position%3==0){
            i=3;
        }
        else {
            i=1;
        }
        return i;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtOrderItemName, txtOrderItemQty;

        public ViewHolder(View itemView) {
            super(itemView);
            this.txtOrderItemName = itemView.findViewById(R.id.txtOrderItemName);
            this.txtOrderItemQty = itemView.findViewById(R.id.txtOrderItemQty);
        }
    }
}
