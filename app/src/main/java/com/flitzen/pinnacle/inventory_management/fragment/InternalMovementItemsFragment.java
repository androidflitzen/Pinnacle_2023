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
import com.flitzen.pinnacle.inventory_management.adapter.InternalMovementItemsAdapter;
import com.flitzen.pinnacle.inventory_management.adapter.InternalMovementsAdapter;
import com.flitzen.pinnacle.inventory_management.model.InternalMovementItemListModel;
import com.flitzen.pinnacle.utils.ApiUrlList;
import com.flitzen.pinnacle.utils.AppUtil;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InternalMovementItemsFragment extends Fragment implements View.OnClickListener {

    View view;
    public ProgressDialog prd;

    RecyclerView recyclerInternal;
    InternalMovementItemsAdapter internalMovementItemsAdapter;
    private RelativeLayout relBack, noDataLayout;
    private String TAG = "InternalMovementItemsFragment";
    private ApiUrlList apiService;
    private ArrayList<InternalMovementItemListModel.Data> arrayList = new ArrayList<>();
    private String movementId,date,time,fromCrateName,toCrateName;
    private TextView txtDate,txtTime,txtFrom,txtTo;

    public InternalMovementItemsFragment(String movementId, String date, String time, String fromCrateName, String toCrateName) {
        this.movementId = movementId;
        this.date = date;
        this.time = time;
        this.fromCrateName = fromCrateName;
        this.toCrateName = toCrateName;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        DrawerActivity.MAIN_FRAGMENT = "SUB_FRAGMENT";
        view = inflater.inflate(R.layout.internal_movement_item_layout, container, false);
        createDialog1();

        apiService =
                MyApplicationClass.getClient().create(ApiUrlList.class);

        recyclerInternal = view.findViewById(R.id.recyclerInternal);
        relBack = view.findViewById(R.id.relBack);
        noDataLayout = view.findViewById(R.id.noDataLayout);
        txtDate = view.findViewById(R.id.txtDate);
        txtTime = view.findViewById(R.id.txtTime);
        txtFrom = view.findViewById(R.id.txtFrom);
        txtTo = view.findViewById(R.id.txtTo);

        relBack.setOnClickListener(this);

        internalMovementItemsAdapter = new InternalMovementItemsAdapter(getActivity(), arrayList);
        recyclerInternal.setHasFixedSize(true);
        recyclerInternal.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerInternal.setAdapter(internalMovementItemsAdapter);

        if (AppUtil.isOnline(getActivity())) {
            getInternalMovementItemList();
        } else {
            AppUtil.showToastFail(getActivity(), getResources().getString(R.string.no_internet));
        }

        txtDate.setText(date);
        txtTime.setText(time);
        txtFrom.setText("Crate - "+fromCrateName);
        txtTo.setText("Crate - "+toCrateName);

        return view;
    }

    private void getInternalMovementItemList() {
        try {
            showDialog();
            Call<InternalMovementItemListModel> call = apiService.getInternalMovementsItemListApi(getResources().getString(R.string.api_key), movementId);
            Log.e(TAG, "get internal movement item list request URL " + call.request().url());
            call.enqueue(new Callback<InternalMovementItemListModel>() {
                @Override
                public void onResponse(Call<InternalMovementItemListModel> call, Response<InternalMovementItemListModel> response) {
                    hideDialog();
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == 1) {
                            recyclerInternal.setVisibility(View.VISIBLE);
                            noDataLayout.setVisibility(View.GONE);
                            arrayList.clear();

                            for (int i = 0; i < response.body().getData().size(); i++) {
                                arrayList.add(response.body().getData().get(i));
                            }

                            // For animation
                            int resId = R.anim.layout_animation_fall_down;
                            LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getActivity(), resId);
                            recyclerInternal.setLayoutAnimation(animation);

                            internalMovementItemsAdapter.notifyDataSetChanged();

                        } else {
                            recyclerInternal.setVisibility(View.GONE);
                            noDataLayout.setVisibility(View.VISIBLE);
                            AppUtil.showToastFail(getActivity(), response.body().getMessage());
                        }
                    } else {
                        Log.d(TAG, "get internal movement item list Response fail " + new Gson().toJson(response));
                        AppUtil.showToastFail(getActivity(), getResources().getString(R.string.something_wrong));
                    }
                }

                @Override
                public void onFailure(Call<InternalMovementItemListModel> call, Throwable t) {
                    hideDialog();
                    Log.d(TAG, "get internal movement item list Request fail " + t.getMessage());
                    AppUtil.showToastFail(getActivity(), getResources().getString(R.string.something_wrong));
                }
            });
        } catch (Exception e) {
            hideDialog();
            Log.d(TAG, "Exception get internal movement item list " + e.getMessage());
            AppUtil.showToastFail(getActivity(), getActivity().getResources().getString(R.string.something_wrong));
        }
    }

    private void createDialog1() {
        prd = new ProgressDialog(getActivity(), R.style.MyAlertDialogStyle);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.relBack:
                getFragmentManager().popBackStack();
                break;
        }
    }
}
