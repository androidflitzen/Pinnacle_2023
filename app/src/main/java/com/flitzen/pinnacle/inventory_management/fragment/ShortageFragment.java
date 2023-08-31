package com.flitzen.pinnacle.inventory_management.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flitzen.pinnacle.MyApplicationClass;
import com.flitzen.pinnacle.R;
import com.flitzen.pinnacle.activity.DrawerActivity;
import com.flitzen.pinnacle.inventory_management.adapter.ItemTypeAdapter;
import com.flitzen.pinnacle.inventory_management.adapter.ShortageAdapter;
import com.flitzen.pinnacle.inventory_management.model.ItemTypeModel;
import com.flitzen.pinnacle.inventory_management.model.PendingOrderBOMListModel;
import com.flitzen.pinnacle.utils.ApiUrlList;
import com.flitzen.pinnacle.utils.AppUtil;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShortageFragment extends Fragment implements View.OnClickListener, ItemTypeAdapter.ItemClickListenerItem {

    View view;
    private RecyclerView recyclerItemType, recyclerShortageList;
    private ShortageAdapter shortageAdapter;
    private ItemTypeAdapter itemTypeAdapter;
    private RelativeLayout relBack, noDataLayout;
    Activity activity;
    public ProgressDialog prd;
    private ArrayList<ItemTypeModel> itemTypeNameArray = new ArrayList<>();
    private ApiUrlList apiService;
    private String TAG = "ShortageFragment";
    private String orderId, itemId;
    private ArrayList<PendingOrderBOMListModel.Data> pendingOrderSHORTAGEListArr = new ArrayList<>();
    private TextView txtDate, txtTime, txtDashboardTitle, txtItemName;

    public ShortageFragment(String orderId, String itemId) {
        this.orderId = orderId;
        this.itemId = itemId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = getActivity();
        DrawerActivity.MAIN_FRAGMENT="SUB_FRAGMENT";
        view = inflater.inflate(R.layout.order_item_shortage_list_layout, container, false);
        createDialog1();

        recyclerItemType = view.findViewById(R.id.recyclerItemType);
        recyclerShortageList = view.findViewById(R.id.recyclerShortageList);
        relBack = view.findViewById(R.id.relBack);
        noDataLayout = view.findViewById(R.id.noDataLayout);
        txtDate = view.findViewById(R.id.txtDate);
        txtTime = view.findViewById(R.id.txtTime);
        txtDashboardTitle = view.findViewById(R.id.txtDashboardTitle);
        txtItemName = view.findViewById(R.id.txtItemName);

        relBack.setOnClickListener(this);

        shortageAdapter = new ShortageAdapter(activity,pendingOrderSHORTAGEListArr);
        recyclerShortageList.setHasFixedSize(true);
        recyclerShortageList.setLayoutManager(new LinearLayoutManager(activity));
        recyclerShortageList.setAdapter(shortageAdapter);

        itemTypeAdapter=new ItemTypeAdapter(activity,itemTypeNameArray);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        recyclerItemType.setLayoutManager(layoutManager);
        recyclerItemType.setAdapter(itemTypeAdapter);
        recyclerItemType.setItemViewCacheSize(100);
        itemTypeAdapter.setClickListenerItem(this);

        apiService =
                MyApplicationClass.getClient().create(ApiUrlList.class);

        if (AppUtil.isOnline(activity)) {
            getPendingOrderBOMListAPI();
        } else {
            AppUtil.showToastFail(activity, getResources().getString(R.string.no_internet));
        }

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.relBack:
                getFragmentManager().popBackStack();
                break;
        }
    }

    private void getPendingOrderBOMListAPI() {
        try {
            showDialog();
            Call<PendingOrderBOMListModel> call = apiService.getShortageBOMList(getResources().getString(R.string.api_key), orderId, itemId);
            Log.e(TAG, "pending order SHORTAGE list request URL " + call.request().url());
            call.enqueue(new Callback<PendingOrderBOMListModel>() {
                @Override
                public void onResponse(Call<PendingOrderBOMListModel> call, Response<PendingOrderBOMListModel> response) {
                    hideDialog();
                    if (response.isSuccessful()) {
                        Log.d(TAG, "pending order SHORTAGE list Response success " + new Gson().toJson(response));
                        if (response.body().getStatus() == 1) {

                            recyclerShortageList.setVisibility(View.VISIBLE);
                            noDataLayout.setVisibility(View.GONE);
                            pendingOrderSHORTAGEListArr.clear();
                            itemTypeNameArray.clear();

                            for (int i = 0; i < response.body().getData().size(); i++) {
                                if (response.body().getData().get(i).getData() != null) {
                                    itemTypeNameArray.add(new ItemTypeModel(response.body().getData().get(i).getCategory(), response.body().getData().get(i).getData().size()));
                                    pendingOrderSHORTAGEListArr.add(response.body().getData().get(i));

                                } else {
                                    itemTypeNameArray.add(new ItemTypeModel(response.body().getData().get(i).getCategory(), 0));
                                }
                            }

                            // For animation
                            int resId = R.anim.layout_animation_fall_down;
                            LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(activity, resId);
                            recyclerItemType.setLayoutAnimation(animation);

                            shortageAdapter.notifyDataSetChanged();
                            itemTypeAdapter.notifyDataSetChanged();

                            SimpleDateFormat input = new SimpleDateFormat("dd MMMM,yyyy");
                            SimpleDateFormat output = new SimpleDateFormat("dd MMM yyyy");
                            try {
                                Date oneWayTripDate;
                                oneWayTripDate = input.parse(response.body().getOrderDate());
                                txtDate.setText(output.format(oneWayTripDate));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            txtDashboardTitle.setText("Order - " + response.body().getOrderNo());
                            txtTime.setText(", " + response.body().getOrderTime());
                            txtItemName.setText(response.body().getItemName());

                        } else {
                            recyclerShortageList.setVisibility(View.GONE);
                            noDataLayout.setVisibility(View.VISIBLE);
                            AppUtil.showToastFail(activity, response.body().getMessage());
                        }
                    } else {
                        Log.d(TAG, "pending order SHORTAGE list Response fail " + new Gson().toJson(response));
                        AppUtil.showToastFail(activity, getResources().getString(R.string.something_wrong));
                    }
                }

                @Override
                public void onFailure(Call<PendingOrderBOMListModel> call, Throwable t) {
                    hideDialog();
                    Log.d(TAG, "pending order SHORTAGE list Request fail " + t.getMessage());
                    AppUtil.showToastFail(activity, getResources().getString(R.string.something_wrong));
                }
            });
        } catch (Exception e) {
            hideDialog();
            Log.d(TAG, "Exception pending order SHORTAGE list " + e.getMessage());
            AppUtil.showToastFail(activity, activity.getResources().getString(R.string.something_wrong));
        }
    }

    @Override
    public void onClickItem(View view, int position) {
        view.setBackgroundColor(getResources().getColor(R.color.login_bg_color));
        itemTypeAdapter.updateUI(position);
        shortageAdapter.updateUI(position);
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
