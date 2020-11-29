package com.example.treasurehunt;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PaceService {

    // In meters
    public static Double paceLength;

    public static double getPaceLength(Context context) {
        if (paceLength == null) {
            paceLength = readPaceLength(context);
            if (paceLength == null)
                paceLength = 0.75;
        }
        return paceLength;
    }

    final static String KEY_PREFS = "PaceLength";
    public static Double readPaceLength(Context context) {
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context.getApplicationContext());
        double val;
        String serializedObject = appSharedPrefs.getString(KEY_PREFS, null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            val = gson.fromJson(serializedObject, Double.class);
            return val;
        }
        return null;
    }

    public static void setPaceLength(Context context, Double paceLength) {
        PaceService.paceLength = paceLength;
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = appSharedPrefs.edit();
        editor.putString(KEY_PREFS, paceLength.toString());
        editor.apply();
    }
}


