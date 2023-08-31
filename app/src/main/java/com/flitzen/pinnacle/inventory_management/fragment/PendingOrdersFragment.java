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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.flitzen.pinnacle.MyApplicationClass;
import com.flitzen.pinnacle.R;
import com.flitzen.pinnacle.activity.DrawerActivity;
import com.flitzen.pinnacle.inventory_management.adapter.PendingOrderAdapter;
import com.flitzen.pinnacle.inventory_management.model.PendingOrderListModel;
import com.flitzen.pinnacle.utils.ApiUrlList;
import com.flitzen.pinnacle.utils.AppUtil;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PendingOrdersFragment extends Fragment implements PendingOrderAdapter.ItemClickListener, View.OnClickListener {

    View view;
    private RecyclerView recyclerPendingOrder;
    private PendingOrderAdapter pendingOrderAdapter;
    private RelativeLayout relBack, noDataLayout;
    Activity activity;
    Intent gcm_rec;
    public ProgressDialog prd;
    private ApiUrlList apiService;
    private String TAG = "PendingOrdersFragment";
    private ArrayList<PendingOrderListModel.Result> pendingOrderListArr = new ArrayList<>();
    private SwipeRefreshLayout swipe;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = getActivity();
        DrawerActivity.MAIN_FRAGMENT="MAIN_FRAGMENT";
        view = inflater.inflate(R.layout.pending_order_layout, container, false);
        createDialog1();
        recyclerPendingOrder = view.findViewById(R.id.recyclerPendingOrder);
        relBack = view.findViewById(R.id.relBack);
        noDataLayout = view.findViewById(R.id.noDataLayout);
        swipe = view.findViewById(R.id.swipe);

        relBack.setOnClickListener(this);
        pendingOrderAdapter = new PendingOrderAdapter(activity,pendingOrderListArr);
        recyclerPendingOrder.setHasFixedSize(true);
        recyclerPendingOrder.setLayoutManager(new LinearLayoutManager(activity));
        recyclerPendingOrder.setAdapter(pendingOrderAdapter);
        pendingOrderAdapter.setClickListener(this);

        apiService =
                MyApplicationClass.getClient().create(ApiUrlList.class);

        if (AppUtil.isOnline(activity)) {
            showDialog();
            getPendingOrderListAPI();
        } else {
            AppUtil.showToastFail(activity, getResources().getString(R.string.no_internet));
        }

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (AppUtil.isOnline(activity)) {
                    getPendingOrderListAPI();
                } else {
                    AppUtil.showToastFail(activity, getResources().getString(R.string.no_internet));
                }
            }
        });

        return view;
    }

    private void getPendingOrderListAPI() {
        try {
            Call<PendingOrderListModel> call = apiService.pendingOrderListApi(getResources().getString(R.string.api_key));
            call.enqueue(new Callback<PendingOrderListModel>() {
                @Override
                public void onResponse(Call<PendingOrderListModel> call, Response<PendingOrderListModel> response) {
                    hideDialog();
                    swipe.setRefreshing(false);
                    if (response.isSuccessful()) {
                        Log.d(TAG, "pending order list Response success " + new Gson().toJson(response));
                        if (response.body().getStatus() == 1) {
                            if(response.body().getData().size()>0){
                                recyclerPendingOrder.setVisibility(View.VISIBLE);
                                noDataLayout.setVisibility(View.GONE);
                                pendingOrderListArr.clear();
                                for (int i = 0; i < response.body().getData().size(); i++) {
                                    pendingOrderListArr.add(response.body().getData().get(i));
                                }
                                // For animation
                                int resId = R.anim.layout_animation_fall_down;
                                LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(activity, resId);
                                recyclerPendingOrder.setLayoutAnimation(animation);

                                pendingOrderAdapter.notifyDataSetChanged();
                            }else {
                                recyclerPendingOrder.setVisibility(View.GONE);
                                noDataLayout.setVisibility(View.VISIBLE);
                            }
                        } else {
                            recyclerPendingOrder.setVisibility(View.GONE);
                            noDataLayout.setVisibility(View.VISIBLE);
                            AppUtil.showToastFail(activity, response.body().getMessage());
                        }
                    } else {
                        Log.d(TAG, "pending order list Response fail " + new Gson().toJson(response));
                        AppUtil.showToastFail(activity, getResources().getString(R.string.something_wrong));
                    }
                }

                @Override
                public void onFailure(Call<PendingOrderListModel> call, Throwable t) {
                    hideDialog();
                    swipe.setRefreshing(false);
                    Log.d(TAG, "pending order list Request fail " + t.getMessage());
                    AppUtil.showToastFail(activity, getResources().getString(R.string.something_wrong));
                }
            });
        } catch (Exception e) {
            hideDialog();
            swipe.setRefreshing(false);
            Log.d(TAG, "Exception pending order list " + e.getMessage());
            AppUtil.showToastFail(activity, activity.getResources().getString(R.string.something_wrong));
        }
    }

    @Override
    public void onClick(View view, int position) {
        DrawerActivity.backPressStatus = 2;
        loadFragment(new SpecificOrderItemListFragment(pendingOrderListArr.get(position)));
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_content, fragment);
        fragmentTransaction.addToBackStack("subOrder");
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

    @Override
    public void onResume() {
        super.onResume();
        this.gcm_rec = new Intent(getResources().getString(R.string.pending_orders));
        LocalBroadcastManager.getInstance(activity).sendBroadcast(this.gcm_rec);
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
