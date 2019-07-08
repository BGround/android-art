package com.example.ipc_messager;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import com.example.ipc_messager.utils.MyConstants;

public class MessengerService extends Service {
    private static final String TAG = "MessagerService";

    @Override
    public IBinder onBind(Intent intent) {
        return mMessager.getBinder();
    }

    private static class MessagerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MyConstants.MSG_FROM_CLIENT:
                    Log.d(TAG, "handleMessage: " + msg.getData().getString("msg"));

                    Messenger client = msg.replyTo;
                    Message replyMessage = Message.obtain(null, MyConstants.MSG_FROM_CLIENT);
                    Bundle bundle = new Bundle();
                    bundle.putString("reply","yes, receiver your message and reply to you in seconds");
                    replyMessage.setData(bundle);

                    try {
                        client.send(replyMessage);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    private final Messenger mMessager = new Messenger(new MessagerHandler());
}
