package com.flitzen.pinnacle.inventory_management.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flitzen.pinnacle.R;
import com.flitzen.pinnacle.activity.DrawerActivity;
import com.flitzen.pinnacle.inventory_management.adapter.SpecificJobWorkAdapter;


public class SpecificJobWorkListFragment extends Fragment implements View.OnClickListener {

    View view;
    private RecyclerView recyclerJobWorkList;
    private SpecificJobWorkAdapter specificJobWorkAdapter;
    Activity activity;
    private RelativeLayout relBack,noDataLayout;
    public ProgressDialog prd;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity=getActivity();
        DrawerActivity.MAIN_FRAGMENT="SUB_FRAGMENT";
        view = inflater.inflate(R.layout.job_work_item_list_layout, container,false);
        createDialog1();

        recyclerJobWorkList=view.findViewById(R.id.recyclerJobWorkList);
        relBack=view.findViewById(R.id.relBack);
        noDataLayout=view.findViewById(R.id.noDataLayout);

        relBack.setOnClickListener(this);

        specificJobWorkAdapter = new SpecificJobWorkAdapter(activity);
        recyclerJobWorkList.setHasFixedSize(true);
        recyclerJobWorkList.setLayoutManager(new LinearLayoutManager(activity));
        recyclerJobWorkList.setAdapter(specificJobWorkAdapter);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
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
