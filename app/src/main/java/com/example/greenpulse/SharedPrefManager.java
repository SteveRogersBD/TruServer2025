package com.example.greenpulse;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {

    private static final String PREF_NAME = "userInfo";
    private static final String KEY_NAME = "userName";
    private static final String KEY_ID = "userID";
    private static final String KEY_EMAIL = "userEmail";
    private static final String KEY_ROLE = "userRole";
    private static final String KEY_DP = "userDp";
    private static final String KEY_LAT = "userLat";
    private static final String KEY_LON = "userDp";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SharedPrefManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    // Method to save user information
    public void saveUserInfo(String name, String email, String role,String dp,int id) {
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_ROLE, role);
        editor.putString(KEY_DP,dp);
        editor.putInt(KEY_ID,id);
        editor.apply(); // Apply changes asynchronously
    }
    public void saveLocation(double lat,double lon)
    {
        editor.putFloat(KEY_LAT, (float) lat);
        editor.putFloat(KEY_LON, (float) lon);
    }

    // Method to get user name
    public String getUserName() {
        return sharedPreferences.getString(KEY_NAME, null);
    }
    public String getUserId() {
        return sharedPreferences.getString(KEY_ID, null);
    }

    // Method to get user email
    public String getUserEmail() {
        return sharedPreferences.getString(KEY_EMAIL, null);
    }

    // Method to get user role
    public String getUserRole() {
        return sharedPreferences.getString(KEY_ROLE, null);
    }

    // Method to clear user data (e.g., on logout)
    public void clearUserData() {
        editor.clear();
        editor.apply();
    }
}

