 package com.example.ipc_binderpool;

import android.annotation.SuppressLint;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

 public class IPC_BinderPool_MainActivity extends AppCompatActivity {

    private static final String TAG = "IPC_BinderPool_MainActivity";

    private ISecurityCenter mISecurityCenter;
    private IComputer mIComputer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ipc__binder_pool__main);
        new Thread(new Runnable() {
            @Override
            public void run() {
                doWork();
            }
        }).start();
    }

    @SuppressLint("LongLogTag")
    private void doWork() {
        BinderPool binderPool = BinderPool.getInstance(IPC_BinderPool_MainActivity.this);

        IBinder securityBinder = binderPool.queryBinder(BinderPool.BINDER_SECURITY_CENTER);
        mISecurityCenter = SecurityCenterImpl.asInterface(securityBinder);
        Log.d(TAG, "doWork: visit security center");
        String msg = "helloworld-安卓";
        Log.d(TAG, "content :" +msg);
        try {
            String password = mISecurityCenter.encrypt(msg);
            System.out.println("encrypt:" + password);
            System.out.println("decrypt:" + mISecurityCenter.decrypt(password));
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        IBinder computerBinder = binderPool.queryBinder(BinderPool.BINDER_COMPUTER);
        mIComputer = ComputerImpl.asInterface(computerBinder);
        try {
            System.out.println("3+5=" + mIComputer.add(3, 5));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
