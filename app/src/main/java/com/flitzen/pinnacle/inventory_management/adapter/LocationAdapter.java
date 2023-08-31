package com.flitzen.pinnacle.inventory_management.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.flitzen.pinnacle.R;
import com.flitzen.pinnacle.inventory_management.model.LocationModel;

import java.util.ArrayList;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder>{

    private Context context;
    private ArrayList<LocationModel.Data> locationList = new ArrayList<>();
    private ItemClickListener clickListener;

    public LocationAdapter(Context context,ArrayList<LocationModel.Data> locationList) {
        this.context=context;
        this.locationList=locationList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.custom_location_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.txtLocationName.setText(locationList.get(position).getLocationName());
        holder.txtCrateCount.setText("Total Item : "+locationList.get(position).getTotalCrates());
    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onClick(View view, int position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public CardView cardLocation;
        public TextView txtLocationName,txtCrateCount;
        public ViewHolder(View itemView) {
            super(itemView);
            this.cardLocation =  itemView.findViewById(R.id.cardLocation);
            this.txtLocationName =  itemView.findViewById(R.id.txtLocationName);
            this.txtCrateCount =  itemView.findViewById(R.id.txtCrateCount);
            this.cardLocation.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onClick(view, getAdapterPosition());
        }
    }
}
