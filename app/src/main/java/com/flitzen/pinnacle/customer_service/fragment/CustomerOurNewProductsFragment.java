package com.flitzen.pinnacle.customer_service.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flitzen.pinnacle.R;
import com.flitzen.pinnacle.customer_service.adapter.NewProductsAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomerOurNewProductsFragment extends Fragment {
    @BindView(R.id.recyclerNewProductsList)
    RecyclerView recyclerNewProductsList;

    private View view;
    Activity activity;
    private NewProductsAdapter newProductsAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = getActivity();
        view = inflater.inflate(R.layout.our_new_products_list_layout, container, false);
        ButterKnife.bind(this,view);

        newProductsAdapter = new NewProductsAdapter(activity);
        recyclerNewProductsList.setHasFixedSize(true);
        recyclerNewProductsList.setLayoutManager(new GridLayoutManager(activity, 2));
        recyclerNewProductsList.setAdapter(newProductsAdapter);

        return view;
    }
}
