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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.flitzen.pinnacle.MyApplicationClass;
import com.flitzen.pinnacle.R;
import com.flitzen.pinnacle.activity.DrawerActivity;
import com.flitzen.pinnacle.inventory_management.model.JobWorkStatusCount;
import com.flitzen.pinnacle.inventory_management.model.PendingOrderListModel;
import com.flitzen.pinnacle.utils.ApiUrlList;
import com.flitzen.pinnacle.utils.AppUtil;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JobWorksFragment extends Fragment implements View.OnClickListener {

    // Delete static code


    View view;
    CardView cardPending, cardInProgress, cardDelivered, cardCompleted;
    private RelativeLayout relBack;
    Intent gcm_rec;
    Activity activity;
    public ProgressDialog prd;
    private String TAG = "JobWorksFragment";
    private ApiUrlList apiService;
    private AppCompatButton btnDelayed,btnPending,btnInProgress,btnCompleted;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = getActivity();
        DrawerActivity.MAIN_FRAGMENT = "MAIN_FRAGMENT";
        view = inflater.inflate(R.layout.job_work_layout, container, false);
        createDialog1();

        apiService =
                MyApplicationClass.getClient().create(ApiUrlList.class);

        cardPending = view.findViewById(R.id.cardPending);
        cardInProgress = view.findViewById(R.id.cardInProgress);
        cardDelivered = view.findViewById(R.id.cardDelivered);
        cardCompleted = view.findViewById(R.id.cardCompleted);
        relBack = view.findViewById(R.id.relBack);
        btnDelayed = view.findViewById(R.id.btnDelayed);
        btnPending = view.findViewById(R.id.btnPending);
        btnInProgress = view.findViewById(R.id.btnInProgress);
        btnCompleted = view.findViewById(R.id.btnCompleted);

        relBack.setOnClickListener(this);
        cardPending.setOnClickListener(this);
        cardInProgress.setOnClickListener(this);
        cardDelivered.setOnClickListener(this);
        cardCompleted.setOnClickListener(this);

        if (AppUtil.isOnline(getActivity())) {
            getJobWorkStatus();
        } else {
            AppUtil.showToastFail(activity, getResources().getString(R.string.no_internet));
        }

        return view;
    }

    private void getJobWorkStatus() {
        try {
            Call<JobWorkStatusCount> call = apiService.getJobWorkStatusCountApi(getResources().getString(R.string.api_key));
            call.enqueue(new Callback<JobWorkStatusCount>() {
                @Override
                public void onResponse(Call<JobWorkStatusCount> call, Response<JobWorkStatusCount> response) {
                    hideDialog();
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == 1) {
                            btnPending.setText(response.body().getPenndingTotal());
                            btnInProgress.setText(response.body().getInProgressTotal());
                            btnDelayed.setText(response.body().getInDelayTotal());
                            btnCompleted.setText(response.body().getCompleteTotal());
                        } else {
                            btnPending.setText("0");
                            btnInProgress.setText("0");
                            btnDelayed.setText("0");
                            btnCompleted.setText("0");
                        }
                    } else {
                        Log.d(TAG, "Job work status count Response fail " + new Gson().toJson(response));
                        AppUtil.showToastFail(activity, getResources().getString(R.string.something_wrong));
                    }
                }

                @Override
                public void onFailure(Call<JobWorkStatusCount> call, Throwable t) {
                    hideDialog();
                    Log.d(TAG, "Job work status count Request fail " + t.getMessage());
                    AppUtil.showToastFail(activity, getResources().getString(R.string.something_wrong));
                }
            });
        } catch (Exception e) {
            hideDialog();
            Log.d(TAG, "Exception Job work status count " + e.getMessage());
            AppUtil.showToastFail(activity, activity.getResources().getString(R.string.something_wrong));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cardPending:
                DrawerActivity.backPressStatus = 2;
                loadFragment(new PendingJobWorkFragment(getActivity().getResources().getString(R.string.pending_job_work), 1));
                break;

            case R.id.cardInProgress:
                DrawerActivity.backPressStatus = 2;
                loadFragment(new PendingJobWorkFragment(getActivity().getResources().getString(R.string.inprogress_job_work), 2));
                break;

            case R.id.cardDelivered:
                DrawerActivity.backPressStatus = 2;
                loadFragment(new PendingJobWorkFragment(getActivity().getResources().getString(R.string.delayed_job_work), 3));
                break;

            case R.id.cardCompleted:
                DrawerActivity.backPressStatus = 2;
                loadFragment(new PendingJobWorkFragment(getActivity().getResources().getString(R.string.completed_job_work), 4));
                break;

            case R.id.relBack:
                getFragmentManager().popBackStack();
                break;
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_content, fragment);
        fragmentTransaction.addToBackStack("MainJobWork");
        fragmentTransaction.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.gcm_rec = new Intent(getResources().getString(R.string.job_works));
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
