package com.flitzen.pinnacle.inventory_management.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.flitzen.pinnacle.R;

public class SpecificJobWorkAdapter extends RecyclerView.Adapter<SpecificJobWorkAdapter.ViewHolder>{

    private Context context;

    public SpecificJobWorkAdapter(Context context) {
        this.context=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.custom_specific_job_work_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtJobStatus,txtItemName,txtItemQty,txtMarkAsAssigned;
        public RecyclerView recyclerItem,recyclerKit;
        public LinearLayout linShortage,linMain;
        public AppCompatButton btnShortage;
        public ViewHolder(View itemView) {
            super(itemView);
            this.txtJobStatus =  itemView.findViewById(R.id.txtJobStatus);
            this.txtItemName =  itemView.findViewById(R.id.txtItemName);
            this.txtItemQty =  itemView.findViewById(R.id.txtItemQty);
            this.txtMarkAsAssigned =  itemView.findViewById(R.id.txtMarkAsAssigned);
        }
    }
}
