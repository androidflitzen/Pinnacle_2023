package com.flitzen.pinnacle.inventory_management.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.flitzen.pinnacle.R;
import com.flitzen.pinnacle.inventory_management.model.CraeteItemModel;
import com.flitzen.pinnacle.inventory_management.model.CrateItemListModel;
import com.flitzen.pinnacle.utils.AppUtil;
import com.flitzen.pinnacle.utils.InputFilterMinMax;

import java.util.ArrayList;

public class NewInternalMovementAdapter extends RecyclerView.Adapter<NewInternalMovementAdapter.ViewHolder> {

    private Context context;
    private ArrayList<CrateItemListModel.Data> arrayListFrom = new ArrayList<>();
    private ArrayList<CraeteItemModel> arrayListFromStore = new ArrayList<>();

    public NewInternalMovementAdapter(Context context, ArrayList<CrateItemListModel.Data> arrayListFrom, ArrayList<CraeteItemModel> arrayListFromStore) {
        this.context = context;
        this.arrayListFrom = arrayListFrom;
        this.arrayListFromStore = arrayListFromStore;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.custom_new_internal_movement_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.txtItemName.setText(arrayListFrom.get(position).getItemName());
        holder.txtItemQTY.setText(String.valueOf(arrayListFrom.get(position).getStockQty()));
        holder.editItemQty.setFilters(new InputFilter[]{ new InputFilterMinMax("0", String.valueOf(arrayListFrom.get(position).getStockQty()))});

        holder.imgPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arrayListFromStore.get(position).getQty().isEmpty()) {
                    if (Float.parseFloat(String.valueOf(arrayListFrom.get(position).getStockQty())) > Float.parseFloat(arrayListFromStore.get(position).getQty())) {
                        arrayListFromStore.get(position).setQty("1");
                    } else {
                        AppUtil.showToastFail(context, "You can not select grater than Stock quantity");
                    }
                } else {
                    if (Float.parseFloat(String.valueOf(arrayListFrom.get(position).getStockQty())) > Float.parseFloat(arrayListFromStore.get(position).getQty())) {
                        if (Float.parseFloat(arrayListFromStore.get(position).getQty()) > -1) {
                            float value1 = Float.parseFloat(arrayListFromStore.get(position).getQty()) + 1;
                            arrayListFromStore.get(position).setQty(String.valueOf(value1));
                        } else {
                            AppUtil.showToastFail(context, "You can not select grater than Stock quantity");
                        }
                    }
                    else {
                        AppUtil.showToastFail(context, "You can not select grater than Stock quantity");
                    }
                }
                holder.editItemQty.setText(arrayListFromStore.get(position).getQty());
            }
        });

        holder.imgMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arrayListFromStore.get(position).getQty().isEmpty()) {

                } else if (Float.parseFloat(arrayListFromStore.get(position).getQty()) > 0) {
                    float value1 = Float.parseFloat(arrayListFromStore.get(position).getQty()) - 1;
                    arrayListFromStore.get(position).setQty(String.valueOf(value1));
                }
                holder.editItemQty.setText(arrayListFromStore.get(position).getQty());
            }
        });

        holder.editItemQty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty()) {
                    arrayListFromStore.get(position).setQty("0");
                } else {
                    arrayListFromStore.get(position).setQty(s.toString());
                    /*if (Float.parseFloat(String.valueOf(arrayListFrom.get(position).getStockQty())) > Float.parseFloat(s.toString())) {
                        arrayListFromStore.get(position).setQty(s.toString());
                    } else {
                        AppUtil.showToastFail(context, "You can not select grater than Stock quantity");
                        arrayListFromStore.get(position).setQty(String.valueOf(arrayListFrom.get(position).getStockQty()));
                    }*/
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayListFrom.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtItemName, txtItemQTY;
        public ImageView imgPlus, imgMinus;
        public EditText editItemQty;

        public ViewHolder(View itemView) {
            super(itemView);
            this.txtItemName = itemView.findViewById(R.id.txtItemName);
            this.editItemQty = itemView.findViewById(R.id.editItemQty);
            this.imgPlus = itemView.findViewById(R.id.imgPlus);
            this.imgMinus = itemView.findViewById(R.id.imgMinus);
            this.txtItemQTY = itemView.findViewById(R.id.txtItemQTY);
        }
    }
}
