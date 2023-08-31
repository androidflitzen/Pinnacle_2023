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
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flitzen.pinnacle.MyApplicationClass;
import com.flitzen.pinnacle.R;
import com.flitzen.pinnacle.activity.DrawerActivity;
import com.flitzen.pinnacle.inventory_management.adapter.ElectricalAdapter;
import com.flitzen.pinnacle.inventory_management.adapter.LocateItemsPartsAdapter;
import com.flitzen.pinnacle.inventory_management.adapter.LocationWiseStockCheckAdapter;
import com.flitzen.pinnacle.inventory_management.model.CratesListModel;
import com.flitzen.pinnacle.inventory_management.model.StockCheckPartsListModel;
import com.flitzen.pinnacle.utils.ApiUrlList;
import com.flitzen.pinnacle.utils.AppUtil;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StockCheckLocationWiseFragment extends Fragment implements View.OnClickListener {

    View view;
    RecyclerView recyclerElectricalList;
    LocationWiseStockCheckAdapter locationWiseStockCheckAdapter;
    LocateItemsPartsAdapter locateItemsPartsAdapter;
    Activity activity;
    TextView txtLocationName,txtCrateName;
    String crateID;
    private RelativeLayout relBack, noDataLayout;
    private ProgressDialog prd;
    private String TAG = "StockCheckLocationWiseFragment";
    private ApiUrlList apiService;
    private ArrayList<StockCheckPartsListModel.Data> arrayList = new ArrayList<>();
    private ArrayList<CratesListModel.Data> cratesList = new ArrayList<>();
    private AlertDialog alertDialog;
    private String selectedLocationName="",selectedCrateName;

    public StockCheckLocationWiseFragment(String selectedLocationName,String selectedCrateName, int type, String crateID) {
        this.selectedLocationName = selectedLocationName;
        this.selectedCrateName = selectedCrateName;
        this.crateID = crateID;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = getActivity();
        DrawerActivity.MAIN_FRAGMENT = "SUB_FRAGMENT";
        view = inflater.inflate(R.layout.location_wise_stock_check_layout, container, false);
        createDialog1();

        apiService =
                MyApplicationClass.getClient().create(ApiUrlList.class);

        recyclerElectricalList = view.findViewById(R.id.recyclerElectricalList);
        txtLocationName = view.findViewById(R.id.txtLocationName);
        txtCrateName = view.findViewById(R.id.txtCrateName);
        relBack = view.findViewById(R.id.relBack);
        noDataLayout = view.findViewById(R.id.noDataLayout);

        relBack.setOnClickListener(this);

        txtLocationName.setText(selectedLocationName);
        txtCrateName.setText(selectedCrateName);

        locationWiseStockCheckAdapter = new LocationWiseStockCheckAdapter(activity, arrayList,crateID);
        recyclerElectricalList.setHasFixedSize(true);
        recyclerElectricalList.setLayoutManager(new LinearLayoutManager(activity));
        recyclerElectricalList.setAdapter(locationWiseStockCheckAdapter);

        if (AppUtil.isOnline(getActivity())) {
            getCratePartsList();
        } else {
            AppUtil.showToastFail(activity, getResources().getString(R.string.no_internet));
        }

        return view;
    }

    private void getCratePartsList() {
        try {
            showDialog();
            Call<StockCheckPartsListModel> call = apiService.getCrateItemListApi1(getResources().getString(R.string.api_key), crateID);
            Log.e(TAG, "get Products list request URL " + call.request().url());
            call.enqueue(new Callback<StockCheckPartsListModel>() {
                @Override
                public void onResponse(Call<StockCheckPartsListModel> call, Response<StockCheckPartsListModel> response) {
                    hideDialog();
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == 1) {
                            recyclerElectricalList.setVisibility(View.VISIBLE);
                            noDataLayout.setVisibility(View.GONE);
                            arrayList.clear();

                            for (int i = 0; i < response.body().getData().size(); i++) {
                                arrayList.add(response.body().getData().get(i));
                            }

                            // For animation
                            int resId = R.anim.layout_animation_fall_down;
                            LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(activity, resId);
                            recyclerElectricalList.setLayoutAnimation(animation);

                            locationWiseStockCheckAdapter.notifyDataSetChanged();

                        } else {
                            recyclerElectricalList.setVisibility(View.GONE);
                            noDataLayout.setVisibility(View.VISIBLE);
                            AppUtil.showToastFail(activity, response.body().getMessage());
                        }
                    } else {
                        Log.d(TAG, "get Products list Response fail " + new Gson().toJson(response));
                        AppUtil.showToastFail(activity, getResources().getString(R.string.something_wrong));
                    }
                }

                @Override
                public void onFailure(Call<StockCheckPartsListModel> call, Throwable t) {
                    hideDialog();
                    Log.d(TAG, "get Products list Request fail " + t.getMessage());
                    AppUtil.showToastFail(activity, getResources().getString(R.string.something_wrong));
                }
            });

        } catch (Exception e) {
            hideDialog();
            Log.d(TAG, "Exception get Products list " + e.getMessage());
            AppUtil.showToastFail(activity, activity.getResources().getString(R.string.something_wrong));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.relBack:
                getFragmentManager().popBackStack();
                break;
        }
    }

    private void getCrateList(String bomProductId) {
        try {
            showDialog();
            Call<CratesListModel> call = apiService.cratesListApi(getResources().getString(R.string.api_key), bomProductId);
            Log.e(TAG, "Crates list request URL " + call.request().url());
            call.enqueue(new Callback<CratesListModel>() {
                @Override
                public void onResponse(Call<CratesListModel> call, Response<CratesListModel> response) {
                    hideDialog();
                    if (response.isSuccessful()) {
                        Log.d(TAG, "Crates list Response success " + new Gson().toJson(response));
                        if (response.body().getStatus() == 1) {
                            cratesList.clear();
                            for (int i = 0; i < response.body().getData().size(); i++) {
                                cratesList.add(response.body().getData().get(i));
                            }
                            locateItemsPartsAdapter.notifyDataSetChanged();
                            alertDialog.show();

                        } else {

                        }
                    } else {
                        Log.d(TAG, "Crates list Response fail " + new Gson().toJson(response));
                        AppUtil.showToastFail(activity, getResources().getString(R.string.something_wrong));
                    }
                }

                @Override
                public void onFailure(Call<CratesListModel> call, Throwable t) {
                    hideDialog();
                    Log.d(TAG, "Crates list Request fail " + t.getMessage());
                    AppUtil.showToastFail(activity, getResources().getString(R.string.something_wrong));
                }
            });
        } catch (Exception e) {
            hideDialog();
            Log.d(TAG, "Exception crate list " + e.getMessage());
            AppUtil.showToastFail(activity, activity.getResources().getString(R.string.something_wrong));
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
