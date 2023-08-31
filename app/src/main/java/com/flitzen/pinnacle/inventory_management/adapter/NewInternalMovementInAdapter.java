package com.flitzen.pinnacle.inventory_management.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.flitzen.pinnacle.R;
import com.flitzen.pinnacle.inventory_management.model.CraeteItemModel;

import java.util.ArrayList;

public class NewInternalMovementInAdapter extends RecyclerView.Adapter<NewInternalMovementInAdapter.ViewHolder>{

    private Context context;
    private ArrayList<CraeteItemModel> arrayListFromStoreTo;

    public NewInternalMovementInAdapter(Context context, ArrayList<CraeteItemModel> arrayListFromStoreTo) {
        this.context=context;
        this.arrayListFromStoreTo=arrayListFromStoreTo;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.custom_new_internal_movement_in_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.txtItemName.setText(arrayListFromStoreTo.get(position).getName());
        holder.txtItemQty.setText(arrayListFromStoreTo.get(position).getQty());
    }

    @Override
    public int getItemCount() {
        return arrayListFromStoreTo.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtItemName,txtItemQty;

        public ViewHolder(View itemView) {
            super(itemView);
            this.txtItemName =  itemView.findViewById(R.id.txtItemName);
            this.txtItemQty =  itemView.findViewById(R.id.txtItemQty);
        }
    }
}
