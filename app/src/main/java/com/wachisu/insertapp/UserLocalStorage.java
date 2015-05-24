package com.wachisu.insertapp;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Sochi on 2015/05/20.
 */
public class UserLocalStorage {

    public static final String SP_NAME = "userDetails";
    SharedPreferences userLocalDatabase;

    public UserLocalStorage(Context context) {
        userLocalDatabase = context.getSharedPreferences(SP_NAME, 0);
    }

    public void storeUserData(String usernameValue, String passwordValue, String databaseValue) {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putString("usernameValue", usernameValue);
        spEditor.putString("passwordValue", passwordValue);
        spEditor.putString("databaseValue", databaseValue);
        spEditor.putBoolean("saveLogin", true);
        spEditor.commit();
    }

    public void setUserLoggedIn(boolean loggedIn) {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putBoolean("loggedIn", loggedIn);
        spEditor.commit();
    }

    public void clearUserData() {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.clear();
        spEditor.commit();
    }

    public boolean getUserLoggedStatus() {
        return userLocalDatabase.getBoolean("loggedIn", false);
    }

}
