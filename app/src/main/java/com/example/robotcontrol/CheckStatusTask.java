package com.example.robotcontrol;

import android.os.AsyncTask;
import android.widget.Toast;

import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
class CheckStatusTask extends AsyncTask<Object, Object, Boolean> {
    protected Boolean doInBackground(Object... arg0) {
        final String ip="192.168.4.1";
        final int port=80;
        final int timeout=300;
            String mas;
            try {
                Socket socket = new Socket();
                socket.connect(new InetSocketAddress(ip, port), timeout);
                socket.close();
                return true;
            }
            catch(ConnectException ce){
                ce.printStackTrace();
                mas="9"+ce;
               /* Toast.makeText(MainActivity.this,
                        mas,
                        Toast.LENGTH_LONG).show();*/
                return false;
            }
            catch (Exception ex) {
                ex.printStackTrace();
                mas=""+ex;
                /*Toast.makeText(MainActivity.this,
                        mas,
                        Toast.LENGTH_LONG).show();*/
                return false;
            }
    }
    protected void onPostExecute(Boolean flag) {
        // use your flag here to check true/false.
    }
}