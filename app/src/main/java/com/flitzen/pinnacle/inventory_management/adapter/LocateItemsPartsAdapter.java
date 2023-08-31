package com.flitzen.pinnacle.inventory_management.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.flitzen.pinnacle.R;
import com.flitzen.pinnacle.inventory_management.model.CratesListModel;

import java.util.ArrayList;

public class LocateItemsPartsAdapter extends RecyclerView.Adapter<LocateItemsPartsAdapter.ViewHolder>{

    private Context context;
    ArrayList<CratesListModel.Data> cratesList;

    public LocateItemsPartsAdapter(Context context, ArrayList<CratesListModel.Data> cratesList) {
        this.context=context;
        this.cratesList=cratesList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.custom_locate_items_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.txtCrateId.setText(cratesList.get(position).getCrateName());
        holder.txtNoOfPcs.setText(String.valueOf(cratesList.get(position).getTotalQty()));
    }

    @Override
    public int getItemCount() {
        return cratesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtCrateId,txtNoOfPcs,txtStatus;

        public ViewHolder(View itemView) {
            super(itemView);
            this.txtCrateId =  itemView.findViewById(R.id.txtCrateId);
            this.txtNoOfPcs =  itemView.findViewById(R.id.txtNoOfPcs);
            this.txtStatus =  itemView.findViewById(R.id.txtStatus);
        }
    }
}
