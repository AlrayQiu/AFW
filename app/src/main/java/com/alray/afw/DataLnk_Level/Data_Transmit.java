package com.alray.afw.DataLnk_Level;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Data_Transmit extends Thread {
    private static final String TAG = "ServerThread";
    private static Context Context;
    boolean isLoop = true;

    Handler mhandler;

    public Data_Transmit(Context context, Handler handler)
    {
        Context = context;
        mhandler = handler;
    }

    public void setIsLoop(boolean isLoop)
    {
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
            serverSocket = new ServerSocket(9999);
            while (isLoop) {
                final Socket socket = serverSocket.accept();
                new Thread(new SocketReadThread(socket,mhandler)).start();
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {

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
        Handler mhandler;
        Message msg = new Message();

        public SocketReadThread(Socket mSocket,Handler handler) throws IOException {

            this.in = new BufferedInputStream(mSocket.getInputStream());
            outputStream = new BufferedOutputStream(mSocket.getOutputStream());
            this.mSocket = mSocket;
            mhandler = handler;
        }
        @Override
        public void run( ) {
            try {
                String readMsg = "yyyy";
                long  currCMD = 0;
                while (true) {
                    try {
                        if (!mSocket.isConnected()) {

                            msg = mhandler.obtainMessage();
                            msg.what = 0;
                            msg.obj ="disconnect";

                            mhandler.sendMessage(msg);
                            break;
                        }
                        //   读到后台发送的消息  然后去处理
                        currCMD = readMsgFromSocket(in);
                        //    处理读到的消息(主要是身份证信息),然后保存在sp中;
                        if (currCMD == 0) {
                            continue;
                        }

                        msg = new Message();
                        msg.what = 0;
                        msg.obj = (Object)(currCMD);
                        mhandler.sendMessage(msg);

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

    int MAX_BUFFER_BYTES = 8;
    byte[] buffer = new byte[MAX_BUFFER_BYTES];
    public long  readMsgFromSocket(InputStream in) {
        long tmsg = 0;
        try {
            if(in.read(buffer, 0, buffer.length) == 0)
                return 0;
            for(int i = 7;i >=0;i--)
            {
                tmsg <<= 8;
                tmsg |= (buffer[i] & 0xff);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        return tmsg ;
    }

}