package com.greatmap.idefunction;

import android.app.Application;

/**
 * Created by police on 2016/6/15.
 */
public class IdeApplication extends Application {
    private static IdeApplication sApplication ;

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
    }

    public static IdeApplication getInstance() {
        return sApplication;
    }
}
