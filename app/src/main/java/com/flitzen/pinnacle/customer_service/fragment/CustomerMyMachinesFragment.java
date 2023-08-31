package com.flitzen.pinnacle.customer_service.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flitzen.pinnacle.R;
import com.flitzen.pinnacle.customer_service.adapter.MyMachinesAdapter;
import com.flitzen.pinnacle.customer_service.adapter.ServiceRequestAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomerMyMachinesFragment extends Fragment {

    @BindView(R.id.recyclerMyMachinesList)
    RecyclerView recyclerMyMachinesList;

    private View view;
    Activity activity;
    private MyMachinesAdapter myMachinesAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = getActivity();
        view = inflater.inflate(R.layout.my_machines_list_layout, container, false);
        ButterKnife.bind(this,view);

        myMachinesAdapter = new MyMachinesAdapter(activity);
        recyclerMyMachinesList.setHasFixedSize(true);
        recyclerMyMachinesList.setLayoutManager(new LinearLayoutManager(activity));
        recyclerMyMachinesList.setAdapter(myMachinesAdapter);

        return view;
    }
}
