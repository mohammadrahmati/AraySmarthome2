package com.example.mohammad.araysmarthome;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.DhcpInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.content.pm.ActivityInfo;
import android.support.v7.widget.AppCompatImageView;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import me.drakeet.materialdialog.MaterialDialog;


public class Otagh extends AppCompatActivity implements View.OnClickListener{
    private static final int SELECT_PICTURE = 100;
    private static final String TAG = "MainActivity";
    public final static String KEY_EXTRA_CONTACT_ID = "KEY_EXTRA_CONTACT_ID";
    //private View root;//for baclground color
    private int currentBackgroundColor = 0xffffffff;
    CoordinatorLayout coordinatorLayout;
    FloatingActionButton btnSelectImage;
    AppCompatImageView imgView;
    RecyclerView recyclerView;
    /////////////////////////////////////////////////
    String Module_IP;
    InetAddress serverAddress;
    Socket socket;
    boolean connected = false;
    CommsThread commsThread;
    String addresss;
    int info;


    /////////////////////////////////////////////////////////////////
    public void main(){


        @SuppressLint("WifiManagerLeak") final WifiManager manager = (WifiManager) super.getSystemService(WIFI_SERVICE);
        final DhcpInfo dhcp = manager.getDhcpInfo();
        final String address = Formatter.formatIpAddress(dhcp.gateway);
        @SuppressLint("WifiManagerLeak") WifiManager managerr = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        Module_IP = address;

    }

////////////////////////////
static Handler UIupdater = new Handler() {
    @Override
    public void handleMessage(Message msg) {
        int numOfBytesReceived = msg.arg1;
        byte[] buffer = (byte[]) msg.obj;

        //---convert the entire byte array to string---
        String strReceived = new String(buffer);

        //---extract only the actual string received---
        strReceived = strReceived.substring(
                0, numOfBytesReceived);


        if (strReceived.contains("v5")) {

        }

        if (strReceived.contains("RELAY TURN OFF")) {
                /*lamp.setImageResource(R.drawable.l_off);*/
        }
        //---display the text received on the TextView---
        // txtMessagesReceived.setText(txtMessagesReceived.getText().toString() + strReceived);
    }
};






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
    public void sendToServer(String message)
    {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        try {
            byte[] thr =
                    message.getBytes();
            new WriteToServerTask().execute(thr);


        } catch (Exception e) {

            Toast.makeText(getBaseContext(),"ErrInside",Toast.LENGTH_SHORT).show();
        }
  /*      byte[] thr =
                message.getBytes();
        new WriteToServerTask().execute(thr);*/
    }

    private class WriteToServerTask extends AsyncTask<byte[], Void, Void> {

        protected Void doInBackground(byte[]...thr) {
            try{
            commsThread.write(thr[0]);}
            catch (Exception e) {
            }
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
///////////////////////////////////////////////////

    String[][] parts;
    static final String[] NICKNAMES = { "f1:u3:r4:m3:01001101", "f1:u3:r4:m3:01010101" };
    RecyclerView products_lv;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        setContentView(R.layout.activity_otagh);
        setUpRecyclerView();
        ///////////////////////////////////////////////////////////////////////////////////
         final String data3 = getIntent().getStringExtra("EXTRA_SESSION_ID");//recieved data
        /////////////////////////////////////////////////////////////////////////////
        final SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = pref.edit();
        //////////////////////////////////////////////////////////////////////////   (RGB)
        //root = findViewById(R.id.Otagh); //for background color
        changeBackgroundColor(currentBackgroundColor);
        findViewById(R.id.RGB).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Context context = Otagh.this;

                ColorPickerDialogBuilder
                        .with(context)
                        .setTitle(R.string.color_dialog_title)
                        .initialColor(currentBackgroundColor)
                        .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                        .density(12)
                        .setOnColorSelectedListener(new OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(int selectedColor) {

                            }
                        })
                        .setPositiveButton("ok", new ColorPickerClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                                changeBackgroundColor(selectedColor);
                                if (allColors != null) {
                                    StringBuilder sb = null;

                                    for (Integer color : allColors) {
                                        if (color == null)
                                            continue;
                                        if (sb == null)
                                            sb = new StringBuilder("Color List:");
                                        sb.append("\r\n#" + Integer.toHexString(color).toUpperCase());
                                    }

                                    if (sb != null)
                                        Toast.makeText(getApplicationContext(), sb.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .showColorEdit(true)
                        .setColorEditTextColor(ContextCompat.getColor(Otagh.this, android.R.color.holo_blue_bright))
                        .build()
                        .show();
            }
        });
        ///////////////////////////////////////////////////////////////////////

        btnSelectImage = (FloatingActionButton) findViewById(R.id.btnSelectImage);
        imgView = (AppCompatImageView) findViewById( R.id.aks);
        btnSelectImage.setOnClickListener(this);
        main();
    }
    RecyclerAdapter ra=new RecyclerAdapter(new Runnable() {
        @Override
        public void run() {

        }
    }, new Runnable() {
        @Override
        public void run() {


        }
    });
    private void setUpRecyclerView() {


     /*   recyclerView = (RecyclerView) findViewById(R.id.otagh_listView1);
        RecyclerAdapter adapter = new RecyclerAdapter(getApplicationContext(),0);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(getApplicationContext());
        mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLinearLayoutManagerVertical);

        //recyclerView.setItemAnimator(new DefaultItemAnimator());*/
    }
    private void changeBackgroundColor(int selectedColor) {
        currentBackgroundColor = selectedColor;
       // root.setBackgroundColor(selectedColor);
    }
    void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                // Get the url from data
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // Get the path from the Uri
                    String path = getPathFromURI(selectedImageUri);
                    Log.i(TAG, "Image Path : " + path);
                    // Set the image in ImageView
                    imgView.setImageURI(selectedImageUri);
                    imgView.getScaleType();
                    imgView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                }
            }
        }
    }


    /* Get the real path from the URI */
    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    @Override
    public void onClick(View v) {
        openImageChooser();
    }
    ////////////////////////////////////////////////////////////////////
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
    public void onBackPressed(){
        this.finish();
    }
}