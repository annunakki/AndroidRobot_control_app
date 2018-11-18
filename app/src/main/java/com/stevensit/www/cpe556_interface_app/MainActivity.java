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
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.lang.String;
import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends Activity implements OnClickListener {

    Button i1;
    TextView t1;
    String address = null , name=null;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    Set<BluetoothDevice> pairedDevices;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private Button btnSettings, btnBTConnect, btnForward, btnReverse, btnLeft, btnRight, btnCameraCenter;
    private TextView textStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        try {initialize();} catch (Exception e) {}
    }

    @SuppressLint("ClickableViewAccessibility")

    private void initialize() throws IOException {

        btnForward = (Button) findViewById(R.id.btnForward);
        btnReverse = (Button) findViewById((R.id.btnReverse));
        btnLeft = (Button) findViewById(R.id.btnTurnLeft);
        btnRight = (Button) findViewById(R.id.btnTurnRight);
        textStatus = (TextView) findViewById(R.id.textStatusBox);

        setButtonListener (btnForward, "F");  //move forward
        setButtonListener (btnReverse, "B");  // move reverse or backward
        setButtonListener (btnLeft, "L");  // move to the left
        setButtonListener (btnRight, "R");  //move to the right



////
//        t1=(TextView)findViewById(R.id.textStatusBox);
        bluetooth_connect_device();



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


    private void bluetooth_connect_device() throws IOException
    {
        try
        {
            myBluetooth = BluetoothAdapter.getDefaultAdapter();
            address = myBluetooth.getAddress();
            pairedDevices = myBluetooth.getBondedDevices();
            if (pairedDevices.size()>0)
            {
                for(BluetoothDevice bt : pairedDevices)
                {
                    address=bt.getAddress().toString();name = bt.getName().toString();
                    Toast.makeText(getApplicationContext(),"Connected", Toast.LENGTH_SHORT).show();

                }
            }

        }
        catch(Exception we){}
        myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
        BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
        btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
        btSocket.connect();
        try { t1.setText("BT Name: "+name+"\nBT Address: "+address); }
        catch(Exception e){}
    }




    @SuppressLint("ClickableViewAccessibility")
    public void  setButtonListener(final Button btn, final String chr){
        btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    setBtnStreamChar(chr);
                    System.out.println(btn.toString());
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    setBtnStreamChar("0");
                    System.out.println("robot stopped");
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
                btSocket.getOutputStream().write(streamChr.toString().getBytes());
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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }
    }


    public void btMenu(View v){

        Intent intentSettings = new Intent(this,BluetoothMenu.class);
        startActivity(intentSettings);


    }

    public void settingMenu(View v){

        Intent intentSettings = new Intent(this,SettingMenu.class);
        startActivity(intentSettings);


    }

}
