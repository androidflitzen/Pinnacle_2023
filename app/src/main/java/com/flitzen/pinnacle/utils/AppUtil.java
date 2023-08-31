package com.flitzen.pinnacle.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.flitzen.pinnacle.MyApplicationClass;
import com.flitzen.pinnacle.R;

public class AppUtil
{

    public static String emailPattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public static void showToastSuccess(Context context, String msg) {
        //Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        if(context!=null && msg!=null){
            new CToast(context).simpleToastSuccess(msg, Toast.LENGTH_SHORT)
                    .show();
        }
    }

    public static void showToastFail(Context context, String msg) {
        //Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        if(context!=null && msg!=null){
            new CToast(context).simpleToastFail(msg, Toast.LENGTH_SHORT)
                    .show();
        }
    }

    public static void showToastGeneral(Context context, String msg, int color) {
        //Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        if(context!=null && msg!=null && color!=0){
            new CToast(context).simpleToastGeneral(msg, Toast.LENGTH_SHORT)
                    .setBackgroundColor(color)
                    .show();
        }
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

}
