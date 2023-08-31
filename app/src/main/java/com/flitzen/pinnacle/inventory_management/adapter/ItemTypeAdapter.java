package com.flitzen.pinnacle.inventory_management.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.flitzen.pinnacle.R;
import com.flitzen.pinnacle.inventory_management.model.ItemTypeModel;
import com.flitzen.pinnacle.utils.AppUtil;

import java.util.ArrayList;

public class ItemTypeAdapter extends RecyclerView.Adapter<ItemTypeAdapter.ViewHolder> {

    private Context context;
    private ItemClickListenerItem clickListener;
    private int selectedPosition;
    private ArrayList<ItemTypeModel> itemTypeNameArray;

    public ItemTypeAdapter(Context context,ArrayList<ItemTypeModel> itemTypeNameArray) {
        this.context = context;
        this.itemTypeNameArray = itemTypeNameArray;
        this.selectedPosition = 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.custom_type_of_item_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            if (selectedPosition == position) {
                holder.mainLine.setBackgroundColor(context.getResources().getColor(R.color.login_bg_color));
            } else {
                holder.mainLine.setBackgroundColor(context.getResources().getColor(R.color.dark_blue_color));
            }

            holder.txtItemTypeName.setText(itemTypeNameArray.get(position).getItemName());
            holder.txtItemTypeCount.setText("("+String.valueOf(itemTypeNameArray.get(position).getItemPartCount())+")");
        }catch (Exception e){
            AppUtil.showToastFail(context, context.getResources().getString(R.string.something_wrong));
        }
    }

    @Override
    public int getItemCount() {
        return itemTypeNameArray.size();
    }

    public void setClickListenerItem(ItemClickListenerItem itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface ItemClickListenerItem {
        void onClickItem(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txtItemTypeName, txtItemTypeCount;
        public LinearLayout mainLine;

        public ViewHolder(View itemView) {
            super(itemView);
            this.txtItemTypeName = itemView.findViewById(R.id.txtItemTypeName);
            this.txtItemTypeCount = itemView.findViewById(R.id.txtItemTypeCount);
            this.mainLine = itemView.findViewById(R.id.mainLine);
            this.mainLine.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onClickItem(view, getAdapterPosition());
        }
    }

    public void updateUI(int position) {
        this.selectedPosition = position;
        notifyDataSetChanged();
    }
}
