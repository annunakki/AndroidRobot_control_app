/**
 * main class for image and video data processing received from the robot output
 *
 * TODO, the code is theoretically done but not tested yet because of the missing hardware on the arduino side
 * ,, will be tested later after receiving the hardware of the camera and finihes the camera code for teh arduino code part
 */


package com.stevensit.www.cpe556_interface_app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.widget.Toast;


public class ImageCapture extends MainActivity {


    public ImageCapture(){

    }

//    public void receiveSensorData(){

//    }

//    public void cameraCaptureButton(View v) {
//
//      captureImage();
//      }

    /**
     * when called sends the command to arduino to start image capturing process
     * @param context
     */
    public  void captureImage(Context context){

        sendToBtStream("C");  // C character to send the capture command to the shutter button
        displayImage(context);
        }

    /**
     * decodes and compress image data in order to display it on ImageView object on the main screen of the app
     * @param context
     */

    public void displayImage(Context context) {
          //  viewCameraWindow = findViewById(R.id.imageViewWindow);
//            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
//            bitmapOptions.inSampleSize = 2;
            if (btInStream!=null) {
                Bitmap bitmapImage = BitmapFactory.decodeStream(btInStream);
                Bitmap scaledBitmap = setBitmapScale(bitmapImage);
                viewCameraWindow.setImageBitmap(scaledBitmap);
            }else{
               System.out.println( "error: bluetooth inputstream is empty");
                Toast.makeText(context , "no BT inputstream detected", Toast.LENGTH_SHORT).show();
            }
        }


    /**
     * rescales the output image to make it fit with the viewImage box
     *
     * @param bmp
     * @return
     */
       private Bitmap setBitmapScale(Bitmap bmp){
           int width = bmp.getWidth();
           int height = bmp.getHeight();
           Matrix bmpMatrix = new Matrix();
           float scaledWidth =  (float) viewCameraWindow.getWidth()/width;
           float scaledHeight = (float) viewCameraWindow.getHeight()/height;
           bmpMatrix.postScale(scaledWidth,scaledHeight);
            return Bitmap.createBitmap(bmp,0,0,width,height,bmpMatrix,true);

       }

}