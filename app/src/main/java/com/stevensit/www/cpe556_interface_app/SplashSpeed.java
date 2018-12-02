package com.stevensit.www.cpe556_interface_app;

import android.app.Application;
import android.os.SystemClock;

/*
controls the display duration time of the splash screen

 */

public class SplashSpeed extends Application {


    @Override
    public void onCreate(){

        super.onCreate();
        SystemClock.sleep(2500);
    }
}
