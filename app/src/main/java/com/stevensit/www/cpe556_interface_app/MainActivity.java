/**
 this is the main container of the app

 Description:   This is an android phone app built using java language to control a ball robot (BB8)
 it contains for direction buttons to control the movement of robot and also it uses
 both gyroscope and accelerometer sensors to control the speed and movement direction
 of a 2-axis small arduino camera mounted on e top of the robot then stream camera's
 real time recording on the control app interface. Bluetooth is used to connect the robot
 to the app.


 the main activity class includes the necessary functions to define sensors, buttons , textView, resetConfigurations
 also all the necessary output forms of the measured data.

 *************
 NOTES >>>: the code body below is virtually divided into segments for faster troubleshooting and code clearance
 also some parts of the code are commented out because is not tested yet or may be used for the future options upgrade.

 also in case the keeps crashing because the value cast issue even the cimpiler doesnt show any errors, uninstall the app
 from the phone before uploading new one

   >>> in progress, working on function to let the robot move to the direction of the sensors readings, in other words
 the robot will mode the same way that the camera motors move

 >>> the first design intent to divide the functions here into classes to organized the code and to reduce the overhead of debugging,
 but due limited time in addition to some issues related to static variables and multi threads, we had to build it into the current
 design ,, can be re organized later into better design

 *****************
 *
 *
 ====================================================================================================================
 ====================================================================================================================
 */

package com.stevensit.www.cpe556_interface_app;


