package com.example.mohammad.araysmarthome;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import android.util.Log;

import com.example.mohammad.araysmarthome.MainActivity;

public class CommsThread extends Thread {
    private final Socket socket;
    private final InputStream inputStream;
    private final OutputStream outputStream;

    public CommsThread(Socket sock) {
        socket = sock;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;
        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) {
            Log.d("SocketChat", e.getLocalizedMessage());
        }
        inputStream = tmpIn;
        outputStream = tmpOut;
    }

    public void run() {
        //---buffer store for the stream---
        byte[] buffer = new byte[1024];

        //---bytes returned from read()---
        int bytes;

        //---keep listening to the InputStream until an
        // exception occurs---
        while (true) {
            try {
                //---read from the inputStream---
                bytes = inputStream.read(buffer);

                //---update the main activity UI---
                Main3Activity.UIupdater.obtainMessage(0,bytes, -1, buffer).sendToTarget();
            } catch (IOException e) {
                break;
            }
        }
    }

    //---call this from the main activity to
    // send data to the remote device---
    public void write(byte[] thr) {
        try {
            outputStream.write(thr);
        } catch (IOException e) { }
    }

    //---call this from the main activity to
    // shutdown the connection---
    public void cancel() {
        try {
            socket.close();
        } catch (IOException e) { }
    }
}