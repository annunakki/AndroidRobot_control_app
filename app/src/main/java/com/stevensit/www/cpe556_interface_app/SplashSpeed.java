package com.stevensit.www.cpe556_interface_app;

import android.app.Application;
import android.os.SystemClock;

public class SplashSpeed extends Application {


    @Override
    public void onCreate(){

        super.onCreate();
        SystemClock.sleep(3000);
    }
}
