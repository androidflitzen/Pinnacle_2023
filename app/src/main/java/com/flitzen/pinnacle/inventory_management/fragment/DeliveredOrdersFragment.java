package com.flitzen.pinnacle.inventory_management.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.flitzen.pinnacle.MyApplicationClass;
import com.flitzen.pinnacle.R;
import com.flitzen.pinnacle.activity.DrawerActivity;
import com.flitzen.pinnacle.inventory_management.adapter.DeliveredOrderAdapter;
import com.flitzen.pinnacle.inventory_management.model.DeliveredOrderListModel;
import com.flitzen.pinnacle.utils.ApiUrlList;
import com.flitzen.pinnacle.utils.AppUtil;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeliveredOrdersFragment extends Fragment implements View.OnClickListener {

    View view;
    private RelativeLayout relBack,noDataLayout;
    Activity activity;
    Intent gcm_rec;
    public ProgressDialog prd;
    private RecyclerView recyclerDeliveredOrder;
    private DeliveredOrderAdapter deliveredOrderAdapter;
    private ApiUrlList apiService;
    private String TAG = "DeliveredOrdersFragment";
    private ArrayList<DeliveredOrderListModel.Result> deliveredOrderListArr = new ArrayList<>();
    private SwipeRefreshLayout swipe;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity=getActivity();
        DrawerActivity.MAIN_FRAGMENT="MAIN_FRAGMENT";
        view = inflater.inflate(R.layout.delivered_order_layout, container,false);
        createDialog1();
        recyclerDeliveredOrder=view.findViewById(R.id.recyclerDeliveredOrder);
        relBack=view.findViewById(R.id.relBack);
        noDataLayout=view.findViewById(R.id.noDataLayout);
        swipe = view.findViewById(R.id.swipe);

        relBack.setOnClickListener(this);

        deliveredOrderAdapter = new DeliveredOrderAdapter(activity,deliveredOrderListArr);
        recyclerDeliveredOrder.setHasFixedSize(true);
        recyclerDeliveredOrder.setItemViewCacheSize(100);
        recyclerDeliveredOrder.setLayoutManager(new LinearLayoutManager(activity));
        recyclerDeliveredOrder.setAdapter(deliveredOrderAdapter);

        apiService =
                MyApplicationClass.getClient().create(ApiUrlList.class);

        if (AppUtil.isOnline(activity)) {
            showDialog();
            getDeliveredOrderListAPI();
        } else {
            AppUtil.showToastFail(activity, getResources().getString(R.string.no_internet));
        }

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (AppUtil.isOnline(activity)) {
                    getDeliveredOrderListAPI();
                } else {
                    AppUtil.showToastFail(activity, getResources().getString(R.string.no_internet));
                }
            }
        });

        return view;
    }

    private void getDeliveredOrderListAPI() {
        try {
            Call<DeliveredOrderListModel> call = apiService.deliveredOrderListApi(getResources().getString(R.string.api_key));
            call.enqueue(new Callback<DeliveredOrderListModel>() {
                @Override
                public void onResponse(Call<DeliveredOrderListModel> call, Response<DeliveredOrderListModel> response) {
                    hideDialog();
                    swipe.setRefreshing(false);
                    if (response.isSuccessful()) {
                        Log.d(TAG, "delivered order list Response success " + new Gson().toJson(response));
                        if (response.body().getStatus() == 1) {
                            if(response.body().getData().size()>0){
                                recyclerDeliveredOrder.setVisibility(View.VISIBLE);
                                noDataLayout.setVisibility(View.GONE);
                                deliveredOrderListArr.clear();
                                for (int i = 0; i < response.body().getData().size(); i++) {
                                    deliveredOrderListArr.add(response.body().getData().get(i));
                                }
                                // For animation
                                int resId = R.anim.layout_animation_fall_down;
                                LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(activity, resId);
                                recyclerDeliveredOrder.setLayoutAnimation(animation);

                                deliveredOrderAdapter.notifyDataSetChanged();
                            }else {
                                recyclerDeliveredOrder.setVisibility(View.GONE);
                                noDataLayout.setVisibility(View.VISIBLE);
                            }
                        } else {
                            recyclerDeliveredOrder.setVisibility(View.GONE);
                            noDataLayout.setVisibility(View.VISIBLE);
                            AppUtil.showToastFail(activity, response.body().getMessage());
                        }
                    } else {
                        Log.d(TAG, "delivered order list Response fail " + new Gson().toJson(response));
                        AppUtil.showToastFail(activity, getResources().getString(R.string.something_wrong));
                    }
                }

                @Override
                public void onFailure(Call<DeliveredOrderListModel> call, Throwable t) {
                    hideDialog();
                    swipe.setRefreshing(false);
                    Log.d(TAG, "delivered order list Request fail " + t.getMessage());
                    AppUtil.showToastFail(activity, getResources().getString(R.string.something_wrong));
                }
            });
        } catch (Exception e) {
            hideDialog();
            swipe.setRefreshing(false);
            Log.d(TAG, "Exception delivered order list " + e.getMessage());
            AppUtil.showToastFail(activity, activity.getResources().getString(R.string.something_wrong));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        this.gcm_rec = new Intent(getResources().getString(R.string.delivered_orders));
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
