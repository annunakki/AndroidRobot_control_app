package com.stevensit.www.cpe556_interface_app;
//
//import android.bluetooth.BluetoothAdapter;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.view.View;
//import android.widget.Button;
//import android.widget.Switch;
//
//
//public class MainActivity extends AppCompatActivity  {
//
//    private Button btnSettings, btnBTConnect, btnForward, btnReverse, btnLeft, btnRight, btnCameraCenter;
//    private Switch swCamera;
//    private BluetoothAdapter btAdapter;
//
//    private final String msgStart = "Robot is Ready";
//
//@Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//       setContentView(R.layout.main_activity) ;
//
//////
//////        btAdapter = BluetoothAdapter.getDefaultAdapter();
//////        if (btAdapter == null) {
//////            Toast.makeText(getApplicationContext(), "BT adapter is not compatible", Toast.LENGTH_LONG).show();
////////            new AlertDialog.Builder(this)
////////                    .setTitle("Adapter is not Compatible")
////////                    .setMessage("BT adapter is not compatible")
////////                    .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
////////                        @Override
////////                        public void onClick(DialogInterface dialog, int which) {
////////                            //System.exit(1);
////////                            Toast.makeText(getApplicationContext(), "BT adapter is not compatible", Toast.LENGTH_LONG).show();
////////                        }
////////                    }).setIcon(android.R.drawable.ic_dialog_alert).show();
//////        } else {
//////            if (btAdapter.isEnabled()) {
//////                Toast.makeText(getApplicationContext(), "Bluetooth is Enabled", Toast.LENGTH_LONG).show();
//////            } else {
//////                Intent turnBtn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//////                startActivityForResult(turnBtn, 1);
//////            }
//////        }
//    }
//
//
//    public void btMenu(View v){
//
//        Intent intentSettings = new Intent(this,BluetoothMenu.class);
//        startActivity(intentSettings);
//
//
//    }
////
//    public void settingMenu(View v){
//
//        Intent intentSettings = new Intent(this,SettingMenu.class);
//        startActivity(intentSettings);
//
//
//    }
//
//
//}

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static android.view.animation.Animation.REVERSE;

public class MainActivity extends Activity implements OnClickListener, SensorEventListener {


    String address = null , name=null;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    Set<BluetoothDevice> pairedDevices;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private Button btnSettings, btnBTConnect, btnForward, btnReverse, btnLeft, btnRight, btnCameraCenter;
    private TextView textStatus,blinkText,robotReadyStatus;
    private Switch cameraSW;
    private ImageButton btImage;
    public final String msgForward = "MOVING FORWARD";
    public  final String msgStop = "ROBOT STOPPED";
    public final String msgBackward = "MOVING IN REVERSE";
    public  final String msgLeft = "TURNING LEFT";
    public final String msgRight = "TURNING RIGHT";

    //=======sensors segment ========

    private SensorManager mSensorManager;
    public  Sensor snsAccel, snsGyro;
    private List snListAccel, snListGyro;
    public TextView txtAccelSensorOutput, txtGyroSensorOut;
    public SensorEventListener gyroListener, accelListener;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
       // System.out.println("current orientation "+getResources().getConfiguration().orientation);
      try {initialize();} catch (Exception e) {}


