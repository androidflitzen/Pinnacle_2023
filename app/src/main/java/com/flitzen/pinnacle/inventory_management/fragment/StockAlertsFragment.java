package com.flitzen.pinnacle.inventory_management.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.flitzen.pinnacle.MyApplicationClass;
import com.flitzen.pinnacle.R;

import com.flitzen.pinnacle.activity.DrawerActivity;
import com.flitzen.pinnacle.inventory_management.adapter.StockAlertAdapter;
import com.flitzen.pinnacle.inventory_management.adapter.StockCheckPagerAdapter;
import com.flitzen.pinnacle.inventory_management.model.ItemTypeModel;
import com.flitzen.pinnacle.inventory_management.model.PendingOrderBOMListModel;
import com.flitzen.pinnacle.inventory_management.model.StockAlertListModel;
import com.flitzen.pinnacle.utils.ApiUrlList;
import com.flitzen.pinnacle.utils.AppUtil;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StockAlertsFragment extends Fragment implements View.OnClickListener {


    View view;
    Activity activity;
    private RelativeLayout relBack;
    Intent gcm_rec;
    StockCheckPagerAdapter mAdapter;
    public ProgressDialog prd;
    RecyclerView recyclerStockAlertsZero, recyclerStockAlertsLow, recyclerStockAlertsMax;
    StockAlertAdapter stockAlertAdapterZero, stockAlertAdapterLow, stockAlertAdapterMax;
    private RelativeLayout noDataLayout;
    private String TAG = "StockAlertsFragment";
    private ApiUrlList apiService;
    private ArrayList<StockAlertListModel.Data> arrayListZero = new ArrayList<>();
    private ArrayList<StockAlertListModel.Data> arrayListLow = new ArrayList<>();
    private ArrayList<StockAlertListModel.Data> arrayListMax = new ArrayList<>();
    private RelativeLayout relZero, relLow, relMax;
    private CardView cardMax, cardLow, cardZero;
    private TextView txtZeroStock, txtMaxStock, txtLowStock;
    private LinearLayout linZero, linLow, linMax;
    private ImageView imgMax, imgLow, imgZero;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.stock_alerts_layout, container, false);
        activity = getActivity();
        DrawerActivity.MAIN_FRAGMENT = "MAIN_FRAGMENT";
        createDialog1();

        relBack = view.findViewById(R.id.relBack);
        recyclerStockAlertsZero = view.findViewById(R.id.recyclerStockAlertsZero);
        recyclerStockAlertsLow = view.findViewById(R.id.recyclerStockAlertsLow);
        recyclerStockAlertsMax = view.findViewById(R.id.recyclerStockAlertsMax);
        noDataLayout = view.findViewById(R.id.noDataLayout);
        relZero = view.findViewById(R.id.relZero);
        relLow = view.findViewById(R.id.relLow);
        relMax = view.findViewById(R.id.relMax);
        cardLow = view.findViewById(R.id.cardLow);
        cardMax = view.findViewById(R.id.cardMax);
        cardZero = view.findViewById(R.id.cardZero);
        txtZeroStock = view.findViewById(R.id.txtZeroStock);
        txtMaxStock = view.findViewById(R.id.txtMaxStock);
        txtLowStock = view.findViewById(R.id.txtLowStock);
        linZero = view.findViewById(R.id.linZero);
        linLow = view.findViewById(R.id.linLow);
        linMax = view.findViewById(R.id.linMax);
        imgZero = view.findViewById(R.id.imgZero);
        imgLow = view.findViewById(R.id.imgLow);
        imgMax = view.findViewById(R.id.imgMax);

        relBack.setOnClickListener(this);

        apiService =
                MyApplicationClass.getClient().create(ApiUrlList.class);

        stockAlertAdapterZero = new StockAlertAdapter(getActivity(), arrayListZero);
        recyclerStockAlertsZero.setHasFixedSize(true);
        recyclerStockAlertsZero.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerStockAlertsZero.setAdapter(stockAlertAdapterZero);

        stockAlertAdapterLow = new StockAlertAdapter(getActivity(), arrayListLow);
        recyclerStockAlertsLow.setHasFixedSize(true);
        recyclerStockAlertsLow.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerStockAlertsLow.setAdapter(stockAlertAdapterLow);

        stockAlertAdapterMax = new StockAlertAdapter(getActivity(), arrayListMax);
        recyclerStockAlertsMax.setHasFixedSize(true);
        recyclerStockAlertsMax.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerStockAlertsMax.setAdapter(stockAlertAdapterMax);

        //Low - 0 , Max - 1 , 0stock - 2

        if (AppUtil.isOnline(getActivity())) {
            getStockAlertData();
        } else {
            AppUtil.showToastFail(activity, getResources().getString(R.string.no_internet));
        }

        cardLow.setOnClickListener(this);
        cardZero.setOnClickListener(this);
        cardMax.setOnClickListener(this);

        return view;
    }

    private void getStockAlertData() {
        try {
            showDialog();
            Call<StockAlertListModel> call = apiService.getStockAlertListApi(getResources().getString(R.string.api_key));
            Log.e(TAG, "Stock alert list request URL " + call.request().url());
            call.enqueue(new Callback<StockAlertListModel>() {
                @Override
                public void onResponse(Call<StockAlertListModel> call, Response<StockAlertListModel> response) {
                    hideDialog();
                    if (response.isSuccessful()) {
                        Log.d(TAG, "Stock alert list Response success " + new Gson().toJson(response));
                        if (response.body().getStatus() == 1) {
                            arrayListZero.clear();
                            arrayListLow.clear();
                            arrayListMax.clear();

                            for (int i = 0; i < response.body().getData().size(); i++) {
                                StockAlertListModel.Data data = response.body().getData().get(i);
                                if (data.getStatus().equalsIgnoreCase(getActivity().getResources().getString(R.string.max))) {
                                    arrayListMax.add(data);
                                } else if (data.getStatus().equalsIgnoreCase(getActivity().getResources().getString(R.string.min))) {
                                    arrayListLow.add(data);
                                } else if (data.getStatus().equalsIgnoreCase(getActivity().getResources().getString(R.string.zero))) {
                                    arrayListZero.add(data);
                                }
                            }

                            stockAlertAdapterLow.notifyDataSetChanged();
                            stockAlertAdapterMax.notifyDataSetChanged();
                            stockAlertAdapterZero.notifyDataSetChanged();
                            relZero.setVisibility(View.VISIBLE);
                            relLow.setVisibility(View.GONE);
                            relMax.setVisibility(View.GONE);
                            if (arrayListZero.size() > 0) {
                                noDataLayout.setVisibility(View.GONE);
                            }

                        } else {
                            AppUtil.showToastFail(activity, response.body().getMessage());
                        }
                    } else {
                        Log.d(TAG, "Stock alert list Response fail " + new Gson().toJson(response));
                        AppUtil.showToastFail(activity, getResources().getString(R.string.something_wrong));
                    }
                }

                @Override
                public void onFailure(Call<StockAlertListModel> call, Throwable t) {
                    hideDialog();
                    Log.d(TAG, "Stock alert list Request fail " + t.getMessage());
                    AppUtil.showToastFail(activity, getResources().getString(R.string.something_wrong));
                }
            });
        } catch (Exception e) {
            hideDialog();
            Log.d(TAG, "Exception Stock alert list " + e.getMessage());
            AppUtil.showToastFail(activity, activity.getResources().getString(R.string.something_wrong));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.relBack:
                getFragmentManager().popBackStack();
                break;

            case R.id.cardZero:
                relZero.setVisibility(View.VISIBLE);
                relLow.setVisibility(View.GONE);
                relMax.setVisibility(View.GONE);
                if (arrayListZero.size() > 0) {
                    noDataLayout.setVisibility(View.GONE);
                }
                cardZero.setCardBackgroundColor(getActivity().getResources().getColor(R.color.zero_alert));
                cardLow.setCardBackgroundColor(getActivity().getResources().getColor(R.color.white));
                cardMax.setCardBackgroundColor(getActivity().getResources().getColor(R.color.white));

                txtZeroStock.setTextColor(getActivity().getResources().getColor(R.color.white));
                txtLowStock.setTextColor(getActivity().getResources().getColor(R.color.dark_blue_color));
                txtMaxStock.setTextColor(getActivity().getResources().getColor(R.color.dark_blue_color));

                linZero.getBackground().setColorFilter(Color.parseColor("#FA7B75"), PorterDuff.Mode.SRC_ATOP);
                linMax.getBackground().setColorFilter(Color.parseColor("#F6F6F6"), PorterDuff.Mode.SRC_ATOP);
                linLow.getBackground().setColorFilter(Color.parseColor("#F6F6F6"), PorterDuff.Mode.SRC_ATOP);

                imgZero.setColorFilter(ContextCompat.getColor(getActivity(), R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
                imgLow.setColorFilter(ContextCompat.getColor(getActivity(), R.color.dark_blue_color), android.graphics.PorterDuff.Mode.SRC_IN);
                imgMax.setColorFilter(ContextCompat.getColor(getActivity(), R.color.dark_blue_color), android.graphics.PorterDuff.Mode.SRC_IN);

                break;

            case R.id.cardLow:
                relLow.setVisibility(View.VISIBLE);
                relZero.setVisibility(View.GONE);
                relMax.setVisibility(View.GONE);
                if (arrayListLow.size() > 0) {
                    noDataLayout.setVisibility(View.GONE);
                }
                cardLow.setCardBackgroundColor(getActivity().getResources().getColor(R.color.low_alert));
                cardZero.setCardBackgroundColor(getActivity().getResources().getColor(R.color.white));
                cardMax.setCardBackgroundColor(getActivity().getResources().getColor(R.color.white));

                txtLowStock.setTextColor(getActivity().getResources().getColor(R.color.white));
                txtZeroStock.setTextColor(getActivity().getResources().getColor(R.color.dark_blue_color));
                txtMaxStock.setTextColor(getActivity().getResources().getColor(R.color.dark_blue_color));

                linZero.getBackground().setColorFilter(Color.parseColor("#F6F6F6"), PorterDuff.Mode.SRC_ATOP);
                linMax.getBackground().setColorFilter(Color.parseColor("#F6F6F6"), PorterDuff.Mode.SRC_ATOP);
                linLow.getBackground().setColorFilter(Color.parseColor("#FFD08B"), PorterDuff.Mode.SRC_ATOP);

                imgLow.setColorFilter(ContextCompat.getColor(getActivity(), R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
                imgZero.setColorFilter(ContextCompat.getColor(getActivity(), R.color.dark_blue_color), android.graphics.PorterDuff.Mode.SRC_IN);
                imgMax.setColorFilter(ContextCompat.getColor(getActivity(), R.color.dark_blue_color), android.graphics.PorterDuff.Mode.SRC_IN);

                break;

            case R.id.cardMax:
                relMax.setVisibility(View.VISIBLE);
                relZero.setVisibility(View.GONE);
                relLow.setVisibility(View.GONE);
                if (arrayListMax.size() > 0) {
                    noDataLayout.setVisibility(View.GONE);
                }
                cardMax.setCardBackgroundColor(getActivity().getResources().getColor(R.color.max_alert));
                cardLow.setCardBackgroundColor(getActivity().getResources().getColor(R.color.white));
                cardZero.setCardBackgroundColor(getActivity().getResources().getColor(R.color.white));

                txtMaxStock.setTextColor(getActivity().getResources().getColor(R.color.white));
                txtLowStock.setTextColor(getActivity().getResources().getColor(R.color.dark_blue_color));
                txtZeroStock.setTextColor(getActivity().getResources().getColor(R.color.dark_blue_color));

                linZero.getBackground().setColorFilter(Color.parseColor("#F6F6F6"), PorterDuff.Mode.SRC_ATOP);
                linMax.getBackground().setColorFilter(Color.parseColor("#AFD37C"), PorterDuff.Mode.SRC_ATOP);
                linLow.getBackground().setColorFilter(Color.parseColor("#F6F6F6"), PorterDuff.Mode.SRC_ATOP);

                imgZero.setColorFilter(ContextCompat.getColor(getActivity(), R.color.dark_blue_color), android.graphics.PorterDuff.Mode.SRC_IN);
                imgLow.setColorFilter(ContextCompat.getColor(getActivity(), R.color.dark_blue_color), android.graphics.PorterDuff.Mode.SRC_IN);
                imgMax.setColorFilter(ContextCompat.getColor(getActivity(), R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);

                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        this.gcm_rec = new Intent(getResources().getString(R.string.stock_alerts));
        LocalBroadcastManager.getInstance(activity).sendBroadcast(this.gcm_rec);
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
