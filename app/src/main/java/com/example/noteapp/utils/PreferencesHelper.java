package com.example.noteapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesHelper {
    private SharedPreferences sharedPreferences = null;

    public void unit(Context context){
        sharedPreferences = context.getSharedPreferences("settings",Context.MODE_PRIVATE);
    }
    public void onSaveOnBoardState(){
        sharedPreferences.edit().putBoolean("isShown", true).apply();
    }

    public boolean isShown(){
        return sharedPreferences.getBoolean("isShown", false);
    }
}
