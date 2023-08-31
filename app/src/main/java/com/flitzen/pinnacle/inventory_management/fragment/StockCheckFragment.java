package com.flitzen.pinnacle.inventory_management.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flitzen.pinnacle.MyApplicationClass;
import com.flitzen.pinnacle.R;
import com.flitzen.pinnacle.activity.DrawerActivity;
import com.flitzen.pinnacle.inventory_management.adapter.CrateAdapter;
import com.flitzen.pinnacle.inventory_management.adapter.LocationAdapter;
import com.flitzen.pinnacle.inventory_management.adapter.SpecificOrderItemAdapter;
import com.flitzen.pinnacle.inventory_management.model.CratesListInternalMovementsModel;
import com.flitzen.pinnacle.inventory_management.model.LocationModel;
import com.flitzen.pinnacle.utils.ApiUrlList;
import com.flitzen.pinnacle.utils.AppUtil;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StockCheckFragment extends Fragment implements View.OnClickListener, LocationAdapter.ItemClickListener, CrateAdapter.ItemClickListener {

    View view;
    CardView cardElectrical, cardMechanicals, cardStickers, cardFasteners;
    private RelativeLayout relBack;
    Intent gcm_rec;
    Activity activity;
    RecyclerView recyclerLocation, recyclerCrate;
    private LocationAdapter locationAdapter;
    private CrateAdapter crateAdapter;
    private ArrayList<LocationModel.Data> locationList = new ArrayList<>();
    private ArrayList<CratesListInternalMovementsModel.Data> cratesList = new ArrayList<>();
    private ProgressDialog prd;
    private String TAG = "StockCheckFragment";
    private ApiUrlList apiService;
    private RelativeLayout noDataLayout;
    private TextView txtLocationName;
    private LinearLayout linPath;
    private String selectedLocationName = "", selectedCrateName = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = getActivity();
        DrawerActivity.MAIN_FRAGMENT = "MAIN_FRAGMENT";

        apiService =
                MyApplicationClass.getClient().create(ApiUrlList.class);
        createDialog1();

        view = inflater.inflate(R.layout.stock_check_layout, container, false);
        cardElectrical = view.findViewById(R.id.cardElectrical);
        cardMechanicals = view.findViewById(R.id.cardMechanicals);
        cardStickers = view.findViewById(R.id.cardStickers);
        cardFasteners = view.findViewById(R.id.cardFasteners);
        relBack = view.findViewById(R.id.relBack);
        recyclerLocation = view.findViewById(R.id.recyclerLocation);
        recyclerCrate = view.findViewById(R.id.recyclerCrate);
        noDataLayout = view.findViewById(R.id.noDataLayout);
        linPath = view.findViewById(R.id.linPath);
        txtLocationName = view.findViewById(R.id.txtLocationName);

        locationAdapter = new LocationAdapter(activity, locationList);
        recyclerLocation.setHasFixedSize(true);
        recyclerLocation.setLayoutManager(new GridLayoutManager(activity, 2));
        recyclerLocation.setAdapter(locationAdapter);
        locationAdapter.setClickListener(this);

        crateAdapter = new CrateAdapter(activity, cratesList);
        recyclerCrate.setHasFixedSize(true);
        recyclerCrate.setLayoutManager(new GridLayoutManager(activity, 3));
        recyclerCrate.setAdapter(crateAdapter);
        crateAdapter.setClickListener(this);

        if (AppUtil.isOnline(getActivity())) {
            getLocationList();
        } else {
            AppUtil.showToastFail(activity, getResources().getString(R.string.no_internet));
        }

        relBack.setOnClickListener(this);

        cardElectrical.setOnClickListener(this);
        cardMechanicals.setOnClickListener(this);
        cardStickers.setOnClickListener(this);
        cardFasteners.setOnClickListener(this);
        linPath.setOnClickListener(this);

        return view;
    }

    private void getLocationList() {
        try {
            showDialog();
            Call<LocationModel> call = apiService.getLocationListApi(getResources().getString(R.string.api_key));
            Log.e(TAG, "Location list request URL " + call.request().url());
            call.enqueue(new Callback<LocationModel>() {
                @Override
                public void onResponse(Call<LocationModel> call, Response<LocationModel> response) {
                    hideDialog();
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == 1) {
                            recyclerCrate.setVisibility(View.GONE);
                            recyclerLocation.setVisibility(View.VISIBLE);
                            noDataLayout.setVisibility(View.GONE);
                            locationList.clear();
                            for (int i = 0; i < response.body().getData().size(); i++) {
                                locationList.add(response.body().getData().get(i));
                            }
                            locationAdapter.notifyDataSetChanged();

                        } else {
                            recyclerCrate.setVisibility(View.GONE);
                            recyclerLocation.setVisibility(View.GONE);
                            noDataLayout.setVisibility(View.VISIBLE);
                            hideDialog();
                            AppUtil.showToastFail(activity, response.body().getMessage());
                        }
                    } else {
                        hideDialog();
                        Log.d(TAG, "Location list Response fail " + new Gson().toJson(response));
                        AppUtil.showToastFail(activity, getResources().getString(R.string.something_wrong));
                    }
                }

                @Override
                public void onFailure(Call<LocationModel> call, Throwable t) {
                    hideDialog();
                    Log.d(TAG, "Location list Request fail " + t.getMessage());
                    AppUtil.showToastFail(activity, getResources().getString(R.string.something_wrong));
                }
            });
        } catch (Exception e) {
            hideDialog();
            Log.d(TAG, "Exception Location list " + e.getMessage());
            AppUtil.showToastFail(activity, activity.getResources().getString(R.string.something_wrong));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cardElectrical:
                DrawerActivity.backPressStatus = 2;
                loadFragment(new ElectricalsFragment(getActivity().getResources().getString(R.string.electricals), 1));
                break;

            case R.id.cardMechanicals:
                DrawerActivity.backPressStatus = 2;
                loadFragment(new ElectricalsFragment(getActivity().getResources().getString(R.string.mechanicals), 2));
                break;

            case R.id.cardStickers:
                DrawerActivity.backPressStatus = 2;
                loadFragment(new ElectricalsFragment(getActivity().getResources().getString(R.string.stickers), 4));
                break;

            case R.id.cardFasteners:
                DrawerActivity.backPressStatus = 2;
                loadFragment(new ElectricalsFragment(getActivity().getResources().getString(R.string.fasteners), 5));
                break;

            case R.id.relBack:
                getFragmentManager().popBackStack();
                break;

            case R.id.linPath:
                linPath.setVisibility(View.GONE);
                recyclerLocation.setVisibility(View.VISIBLE);
                recyclerCrate.setVisibility(View.GONE);
                noDataLayout.setVisibility(View.GONE);

                break;
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_content, fragment);
        fragmentTransaction.addToBackStack("SubStockCheck");
        fragmentTransaction.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.gcm_rec = new Intent(getResources().getString(R.string.stock_check));
        LocalBroadcastManager.getInstance(activity).sendBroadcast(this.gcm_rec);
    }

    @Override
    public void onClick(View view, int position) {
        txtLocationName.setText(locationList.get(position).getLocationName());
        selectedLocationName = locationList.get(position).getLocationName();
        linPath.setVisibility(View.VISIBLE);
        if (AppUtil.isOnline(getActivity())) {
            getCrateList(locationList.get(position).getLocationId());
        } else {
            AppUtil.showToastFail(activity, getResources().getString(R.string.no_internet));
        }
    }

    private void getCrateList(String locationId) {
        showDialog();
        recyclerLocation.setVisibility(View.GONE);
        try {
            Call<CratesListInternalMovementsModel> call = apiService.getCratesListApi(getResources().getString(R.string.api_key), locationId);
            Log.e(TAG, "Crates list request URL " + call.request().url());
            call.enqueue(new Callback<CratesListInternalMovementsModel>() {
                @Override
                public void onResponse(Call<CratesListInternalMovementsModel> call, Response<CratesListInternalMovementsModel> response) {
                    hideDialog();
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == 1) {
                            recyclerCrate.setVisibility(View.VISIBLE);
                            recyclerLocation.setVisibility(View.GONE);
                            noDataLayout.setVisibility(View.GONE);
                            cratesList.clear();
                            for (int i = 0; i < response.body().getData().size(); i++) {
                                cratesList.add(response.body().getData().get(i));
                            }
                            crateAdapter.notifyDataSetChanged();

                        } else {
                            recyclerCrate.setVisibility(View.GONE);
                            recyclerLocation.setVisibility(View.GONE);
                            noDataLayout.setVisibility(View.VISIBLE);
                            cratesList.clear();
                            hideDialog();
                            AppUtil.showToastFail(activity, response.body().getMessage());
                        }
                    } else {
                        cratesList.clear();
                        Log.d(TAG, "Crates list Response fail " + new Gson().toJson(response));
                        AppUtil.showToastFail(activity, getResources().getString(R.string.something_wrong));
                    }
                }

                @Override
                public void onFailure(Call<CratesListInternalMovementsModel> call, Throwable t) {
                    cratesList.clear();
                    hideDialog();
                    Log.d(TAG, "Crates list Request fail " + t.getMessage());
                    AppUtil.showToastFail(activity, getResources().getString(R.string.something_wrong));
                }
            });
        } catch (Exception e) {
            hideDialog();
            Log.d(TAG, "Exception Crates list " + e.getMessage());
            AppUtil.showToastFail(activity, activity.getResources().getString(R.string.something_wrong));
        }
    }

    @Override
    public void onClickCrate(View view, int position) {
        selectedCrateName=cratesList.get(position).getCrateName();
        DrawerActivity.backPressStatus = 2;
        loadFragment(new StockCheckLocationWiseFragment(selectedLocationName,selectedCrateName, 6, cratesList.get(position).getCrateId()));
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
