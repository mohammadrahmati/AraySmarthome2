
        package com.example.mohammad.araysmarthome;


        import android.annotation.SuppressLint;
        import android.app.ActionBar;
        import android.app.Activity;
        import android.content.Context;
        import android.content.Intent;
        import android.content.pm.ActivityInfo;
        import android.net.DhcpInfo;
        import android.net.wifi.WifiManager;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.os.Handler;
        import android.support.v7.app.AppCompatActivity;
        import android.text.format.Formatter;
        import android.util.Log;
        import android.view.View;
        import android.widget.Toast;

        import java.io.IOException;
        import java.math.BigInteger;
        import java.net.InetAddress;
        import java.net.Socket;
        import java.net.UnknownHostException;
        import java.nio.ByteOrder;
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
                socket = new Socket(serverAddress, 1394);
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
        try {

            new CreateCommThreadTask().execute();

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                public void run() {
                    finish();
                    Intent intent = new Intent(WaitingActivity.this, Main3Activity.class);
                    startActivity(intent);
                }
            }, 2000);
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