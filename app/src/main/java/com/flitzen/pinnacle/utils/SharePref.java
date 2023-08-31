package com.flitzen.pinnacle.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePref {

    public static String SharePrefName = "PINNACLE";

    public static String isLoggedIn = "isLoggedIn";
    public static String userId = "userId";
    public static String userRoleId = "userRoleId";
    public static String userName = "userName";
    public static String userEmail = "userEmail";
    public static String userMobile = "userMobile";
    public static String userRole = "userRole";
    public static String password = "password";
    public static String userType = "userType";
    public static String FIREBASE_TOKEN = "firebase_token";

    public static SharedPreferences getSharePref(Context context) {
        return context.getSharedPreferences(SharePrefName, Context.MODE_PRIVATE);
    }

    public static void clear(SharedPreferences sharedPreferences) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }
}
