package com.flitzen.pinnacle.inventory_management.adapter;

import android.content.Context;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.flitzen.pinnacle.R;
import com.flitzen.pinnacle.inventory_management.model.PendingOrderBOMListModel;
import com.flitzen.pinnacle.utils.AppUtil;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;

public class BOMListAdapter extends RecyclerView.Adapter<BOMListAdapter.ViewHolder> {

    private Context context;
    private ItemClickListener clickListener;
    private ArrayList<PendingOrderBOMListModel.Data> pendingOrderBOMListArr;
    int selectedItemPosition;
    DecimalFormat format = new DecimalFormat();

    public BOMListAdapter(Context context, ArrayList<PendingOrderBOMListModel.Data> pendingOrderBOMListArr) {
        this.context = context;
        this.pendingOrderBOMListArr = pendingOrderBOMListArr;
        format.setMaximumFractionDigits(2);
        //format.setDecimalSeparatorAlwaysShown(false);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.custom_bom_list_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            PendingOrderBOMListModel.BOMDetails bomDetails = pendingOrderBOMListArr.get(selectedItemPosition).getData().get(position);
            holder.txtPartCode.setText(bomDetails.getBomCode());
            holder.txtPartName.setText(bomDetails.getBomName());

            if(bomDetails.getBomRequireQty().toString().trim().equalsIgnoreCase("0")){
                holder.txtRequiredQty.setText("-");
            }else {
                holder.txtRequiredQty.setText(bomDetails.getBomRequireQty());
            }

            if(bomDetails.getBomDeliverQty().toString().trim().equalsIgnoreCase("0")){
                holder.txtDeliveredQty.setText("-");
            }else {
                holder.txtDeliveredQty.setText(bomDetails.getBomDeliverQty());
            }


            if (bomDetails.getBomRequireQty() != null && bomDetails.getBomDeliverQty() != null) {
                String balancedQty = String.valueOf(Float.parseFloat(bomDetails.getBomRequireQty()) - Float.parseFloat(bomDetails.getBomDeliverQty()));

                if(balancedQty.equalsIgnoreCase("0")){
                    holder.txtBalancedQty.setText("-");
                }else {
                    holder.txtBalancedQty.setText(format.format(Double.parseDouble(balancedQty)));
                }

                if (bomDetails.getBomRequireQty().equalsIgnoreCase(bomDetails.getBomDeliverQty())) {
                    holder.btnAdd.setVisibility(View.GONE);
                } else {
                    holder.btnAdd.setVisibility(View.VISIBLE);
                }

            }

            if(pendingOrderBOMListArr.get(selectedItemPosition).getData().get(position).getBomStockQty().trim().equalsIgnoreCase("0")){
                holder.txtInStockQty.setText("-");
            }else {
                holder.txtInStockQty.setText(pendingOrderBOMListArr.get(selectedItemPosition).getData().get(position).getBomStockQty());
            }

        }catch (Exception e){
            AppUtil.showToastFail(context, context.getString(R.string.something_wrong));
        }
    }

    @Override
    public int getItemCount() {
        if (pendingOrderBOMListArr.size() > 0) {
            return pendingOrderBOMListArr.get(selectedItemPosition).getData().size();
        } else {
            return 0;
        }
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onClick(View view, int position, int selectedItemPosition);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txtPartCode, txtInStockQty, txtRequiredQty, txtPartName, txtDeliveredQty, txtBalancedQty;
        public AppCompatButton btnAdd;

        public ViewHolder(View itemView) {
            super(itemView);
            this.txtPartCode = itemView.findViewById(R.id.txtPartCode);
            this.txtPartName = itemView.findViewById(R.id.txtPartName);
            this.txtInStockQty = itemView.findViewById(R.id.txtInStockQty);
            this.txtRequiredQty = itemView.findViewById(R.id.txtRequiredQty);
            this.txtDeliveredQty = itemView.findViewById(R.id.txtDeliveredQty);
            this.txtBalancedQty = itemView.findViewById(R.id.txtBalancedQty);
            this.btnAdd = itemView.findViewById(R.id.btnAdd);
            this.btnAdd.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null)
                clickListener.onClick(view, getAdapterPosition(), selectedItemPosition);
        }
    }

    public void updateUI(int selectedItemPosition) {
        this.selectedItemPosition = selectedItemPosition;
        notifyDataSetChanged();
    }

}
