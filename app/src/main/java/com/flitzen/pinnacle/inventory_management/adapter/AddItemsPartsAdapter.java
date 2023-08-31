package com.flitzen.pinnacle.inventory_management.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.flitzen.pinnacle.R;
import com.flitzen.pinnacle.inventory_management.model.CratesListModel;
import com.flitzen.pinnacle.utils.AppUtil;

import java.util.ArrayList;

public class AddItemsPartsAdapter extends RecyclerView.Adapter<AddItemsPartsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<CratesListModel.Data> cratesList = new ArrayList<>();
    private ItemClickListener clickListener;
    private int selectedPosition = -1;

    public AddItemsPartsAdapter(Context context, ArrayList<CratesListModel.Data> cratesList) {
        this.context = context;
        this.cratesList = cratesList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.custom_locate_items_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        try {
            holder.llLocation.setVisibility(View.VISIBLE);
            if (selectedPosition == position) {
                holder.linMainCrate.getBackground().setTint(context.getResources().getColor(R.color.blue));
                //  holder.imgBox.getBackground().setTint(context.getResources().getColor(R.color.white));
                // holder.imgBox.setColorFilter(ContextCompat.getColor(context, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);
                holder.imgBox.setColorFilter(ContextCompat.getColor(context, R.color.white));
                holder.txtCrate.setTextColor(context.getResources().getColor(R.color.white));
                holder.txtCrateId.setTextColor(context.getResources().getColor(R.color.white));
                holder.tvLocation.setTextColor(context.getResources().getColor(R.color.white));
                holder.tvLocationId.setTextColor(context.getResources().getColor(R.color.white));
            } else {
                holder.linMainCrate.getBackground().setTint(context.getResources().getColor(R.color.light_black));
                // holder.imgBox.getBackground().setTint(context.getResources().getColor(R.color.blue));
                // holder.imgBox.setColorFilter(ContextCompat.getColor(context, R.color.blue), android.graphics.PorterDuff.Mode.MULTIPLY);
                holder.imgBox.setColorFilter(ContextCompat.getColor(context, R.color.blue));
                holder.txtCrate.setTextColor(context.getResources().getColor(R.color.light_blue_icon_color));
                holder.txtCrateId.setTextColor(context.getResources().getColor(R.color.dark_blue_color));
                holder.tvLocation.setTextColor(context.getResources().getColor(R.color.light_blue_icon_color));
                holder.tvLocationId.setTextColor(context.getResources().getColor(R.color.light_blue_icon_color));
            }
            holder.txtCrateId.setText(cratesList.get(position).getCrateName());
            holder.tvLocationId.setText(cratesList.get(position).getLocation_name());
            holder.txtNoOfPcs.setText(String.valueOf(cratesList.get(position).getTotalQty()));
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
        public TextView txtCrateId, txtNoOfPcs, txtStatus,txtCrate, tvLocation, tvLocationId;
        public LinearLayout linMainCrate, llLocation;
        public ImageView imgBox;

        public ViewHolder(View itemView) {
            super(itemView);
            this.llLocation = itemView.findViewById(R.id.llLocation);
            this.tvLocation = itemView.findViewById(R.id.txtLocation);
            this.tvLocationId = itemView.findViewById(R.id.txtLocationId);
            this.txtCrateId = itemView.findViewById(R.id.txtCrateId);
            this.txtNoOfPcs = itemView.findViewById(R.id.txtNoOfPcs);
            this.txtStatus = itemView.findViewById(R.id.txtStatus);
            this.linMainCrate = itemView.findViewById(R.id.linMainCrate);
            this.imgBox = itemView.findViewById(R.id.imgBox);
            this.txtCrate = itemView.findViewById(R.id.txtCrate);
            this.linMainCrate.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) clickListener.onClickCrate(v, getAdapterPosition());
        }
    }

    public void updateUI(int position) {
        this.selectedPosition = position;
        notifyDataSetChanged();
    }
}
