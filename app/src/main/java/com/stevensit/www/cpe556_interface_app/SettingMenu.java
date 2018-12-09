package com.stevensit.www.cpe556_interface_app;


/*

contains all the objects of the settings sub menu that's assigned with SETTINGS button on the main app screen
it contains both help and about menus

 */

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import static com.stevensit.www.cpe556_interface_app.MainActivity.btConnectionStatus;
import static com.stevensit.www.cpe556_interface_app.MainActivity.checkBoxChecked;
import static com.stevensit.www.cpe556_interface_app.MainActivity.firstTimeRead;
import static com.stevensit.www.cpe556_interface_app.MainActivity.gyroPitch_Threshold;
import static com.stevensit.www.cpe556_interface_app.MainActivity.gyroRoll_Threshold;
import static com.stevensit.www.cpe556_interface_app.MainActivity.sharedPref;

public class SettingMenu extends AppCompatActivity {

    public EditText editGyroPitchThresh, editGyroRollThresh;
    protected CheckBox cbSensorsOut;

    private int maxVal = 50; // the max allowed value for the threshold output


    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_menu);
        editGyroRollThresh = findViewById(R.id.editRollThreshold);
        editGyroPitchThresh = findViewById(R.id.editPitchThreshold);
        editGyroPitchThresh.setHint(String.valueOf(gyroPitch_Threshold));
        editGyroRollThresh.setHint(String.valueOf(gyroRoll_Threshold));
        cbSensorsOut = findViewById(R.id.checkBoxSensors);

        cbSensorsOut.setChecked(checkBoxChecked);

        setTextListeners();
    }


    /**
     * checks for the entered numerical value is within the permitted set range
     */

    private void setTextListeners() {

        editGyroPitchThresh.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable text) {

                float tempVal;

                if (text.toString().equals(null) || text.toString().equals("")) {
                    tempVal = 0;
                } else {
                    tempVal = Float.valueOf(text.toString());
                }


                try {

                    if (tempVal > 50) {
                        text.replace(0, text.length(), String.valueOf(maxVal), 0, 2);

//                    } else if(tempVal < 0) {
//                        text.replace(0, text.length(), String.valueOf(minVal), 0, 1);
//                        System.out.println("the entered value is less than 0");
                        //  Toast.makeText(getApplicationContext(),"min value is: "+minVal,Toast.LENGTH_SHORT).show();
                        //  tempVal=minVal;
                    }
                } catch (NumberFormatException ex) {
                }
                storeValue("pitch", text);
            }
        });

        /**
         * listens to the text changes of gyro roll input box
         */

        editGyroRollThresh.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable text) { // Roll

                float tempVal;

                if (text.toString().equals(null) || text.toString().equals("")) {

                    tempVal = 0;
                    Toast.makeText(getApplicationContext(), String.valueOf(tempVal), Toast.LENGTH_SHORT).show();
                } else {
                    tempVal = Float.valueOf(text.toString());
                }


                try {
                    if (tempVal > 50) {
                        text.replace(0, text.length(), String.valueOf(maxVal), 0, 2);
                        // Toast.makeText(getApplicationContext(), "max value is: " + maxVal, Toast.LENGTH_SHORT).show();
//                    } else if (tempVal < 0) {
//                          text.replace(0, text.length(), "0", 0, 1);
//                        System.out.println("the entered value is less than 0");
//                       // Toast.makeText(getApplicationContext(), "min value is: " + minVal, Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException ex) {
                }
                storeValue("roll", text);
            }
        });

    }


    /**
     * called by the text change listener to save the entered value
     * @param tag
     * @param txtVal
     */

    private void storeValue(String tag, Editable txtVal){
        if (txtVal.toString().equals(null) || txtVal.toString().equals("")) txtVal.append("0");
           sharedPref.saveValue(tag,Float.valueOf(txtVal.toString()));

       }

    /**
     * stores the value of the checkbox
     * @param v
     */

    public void checkBox (View v){
        if (cbSensorsOut.isChecked()) {
            checkBoxChecked = true;
        }

        else {
            checkBoxChecked = false;
        }
    }


    //public void settingMenu(View v){ }

    /**
     * it calibrates gyroscope sensor output to zero at the set orientation position of the phone
     * by setting firstTimeRead = true;
     * @param v
     */

    public void calibrateGyroSensor(View v) {

        if (!btConnectionStatus) {
            Toast.makeText(getApplicationContext(), "Error\nBluetooth is not activated", Toast.LENGTH_SHORT).show();
        } else {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setIcon(android.R.drawable.ic_dialog_alert);
            builder.setTitle("Confirmation Message");
            builder.setMessage("This will set the gyroscope sensor to zero position\n \nPlease hold the phone in the desired orientation then press calibrate to processed with the calibration process");
            builder.setPositiveButton("Calibrate",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getApplicationContext(), "Please wait\n" + "Calibrating ...", Toast.LENGTH_LONG).show();

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
                            Toast.makeText(getApplicationContext(), "Calibration successful", Toast.LENGTH_SHORT).show();
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
    }

    /**
     * opens about menu when help button is pressed
     * @param v
     */

    public void aboutMenu(View v){
        Intent intentSettings = new Intent(this,AboutMenu.class);
        startActivity(intentSettings);

    }


    /**
     * opens help menu when help button is pressed
     * @param v
     */

    public void helpMenu(View v){
        Intent intentSettings = new Intent(this,HelpMenu.class);
        startActivity(intentSettings);

    }

}
