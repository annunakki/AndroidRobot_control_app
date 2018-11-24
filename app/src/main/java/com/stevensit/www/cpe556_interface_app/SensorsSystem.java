//package com.stevensit.www.cpe556_interface_app;
//
//import android.content.Context;
//import android.hardware.Sensor;
//import android.hardware.SensorEvent;
//import android.hardware.SensorEventListener;
//import android.hardware.SensorManager;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import java.util.List;
//
//public class SensorsSystem extends MainActivity implements SensorEventListener {
//
//    private SensorManager mSensorManager;
//    private Sensor sensor;
//    private List sensorList;
//    //private TextView txtSensorOutput;
//
//
//    SensorEventListener sensorListener;//= new SensorEventListener() {
////        @Override
////        public void onSensorChanged(SensorEvent event) {
////            float[] values = event.values;
////            txtSensorOutput.setText("X:" + values[0] + "\n" + "Y:" + values[1] + "\n" + "Z:" + values[2]);
////        }
////
////        @Override
////        public void onAccuracyChanged(Sensor sensor, int accuracy) {
////
////        }
////    };
//
//
//    public SensorsSystem() {
//
//
//        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
//        sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//        sensorList = mSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
//        if (sensorList.size() > 0)
//            mSensorManager.registerListener(sensorListener, (Sensor) sensorList.get(0), SensorManager.SENSOR_DELAY_NORMAL);
//        else
//            Toast.makeText(getApplicationContext(),"no accelerometer sensor detected",Toast.LENGTH_SHORT).show();
//
//    }
//
//
//    @Override
//    public void onSensorChanged(SensorEvent event) {
//        float[] values = event.values;
//        txtSensorOutput.setText("X:" + values[0] + "\n" + "Y:" + values[1] + "\n" + "Z:" + values[2]);
//    }
//
//
//    @Override
//    public void onAccuracyChanged(Sensor sensor, int accuracy) {
//
//    }
//
//    @Override
//    public void onStop() {
//        if (sensorList.size() > 0) {
//            mSensorManager.unregisterListener(sensorListener);
//        }
//        super.onStop();
//    }
//}
