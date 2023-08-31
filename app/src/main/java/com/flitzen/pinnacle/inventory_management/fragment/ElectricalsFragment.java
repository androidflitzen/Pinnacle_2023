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
import com.flitzen.pinnacle.inventory_management.model.CratesListModel;
import com.flitzen.pinnacle.inventory_management.model.PendingOrderBOMListModel;
import com.flitzen.pinnacle.inventory_management.model.StockCheckPartsListModel;
import com.flitzen.pinnacle.utils.ApiUrlList;
import com.flitzen.pinnacle.utils.AppUtil;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ElectricalsFragment extends Fragment implements View.OnClickListener, ElectricalAdapter.ItemClickListener {

    View view;
    RecyclerView recyclerElectricalList;
    ElectricalAdapter electricalAdapter;
    LocateItemsPartsAdapter locateItemsPartsAdapter;
    Activity activity;
    TextView txtTitle;
    String title,crateID;
    private RelativeLayout relBack, noDataLayout;
    private ProgressDialog prd;
    private String TAG = "ElectricalsFragment";
    private ApiUrlList apiService;
    private int type;
    private ArrayList<StockCheckPartsListModel.Data> arrayList = new ArrayList<>();
    private ArrayList<CratesListModel.Data> cratesList = new ArrayList<>();
    private AlertDialog alertDialog;

    public ElectricalsFragment(String title, int type) {
        this.title = title;
        this.type = type;
    }

    public ElectricalsFragment(String title, int type,String crateID) {
        this.title = title;
        this.type = type;
        this.crateID = crateID;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = getActivity();
        DrawerActivity.MAIN_FRAGMENT = "SUB_FRAGMENT";
        view = inflater.inflate(R.layout.electricals_layout, container, false);
        createDialog1();

        apiService =
                MyApplicationClass.getClient().create(ApiUrlList.class);

        recyclerElectricalList = view.findViewById(R.id.recyclerElectricalList);
        txtTitle = view.findViewById(R.id.txtTitle);
        relBack = view.findViewById(R.id.relBack);
        noDataLayout = view.findViewById(R.id.noDataLayout);

        relBack.setOnClickListener(this);

        txtTitle.setText(title);

        electricalAdapter = new ElectricalAdapter(activity, arrayList);
        recyclerElectricalList.setHasFixedSize(true);
        recyclerElectricalList.setLayoutManager(new LinearLayoutManager(activity));
        recyclerElectricalList.setAdapter(electricalAdapter);
        electricalAdapter.setClickListener(this);

        if (type == 6) {
            if (AppUtil.isOnline(getActivity())) {
                getCratePartsList();
            } else {
                AppUtil.showToastFail(activity, getResources().getString(R.string.no_internet));
            }
        } else {
            if (AppUtil.isOnline(getActivity())) {
                getPartsList();
            } else {
                AppUtil.showToastFail(activity, getResources().getString(R.string.no_internet));
            }
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

                            electricalAdapter.notifyDataSetChanged();

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

    private void getPartsList() {
        try {
            showDialog();
            Call<StockCheckPartsListModel> call = apiService.getPartsListApi(getResources().getString(R.string.api_key), String.valueOf(type));
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

                            electricalAdapter.notifyDataSetChanged();

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

    @Override
    public void onClick(View view, int position) {
        if (view.getId() == R.id.btnLocate) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            LayoutInflater layoutInflaterAndroid = LayoutInflater.from(activity);
            View view2 = layoutInflaterAndroid.inflate(R.layout.locate_items_layout, null);
            builder.setView(view2);
            builder.setCancelable(true);
            alertDialog = builder.create();
            alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            RecyclerView recyclerLocatePart = view2.findViewById(R.id.recyclerLocatePart);
            TextView txtCustomerName = view2.findViewById(R.id.txtCustomerName);
            TextView txtPartCode = view2.findViewById(R.id.txtPartCode);

            txtCustomerName.setText(arrayList.get(position).getItemName());
            txtPartCode.setText(arrayList.get(position).getItemCode());

            locateItemsPartsAdapter = new LocateItemsPartsAdapter(activity, cratesList);
            recyclerLocatePart.setHasFixedSize(true);
            recyclerLocatePart.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
            recyclerLocatePart.setAdapter(locateItemsPartsAdapter);

            if (AppUtil.isOnline(activity)) {
                getCrateList(arrayList.get(position).getItemId());
            } else {
                AppUtil.showToastFail(activity, getResources().getString(R.string.no_internet));
            }

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
