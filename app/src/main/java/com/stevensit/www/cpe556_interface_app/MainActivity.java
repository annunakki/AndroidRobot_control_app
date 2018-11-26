package com.stevensit.www.cpe556_interface_app;


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
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.Set;
import java.util.UUID;

import static android.view.animation.Animation.REVERSE;

public class MainActivity extends Activity implements OnClickListener, SensorEventListener {

    private String address = null , name=null;
    private BluetoothAdapter myBluetooth = null;
    private BluetoothSocket btSocket = null;
    private OutputStream btOutputStream=null;
    private Set<BluetoothDevice> pairedDevices;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private Button btnSettings, btnBTConnect, btnForward, btnReverse, btnLeft, btnRight, btnCameraCenter;
    public TextView textStatus,blinkText,robotReadyStatus;
    private Switch cameraSW;
    private ImageButton btImage;
    public final String msgForward = "MOVING FORWARD";
    public  final String msgStop = "ROBOT STOPPED";
    public final String msgBackward = "MOVING IN REVERSE";
    public  final String msgLeft = "TURNING LEFT";
    public final String msgRight = "TURNING RIGHT";

    //=======sensors segment ========

    private SensorManager mSensorManager;
    public  Sensor snsAccelerometer, snsGyroscopeVector;
    private TextView txtAccelSensorOut, txtGyroSensorOut;
    private float [] rotationMatrix = new float[9];
    private float [] orientationValues = new float[3];



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
    public void connectBluetooth(View v) throws IOException {  // bluetooth connection handler

        myBluetooth = BluetoothAdapter.getDefaultAdapter();
        System.out.println("bt_device_name " + myBluetooth);
        if (myBluetooth == null)
            Toast.makeText(getApplicationContext(), "The BT device is not detected or not compatible", Toast.LENGTH_SHORT).show();
        else if (!myBluetooth.isEnabled()) {
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, 0);
            Toast.makeText(getApplicationContext(), "BT is Turned on", Toast.LENGTH_LONG).show();
        }else{
            address = myBluetooth.getAddress();
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
                        Toast.makeText(getApplicationContext(),"BT connection error\n" +e.getMessage(), Toast.LENGTH_SHORT).show();

                        try {
                            btSocket.close();
                        } catch (IOException e1) {
                            Toast.makeText(getApplicationContext(),"BT connection error\n" +e1.getMessage(), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                }
            },500);

