package com.flitzen.pinnacle.inventory_management.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flitzen.pinnacle.R;
import com.flitzen.pinnacle.inventory_management.model.PendingOrderListModel;
import com.flitzen.pinnacle.utils.AppUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PendingOrderAdapter extends RecyclerView.Adapter<PendingOrderAdapter.ViewHolder> {

    OrderItemAdapter orderItemAdapter;
    KitAdapter kitAdapter;
    private Context context;
    private ItemClickListener clickListener;
    private ArrayList<PendingOrderListModel.Result> pendingOrderListArr = new ArrayList<>();
    private ArrayList<PendingOrderListModel.OrderItem> orderItemArrayList = new ArrayList<>();
    private String TAG = "PendingOrderAdapter";

    public PendingOrderAdapter(Context context, ArrayList<PendingOrderListModel.Result> pendingOrderListArr) {
        this.context = context;
        this.pendingOrderListArr = pendingOrderListArr;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.custom_pending_order_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            PendingOrderListModel.Result result = pendingOrderListArr.get(position);

            SimpleDateFormat input = new SimpleDateFormat("dd MMMM,yyyy");
            SimpleDateFormat output = new SimpleDateFormat("dd MMM yyyy");
            try {
                Date oneWayTripDate;
                oneWayTripDate = input.parse(result.getOrderDate());
                holder.txtDate.setText(output.format(oneWayTripDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            holder.txtTime.setText(", "+result.getOrderTime());
            //holder.txtOrderNo.setText("Order No\n"+result.getOrderNo());
            holder.txtOrderNo.setText(result.getOrderNo());
            holder.txtCustomerName.setText(result.getCustomerName());
            holder.txtCustomerName.setText(result.getCustomerName());

            if (result.getOrderItem().size() > 0) {
                orderItemAdapter.updateList(result.getOrderItem());
            } else {

            }
            /*orderItemAdapter = new OrderItemAdapter(position);
            holder.recyclerItem.setHasFixedSize(true);
            holder.recyclerItem.setLayoutManager(new LinearLayoutManager(context));
            holder.recyclerItem.setAdapter(orderItemAdapter);*/


            //TODO in future
           /* if (position==9){
                holder.linKit.setVisibility(View.VISIBLE);
                kitAdapter=new KitAdapter();
                LinearLayoutManager layoutManager = new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,true);
                holder.recyclerKit.setLayoutManager(layoutManager);
                holder.recyclerKit.setAdapter(kitAdapter);
            }*/
        }catch (Exception e){
            Log.d(TAG, "Exception pending order list " + e.getMessage());
            AppUtil.showToastFail(context, context.getResources().getString(R.string.something_wrong));
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
        return pendingOrderListArr.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txtDate, txtTime, txtCustomerName, txtOrderNo;
        public RecyclerView recyclerItem, recyclerKit;
        public LinearLayout linKit, linMain;

        public ViewHolder(View itemView) {
            super(itemView);
            this.txtDate = itemView.findViewById(R.id.txtDate);
            this.txtTime = itemView.findViewById(R.id.txtTime);
            this.txtCustomerName = itemView.findViewById(R.id.txtCustomerName);
            this.txtOrderNo = itemView.findViewById(R.id.txtOrderNo);
            this.recyclerItem = itemView.findViewById(R.id.recyclerItem);
            this.linKit = itemView.findViewById(R.id.linKit);
            this.linMain = itemView.findViewById(R.id.linMain);
            this.recyclerKit = itemView.findViewById(R.id.recyclerKit);
            linMain.setOnClickListener(this);

            orderItemAdapter = new OrderItemAdapter(orderItemArrayList,context);
            this.recyclerItem.setHasFixedSize(true);
            this.recyclerItem.setLayoutManager(new LinearLayoutManager(context));
            this.recyclerItem.setAdapter(orderItemAdapter);

        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onClick(view, getAdapterPosition());
        }
    }
}
