package com.stevensit.www.cpe556_interface_app;


/*

contains all the objects of the settings sub menu that's assigned with SETTINGS button on the main app screen

 */
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import static com.stevensit.www.cpe556_interface_app.MainActivity.firstTimeRead;

public class SettingMenu extends AppCompatActivity  {



    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_menu);

    }

    public void settingMenu(View v){

    }

    public void calibrateGyroSensor(View v){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setTitle("Confirmation Message");
        builder.setMessage("This will set the gyroscope sensor to zero position\n \nPlease hold the phone in the desired orientation then press calibrate to processed with the calibration process");
        builder.setPositiveButton("Calibrate",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"Please wait\n"+"Calibrating ...", Toast.LENGTH_LONG).show();

                        new Handler().postDelayed(new Runnable() { // add a delay before connecting the bt socket to the paired device
                            @Override
                            public void run() {
                                try {
                                     } catch (Exception e) {
                                    //   Toast.makeText(getApplicationContext(),"BT connection error\n" +e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, 3000);
                        firstTimeRead = true;
                        System.out.println("Gyro calibration pos set to zero");
                        Toast.makeText(getApplicationContext(),"Calibration successful", Toast.LENGTH_SHORT).show();
                    }
                });

        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void aboutMenu(View v){
        Intent intentSettings = new Intent(this,AboutMenu.class);
        startActivity(intentSettings);

    }


}