/**
 * all the necessary variables and constants are collected here hence there are additional variables included among the code part
 * that are function specific
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
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
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.Set;
import java.util.UUID;

import static android.view.animation.Animation.REVERSE;




public class MainActivity extends Activity implements OnClickListener, SensorEventListener {

    public EditText editGyroPitchThresh, editGyroRollThresh;
    protected Button btnForward;
    protected Button btnReverse;
    protected Button btnLeft;
    protected Button btnRight;
    //private Button btnCameraCenter;
    //private Button btnCalibrateGyro;
    public TextView screenStatusDisplay;
    public TextView blinkText, robotReadyStatus;
    protected Switch cameraSW;
    protected ImageView viewCameraWindow;
    public ImageButton btImage, btCaptureImage;
    public final String msgForward = "MOVING FORWARD";
    public final String msgStop = "ROBOT STOPPED";
    public final String msgBackward = "MOVING IN REVERSE";
    public final String msgLeft = "TURNING LEFT";
    public final String msgRight = "TURNING RIGHT";
    //public final String msgSetToCenter = " Camera set to Center";  // sets the camera to home position


    //=========bluetooth segment ===========
    public static boolean btConnectionStatus = false; // true if the bt connection is active
    public byte[] imageBuffer = new byte[15360];  //reserve 15KB for the image stream
    private static int numIndex = 0;
    public InputStream btInStream;
    private static BTStreamListener thBTDataReceiverThread;
    public static INStreamListenService inboudStreamThread;
    public static boolean msgBTListenService = false; // checks if the inbound listen service is started
    public static boolean msgBTInboundReceiver = false; // checks if the inblound service is listening and capturing the inStream
    protected ImageCapture captureImg;
    private Handler msgHandler;
    private static String btPairedDeviceName = null;
    public BluetoothAdapter myBluetooth = null;
    private BluetoothSocket btSocket = null;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    public OutputStream btOutputStream;
//  public OutputStream btOutputStream;
//    public BluetoothSocket inputSocket;
//    private InputStream inStream=null;
//    private byte [] btReadBuffer;
//    private Handler readBTHandler;

    //=======sensors segment ========

    public static SensorManager mSensorManager;
    public static Sensor snsAccelerometer, snsGyroscopeVector;
    private TextView txtAccelSensorOut, txtGyroSensorOut;
    private float[] rotationMatrix = new float[9];
    private float[] orientationValues = new float[3];
    public static int sensorDelay;
    boolean sensorOut = false; // it's included in camera view switch to activate the sensors readings when it's true
    public static AppReferences sharedPref;


    /**
     * // =============== Main =================================================================
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        loadPrefValues();
        // System.out.println("current orientation "+getResources().getConfiguration().orientation);
        try {
            initialize(); // define all the objects on the app screen
        } catch (Exception e) {
        }
        sensorsSystemInitialize(); // define and set the selected sensors
        initializeImageCapture();
    }

    @Override
    public void onBackPressed() { // to kill the background process of the app after pressing back button
        savePrefValues();
        android.os.Process.killProcess(Process.myPid());
    }

    public void loadPrefValues() {
        sharedPref = new AppReferences(getApplicationContext());
        gyroRoll_Threshold = sharedPref.getValue("roll");
        gyroPitch_Threshold = sharedPref.getValue("pitch");
        sensorDelay = sharedPref.getInt("delay");
    }

    private void savePrefValues() {
        sharedPref.saveValue("roll", gyroRoll_Threshold);
        sharedPref.saveValue("pitch", gyroPitch_Threshold);
        sharedPref.saveIntValue("delay", sensorDelay);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int orientation = newConfig.orientation;
        System.out.println("msg: " + orientation);
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            System.out.println("msg: screen is portrait");
            System.out.println("msg: " + orientation);
        } else
            System.out.println("msg: " + orientation);
    }

    /**
     * define and initialize all the necessary variables and objects when first launch the app
     */

    @SuppressLint("ClickableViewAccessibility")
    private void initialize() {
        btnForward = findViewById(R.id.btnForward);
        btnReverse = findViewById((R.id.btnReverse));
        btnLeft = findViewById(R.id.btnTurnLeft);
        btnRight = findViewById(R.id.btnTurnRight);
        screenStatusDisplay = findViewById(R.id.textStatusBox);
        btImage = findViewById(R.id.btnImage);
        robotReadyStatus = findViewById(R.id.txtRobotONStatus);
        cameraSW = findViewById(R.id.swCamera);
        btImage.setVisibility(View.INVISIBLE);
        btCaptureImage = findViewById(R.id.btnCamera);
        robotReadyStatus.setVisibility(View.INVISIBLE);
        viewCameraWindow = findViewById(R.id.imageViewWindow);
        txtGyroSensorOut = findViewById(R.id.txtGyroSensorOut);
        txtAccelSensorOut = findViewById(R.id.txtAccelSensorOut);
        editGyroPitchThresh = findViewById(R.id.editPitchThreshold);
        editGyroRollThresh = findViewById(R.id.editRollThreshold);

        btImage.setActivated(false);

// not tested yet , an alternative initialize function for the control buttons
        //***************
//        setButtonListener (btnForward, "F");  //move forward
//        setButtonListener (btnReverse, "B");  // move reverse or backward
//        setButtonListener (btnLeft, "L");  // move to the left
//        setButtonListener (btnRight, "R");  //move to the right

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


    /**
     * set blinking text , not tested and can be uncommented later
     */

//    public void setBlinkingText (TextView obj)  {  // to enable the blinking option on the selected textView object
//        Animation anim = new AlphaAnimation(0.0f, 1.0f);
//        anim.setDuration(10000);
//        anim.setStartOffset(10000);
//        anim.setRepeatMode(REVERSE);
//        anim.setRepeatCount(Animation.INFINITE);
//        obj.setAnimation(anim);
//    }


    /**
     * prepare image capture function and bluetooth receiver
     */

    public void initializeImageCapture() {
        viewCameraWindow = findViewById(R.id.imageViewWindow);
        viewCameraWindow.setVisibility(View.INVISIBLE);
        // viewCameraWindow.setImageBitmap();
        captureImg = new ImageCapture();
    }

    /**
     * enables any selected object to blink with set durations and intervals
     *
     * @param obj
     */

    public void setBlinkingImage(ImageButton obj) {  // to enable the blinking option on the selected image button object

        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(500);
        anim.setStartOffset(50);
        anim.setRepeatMode(REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        obj.setAnimation(anim);
    }

    /**
     * displays stop messages on the text display screen
     */

    @SuppressLint("SetTextI18n")
    public void displayStopMSG() {  // display the stop messages

        if (btImage.isActivated()) {
            sendToBtStream("S");
            System.out.println("msg: " + msgStop);
            screenStatusDisplay.setText(msgStop);
        } else {
            System.out.println("msg: " + "BT is not enabled");
            screenStatusDisplay.setText("BT is not enabled");
        }
    }

    /**
     * displays the robot's moving status on the display screen
     *
     * @param s , reads the required string message
     */

    @SuppressLint("SetTextI18n")
    public void displayMoveStatus(String s) { // display the selected messages

        if (btImage.isActivated()) {
            screenStatusDisplay.setText(s);
            System.out.println("msg: " + s);
        } else {
            screenStatusDisplay.setText("BT is not enabled");
            System.out.println("msg: " + "BT is not enabled");
        }
    }

    /**
     * displays toast short notification on the screen, can take any kind od string messages
     *
     * @param title
     * @param msg
     */

    public void showToastMSG(final Context ctx, final String title, final String msg) { // display custom toast message
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ctx, title + "\n" + msg, Toast.LENGTH_SHORT).show();
            }
        });
    }


    @SuppressLint("SetTextI18n")
    public void btConnectedMSG() {  // display bluetooth connection messages and set the indicators
        showToastMSG(getApplicationContext(), "Now connected to: ", btPairedDeviceName);
        btImage.setVisibility(View.VISIBLE);
        robotReadyStatus.setVisibility(View.VISIBLE);
        btImage.setActivated(true);
        setBlinkingImage(btImage);
        screenStatusDisplay.setText("BT Name: \n" + btPairedDeviceName);
        btConnectionStatus = true;
    }


    /**
     * built to simplify the creation of screen buttons and assign it to onClick listener
     * not tested yet and may need to perform some code modifications to the main code if added
     * @param
     */

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

    /**
     * connected with settings button to call te settings menu when clicked
     * @param v
     */

    public void settingMenu(View v) {

        Intent intentSettings = new Intent(this, SettingMenu.class);
        startActivity(intentSettings);
    }


    /**
     * rounds the fed float value into the selected decimal output format
     *
     * @param num
     * @return
     */

    public float roundDecimalNum(float num) {
        DecimalFormat numFormat = new DecimalFormat(pattern);
        return Float.valueOf(numFormat.format(num));
    }

    /**
     * // ^^^^^^^^^^^^^^^^^^^^^^ main segment ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^




     * // ============== Buttons segment =============================================================
     */

    /**
     * move forward button
     * @param v
     */
    @SuppressLint("ClickableViewAccessibility")
    public void moveForward(View v) {  // move forward command
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
            }
        });
    }
    /**
     * move backward (reverse) button
     * @param v
     */
    @SuppressLint("ClickableViewAccessibility")
    public void moveReverse(View v) {  // move in reverse command
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
            }
        });
    }

    /**
     * turn to the left button
     * @param v
     */

    @SuppressLint("ClickableViewAccessibility")
    public void turnLeft(View v) { // turn to the left command

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
            }
        });
    }

    /**
     * turn to the right button
     * @param v
     */

    @SuppressLint("ClickableViewAccessibility")
    public void turnRight(View v) {   // turn to the right command

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
            }
        });
    }

    /**
     * onClick builtin function listener
     * @param v
     */

    @Override
    public void onClick(View v) {
        try {
        } catch (Exception e) {
            //  Toast.makeText(getApplicationContext(),"click action error msg :\n" +e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * camera control switch to enable or disable the camera while running the robot
     * @param v
     */
    public void cameraSwitch(View v) {
        if (btConnectionStatus)
            activateCamera();
        else
            screenStatusDisplay.setText("Bluetooth is OFF");
    }

    /**
     * controls the enable process of the camera, it's part of the cameraSwitch fucntion
     */

    public void activateCamera() {

        if (cameraSW.isChecked()) {
            //cameraSW.setTextOff("Camera ON");
            cameraSW.setText("Disable Camera");
            sensorOut = true;
            screenStatusDisplay.setText("Camera is activated");
            viewCameraWindow.setVisibility(View.VISIBLE);
            btCaptureImage.setVisibility(View.VISIBLE);
            txtGyroSensorOut.setEnabled(true);
            txtAccelSensorOut.setEnabled(true);

        } else {
            //cameraSW.setTextOff("Camera OFF");
            cameraSW.setText("Enable Camera");
            if (!checkBoxChecked) {
                txtGyroSensorOut.setEnabled(false);
                txtAccelSensorOut.setEnabled(false);
                sensorOut = false;
            }
            screenStatusDisplay.setText("Camera is deactivated");
            viewCameraWindow.setVisibility(View.INVISIBLE);
            btCaptureImage.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * image capture button , with the upgrade later, can have the function to capture video stream
     */

    public void cameraCaptureButton(View v) {

        if (captureImg != null) {
            startBTListenerService();
//            thBTDataReceiverThread.readBTStream();
            captureImg.captureImage(getApplicationContext());
            System.out.println("camera capture button pressed ,, capturing signal sent.....");
        } else
            showToastMSG(getApplicationContext(), "code error", "image capture class is not instantiate ");
    }


    /**
     * sets the camera position back to its home position
     * not tested or enabled yet
     */

//    @SuppressLint("ClickableViewAccessibility")
//    public void setCameraToCenter(View v) {
//
//        sendToBtStream("C"); // set the camera to home position
//        displayMoveStatus(msgSetToCenter);
//        System.out.println("msg: camera set to center pos");
//    }

    /**
     * ^^^^^^^^^^^^^^^^ Buttons segment ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^




     * //================Bluetooth segment =============================================================

     * onClickListener assigned to bluetooth connect button on the main interface
     */

    public void connectBluetooth(View v) {
        try {
            bluetoothConnectionManager();
        } catch (IOException e) {
        }
    }


    /**
     * main bluetooth connection handler, it initiates bt sockets and search for the paired devices to pair with
     * it's called when the bluetooth connect button is pressed from the main screen
     *
     * @throws IOException
     */
    @SuppressLint({"HardwareIds"})
    public void bluetoothConnectionManager() throws IOException {
        msgHandler = new Handler(Looper.getMainLooper());
        cameraSW.setEnabled(true);

        if (btConnectionStatus) {
            System.out.println("msg: " + "BT is already connected");
            showToastMSG(getApplicationContext(), "Information Message:", "BT is already paired");
        } else {

            myBluetooth = BluetoothAdapter.getDefaultAdapter();
            System.out.println("msg: " + "bt_device_name " + myBluetooth);
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
                        btPairedDeviceName = bt.getName();
                    }
                }

                BluetoothDevice btDevice = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
                btSocket = btDevice.createInsecureRfcommSocketToServiceRecord(myUUID);//create socket connection
                System.out.println("connected to: " + BluetoothAdapter.STATE_CONNECTED);

                msgHandler.postDelayed(new Runnable() {

                    // new Handler().postDelayed(new Runnable() { // add a delay before connecting the bt socket to the paired device
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
                }, 200);

                btConnectionStatus = true;
                btConnectedMSG();
            }
        }
    }


    /**
     * Bluetooth output socket, it sends the defined data through the BT connection to the receiving party
     *
     * @param streamChr
     */

    public void sendToBtStream(String streamChr) {  // send the selected output to bluetooth output socket
        streamChr = streamChr + ":0:0:0:0 ";
        try {
            OutputStream btOutputStream = btSocket.getOutputStream();
            if (!streamChr.isEmpty() && btOutputStream != null) {
                btOutputStream.write(streamChr.getBytes());
                System.out.println("msg: " + streamChr);
                System.out.println("msg: " + "bt stream: " + btSocket.getOutputStream());
                System.out.println("====================================================================");
            }
        } catch (Exception e) {
            // showToastMSG("BT stream error msg",e.getMessage());
        }
    }

    /**
     * to clean out the bluetooth connection stream when called
     * this one and the onResume function are called to free up phone resources when the hardware or the thread process are idle
     */

    public void onPause() {
        savePrefValues();

        if (btOutputStream != null) {
            try {
                btOutputStream.flush();
            } catch (IOException e) {
                showToastMSG(getApplicationContext(), "BT outputStream error", e.getMessage());
            }
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        loadPrefValues();
        registerTheSensorsListeners();
        super.onResume();
    }

    /**
     * terminates BT connection  when called,, later will be linked to a BT disconnedt button
     */

    public void closeConnection() {
        try {
            btSocket.close();
        } catch (IOException e2) {
            showToastMSG(getApplicationContext(), "BT socket error", e2.getMessage());
        }
    }

    /**
     *  encapsulates the sensors output into on bt stream packet
     *  output format  "tag:accelX:accelY:gyroRoll:gyroPitch "
     *
     * @param x for accelerometer X
     * @param y for accelerometer Y
     * @param a for gyroscope Roll
     * @param b for gyroscope Pitch
     */

    public void encodeBTSensorStream(Pair x, Pair y, Pair a, Pair b) { // encapsulate the bluetooth messages sent to arduino
        //

        StringBuilder btMessage = new StringBuilder();
        btMessage.append(tag)
                .append(separateCharacter)
                .append(x.second.toString()) // accelX
                .append(separateCharacter)
                .append(y.second.toString()) //accelY
                .append(separateCharacter)
                .append(a.second.toString()) //gyroRoll
                .append(separateCharacter)
                .append(b.second.toString()) //gyroPitch
                .append(" "); // added for the arduino part for the packet detection and decoding

        sendToBtStream(btMessage.toString());
    }

    // =============== bt thread segment ==================================

    /**
     * starts thread listening service for the incoming bluetooth data
     */

    public synchronized void startBTListenerService() {  /// start the thread service from here
        if (thBTDataReceiverThread != null) {
            closeConnection();
            thBTDataReceiverThread = null;
        }
        if (inboudStreamThread == null) {
            inboudStreamThread = new INStreamListenService();
            inboudStreamThread.start();
        }
        msgBTListenService = true;
    }

    public synchronized void startBTInboundListener() {
        if (inboudStreamThread != null) {
            closeConnection();
            inboudStreamThread = null;
        }
        if (thBTDataReceiverThread == null) {
            thBTDataReceiverThread = new BTStreamListener();
            thBTDataReceiverThread.start();
        }
        msgBTInboundReceiver = true;
    }

    /**
     * stops all the running threads
     */

    public synchronized void stop() {
        if (inboudStreamThread != null) {
            closeConnection();
            inboudStreamThread = null;
        }
        if (thBTDataReceiverThread != null) {
            closeConnection();
            thBTDataReceiverThread = null;
        }
        msgBTInboundReceiver = false;
        msgBTListenService = false;
    }

    /**
     * // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ bluetooth segment ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^



     * // =============== Sensors Segment ==========================================================
     */

    public void sensorsSystemInitialize() { //initialize and register the selected sensors
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        snsAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        snsGyroscopeVector = mSensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);

        registerTheSensorsListeners();
    }

    /**
     * builtin function to register the defined sensors with the sensors manager
     */

    public void registerTheSensorsListeners() {
        int sensorLatency = sensorDelay;

        if (snsAccelerometer != null)
            mSensorManager.registerListener(this, snsAccelerometer, sensorDelay, sensorLatency);
        else
            Toast.makeText(getApplicationContext(), "no accelerometer sensor detected", Toast.LENGTH_SHORT).show();

        if (snsGyroscopeVector != null)
            mSensorManager.registerListener(this, snsGyroscopeVector, sensorDelay, sensorLatency);
        else
            Toast.makeText(getApplicationContext(), "no gyroscope sensor detected", Toast.LENGTH_SHORT).show();
    }

    /**
     * sensors events change listen service
     *
     * @param event
     */
    @Override
    public synchronized void onSensorChanged(SensorEvent event) {  // senses the value changes of the sensors
        readSensorsData(event);
    }

    /**
     * checks the type of sensor data and filter out the desired sensor types
     *
     * @param event
     */

//    float accelX_Threshold=0.05f;
//    float accelY_Threshold=0.05f;
//    float gyroPitch_Threshold=5f;
//    float gyroRoll_Threshold =5f;

    public static float gyroPitch_Threshold;//= 0;
    public static float gyroRoll_Threshold;//= 0;
    public static boolean checkBoxChecked;
    float gyroPitch, gyroRoll;
    float gyroPitchPrev = 0, gyroRollPrev = 0;
    int msgCounter = 0;  // counts the number of the out output messages from the debugger for troubleshooting purposes

    /**
     * reads the output of sensors event to process the output data
     * contains:
     * system out instructions for debugging purpose
     * sets the final output results to the text out boxes on the main screen
     * sensors output threshold check for bluetooth data output
     *
     * @param event
     */

    @SuppressLint("SetTextI18n")
    public void readSensorsData(SensorEvent event) {
        Sensor sensorType = event.sensor;

        if (sensorType.getType() == snsAccelerometer.getType()) {
            //if (event.values[0] > accelX_Threshold || event.values[1] > accelY_Threshold)
            accelValues = readAccelerometerSensor(event);
        }

        if (sensorType.getType() == snsGyroscopeVector.getType()) {
            //if (event.values[0] > gyroPitch_Threshold || event.values[1] > gyroRoll_Threshold)
            gyroValues = readGyroSensor(event);
        }

        if (sensorOut) {

            txtAccelSensorOut.setText("Accel m/s^2\n" +
                    accelValues[0].first + accelValues[0].second + "\n" +
                    accelValues[1].first + accelValues[1].second + "\n");

            txtGyroSensorOut.setText("Gyro rad\n" +
                    gyroValues[0].first + gyroValues[0].second + "\n" +
                    gyroValues[1].first + gyroValues[1].second + "\n");

            // for debugging purpose, it shows all the set configurations and out data of the sensors
            System.out.print("msg#" + msgCounter + "  Gyro data:  ");
            gyroPitch = (float) gyroValues[0].second;
            System.out.print("roll: " + gyroPitch + " || ");
            gyroRoll = (float) gyroValues[1].second;
            System.out.print("pitch: " + gyroRoll);
            System.out.print("|| Threshold: " + gyroPitch_Threshold + " <> " + gyroRoll_Threshold);
            System.out.print("  || sensor delay: " + sensorDelay);
            System.out.print("  || smooth output: " + smoothOutputCheck);
            System.out.println("  || sensor output " + checkBoxChecked);
            msgCounter++;

// sets the output threshold of the sensors to the bluetooth
            if (Math.abs(gyroPitch - gyroPitchPrev) > gyroPitch_Threshold || Math.abs(gyroRoll - gyroRollPrev) > gyroRoll_Threshold) {
                encodeBTSensorStream(accelValues[0], accelValues[1], gyroValues[0], gyroValues[1]);
                gyroPitchPrev = gyroPitch;
                gyroRollPrev = gyroRoll;
            }
        }
    }

    /**
     * some variables related to sensors operations
     */

    String tag = "T";
    String separateCharacter = ":";
    String pattern = "#.##"; // sets the output format of the float numbers through function roundDecimalNum()

    public static Pair[] accelValues, gyroValues;
    float accelAvgX, accelAvgY, gyroAvgRoll, gyroAvgPitch;
    public static boolean sensorFirstTimeRead = false; // sets the calibration position
    float pitchValueOffset = 0, rollValueOffset = 0, roll = 0, pitch = 0; // x = pitch ,, y = roll
    float accel_X, accel_Y;
    public static boolean smoothOutputCheck = false;

    /**
     * read and process the accelerometer sensor event outputs for both axis X and axis Y
     * @param event
     * @return
     */

    public Pair[] readAccelerometerSensor(SensorEvent event) { // reads and calculates the output of the accelerometer sensor after the event occurrence

        if (smoothOutputCheck) {
            accelAvgX = smoothOutput(accelAvgX, event.values[0], 3);
            accelAvgY = smoothOutput(accelAvgY, event.values[1], 3);
        } else {
            accelAvgX = event.values[0];
            accelAvgY = event.values[1];
        }
        accel_X = roundDecimalNum(accelAvgX);
        accel_Y = roundDecimalNum(accelAvgY);

        return new Pair[]{new Pair<>("axis_X: ", accel_X), new Pair<>("axis_Y: ", accel_Y)};
    }

    /**
     * produces the average of the last three readings (can be changed to more but it'll add more delay)
     * to prevent the sudden change in the sensors output data
     *
     * @param avg
     * @param newVal
     * @param n
     * @return
     */

    private float smoothOutput(float avg, float newVal, float n) {

//        if (Math.abs(current - avg) > threshold) current = avg;
        //newAvg = oldAvg*(n-1)/n +  newVal /n;  // <<- general equation for avg output

        avg -= avg / n;
        avg += newVal / n;
        return avg;
    }

    /**
     * reads and process the sensor event output of the gyroscope sensor , it contains the rotation matrices and axis remapping
     * to correct phone's orientation frame data with the global frame
     *
     *
     * TODO  sets the gyro calibration to set the zero point of the roll position of the phone with the local frame
     *
     * * we need to transfer the global orientation of the gyro to the local orientation of the phone
     *
     * the issue is the output of the gyro goes out of the required range when the values turn into negative
     *
     * NOTE :: the gyro calibration to zero point issue is not solved yet, it should be included here, there are some  ideas to solve it
     * can be performed later because of the limited time
     * sol1: convert the negative part of the periodic angle to positive to obtain a positive 360 degree angle then subtract the offset value
     * from the readings
     *
     * sol2: we check the angle difference output  of the gyro and move the servo motors when the value is less than the set threshold
     *
     * sol3: apply the current rotation matrix to another one that will shift the output with the selected angle of the phone (euler angles)
     * check http://www.chrobotics.com/library/understanding-euler-angles
     *
     * @param event
     * @return
     */
    public Pair[] readGyroSensor(SensorEvent event) {  // reads and calculate the output of the gyroscope and vector rotation sensor after the event occurrence

        //roll is index 1  // pitch is index 0
        SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values);
        SensorManager.remapCoordinateSystem(rotationMatrix, SensorManager.AXIS_X, SensorManager.AXIS_Z, rotationMatrix);
        SensorManager.getOrientation(rotationMatrix, orientationValues);

        if (sensorFirstTimeRead) {
            pitchValueOffset = (orientationValues[0]);
            sensorFirstTimeRead = false;
        }
        if (orientationValues[0] < 0)
            pitch = (roundDecimalNum((float) Math.toDegrees(orientationValues[0]) + (float) Math.toDegrees(pitchValueOffset)));
        else
            pitch = (roundDecimalNum((float) Math.toDegrees(orientationValues[0]) - (float) Math.toDegrees(pitchValueOffset))); // y asix rotation

        roll = -roundDecimalNum((float) Math.toDegrees(orientationValues[1]));  // x axis rotation

        if (smoothOutputCheck) {
            gyroAvgRoll = roundDecimalNum(smoothOutput(gyroAvgRoll, roll, 3));
            gyroAvgPitch = roundDecimalNum(smoothOutput(gyroAvgPitch, pitch, 3));
        } else {
            gyroAvgRoll = roll;
            gyroAvgPitch = pitch;
        }

        return new Pair[]{new Pair<>("Roll: ", gyroAvgRoll), new Pair<>("Pitch: ", gyroAvgPitch)};

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onStop() {
        mSensorManager.unregisterListener(this);
        super.onStop();
    }


    /**
     * // ========= inner class ===== bluetooth stream Listener thread============================================
     * /**
     * Contains all the necessary functions to establish the connection thread with the paired bt device
     * the thread is utilized to listen to the incoming stream
     */

    public class BTStreamListener extends Thread {

        //  public OutputStream btOutputStream;
        private BluetoothSocket btInputSocket;
        private BluetoothServerSocket btServerSocket;
        //public InputStream inStream;
        private byte[] btReadBuffer;
        private Handler readBTHandler;

        /**
         * sets a listener for bt inputStream buffer to detect the incoming data, used specially to receive the video output from the robot
         */

        private BTStreamListener() { //BluetoothSocket btSocket

            btInStream = null;
            openBTServerSocket();
            readBTStream();
        }

        /**
         * recalls bt connection manger in case the bt connection is off
         */
        private void openBTServerSocket() { //

            if (!btConnectionStatus) {
                try {
                    bluetoothConnectionManager();
                } catch (IOException e) {
                }
            }
        }

        /**
         * reads and process the incoming data from the bt buffer
         */

        void readBTStream() {
            InputStream tempIN = null;

            try {
                tempIN = btInputSocket.getInputStream();
            } catch (IOException e) {
                showToastMSG(getApplicationContext(), "inputStream error:", "inputStream is empty");
            }
            btInStream = tempIN;
        }


        public void run() {  // start recording bt stream RUN()
            btReadBuffer = new byte[1024];
            int numByte;

            while (true) {
                try {
                    numByte = btInStream.read(btReadBuffer);

                    for (int bIndex = 0; bIndex < numByte; bIndex++) {

                        imageBuffer[numIndex] = btReadBuffer[bIndex];
                        numIndex++;
                        if (bIndex > 0) {
                            if (btReadBuffer[bIndex - 1] == (byte) 0xFF && btReadBuffer[bIndex] == (byte) 0xD9) {
                                Message readMsg = readBTHandler.obtainMessage(0, numByte, -1, btReadBuffer);
                                readMsg.sendToTarget();
                            }
                        }
                    }

                } catch (IOException e) {
                    showToastMSG(getApplicationContext(), "bt stream read error", "input stream is disconnected");
                    closeConnection();
                    break;
                }
            }
        }
    }

    /**
     * // ^^^^^^^^^^^^^^^^^^^^ inner class ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^



     * //================ bt inStream receiver thread ===========================================

     * /** starts the receiving buffer thread to capture all the incoming stream from the paired device
     * it runs the app as server to receive all the incoming bytes form the robot and it runs until it
     * get closed or the connection fails
     */

    private class INStreamListenService extends Thread {

        private INStreamListenService() {
        }

        public void run() {

            setName("Inbound Thread"); // set the name of the thread

            while (!btConnectionStatus) {
                try {
                    bluetoothConnectionManager();
                } catch (IOException e) {
                    showToastMSG(getApplicationContext(), "BT connection Error:", "BT connection failed");
                    break;
                }

                if (myBluetooth != null) {
                    synchronized (MainActivity.this) {
                        if (btSocket.getRemoteDevice() != null) {
                            startBTInboundListener();
                        }
                    }
                }
            }
        }
    }

/** ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ inner class ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 */
}