package com.flitzen.pinnacle.inventory_management.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flitzen.pinnacle.R;

public class JobWorkAdapter extends RecyclerView.Adapter<JobWorkAdapter.ViewHolder>{

    JobWorkOrderItemAdapter orderItemAdapter;
    KitAdapter kitAdapter;
    private Context context;
    private ItemClickListener clickListener;

    public JobWorkAdapter(Context context) {
        this.context=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.custom_pending_job_work_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
            orderItemAdapter = new JobWorkOrderItemAdapter(position);
            holder.recyclerItem.setHasFixedSize(true);
            holder.recyclerItem.setLayoutManager(new LinearLayoutManager(context));
            holder.recyclerItem.setAdapter(orderItemAdapter);

    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onClick(View view, int position);
    }


    @Override
    public int getItemCount() {
        return 10;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView txtDate,txtTime,txtVendorName,txtOrderNo,txtVendorAddress;
        public RecyclerView recyclerItem,recyclerKit;
        public LinearLayout linKit,linMain;
        public ViewHolder(View itemView) {
            super(itemView);
            this.txtDate =  itemView.findViewById(R.id.txtDate);
            this.txtTime =  itemView.findViewById(R.id.txtTime);
            this.txtVendorName =  itemView.findViewById(R.id.txtVendorName);
            this.txtVendorAddress =  itemView.findViewById(R.id.txtVendorAddress);
            this.txtOrderNo =  itemView.findViewById(R.id.txtOrderNo);
            this.recyclerItem =  itemView.findViewById(R.id.recyclerItem);
            this.linKit =  itemView.findViewById(R.id.linKit);
            this.linMain =  itemView.findViewById(R.id.linMain);
            this.recyclerKit =  itemView.findViewById(R.id.recyclerKit);
            linMain.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onClick(view, getAdapterPosition());
        }
    }
}
