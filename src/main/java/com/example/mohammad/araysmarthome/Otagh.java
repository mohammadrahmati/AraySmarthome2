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
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
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

import me.drakeet.materialdialog.MaterialDialog;


public class Otagh extends AppCompatActivity implements View.OnClickListener{
    private static final int SELECT_PICTURE = 100;
    private static final String TAG = "MainActivity";
    public final static String KEY_EXTRA_CONTACT_ID = "KEY_EXTRA_CONTACT_ID";
    private View root;
    private int currentBackgroundColor = 0xffffffff;
    CoordinatorLayout coordinatorLayout;
    FloatingActionButton btnSelectImage;
    AppCompatImageView imgView;

    /////////////////////////////////////////////////
    String Module_IP;
    static final String NICKNAME = "salam\r\n";
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
    public void sendToServer(String message) {
        try {{
            byte[] thr =
                    message.getBytes();
            new WriteToServerTask().execute(thr);
            }

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

ListView products_lv;
    @SuppressLint("WrongViewCast")
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        setContentView(R.layout.activity_otagh);
         final String data3 = getIntent().getStringExtra("EXTRA_SESSION_ID");

/////////////////////////////////////////////////////////////////////////////
        final SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = pref.edit();
//////////////////////////////////////////////////////////////////////////
        root = findViewById(R.id.Otagh);
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


        ///////////////////////////////////
        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.linearLayout2);
        Button btn = new Button(this);

        /*mainLayout.addView(btn);
        products_lv = (ListView) findViewById(R.id.listView);
        final ProductAdapter productAdapter = new ProductAdapter(this, R.layout.product_list_row);
        products_lv.setAdapter(productAdapter);


        Product pr;
        for(int i = 0; i < 10; i++){
            pr = new Product(data3+i, i);
            productAdapter.add(pr);
        }
        products_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String string= (String) productAdapter.getList().get(position).getName();
                Toast.makeText(getBaseContext(), string,Toast.LENGTH_SHORT).show();
            }
        });
*/
        ////////////////////////////////////////////////////////////////////////////
        btnSelectImage = (FloatingActionButton) findViewById(R.id.btnSelectImage);
        imgView = (AppCompatImageView) findViewById( R.id.aks);
        btnSelectImage.setOnClickListener(this);
        main();
        Switch mySwitch2 = (Switch) findViewById(R.id.switch2);
        Switch mySwitch3 = (Switch) findViewById(R.id.switch3);
        Switch mySwitch4 = (Switch) findViewById(R.id.switch4);
        mySwitch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    try {
                        sendToServer(data3 + ".k1.ON\r\n");
                    }
                    catch (Exception e) {
                        Toast.makeText(getBaseContext(),"ارتباط برقرار نیست",Toast.LENGTH_SHORT).show();}

                } else {
                    try{ sendToServer(data3+".k1.OFF\r\n");}
                    catch (Exception e) {
                        Toast.makeText(getBaseContext(),"ارتباط برقرار نیست",Toast.LENGTH_SHORT).show();}

                }
            }
        });

        mySwitch3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    try {
                        String adress = pref.getString("MACadress",null);
                        Toast.makeText(getBaseContext(),adress,Toast.LENGTH_SHORT).show();
                        sendToServer(data3 + ".k2.ON\r\n");
                    }
                    catch (Exception e) {
                        Toast.makeText(getBaseContext(),"ارتباط برقرار نیست",Toast.LENGTH_SHORT).show();}

                } else {
                    try{ sendToServer(data3+".k2.OFF\r\n");}
                    catch (Exception e) {
                        Toast.makeText(getBaseContext(),"ارتباط برقرار نیست",Toast.LENGTH_SHORT).show();}

                }
            }
        });

        mySwitch4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    try {
                        sendToServer(data3 + ".k3.ON\r\n");
                    }
                    catch (Exception e) {
                        Toast.makeText(getBaseContext(),"ارتباط برقرار نیست",Toast.LENGTH_SHORT).show();}

                } else {
                    try{ sendToServer(data3+".k3.OFF\r\n");}
                    catch (Exception e) {
                        Toast.makeText(getBaseContext(),"ارتباط برقرار نیست",Toast.LENGTH_SHORT).show();}

                }
            }
        });

    }
    private void changeBackgroundColor(int selectedColor) {
        currentBackgroundColor = selectedColor;
        root.setBackgroundColor(selectedColor);
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
//////////////////////////////////////////////////////////////


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