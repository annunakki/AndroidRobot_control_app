package com.stevensit.www.cpe556_interface_app;
/**
 * sets and stores the values of the entered settings values after app exit
 * then it restores it when start the app
 * it can be extended to any object or value type
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AppReferences {

    private SharedPreferences sharedRef;


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

    public int getInt (String tag){

        return sharedRef.getInt(tag,0);
    }


//    public boolean getBoolean(String tag){
//        return sharedRef.getBoolean(tag,false);
//    }

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


    public void saveIntValue(String tag, int val){
        try{
            sharedRef.edit().putInt(tag,val).apply();
        }catch(NullPointerException e){
            throw new NullPointerException();}
    }

//    public void saveBoolean(String tag, boolean val){
//        try{
//            sharedRef.edit().putBoolean(tag,val).apply();
//        }catch(NullPointerException e){
//            throw new NullPointerException();}
//    }

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