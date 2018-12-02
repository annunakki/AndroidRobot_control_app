package com.stevensit.www.cpe556_interface_app;

/*
 this is the main container of the app

 Description:   This is an android phone app built using java language to control a ball robot (BB8)
                it contains for direction buttons to control the movement of robot and also it uses
                both gyroscope and accelerometer sensors to control the speed and movement direction
                of a 2-axis small arduino camera mounted on e top of the robot then stream camera's
                real time recording on the control app interface. Bluetooth is used to connect the robot
                to the app.


 the main activity class includes the necessary functions to define sensors, buttons , textView, resetConfigurations
 also all the necessary output forms of the measured data
 */

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
import android.os.Process;
import android.util.Pair;
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
import android.widget.VideoView;

import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.Set;
import java.util.UUID;

import static android.view.animation.Animation.REVERSE;

public class MainActivity extends Activity implements OnClickListener, SensorEventListener {


    private Button btnSettings;
    private Button btnBTConnect;
    protected Button btnForward;
    protected Button btnReverse;
    protected Button btnLeft;
    protected Button btnRight;
    private Button btnCameraCenter;
    private Button btnCalibrateGyro;
    public TextView screenStatusDisplay,blinkText,robotReadyStatus;
    protected Switch cameraSW;
    protected VideoView viewCameraWindow;
    private ImageButton btImage;
    public final String msgForward = "MOVING FORWARD";
    public  final String msgStop = "ROBOT STOPPED";
    public final String msgBackward = "MOVING IN REVERSE";
    public  final String msgLeft = "TURNING LEFT";
    public final String msgRight = "TURNING RIGHT";
    public final String msgSetToCenter = " Camera set to Center";

    //=======sensors segment ========

    public static SensorManager mSensorManager;
    public  static Sensor snsAccelerometer, snsGyroscopeVector;
    private TextView txtAccelSensorOut, txtGyroSensorOut;
    private float [] rotationMatrix = new float[9];
    private float [] orientationValues = new float[3];
    private int sensorReadyDelay= 1000000;//default value = SensorManager.SENSOR_DELAY_UI; // set the ready intervals for the sensors
    private int sensorLatency = 1000000;




 // =============== Main =================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
       // System.out.println("current orientation "+getResources().getConfiguration().orientation);
      try {initialize();} catch (Exception e){} // define all the objects on the app screen
      sensorsSystemInitialize(); // define and set the selected sensors

    }

    @Override
    public void onBackPressed(){ // to kill the background process of the app after pressing back button
        android.os.Process.killProcess(Process.myPid());
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int orientation = newConfig.orientation;
        System.out.println("msg: "+orientation);
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            System.out.println("msg: screen is portrait");
            System.out.println("msg: "+orientation);
        } else
            System.out.println("msg: "+orientation);
    }


