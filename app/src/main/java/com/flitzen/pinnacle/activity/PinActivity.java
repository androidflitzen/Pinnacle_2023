package com.flitzen.pinnacle.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.flitzen.pinnacle.MyApplicationClass;
import com.flitzen.pinnacle.R;
import com.flitzen.pinnacle.inventory_management.model.CommonModel;
import com.flitzen.pinnacle.utils.ApiUrlList;
import com.flitzen.pinnacle.utils.AppUtil;
import com.flitzen.pinnacle.utils.GenericKeyEvent;
import com.flitzen.pinnacle.utils.GenericTextWatcher;
import com.flitzen.pinnacle.utils.SharePref;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PinActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.edtPin1)
    EditText edtPin1;
    @BindView(R.id.edtPin2)
    EditText edtPin2;
    @BindView(R.id.edtPin3)
    EditText edtPin3;
    @BindView(R.id.edtPin4)
    EditText edtPin4;
    @BindView(R.id.btnSubmit)
    AppCompatButton btnSubmit;
    @BindView(R.id.rel1)
    RelativeLayout rel1;
    @BindView(R.id.rel2)
    RelativeLayout rel2;
    @BindView(R.id.rel3)
    RelativeLayout rel3;
    @BindView(R.id.rel4)
    RelativeLayout rel4;

    private ApiUrlList apiService;
    SharedPreferences sharedPreferences;
    private String TAG = "PinActivity";
    public ProgressDialog prd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);
        ButterKnife.bind(PinActivity.this);

        sharedPreferences = SharePref.getSharePref(PinActivity.this);
        apiService =
                MyApplicationClass.getClient().create(ApiUrlList.class);
        createDialog1();

        edtPin1.requestFocus();

        edtPin1.addTextChangedListener(new GenericTextWatcher(edtPin1, edtPin2));
        edtPin2.addTextChangedListener(new GenericTextWatcher(edtPin2, edtPin3));
        edtPin3.addTextChangedListener(new GenericTextWatcher(edtPin3, edtPin4));
        edtPin4.addTextChangedListener(new GenericTextWatcher(edtPin4, null));

        edtPin1.setOnKeyListener(new GenericKeyEvent(edtPin1, null));
        edtPin2.setOnKeyListener(new GenericKeyEvent(edtPin2, edtPin1));
        edtPin3.setOnKeyListener(new GenericKeyEvent(edtPin3, edtPin2));
        edtPin4.setOnKeyListener(new GenericKeyEvent(edtPin4, edtPin3));

        edtPin4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnSubmit.setOnClickListener(this);
    }

    private boolean checkValidation() {
        if (edtPin1.getText().toString().trim().isEmpty()) {
            AppUtil.showToastGeneral(PinActivity.this, getString(R.string.enter_pin), R.color.red_light);
            return false;
        } else if (edtPin2.getText().toString().trim().isEmpty()) {
            AppUtil.showToastGeneral(PinActivity.this, getString(R.string.enter_pin), R.color.red_light);
            return false;
        } else if (edtPin3.getText().toString().trim().isEmpty()) {
            AppUtil.showToastGeneral(PinActivity.this, getString(R.string.enter_pin), R.color.red_light);
            return false;
        } else if (edtPin4.getText().toString().trim().isEmpty()) {
            AppUtil.showToastGeneral(PinActivity.this, getString(R.string.enter_pin), R.color.red_light);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSubmit:
                if (checkValidation()) {
                    if (AppUtil.isOnline(PinActivity.this)) {
                        String pin = edtPin1.getText().toString().trim() + edtPin2.getText().toString().trim() + edtPin3.getText().toString().trim() + edtPin4.getText().toString().trim();
                        if (pin != null && pin.length() == 4) {
                            pinApi(pin);
                        }
                    } else {
                        AppUtil.showToastFail(PinActivity.this, getResources().getString(R.string.no_internet));
                    }
                }
                break;
        }
    }

    private void pinApi(String pin) {
        try {

            showDialog();

            Call<CommonModel> call = apiService.pinApi(getResources().getString(R.string.api_key), sharedPreferences.getString(SharePref.userId, ""), pin);
            Log.d(TAG, "Pin Request  " + call.request().body());
            call.enqueue(new Callback<CommonModel>() {
                @Override
                public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                    hideDialog();
                    if (response != null) {
                        if (response.isSuccessful()) {
                            Log.d(TAG, "Pin Response success " + new Gson().toJson(response));
                            if (response.body().getStatus() == 1) {
                               // AppUtil.showToastSuccess(PinActivity.this, response.body().getMessage());

                                startActivity(new Intent(PinActivity.this, DrawerActivity.class));
                               // startActivity(new Intent(PinActivity.this, CustomerDrawerActivity.class));
                                finish();

                            } else {
                                AppUtil.showToastFail(PinActivity.this, response.body().getMessage());
                            }
                        } else {
                            Log.d(TAG, "Pin Response fail " + new Gson().toJson(response));
                            AppUtil.showToastFail(PinActivity.this, getResources().getString(R.string.something_wrong));
                        }
                    } else {
                        AppUtil.showToastFail(PinActivity.this, getResources().getString(R.string.something_wrong));
                    }
                }

                @Override
                public void onFailure(Call<CommonModel> call, Throwable t) {
                    hideDialog();
                    Log.d(TAG, "Pin Request fail " + t.getMessage());
                    AppUtil.showToastFail(PinActivity.this, getResources().getString(R.string.something_wrong));
                }
            });

        } catch (Exception e) {
            hideDialog();
            Log.d(TAG, "Exception PinApi " + e.getMessage());
            AppUtil.showToastFail(PinActivity.this, getResources().getString(R.string.something_wrong));
        }
    }

    private void createDialog1() {
        prd = new ProgressDialog(PinActivity.this, R.style.MyAlertDialogStyle);
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