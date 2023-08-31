package com.flitzen.pinnacle.inventory_management.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.flitzen.pinnacle.R;
import com.flitzen.pinnacle.inventory_management.model.PendingOrderBOMListModel;

import java.util.ArrayList;

public class ShortageAdapter extends RecyclerView.Adapter<ShortageAdapter.ViewHolder> {

    private Context context;
    int selectedItemPosition;
    private ArrayList<PendingOrderBOMListModel.Data> pendingOrderBOMListArr;

    public ShortageAdapter(Context context, ArrayList<PendingOrderBOMListModel.Data> pendingOrderBOMListArr) {
        this.context = context;
        this.pendingOrderBOMListArr = pendingOrderBOMListArr;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.custom_shortage_list_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PendingOrderBOMListModel.BOMDetails bomDetails = pendingOrderBOMListArr.get(selectedItemPosition).getData().get(position);
        holder.txtPartCode.setText(bomDetails.getBomCode());
        holder.txtPartName.setText(bomDetails.getBomName());
        if (bomDetails.getBomRequireQty().toString().trim().equalsIgnoreCase("0")) {
            holder.txtRequiredQty.setText("-");
        }else {
            holder.txtRequiredQty.setText(bomDetails.getBomRequireQty());
        }

        if (bomDetails.getBomRequireQty() != null && bomDetails.getBomStockQty() != null) {
            String balancedQty = String.valueOf(Float.parseFloat(bomDetails.getBomRequireQty()) - Float.parseFloat(bomDetails.getBomStockQty()));
            if (balancedQty.toString().trim().equalsIgnoreCase("0")){
                holder.txtShortageQty.setText("-");
            }else {
                holder.txtShortageQty.setText(balancedQty);
            }
        }

        if(bomDetails.getBomStockQty().toString().trim().equalsIgnoreCase("0")){
            holder.txtInStockQty.setText("-");
        }else {
            holder.txtInStockQty.setText(bomDetails.getBomStockQty());
        }
    }

    @Override
    public int getItemCount() {
        if (pendingOrderBOMListArr.size() > 0) {
            return pendingOrderBOMListArr.get(selectedItemPosition).getData().size();
        } else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtPartCode, txtPartName, txtInStockQty, txtShortageQty, txtRequiredQty;

        public ViewHolder(View itemView) {
            super(itemView);
            this.txtPartCode = itemView.findViewById(R.id.txtPartCode);
            this.txtPartName = itemView.findViewById(R.id.txtPartName);
            this.txtInStockQty = itemView.findViewById(R.id.txtInStockQty);
            this.txtShortageQty = itemView.findViewById(R.id.txtShortageQty);
            this.txtRequiredQty = itemView.findViewById(R.id.txtRequiredQty);
        }
    }

    public void updateUI(int selectedItemPosition) {
        this.selectedItemPosition = selectedItemPosition;
        notifyDataSetChanged();
    }
}
