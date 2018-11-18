//
//package com.stevensit.www.cpe556_interface_app;
//
//
//import android.bluetooth.BluetoothAdapter;
//import android.bluetooth.BluetoothDevice;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.support.v7.app.ActionBar;
//import android.support.v7.app.AlertDialog;
//import android.widget.Button;
//import android.widget.Switch;
//import android.widget.Toast;
//
//import java.util.Set;
//
//import static android.support.v4.app.ActivityCompat.startActivityForResult;
//
//
//public class BTconnect extends ActionBar {
//
//
//    private Set<BluetoothDevice> deviceList;
//    Button btPair, btSearch;
//    private BluetoothAdapter btAdapter = null;
//    private Switch BTStatus;
//
//    public void startBTConnection() {
//
//        btAdapter = BluetoothAdapter.getDefaultAdapter();
//        if (btAdapter == null) {
//            new AlertDialog.Builder(this)
//                    .setTitle("Adapter is not Compatible")
//                    .setMessage("BT adapter is not compatible")
//                    .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            System.exit(0);
//                        }
//                    }).setIcon(android.R.drawable.ic_dialog_alert).show();
//        } else {
//            if (btAdapter.isEnabled()) {
//                Toast.makeText(getApplicationContext(), "Bluetooth is Enabled", Toast.LENGTH_LONG).show();
//            } else {
//                Intent turnBtn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//                startActivityForResult(turnBtn, 1);
//            }
//        }
//
//        deviceList = btAdapter.getBondedDevices();
//        if (deviceList.size() > 0) {
//
//            for (BluetoothDevice device : deviceList) {
//                String deviceName = device.getName();
//                String deviceMAC = device.getAddress();
//            }
//        }
//    }
//
//    public void listBTDevices (View v){
//        pairedDevices = BA
//
//
//    }
//}
//
//
