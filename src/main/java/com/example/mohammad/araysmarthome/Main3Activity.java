
        package com.example.mohammad.araysmarthome;

        import android.annotation.SuppressLint;
        import android.app.Activity;
        import android.content.Context;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.content.res.Configuration;
        import android.media.audiofx.BassBoost;
        import android.net.DhcpInfo;
        import android.net.wifi.WifiManager;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.os.Handler;
        import android.os.Message;
        import android.support.annotation.NonNull;
        import android.support.design.widget.FloatingActionButton;
        import android.support.design.widget.Snackbar;
        import android.text.format.Formatter;
        import android.util.Log;
        import android.view.View;
        import android.support.design.widget.NavigationView;
        import android.support.v4.view.GravityCompat;
        import android.support.v4.widget.DrawerLayout;
        import android.support.v7.app.ActionBarDrawerToggle;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.Toolbar;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.widget.AdapterView;
        import android.widget.Button;
        import android.widget.LinearLayout;
        import android.widget.ListView;
        import android.widget.TextView;
        import android.widget.Toast;

        import org.w3c.dom.Text;

        import java.io.IOException;
        import java.net.InetAddress;
        import java.net.NetworkInterface;
        import java.net.Socket;
        import java.net.UnknownHostException;
        import java.util.ArrayList;
        import java.util.Collections;
        import java.util.List;
        import java.util.Locale;
        import java.util.StringTokenizer;

        public class Main3Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    /////////////////////////////////////////////////
    String Module_IP;
    String NICKNAME = "t1.v3.of4.k3.01001101";
    InetAddress serverAddress;
    Socket socket;
    boolean connected = false;
    CommsThread commsThread;
            public static Context mContext;
    //////////////////////////////////////////////////////////////////
           static String hasan;
            //////////////////////////////////
    public void main(){
        @SuppressLint("WifiManagerLeak") final WifiManager manager = (WifiManager) super.getSystemService(WIFI_SERVICE);
        final DhcpInfo dhcp = manager.getDhcpInfo();
        final String address = Formatter.formatIpAddress(dhcp.gateway);
        @SuppressLint("WifiManagerLeak") WifiManager managerr = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        Module_IP = address;

    }
    /////////////////////////////////////////////////////////////////////
    public class CreateCommThreadTask extends AsyncTask<Void, Integer, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {

                serverAddress =
                        InetAddress.getByName(Module_IP);
                socket = new Socket(serverAddress,1716);
                commsThread = new CommsThread(socket);
                commsThread.start();
                connected = true;
            } catch (UnknownHostException e) {
                Log.d("Sockets", e.getLocalizedMessage());
            } catch (IOException e) {
                Log.d("Sockets", e.getLocalizedMessage());
            }
            return null;
        }
    }
    private void sendToServer(String message) {
        try{
            byte[] thr =
                    message.getBytes();
            new WriteToServerTask().execute(thr);

        }catch (Exception e) {

        }
    }

    private class WriteToServerTask extends AsyncTask<byte[], Void, Void> {
        protected Void doInBackground(byte[]...thr) {
            try {
                commsThread.write(thr[0]);

            }catch (Exception e) {

            }
            return null;
        }
    }
    //////////////////////////////////////

    ////////////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        //////////////////////////////////////////////////////////////////////////////////
        final SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = pref.edit();
        ///////////////////////////////////////////////////////////////////////////
        main();
        ///////////////////////////////////////////////////////////////////////
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("صفحه اصلی");
        setSupportActionBar(toolbar);
        setTheme(R.style.AppTheme);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Button L1=(Button)findViewById(R.id.b1) ;
        Button L2=(Button)findViewById(R.id.b2) ;
        Button L3=(Button)findViewById(R.id.b3) ;
        Button L4=(Button)findViewById(R.id.b4) ;

        L1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent intent = new Intent(Main3Activity.this, Floorlist.class);*/
          /*      Intent data = new Intent(getBaseContext(), Floorlist.class);
                String sessionId="t1";
                data.putExtra("EXTRA_SESSION_ID", sessionId);
                startActivity(data);*/
               /* startActivity(intent);*/
               /* Toast.makeText(getBaseContext(),hasan, Toast.LENGTH_SHORT).show();*/

            }
        });
        L2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent intent = new Intent(Main3Activity.this, Floorlist.class);*/
                Intent data = new Intent(getBaseContext(), Floorlist.class);
                String sessionId="2";
                data.putExtra("EXTRA_SESSION_ID", sessionId);
                startActivity(data);
              /*  startActivity(intent);*/
            }
        });
        L3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent data = new Intent(getBaseContext(), Floorlist.class);
                String sessionId="3";
                data.putExtra("EXTRA_SESSION_ID", sessionId);
                startActivity(data);
            }
        });
        L4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main3Activity.this, Floorlist.class);
                String sessionId="4";
                intent.putExtra("EXTRA_SESSION_ID", sessionId);
                startActivity(intent);

            }
        });
    }
    ///////////////////////////////////////////////////////
    static Handler UIupdater = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            int numOfBytesReceived = msg.arg1;
            byte[] buffer = (byte[]) msg.obj;


            Log.i("asdasdasd","asdadasd");
            //---convert the entire byte array to string---
            String strReceived = new String(buffer);

            //---extract only the actual string received---
            strReceived = strReceived.substring(0, numOfBytesReceived);
            hasan = strReceived;

            if(hasan.contains("new")) {
                database.save(mContext, "database", hasan);
                database.save(mContext, "DBA", "1");
            }
        }
    };
    /////////////////////////////////////////////////////////////
    private class CloseSocketTask extends AsyncTask
            <Void, Void, Void> {
        //@Override
        protected Void doInBackground(Void... params) {
            try {
                if(connected){
                    socket.close();
                }
            } catch (Exception e) {
                //Log.d("Sockets", e.getLocalizedMessage());
                Toast.makeText(getBaseContext(),"ErrInside",Toast.LENGTH_SHORT).show();
            }
            return null;
        }
    }
    /////////////////////////////////////////////////////////////
    public static String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:",b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "02:00:00:00:00:00";
    }
/////////////////////////////////////////////////////////////////////



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main3, menu);
        return true;
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = pref.edit();
        if (id == R.id.senarios) {

        } else if (id == R.id.update) {
                try{
                    String b ;
                    Toast.makeText(getBaseContext(),hasan, Toast.LENGTH_SHORT).show();
                    b=pref.getString("DBA","0");
                    if (b.equals("1")){
                        Intent login = new Intent(getBaseContext(), WaitingActivity.class);
                        mContext.startActivity(login);
                        finish();
                    }

                editor.putString("MACadress",getMacAddr());
                }catch (Exception e) {

                }
        }
        else if (id == R.id.c ) {
            try{
                Intent intent = new Intent(Main3Activity.this, settings.class);
                startActivity(intent);
                finish();
            }catch (Exception e) {

            }
        }
        else if (id == R.id.nav_share) {
            Intent intent = new Intent(Main3Activity.this, contact.class);
            startActivity(intent);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
    @Override
    public void onResume(){
        super.onResume();
        try {
            new CreateCommThreadTask().execute();
        } catch (Exception e) {
            Log.d("Sockets", "onResomeError");
        }

    }
    @Override
    public void onPause() {
        super.onPause();
        new CloseSocketTask().execute();
    }

    @Override
    public void onStop() {
        super.onStop();
        new CloseSocketTask().execute();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        new CloseSocketTask().execute();
    }
}