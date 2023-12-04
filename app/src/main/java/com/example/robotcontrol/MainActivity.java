package com.example.robotcontrol;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.os.Bundle;
import android.widget.Toast;
import java.io.IOException;
import java.net.ConnectException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class MainActivity extends Activity  implements SensorEventListener {

    RelativeLayout layout_joystick;
    ImageView image_joystick, image_border;
    TextView textView1, textView2, textView3, textView4, textView5, textView6;
    private Sensor Accelerometer;
    private SensorManager sm;
    JoyStickClass js;
    String str=null,mas;
    int D,A;
    int mport=80;
    byte[] send_data = new byte[1024];
    private final int INTERNET_PERMISSION_CODE = 1;
    private final int ACCESS_NETWORK_STATE_PERMISSION_CODE = 2;
    private final int ACCESS_WIFI_STATE_PERMISSION_CODE = 3;

    public void send() throws IOException  {
        DatagramSocket client_socket = new DatagramSocket(mport);

        InetAddress IPAddress =  InetAddress.getByName("192.168.4.1");

        send_data = str.getBytes();

        DatagramPacket send_packet = new DatagramPacket(send_data,str.length(), IPAddress, mport);

        client_socket.send(send_packet);
    }
    float x,y,z;

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        x=sensorEvent.values[0];
        y=sensorEvent.values[1];
        z=sensorEvent.values[2];
        str=("x="+x+" y="+y+" z="+z);
        str="temp";
        /*try {
            send();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(MainActivity.this,
                    "Can't send, Acc",
                    Toast.LENGTH_SHORT).show();
        }*/

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case INTERNET_PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(MainActivity.this, "INTERNET permission granted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(MainActivity.this, "INTERNET permission Denied", Toast.LENGTH_SHORT).show();
            case ACCESS_NETWORK_STATE_PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(MainActivity.this, "ACCESS_NETWORK_STATE permission granted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(MainActivity.this, "ACCESS_NETWORK_STATE permission Denied", Toast.LENGTH_SHORT).show();
            case ACCESS_WIFI_STATE_PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(MainActivity.this, "ACCESS_WIFI_STATE permission granted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(MainActivity.this, "ACCESS_WIFI_STATE permission Denied", Toast.LENGTH_SHORT).show();
        }
    }
    public boolean isPortOpen(final String ip, final int port, final int timeout) {

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
            Toast.makeText(MainActivity.this,
                    mas,
                    Toast.LENGTH_LONG).show();
            return false;
        }

        catch (Exception ex) {
            ex.printStackTrace();
            mas=""+ex;
            Toast.makeText(MainActivity.this,
                    mas,
                    Toast.LENGTH_LONG).show();
            return false;
        }
    }
    public void onCreate(Bundle savedInstanceState) {
        boolean b;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //b=isPortOpen("192.168.4.1",mport,300);
        new CheckStatusTask().execute();
       /* if (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED)
            Toast.makeText(MainActivity.this, "INTERNET permission already granted", Toast.LENGTH_SHORT).show();
        if (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.INTERNET}, INTERNET_PERMISSION_CODE );
        else
            Toast.makeText(MainActivity.this, "INTERNET permission already granted", Toast.LENGTH_SHORT).show();

        if (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_WIFI_STATE}, ACCESS_WIFI_STATE_PERMISSION_CODE );
        else
            Toast.makeText(MainActivity.this, "ACCESS_WIFI_STATE permission already granted", Toast.LENGTH_SHORT).show();

        if (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_NETWORK_STATE}, ACCESS_NETWORK_STATE_PERMISSION_CODE );
        else
            Toast.makeText(MainActivity.this, "ACCESS_NETWORK_STATE permission already granted", Toast.LENGTH_SHORT).show();*/

        sm=(SensorManager)getSystemService(SENSOR_SERVICE);
        Accelerometer=sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sm.registerListener(this,Accelerometer,SensorManager.SENSOR_DELAY_NORMAL);

        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);
        textView4 = findViewById(R.id.textView4);
        textView5 = findViewById(R.id.textView5);
        textView6 = findViewById(R.id.textView6);

        layout_joystick = findViewById(R.id.layout_joystick);

        js = new JoyStickClass(getApplicationContext(), layout_joystick, R.drawable.image_button);
        js.setStickSize(150, 150);
        js.setLayoutSize(500, 500);
        js.setLayoutAlpha(150);
        js.setStickAlpha(100);
        js.setOffset(90);
        js.setMinimumDistance(50);
        layout_joystick.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                js.drawStick(arg1);
                if(arg1.getAction() == MotionEvent.ACTION_DOWN
                        || arg1.getAction() == MotionEvent.ACTION_MOVE) {
                    textView1.setText("X : " + String.valueOf(js.getX()));
                    textView2.setText("Y : " + String.valueOf(js.getY()));
                    textView3.setText("Angle : " + String.valueOf(js.getAngle()));
                    textView4.setText("Distance : " + String.valueOf(js.getDistance()));
                    A=(int)(js.getAngle()/360*255);
                    D=(int)(js.getDistance()/240*255);
                    if(D>255)
                        D=255;
                    str=("A"+A+"D"+D);
                    textView6.setText(str);
                    MessageSender massegesender=new MessageSender();
                    massegesender.execute(str);
                    /*try {
                        send();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        mas=""+e;
                        Toast.makeText(MainActivity.this,
                                mas,
                                Toast.LENGTH_SHORT).show();
                    }*/
                    int direction = js.get8Direction();
                    if(direction == JoyStickClass.STICK_UP) {
                        textView5.setText("Direction : Up");
                    } else if(direction == JoyStickClass.STICK_UPRIGHT) {
                        textView5.setText("Direction : Up Right");
                    } else if(direction == JoyStickClass.STICK_RIGHT) {
                        textView5.setText("Direction : Right");
                    } else if(direction == JoyStickClass.STICK_DOWNRIGHT) {
                        textView5.setText("Direction : Down Right");
                    } else if(direction == JoyStickClass.STICK_DOWN) {
                        textView5.setText("Direction : Down");
                    } else if(direction == JoyStickClass.STICK_DOWNLEFT) {
                        textView5.setText("Direction : Down Left");
                    } else if(direction == JoyStickClass.STICK_LEFT) {
                        textView5.setText("Direction : Left");
                    } else if(direction == JoyStickClass.STICK_UPLEFT) {
                        textView5.setText("Direction : Up Left");
                    } else if(direction == JoyStickClass.STICK_NONE) {
                        textView5.setText("Direction : Center");
                    }
                } else if(arg1.getAction() == MotionEvent.ACTION_UP) {
                    textView1.setText("X :");
                    textView2.setText("Y :");
                    textView3.setText("Angle :");
                    textView4.setText("Distance :");
                    textView5.setText("Direction :");
                }
                return true;
            }
        });
    }
}
