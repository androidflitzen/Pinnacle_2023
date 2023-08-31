package com.flitzen.pinnacle.inventory_management.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flitzen.pinnacle.MyApplicationClass;
import com.flitzen.pinnacle.R;
import com.flitzen.pinnacle.activity.DrawerActivity;
import com.flitzen.pinnacle.inventory_management.adapter.NewInternalMovementAdapter;
import com.flitzen.pinnacle.inventory_management.adapter.NewInternalMovementInAdapter;
import com.flitzen.pinnacle.inventory_management.adapter.SpinnerCrates2Adapter;
import com.flitzen.pinnacle.inventory_management.adapter.SpinnerCratesAdapter;
import com.flitzen.pinnacle.inventory_management.adapter.SpinnerLocationAdapter;
import com.flitzen.pinnacle.inventory_management.model.CommonModel;
import com.flitzen.pinnacle.inventory_management.model.CraeteItemModel;
import com.flitzen.pinnacle.inventory_management.model.CrateItemListModel;
import com.flitzen.pinnacle.inventory_management.model.CratesListInternalMovementsModel;
import com.flitzen.pinnacle.inventory_management.model.LocationModel;
import com.flitzen.pinnacle.utils.ApiUrlList;
import com.flitzen.pinnacle.utils.AppUtil;
import com.flitzen.pinnacle.utils.NDSpinner;
import com.flitzen.pinnacle.utils.SharePref;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewInternalMovementFragment_test extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    View view;
    private RecyclerView recyclerItems, recyclerItemsIn;
    private NewInternalMovementAdapter newInternalMovementAdapter;
    private NewInternalMovementInAdapter newInternalMovementInAdapter;
    Activity activity;
    private RelativeLayout relBack;
    private SpinnerLocationAdapter spinnerAdapter;
    private SpinnerCratesAdapter spinnerCratesAdapter;
    private SpinnerCrates2Adapter spinnerCratesAdapter2;
    private ArrayList<LocationModel.Data> locationList = new ArrayList<>();
    private ArrayList<CrateItemListModel.Data> arrayListFrom = new ArrayList<>();
    private ArrayList<CraeteItemModel> arrayListFromStore = new ArrayList<>();
    private ArrayList<CraeteItemModel> arrayListFromStoreTo = new ArrayList<>();
    private ArrayList<CratesListInternalMovementsModel.Data> cratesList = new ArrayList<>();
    private ArrayList<CratesListInternalMovementsModel.Data> cratesList2 = new ArrayList<>();
    private NDSpinner spinnerLocation1, spinnerLocation2, spinnerCrate1, spinnerCrate2;
    private ProgressDialog prd;
    private String TAG = "NewInternalMovementFragment_test";
    private ApiUrlList apiService;
    private TextView txtLocationName1, txtLocationName2, txtCrateName1, txtCrateName2, txtItemCount, txtCrateFrom, txtCrateTo;
    public ArrayAdapter<String> yearAdapter;
    private String location1, location2, crate1, crate2, crateID1, crateID2;
    private LinearLayout linShowData,linNoCrate1,linNoCrate2;
    private RelativeLayout noDataLayout;
    private AppCompatButton btnProcess, btnConfirm;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = getActivity();
        DrawerActivity.MAIN_FRAGMENT = "SUB_FRAGMENT";
        view = inflater.inflate(R.layout.new_internal_movement_layout_test, container, false);
        createDialog1();

        apiService =
                MyApplicationClass.getClient().create(ApiUrlList.class);
        sharedPreferences = SharePref.getSharePref(getActivity());

        recyclerItems = view.findViewById(R.id.recyclerItems);
        recyclerItemsIn = view.findViewById(R.id.recyclerItemsIn);
        relBack = view.findViewById(R.id.relBack);
        spinnerLocation1 = view.findViewById(R.id.spinnerLocation1);
        spinnerLocation2 = view.findViewById(R.id.spinnerLocation2);
        spinnerCrate1 = view.findViewById(R.id.spinnerCrate1);
        spinnerCrate2 = view.findViewById(R.id.spinnerCrate2);
        txtLocationName1 = view.findViewById(R.id.txtLocationName1);
        txtLocationName2 = view.findViewById(R.id.txtLocationName2);
        txtCrateName1 = view.findViewById(R.id.txtCrateName1);
        txtCrateName2 = view.findViewById(R.id.txtCrateName2);
        linShowData = view.findViewById(R.id.linShowData);
        noDataLayout = view.findViewById(R.id.noDataLayout);
        txtItemCount = view.findViewById(R.id.txtItemCount);
        txtCrateFrom = view.findViewById(R.id.txtCrateFrom);
        txtCrateTo = view.findViewById(R.id.txtCrateTo);
        btnProcess = view.findViewById(R.id.btnProcess);
        btnConfirm = view.findViewById(R.id.btnConfirm);
        linNoCrate1 = view.findViewById(R.id.linNoCrate1);
        linNoCrate2 = view.findViewById(R.id.linNoCrate2);

        relBack.setOnClickListener(this);
        btnProcess.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);

        newInternalMovementAdapter = new NewInternalMovementAdapter(activity, arrayListFrom, arrayListFromStore);
        recyclerItems.setHasFixedSize(true);
        recyclerItems.setLayoutManager(new LinearLayoutManager(activity));
        recyclerItems.setAdapter(newInternalMovementAdapter);
        recyclerItems.setItemViewCacheSize(1000);

        newInternalMovementInAdapter = new NewInternalMovementInAdapter(activity, arrayListFromStoreTo);
        recyclerItemsIn.setHasFixedSize(true);
        recyclerItemsIn.setLayoutManager(new LinearLayoutManager(activity));
        recyclerItemsIn.setAdapter(newInternalMovementInAdapter);
        recyclerItemsIn.setItemViewCacheSize(1000);

        spinnerAdapter = new SpinnerLocationAdapter(getActivity(), locationList);
        spinnerLocation1.setOnItemSelectedListener(this);
        spinnerLocation1.setAdapter(spinnerAdapter);

        spinnerLocation2.setOnItemSelectedListener(this);
        spinnerLocation2.setAdapter(spinnerAdapter);

        if (AppUtil.isOnline(getActivity())) {
            getLocationList();
        } else {
            AppUtil.showToastFail(activity, getResources().getString(R.string.no_internet));
        }

        return view;
    }

    public void initializeCrate(int type) {
        if (type == 1) {
            spinnerCratesAdapter = new SpinnerCratesAdapter(getActivity(), cratesList);
            spinnerCrate1.setOnItemSelectedListener(this);
            spinnerCrate1.setAdapter(spinnerCratesAdapter);

        } else if (type == 2) {
            spinnerCratesAdapter = new SpinnerCratesAdapter(getActivity(), cratesList2);
            spinnerCrate2.setOnItemSelectedListener(this);
            spinnerCrate2.setAdapter(spinnerCratesAdapter);
        }
    }

    private void getCrateItemList(String crateId) {
        try {
            showDialog();
            System.out.println("==========crateId  " + crateId);
            Call<CrateItemListModel> call = apiService.getCrateItemListApi(getResources().getString(R.string.api_key), crateId);
            Log.e(TAG, "Crate Item list request URL " + call.request().url());
            call.enqueue(new Callback<CrateItemListModel>() {
                @Override
                public void onResponse(Call<CrateItemListModel> call, Response<CrateItemListModel> response) {
                    hideDialog();
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == 1) {
                            linNoCrate1.setVisibility(View.GONE);
                            btnProcess.setVisibility(View.VISIBLE);
                            linShowData.setVisibility(View.VISIBLE);
                            noDataLayout.setVisibility(View.GONE);
                            arrayListFrom.clear();
                            arrayListFromStore.clear();

                            for (int i = 0; i < response.body().getData().size(); i++) {
                                arrayListFrom.add(response.body().getData().get(i));
                                arrayListFromStore.add(new CraeteItemModel(response.body().getData().get(i).getItemId(), "0", response.body().getData().get(i).getItemName()));
                            }

                            txtItemCount.setText("Items(" + String.valueOf(arrayListFrom.size()) + ")");

                            // For animation
                            int resId = R.anim.layout_animation_fall_down;
                            LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(activity, resId);
                            recyclerItems.setLayoutAnimation(animation);

                            newInternalMovementAdapter.notifyDataSetChanged();
                        } else {
                            linNoCrate1.setVisibility(View.VISIBLE);
                            btnProcess.setVisibility(View.GONE);
                            linShowData.setVisibility(View.GONE);
                            noDataLayout.setVisibility(View.VISIBLE);
                            AppUtil.showToastFail(activity, response.body().getMessage());
                        }

                    } else {
                        Log.d(TAG, "Crate Item list Response fail " + new Gson().toJson(response));
                        AppUtil.showToastFail(activity, getResources().getString(R.string.something_wrong));
                    }
                }

                @Override
                public void onFailure(Call<CrateItemListModel> call, Throwable t) {
                    hideDialog();
                    Log.d(TAG, "Crate Item list Request fail " + t.getMessage());
                    AppUtil.showToastFail(activity, getResources().getString(R.string.something_wrong));
                }
            });

        } catch (Exception e) {
            hideDialog();
            Log.d(TAG, "Exception Crate Item list " + e.getMessage());
            AppUtil.showToastFail(activity, activity.getResources().getString(R.string.something_wrong));
        }
    }

    private void getLocationList() {
        try {
            showDialog();
            Call<LocationModel> call = apiService.getLocationListApi(getResources().getString(R.string.api_key));
            Log.e(TAG, "Location list request URL " + call.request().url());
            call.enqueue(new Callback<LocationModel>() {
                @Override
                public void onResponse(Call<LocationModel> call, Response<LocationModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == 1) {
                            locationList.clear();
                            for (int i = 0; i < response.body().getData().size(); i++) {
                                locationList.add(response.body().getData().get(i));
                            }
                            spinnerAdapter.notifyDataSetChanged();

                        } else {
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spinnerLocation1:
                System.out.println("===========spinnerLocation1");
                txtLocationName1.setText(locationList.get(position).getLocationName());
                location1 = txtLocationName1.getText().toString();
                if (AppUtil.isOnline(getActivity())) {
                    getCrateList(locationList.get(position).getLocationId(), 1);
                } else {
                    AppUtil.showToastFail(activity, getResources().getString(R.string.no_internet));
                }
                break;

            case R.id.spinnerLocation2:
                txtLocationName2.setText(locationList.get(position).getLocationName());
                location2 = txtLocationName2.getText().toString();
                if (AppUtil.isOnline(getActivity())) {
                    getCrateList(locationList.get(position).getLocationId(), 2);
                } else {
                    AppUtil.showToastFail(activity, getResources().getString(R.string.no_internet));
                }
                break;

            case R.id.spinnerCrate1:
                txtCrateName1.setText(cratesList.get(position).getCrateName());
                txtCrateFrom.setText(cratesList.get(position).getCrateName());
                crate1 = txtCrateName1.getText().toString();
                crateID1 = cratesList.get(position).getCrateId();

                if (AppUtil.isOnline(getActivity())) {
                    getCrateItemList(cratesList.get(position).getCrateId());
                } else {
                    AppUtil.showToastFail(activity, getResources().getString(R.string.no_internet));
                }

                break;

            case R.id.spinnerCrate2:
                txtCrateName2.setText(cratesList2.get(position).getCrateName());
                crate2 = txtCrateName2.getText().toString();
                txtCrateTo.setText(cratesList2.get(position).getCrateName());
                crateID2 = cratesList2.get(position).getCrateId();

                break;

            default:
                break;
        }
    }

    private void getCrateList(String locationId, int type) {
        try {
            Call<CratesListInternalMovementsModel> call = apiService.getCratesListApi(getResources().getString(R.string.api_key), locationId);
            Log.e(TAG, "Crates list request URL " + call.request().url());
            call.enqueue(new Callback<CratesListInternalMovementsModel>() {
                @Override
                public void onResponse(Call<CratesListInternalMovementsModel> call, Response<CratesListInternalMovementsModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == 1) {
                            if (type == 1) {
                                linNoCrate1.setVisibility(View.GONE);
                                cratesList.clear();
                                for (int i = 0; i < response.body().getData().size(); i++) {
                                    cratesList.add(response.body().getData().get(i));
                                }
                            } else if (type == 2) {
                                hideDialog();
                                linNoCrate2.setVisibility(View.GONE);
                                cratesList2.clear();
                                for (int i = 0; i < response.body().getData().size(); i++) {
                                    cratesList2.add(response.body().getData().get(i));
                                }
                            }
                            initializeCrate(type);
                        } else {
                            hideDialog();
                            AppUtil.showToastFail(activity, response.body().getMessage());
                            if (type == 1) {
                                System.out.println("===========type");
                                cratesList.clear();
                                txtCrateName1.setText("No Crate");
                                txtCrateFrom.setText("-");
                                btnProcess.setVisibility(View.GONE);
                                linShowData.setVisibility(View.GONE);
                                noDataLayout.setVisibility(View.VISIBLE);
                                linNoCrate1.setVisibility(View.VISIBLE);
                                crateID1="";
                            } else if (type == 2) {
                                cratesList2.clear();
                                txtCrateName2.setText("No Crate");
                                txtCrateTo.setText("-");
                                linNoCrate2.setVisibility(View.VISIBLE);
                                crateID2="";
                            }
                        }
                    } else {
                        if (type == 1) {
                            cratesList.clear();
                        } else if (type == 2) {
                            hideDialog();
                            cratesList2.clear();
                        }
                        Log.d(TAG, "Crates list Response fail " + new Gson().toJson(response));
                        AppUtil.showToastFail(activity, getResources().getString(R.string.something_wrong));
                    }
                }

                @Override
                public void onFailure(Call<CratesListInternalMovementsModel> call, Throwable t) {
                    if (type == 1) {
                        cratesList.clear();
                    } else if (type == 2) {
                        cratesList2.clear();
                    }
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.relBack:
                getFragmentManager().popBackStack();
                break;

            case R.id.btnProcess:
                arrayListFromStoreTo.clear();
                showDialog();
                if(arrayListFromStore.size()>0){
                    for (int i = 0; i < arrayListFromStore.size(); i++) {
                        if (Float.parseFloat(arrayListFromStore.get(i).getQty()) > 0.00f) {
                            arrayListFromStoreTo.add(arrayListFromStore.get(i));
                        }
                    }
                }else {
                    AppUtil.showToastFail(activity, "Please select items.");
                }

                hideDialog();
                newInternalMovementInAdapter.notifyDataSetChanged();
                break;

            case R.id.btnConfirm:
                if (arrayListFromStoreTo.size() > 0) {
                    if (location2 != null && crate2 != null && !txtCrateName2.getText().toString().equalsIgnoreCase("Select Crate")
                            && !txtCrateName2.getText().toString().equalsIgnoreCase("No Crate")) {
                        if (AppUtil.isOnline(getActivity())) {
                            moveItems();
                        } else {
                            AppUtil.showToastFail(activity, getResources().getString(R.string.no_internet));
                        }
                    } else {
                        AppUtil.showToastFail(getActivity(), "Please select Location and crate.");
                    }
                } else {
                    AppUtil.showToastFail(getActivity(), "Please select items which you want to move.");
                }
                break;
        }
    }

    private void moveItems() {
        try {
            showDialog();

            Map<String, String> params = new HashMap<>();
            params.put("api_key", getResources().getString(R.string.api_key));
            params.put("user_id",  sharedPreferences.getString(SharePref.userId, ""));
            params.put("from_crate_id", crateID1);
            params.put("to_crate_id", crateID2);

            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < arrayListFromStoreTo.size(); i++) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("item_id", arrayListFromStoreTo.get(i).getId());
                    jsonObject.put("item_qty", arrayListFromStoreTo.get(i).getQty());
                    jsonArray.put(jsonObject);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            params.put("item_data", jsonArray.toString());

            Call<CommonModel> call = apiService.moveItems(params);
            Log.e(TAG, "Move items request URL " + call.request().url());
            call.enqueue(new Callback<CommonModel>() {
                @Override
                public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                    hideDialog();
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == 1) {
                            getActivity().getSupportFragmentManager().popBackStack();
                        } else {
                            AppUtil.showToastFail(activity, response.body().getMessage());
                        }
                    } else {
                        Log.d(TAG, "Move items Response fail " + new Gson().toJson(response));
                        AppUtil.showToastFail(activity, getResources().getString(R.string.something_wrong));
                    }
                }

                @Override
                public void onFailure(Call<CommonModel> call, Throwable t) {
                    hideDialog();
                    Log.d(TAG, "Move items Request fail " + t.getMessage());
                    AppUtil.showToastFail(activity, getResources().getString(R.string.something_wrong));
                }
            });
        } catch (Exception e) {
            hideDialog();
            Log.d(TAG, "Exception Move items " + e.getMessage());
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

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
