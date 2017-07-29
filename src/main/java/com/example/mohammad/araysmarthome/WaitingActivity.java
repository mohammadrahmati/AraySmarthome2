
        package com.example.mohammad.araysmarthome;


        import android.annotation.SuppressLint;
        import android.app.ActionBar;
        import android.app.Activity;
        import android.content.Context;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import java.util.Random;
        import android.content.pm.ActivityInfo;
        import android.net.DhcpInfo;
        import android.net.wifi.WifiManager;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.os.Handler;
        import android.os.Message;
        import android.support.v7.app.AppCompatActivity;
        import android.text.format.Formatter;
        import android.util.Log;
        import android.view.View;
        import android.widget.TextView;
        import android.widget.Toast;

        import java.io.IOException;
        import java.math.BigInteger;
        import java.net.InetAddress;
        import java.net.Socket;
        import java.net.UnknownHostException;
        import java.nio.ByteOrder;
        import java.util.Random;
        import java.util.StringTokenizer;
        import java.util.Timer;
        import java.util.TimerTask;

        import me.drakeet.materialdialog.MaterialDialog;


public class WaitingActivity extends AppCompatActivity {
    /////////////////////////////////////////////////
    String Module_IP;
    static final String NICKNAME = "salam\r\n";
    InetAddress serverAddress;
    Socket socket;
    boolean connected = false;
    CommsThread commsThread;
    String addresss;
    int info;
    public static Context mContext;
    MaterialDialog mMaterialDialog;
    /////////////////////////////////////////////////////////////////
    public void main(){


        @SuppressLint("WifiManagerLeak") final WifiManager manager = (WifiManager) super.getSystemService(WIFI_SERVICE);
        final DhcpInfo dhcp = manager.getDhcpInfo();
        final String address = Formatter.formatIpAddress(dhcp.gateway);
        @SuppressLint("WifiManagerLeak") WifiManager managerr = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        Module_IP = address;

    }

    public class CreateCommThreadTask extends AsyncTask<Void, Integer, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                serverAddress =
                        InetAddress.getByName(Module_IP);
                socket = new Socket(serverAddress, 1716);
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
        byte[] thr =
                message.getBytes();
        new WriteToServerTask().execute(thr);
    }

    private class WriteToServerTask extends AsyncTask<byte[], Void, Void> {
        protected Void doInBackground(byte[]...thr) {
            commsThread.write(thr[0]);
            return null;
        }
    }
    //////////////////////////////////////////
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
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.waiting);
        main();
        mContext=this;

////////////////////////////////////////////////////////////
        mMaterialDialog = new MaterialDialog(this)
                .setTitle("خطا")
                .setMessage("شما به دستگاه متصل نیستید")
                .setPositiveButton("خروج", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();
                        finish();
                    }
                });
        ////////////////////////////////////////////////////////////

    }
    protected void wifiIpAddress(Context context) {
        @SuppressLint("WifiManagerPotentialLeak") WifiManager wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
        int ipAddress = wifiManager.getConnectionInfo().getIpAddress();
        @SuppressLint("HardwareIds") String Mac = String.valueOf(wifiManager.getConnectionInfo().getMacAddress());
        sendToServer(Mac+"\r\n");

    }
    @Override
    public void onResume(){
        super.onResume();
        if(0==0){
        try {
            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
            @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = pref.edit();
            new CreateCommThreadTask().execute();
            ///////////////////////////////////////////////////
            String DBA=pref.getString("DBA","0");
            if (DBA.equals("1")) {
                 for(int i = 0;i<10;i++) {
                     String data = pref.getString("database", null);
                     StringTokenizer sec = new StringTokenizer(data, ":");
                     String New = sec.nextToken();
                     String code = sec.nextToken();
                     Log.i("sadasdasd", "Asdasdsda");
                     String floor = sec.nextToken();
                     String unit = sec.nextToken();
                     String room = sec.nextToken();
                     String type = sec.nextToken();
                     editor.putString("m" + 1, code);
                     floor = floor.substring(1, 2);
                     unit = unit.substring(1, 2);
                     room = room.substring(1, 2);
                     Log.i("sadasdasd", "Asdasdsda");
                 }






               /* final int a = Integer.parseInt(Count);
                String []madule = new String[a];
                int h =0,k;
                String s;
                for(int i=0; i<a; i++) {
                    k = MainDataString.indexOf("new",h);
                    madule[i]= MainDataString.substring(h,k);
                    s=String.valueOf(i);
                    editor.putString("madule"+s, madule[i]);
                    editor.apply();
                    Toast.makeText(getBaseContext(),madule[i], Toast.LENGTH_SHORT).show();
                    h=k+3;
                }*/
            editor.putString(DBA,"0");
            }
            ///////////////////////////////////////////////////

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                public void run() {
                    finish();
                    Intent intent = new Intent(WaitingActivity.this, Main3Activity.class);
                    startActivity(intent);
                    finish();
                }
            }, 1000);
        } catch (Exception e) {
            Log.d("Sockets", "onResomeError");
        }}
        else {
            mMaterialDialog.show();
        }

    }
    private String getRandomHexString(int numchars){
        Random r = new Random();
        StringBuffer sb = new StringBuffer();
        while(sb.length() < numchars){
            sb.append(Integer.toHexString(r.nextInt()));
        }

        return sb.toString().substring(0, numchars);
    }
    private static String hasan;
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


        }
    };
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