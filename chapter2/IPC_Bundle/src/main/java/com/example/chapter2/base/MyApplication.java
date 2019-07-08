package com.example.chapter2.base;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import java.util.List;

public class MyApplication extends Application {
    private static final String TAG = "MyApplication";
    private static MyApplication mInstance;
    private static MyApplication getInstance() {return mInstance;}

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        String processName = getProcessName(getApplicationContext(),android.os.Process.myPid());
        Log.d(TAG, "processName: " + processName);
    }

    public static String getProcessName(Context cxt, int pid) {
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName;
            }
        }
        return null;
    }
}
