package com.flitzen.pinnacle.customer_service.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flitzen.pinnacle.R;
import com.flitzen.pinnacle.customer_service.fragment.CustomerDashBoardFragment;
import com.flitzen.pinnacle.customer_service.fragment.CustomerMyMachinesFragment;
import com.flitzen.pinnacle.customer_service.fragment.CustomerOurNewProductsFragment;
import com.flitzen.pinnacle.customer_service.fragment.CustomerProblemSolvingFragment;
import com.flitzen.pinnacle.customer_service.fragment.CustomerServiceRequestFragment;
import com.flitzen.pinnacle.customer_service.fragment.CustomerSettingsFragment;
import com.flitzen.pinnacle.inventory_management.fragment.DashBoardFragment;
import com.flitzen.pinnacle.utils.CircleImageView;
import com.flitzen.pinnacle.utils.SlidingPaneLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomerDrawerActivity extends AppCompatActivity implements SlidingPaneLayout.PanelSlideListener, View.OnClickListener {

    public Fragment currentFragment;

    @BindView(R.id.fragment_master)
    View fragment_master;
    @BindView(R.id.fragment_content)
    FrameLayout fragment_content;
    @BindView(R.id.slide)
    SlidingPaneLayout slidingPaneLayout;
    @BindView(R.id.linHome)
    LinearLayout linHome;
    @BindView(R.id.linMyMachines)
    LinearLayout linMyMachines;
    @BindView(R.id.linServiceRequesr)
    LinearLayout linServiceRequesr;
    @BindView(R.id.linProblemSolving)
    LinearLayout linProblemSolving;
    @BindView(R.id.linNewProducts)
    LinearLayout linNewProducts;
    @BindView(R.id.linSettings)
    LinearLayout linSettings;
    @BindView(R.id.linLogout)
    LinearLayout linLogout;
    @BindView(R.id.linHome1)
    LinearLayout linHome1;
    @BindView(R.id.linMyMachines1)
    LinearLayout linMyMachines1;
    @BindView(R.id.linServiceRequesr1)
    LinearLayout linServiceRequesr1;
    @BindView(R.id.linProblemSolving1)
    LinearLayout linProblemSolving1;
    @BindView(R.id.linNewProducts1)
    LinearLayout linNewProducts1;
    @BindView(R.id.linSettings1)
    LinearLayout linSettings1;
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
    @BindView(R.id.txtMyMachines)
    TextView txtMyMachines;
    @BindView(R.id.txtMyMachines1)
    TextView txtMyMachines1;
    @BindView(R.id.txtServiceRequesr)
    TextView txtServiceRequesr;
    @BindView(R.id.txtServiceRequesr1)
    TextView txtServiceRequesr1;
    @BindView(R.id.txtProblemSolving)
    TextView txtProblemSolving;
    @BindView(R.id.txtProblemSolving1)
    TextView txtProblemSolving1;
    @BindView(R.id.txtNewProducts)
    TextView txtNewProducts;
    @BindView(R.id.txtNewProducts1)
    TextView txtNewProducts1;
    @BindView(R.id.txtSettings)
    TextView txtSettings;
    @BindView(R.id.txtSettings1)
    TextView txtSettings1;
    @BindView(R.id.txtHome)
    TextView txtHome;
    @BindView(R.id.txtHome1)
    TextView txtHome1;
    @BindView(R.id.txtUserRole)
    TextView txtUserRole;
    @BindView(R.id.txtUserName)
    TextView txtUserName;

    @BindView(R.id.imgHome)
    ImageView imgHome;
    @BindView(R.id.imgHome1)
    ImageView imgHome1;
    @BindView(R.id.imgMyMachines)
    ImageView imgMyMachines;
    @BindView(R.id.imgMyMachines1)
    ImageView imgMyMachines1;
    @BindView(R.id.imgServiceRequesr)
    ImageView imgServiceRequesr;
    @BindView(R.id.imgServiceRequesr1)
    ImageView imgServiceRequesr1;
    @BindView(R.id.imgProblemSolving)
    ImageView imgProblemSolving;
    @BindView(R.id.imgProblemSolving1)
    ImageView imgProblemSolving1;
    @BindView(R.id.imgNewProducts)
    ImageView imgNewProducts;
    @BindView(R.id.imgNewProducts1)
    ImageView imgNewProducts1;
    @BindView(R.id.imgSettings)
    ImageView imgSettings;
    @BindView(R.id.imgSettings1)
    ImageView imgSettings1;
    @BindView(R.id.imgProfile)
    CircleImageView imgProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_drawer);
        ButterKnife.bind(this);

        // Set clickListener
        linHome.setOnClickListener(this);
        linMyMachines.setOnClickListener(this);
        linServiceRequesr.setOnClickListener(this);
        linProblemSolving.setOnClickListener(this);
        linNewProducts.setOnClickListener(this);
        linSettings.setOnClickListener(this);
        linLogout.setOnClickListener(this);
        linHome1.setOnClickListener(this);
        linMyMachines1.setOnClickListener(this);
        linServiceRequesr1.setOnClickListener(this);
        linProblemSolving1.setOnClickListener(this);
        linNewProducts1.setOnClickListener(this);
        linSettings1.setOnClickListener(this);
        linLogout1.setOnClickListener(this);
        imgMenuBottom.setOnClickListener(this);
        imgMenuBottom1.setOnClickListener(this);
        imgMenuTop.setOnClickListener(this);
        imgProfile.setOnClickListener(this);

        slidingPaneLayout.setPanelSlideListener(this);

        currentFragment = new CustomerDashBoardFragment();
        loadFragment(currentFragment, getResources().getString(R.string.dashboard));

    }

    @Override
    public void onPanelSlide(@NonNull View panel, float slideOffset) {

    }

    @Override
    public void onPanelOpened(@NonNull View panel) {
        linHome.setOrientation(LinearLayout.HORIZONTAL);
        linMyMachines.setOrientation(LinearLayout.HORIZONTAL);
        linServiceRequesr.setOrientation(LinearLayout.HORIZONTAL);
        linProblemSolving.setOrientation(LinearLayout.HORIZONTAL);
        linNewProducts.setOrientation(LinearLayout.HORIZONTAL);
        linSettings.setOrientation(LinearLayout.HORIZONTAL);
        linLogout.setOrientation(LinearLayout.HORIZONTAL);
        linHeader.setVisibility(View.VISIBLE);
        imgMenuTop.setVisibility(View.VISIBLE);
        imgMenuBottom.setVisibility(View.GONE);
        linCloseDrawer.setVisibility(View.GONE);
        linOpenDrawer.setVisibility(View.VISIBLE);

        txtMyMachines.setText(getResources().getString(R.string.my_machines));
        txtServiceRequesr.setText(getResources().getString(R.string.service_request));
        txtProblemSolving.setText(getResources().getString(R.string.problem_solving));
        txtNewProducts.setText(getResources().getString(R.string.new_products));
        txtSettings.setText(getResources().getString(R.string.settings));
    }

    @Override
    public void onPanelClosed(@NonNull View panel) {
        linHome.setOrientation(LinearLayout.VERTICAL);
        linMyMachines.setOrientation(LinearLayout.VERTICAL);
        linServiceRequesr.setOrientation(LinearLayout.VERTICAL);
        linProblemSolving.setOrientation(LinearLayout.VERTICAL);
        linNewProducts.setOrientation(LinearLayout.VERTICAL);
        linSettings.setOrientation(LinearLayout.VERTICAL);
        linLogout.setOrientation(LinearLayout.VERTICAL);
        linHeader.setVisibility(View.GONE);
        imgMenuTop.setVisibility(View.GONE);
        imgMenuBottom.setVisibility(View.VISIBLE);

        linCloseDrawer.setVisibility(View.VISIBLE);
        linOpenDrawer.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.linHome:
                currentFragment = new CustomerDashBoardFragment();
                loadFragment(currentFragment, getResources().getString(R.string.dashboard));
                slidingPaneLayout.closePane();
                homeSelection();
                break;

            case R.id.linMyMachines:
                currentFragment = new CustomerMyMachinesFragment();
                loadFragment(currentFragment, getResources().getString(R.string.my_machines));
                slidingPaneLayout.closePane();
                myMachinesSelection();
                break;

            case R.id.linServiceRequesr:
                currentFragment = new CustomerServiceRequestFragment();
                loadFragment(currentFragment, getResources().getString(R.string.service_request));
                slidingPaneLayout.closePane();
                ServiceRequestSelection();
                break;

            case R.id.linProblemSolving:
                currentFragment = new CustomerProblemSolvingFragment();
                loadFragment(currentFragment, getResources().getString(R.string.problem_solving));
                slidingPaneLayout.closePane();
                problemSolvingSelection();
                break;

            case R.id.linNewProducts:
                currentFragment = new CustomerOurNewProductsFragment();
                loadFragment(currentFragment, getResources().getString(R.string.new_products));
                slidingPaneLayout.closePane();
                NewProductsSelection();
                break;

            case R.id.linSettings:
                currentFragment = new CustomerSettingsFragment();
                loadFragment(currentFragment, getResources().getString(R.string.settings));
                slidingPaneLayout.closePane();
                settingsSelection();
                break;

            case R.id.linHome1:
                currentFragment = new CustomerDashBoardFragment();
                loadFragment(currentFragment, getResources().getString(R.string.dashboard));
                homeSelection();
                break;

            case R.id.linMyMachines1:
                currentFragment = new CustomerMyMachinesFragment();
                loadFragment(currentFragment, getResources().getString(R.string.my_machines));
                myMachinesSelection();
                break;

            case R.id.linServiceRequesr1:
                currentFragment = new CustomerServiceRequestFragment();
                loadFragment(currentFragment, getResources().getString(R.string.service_request));
                ServiceRequestSelection();
                break;

            case R.id.linProblemSolving1:
                currentFragment = new CustomerProblemSolvingFragment();
                loadFragment(currentFragment, getResources().getString(R.string.problem_solving));
                problemSolvingSelection();
                break;

            case R.id.linNewProducts1:
                currentFragment = new CustomerOurNewProductsFragment();
                loadFragment(currentFragment, getResources().getString(R.string.new_products));
                NewProductsSelection();
                break;

            case R.id.linSettings1:
                currentFragment = new CustomerSettingsFragment();
                loadFragment(currentFragment, getResources().getString(R.string.settings));
                settingsSelection();
                break;

            case R.id.linLogout:
                break;

            case R.id.linLogout1:
                break;
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

    public void homeSelection() {
        txtHome.setTextColor(getResources().getColor(R.color.login_bg_color));
        txtHome1.setTextColor(getResources().getColor(R.color.login_bg_color));

        txtMyMachines.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtMyMachines1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtServiceRequesr.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtServiceRequesr1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtProblemSolving.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtProblemSolving1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtNewProducts.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtNewProducts1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtSettings.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtSettings1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));

        imgHome.setColorFilter(getResources().getColor(R.color.login_bg_color));
        imgHome1.setColorFilter(getResources().getColor(R.color.login_bg_color));

        imgMyMachines.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgMyMachines1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgServiceRequesr.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgServiceRequesr1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgProblemSolving.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgProblemSolving1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgNewProducts.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgNewProducts1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgSettings.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgSettings1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
    }
    private void myMachinesSelection() {
        txtMyMachines.setTextColor(getResources().getColor(R.color.login_bg_color));
        txtMyMachines1.setTextColor(getResources().getColor(R.color.login_bg_color));

        txtHome.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtHome1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtServiceRequesr.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtServiceRequesr1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtProblemSolving.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtProblemSolving1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtNewProducts.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtNewProducts1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtSettings.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtSettings1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));

        imgMyMachines.setColorFilter(getResources().getColor(R.color.login_bg_color));
        imgMyMachines1.setColorFilter(getResources().getColor(R.color.login_bg_color));

        imgHome.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgHome1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgServiceRequesr.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgServiceRequesr1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgProblemSolving.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgProblemSolving1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgNewProducts.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgNewProducts1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgSettings.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgSettings1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
    }

    private void ServiceRequestSelection() {
        txtServiceRequesr.setTextColor(getResources().getColor(R.color.login_bg_color));
        txtServiceRequesr1.setTextColor(getResources().getColor(R.color.login_bg_color));

        txtHome.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtHome1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtMyMachines.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtMyMachines1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtProblemSolving.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtProblemSolving1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtNewProducts.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtNewProducts1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtSettings.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtSettings1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));

        imgServiceRequesr.setColorFilter(getResources().getColor(R.color.login_bg_color));
        imgServiceRequesr1.setColorFilter(getResources().getColor(R.color.login_bg_color));

        imgHome.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgHome1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgMyMachines.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgMyMachines1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgProblemSolving.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgProblemSolving1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgNewProducts.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgNewProducts1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgSettings.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgSettings1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
    }

    private void problemSolvingSelection() {

        txtProblemSolving.setTextColor(getResources().getColor(R.color.login_bg_color));
        txtProblemSolving1.setTextColor(getResources().getColor(R.color.login_bg_color));

        txtHome.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtHome1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtMyMachines.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtMyMachines1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtServiceRequesr.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtServiceRequesr1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtNewProducts.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtNewProducts1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtSettings.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtSettings1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));

        imgProblemSolving.setColorFilter(getResources().getColor(R.color.login_bg_color));
        imgProblemSolving1.setColorFilter(getResources().getColor(R.color.login_bg_color));

        imgHome.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgHome1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgMyMachines.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgMyMachines1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgServiceRequesr.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgServiceRequesr1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgNewProducts.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgNewProducts1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgSettings.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgSettings1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
    }

    private void NewProductsSelection() {

        txtNewProducts.setTextColor(getResources().getColor(R.color.login_bg_color));
        txtNewProducts1.setTextColor(getResources().getColor(R.color.login_bg_color));

        txtHome.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtHome1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtMyMachines.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtMyMachines1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtServiceRequesr.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtServiceRequesr1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtProblemSolving.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtProblemSolving1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtSettings.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtSettings1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));

        imgNewProducts.setColorFilter(getResources().getColor(R.color.login_bg_color));
        imgNewProducts1.setColorFilter(getResources().getColor(R.color.login_bg_color));

        imgHome.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgHome1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgMyMachines.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgMyMachines1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgServiceRequesr.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgServiceRequesr1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgProblemSolving.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgProblemSolving1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgSettings.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgSettings1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
    }

    private void settingsSelection() {

        txtSettings.setTextColor(getResources().getColor(R.color.login_bg_color));
        txtSettings1.setTextColor(getResources().getColor(R.color.login_bg_color));

        txtHome.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtHome1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtMyMachines.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtMyMachines1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtServiceRequesr.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtServiceRequesr1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtProblemSolving.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtProblemSolving1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtNewProducts.setTextColor(getResources().getColor(R.color.light_blue_icon_color));
        txtNewProducts1.setTextColor(getResources().getColor(R.color.light_blue_icon_color));

        imgSettings.setColorFilter(getResources().getColor(R.color.login_bg_color));
        imgSettings1.setColorFilter(getResources().getColor(R.color.login_bg_color));

        imgHome.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgHome1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgMyMachines.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgMyMachines1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgServiceRequesr.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgServiceRequesr1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgProblemSolving.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgProblemSolving1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgNewProducts.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));
        imgNewProducts1.setColorFilter(getResources().getColor(R.color.light_blue_icon_color));

    }

}