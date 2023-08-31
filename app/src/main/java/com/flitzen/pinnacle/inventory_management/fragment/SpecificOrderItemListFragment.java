package com.flitzen.pinnacle.inventory_management.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flitzen.pinnacle.R;
import com.flitzen.pinnacle.activity.DrawerActivity;
import com.flitzen.pinnacle.inventory_management.adapter.SpecificOrderItemAdapter;
import com.flitzen.pinnacle.inventory_management.model.PendingOrderListModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SpecificOrderItemListFragment extends Fragment implements SpecificOrderItemAdapter.ItemClickListener, View.OnClickListener {

    View view;
    private RecyclerView recyclerOrderItem;
    private SpecificOrderItemAdapter specificOrderItemAdapter;
    private RelativeLayout relBack, noDataLayout;
    Activity activity;
    public ProgressDialog prd;
    private PendingOrderListModel.Result result;
    private TextView txtDate, txtTime, txtCustomerName,txtOrderNo;
    private String TAG = "SpecificOrderItemListFragment";

    public SpecificOrderItemListFragment(PendingOrderListModel.Result result) {
        this.result = result;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = getActivity();
        DrawerActivity.MAIN_FRAGMENT="SUB_FRAGMENT";
        view = inflater.inflate(R.layout.order_item_list_layout, container, false);
        createDialog1();

        recyclerOrderItem = view.findViewById(R.id.recyclerOrderItem);
        relBack = view.findViewById(R.id.relBack);
        noDataLayout = view.findViewById(R.id.noDataLayout);
        txtDate = view.findViewById(R.id.txtDate);
        txtTime = view.findViewById(R.id.txtTime);
        txtCustomerName = view.findViewById(R.id.txtCustomerName);
        txtOrderNo = view.findViewById(R.id.txtOrderNo);

        relBack.setOnClickListener(this);

        txtOrderNo.setText("Order - "+result.getOrderNo());
        SimpleDateFormat input = new SimpleDateFormat("dd MMMM,yyyy");
        SimpleDateFormat output = new SimpleDateFormat("dd MMM yyyy");
        try {
            Date oneWayTripDate;
            oneWayTripDate = input.parse(result.getOrderDate());
            txtDate.setText(output.format(oneWayTripDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        txtTime.setText(", "+result.getOrderTime());
        txtCustomerName.setText(result.getCustomerName());

        specificOrderItemAdapter = new SpecificOrderItemAdapter(activity,result.getOrderItem());
        recyclerOrderItem.setHasFixedSize(true);
        recyclerOrderItem.setLayoutManager(new LinearLayoutManager(activity));
        recyclerOrderItem.setAdapter(specificOrderItemAdapter);
        specificOrderItemAdapter.setClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view, int position) {
        if (view.getId() == R.id.btnShortage) {
            DrawerActivity.backPressStatus = 2;
            loadFragment(new ShortageFragment(result.getOrderId(),result.getOrderItem().get(position).getMachineId()));
        } else if (view.getId() == R.id.txtBom) {
            DrawerActivity.backPressStatus = 2;
            loadFragment(new BOMFragment(result.getOrderId(),result.getOrderItem().get(position).getMachineId()));
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_content, fragment);
        fragmentTransaction.addToBackStack("Shortage");
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.relBack:
                getFragmentManager().popBackStack();
                break;
        }
    }

    private void createDialog1() {
        prd = new ProgressDialog(activity, R.style.MyAlertDialogStyle);
        prd.setMessage("Please wait...");
        prd.setCancelable(false);
    }

    public void showDialog() {
        if(prd!=null){
            if (prd.isShowing() == false) {
                prd.show();
            }
        }
    }

    public void hideDialog() {
        if(prd!=null){
            if(prd.isShowing()){
                prd.dismiss();
            }
        }
    }
}
