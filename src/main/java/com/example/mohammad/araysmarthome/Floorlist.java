package com.example.mohammad.araysmarthome;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.InputStream;
import java.util.StringTokenizer;

public class Floorlist extends Activity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floorlist);
        final String data2 = getIntent().getStringExtra("EXTRA_SESSION_ID");
/////////////////////////////////////////////////////////////////////////////
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = pref.edit();

        /////////////////////////////////////////////////////////////////////
        Button vahed1 = (Button) findViewById(R.id.vahed1);
        vahed1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(Floorlist.this, Otagh.class);*/
                Intent data1 = new Intent(getBaseContext(), Otagh.class);
                String sessionId1=data2+".v1";
                data1.putExtra("EXTRA_SESSION_ID", sessionId1);
                startActivity(data1);
               /* startActivity(intent);*/

            }
        });
        Button vahed2 = (Button) findViewById(R.id.vahed2);
        vahed2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent intent = new Intent(Floorlist.this, Room.class);*/
                Intent data1 = new Intent(getBaseContext(), Otagh.class);
                String sessionId1=data2+".v2";
                data1.putExtra("EXTRA_SESSION_ID", sessionId1);
                startActivity(data1);
               /* startActivity(intent)*/;


            }
        });
        Button vahed3 = (Button) findViewById(R.id.vahed3);
        vahed3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(Floorlist.this, First.class);*/
                Intent data1 = new Intent(getBaseContext(), Otagh.class);
                String sessionId1=data2+".v3";
                data1.putExtra("EXTRA_SESSION_ID", sessionId1);
                startActivity(data1);
                /*startActivity(intent);*/

            }
        });

        Button vahed4 = (Button) findViewById(R.id.vahed4);
        vahed4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent intent = new Intent(Floorlist.this, Otagh.class);*/
                Intent data1 = new Intent(getBaseContext(), Otagh.class);
                String sessionId1=data2+".v4";
                data1.putExtra("EXTRA_SESSION_ID", sessionId1);
                startActivity(data1);
                /*startActivity(intent);*/

            }
        });

    }



}