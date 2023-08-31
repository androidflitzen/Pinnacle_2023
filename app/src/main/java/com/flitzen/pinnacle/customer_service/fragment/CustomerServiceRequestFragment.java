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
import com.flitzen.pinnacle.customer_service.adapter.ServiceRequestAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomerServiceRequestFragment extends Fragment {

    @BindView(R.id.recyclerServiceRequestList)
    RecyclerView recyclerServiceRequestList;

    private View view;
    Activity activity;
    private ServiceRequestAdapter serviceRequestAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = getActivity();
        view = inflater.inflate(R.layout.service_request_item_list_layout, container, false);
        ButterKnife.bind(this,view);

        serviceRequestAdapter = new ServiceRequestAdapter(activity);
        recyclerServiceRequestList.setHasFixedSize(true);
        recyclerServiceRequestList.setLayoutManager(new LinearLayoutManager(activity));
        recyclerServiceRequestList.setAdapter(serviceRequestAdapter);

        return view;
    }

}
