package com.flitzen.pinnacle.inventory_management.fragment;

import android.app.Activity;
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
import com.flitzen.pinnacle.inventory_management.adapter.NewInternalMovementAdapter;
import com.flitzen.pinnacle.inventory_management.adapter.NewInternalMovementInAdapter;

public class NewInternalMovementFragment extends Fragment implements View.OnClickListener {

    View view;
    private RecyclerView recyclerItems,recyclerItemsIn;
    private NewInternalMovementAdapter newInternalMovementAdapter;
    private NewInternalMovementInAdapter newInternalMovementInAdapter;
    Activity activity;
    private RelativeLayout relBack;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity=getActivity();
        DrawerActivity.MAIN_FRAGMENT="SUB_FRAGMENT";
        view = inflater.inflate(R.layout.new_internal_movement_layout, container,false);
        recyclerItems=view.findViewById(R.id.recyclerItems);
        recyclerItemsIn=view.findViewById(R.id.recyclerItemsIn);
        relBack=view.findViewById(R.id.relBack);

        relBack.setOnClickListener(this);

      //  newInternalMovementAdapter = new NewInternalMovementAdapter(activity);
        recyclerItems.setHasFixedSize(true);
        recyclerItems.setLayoutManager(new LinearLayoutManager(activity));
        recyclerItems.setAdapter(newInternalMovementAdapter);

      //  newInternalMovementInAdapter = new NewInternalMovementInAdapter(activity);
        recyclerItemsIn.setHasFixedSize(true);
        recyclerItemsIn.setLayoutManager(new LinearLayoutManager(activity));
        recyclerItemsIn.setAdapter(newInternalMovementInAdapter);

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
}
