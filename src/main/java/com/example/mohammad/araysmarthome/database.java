package com.example.mohammad.araysmarthome;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.Log;


public class database {

    public static final String PREFS_NAME = "PESASEND_PREFS";
    public static final String PREFS_KEY = "AOP_PREFS_String";


    public database() {
        super();
    }

    public static void save(Context context, String text , String text2) {
        Log.i("asdasdasd","asdadasd");
        SharedPreferences pref;
       /* Editor editor;*/

        pref = PreferenceManager.getDefaultSharedPreferences(context);
        pref = context.getSharedPreferences("MyPref", 0); //1
        SharedPreferences.Editor editor = pref.edit(); //2
        Log.i("asdasdasd","asdadasd");
        editor.putString(text,text2); //3
        editor.apply(); //4
    }

    public static String getValue(Context context , String text2) {
        SharedPreferences settings;
        String text;
        //settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        text = settings.getString( text2 , null);
        return text;
    }

    public void clearSharedPreference(Context context) {
        SharedPreferences settings;
        Editor editor;

        //settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.clear();
        editor.commit();
    }

    public void removeValue(Context context) {
        SharedPreferences settings;
        Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.remove(PREFS_KEY);
        editor.commit();
    }
}