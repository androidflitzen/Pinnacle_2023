package com.flitzen.pinnacle.inventory_management.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.flitzen.pinnacle.R;
import com.flitzen.pinnacle.activity.DrawerActivity;

public class PurchaseOrdersFragment extends Fragment implements View.OnClickListener {

    View view;
    private RelativeLayout relBack;
    Activity activity;
    Intent gcm_rec;
    public ProgressDialog prd;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity=getActivity();
        DrawerActivity.MAIN_FRAGMENT="MAIN_FRAGMENT";
        view = inflater.inflate(R.layout.purchase_order_layout, container,false);
        createDialog1();

        relBack=view.findViewById(R.id.relBack);
        relBack.setOnClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        this.gcm_rec = new Intent(getResources().getString(R.string.purchase_orders));
        LocalBroadcastManager.getInstance(activity).sendBroadcast(this.gcm_rec);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
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
        if (prd != null) {
            if (prd.isShowing() == false) {
                prd.show();
            }
        }
    }

    public void hideDialog() {
        if (prd != null) {
            if (prd.isShowing()) {
                prd.dismiss();
            }
        }
    }
}