        sensorsSystemInitialize();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        int orientation = newConfig.orientation;
        System.out.println(orientation);
        if(orientation == Configuration.ORIENTATION_PORTRAIT){
            System.out.println("screen is portrait");
            System.out.println(orientation);
        }
        else
            System.out.println(orientation);


    }

    public void setBlinkingText (TextView obj)  {  // to enable the blinking option on the selected textView object


        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(10000);
        anim.setStartOffset(10000);
        anim.setRepeatMode(REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        obj.setAnimation(anim);
    }

    public void setBlinkingImage (ImageButton obj)  {  // to enable the blinking option on the selected image button object


        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(500);
        anim.setStartOffset(50);
        anim.setRepeatMode(REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        obj.setAnimation(anim);
    }

    @SuppressLint("ClickableViewAccessibility")

    private void initialize() {

        btnForward =  findViewById(R.id.btnForward);
        btnReverse =  findViewById((R.id.btnReverse));
        btnLeft = findViewById(R.id.btnTurnLeft);
        btnRight =  findViewById(R.id.btnTurnRight);
        textStatus =  findViewById(R.id.textStatusBox);
        btImage =  findViewById(R.id.btnImage);
        robotReadyStatus = findViewById(R.id.txtRobotONStatus);
        cameraSW =  findViewById(R.id.swCamera) ;
        btImage.setVisibility(View.INVISIBLE);
        robotReadyStatus.setVisibility(View.INVISIBLE);
        txtAccelSensorOutput = findViewById(R.id.txtAccelSensorOut);
        txtGyroSensorOut = findViewById(R.id.txtGyroSensorOut);


//        btnForward.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                moveForward();
//            }
//        });

//        setButtonListener (btnForward, "F");  //move forward
//        setButtonListener (btnReverse, "B");  // move reverse or backward
//        setButtonListener (btnLeft, "L");  // move to the left
//        setButtonListener (btnRight, "R");  //move to the right

////
//        t1=(TextView)findViewById(R.id.textStatusBox);
        ///bluetooth_connect_device();

//        i1=(Button)findViewById(R.id.btnForward);

//        i1.setOnTouchListener(new View.OnTouchListener()
//        {   @Override
//        public boolean onTouch(View v, MotionEvent event){
//            if(event.getAction() == MotionEvent.ACTION_DOWN) {led_on_off("1");
//            System.out.print("forward down\n");}
//            if(event.getAction() == MotionEvent.ACTION_UP){led_on_off("0");
//                System.out.print("forward up\n");}
//            return true;}
//        });
    }


    @SuppressLint({"HardwareIds"})
    public void connectBluetooth(View v) throws IOException {

        myBluetooth = BluetoothAdapter.getDefaultAdapter();
        System.out.println("bt_device_name " + myBluetooth);
        if (myBluetooth == null)
            Toast.makeText(getApplicationContext(), "The BT device is not detected or not compatible", Toast.LENGTH_SHORT).show();
        else if (!myBluetooth.isEnabled()) {
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, 0);
            Toast.makeText(getApplicationContext(), "BT is Turned on", Toast.LENGTH_LONG).show();
        }else{
            //address = myBluetooth.getAddress();
            pairedDevices = myBluetooth.getBondedDevices();

            if (pairedDevices.size() > 0) {
                for (BluetoothDevice bt : pairedDevices) {
                    address = bt.getAddress();
                    name = bt.getName();
                }
            }

            myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
            BluetoothDevice btDevice = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
            btSocket = btDevice.createInsecureRfcommSocketToServiceRecord(myUUID);//create paired connection


            new Handler().postDelayed(new Runnable() { // add a delay before connecting the bt socket to the paired device
                @Override
                public void run() {
                    try {
                        btSocket.connect();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            },500);

            connectedMSG();
        }
    }

    @SuppressLint("SetTextI18n")
    public void connectedMSG (){
        Toast.makeText(getApplicationContext(), "Now connected to"+name, Toast.LENGTH_SHORT).show();
        btImage.setVisibility(View.VISIBLE);
        robotReadyStatus.setVisibility(View.VISIBLE);
        setBlinkingImage(btImage);
        textStatus.setText("BT Name: " + name + "\nBT Address: " + address);
    }


//    @SuppressLint("ClickableViewAccessibility")
//
//    public void  setButtonListener(final Button btn, final String chr, final String msg){
//
//        btnForward.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    setBtnStreamChar(chr);
//                    moveScreenStatus(msg);
//
//                }
//                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    displayStopMSG();
//                }
//                return true;
//            }});
//        }

   public void displayStopMSG(){
        setBtnStreamChar("0");
        System.out.println(msgStop);
        textStatus.setText(msgStop);
    }

    public void moveScreenStatus(String s){

        System.out.println(s);
        textStatus.setText(s);

    }

    @SuppressLint("ClickableViewAccessibility")
    public void moveForward (View v){

       // setButtonListener(btnForward, "F", msgForward);

        btnForward.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    setBtnStreamChar("F");
                    moveScreenStatus(msgForward);

                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    displayStopMSG();
                }
                return true;
            }});

    }

    @SuppressLint("ClickableViewAccessibility")
    public void moveReverse (View v){

       // setButtonListener(btnReverse, "R", msgBackward);

        btnReverse.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    setBtnStreamChar("B");
                    moveScreenStatus(msgBackward);

                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    displayStopMSG();
                }
                return true;
            }});

    }


    @SuppressLint("ClickableViewAccessibility")
    public void turnLeft (View v){

        //setButtonListener(btnLeft, "L", msgLeft);

        btnLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    setBtnStreamChar("F");
                    moveScreenStatus(msgLeft);

                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    displayStopMSG();
                }
                return true;
            }});

    }

    @SuppressLint("ClickableViewAccessibility")
    public void turnRight (View v){

     //   setButtonListener(btnRight, "F", msgRight);

        btnRight.setOnTouchListener(new View.OnTouchListener() {
            @Override

            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    setBtnStreamChar("F");
                    moveScreenStatus(msgRight);

                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    displayStopMSG();
                }
                return true;
            }});

    }


    @Override
    public void onClick(View v)
    {
        try
        {
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void setBtnStreamChar (String streamChr){  // sets the assigned character to the selected button
        try
        {
            if (btSocket!=null)
            {
                btSocket.getOutputStream().write(streamChr.getBytes());
                System.out.println(btSocket.toString());
                System.out.println(btSocket.getOutputStream());
                System.out.println("===============================");
            }
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


//    public void btMenu(View v){
//
//        Intent intentSettings = new Intent(this,BluetoothMenu.class);
//        startActivity(intentSettings);
//
//
//    }

    public void settingMenu(View v){

        Intent intentSettings = new Intent(this,SettingMenu.class);
        startActivity(intentSettings);

    }


//    public void cameraSwitch(View v) {
//        if (cameraSW.isChecked()){
//            cameraSW.setTextOff("Camera ON");
//        }else{
//            cameraSW.setTextOff("Camera OFF");
//        }

    public void sensorsSystemInitialize() {

        final DecimalFormat numFormat = new DecimalFormat("#.#"); // to change sensor output decimal resolution
//        txtAccelSensorOutput = findViewById(R.id.txtAccelSensorOut);
//        txtGyroSensorOut = findViewById(R.id.txtGyroSensorOut);

        accelListener = new SensorsSystem();
        gyroListener = new SensorsSystem();
//
//            @Override
//
//            public void onSensorChanged(SensorEvent event) {
//                float[] values = event.values;
//                txtAccelSensorOutput.setText("Linear Accel\n"+"X:" + roundDecimalNum(values[0],numFormat) + "\n" + "Y:" + roundDecimalNum(values[1],numFormat) + "\n" + "Z:" + roundDecimalNum(values[2],numFormat));
//              //  txtGyroSensorOut.setText("Gyro rad/s \n"+"X:" + roundDecimalNum(values[0],numFormat) + "\n" + "Y:" + roundDecimalNum(values[1],numFormat) + "\n" + "Z:" + roundDecimalNum(values[2],numFormat));
//
//            }
//            @Override
//
//            public void onAccuracyChanged(Sensor sensor, int accuracy) {
//
//            }
//        };


//            @Override
//
//            public void onSensorChanged(SensorEvent event) {
//
//                float[] values = event.values;
//               // txtAccelSensorOutput.setText("Linear Accel\n"+"X:" + roundDecimalNum(values[0],numFormat) + "\n" + "Y:" + roundDecimalNum(values[1],numFormat) + "\n" + "Z:" + roundDecimalNum(values[2],numFormat));
//                txtGyroSensorOut.setText("Gyro rad/s \n"+"X:" + roundDecimalNum(values[0],numFormat) + "\n" + "Y:" + roundDecimalNum(values[1],numFormat) + "\n" + "Z:" + roundDecimalNum(values[2],numFormat));
//
//            }
//            @Override
//
//            public void onAccuracyChanged(Sensor sensor, int accuracy) {
//
//            }
//        };

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        snsAccel = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        snsGyro = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        snListAccel = mSensorManager.getSensorList(Sensor.TYPE_LINEAR_ACCELERATION);
        snListGyro = mSensorManager.getSensorList(Sensor.TYPE_GYROSCOPE);

        if (snListAccel.size() > 0)
            mSensorManager.registerListener(accelListener, (Sensor) snListAccel.get(0), SensorManager.SENSOR_DELAY_NORMAL);
        else
            Toast.makeText(getApplicationContext(),"no accelerometer sensor detected",Toast.LENGTH_SHORT).show();

        if (snListGyro.size() > 0)
            mSensorManager.registerListener(gyroListener, (Sensor) snListGyro.get(0), SensorManager.SENSOR_DELAY_NORMAL);
        else
            Toast.makeText(getApplicationContext(),"no gyroscope sensor detected",Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onStop() {
        if (snListAccel.size() > 0) {
            mSensorManager.unregisterListener(accelListener);
        }
        if (snListGyro.size() > 0) {
            mSensorManager.unregisterListener(gyroListener);
        }
        super.onStop();
    }

    public double roundDecimalNum(float num, DecimalFormat numFormat){  // to round the output number to the desired format
//        DecimalFormat numFormat = new DecimalFormat("#.##");
        return Double.valueOf(numFormat.format(num));
    }



    }
//
//    public class SensorListener implements SensorEventListener{
//
//
//      @Override
//        public void onSensorChanged(SensorEvent event) {
//
//          txtAccelSensorOutput.setText("Linear Accel\n"+"X:" + roundDecimalNum(values[0],numFormat) + "\n" + "Y:" + roundDecimalNum(values[1],numFormat) + "\n" + "Z:" + roundDecimalNum(values[2],numFormat));
//          txtGyroSensorOut.setText("Gyro rad/s \n"+"X:" + roundDecimalNum(values[0],numFormat) + "\n" + "Y:" + roundDecimalNum(values[1],numFormat) + "\n" + "Z:" + roundDecimalNum(values[2],numFormat));
//
//
//        }
//
//        @Override
//        public void onAccuracyChanged(Sensor sensor, int accuracy) {
//
//        }
//    }



