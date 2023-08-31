package com.flitzen.pinnacle.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.flitzen.pinnacle.MyApplicationClass;
import com.flitzen.pinnacle.R;
import com.flitzen.pinnacle.inventory_management.model.LoginModel;
import com.flitzen.pinnacle.inventory_management.model.LoginModel.*;
import com.flitzen.pinnacle.utils.ApiUrlList;
import com.flitzen.pinnacle.utils.AppUtil;
import com.flitzen.pinnacle.utils.SharePref;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    public ProgressDialog prd;
    private ApiUrlList apiService;
    SharedPreferences sharedPreferences;
    private String TAG = "LoginActivity";

    @BindView(R.id.edtUserName)
    EditText edtUserName;
    @BindView(R.id.edtPassword)
    EditText edtPassword;
    @BindView(R.id.btnLogin)
    AppCompatButton btnLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(LoginActivity.this);
        sharedPreferences = SharePref.getSharePref(LoginActivity.this);
        apiService =
                MyApplicationClass.getClient().create(ApiUrlList.class);
        createDialog1();

        btnLogin.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                if (checkValidation() == true) {
                    if (AppUtil.isOnline(LoginActivity.this)) {
                        loginApi();
                    } else {
                        AppUtil.showToastFail(LoginActivity.this, getResources().getString(R.string.no_internet));
                    }
                }
                break;
        }
    }

    private boolean checkValidation() {
        if (edtUserName.getText().toString().trim().isEmpty()) {
            AppUtil.showToastGeneral(LoginActivity.this,getString(R.string.enter_username),R.color.red_light);
            edtUserName.requestFocus();
            return false;
        } else if (!edtUserName.getText().toString().trim().matches(AppUtil.emailPattern)) {
            AppUtil.showToastGeneral(LoginActivity.this,getString(R.string.valide_username),R.color.red_light);
            edtUserName.requestFocus();
            return false;
        } else if (edtPassword.getText().toString().trim().isEmpty()) {
            AppUtil.showToastGeneral(LoginActivity.this,getString(R.string.enter_password),R.color.red_light);
            edtPassword.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    private void loginApi() {
        try {
            showDialog();

            Call<LoginModel> call = apiService.loginApi(getResources().getString(R.string.api_key), edtUserName.getText().toString().trim(), edtPassword.getText().toString().trim());
            Log.d(TAG, "login Request  " + call.request().body());
            call.enqueue(new Callback<LoginModel>() {
                @Override
                public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                    hideDialog();
                    if (response != null) {
                        if (response.isSuccessful()) {
                            Log.d(TAG, "login Response success " + new Gson().toJson(response));
                            if (response.body().getStatus() == 1) {
                                AppUtil.showToastSuccess(LoginActivity.this, response.body().getMessage());

                                Data data = response.body().getData();
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean(SharePref.isLoggedIn, true);
                                // editor.putString(SharePref.userName, edtUserName.getText().toString());
                                editor.putString(SharePref.userId, data.getID());
                                editor.putString(SharePref.password, data.getPASSWORD());
                                editor.putString(SharePref.userRoleId, data.getUSERSROLESID());
                                editor.putString(SharePref.userName, data.getNAME());
                                editor.putString(SharePref.userEmail, data.getEMAIL());
                                editor.putString(SharePref.userMobile, data.getPHONENUMBER());
                                editor.putString(SharePref.userRole, data.getUSERROLES());
                                editor.commit();

                                startActivity(new Intent(LoginActivity.this, PinActivity.class));
                                finish();
                                overridePendingTransition(0, 0);


                            } else {
                                AppUtil.showToastFail(LoginActivity.this, response.body().getMessage());
                            }
                        } else {
                            Log.d(TAG, "login Response fail " + new Gson().toJson(response));
                            AppUtil.showToastFail(LoginActivity.this, getResources().getString(R.string.something_wrong));
                        }
                    } else {
                        AppUtil.showToastFail(LoginActivity.this, getResources().getString(R.string.something_wrong));
                    }
                }

                @Override
                public void onFailure(Call<LoginModel> call, Throwable t) {
                    hideDialog();
                    Log.d(TAG, "login Request fail " + t.getMessage());
                    AppUtil.showToastFail(LoginActivity.this, getResources().getString(R.string.something_wrong));
                }
            });
        } catch (Exception e) {
            hideDialog();
            Log.d(TAG, "Exception loginApi " + e.getMessage());
            AppUtil.showToastFail(LoginActivity.this, getResources().getString(R.string.something_wrong));
        }
    }

    private void createDialog1() {
        prd = new ProgressDialog(LoginActivity.this, R.style.MyAlertDialogStyle);
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