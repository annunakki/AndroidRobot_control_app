//package com.stevensit.www.cpe556_interface_app;
//
//import android.bluetooth.BluetoothAdapter;
//import android.bluetooth.BluetoothDevice;
//import android.bluetooth.BluetoothSocket;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.view.View;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.ListView;
//import android.widget.Switch;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Set;
//import java.util.UUID;
//
//public class BluetoothMenu extends AppCompatActivity {
//
//    Button btnSearch, btnPair, btForward;
//    Switch swTurnON;
//    private ListView deviceList;
//    private BluetoothAdapter btAdapter;
//    private Set<BluetoothDevice> pairedDevices;
//    public  TextView vtext;
//    BluetoothSocket btSocket ;
//    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        Intent newint = getIntent();
//       // String address = newint.getStringExtra(DeviceList.EXTRA_ADDRESS);
//        String address = "98D311FC177D";
//
//        setContentView(R.layout.bluetooth_menu);
//
//        btnSearch = (Button) findViewById(R.id.btnSearch);
//        btnPair = (Button) findViewById(R.id.btnPair);
//        btAdapter = BluetoothAdapter.getDefaultAdapter();
//        deviceList = (ListView) findViewById(R.id.deviceList);
//        vtext = (TextView)findViewById(R.id.textView);
//        btForward = (Button) findViewById(R.id.btnForward);
//
//
//
//        btAdapter = BluetoothAdapter.getDefaultAdapter();
//        BluetoothDevice dispositivo = btAdapter.getRemoteDevice(address);
//
//
//        try {
//            btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//        btForward.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick (View v){
//                moveForward();
//            }});
//
//
//
//    }
//
//    public void btTurnOn(View v){
//        if (!btAdapter.isEnabled()) {
//            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivityForResult(turnOn, 0);
//            Toast.makeText(getApplicationContext(), "BT is Turned on",Toast.LENGTH_LONG).show();
//        } else {
//            Toast.makeText(getApplicationContext(), "BT Already on", Toast.LENGTH_LONG).show();
//        }
//    }
//
//    public void btTurnOff(View v){
//        btAdapter.disable();
//        Toast.makeText(getApplicationContext(), "BT is Turned off" ,Toast.LENGTH_LONG).show();
//    }
//
//
//
//    public void searchList(View v){
//        pairedDevices = btAdapter.getBondedDevices();
//
//
//        ArrayList list = new ArrayList();
//
//        for(BluetoothDevice bt : pairedDevices)
//            list.add(bt.getName());
//        Toast.makeText(getApplicationContext(), "Paired Devices",Toast.LENGTH_SHORT).show();
//
//
//        final ArrayAdapter adapter = new  ArrayAdapter(this,android.R.layout.simple_list_item_1, list);
//
//
//        deviceList.setAdapter(adapter);
//        vtext.setText(adapter.toString());
//    }
//
//    public void btMenu(View view) {
//
//    }
//
//    private void msg(String s)
//    {
//        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
//    }
//
//
//
//
//
//
//    public void moveForward(){
//
//        if (btSocket!=null)
//        {
//            try
//            {
//                btSocket.getOutputStream().write("1".toString().getBytes());
//            }
//            catch (IOException e)
//            {
//                msg("Error");
//            }
//        }
//    }
//}
//
