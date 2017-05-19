package com.example.mohammad.araysmarthome;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.Toast;
import java.io.IOException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteOrder;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;


import static android.provider.ContactsContract.DisplayNameSources.NICKNAME;


public class MainActivity extends AppCompatActivity {

    /////////////////////////////////////////////////
    String Module_IP;
    static final String NICKNAME = "salam\r\n";
    InetAddress serverAddress;
    Socket socket;
    boolean connected = false;
    CommsThread commsThread;
    int info;
    String addresss;

  /////////////////////////////////////////////////////////////////

     static Handler UIupdater = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int numOfBytesReceived = msg.arg1;
            byte[] buffer = (byte[]) msg.obj;
            String strReceived = new String(buffer);
            strReceived = strReceived.substring(
                    0, numOfBytesReceived);

        }
    };

    public class CreateCommThreadTask extends AsyncTask<Void, Integer, Void> {
        @Override
        public Void doInBackground(Void... params) {
            try {
                //---create a socket---
                serverAddress =
                        InetAddress.getByName(Module_IP);
                socket = new Socket(serverAddress, 1394);
                commsThread = new CommsThread(socket);
                commsThread.start();
                //---sign in for the user; sends the nick name---
                sendToServer(NICKNAME);
                connected = true;
            } catch (UnknownHostException e) {
                Log.d("Sockets", e.getLocalizedMessage());
            } catch (IOException e) {
                Log.d("Sockets", e.getLocalizedMessage());
            }
            return null;
        }

    }
    ////////////////////////////////////////////////////////////
    private class WriteToServerTask extends AsyncTask
            <byte[], Void, Void> {
        protected Void doInBackground(byte[]...data) {
            commsThread.write(data[0]);
            return null;
        }
    }
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashsceen);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        @SuppressLint("CommitPrefEdits") final SharedPreferences.Editor editor = pref.edit();
        boolean a =pref.getBoolean("onlangaugeClick",false);
        if (a) {
            String languageToLoad = "pe";
            Locale locale = new Locale(languageToLoad);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        }else{
            String languageToLoad="en";
            Locale locale = new Locale(languageToLoad);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getResources().updateConfiguration(config,getResources().getDisplayMetrics());
        }
        main();
       /* try {
            new CreateCommThreadTask().execute();
            *//*sendToServer("ON\r\n");*//*
        } catch (Exception e) {
            Log.d("Sockets", "onResomeError");
        }*/

              Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    public void run() {
                        Intent intent = new Intent(MainActivity.this, WaitingActivity.class);
                        startActivity(intent);
                    finish();
                    }
                }, 1000);
        }
    ////////////////////////////////////////////////////////////
    private void sendToServer(String message) {
        byte[] theByteArray =
                message.getBytes();
        new WriteToServerTask().execute(theByteArray);
    }
    ////////////////////////////////////////////////////////////

    protected String wifiIpAddress(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
        int ipAddress = wifiManager.getConnectionInfo().getIpAddress();

        // Convert little-endian to big-endian if needed
        if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) {
            ipAddress = Integer.reverseBytes(ipAddress);
        }

        byte[] ipByteArray = BigInteger.valueOf(ipAddress).toByteArray();

        String ipAddressString;
        try {
            ipAddressString = InetAddress.getByAddress(ipByteArray).getHostAddress();
        } catch (UnknownHostException ex) {
            Log.e("WIFIIP", "Unable to get host address.");
            ipAddressString = "Not Connect!";
        }

        return ipAddressString;
    }

    ////////////////////////////////////////////////////////////
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





    public void main(){


        @SuppressLint("WifiManagerLeak") final WifiManager manager = (WifiManager) super.getSystemService(WIFI_SERVICE);
        final DhcpInfo dhcp = manager.getDhcpInfo();
        final String address = Formatter.formatIpAddress(dhcp.gateway);
        @SuppressLint("WifiManagerLeak") WifiManager managerr = (WifiManager) getSystemService(Context.WIFI_SERVICE);


        Module_IP = address;
       /* mohammad2.setText(Module_IP);*/
        /*try{
            new CreateCommThreadTask().execute();
        }catch(Exception e){
            Log.d("Sockets","onResomeError");

        }*/

    }


}



