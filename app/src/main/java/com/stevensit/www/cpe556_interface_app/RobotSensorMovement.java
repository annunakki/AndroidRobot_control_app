package com.stevensit.www.cpe556_interface_app;

import android.view.View;

public class RobotSensorMovement extends MainActivity {



    public RobotSensorMovement(){

    }

    public void receiveSensorData(){


    }

    @Override
    public void onClick(View v) {

      captureAnImage();
      }

    public  void captureAnImage(){

        sendToBtStream("C");  // C character to send the capture command to the shutter button
        }


        public void displayImage(){



}
}
