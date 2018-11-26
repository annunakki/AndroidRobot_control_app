//package com.stevensit.www.cpe556_interface_app;
//
//import android.hardware.Sensor;
//import android.hardware.SensorEvent;
//import android.hardware.SensorEventListener;
//
//public class SensorsSystem extends MainActivity implements SensorEventListener {
//
////    private SensorManager mSensorManager;
////    private Sensor sensor;
////    private List sensorList;
//   // private TextView txtAccelSensorOutput ,txtGyroSensorOut ;
//    //private TextView txtSensorOutput;
//
//
//   // SensorEventListener sensorListener;//= new SensorEventListener() {
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
////    public SensorsSystem() {
////
////
////        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
////        sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
////        sensorList = mSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
////        if (sensorList.size() > 0)
////            mSensorManager.registerListener(sensorListener, (Sensor) sensorList.get(0), SensorManager.SENSOR_DELAY_NORMAL);
////        else
////            Toast.makeText(getApplicationContext(),"no accelerometer sensor detected",Toast.LENGTH_SHORT).show();
////
////    }
//
//
////    @Override
////    public void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////
////        txtAccelSensorOutput = findViewById(R.id.txtAccelSensorOut);
////        txtGyroSensorOut = findViewById(R.id.txtGyroSensorOut);
////    }
//
//    @Override
//    public void onSensorChanged(SensorEvent event) {
//        float[] values = event.values;
//        Sensor sens = event.sensor;
//
//       // if (sens.getType() == Sensor.TYPE_ACCELEROMETER)
//            txtAccelSensorOutput.setText("accel\n"+"X:" + values[0] + "\n" + "Y:" + values[1] + "\n" + "Z:" + values[2]);
//
////        if (sens.getType() == Sensor.TYPE_GYROSCOPE)
////            txtGyroSensorOut.setText("gyro\n"+"X:" + values[0] + "\n" + "Y:" + values[1] + "\n" + "Z:" + values[2]);
//
//    }
//
//
//    @Override
//    public void onAccuracyChanged(Sensor sensor, int accuracy) {
//
//    }
//
////    @Override
////    public void onStop() {
////        if (sensorList.size() > 0) {
////            mSensorManager.unregisterListener(sensorListener);
////        }
////        super.onStop();
////    }
//}





//        gyroListener = new SensorEventListener() {
//
//            @Override
//            public void onSensorChanged(SensorEvent event) {
//                float[] values = event.values;
//                txtAccelSensorOut.setText("Linear Accel\n"+"X:" + roundDecimalNum(values[0],numFormat) + "\n" + "Y:" + roundDecimalNum(values[1],numFormat) + "\n" + "Z:" + roundDecimalNum(values[2],numFormat));
//              //  txtGyroSensorOut.setText("Gyro rad/s \n"+"X:" + roundDecimalNum(values[0],numFormat) + "\n" + "Y:" + roundDecimalNum(values[1],numFormat) + "\n" + "Z:" + roundDecimalNum(values[2],numFormat));
//
//            }
//            @Override
//
//            public void onAccuracyChanged(Sensor sensor, int accuracy) {
//
//            }
//        };
