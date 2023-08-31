package com.flitzen.pinnacle.inventory_management.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.flitzen.pinnacle.R;
import com.flitzen.pinnacle.inventory_management.model.InternalMovementItemListModel;
import com.flitzen.pinnacle.inventory_management.model.InternalMovementListModel;

import java.util.ArrayList;

public class InternalMovementItemsAdapter extends RecyclerView.Adapter<InternalMovementItemsAdapter.ViewHolder>{

    private Context context;
    ArrayList<InternalMovementItemListModel.Data> arrayList;

    public InternalMovementItemsAdapter(Context context, ArrayList<InternalMovementItemListModel.Data> arrayList) {
        this.context=context;
        this.arrayList=arrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.custom_internal_movement_items_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.txtItemName.setText(arrayList.get(position).getItemName());
        holder.txtQty.setText(arrayList.get(position).getMoveItemQty());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtItemName,txtQty;
        public ViewHolder(View itemView) {
            super(itemView);
            this.txtItemName =  itemView.findViewById(R.id.txtItemName);
            this.txtQty =  itemView.findViewById(R.id.txtQty);
        }
    }
}
