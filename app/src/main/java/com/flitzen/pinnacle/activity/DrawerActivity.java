package com.flitzen.pinnacle.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flitzen.pinnacle.MyApplicationClass;
import com.flitzen.pinnacle.R;
import com.flitzen.pinnacle.inventory_management.fragment.DashBoardFragment;
import com.flitzen.pinnacle.inventory_management.fragment.DeliveredOrdersFragment;
import com.flitzen.pinnacle.inventory_management.fragment.InternalMovementsFragment;
import com.flitzen.pinnacle.inventory_management.fragment.JobWorksFragment;
import com.flitzen.pinnacle.inventory_management.fragment.PendingOrdersFragment;
import com.flitzen.pinnacle.inventory_management.fragment.PurchaseOrdersFragment;
import com.flitzen.pinnacle.inventory_management.fragment.StockAlertsFragment;
import com.flitzen.pinnacle.inventory_management.fragment.StockCheckFragment;
import com.flitzen.pinnacle.inventory_management.model.CommonModel;

import com.flitzen.pinnacle.utils.ApiUrlList;
import com.flitzen.pinnacle.utils.AppUtil;
import com.flitzen.pinnacle.utils.CircleImageView;
import com.flitzen.pinnacle.utils.Permission;
import com.flitzen.pinnacle.utils.SharePref;
import com.flitzen.pinnacle.utils.SlidingPaneLayout;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DrawerActivity extends AppCompatActivity implements SlidingPaneLayout.PanelSlideListener, View.OnClickListener {

    public Fragment currentFragment;
    boolean doubleBackToExitPressedOnce = false;
    private BroadcastReceiver mMyBroadcastReceiver;
    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    private String[] permissions1 = {Manifest.permission.CAMERA};
    String pictureFilePath;
    private ApiUrlList apiService;
    private SharedPreferences sharedPreferences;
    private String TAG = "DrawerActivity";
    public ProgressDialog prd;
    public static int backPressStatus = 0;
    public static String MAIN_FRAGMENT = "MAIN_FRAGMENT";

    @BindView(R.id.fragment_master)
    View fragment_master;
    @BindView(R.id.fragment_content)
    FrameLayout fragment_content;
    @BindView(R.id.slide)
    SlidingPaneLayout slidingPaneLayout;
    @BindView(R.id.linHome)
    LinearLayout linHome;
    @BindView(R.id.linPendingOrder)
    LinearLayout linPendingOrder;
    @BindView(R.id.linDeliveredOrder)
    LinearLayout linDeliveredOrder;
    @BindView(R.id.linStockCheck)
    LinearLayout linStockCheck;
    @BindView(R.id.linStockAlerts)
    LinearLayout linStockAlerts;
    @BindView(R.id.linPurchaseOrder)
    LinearLayout linPurchaseOrder;
    @BindView(R.id.linInternalMovements)
    LinearLayout linInternalMovements;
    @BindView(R.id.linJobWork)
    LinearLayout linJobWork;
    @BindView(R.id.linLogout)
    LinearLayout linLogout;
    @BindView(R.id.linHome1)
    LinearLayout linHome1;
    @BindView(R.id.linPendingOrder1)
    LinearLayout linPendingOrder1;
    @BindView(R.id.linDeliveredOrder1)
    LinearLayout linDeliveredOrder1;
    @BindView(R.id.linStockCheck1)
    LinearLayout linStockCheck1;
    @BindView(R.id.linStockAlerts1)
    LinearLayout linStockAlerts1;
    @BindView(R.id.linPurchaseOrder1)
    LinearLayout linPurchaseOrder1;
    @BindView(R.id.linInternalMovements1)
    LinearLayout linInternalMovements1;
    @BindView(R.id.linJobWork1)
    LinearLayout linJobWork1;
    @BindView(R.id.linLogout1)
    LinearLayout linLogout1;
    @BindView(R.id.linHeader)
    LinearLayout linHeader;
    @BindView(R.id.linCloseDrawer)
    LinearLayout linCloseDrawer;
    @BindView(R.id.linOpenDrawer)
    LinearLayout linOpenDrawer;

    @BindView(R.id.imgMenuTop)
    ImageView imgMenuTop;
    @BindView(R.id.imgMenuBottom)
    ImageView imgMenuBottom;
    @BindView(R.id.imgMenuBottom1)
    ImageView imgMenuBottom1;
    @BindView(R.id.txtPendingOrder)
    TextView txtPendingOrder;
    @BindView(R.id.txtDeliverdOrder)
    TextView txtDeliverdOrder;
    @BindView(R.id.txtStockCheck)
    TextView txtStockCheck;
    @BindView(R.id.txtStockAlerts)
    TextView txtStockAlerts;
    @BindView(R.id.txtPurchaseOrder)
    TextView txtPurchaseOrder;
    @BindView(R.id.txtInternalMovements)
    TextView txtInternalMovements;
    @BindView(R.id.txtJobWork)
    TextView txtJobWork;
    @BindView(R.id.txtHome)
    TextView txtHome;
    @BindView(R.id.txtHome1)
    TextView txtHome1;
    @BindView(R.id.txtPendingOrder1)
    TextView txtPendingOrder1;
    @BindView(R.id.txtDeliverdOrder1)
    TextView txtDeliverdOrder1;
    @BindView(R.id.txtStockCheck1)
    TextView txtStockCheck1;
    @BindView(R.id.txtStockAlerts1)
    TextView txtStockAlerts1;
    @BindView(R.id.txtPurchaseOrder1)
    TextView txtPurchaseOrder1;
    @BindView(R.id.txtInternalMovements1)
    TextView txtInternalMovements1;
    @BindView(R.id.txtJobWork1)
    TextView txtJobWork1;
    @BindView(R.id.txtUserRole)
    TextView txtUserRole;
    @BindView(R.id.txtUserName)
    TextView txtUserName;

    @BindView(R.id.imgHome)
    ImageView imgHome;
    @BindView(R.id.imgHome1)
    ImageView imgHome1;
    @BindView(R.id.imgPending)
    ImageView imgPending;
    @BindView(R.id.imgPending1)
    ImageView imgPending1;
    @BindView(R.id.imgDelivered)
    ImageView imgDelivered;
    @BindView(R.id.imgDelivered1)
    ImageView imgDelivered1;
    @BindView(R.id.imgStockCheck)
    ImageView imgStockCheck;
    @BindView(R.id.imgStockCheck1)
    ImageView imgStockCheck1;
    @BindView(R.id.imgStockAlerts)
    ImageView imgStockAlerts;
    @BindView(R.id.imgStockAlerts1)
    ImageView imgStockAlerts1;
    @BindView(R.id.imgPurchase)
    ImageView imgPurchase;
    @BindView(R.id.imgPurchase1)
    ImageView imgPurchase1;
    @BindView(R.id.imgInternalMove)
    ImageView imgInternalMove;
    @BindView(R.id.imgInternalMove1)
    ImageView imgInternalMove1;
    @BindView(R.id.imgJobWork)
    ImageView imgJobWork;
    @BindView(R.id.imgJobWork1)
    ImageView imgJobWork1;
    @BindView(R.id.imgProfile)
    CircleImageView imgProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        ButterKnife.bind(this);

        createDialog1();
        sharedPreferences = SharePref.getSharePref(DrawerActivity.this);

        txtUserRole.setText(sharedPreferences.getString(SharePref.userRole, ""));
        txtUserName.setText(sharedPreferences.getString(SharePref.userName, ""));

        // Set clickListener
        linHome.setOnClickListener(this);
        linPendingOrder.setOnClickListener(this);
        linDeliveredOrder.setOnClickListener(this);
        linStockCheck.setOnClickListener(this);
        linStockAlerts.setOnClickListener(this);
        linPurchaseOrder.setOnClickListener(this);
        linInternalMovements.setOnClickListener(this);
        linJobWork.setOnClickListener(this);
        linLogout.setOnClickListener(this);
        linHome1.setOnClickListener(this);
        linPendingOrder1.setOnClickListener(this);
        linDeliveredOrder1.setOnClickListener(this);
        linStockCheck1.setOnClickListener(this);
        linStockAlerts1.setOnClickListener(this);
        linPurchaseOrder1.setOnClickListener(this);
        linInternalMovements1.setOnClickListener(this);
        linJobWork1.setOnClickListener(this);
        linLogout1.setOnClickListener(this);
        imgMenuBottom.setOnClickListener(this);
        imgMenuBottom1.setOnClickListener(this);
        imgMenuTop.setOnClickListener(this);
        imgProfile.setOnClickListener(this);

        slidingPaneLayout.setPanelSlideListener(this);

        currentFragment = new DashBoardFragment();
        loadFragment(currentFragment, getResources().getString(R.string.dashboard));

    }

    @Override
    public void onPanelSlide(@NonNull View panel, float slideOffset) {

    }

    @Override
    public void onPanelOpened(@NonNull View panel) {

        linHome.setOrientation(LinearLayout.HORIZONTAL);
        linPendingOrder.setOrientation(LinearLayout.HORIZONTAL);
        linDeliveredOrder.setOrientation(LinearLayout.HORIZONTAL);
        linStockCheck.setOrientation(LinearLayout.HORIZONTAL);
        linStockAlerts.setOrientation(LinearLayout.HORIZONTAL);
        linPurchaseOrder.setOrientation(LinearLayout.HORIZONTAL);
        linInternalMovements.setOrientation(LinearLayout.HORIZONTAL);
        linJobWork.setOrientation(LinearLayout.HORIZONTAL);
        linLogout.setOrientation(LinearLayout.HORIZONTAL);
        linHeader.setVisibility(View.VISIBLE);
        imgMenuTop.setVisibility(View.VISIBLE);
        imgMenuBottom.setVisibility(View.GONE);
        linCloseDrawer.setVisibility(View.GONE);
        linOpenDrawer.setVisibility(View.VISIBLE);

        txtPendingOrder.setText(getResources().getString(R.string.pending_orders));
        txtDeliverdOrder.setText(getResources().getString(R.string.delivered_orders));
        txtStockCheck.setText(getResources().getString(R.string.stock_check));
        txtStockAlerts.setText(getResources().getString(R.string.stock_alerts));
        txtPurchaseOrder.setText(getResources().getString(R.string.purchase_orders));
        txtInternalMovements.setText(getResources().getString(R.string.internal_movements));
        txtJobWork.setText(getResources().getString(R.string.job_works));
    }

    @Override
    public void onPanelClosed(@NonNull View panel) {

        linHome.setOrientation(LinearLayout.VERTICAL);
        linPendingOrder.setOrientation(LinearLayout.VERTICAL);
        linDeliveredOrder.setOrientation(LinearLayout.VERTICAL);
        linStockCheck.setOrientation(LinearLayout.VERTICAL);
        linStockAlerts.setOrientation(LinearLayout.VERTICAL);
        linPurchaseOrder.setOrientation(LinearLayout.VERTICAL);
        linInternalMovements.setOrientation(LinearLayout.VERTICAL);
        linJobWork.setOrientation(LinearLayout.VERTICAL);
        linLogout.setOrientation(LinearLayout.VERTICAL);
        linHeader.setVisibility(View.GONE);
        imgMenuTop.setVisibility(View.GONE);
        imgMenuBottom.setVisibility(View.VISIBLE);

        linCloseDrawer.setVisibility(View.VISIBLE);
        linOpenDrawer.setVisibility(View.GONE);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.linHome:
                currentFragment = new DashBoardFragment();
                loadFragment(currentFragment, getResources().getString(R.string.dashboard));
                backPressStatus = 0;
                slidingPaneLayout.closePane();
                homeSelection();

                break;

            case R.id.linPendingOrder:
                currentFragment = new PendingOrdersFragment();
                loadFragment(currentFragment, getResources().getString(R.string.pending_orders));
                backPressStatus = 1;
                slidingPaneLayout.closePane();
                pendingSelection();

                break;

            case R.id.linDeliveredOrder:
                currentFragment = new DeliveredOrdersFragment();
                loadFragment(currentFragment, getResources().getString(R.string.delivered_orders));
                backPressStatus = 1;
                slidingPaneLayout.closePane();
                deliveredSelection();

                break;

            case R.id.linJobWork:
                currentFragment = new JobWorksFragment();
                loadFragment(currentFragment, getResources().getString(R.string.job_works));
                backPressStatus = 1;
                slidingPaneLayout.closePane();
                jobWorkSelection();

                break;

            case R.id.linStockCheck:
                currentFragment = new StockCheckFragment();
                loadFragment(currentFragment, getResources().getString(R.string.stock_check));
                backPressStatus = 1;
                slidingPaneLayout.closePane();
                stockCheckSelection();

                break;

            case R.id.linStockAlerts:
                currentFragment = new StockAlertsFragment();
                loadFragment(currentFragment, getResources().getString(R.string.stock_alerts));
                backPressStatus = 1;
                slidingPaneLayout.closePane();
                stockAlertSelection();

                break;

            case R.id.linInternalMovements:
                currentFragment = new InternalMovementsFragment();
                loadFragment(currentFragment, getResources().getString(R.string.internal_movements));
                backPressStatus = 1;
                slidingPaneLayout.closePane();
                internalMovementSelection();

                break;

            case R.id.linPurchaseOrder:
                currentFragment = new PurchaseOrdersFragment();
                loadFragment(currentFragment, getResources().getString(R.string.purchase_orders));
                backPressStatus = 1;
                slidingPaneLayout.closePane();
                purchaseOrderSelection();

                break;

            case R.id.linHome1:
                currentFragment = new DashBoardFragment();
                loadFragment(currentFragment, getResources().getString(R.string.dashboard));
                backPressStatus = 0;
                homeSelection();
                break;

            case R.id.linPendingOrder1:
                currentFragment = new PendingOrdersFragment();
                loadFragment(currentFragment, getResources().getString(R.string.pending_orders));
                backPressStatus = 1;
                pendingSelection();

                break;

            case R.id.linDeliveredOrder1:
                currentFragment = new DeliveredOrdersFragment();
                loadFragment(currentFragment, getResources().getString(R.string.delivered_orders));
                backPressStatus = 1;
                deliveredSelection();
                break;

            case R.id.linJobWork1:
                currentFragment = new JobWorksFragment();
                loadFragment(currentFragment, getResources().getString(R.string.job_works));
                backPressStatus = 1;
                jobWorkSelection();

                break;

            case R.id.linStockCheck1:
                currentFragment = new StockCheckFragment();
                loadFragment(currentFragment, getResources().getString(R.string.stock_check));
                backPressStatus = 1;
                stockCheckSelection();

                break;

            case R.id.linStockAlerts1:
                currentFragment = new StockAlertsFragment();
                loadFragment(currentFragment, getResources().getString(R.string.stock_alerts));
                backPressStatus = 1;
                stockAlertSelection();

                break;

            case R.id.linInternalMovements1:
                currentFragment = new InternalMovementsFragment();
                loadFragment(currentFragment, getResources().getString(R.string.internal_movements));
                backPressStatus = 1;
                internalMovementSelection();

                break;

            case R.id.linPurchaseOrder1:
                currentFragment = new PurchaseOrdersFragment();
                loadFragment(currentFragment, getResources().getString(R.string.purchase_orders));
                backPressStatus = 1;
                purchaseOrderSelection();

                break;

            case R.id.imgMenuBottom:
                slidingPaneLayout.openPane();
                break;

            case R.id.imgMenuBottom1:
                slidingPaneLayout.openPane();
                break;

            case R.id.imgMenuTop:
                slidingPaneLayout.closePane();
                break;

            case R.id.imgProfile:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (Permission.hasPermissions(DrawerActivity.this, permissions)) {
                        profilePickerOptionDialog();
                    } else {
                        Permission.requestPermissions(DrawerActivity.this, permissions);
                    }
                } else {
                    profilePickerOptionDialog();
                }
                break;

            case R.id.linLogout:
                logoutConfirmation();
                break;

            case R.id.linLogout1:
                logoutConfirmation();
                break;
        }
    }

    private void logoutConfirmation() {

        try {
            LayoutInflater localView = LayoutInflater.from(DrawerActivity.this);
            View promptsView = localView.inflate(R.layout.logout_layout_test, null);

            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(DrawerActivity.this);
            builder.setView(promptsView);
            final android.app.AlertDialog alertDialog = builder.create();
            alertDialog.setCancelable(false);

            final TextView txtNo = promptsView.findViewById(R.id.txtNo);
            final TextView txtYes = promptsView.findViewById(R.id.txtYes);

            txtNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });

            txtYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (AppUtil.isOnline(DrawerActivity.this)) {
                        logoutApi();
                    } else {
                        AppUtil.showToastFail(DrawerActivity.this, getResources().getString(R.string.no_internet));
                    }
                }
            });

            android.app.AlertDialog alert = builder.create();
            alertDialog.show();
        } catch (Exception e) {
            AppUtil.showToastFail(DrawerActivity.this, getResources().getString(R.string.something_wrong));
        }
    }

    private void logoutApi() {
        try {
            showDialog();
            apiService =
                    MyApplicationClass.getClient().create(ApiUrlList.class);

            Call<CommonModel> call = apiService.logoutApi(getResources().getString(R.string.api_key), sharedPreferences.getString(SharePref.userId, ""));
            Log.d(TAG, "logout Request  " + call.request().body());
            call.enqueue(new Callback<CommonModel>() {
                @Override
                public void onResponse(Call<CommonModel> call, Response<CommonModel> response) {
                    hideDialog();
                    if (response != null) {
                        if (response.isSuccessful()) {
                            Log.d(TAG, "logout Response success " + new Gson().toJson(response));
                            if (response.body().getStatus() == 1) {
                                AppUtil.showToastSuccess(DrawerActivity.this, response.body().getMessage());
                                SharePref.clear(SharePref.getSharePref(DrawerActivity.this));
                                startActivity(new Intent(DrawerActivity.this, LoginActivity.class));
                                finish();
                            } else {
                                AppUtil.showToastFail(DrawerActivity.this, response.body().getMessage());
                            }
                        } else {
                            Log.d(TAG, "logout Response fail " + new Gson().toJson(response));
                            AppUtil.showToastFail(DrawerActivity.this, getResources().getString(R.string.something_wrong));
                        }
                    } else {
                        AppUtil.showToastFail(DrawerActivity.this, getResources().getString(R.string.something_wrong));
                    }
                }

                @Override
                public void onFailure(Call<CommonModel> call, Throwable t) {
                    hideDialog();
                    Log.d(TAG, "logout Request fail " + t.getMessage());
                    AppUtil.showToastFail(DrawerActivity.this, getResources().getString(R.string.something_wrong));
                }
            });
        } catch (Exception e) {
            hideDialog();
            Log.d(TAG, "Exception logoutApi " + e.getMessage());
            AppUtil.showToastFail(DrawerActivity.this, getResources().getString(R.string.something_wrong));
        }
    }

    private void profilePickerOptionDialog() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(DrawerActivity.this);
            LayoutInflater layoutInflaterAndroid = LayoutInflater.from(DrawerActivity.this);
            View view2 = layoutInflaterAndroid.inflate(R.layout.image_picker_option_dialog_layout, null);
            builder.setView(view2);
            builder.setCancelable(true);
            final AlertDialog alertDialog = builder.create();
            alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            alertDialog.show();

            LinearLayout linCamera = view2.findViewById(R.id.linCamera);
            LinearLayout linGallery = view2.findViewById(R.id.linGallery);

            linCamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (Permission.hasPermissions(DrawerActivity.this, permissions1)) {
                            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                                File pictureFile = null;
                                try {
                                    pictureFile = getPictureFile();

                                } catch (IOException ex) {
                                    Log.e("5419", ex.getMessage());
                                    return;
                                }
                                if (pictureFile != null) {
                                    Uri photoURI = FileProvider.getUriForFile(DrawerActivity.this,
                                            getPackageName() + ".fileProvider",
                                            pictureFile);
                                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                    startActivityForResult(cameraIntent, 1);
                                }
                            }
                        } else {
                            Permission.requestPermissions(DrawerActivity.this, permissions1);
                        }
                    } else {
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                            File pictureFile = null;
                            try {
                                pictureFile = getPictureFile();

                            } catch (IOException ex) {
                                Log.e("5419", ex.getMessage());
                                return;
                            }
                            if (pictureFile != null) {
                                Uri photoURI = FileProvider.getUriForFile(DrawerActivity.this,
                                        getPackageName() + ".fileProvider",
                                        pictureFile);
                                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                startActivityForResult(cameraIntent, 1);
                            }
                        }
                    }
                    alertDialog.dismiss();
                }
            });

            linGallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 0);
                    alertDialog.dismiss();
                }
            });
        } catch (Exception e) {
            AppUtil.showToastFail(DrawerActivity.this, getResources().getString(R.string.something_wrong));
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        try {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK) {
                        Uri selectedImage = imageReturnedIntent.getData();
                        imgProfile.setImageURI(selectedImage);
                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK) {
                        Uri uri_1 = null;
                        File imgFile = new File(pictureFilePath);
                        if (imgFile.exists()) {
                            try {
                                uri_1 = Uri.fromFile(imgFile);
                                final InputStream imageStream = getContentResolver().openInputStream(uri_1);
                                final Bitmap selectedImage1 = BitmapFactory.decodeStream(imageStream);
                         /*   String encodedImage = encodeImage(selectedImage1);
                            String img_code1 = encodedImage;*/
                                imgProfile.setImageBitmap(selectedImage1);

                            } catch (Exception e) {
                                Log.e("5543", e.getMessage());
                            }
                        }
                    }
                    break;
            }
        } catch (Exception e) {
            AppUtil.showToastFail(DrawerActivity.this, getResources().getString(R.string.something_wrong));
        }
    }

    private void loadFragment(Fragment fragment, String fragmentName) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_content, fragment);
        if (currentFragment instanceof DashBoardFragment) {

        } else {
            fragmentTransaction.addToBackStack(fragmentName);
        }
        fragmentTransaction.commit();
    }

   /* @Override
    public void onBackPressed() {

        // double click for exit

        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else if (!doubleBackToExitPressedOnce) {
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click back again to exit.", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        } else {
            super.onBackPressed();
        }
    }*/

    @Override
    public void onBackPressed() {

        // double click for exit

        if (backPressStatus == 2) {
            if(DrawerActivity.MAIN_FRAGMENT.equalsIgnoreCase("MAIN_FRAGMENT")){
                currentFragment = new DashBoardFragment();
                loadFragment(currentFragment, getResources().getString(R.string.dashboard));
                backPressStatus = 0;
            }else {
                getSupportFragmentManager().popBackStack();
            }

        } else if (backPressStatus == 0) {
            if (!doubleBackToExitPressedOnce) {
                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Please click back again to exit.", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);
            } else {
                finish();
            }
        } else if(backPressStatus == 1){
            currentFragment = new DashBoardFragment();
            loadFragment(currentFragment, getResources().getString(R.string.dashboard));
            backPressStatus = 0;
        }
    }

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }

    private File getPictureFile() throws IOException {
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            String pictureFile = "pinnacle_" + timeStamp;
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File img = File.createTempFile(pictureFile, ".jpg", storageDir);
            if (storageDir.isDirectory()) {
                String[] children = storageDir.list();
                for (int i = 0; i < children.length; i++) {
                    new File(storageDir, children[i]).delete();
                }
            }
            pictureFilePath = img.getAbsolutePath();
            return img;
        } catch (Exception e) {
            AppUtil.showToastFail(DrawerActivity.this, getResources().getString(R.string.something_wrong));
        }
        return null;
    }

    private void internalMovementSelection() {
        txtInternalMovements.setTextColor(getResources().getColor(R.color.login_bg_color));
        txtInternalMovements1.setTextColor(getResources().getColor(R.color.login_bg_color));

        txtHome.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtHome1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtPendingOrder.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtPendingOrder1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtDeliverdOrder.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtDeliverdOrder1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtStockCheck.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtStockCheck1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtStockAlerts.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtStockAlerts1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtPurchaseOrder.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtPurchaseOrder1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtJobWork.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtJobWork1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));

        imgInternalMove.setColorFilter(getResources().getColor(R.color.login_bg_color));
        imgInternalMove1.setColorFilter(getResources().getColor(R.color.login_bg_color));

        imgPending.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgPending1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgHome.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgHome1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgDelivered.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgDelivered1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgStockCheck.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgStockCheck1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgStockAlerts.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgStockAlerts1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgPurchase.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgPurchase1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgJobWork.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgJobWork1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
    }

    private void stockAlertSelection() {
        txtStockAlerts.setTextColor(getResources().getColor(R.color.login_bg_color));
        txtStockAlerts1.setTextColor(getResources().getColor(R.color.login_bg_color));

        txtHome.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtHome1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtPendingOrder.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtPendingOrder1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtDeliverdOrder.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtDeliverdOrder1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtStockCheck.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtStockCheck1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtPurchaseOrder.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtPurchaseOrder1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtInternalMovements.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtInternalMovements1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtJobWork.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtJobWork1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));

        imgStockAlerts.setColorFilter(getResources().getColor(R.color.login_bg_color));
        imgStockAlerts1.setColorFilter(getResources().getColor(R.color.login_bg_color));

        imgPending.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgPending1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgHome.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgHome1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgDelivered.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgDelivered1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgStockCheck.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgStockCheck1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgPurchase.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgPurchase1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgInternalMove.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgInternalMove1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgJobWork.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgJobWork1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
    }

    private void purchaseOrderSelection() {

        txtPurchaseOrder.setTextColor(getResources().getColor(R.color.login_bg_color));
        txtPurchaseOrder1.setTextColor(getResources().getColor(R.color.login_bg_color));

        txtHome.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtHome1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtPendingOrder.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtPendingOrder1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtDeliverdOrder.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtDeliverdOrder1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtStockAlerts.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtStockAlerts1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtStockCheck.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtStockCheck1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtInternalMovements.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtInternalMovements1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtJobWork.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtJobWork1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));

        imgPurchase.setColorFilter(getResources().getColor(R.color.login_bg_color));
        imgPurchase1.setColorFilter(getResources().getColor(R.color.login_bg_color));

        imgPending.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgPending1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgHome.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgHome1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgStockCheck.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgStockCheck1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgDelivered.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgDelivered1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgStockAlerts.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgStockAlerts1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgInternalMove.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgInternalMove1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgJobWork.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgJobWork1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
    }

    private void stockCheckSelection() {
        txtStockCheck.setTextColor(getResources().getColor(R.color.login_bg_color));
        txtStockCheck1.setTextColor(getResources().getColor(R.color.login_bg_color));

        txtHome.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtHome1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtPendingOrder.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtPendingOrder1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtDeliverdOrder.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtDeliverdOrder1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtStockAlerts.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtStockAlerts1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtPurchaseOrder.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtPurchaseOrder1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtInternalMovements.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtInternalMovements1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtJobWork.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtJobWork1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));

        imgStockCheck.setColorFilter(getResources().getColor(R.color.login_bg_color));
        imgStockCheck1.setColorFilter(getResources().getColor(R.color.login_bg_color));

        imgPending.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgPending1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgHome.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgHome1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgDelivered.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgDelivered1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgStockAlerts.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgStockAlerts1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgPurchase.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgPurchase1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgInternalMove.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgInternalMove1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgJobWork.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgJobWork1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
    }

    private void jobWorkSelection() {
        txtJobWork.setTextColor(getResources().getColor(R.color.login_bg_color));
        txtJobWork1.setTextColor(getResources().getColor(R.color.login_bg_color));

        txtHome.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtHome1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtPendingOrder.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtPendingOrder1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtDeliverdOrder.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtDeliverdOrder1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtStockCheck.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtStockCheck1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtStockAlerts.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtStockAlerts1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtPurchaseOrder.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtPurchaseOrder1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtInternalMovements.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtInternalMovements1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));

        imgJobWork.setColorFilter(getResources().getColor(R.color.login_bg_color));
        imgJobWork1.setColorFilter(getResources().getColor(R.color.login_bg_color));

        imgPending.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgPending1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgHome.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgHome1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgDelivered.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgDelivered1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgStockCheck.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgStockCheck1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgStockAlerts.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgStockAlerts1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgPurchase.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgPurchase1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgInternalMove.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgInternalMove1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
    }

    private void pendingSelection() {
        txtPendingOrder.setTextColor(getResources().getColor(R.color.login_bg_color));
        txtPendingOrder1.setTextColor(getResources().getColor(R.color.login_bg_color));

        txtHome.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtHome1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtDeliverdOrder.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtDeliverdOrder1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtStockCheck.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtStockCheck1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtStockAlerts.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtStockAlerts1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtPurchaseOrder.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtPurchaseOrder1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtInternalMovements.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtInternalMovements1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtJobWork.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtJobWork1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));

        imgPending.setColorFilter(getResources().getColor(R.color.login_bg_color));
        imgPending1.setColorFilter(getResources().getColor(R.color.login_bg_color));

        imgHome.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgHome1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgDelivered.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgDelivered1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgStockCheck.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgStockCheck1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgStockAlerts.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgStockAlerts1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgPurchase.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgPurchase1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgInternalMove.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgInternalMove1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgJobWork.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgJobWork1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
    }

    private void deliveredSelection() {

        txtDeliverdOrder.setTextColor(getResources().getColor(R.color.login_bg_color));
        txtDeliverdOrder1.setTextColor(getResources().getColor(R.color.login_bg_color));

        txtPendingOrder.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtPendingOrder1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtHome.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtHome1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtStockCheck.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtStockCheck1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtStockAlerts.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtStockAlerts1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtPurchaseOrder.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtPurchaseOrder1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtInternalMovements.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtInternalMovements1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtJobWork.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtJobWork1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));


        imgDelivered.setColorFilter(getResources().getColor(R.color.login_bg_color));
        imgDelivered1.setColorFilter(getResources().getColor(R.color.login_bg_color));

        imgHome.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgHome1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgPending.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgPending1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgStockCheck.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgStockCheck1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgStockAlerts.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgStockAlerts1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgPurchase.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgPurchase1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgInternalMove.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgInternalMove1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgJobWork.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgJobWork1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
    }

    public void homeSelection() {
        txtHome.setTextColor(getResources().getColor(R.color.login_bg_color));
        txtHome1.setTextColor(getResources().getColor(R.color.login_bg_color));

        txtPendingOrder.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtPendingOrder1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtDeliverdOrder.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtDeliverdOrder1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtStockCheck.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtStockCheck1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtStockAlerts.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtStockAlerts1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtPurchaseOrder.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtPurchaseOrder1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtInternalMovements.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtInternalMovements1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtJobWork.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtJobWork1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));

        imgHome.setColorFilter(getResources().getColor(R.color.login_bg_color));
        imgHome1.setColorFilter(getResources().getColor(R.color.login_bg_color));

        imgPending.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgPending1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgDelivered.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgDelivered1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgStockCheck.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgStockCheck1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgStockAlerts.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgStockAlerts1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgPurchase.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgPurchase1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgInternalMove.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgInternalMove1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgJobWork.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgJobWork1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Set broadcast for to change menu selection color

        this.mMyBroadcastReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.equalsIgnoreCase(getResources().getString(R.string.internal_movements))) {
                    internalMovementSelection();
                } else if (action.equalsIgnoreCase(getResources().getString(R.string.menu_home))) {
                    homeSelection();
                } else if (action.equalsIgnoreCase(getResources().getString(R.string.pending_orders))) {
                    pendingSelection();
                } else if (action.equalsIgnoreCase(getResources().getString(R.string.delivered_orders))) {
                    deliveredSelection();
                } else if (action.equalsIgnoreCase(getResources().getString(R.string.stock_check))) {
                    stockCheckSelection();
                } else if (action.equalsIgnoreCase(getResources().getString(R.string.stock_alerts))) {
                    stockAlertSelection();
                } else if (action.equalsIgnoreCase(getResources().getString(R.string.purchase_orders))) {
                    purchaseOrderSelection();
                } else if (action.equalsIgnoreCase(getResources().getString(R.string.job_works))) {
                    jobWorkSelection();
                }
            }
        };
        try {
            LocalBroadcastManager.getInstance(DrawerActivity.this).registerReceiver(this.mMyBroadcastReceiver, new IntentFilter(getResources().getString(R.string.internal_movements)));
            LocalBroadcastManager.getInstance(DrawerActivity.this).registerReceiver(this.mMyBroadcastReceiver, new IntentFilter(getResources().getString(R.string.menu_home)));
            LocalBroadcastManager.getInstance(DrawerActivity.this).registerReceiver(this.mMyBroadcastReceiver, new IntentFilter(getResources().getString(R.string.pending_orders)));
            LocalBroadcastManager.getInstance(DrawerActivity.this).registerReceiver(this.mMyBroadcastReceiver, new IntentFilter(getResources().getString(R.string.delivered_orders)));
            LocalBroadcastManager.getInstance(DrawerActivity.this).registerReceiver(this.mMyBroadcastReceiver, new IntentFilter(getResources().getString(R.string.stock_check)));
            LocalBroadcastManager.getInstance(DrawerActivity.this).registerReceiver(this.mMyBroadcastReceiver, new IntentFilter(getResources().getString(R.string.stock_alerts)));
            LocalBroadcastManager.getInstance(DrawerActivity.this).registerReceiver(this.mMyBroadcastReceiver, new IntentFilter(getResources().getString(R.string.purchase_orders)));
            LocalBroadcastManager.getInstance(DrawerActivity.this).registerReceiver(this.mMyBroadcastReceiver, new IntentFilter(getResources().getString(R.string.job_works)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createDialog1() {
        prd = new ProgressDialog(DrawerActivity.this, R.style.MyAlertDialogStyle);
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