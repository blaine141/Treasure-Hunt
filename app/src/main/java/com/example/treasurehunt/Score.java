package com.example.treasurehunt;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Score implements Serializable {
    public double startDistance;
    public double endDistance;
    public int hints;
    public Cache cache;

    public Score(boolean found, double startDistance, Cache cache, int hints) {
        this.cache = cache;
        this.startDistance = startDistance;
        this.hints = hints;
        this.endDistance = cache.distance;
        if (found)
            this.endDistance = 0;
    }

    public double getAccuracy() {
        return (startDistance - endDistance) / startDistance;
    }

    public boolean isFound() {
        return endDistance == 0;
    }

    final static String KEY_PREFS = "Scores";
    public static List<Score> getScores(Context context) {
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context.getApplicationContext());
        List<Score> arrayItems;
        String serializedObject = appSharedPrefs.getString(KEY_PREFS, null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<Score>>(){}.getType();
            arrayItems = gson.fromJson(serializedObject, type);
            return arrayItems;
        }
        return new ArrayList<>();
    }

    public static void saveNewScore(Context context, Score score) {
        List<Score> scores = getScores(context);
        scores.add(score);

        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = appSharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(scores);
        editor.putString(KEY_PREFS, json);
        editor.apply();
    }
}
