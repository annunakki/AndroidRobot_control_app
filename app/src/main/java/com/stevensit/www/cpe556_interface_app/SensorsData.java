//package com.stevensit.www.cpe556_interface_app;
//
//import android.content.Context;
//import android.hardware.Sensor;
//import android.hardware.SensorEvent;
//import android.hardware.SensorEventListener;
//import android.hardware.SensorManager;
//import android.widget.Toast;
//
//import static android.support.v4.content.ContextCompat.getSystemService;
//
//public class SensorsData implements SensorEventListener {
//
//
//    private SensorManager mSensorManager;
//    private Sensor snsAccelerometer, snsGyroscopeVector;
//
//
//    public SensorsData(){
//        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
//
//        snsAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
//        snsGyroscopeVector = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
//
//        if (snsAccelerometer!=null )
//            mSensorManager.registerListener(this, snsAccel, SensorManager.SENSOR_DELAY_NORMAL);
//        else
//            Toast.makeText(getApplicationContext(),"no accelerometer sensor detected",Toast.LENGTH_SHORT).show();
//
//        if (snsGyroscopeVector != null)
//            mSensorManager.registerListener(this, snsGyro, SensorManager.SENSOR_DELAY_NORMAL);
//        else
//            Toast.makeText(getApplicationContext(),"no gyroscope sensor detected",Toast.LENGTH_SHORT).show();
//
//
//    }
//
//
//    @Override
//    public void onSensorChanged(SensorEvent event) {
//
//    }
//
//    @Override
//    public void onAccuracyChanged(Sensor sensor, int accuracy) {
//
//    }
//
//    @Override
//    public void pause(){
//
//    }
//}
