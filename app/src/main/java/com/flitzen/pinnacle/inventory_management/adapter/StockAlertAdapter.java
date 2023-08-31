package com.flitzen.pinnacle.inventory_management.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.flitzen.pinnacle.R;
import com.flitzen.pinnacle.inventory_management.fragment.StockAlertsFragment;
import com.flitzen.pinnacle.inventory_management.model.StockAlertListModel;
import com.flitzen.pinnacle.utils.AppUtil;

import java.util.ArrayList;

public class StockAlertAdapter extends RecyclerView.Adapter<StockAlertAdapter.ViewHolder> {

    private Context context;
    ArrayList<StockAlertListModel.Data> arrayListZero;

    public StockAlertAdapter(Context context, ArrayList<StockAlertListModel.Data> arrayListZero) {
        this.context = context;
        this.arrayListZero = arrayListZero;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.custom_stock_alerts_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.txtPartCode.setText(arrayListZero.get(position).getItemCode());
        holder.txtPartName.setText(arrayListZero.get(position).getItemName());
        holder.txtInStockQty.setText(arrayListZero.get(position).getStockQty());

    }

    @Override
    public int getItemCount() {
        return arrayListZero.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtPartCode,txtPartName,txtInStockQty;

        public ViewHolder(View itemView) {
            super(itemView);
            this.txtPartCode = itemView.findViewById(R.id.txtPartCode);
            this.txtPartName = itemView.findViewById(R.id.txtPartName);
            this.txtInStockQty = itemView.findViewById(R.id.txtInStockQty);
        }
    }
}
