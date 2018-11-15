package com.stevensit.www.cpe556_interface_app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Switch;


public class MainActivity extends AppCompatActivity  {

    private Button btnSettings, btnBTConnect, btnForward, btnReverse, btnLeft, btnRight, btnCameraCenter;
    private Switch swCamera;

    private final String msgStart = "Robot is Ready";

@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.main_activity) ;

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
    }


//    public void btMenu(View v){
//
//        Intent intentSettings = new Intent(this,BluetoothMenu.class);
//        startActivity(intentSettings);
//
//
//    }
//
//    public void settingMenu(View v){
//
//        Intent intentSettings = new Intent(this,BluetoothMenu.class);
//        startActivity(intentSettings);
//
//
//    }


}
