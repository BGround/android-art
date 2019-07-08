package com.example.ipc_socket;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ipc_socket.utils.MyUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Date;
import java.text.SimpleDateFormat;

public class IPC_Socket_ClientActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int MESSAGE_RECEIVE_NEW_MSG = 1;
    private static final int MESSAGE_SOCKET_CONNECTED = 2;

    private Button mSendButton;
    private TextView mMsgContainer;
    private EditText mMsgEditText;

    private PrintWriter mPrintWriter;
    private Socket mClicentSocket;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MESSAGE_RECEIVE_NEW_MSG: {
                    System.out.println("handler: "+ msg.obj +" msg :" +msg.toString());
                    mMsgContainer.setText(mMsgContainer.getText() + (String) msg.obj);//? msg.obj
                    break;
                }
                case MESSAGE_SOCKET_CONNECTED: {
                    mSendButton.setEnabled(true);
                    break;
                }
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ipc__socket__main);
        mMsgContainer = findViewById(R.id.msg_container);
        mMsgEditText = findViewById(R.id.msg);
        mSendButton = findViewById(R.id.send);
        mSendButton.setOnClickListener(this);
        Intent service = new Intent(this, TCPServerService.class);
        startService(service);
        new Thread() {
            @Override
            public void run() {
                super.run();
                connectTCPServer();
            }
        }.start();
    }

    private void connectTCPServer() {
        //
        Socket socket = null;
        while (socket == null) {
            try {
                socket = new Socket("localhost", 8688);
                mClicentSocket = socket;
                mPrintWriter = new PrintWriter(new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream())), true);
                mHandler.sendEmptyMessage(MESSAGE_SOCKET_CONNECTED);
            } catch (IOException e) {
                SystemClock.sleep(1000);
                System.out.println("connect tcp server failed, retry...");
                e.printStackTrace();
            }
        }

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (!IPC_Socket_ClientActivity.this.isFinishing()) {
                String msg = br.readLine();
                System.out.println("receive :" + msg);
                if (msg != null) {
                    String time = formatDateTime(System.currentTimeMillis());
                    final String showed_serverMsg = "server" + time + ":" + msg + "\n";
                    mHandler.obtainMessage(MESSAGE_RECEIVE_NEW_MSG, showed_serverMsg)
                            .sendToTarget();
                }
            }
            System.out.println("quit...");
            MyUtils.close(mPrintWriter);
            MyUtils.close(br);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mClicentSocket != null) {
            try {
                mClicentSocket.shutdownInput();
//                mClicentSocket.shutdownOutput();
                mClicentSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        //
        if (v == mSendButton) {
            final String msg = mMsgEditText.getText().toString();
            if (!TextUtils.isEmpty(msg) && mPrintWriter != null) {
                mPrintWriter.println(msg);
                mMsgEditText.setText("");
                String time = formatDateTime(System.currentTimeMillis());
                final String showed_selfMsg = "self" + time + ":" + msg + "\n";
                Log.d("zhangya", "onClick: "+showed_selfMsg);
                mMsgContainer.setText(mMsgContainer.getText() +showed_selfMsg);
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private String formatDateTime(long currentTimeMillis) {
        return new SimpleDateFormat("HH:mm:ss").format(new Date(currentTimeMillis));
    }
}




