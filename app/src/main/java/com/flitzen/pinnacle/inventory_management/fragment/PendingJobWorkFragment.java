package com.flitzen.pinnacle.inventory_management.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flitzen.pinnacle.R;
import com.flitzen.pinnacle.activity.DrawerActivity;
import com.flitzen.pinnacle.inventory_management.adapter.JobWorkAdapter;

import java.util.ArrayList;

public class PendingJobWorkFragment extends Fragment implements JobWorkAdapter.ItemClickListener, View.OnClickListener {

    // Delete static code

    View view;
    RecyclerView recyclerWork;
    JobWorkAdapter jobWorkAdapter;
    Activity activity;
    TextView txtTitle;
    String title;
    private RelativeLayout relBack, noDataLayout;
    public ProgressDialog prd;
    private int jobWorkStatusId;


    //Only for testing
    ArrayList<jobWorkTest> jobWorkTests=new ArrayList<>();

    public PendingJobWorkFragment(String title, int jobWorkStatusId) {
        this.title = title;
        this.jobWorkStatusId = jobWorkStatusId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = getActivity();
        DrawerActivity.MAIN_FRAGMENT="SUB_FRAGMENT";
        view = inflater.inflate(R.layout.job_work_pending_layout, container, false);
        createDialog1();
        recyclerWork = view.findViewById(R.id.recyclerWork);
        txtTitle = view.findViewById(R.id.txtTitle);
        relBack = view.findViewById(R.id.relBack);
        noDataLayout = view.findViewById(R.id.noDataLayout);

        relBack.setOnClickListener(this);

        txtTitle.setText(title);


        //Only for testing

        if(jobWorkStatusId==1){
           // jobWorkTests.add(new jobWorkTest("10 Feb 2021","11:20 AM","103",""))
        }else if(jobWorkStatusId==2){

        }else if(jobWorkStatusId==3){

        }else if(jobWorkStatusId==4){

        }

        jobWorkAdapter = new JobWorkAdapter(activity);
        recyclerWork.setHasFixedSize(true);
        recyclerWork.setLayoutManager(new LinearLayoutManager(activity));
        recyclerWork.setAdapter(jobWorkAdapter);
        jobWorkAdapter.setClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view, int position) {
        DrawerActivity.backPressStatus = 2;
        loadFragment(new SpecificJobWorkListFragment());
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_content, fragment);
        fragmentTransaction.addToBackStack("SubJobWork");
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.relBack:
                getFragmentManager().popBackStack();
                break;
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



    //Only for testing

    public class jobWorkTest{
        String date;
        String time;
        String jobNo;
        String vendorName;
        String address;
        ArrayList<ItemName> itemNameArrayList;

        public jobWorkTest(String date, String time, String jobNo, String vendorName, String address, ArrayList<ItemName> itemNameArrayList) {
            this.date = date;
            this.time = time;
            this.jobNo = jobNo;
            this.vendorName = vendorName;
            this.address = address;
            this.itemNameArrayList = itemNameArrayList;
        }


        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getJobNo() {
            return jobNo;
        }

        public void setJobNo(String jobNo) {
            this.jobNo = jobNo;
        }

        public String getVendorName() {
            return vendorName;
        }

        public void setVendorName(String vendorName) {
            this.vendorName = vendorName;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public ArrayList<ItemName> getItemNameArrayList() {
            return itemNameArrayList;
        }

        public void setItemNameArrayList(ArrayList<ItemName> itemNameArrayList) {
            this.itemNameArrayList = itemNameArrayList;
        }
    }

    public class ItemName{
        String itemName;

        public ItemName(String itemName) {
            this.itemName = itemName;
        }

        public String getItemName() {
            return itemName;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }
    }

}
