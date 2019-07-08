package com.example.ipc_socket;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.example.ipc_socket.utils.MyUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class TCPServerService extends Service {

    private boolean mIsServiceDestoryed = false;
    private String[] mDefinedMessages = new String[]{
            "你好啊",
            "1111111111",
            "2222222222",
            "333333333",
            "444444444"
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        new Thread(new TcpServer()).start();
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mIsServiceDestoryed = true;
    }

    private class TcpServer implements Runnable {

        @Override
        public void run() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(8688);
        } catch (IOException e) {
            System.err.println("establish tcp server fail port:8688");
            e.printStackTrace();
            return;
        }

        while (!mIsServiceDestoryed) {
            try {
                //接受client请求
                final Socket client = serverSocket.accept();
                System.out.println("accept");
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            responseClient(client);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

    private void responseClient(Socket client) throws IOException {
        //用于接受客户端信息
        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        //用于向客户端发送消息
        PrintWriter out = new PrintWriter(
                new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);
        out.println("欢迎来到聊天室");
        while (!mIsServiceDestoryed) {
            String str = in.readLine();
            System.out.println("msg from client:" + str);
            if (str == null) {
                break;
            }
            int i = new Random().nextInt(mDefinedMessages.length);
            String msg = mDefinedMessages[i];
            out.println(msg);
            System.out.println("send:" + msg);
        }
        System.out.println("client quit.");
        //关闭流
        MyUtils.close(out);
        MyUtils.close(in);
        client.close();
    }
}


































