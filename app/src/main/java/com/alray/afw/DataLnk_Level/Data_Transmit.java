package com.alray.afw.DataLnk_Level;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Data_Transmit extends Thread {
    private static final String TAG = "ServerThread";
    private static Context Context;
    boolean isLoop = true;

    public Data_Transmit(Context context)
    {
        Context = context;
    }

    public void setIsLoop(boolean isLoop) {
        this.isLoop = isLoop;
    }

    public void SetContext(Context context)
    {
       Context = context;
    }

    @Override
    public void run( ) {

        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(9000);

            while (isLoop) {
                final Socket socket = serverSocket.accept();
                new Thread(new SocketReadThread(socket)).start();
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            Log.d("anzi", "destory");

            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static void sendToast(String msg) throws IOException {
        Socket socket = new Socket("127.0.0.1", 45425);
        DataInputStream dis = new DataInputStream(socket.getInputStream());
    }

    class SocketReadThread implements Runnable {

        private BufferedInputStream in;
        private BufferedOutputStream outputStream;
        Socket mSocket;

        public SocketReadThread(Socket mSocket) throws IOException {

            this.in = new BufferedInputStream(mSocket.getInputStream());
            outputStream = new BufferedOutputStream(mSocket.getOutputStream());
            this.mSocket = mSocket;
        }

        public void run( ) {
            try {
                String readMsg = "yyyy";
                int currCMD = 0;
                while (true) {
                    try {
                        if (!mSocket.isConnected()) {
                            break;
                        }

                        //   读到后台发送的消息  然后去处理
                        currCMD = readMsgFromSocket(in);
                        //    处理读到的消息(主要是身份证信息),然后保存在sp中;
                        if (currCMD == 0) {
                            break;
                        }
                        Toast.makeText(Context, String.format("{0}",currCMD), Toast.LENGTH_SHORT).show();


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
            }
        }
    }

    int MAX_BUFFER_BYTES = 4;
    public int readMsgFromSocket(InputStream in) {
        byte[] tempbuffer = new byte[MAX_BUFFER_BYTES];
        int msg = 0;
        try {
            if(in.read(tempbuffer, 0, tempbuffer.length) == 0)
                return 0;
            msg = tempbuffer[3] << 24 | tempbuffer[2] << 16 | tempbuffer[1] << 8 | tempbuffer[0];

        } catch (Exception e) {
            e.printStackTrace();
        }
        return msg ;
    }

}