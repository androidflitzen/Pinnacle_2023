package com.flitzen.pinnacle.inventory_management.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.flitzen.pinnacle.R;
import com.flitzen.pinnacle.inventory_management.model.CratesListInternalMovementsModel;
import com.flitzen.pinnacle.inventory_management.model.LocationModel;
import com.flitzen.pinnacle.utils.AppUtil;

import java.util.ArrayList;

public class CrateAdapter extends RecyclerView.Adapter<CrateAdapter.ViewHolder>{

    private Context context;
    private ArrayList<CratesListInternalMovementsModel.Data> cratesList = new ArrayList<>();
    private ItemClickListener clickListener;

    public CrateAdapter(Context context, ArrayList<CratesListInternalMovementsModel.Data> cratesList) {
        this.context=context;
        this.cratesList=cratesList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.custom_crate_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            holder.txtCrateName.setText(cratesList.get(position).getCrateName());
            holder.txtCrateCount.setText("Total Item : "+cratesList.get(position).getTotalItems());
        }catch (Exception e){
            AppUtil.showToastFail(context, context.getString(R.string.something_wrong));
        }
    }

    @Override
    public int getItemCount() {
        return cratesList.size();
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onClickCrate(View view, int position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public CardView cardCrate;
        public TextView txtCrateName,txtCrateCount;
        public ViewHolder(View itemView) {
            super(itemView);
            this.cardCrate =  itemView.findViewById(R.id.cardCrate);
            this.txtCrateName =  itemView.findViewById(R.id.txtCrateName);
            this.txtCrateCount =  itemView.findViewById(R.id.txtCrateCount);
            this.cardCrate.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onClickCrate(view, getAdapterPosition());
        }
    }
}
