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
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flitzen.pinnacle.MyApplicationClass;
import com.flitzen.pinnacle.R;
import com.flitzen.pinnacle.activity.DrawerActivity;
import com.flitzen.pinnacle.inventory_management.adapter.InternalMovementsAdapter;
import com.flitzen.pinnacle.inventory_management.model.InternalMovementListModel;
import com.flitzen.pinnacle.inventory_management.model.StockCheckPartsListModel;
import com.flitzen.pinnacle.utils.ApiUrlList;
import com.flitzen.pinnacle.utils.AppUtil;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InternalMovementsFragment extends Fragment implements View.OnClickListener, InternalMovementsAdapter.ItemClickListener {

    View view;
    RecyclerView recyclerInternal;
    InternalMovementsAdapter internalMovementsAdapter;
    Activity activity;
    private RelativeLayout relBack, noDataLayout;
    private AppCompatButton btnNewMovement;
    Intent gcm_rec;
    public ProgressDialog prd;
    private String TAG = "InternalMovementsFragment";
    private ApiUrlList apiService;
    private ArrayList<InternalMovementListModel.Data> arrayList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = getActivity();
        DrawerActivity.MAIN_FRAGMENT="MAIN_FRAGMENT";
        view = inflater.inflate(R.layout.internal_movements_layout, container, false);
        createDialog1();

        apiService =
                MyApplicationClass.getClient().create(ApiUrlList.class);

        recyclerInternal = view.findViewById(R.id.recyclerInternal);
        btnNewMovement = view.findViewById(R.id.btnNewMovement);
        relBack = view.findViewById(R.id.relBack);
        noDataLayout = view.findViewById(R.id.noDataLayout);

        relBack.setOnClickListener(this);
        btnNewMovement.setOnClickListener(this);

        internalMovementsAdapter = new InternalMovementsAdapter(activity,arrayList);
        recyclerInternal.setHasFixedSize(true);
        recyclerInternal.setLayoutManager(new LinearLayoutManager(activity));
        recyclerInternal.setAdapter(internalMovementsAdapter);
        internalMovementsAdapter.setClickListener(this);

        if(AppUtil.isOnline(getActivity())){
            getInternalMovementList();
        }else {
            AppUtil.showToastFail(activity, getResources().getString(R.string.no_internet));
        }

        return view;
    }

    private void getInternalMovementList() {
        try {
            showDialog();
            Call<InternalMovementListModel> call = apiService.getInternalMovementsListApi(getResources().getString(R.string.api_key));
            Log.e(TAG, "get internal movement list request URL " + call.request().url());
            call.enqueue(new Callback<InternalMovementListModel>() {
                @Override
                public void onResponse(Call<InternalMovementListModel> call, Response<InternalMovementListModel> response) {
                    hideDialog();
                    if (response.isSuccessful()){
                        if(response.body().getStatus()==1){
                            recyclerInternal.setVisibility(View.VISIBLE);
                            noDataLayout.setVisibility(View.GONE);
                            arrayList.clear();

                            for (int i=0;i<response.body().getData().size();i++){
                                arrayList.add(response.body().getData().get(i));
                            }

                            internalMovementsAdapter.updateList(arrayList);

                            // For animation
                            int resId = R.anim.layout_animation_fall_down;
                            LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(activity, resId);
                            recyclerInternal.setLayoutAnimation(animation);

                        }else {
                            recyclerInternal.setVisibility(View.GONE);
                            noDataLayout.setVisibility(View.VISIBLE);
                            AppUtil.showToastFail(activity, response.body().getMessage());
                        }
                    }else {
                        Log.d(TAG, "get internal movement list Response fail " + new Gson().toJson(response));
                        AppUtil.showToastFail(activity, getResources().getString(R.string.something_wrong));
                    }
                }

                @Override
                public void onFailure(Call<InternalMovementListModel> call, Throwable t) {
                    hideDialog();
                    Log.d(TAG, "get internal movement list Request fail " + t.getMessage());
                    AppUtil.showToastFail(activity, getResources().getString(R.string.something_wrong));
                }
            });
        }catch (Exception e){
            hideDialog();
            Log.d(TAG, "Exception get internal movement list " + e.getMessage());
            AppUtil.showToastFail(activity, activity.getResources().getString(R.string.something_wrong));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.relBack:
                getFragmentManager().popBackStack();
                break;

            case R.id.btnNewMovement:
                DrawerActivity.backPressStatus = 2;
                loadFragment(new NewInternalMovementFragment_test());
                break;
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_content, fragment);
        fragmentTransaction.addToBackStack("NewInternalMovement");
        fragmentTransaction.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.gcm_rec = new Intent(getResources().getString(R.string.internal_movements));
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

    @Override
    public void onClick(View view, int position) {
        DrawerActivity.backPressStatus = 2;
        loadFragment(new InternalMovementItemsFragment(arrayList.get(position).getMovementId(),arrayList.get(position).getDate(),arrayList.get(position).getTime()
                ,arrayList.get(position).getFromCrateName(),arrayList.get(position).getToCrateName()));
    }
}
