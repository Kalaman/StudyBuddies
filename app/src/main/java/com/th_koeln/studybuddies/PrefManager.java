package com.th_koeln.studybuddies;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Kalaman on 02.01.2018.
 */
public class PrefManager {
    private static SharedPreferences mSharedPref;
    private static SharedPreferences.Editor mSharedPrefEditor;

    public PrefManager(Context context)
    {
        mSharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        mSharedPrefEditor = mSharedPref.edit();
    }

    /**
     * These Methods are reading the Values
     */
    public static int readSharedInt (String sharedKey)
    {
        return mSharedPref.getInt(sharedKey, 0);
    }

    public static String readSharedString (String sharedKey)
    {
        return mSharedPref.getString(sharedKey, "");
    }

    public static boolean readSharedBoolean (String sharedKey)
    {
        return mSharedPref.getBoolean(sharedKey, false);
    }


    /**
     * These Methods are writing the Values
     */
    public static void writeSharedInt (String sharedKey , int sharedValue)
    {
        mSharedPrefEditor.putInt(sharedKey, sharedValue);
        mSharedPrefEditor.commit();
    }

    public static void writeSharedString (String sharedKey, String sharedValue)
    {
        mSharedPrefEditor.putString(sharedKey, sharedValue);
        mSharedPrefEditor.commit();
    }

    public static void writeSharedBoolean (String sharedKey, boolean sharedValue)
    {
        mSharedPrefEditor.putBoolean(sharedKey, sharedValue);
        mSharedPrefEditor.commit();
    }

    public static SharedPreferences.Editor getSharedPrefEditor()
    {
        return mSharedPrefEditor;
    }

    public static SharedPreferences getmSharedPref() {
        return mSharedPref;
    }
}
