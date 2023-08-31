package com.flitzen.pinnacle.inventory_management.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flitzen.pinnacle.MyApplicationClass;
import com.flitzen.pinnacle.R;
import com.flitzen.pinnacle.activity.DrawerActivity;
import com.flitzen.pinnacle.inventory_management.adapter.RecentActiviesAdapter;
import com.flitzen.pinnacle.inventory_management.model.DashBoardDataLocalModel;
import com.flitzen.pinnacle.inventory_management.model.DashBoardDataModel;
import com.flitzen.pinnacle.inventory_management.model.PendingOrderBOMListModel;
import com.flitzen.pinnacle.utils.ApiUrlList;
import com.flitzen.pinnacle.utils.AppUtil;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashBoardFragment extends Fragment implements View.OnClickListener {

    private View view;
    private RecyclerView recyclerRecentActivity;
    private RecentActiviesAdapter recentActiviesAdapter;
    private CardView cardPending, cardDelivered, cardStockCheck, cardStockAlerts, cardPurchase, cardInternal, cardJobWork;
    Activity activity;
    public Fragment currentFragment;
    Intent gcm_rec;
    RelativeLayout noDataLayout;
    public ProgressDialog prd;
    private String TAG = "DashBoardFragment";
    private ApiUrlList apiService;
    private TextView txtPendingOrderCount, txtDeliveredOrderCount;
    private RelativeLayout relDeliveredCount, relPendingCount;
    private ArrayList<DashBoardDataModel.RecentActivity> recentActivityArrayList = new ArrayList<>();
    private ArrayList<DashBoardDataLocalModel> recentActivityArrayListTemp = new ArrayList<>();
    private ArrayList<String> dateList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = getActivity();
        view = inflater.inflate(R.layout.home_layout_test, container, false);
        createDialog1();
        apiService =
                MyApplicationClass.getClient().create(ApiUrlList.class);
        recyclerRecentActivity = view.findViewById(R.id.recyclerRecentActivity);
        cardPending = view.findViewById(R.id.cardPending);
        cardDelivered = view.findViewById(R.id.cardDelivered);
        cardStockCheck = view.findViewById(R.id.cardStockCheck);
        cardStockAlerts = view.findViewById(R.id.cardStockAlerts);
        cardPurchase = view.findViewById(R.id.cardPurchase);
        cardInternal = view.findViewById(R.id.cardInternal);
        cardJobWork = view.findViewById(R.id.cardJobWork);
        noDataLayout = view.findViewById(R.id.noDataLayout);
        txtPendingOrderCount = view.findViewById(R.id.txtPendingOrderCount);
        txtDeliveredOrderCount = view.findViewById(R.id.txtDeliveredOrderCount);
        relDeliveredCount = view.findViewById(R.id.relDeliveredCount);
        relPendingCount = view.findViewById(R.id.relPendingCount);

        // txtHome=((DrawerActivity)activity).findViewById(R.id.txtHome);

        cardPending.setOnClickListener(this);
        cardDelivered.setOnClickListener(this);
        cardStockCheck.setOnClickListener(this);
        cardStockAlerts.setOnClickListener(this);
        cardPurchase.setOnClickListener(this);
        cardInternal.setOnClickListener(this);
        cardJobWork.setOnClickListener(this);

        recentActiviesAdapter = new RecentActiviesAdapter(getActivity(), recentActivityArrayListTemp);
        recyclerRecentActivity.setHasFixedSize(true);
        recyclerRecentActivity.setLayoutManager(new LinearLayoutManager(activity));
        recyclerRecentActivity.setAdapter(recentActiviesAdapter);

        if (AppUtil.isOnline(getActivity())) {
            showDialog();
            getDashBoardData();
        } else {
            AppUtil.showToastFail(activity, getResources().getString(R.string.no_internet));
        }

        return view;
    }

    private void getDashBoardData() {
        try {
            Call<DashBoardDataModel> call = apiService.getDashBoardData(getResources().getString(R.string.api_key));
            Log.e(TAG, "Dash Board Data list request URL " + call.request().url());
            call.enqueue(new Callback<DashBoardDataModel>() {
                @Override
                public void onResponse(Call<DashBoardDataModel> call, Response<DashBoardDataModel> response) {
                    if (response.isSuccessful()) {
                        if (Integer.parseInt(response.body().getTotalOrderPending()) > 0) {
                            relPendingCount.setVisibility(View.VISIBLE);
                            txtPendingOrderCount.setText(response.body().getTotalOrderPending());
                        } else {
                            relPendingCount.setVisibility(View.GONE);
                        }

                        if (Integer.parseInt(response.body().getTotalOrderDeliver()) > 0) {
                            relDeliveredCount.setVisibility(View.VISIBLE);
                            txtDeliveredOrderCount.setText(response.body().getTotalOrderDeliver());
                        } else {
                            relDeliveredCount.setVisibility(View.GONE);
                        }

                        if (response.body().getStatus() == 1) {
                            if (response.body().getRecentActivity().size() > 0) {
                                recyclerRecentActivity.setVisibility(View.VISIBLE);
                                noDataLayout.setVisibility(View.GONE);

                                dateList.clear();
                                recentActivityArrayList.clear();
                                recentActivityArrayListTemp.clear();

                                for (int i = 0; i < response.body().getRecentActivity().size(); i++) {
                                    DashBoardDataModel.RecentActivity recentActivity = response.body().getRecentActivity().get(i);
                                    if (dateList.contains(recentActivity.getDate())) {
                                        recentActivityArrayListTemp.add(new DashBoardDataLocalModel(recentActivity.getRemarks(), recentActivity.getUserName(), recentActivity.getDate()
                                                , recentActivity.getTime(), "1"));
                                    } else {
                                        dateList.add(recentActivity.getDate());
                                        recentActivityArrayListTemp.add(new DashBoardDataLocalModel(recentActivity.getRemarks(), recentActivity.getUserName(), recentActivity.getDate()
                                                , recentActivity.getTime(), "0"));
                                        recentActivityArrayListTemp.add(new DashBoardDataLocalModel(recentActivity.getRemarks(), recentActivity.getUserName(), recentActivity.getDate()
                                                , recentActivity.getTime(), "1"));
                                    }
                                }

                                hideDialog();
                                recentActiviesAdapter.updateUI(recentActivityArrayListTemp);


                            } else {
                                hideDialog();
                                recyclerRecentActivity.setVisibility(View.GONE);
                                noDataLayout.setVisibility(View.VISIBLE);
                            }
                        } else {
                            hideDialog();
                            recyclerRecentActivity.setVisibility(View.GONE);
                            noDataLayout.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Log.d(TAG, "Dash Board Data list Response fail " + new Gson().toJson(response));
                        AppUtil.showToastFail(activity, getResources().getString(R.string.something_wrong));
                    }
                }

                @Override
                public void onFailure(Call<DashBoardDataModel> call, Throwable t) {
                    hideDialog();
                    Log.d(TAG, "Dash Board Data list Request fail " + t.getMessage());
                    AppUtil.showToastFail(activity, getResources().getString(R.string.something_wrong));
                }
            });
        } catch (Exception e) {
            hideDialog();
            Log.d(TAG, "Exception Dash Board Data list " + e.getMessage());
            AppUtil.showToastFail(activity, activity.getResources().getString(R.string.something_wrong));
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.cardPending:
                currentFragment = new PendingOrdersFragment();
                DrawerActivity.backPressStatus = 1;
                loadFragment(currentFragment, activity.getResources().getString(R.string.pending_orders));

                this.gcm_rec = new Intent(getResources().getString(R.string.pending_orders));
                LocalBroadcastManager.getInstance(activity).sendBroadcast(this.gcm_rec);

                break;

            case R.id.cardDelivered:
                currentFragment = new DeliveredOrdersFragment();
                DrawerActivity.backPressStatus = 1;
                loadFragment(currentFragment, activity.getResources().getString(R.string.delivered_orders));

                this.gcm_rec = new Intent(getResources().getString(R.string.delivered_orders));
                LocalBroadcastManager.getInstance(activity).sendBroadcast(this.gcm_rec);
                break;

            case R.id.cardStockCheck:
                currentFragment = new StockCheckFragment();
                DrawerActivity.backPressStatus = 1;
                loadFragment(currentFragment, activity.getResources().getString(R.string.stock_check));

                this.gcm_rec = new Intent(getResources().getString(R.string.stock_check));
                LocalBroadcastManager.getInstance(activity).sendBroadcast(this.gcm_rec);

                break;

            case R.id.cardStockAlerts:
                currentFragment = new StockAlertsFragment();
                DrawerActivity.backPressStatus = 1;
                loadFragment(currentFragment, activity.getResources().getString(R.string.stock_alerts));

                this.gcm_rec = new Intent(getResources().getString(R.string.stock_alerts));
                LocalBroadcastManager.getInstance(activity).sendBroadcast(this.gcm_rec);

                break;

            case R.id.cardPurchase:
                currentFragment = new PurchaseOrdersFragment();
                DrawerActivity.backPressStatus = 1;
                loadFragment(currentFragment, activity.getResources().getString(R.string.purchase_orders));

                this.gcm_rec = new Intent(getResources().getString(R.string.purchase_orders));
                LocalBroadcastManager.getInstance(activity).sendBroadcast(this.gcm_rec);
                break;

            case R.id.cardInternal:
                currentFragment = new InternalMovementsFragment();
                DrawerActivity.backPressStatus = 1;
                loadFragment(currentFragment, activity.getResources().getString(R.string.internal_movements));

                this.gcm_rec = new Intent(getResources().getString(R.string.internal_movements));
                LocalBroadcastManager.getInstance(activity).sendBroadcast(this.gcm_rec);

                break;

            case R.id.cardJobWork:
                currentFragment = new JobWorksFragment();
                DrawerActivity.backPressStatus = 1;
                loadFragment(currentFragment, activity.getResources().getString(R.string.job_works));

                this.gcm_rec = new Intent(getResources().getString(R.string.job_works));
                LocalBroadcastManager.getInstance(activity).sendBroadcast(this.gcm_rec);

                break;
        }
    }

    private void loadFragment(Fragment fragment, String fragmentName) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_content, fragment);
        fragmentTransaction.addToBackStack(fragmentName);
        fragmentTransaction.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.gcm_rec = new Intent(getResources().getString(R.string.menu_home));
        LocalBroadcastManager.getInstance(activity).sendBroadcast(this.gcm_rec);
    }

    private void createDialog1() {
        prd = new ProgressDialog(activity, R.style.MyAlertDialogStyle);
        prd.setMessage("Please wait...");
        prd.setCancelable(false);
    }

    public void showDialog() {
        if (prd != null) {
            if (!prd.isShowing()) {
                System.out.println("==========showDialog   ");
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
