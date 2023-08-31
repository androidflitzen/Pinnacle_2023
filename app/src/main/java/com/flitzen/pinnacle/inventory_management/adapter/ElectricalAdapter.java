package com.flitzen.pinnacle.inventory_management.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.flitzen.pinnacle.R;
import com.flitzen.pinnacle.inventory_management.model.StockCheckPartsListModel;
import com.flitzen.pinnacle.utils.AppUtil;

import java.util.ArrayList;

public class ElectricalAdapter extends RecyclerView.Adapter<ElectricalAdapter.ViewHolder>{

    private Context context;
    private ItemClickListener clickListener;
    private ArrayList<StockCheckPartsListModel.Data> arrayList;

    public ElectricalAdapter(Context context, ArrayList<StockCheckPartsListModel.Data> arrayList) {
        this.context=context;
        this.arrayList=arrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.custom_electrical_list_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            holder.txtInStockQty.setText(String.valueOf(arrayList.get(position).getInStockQty()));
            holder.txtParticular.setText(arrayList.get(position).getItemName());
            holder.txtPartCode.setText(arrayList.get(position).getItemCode());
        }catch (Exception e){

        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txtPartCode,txtParticular,txtInStockQty;
        public AppCompatButton btnLocate;
        public RelativeLayout relLocate;
        public ViewHolder(View itemView) {
            super(itemView);
            this.txtPartCode =  itemView.findViewById(R.id.txtPartCode);
            this.txtParticular =  itemView.findViewById(R.id.txtParticular);
            this.txtInStockQty =  itemView.findViewById(R.id.txtInStockQty);
            this.btnLocate =  itemView.findViewById(R.id.btnLocate);
            this.relLocate =  itemView.findViewById(R.id.relLocate);

            this.btnLocate.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onClick(view, getAdapterPosition());
        }
    }
}