            btConnectedMSG();
        }
    }

    private void sendToBtStream (String streamChr){  // send the selected output to bluetooth output socket
        try
        {
            btOutputStream =btSocket.getOutputStream();
            if (!streamChr.isEmpty() && btOutputStream!=null){
                btOutputStream.write(streamChr.getBytes());
                System.out.println(streamChr);
                System.out.println("bt socket current output"+btSocket.toString());
                System.out.println("bt stream"+btSocket.getOutputStream());
                System.out.println("===============================");
            }
        }
        catch (Exception e)
        {
            showToastMSG("BT stream error msg",e.getMessage());
        }
    }

    public void onPause(){

        if (btOutputStream!=null){
            try {
                btOutputStream.flush();
            }catch (IOException e){
                showToastMSG("BT outputStream error",e.getMessage());
            }
        }

        try {
            btSocket.close();
        }catch (IOException e2){
            showToastMSG("BT socket error",e2.getMessage());
        }

        super.onPause();
    }

    public void showToastMSG(String title, String msg){ // display custom toast message
        Toast.makeText(getApplicationContext(),title+"\n"+msg, Toast.LENGTH_SHORT).show();
    }



    @SuppressLint("SetTextI18n")
    public void btConnectedMSG (){  // display bluetooth connection messages
        Toast.makeText(getApplicationContext(), "Now connected to"+name, Toast.LENGTH_SHORT).show();
        btImage.setVisibility(View.VISIBLE);
        robotReadyStatus.setVisibility(View.VISIBLE);
        setBlinkingImage(btImage);
        textStatus.setText("BT Name: \n" + name );
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

   public void displayStopMSG(){  // display the stop messages
       sendToBtStream("0");
        System.out.println(msgStop);
        textStatus.setText(msgStop);
    }

    public void moveScreenStatus(String s){ // display the selected messages
        System.out.println(s);
        textStatus.setText(s);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void moveForward (View v){  // move forward command

       // setButtonListener(btnForward, "F", msgForward);

        btnForward.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    sendToBtStream("F");
                    moveScreenStatus(msgForward);
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    displayStopMSG();
                }
                return true;
            }});
    }

    @SuppressLint("ClickableViewAccessibility")
    public void moveReverse (View v){  // move in reverse command

       // setButtonListener(btnReverse, "R", msgBackward);

        btnReverse.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    sendToBtStream("B");
                    moveScreenStatus(msgBackward);
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    displayStopMSG();
                }
                return true;
            }});
    }

    @SuppressLint("ClickableViewAccessibility")
    public void turnLeft (View v){ // turn to the left command

        //setButtonListener(btnLeft, "L", msgLeft);

        btnLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    sendToBtStream("F");
                    moveScreenStatus(msgLeft);

                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    displayStopMSG();
                }
                return true;
            }});
    }

    @SuppressLint("ClickableViewAccessibility")
    public void turnRight (View v){   // turn to the right command

     //   setButtonListener(btnRight, "F", msgRight);

        btnRight.setOnTouchListener(new View.OnTouchListener() {
            @Override

            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    sendToBtStream("F");
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
        try{}
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),"click action error msg :\n" +e.getMessage(), Toast.LENGTH_SHORT).show();
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

    public void sensorsSystemInitialize() { //initialize and register the selected sensors

        txtGyroSensorOut = findViewById(R.id.txtGyroSensorOut);
        txtAccelSensorOut = findViewById(R.id.txtAccelSensorOut);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        snsAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        snsGyroscopeVector = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        registerTheSensorsListeners();
    }

    public void registerTheSensorsListeners(){
        if (snsAccelerometer!=null )
            mSensorManager.registerListener(this, snsAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        else
            Toast.makeText(getApplicationContext(),"no accelerometer sensor detected",Toast.LENGTH_SHORT).show();

        if (snsGyroscopeVector != null)
            mSensorManager.registerListener(this, snsGyroscopeVector, SensorManager.SENSOR_DELAY_NORMAL);
        else
            Toast.makeText(getApplicationContext(),"no gyroscope sensor detected",Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onSensorChanged(SensorEvent event) {  // senses the value changes of the sensors

        Sensor sensorType = event.sensor;
        if (sensorType.getType() == snsAccelerometer.getType())
            readAccelerometerSensor(event);
        if (sensorType.getType() == snsGyroscopeVector.getType())
            readGyroSensor(event);
        }

    public void readAccelerometerSensor (SensorEvent event) { // reads and calculates the output of the accelerometer sensor after the event occurrence

        float[] value = event.values;
        double x = roundDecimalNum(value[0]);
        double y = roundDecimalNum(value[1]);
        double z = roundDecimalNum(value[2]);

        encodeBTStream(tagAccel,x,y,z);

        txtAccelSensorOut.setText("Gyro deg\n" + "X:" + x + "\n" + "Y:" + y + "\n" + "Z:" + z);
    }

    public void readGyroSensor (SensorEvent event){  // reads and calculate the output of the gyroscope and vector rotation sensor after the event occurrence

        SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values);
        SensorManager.remapCoordinateSystem(rotationMatrix,SensorManager.AXIS_X,SensorManager.AXIS_Z,rotationMatrix);
        SensorManager.getOrientation(rotationMatrix,orientationValues);
        double azimuth = Math.toDegrees(orientationValues[0]);
        double pitch = Math.toDegrees(orientationValues[1]);
        double roll = Math.toDegrees(orientationValues[2]);

        double x = roundDecimalNum(roll);
        double y = roundDecimalNum(pitch);
        double z = roundDecimalNum(azimuth);

        encodeBTStream(tagGyro, x,y,z);

        txtGyroSensorOut.setText("Gyro deg\n" + "X:" + x + "\n" + "Y:" + y + "\n" + "Z:" + z);
    }

    String tagAccel ="ac";
    String tagGyro = "gr";
    String startCharacter = "s";
    String separateCharacter = "-";
    String stopCharacter = "p";

    public void encodeBTStream (String tag, double x, double y, double z){ // encapsulate the bluetooth messages sent to arduino

        sendToBtStream(startCharacter);
        sendToBtStream(tag);
        sendToBtStream(Double.toString(x));
        sendToBtStream(separateCharacter);
        sendToBtStream(Double.toHexString(y));
        sendToBtStream(separateCharacter);
        sendToBtStream(Double.toHexString(z));
        sendToBtStream(stopCharacter);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onStop() {
            mSensorManager.unregisterListener(this);
            super.onStop();
    }

    @Override
    public void onResume(){
        registerTheSensorsListeners();
        super.onResume();
    }

    public double roundDecimalNum(double num){  // to round the output number to the desired format
       DecimalFormat numFormat = new DecimalFormat("#.##");
        return Double.valueOf(numFormat.format(num));
    }

    }