//    public void setBlinkingText (TextView obj)  {  // to enable the blinking option on the selected textView object
//
//
//        Animation anim = new AlphaAnimation(0.0f, 1.0f);
//        anim.setDuration(10000);
//        anim.setStartOffset(10000);
//        anim.setRepeatMode(REVERSE);
//        anim.setRepeatCount(Animation.INFINITE);
//        obj.setAnimation(anim);
//    }

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
        btnCalibrateGyro = findViewById(R.id.btnCalibrateGyro);
        screenStatusDisplay =  findViewById(R.id.textStatusBox);
        btImage =  findViewById(R.id.btnImage);
        robotReadyStatus = findViewById(R.id.txtRobotONStatus);
        cameraSW =  findViewById(R.id.swCamera) ;
        btImage.setVisibility(View.INVISIBLE);
        robotReadyStatus.setVisibility(View.INVISIBLE);
        btImage.setActivated(false);
        viewCameraWindow = findViewById(R.id.videoView);
        txtGyroSensorOut = findViewById(R.id.txtGyroSensorOut);
        txtAccelSensorOut = findViewById(R.id.txtAccelSensorOut);


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

    boolean btConnectionStatus = false;
    private String name=null;
    private BluetoothSocket btSocket = null;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @SuppressLint({"HardwareIds"})
    public void connectBluetooth(View v) throws IOException {  // bluetooth connection handler

        if (btConnectionStatus) {
        System.out.println("msg: " + "BT is already connected");
        showToastMSG("Information Message:", "BT is already paired");
    }else {

            BluetoothAdapter myBluetooth = BluetoothAdapter.getDefaultAdapter();
            System.out.println("msg: "+"bt_device_name " + myBluetooth);
            if (myBluetooth == null)
                Toast.makeText(getApplicationContext(), "The BT device is not detected or not compatible", Toast.LENGTH_SHORT).show();
            else if (!myBluetooth.isEnabled()) {
                Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(turnOn, 0);
                Toast.makeText(getApplicationContext(), "BT is Turned on", Toast.LENGTH_LONG).show();
            } else {
                String address = myBluetooth.getAddress();
                Set<BluetoothDevice> pairedDevices = myBluetooth.getBondedDevices();

                if (pairedDevices.size() > 0) {
                    for (BluetoothDevice bt : pairedDevices) {
                        address = bt.getAddress();
                        name = bt.getName();
                    }
                }

              //  myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                BluetoothDevice btDevice = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
                btSocket = btDevice.createInsecureRfcommSocketToServiceRecord(myUUID);//create paired connection
                System.out.println("after"+BluetoothAdapter.STATE_CONNECTED);
                new Handler().postDelayed(new Runnable() { // add a delay before connecting the bt socket to the paired device
                    @Override
                    public void run() {
                        try {
                            btSocket.connect();
                        } catch (IOException e) {
                            //   Toast.makeText(getApplicationContext(),"BT connection error\n" +e.getMessage(), Toast.LENGTH_SHORT).show();

                            try {
                                btSocket.close();
                            } catch (IOException e1) {
                                Toast.makeText(getApplicationContext(), "BT connection error\n" + e1.getMessage(), Toast.LENGTH_SHORT).show();
                                onBackPressed();
                            }
                        }
                    }
                }, 500);

                btConnectionStatus=true;
                btConnectedMSG();
            }
        }
    }

    public void sendToBtStream(String streamChr){  // send the selected output to bluetooth output socket
        try{
            OutputStream btOutputStream = btSocket.getOutputStream();
            if (!streamChr.isEmpty() && btOutputStream !=null){
                btOutputStream.write(streamChr.getBytes());
                System.out.println("msg: "+streamChr);
                System.out.println("msg: "+"bt socket current output: "+btSocket.toString());
                System.out.println("msg: "+"bt stream: "+btSocket.getOutputStream());
                System.out.println("====================================================================");
            }
        }
        catch (Exception e)
        {
           // showToastMSG("BT stream error msg",e.getMessage());
        }
    }

