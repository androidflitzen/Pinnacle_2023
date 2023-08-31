package com.flitzen.pinnacle.inventory_management.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flitzen.pinnacle.R;
import com.flitzen.pinnacle.inventory_management.adapter.LocationAdapter;
import com.flitzen.pinnacle.inventory_management.adapter.StockAlertAdapter;
import com.flitzen.pinnacle.inventory_management.model.StockAlertListModel;

import java.util.ArrayList;

public class ZeroStockAlertFragment extends Fragment {

    View view;
    RecyclerView recyclerStockAlerts;
    StockAlertAdapter stockAlertAdapter;
    private RelativeLayout noDataLayout;
    private ArrayList<StockAlertListModel.Data> arrayListZero;

    public ZeroStockAlertFragment(ArrayList<StockAlertListModel.Data> arrayListZero) {
        this.arrayListZero = arrayListZero;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.zero_stock_alert_layout, container, false);
        recyclerStockAlerts = view.findViewById(R.id.recyclerStockAlerts);
        noDataLayout = view.findViewById(R.id.noDataLayout);

        stockAlertAdapter = new StockAlertAdapter(getActivity(),arrayListZero);
        recyclerStockAlerts.setHasFixedSize(true);
        recyclerStockAlerts.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerStockAlerts.setAdapter(stockAlertAdapter);

        if(arrayListZero.size()>0){
            recyclerStockAlerts.setVisibility(View.VISIBLE);
            noDataLayout.setVisibility(View.GONE);
        }else {
            recyclerStockAlerts.setVisibility(View.GONE);
            noDataLayout.setVisibility(View.VISIBLE);
        }

        return view;
    }
}
