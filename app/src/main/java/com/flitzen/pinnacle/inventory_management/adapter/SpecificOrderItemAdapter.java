package com.flitzen.pinnacle.inventory_management.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.flitzen.pinnacle.R;
import com.flitzen.pinnacle.inventory_management.model.PendingOrderListModel;

import java.util.List;

public class SpecificOrderItemAdapter extends RecyclerView.Adapter<SpecificOrderItemAdapter.ViewHolder> {

    private Context context;
    private ItemClickListener clickListener;
    private List<PendingOrderListModel.OrderItem> orderItem;

    public SpecificOrderItemAdapter(Context context, List<PendingOrderListModel.OrderItem> orderItem) {
        this.context = context;
        this.orderItem = orderItem;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.custom_order_item_list_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.txtItemName.setText(orderItem.get(position).getMachineName());
        holder.txtItemQty.setText(orderItem.get(position).getMachineQty());
        if (orderItem.get(position).getMachineStatus() == 0) {
            holder.txtOrderStatus.setText(context.getResources().getString(R.string.pending));
            holder.linOrderStatus.getBackground().setTint(context.getResources().getColor(R.color.colorPrimary));
            holder.linBOM.setVisibility(View.VISIBLE);
        } else {
            holder.txtOrderStatus.setText(context.getResources().getString(R.string.delivered));
            holder.linOrderStatus.getBackground().setTint(context.getResources().getColor(R.color.green_box_color));
            holder.linBOM.setVisibility(View.INVISIBLE);
        }

        if (orderItem.get(position).getIs_shortage() == 1) {
            holder.linShortage.setVisibility(View.VISIBLE);
        } else {
            holder.linShortage.setVisibility(View.INVISIBLE);
        }
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onClick(View view, int position);
    }

    @Override
    public int getItemCount() {
        return orderItem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txtOrderStatus, txtItemName, txtItemQty, txtBom;
        public RecyclerView recyclerItem, recyclerKit;
        public LinearLayout linShortage, linMain, linOrderStatus,linBOM;
        public AppCompatButton btnShortage;

        public ViewHolder(View itemView) {
            super(itemView);
            this.txtOrderStatus = itemView.findViewById(R.id.txtOrderStatus);
            this.txtItemName = itemView.findViewById(R.id.txtItemName);
            this.txtItemQty = itemView.findViewById(R.id.txtItemQty);
            this.txtBom = itemView.findViewById(R.id.txtBom);
            this.linShortage = itemView.findViewById(R.id.linShortage);
            this.btnShortage = itemView.findViewById(R.id.btnShortage);
            this.linOrderStatus = itemView.findViewById(R.id.linOrderStatus);
            this.linBOM = itemView.findViewById(R.id.linBOM);
            this.btnShortage.setOnClickListener(this);
            this.txtBom.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onClick(view, getAdapterPosition());
        }
    }
}
