package com.flitzen.pinnacle.inventory_management.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flitzen.pinnacle.MyApplicationClass;
import com.flitzen.pinnacle.R;
import com.flitzen.pinnacle.inventory_management.adapter.AddItemsPartsAdapter;
import com.flitzen.pinnacle.inventory_management.adapter.BOMListAdapter;
import com.flitzen.pinnacle.inventory_management.model.CratesListModel;
import com.flitzen.pinnacle.inventory_management.adapter.ItemTypeAdapter;
import com.flitzen.pinnacle.inventory_management.model.CommonModel;
import com.flitzen.pinnacle.inventory_management.model.ItemTypeModel;
import com.flitzen.pinnacle.inventory_management.model.PendingOrderBOMListModel;
import com.flitzen.pinnacle.utils.ApiUrlList;
import com.flitzen.pinnacle.utils.AppUtil;
import com.flitzen.pinnacle.utils.DecimalDigitsInputFilter;
import com.flitzen.pinnacle.utils.InputFilterMinMax;
import com.flitzen.pinnacle.utils.SharePref;
import com.google.gson.Gson;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BOMFragment extends Fragment implements View.OnClickListener, BOMListAdapter.ItemClickListener, ItemTypeAdapter.ItemClickListenerItem, AddItemsPartsAdapter.ItemClickListener {

    View view;
    private RecyclerView recyclerItemType, recyclerBOMList;
    private BOMListAdapter bomListAdapter;
    private ItemTypeAdapter itemTypeAdapter;
    private AddItemsPartsAdapter addItemsPartsAdapter;
    private RelativeLayout relBack, noDataLayout;
    Activity activity;
    public ProgressDialog prd;
    private ArrayList<ItemTypeModel> itemTypeNameArray = new ArrayList<>();
    private String orderId, machineId, crateID, isDecimal = "-1";
    private String TAG = "BOMFragment";
    private ApiUrlList apiService;
    private TextView txtDashboardTitle, txtDate, txtTime, txtItemName, txtCrateName;
    private LinearLayout lin;
    private ImageView imgMinus, imgPlus;
    private Float crateDeliverQty = 0.00f;
    private Float balancedQty = 0.00f;
    private Float availableQtyINCrate = 0.00f;
    private AlertDialog alertDialog;
    private EditText txtDeliveredQty;
    private SharedPreferences sharedPreferences;
    private boolean isCrateSelect = false;
    BigDecimal a = new BigDecimal("123.13698");
    BigDecimal roundOff = a.setScale(2, BigDecimal.ROUND_HALF_EVEN);
    DecimalFormat df = new DecimalFormat("0.00");

    private ArrayList<PendingOrderBOMListModel.Data> pendingOrderBOMListArr = new ArrayList<>();
    private ArrayList<CratesListModel.Data> cratesList = new ArrayList<>();

    public BOMFragment(String orderId, String machineId) {
        this.orderId = orderId;
        this.machineId = machineId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = getActivity();
        view = inflater.inflate(R.layout.order_item_bom_list_layout, container, false);
        createDialog1();
        recyclerItemType = view.findViewById(R.id.recyclerItemType);
        recyclerBOMList = view.findViewById(R.id.recyclerBOMList);
        relBack = view.findViewById(R.id.relBack);
        noDataLayout = view.findViewById(R.id.noDataLayout);
        txtDashboardTitle = view.findViewById(R.id.txtDashboardTitle);
        txtDate = view.findViewById(R.id.txtDate);
        txtTime = view.findViewById(R.id.txtTime);
        txtItemName = view.findViewById(R.id.txtItemName);

        relBack.setOnClickListener(this);

        bomListAdapter = new BOMListAdapter(activity, pendingOrderBOMListArr);
        recyclerBOMList.setHasFixedSize(true);
        recyclerBOMList.setLayoutManager(new LinearLayoutManager(activity));
        recyclerBOMList.setAdapter(bomListAdapter);
        bomListAdapter.setClickListener(this);

        itemTypeAdapter = new ItemTypeAdapter(activity, itemTypeNameArray);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        recyclerItemType.setLayoutManager(layoutManager);
        recyclerItemType.setAdapter(itemTypeAdapter);
        recyclerItemType.setItemViewCacheSize(100);
        itemTypeAdapter.setClickListenerItem(this);

        apiService =
                MyApplicationClass.getClient().create(ApiUrlList.class);
        sharedPreferences = SharePref.getSharePref(getActivity());

        if (AppUtil.isOnline(activity)) {
            getPendingOrderBOMListAPI();
        } else {
            AppUtil.showToastFail(activity, getResources().getString(R.string.no_internet));
        }

        return view;
    }

    private void getPendingOrderBOMListAPI() {
        try {
            showDialog();
            Call<PendingOrderBOMListModel> call = apiService.pendingOrderBOMListApi(getResources().getString(R.string.api_key), orderId, machineId);
            Log.e(TAG, "pending order BOM list request URL " + call.request().url());
            call.enqueue(new Callback<PendingOrderBOMListModel>() {
                @Override
                public void onResponse(Call<PendingOrderBOMListModel> call, Response<PendingOrderBOMListModel> response) {
                    hideDialog();
                    if (response.isSuccessful()) {
                        Log.d(TAG, "pending order BOM list Response success " + new Gson().toJson(response));
                        if (response.body().getStatus() == 1) {

                            recyclerBOMList.setVisibility(View.VISIBLE);
                            noDataLayout.setVisibility(View.GONE);
                            pendingOrderBOMListArr.clear();
                            itemTypeNameArray.clear();

                            for (int i = 0; i < response.body().getData().size(); i++) {
                                if (response.body().getData().get(i).getData() != null) {
                                    itemTypeNameArray.add(new ItemTypeModel(response.body().getData().get(i).getCategory(), response.body().getData().get(i).getData().size()));
                                    pendingOrderBOMListArr.add(response.body().getData().get(i));

                                } else {
                                    itemTypeNameArray.add(new ItemTypeModel(response.body().getData().get(i).getCategory(), 0));
                                }
                            }

                            // For animation
                            int resId = R.anim.layout_animation_fall_down;
                            LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(activity, resId);
                            recyclerBOMList.setLayoutAnimation(animation);

                            bomListAdapter.notifyDataSetChanged();
                            itemTypeAdapter.notifyDataSetChanged();

                            SimpleDateFormat input = new SimpleDateFormat("dd MMMM,yyyy");
                            SimpleDateFormat output = new SimpleDateFormat("dd MMM yyyy");
                            try {
                                Date oneWayTripDate;
                                oneWayTripDate = input.parse(response.body().getOrderDate());
                                txtDate.setText(output.format(oneWayTripDate));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            txtDashboardTitle.setText("Order - " + response.body().getOrderNo());
                            txtTime.setText(", " + response.body().getOrderTime());
                            txtItemName.setText(response.body().getItemName());

                        } else {
                            recyclerBOMList.setVisibility(View.GONE);
                            noDataLayout.setVisibility(View.VISIBLE);
                            AppUtil.showToastFail(activity, response.body().getMessage());
                        }
                    } else {
                        Log.d(TAG, "pending order BOM list Response fail " + new Gson().toJson(response));
                        AppUtil.showToastFail(activity, getResources().getString(R.string.something_wrong));
                    }
                }

                @Override
                public void onFailure(Call<PendingOrderBOMListModel> call, Throwable t) {
                    hideDialog();
                    Log.d(TAG, "pending order BOM list Request fail " + t.getMessage());
                    AppUtil.showToastFail(activity, getResources().getString(R.string.something_wrong));
                }
            });
        } catch (Exception e) {
            hideDialog();
            Log.d(TAG, "Exception pending order BOM list " + e.getMessage());
            AppUtil.showToastFail(activity, activity.getResources().getString(R.string.something_wrong));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.relBack:
                getFragmentManager().popBackStack();
                break;

            case R.id.imgPlus:
                System.out.println("============balancedQty   " + balancedQty);
                System.out.println("============Float.valueOf(crateDeliverQty)   " + Float.valueOf(crateDeliverQty));

                if (isCrateSelect==true){
                    if ((float) crateDeliverQty < Float.parseFloat(df.format(balancedQty))) {
                        if ((float) crateDeliverQty < Float.parseFloat(df.format(availableQtyINCrate))) {
                            crateDeliverQty = crateDeliverQty + 1;
                            if ((float) crateDeliverQty <= Float.parseFloat(df.format(balancedQty))) {
                                if ((float) crateDeliverQty <= Float.parseFloat(df.format(availableQtyINCrate))) {
                                    txtDeliveredQty.setText(String.valueOf(Float.parseFloat(df.format(crateDeliverQty))));
                                }
                                else {
                                    txtDeliveredQty.setText(String.valueOf(Float.parseFloat(df.format(crateDeliverQty))));
                                    AppUtil.showToastFail(activity, "Crate " + txtCrateName.getText().toString() + "has available quantity is " + String.valueOf(availableQtyINCrate));
                                }
                            }else {
                                AppUtil.showToastFail(activity, "You can not select grater than Balanced Quantity");
                            }

                        } else {
                            txtDeliveredQty.setText(String.valueOf(Float.parseFloat(df.format(crateDeliverQty))));
                            AppUtil.showToastFail(activity, "Crate " + txtCrateName.getText().toString() + "has available quantity is " + String.valueOf(availableQtyINCrate));
                        }
                    } else {
                        txtDeliveredQty.setText(String.valueOf(Float.parseFloat(df.format(crateDeliverQty))));
                        AppUtil.showToastFail(activity, "You can not select grater than Balanced Quantity");
                    }
                }else {
                    AppUtil.showToastFail(activity, "Please select crate.");
                }
                break;

            case R.id.imgMinus:
                if(isCrateSelect==true){
                    if (!txtDeliveredQty.getText().toString().isEmpty()) {
                        if (crateDeliverQty > 0) {
                            crateDeliverQty = crateDeliverQty - 1;
                            txtDeliveredQty.setText(String.valueOf(Float.parseFloat(df.format(crateDeliverQty))));
                        }
                    }
                }else {
                    AppUtil.showToastFail(activity, "Please select crate.");
                }
                break;
        }
    }

    @Override
    public void onClick(View view, int position, int selectedItemPosition) {
        if (view.getId() == R.id.btnAdd) {
            isCrateSelect = false;
            crateDeliverQty = 0.00f;
            isDecimal = "-1";
            PendingOrderBOMListModel.BOMDetails bomDetails = pendingOrderBOMListArr.get(selectedItemPosition).getData().get(position);
            balancedQty = Float.parseFloat(bomDetails.getBomRequireQty()) - Float.parseFloat(bomDetails.getBomDeliverQty());
            isDecimal = bomDetails.getBomIsDecimal();
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            LayoutInflater layoutInflaterAndroid = LayoutInflater.from(activity);
            View view2 = layoutInflaterAndroid.inflate(R.layout.add_items_layout, null);
            builder.setView(view2);
            builder.setCancelable(true);
            alertDialog = builder.create();
            alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            RecyclerView recyclerAddPart = view2.findViewById(R.id.recyclerAddPart);
            CardView btnScan = view2.findViewById(R.id.btnScan);
            TextView txtBtn = view2.findViewById(R.id.txtBtn);
            txtDeliveredQty = view2.findViewById(R.id.txtDeliveredQty);
            txtCrateName = view2.findViewById(R.id.txtCrateName);
            imgMinus = view2.findViewById(R.id.imgMinus);
            imgPlus = view2.findViewById(R.id.imgPlus);
            lin = view2.findViewById(R.id.lin);
            TextView txtPartName = view2.findViewById(R.id.txtPartName);

            txtPartName.setText(bomDetails.getBomName());

            imgMinus.setOnClickListener(this);
            imgPlus.setOnClickListener(this);

            //TODO : If implement barcode scan then remove this 2 line

            btnScan.setCardBackgroundColor(getResources().getColor(R.color.green_box_color));
            txtBtn.setText(getResources().getString(R.string.confirm));

            addItemsPartsAdapter = new AddItemsPartsAdapter(activity, cratesList);
            recyclerAddPart.setHasFixedSize(true);
            recyclerAddPart.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
            recyclerAddPart.setAdapter(addItemsPartsAdapter);
            addItemsPartsAdapter.setClickListener(this);


            if (AppUtil.isOnline(activity)) {
                getCrateList(bomDetails.getBom_product_id());
            } else {
                AppUtil.showToastFail(activity, getResources().getString(R.string.no_internet));
            }

            btnScan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //TODO : If implement barcode scan then unComment this line

                    //txtBtn.setText(getResources().getString(R.string.confirm));
                    //  btnScan.setCardBackgroundColor(getResources().getColor(R.color.green_box_color));
                    //  lin.setVisibility(View.VISIBLE);

                    if (crateDeliverQty == 0) {
                        AppUtil.showToastFail(activity, "You can not select 0 delivered Quantity.");
                    } else {
                        if (AppUtil.isOnline(activity)) {
                            if ((float) crateDeliverQty <= balancedQty) {
                                if ((float) crateDeliverQty <= availableQtyINCrate) {
                                    addBOM(bomDetails.getBomId(), bomDetails.getBom_product_id());
                                } else {
                                    AppUtil.showToastFail(activity, "Crate " + txtCrateName.getText().toString() + "has available quantity is " + String.valueOf(availableQtyINCrate));
                                }
                            } else {
                                AppUtil.showToastFail(activity, "You can not select grater than Balanced Quantity");
                            }
                        } else {
                            AppUtil.showToastFail(activity, getResources().getString(R.string.no_internet));
                        }
                    }
                }
            });
        }
    }

    private void addBOM(String bomId, String bom_product_id) {
        try {
            showDialog();
            Call<CommonModel> call = apiService.addBOMApi(getResources().getString(R.string.api_key), orderId, machineId, bomId, bom_product_id, sharedPreferences.getString(SharePref.userId, ""),
                    String.valueOf(crateDeliverQty), crateID);
            Log.e(TAG, "Crates list request URL " + call.request().url());
            call.enqueue(new Callback<CommonModel>() {
                @Override
                public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                    hideDialog();
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == 1) {
                            AppUtil.showToastSuccess(activity, response.body().getMessage());
                            alertDialog.dismiss();
                            if (AppUtil.isOnline(activity)) {
                                getPendingOrderBOMListAPI();
                            } else {
                                AppUtil.showToastFail(activity, getResources().getString(R.string.no_internet));
                            }

                        } else {
                            AppUtil.showToastFail(activity, response.body().getMessage());
                        }
                    } else {
                        Log.d(TAG, "Add BOM Response fail " + new Gson().toJson(response));
                        AppUtil.showToastFail(activity, getResources().getString(R.string.something_wrong));
                    }
                }

                @Override
                public void onFailure(Call<CommonModel> call, Throwable t) {
                    hideDialog();
                    Log.d(TAG, "Add BOM  Request fail " + t.getMessage());
                    AppUtil.showToastFail(activity, getResources().getString(R.string.something_wrong));
                }
            });

        } catch (Exception e) {
            hideDialog();
            Log.d(TAG, "Exception add BOM " + e.getMessage());
            AppUtil.showToastFail(activity, activity.getResources().getString(R.string.something_wrong));
        }
    }

    @Override
    public void onClickCrate(View view, int position) {
        isCrateSelect = true;
        crateDeliverQty = 0.00f;
        if (isDecimal.equalsIgnoreCase("0")) {
            txtDeliveredQty.setFilters(new InputFilter[]{new InputFilterMinMax("0", String.valueOf(cratesList.get(position).getTotalQty())), new DecimalDigitsInputFilter(5, 2),
                    new InputFilterMinMax("0", String.valueOf(balancedQty))});
            txtDeliveredQty.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        } else if (isDecimal.equalsIgnoreCase("1")) {
            txtDeliveredQty.setFilters(new InputFilter[]{new InputFilterMinMax("0", String.valueOf(cratesList.get(position).getTotalQty()))
                    ,new InputFilterMinMax("0", String.valueOf(balancedQty))});
            txtDeliveredQty.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
        }

        txtDeliveredQty.setText(String.valueOf(crateDeliverQty));
        lin.setVisibility(View.VISIBLE);
        txtCrateName.setText(cratesList.get(position).getCrateName());
        availableQtyINCrate = cratesList.get(position).getTotalQty();
        crateID = cratesList.get(position).getCrateId();

        txtDeliveredQty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty()) {
                    crateDeliverQty = 0.00f;
                } else {
                    try {
                        crateDeliverQty = Float.parseFloat(s.toString());
                    }catch (NumberFormatException e){

                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        System.out.println("===============availableQtyINCrate      " + availableQtyINCrate);
        addItemsPartsAdapter.updateUI(position);
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
                            addItemsPartsAdapter.notifyDataSetChanged();
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

    @Override
    public void onClickItem(View view, int position) {
        view.setBackgroundColor(getResources().getColor(R.color.login_bg_color));
        itemTypeAdapter.updateUI(position);
        bomListAdapter.updateUI(position);
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
