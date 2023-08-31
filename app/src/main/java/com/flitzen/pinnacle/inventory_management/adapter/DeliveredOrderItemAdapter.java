package com.flitzen.pinnacle.inventory_management.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.flitzen.pinnacle.R;
import com.flitzen.pinnacle.inventory_management.model.DeliveredOrderListModel;

import com.flitzen.pinnacle.utils.AppUtil;

import java.util.ArrayList;
import java.util.List;

public class DeliveredOrderItemAdapter extends RecyclerView.Adapter<DeliveredOrderItemAdapter.ViewHolder>{

    private List<DeliveredOrderListModel.OrderItem> orderItemArrayList = new ArrayList<>();
    private Context context;
    private String TAG = "DeliveredOrderItemAdapter";

    public DeliveredOrderItemAdapter(ArrayList<DeliveredOrderListModel.OrderItem> orderItemArrayList, Context context) {
        this.context = context;
        this.orderItemArrayList = orderItemArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.custom_ordered_item_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            holder.txtOrderItemName.setText(orderItemArrayList.get(position).getMachineName());
            holder.txtOrderItemQty.setText(orderItemArrayList.get(position).getMachineQty());
        } catch (Exception e) {
            Log.d(TAG, "Exception delivered order list " + e.getMessage());
           // AppUtil.showToastFail(context, context.getResources().getString(R.string.something_wrong));
        }
    }


    @Override
    public int getItemCount() {
        return orderItemArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtOrderItemName,txtOrderItemQty;
        public ViewHolder(View itemView) {
            super(itemView);
            this.txtOrderItemName =  itemView.findViewById(R.id.txtOrderItemName);
            this.txtOrderItemQty =  itemView.findViewById(R.id.txtOrderItemQty);
        }
    }

    public void updateList(List<DeliveredOrderListModel.OrderItem> orderItemArrayList) {
        this.orderItemArrayList = orderItemArrayList;
        notifyDataSetChanged();
    }
}
