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
import static com.stevensit.www.cpe556_interface_app.MainActivity.sensorDelay;
import static com.stevensit.www.cpe556_interface_app.MainActivity.sensorFirstTimeRead;
import static com.stevensit.www.cpe556_interface_app.MainActivity.gyroPitch_Threshold;
import static com.stevensit.www.cpe556_interface_app.MainActivity.gyroRoll_Threshold;
import static com.stevensit.www.cpe556_interface_app.MainActivity.sharedPref;

public class SettingMenu extends AppCompatActivity {

    public EditText editGyroPitchThresh, editGyroRollThresh, editSensorDelay;
    protected CheckBox cbSensorsOut;

    private int maxVal = 50; // the max allowed value for the threshold output


    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_menu);
        editGyroRollThresh = findViewById(R.id.editRollThreshold);
        editGyroPitchThresh = findViewById(R.id.editPitchThreshold);
        editSensorDelay=findViewById(R.id.editSensorDelay);
        cbSensorsOut = findViewById(R.id.checkBoxSensors);

        editGyroPitchThresh.setHint(String.valueOf((int)gyroPitch_Threshold));
        editGyroRollThresh.setHint(String.valueOf((int)gyroRoll_Threshold));
        editSensorDelay.setHint(String.valueOf(sensorDelay));

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

                    if (tempVal > maxVal) {
                        text.replace(0, text.length(), String.valueOf(maxVal), 0, 2);
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
                } else {
                    tempVal = Float.valueOf(text.toString());
                }


                try {
                    if (tempVal > maxVal) {
                        text.replace(0, text.length(), String.valueOf(maxVal), 0, 2);

                    }
                } catch (NumberFormatException ex) {
                }
                storeValue("roll", text);
            }
        });

        editSensorDelay.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable text) {

                int tempVal;

                if (text.toString().equals(null) || text.toString().equals("")) {
                    tempVal = 0;
                } else {
                    tempVal = Integer.valueOf(text.toString());
                }

                try {

                    if (tempVal > 100000000) {
                        text.replace(0, text.length(), String.valueOf(100000000), 0, 9);
                    }
                } catch (NumberFormatException ex) {
                }

                if (text.toString().equals(null) || text.toString().equals("")){
                    text.append("");
                    sharedPref.saveIntValue("delay", 0);

                }else
                    sharedPref.saveIntValue("delay", Integer.valueOf(text.toString()));
            }
        });


    }


    /**
     * called by the text change listener to save the entered value
     * @param tag
     * @param txtVal
     */

    private void storeValue(String tag, Editable txtVal){

        if (txtVal.toString().equals(null) || txtVal.toString().equals("")){
            txtVal.append("");
            sharedPref.saveValue(tag,0);

        }else
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
     * by setting sensorFirstTimeRead = true;
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
                            sensorFirstTimeRead = true;
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
