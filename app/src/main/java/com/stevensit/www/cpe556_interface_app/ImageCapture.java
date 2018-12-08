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
//
//
//
//    }

//    public void cameraCaptureButton(View v) {
//
//      captureImage();
//      }

    public  void captureImage(Context context){

        sendToBtStream("C");  // C character to send the capture command to the shutter button
        displayImage(context);
        }


        public void displayImage(Context context) {
          //  viewCameraWindow = findViewById(R.id.imageViewWindow);
//            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
//            bitmapOptions.inSampleSize = 2;
            if (btInStream!=null) {
                Bitmap bitmapImage = BitmapFactory.decodeStream(btInStream);
                Bitmap scaledBitmap = setBitmaScale(bitmapImage);
                viewCameraWindow.setImageBitmap(scaledBitmap);

            }else{
               System.out.println( "error: bluetooth inputstream is empty");
                Toast.makeText(context , "no BT inputstream detected", Toast.LENGTH_SHORT).show();
            }
        }

       private Bitmap setBitmaScale(Bitmap bmp){
           int width = bmp.getWidth();
           int height = bmp.getHeight();
           Matrix bmpMatrix = new Matrix();
           float scaledWidth =  (float) viewCameraWindow.getWidth()/width;
           float scaledHeight = (float) viewCameraWindow.getHeight()/height;
           bmpMatrix.postScale(scaledWidth,scaledHeight);
            return Bitmap.createBitmap(bmp,0,0,width,height,bmpMatrix,true);

       }


}