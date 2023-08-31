package com.flitzen.pinnacle.inventory_management.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.flitzen.pinnacle.R;
import com.flitzen.pinnacle.inventory_management.model.InternalMovementListModel;
import com.flitzen.pinnacle.utils.AppUtil;

import java.util.ArrayList;

public class InternalMovementsAdapter extends RecyclerView.Adapter<InternalMovementsAdapter.ViewHolder>{

    private Context context;
    ArrayList<InternalMovementListModel.Data> arrayList;
    private ItemClickListener clickListener;

    public InternalMovementsAdapter(Context context, ArrayList<InternalMovementListModel.Data> arrayList) {
        this.context=context;
        this.arrayList=arrayList;
        System.out.println("===============arrayList   "+arrayList.size());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.custom_internal_movements_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            holder.txtDate.setText(arrayList.get(position).getDate());
            holder.txtTime.setText(arrayList.get(position).getTime());
            holder.txtItemName.setText(arrayList.get(position).getItemData().get(0).getItemName());
            holder.txtFrom.setText("Crate - "+arrayList.get(position).getFromCrateName());
            holder.txtTo.setText("Crate - "+arrayList.get(position).getToCrateName());
            holder.txtItemCount.setText("Item("+arrayList.get(position).getItem_total()+")");
            if(Integer.parseInt(arrayList.get(position).getItem_total())>0){
                holder.relShowMore.setVisibility(View.VISIBLE);
            }else {
                holder.relShowMore.setVisibility(View.GONE);
            }
        }catch (Exception e){
            AppUtil.showToastFail(context, context.getString(R.string.something_wrong));
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txtDate,txtTime,txtItemCount,txtItemName,txtFrom,txtTo;
        public RelativeLayout relShowMore;
        public ViewHolder(View itemView) {
            super(itemView);
            this.txtDate =  itemView.findViewById(R.id.txtDate);
            this.txtTime =  itemView.findViewById(R.id.txtTime);
            this.txtItemCount =  itemView.findViewById(R.id.txtItemCount);
            this.txtItemName =  itemView.findViewById(R.id.txtItemName);
            this.txtFrom =  itemView.findViewById(R.id.txtFrom);
            this.txtTo =  itemView.findViewById(R.id.txtTo);
            this.relShowMore =  itemView.findViewById(R.id.relShowMore);
            this.relShowMore.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) clickListener.onClick(v, getAdapterPosition());
        }
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onClick(View view, int position);
    }

    public void updateList(ArrayList<InternalMovementListModel.Data> arrayList){
        this.arrayList=arrayList;
        notifyDataSetChanged();
    }

}
