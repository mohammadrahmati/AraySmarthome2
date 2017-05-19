package com.example.mohammad.araysmarthome;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.StringTokenizer;

public class settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Switch mySwitch2 = (Switch) findViewById(R.id.switch5);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        @SuppressLint("CommitPrefEdits") final SharedPreferences.Editor editor = pref.edit();
        mySwitch2.setChecked(pref.getBoolean("onlangaugeClick",false));
        mySwitch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    try {
                        String languageToLoad="pe";
                        Locale locale = new Locale(languageToLoad);
                        Locale.setDefault(locale);
                        Configuration config = new Configuration();
                        config.locale = locale;
                        getResources().updateConfiguration(config,getResources().getDisplayMetrics());
                        editor.putBoolean("onlangaugeClick",true);
                        editor.commit();
                    }
                    catch (Exception e) {

                       }

                } else {
                    try{
                        String languageToLoad="en";
                        Locale locale = new Locale(languageToLoad);
                        Locale.setDefault(locale);
                        Configuration config = new Configuration();
                        config.locale = locale;
                        getResources().updateConfiguration(config,getResources().getDisplayMetrics());
                        editor.putBoolean("onlangaugeClick",false);
                        editor.commit();
                    }
                    catch (Exception e) {
                       }

                }
            }
        });

    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(settings.this, Main3Activity.class);
        startActivity(intent);
        finish();

    }
}
