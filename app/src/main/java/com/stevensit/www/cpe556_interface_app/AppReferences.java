package com.stevensit.www.cpe556_interface_app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AppReferences {
    //private static final String appSharedRef = AppReferences.class.getSimpleName();

    private SharedPreferences sharedRef;
    //  private SharedPreferences.Editor prefEditor;


    @SuppressLint("CommitPrefEdits")
    public AppReferences(Context contex){
        sharedRef = PreferenceManager.getDefaultSharedPreferences(contex);
    }

    /**
     * get the value from the preference
     * @param tag
     * @return
     */

    public float getValue(String tag){
        return sharedRef.getFloat(tag,0);
    }

    /**
     * save the value in the preferences
     * @param tag
     */

    public void saveValue(String tag, float val){
        try{
            sharedRef.edit().putFloat(tag,val).apply();
        }catch(NullPointerException e){
            throw new NullPointerException();}
    }

    /**
     //     * Register SharedPreferences change listener
     //     * @param listener listener object of OnSharedPreferenceChangeListener
     //     */
    public void registerOnSharedPreferenceChangeListener(
            SharedPreferences.OnSharedPreferenceChangeListener listener) {

        sharedRef.registerOnSharedPreferenceChangeListener(listener);
    }

    /**
     * Unregister SharedPreferences change listener
     * @param listener listener object of OnSharedPreferenceChangeListener to be unregistered
     */
    public void unregisterOnSharedPreferenceChangeListener(
            SharedPreferences.OnSharedPreferenceChangeListener listener) {

        sharedRef.unregisterOnSharedPreferenceChangeListener(listener);
    }

}