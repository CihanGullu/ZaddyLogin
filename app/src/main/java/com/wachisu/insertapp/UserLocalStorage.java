package com.wachisu.insertapp;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Sochi on 2015/05/20.
 */
public class UserLocalStorage {

    public static final String SP_NAME = "userDetails";

    SharedPreferences userLocalDatabase;

    // Als we de functie aanroepen, dan geven we de locale context door, zodat we geen errors krijgen.

    public UserLocalStorage(Context context) {
        userLocalDatabase = context.getSharedPreferences(SP_NAME, 0);
    }

    // Hier voegen we data toe aan de spEditor, als je meer fields add, dan voeg je ze hier ook toe ( Als je wilt dat ze remembered worden )

    public void storeUserData(String usernameValue, String passwordValue, String databaseValue) {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putString("usernameValue", usernameValue);
        spEditor.putString("passwordValue", passwordValue);
        spEditor.putString("databaseValue", databaseValue);
        spEditor.putBoolean("saveLogin", true);
        spEditor.commit();
    }

    // Is een user gelogged? loggedIn true. Dit moet naar FALSE gezet worden als de user zich afmeld..

    public void setUserLoggedIn(boolean loggedIn) {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putBoolean("loggedIn", loggedIn);
        spEditor.commit();
    }

    // Wilt de gebruiker NIET dat de app zijn/haar data onthoudt? Gebruik dan clearUserData om alles te flushen.

    public void clearUserData() {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.clear();
        spEditor.commit();
    }

    // Functie die we niet gebruiken. Hiermee kijk je of de gebruiker wel of niet is ingelogd.

    public boolean getUserLoggedStatus() {
        return userLocalDatabase.getBoolean("loggedIn", false);
    }

}