//    public void onPause(){
//
//        if (btOutputStream!=null){
//            try {
//                btOutputStream.flush();
//            }catch (IOException e){
//                showToastMSG("BT outputStream error",e.getMessage());
//            }
//        }
//
//        try {
//            btSocket.close();
//        }catch (IOException e2){
//            showToastMSG("BT socket error",e2.getMessage());
//        }
//
//        super.onPause();
//    }

    public void showToastMSG(String title, String msg){ // display custom toast message
        Toast.makeText(getApplicationContext(),title+"\n"+msg, Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("SetTextI18n")
    public void btConnectedMSG (){  // display bluetooth connection messages and set the indicators
        Toast.makeText(getApplicationContext(), "Now connected to: "+name, Toast.LENGTH_SHORT).show();
        btImage.setVisibility(View.VISIBLE);
        robotReadyStatus.setVisibility(View.VISIBLE);
        btImage.setActivated(true);
        setBlinkingImage(btImage);
        screenStatusDisplay.setText("BT Name: \n" + name );
        btConnectionStatus=true;
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

   @SuppressLint("SetTextI18n")
   public void displayStopMSG(){  // display the stop messages

       if (btImage.isActivated()) {
           sendToBtStream("S");
           System.out.println("msg: "+msgStop);
           screenStatusDisplay.setText(msgStop);
       }
       else {
           System.out.println("msg: "+"BT is not enabled");
           screenStatusDisplay.setText("BT is not enabled");
       }
    }

    @SuppressLint("SetTextI18n")
    public void displayMoveStatus(String s) { // display the selected messages

        if (btImage.isActivated()) {
            screenStatusDisplay.setText(s);
            System.out.println("msg: "+s);
        } else {
            screenStatusDisplay.setText("BT is not enabled");
            System.out.println("msg: "+"BT is not enabled");
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    public void moveForward (View v){  // move forward command
       // setButtonListener(btnForward, "F", msgForward);
        btnForward.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    sendToBtStream("F");
                    displayMoveStatus(msgForward);
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
                    displayMoveStatus(msgBackward);

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
                    sendToBtStream("L");
                    displayMoveStatus(msgLeft);

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
                    sendToBtStream("R");
                    displayMoveStatus(msgRight);
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
          //  Toast.makeText(getApplicationContext(),"click action error msg :\n" +e.getMessage(), Toast.LENGTH_SHORT).show();
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


    public void cameraSwitch(View v) {
        if (cameraSW.isChecked()) {
            //cameraSW.setTextOff("Camera ON");
            cameraSW.setText("Camera ON");
            sensorOut = true;
            screenStatusDisplay.setText("Camera is activated");
            viewCameraWindow.setVisibility(View.VISIBLE);

        } else {
            //cameraSW.setTextOff("Camera OFF");
            cameraSW.setText("Camera OFF");
            sensorOut = false;
            screenStatusDisplay.setText("Camera is deactivated");
            viewCameraWindow.setVisibility(View.INVISIBLE);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    public void setCameraToCenter(View v){

       sendToBtStream("C"); // set the camera to home position
        displayMoveStatus(msgSetToCenter);
        System.out.println("msg: camera set to center pos");
    }


    public void sensorsSystemInitialize() { //initialize and register the selected sensors

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        snsAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        snsGyroscopeVector = mSensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);

        registerTheSensorsListeners();
    }



    public void registerTheSensorsListeners(){
        if (snsAccelerometer!=null )
            mSensorManager.registerListener(this, snsAccelerometer, sensorReadyDelay, sensorLatency);
        else
            Toast.makeText(getApplicationContext(),"no accelerometer sensor detected",Toast.LENGTH_SHORT).show();

        if (snsGyroscopeVector != null)
            mSensorManager.registerListener(this, snsGyroscopeVector, sensorReadyDelay, sensorLatency);
        else
            Toast.makeText(getApplicationContext(),"no gyroscope sensor detected",Toast.LENGTH_SHORT).show();
    }


    boolean sensorOut = false;

    @Override
    public void onSensorChanged(SensorEvent event) {  // senses the value changes of the sensors

//        Sensor sensorType = event.sensor;
//        if (sensorType.getType() == snsAccelerometer.getType())
//            readAccelerometerSensor(event);
//        if (sensorType.getType() == snsGyroscopeVector.getType())
//            readGyroSensor(event);

//        if (sensorType.getType() == snsAccelerometer.getType() || sensorType.getType() == snsGyroscopeVector.getType())
            readSensorsData(event);
        }

        @SuppressLint("SetTextI18n")
        public void readSensorsData(SensorEvent event) {
            Sensor sensorType = event.sensor;
            if (sensorType.getType() == snsAccelerometer.getType())
                accelValues = readAccelerometerSensor(event);
            if (sensorType.getType() == snsGyroscopeVector.getType())
                gyroValues = readGyroSensor(event);

            if (sensorOut) {
                encodeBTStream(accelValues[0], accelValues[1], gyroValues[0], gyroValues[1]);
                txtAccelSensorOut.setText("Accel m/s^2\n" +
                        accelValues[0].first+ accelValues[0].second+"\n"+
                        accelValues[1].first+accelValues[1].second+"\n");

                txtGyroSensorOut.setText("Gyro rad\n" +
                        gyroValues[0].first+ gyroValues[0].second+"\n"+
                        gyroValues[1].first+gyroValues[1].second+"\n");
            }
        }


    // String tagAccel ="ac";
    //String tagGyro = "gr";
    String tag ="SENS";
    String startCharacter = "s";
    String separateCharacter = ":";
    String stopCharacter = "p";
    Pair [] accelValues,gyroValues;

    public void encodeBTStream ( Pair x, Pair y, Pair a, Pair b){ // encapsulate the bluetooth messages sent to arduino

        //sendToBtStream(startCharacter);

        StringBuilder btMessage = new StringBuilder();

        btMessage.append(tag)
                .append(separateCharacter)
                .append(x.second.toString())
                .append(separateCharacter)
                .append(y.second.toString())
                .append(separateCharacter)
                .append(a.second.toString())
                .append(separateCharacter)
                .append(b.second.toString());

        sendToBtStream(btMessage.toString());
    }

    float accel_X, accel_Y;

    public Pair[] readAccelerometerSensor (SensorEvent event) { // reads and calculates the output of the accelerometer sensor after the event occurrence

        //System.arraycopy(event.values,0,accelEventValues,0,3);
//        SensorManager.getRotationMatrix(rotationMatrix,null,event.values,null);
//        SensorManager.getOrientation(rotationMatrix,orientationValues);
//        accel_X = orientationValues[0];
//        accel_Y= orientationValues[1];

        accel_X= roundDecimalNum(event.values[0]);
        accel_Y = roundDecimalNum(event.values[1]);

        return new Pair[] {new Pair<>("axis_X: ", accel_X), new Pair<>("axis_Y: ",accel_Y)};
    }


    public static boolean firstTimeRead=  true;
    float pitchValueOffset=0 , rollValueOffset=0, roll=0, pitch=0; // x = pitch ,, y = roll
    String pattern = "#.##";

    public Pair[] readGyroSensor (SensorEvent event) {  // reads and calculate the output of the gyroscope and vector rotation sensor after the event occurrence

       // System.arraycopy(event.values,0,gyroEventValues,0,3);
      SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values);
      SensorManager.remapCoordinateSystem(rotationMatrix, SensorManager.AXIS_X, SensorManager.AXIS_Z, rotationMatrix);
      SensorManager.getOrientation(rotationMatrix, orientationValues);

//      double pitch = Math.toDegrees(orientationValues[1]);
//      double roll = Math.toDegrees(orientationValues[0]);

        if (firstTimeRead){
            rollValueOffset = orientationValues[1];
            pitchValueOffset = orientationValues[0];

            firstTimeRead=false;
        }

        roll = - roundDecimalNum(orientationValues[1]-rollValueOffset); // x asix rotation
        pitch = roundDecimalNum(orientationValues[0]-pitchValueOffset);  // y axis rotation



/////=================================== raw data readings=====
//        if (firstTimeRead){
//            pitchValueOffset = event.values[0];
//            rollValueOffset = event.values[1];
//            firstTimeRead=false;
//        }
//
//        pitch = - roundDecimalNum(event.values[0]-pitchValueOffset) ;  // the minus sign is to invert the angle direction in regards with the phone movement
//        roll= roundDecimalNum(event.values[1]-rollValueOffset) ;


        return new Pair[]{ new Pair<>("Roll: ", roll), new Pair<>("Pitch: ", pitch)};

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

    public float roundDecimalNum(float num){  // to round the output number to the desired format
        DecimalFormat numFormat = new DecimalFormat(pattern);
        return Float.valueOf(numFormat.format(num));
    }

    }

//    //======================================================================================================================
///*
//All buttons functions are included here
// */
//
//    @SuppressLint("Registered")
//    class ButtonsFunctions extends MainActivity{
//
//        @SuppressLint("ClickableViewAccessibility")
//        public void moveForward (View v){  // move forward command
//            // setButtonListener(btnForward, "F", msgForward);
//            v.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                        sendToBtStream("F");
//                        displayMoveStatus(msgForward);
//                    }
//                    if (event.getAction() == MotionEvent.ACTION_UP) {
//                        displayStopMSG();
//                    }
//                    return true;
//                }});
//        }
//
//        public void cameraSwitch(View v) {
//            if (cameraSW.isChecked()) {
//                //cameraSW.setTextOff("Camera ON");
//                cameraSW.setText("Camera ON");
//                sensorOut = true;
//                screenStatusDisplay.setText("Camera is activated");
//                viewCameraWindow.setVisibility(View.VISIBLE);
//
//            } else {
//                //cameraSW.setTextOff("Camera OFF");
//                cameraSW.setText("Camera OFF");
//                sensorOut = false;
//                screenStatusDisplay.setText("Camera is deactivated");
//                viewCameraWindow.setVisibility(View.INVISIBLE);
//            }
//        }
//
//        @SuppressLint("ClickableViewAccessibility")
//        public void moveReverse (View v){  // move in reverse command
//            // setButtonListener(btnReverse, "R", msgBackward);
//            v.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//
//                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                        sendToBtStream("B");
//                        displayMoveStatus(msgBackward);
//
//                    }
//                    if (event.getAction() == MotionEvent.ACTION_UP) {
//                        displayStopMSG();
//                    }
//                    return true;
//                }});
//        }
//
//        @SuppressLint("ClickableViewAccessibility")
//        public void turnLeft (View v){ // turn to the left command
//
//            //setButtonListener(btnLeft, "L", msgLeft);
//
//            v.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                        sendToBtStream("L");
//                        displayMoveStatus(msgLeft);
//
//                    }
//                    if (event.getAction() == MotionEvent.ACTION_UP) {
//                        displayStopMSG();
//                    }
//                    return true;
//                }});
//        }
//
//        @SuppressLint("ClickableViewAccessibility")
//        public void turnRight (View v){   // turn to the right command
//
//            //   setButtonListener(btnRight, "F", msgRight);
//
//            v.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//
//                public boolean onTouch(View v, MotionEvent event) {
//                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                        sendToBtStream("R");
//                        displayMoveStatus(msgRight);
//                    }
//                    if (event.getAction() == MotionEvent.ACTION_UP) {
//                        displayStopMSG();
//                    }
//                    return true;
//                }});
//        }
//
//
//
//
//
//
//
//
//    }
