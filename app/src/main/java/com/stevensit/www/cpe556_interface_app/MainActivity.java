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
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends Activity implements OnClickListener {


    String address = null , name=null;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    Set<BluetoothDevice> pairedDevices;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private Button btnSettings, btnBTConnect, btnForward, btnReverse, btnLeft, btnRight, btnCameraCenter;
    private TextView textStatus;
    private Switch cameraSW;
    private ImageButton btImage;
    public final String msgForward = " moving forward";
    public  final String msgStop = " robot stopped";
    public final String msgBackward = " moving reverse";
    public  final String msgLeft = " robot left";
    public final String msgRight = " moving right";



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        try {initialize();} catch (Exception e) {}


    }

    @SuppressLint("ClickableViewAccessibility")

    private void initialize() {

        btnForward =  findViewById(R.id.btnForward);
        btnReverse =  findViewById((R.id.btnReverse));
        btnLeft = findViewById(R.id.btnTurnLeft);
        btnRight =  findViewById(R.id.btnTurnRight);
        textStatus =  findViewById(R.id.textStatusBox);
        btImage =  findViewById(R.id.btnImage);
        cameraSW =  findViewById(R.id.swCamera) ;
        btImage.setVisibility(View.INVISIBLE);

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


    public void connectBluetooth(View v) throws IOException
    {


        try
        {
            myBluetooth = BluetoothAdapter.getDefaultAdapter();
            if (!myBluetooth.isEnabled()){
                Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(turnOn, 0);
                Toast.makeText(getApplicationContext(), "BT is Turned on",Toast.LENGTH_LONG).show();
            }

            address = myBluetooth.getAddress();
            pairedDevices = myBluetooth.getBondedDevices();
            if (pairedDevices.size()>0)
            {
                for(BluetoothDevice bt : pairedDevices)
                {
                    address=bt.getAddress();
                    name = bt.getName();
                    Toast.makeText(getApplicationContext(),"Connected", Toast.LENGTH_SHORT).show();
                    btImage.setVisibility(View.VISIBLE);

                }
            }

        }
        catch(Exception we){}
        //myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
        BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
        btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
        btSocket.connect();
        try { textStatus.setText("BT Name: "+name+"\nBT Address: "+address); }
        catch(Exception e){}
    }




    @SuppressLint("ClickableViewAccessibility")
//    public void  setButtonListener(final Button btn, final String chr){
//        btn.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    setBtnStreamChar(chr);
//                    System.out.println(btn.toString());
//                }
//                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    setBtnStreamChar("0");
//                    System.out.println("robot stopped");
//                }
//                return true;
//            }});
//        }

   public void stopMSGDisplay(){
        setBtnStreamChar("0");
        System.out.println(msgStop);
        textStatus.setText(msgStop);
    }

    public void moveScreenStatus(String s){

        System.out.println(s);
        textStatus.setText(s);

    }

    public void moveForward (View v){

        btnForward.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    setBtnStreamChar("F");
                    moveScreenStatus(msgForward);

                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    stopMSGDisplay();
                }
                return true;
            }});

    }

    public void moveReverse (View v){

        btnReverse.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    setBtnStreamChar("B");
                    moveScreenStatus(msgBackward);

                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    stopMSGDisplay();
                }
                return true;
            }});

    }

    public void turnLeft (View v){

        btnLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    setBtnStreamChar("F");
                    moveScreenStatus(msgLeft);

                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    stopMSGDisplay();
                }
                return true;
            }});

    }

    public void turnRight (View v){
        final String msgRight = "turning right";
        btnRight.setOnTouchListener(new View.OnTouchListener() {
            @Override

            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    setBtnStreamChar("F");
                    moveScreenStatus(msgRight);

                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    stopMSGDisplay();
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

//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//
//        // Checks the orientation of the screen
//        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
//        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
//            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
//        }
//    }


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
//        if (cameraSW.isChecked())
//
//
//    }
}
